package net.trippedout.android.shadercamera.gl;


import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import net.trippedout.android.shadercamera.utils.AndroidUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Base camera rendering class. Extend this and override the fragment and vertex shaders, as well
 * as the methods in {@link #draw()} which set up uniforms, draw the elements, and clean up, if need
 * be. Check out {@link SimpleCameraRenderer} for simple usage of this.
 *
 * Original code by https://www.virag.si/
 * Updated/refactored by Anthony Tripaldi
 */

public class CameraRenderer extends TextureSurfaceRenderer implements SurfaceTexture.OnFrameAvailableListener
{
    private static final String TAG = CameraRenderer.class.getSimpleName();


    public static final String DEFAULT_FRAGMENT_SHADER = "camera.frag.glsl";
    public static final String DEFAULT_VERTEX_SHADER = "camera.vert.glsl";

    protected String vertexShaderCode;
    protected String fragmentShaderCode;

    private static float squareSize = 1.0f;
    private static float squareCoords[] = {-squareSize, squareSize, 0.0f,   // top left
            -squareSize, -squareSize, 0.0f,   // bottom left
            squareSize, -squareSize, 0.0f,   // bottom right
            squareSize, squareSize, 0.0f}; // top right

    private static short drawOrder[] = {0, 1, 2, 0, 2, 3};

    private Context ctx;

    // Texture to be shown in backgrund
    private FloatBuffer textureBuffer;
    private float textureCoords[] = {0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f};
    private int[] textures = new int[1];

    protected int shaderProgram;
    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;

    private SurfaceTexture videoTexture;
    private float[] videoTextureTransform = new float[16];

    protected boolean frameAvailable = false;

    private int videoWidth;
    private int videoHeight;
    private boolean adjustViewport = false;

    private int textureCoordinateHandle;
    private int positionHandle;

    //aspect ratio
    private float mAspectRatio = 1.0f;


    public CameraRenderer(Context context, SurfaceTexture texture, int width, int height)
    {
        super(texture, width, height);
        this.ctx = context;

        if(fragmentShaderCode == null || vertexShaderCode == null)
            loadFromShadersFromAssets(DEFAULT_FRAGMENT_SHADER, DEFAULT_VERTEX_SHADER);
    }

    public CameraRenderer(Context context, SurfaceTexture texture, int width, int height, String fragPath, String vertPath) {
        super(texture, width, height);
        this.ctx = context;

        if(fragmentShaderCode == null || vertexShaderCode == null)
            loadFromShadersFromAssets(fragPath, vertPath);
    }

    private void loadFromShadersFromAssets(String pathToFragment, String pathToVertex)
    {
        try {
            fragmentShaderCode = AndroidUtils.getStringFromFileInAssets(ctx, pathToFragment);
            vertexShaderCode = AndroidUtils.getStringFromFileInAssets(ctx, pathToVertex);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setupShaders()
    {
        int vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vertexShaderHandle, vertexShaderCode);
        GLES20.glCompileShader(vertexShaderHandle);
        checkGlError("Vertex shader compile");

        Log.d(TAG, "vertex shader info log:\n " + GLES20.glGetShaderInfoLog(vertexShaderHandle) );

        int fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragmentShaderHandle, fragmentShaderCode);
        GLES20.glCompileShader(fragmentShaderHandle);
        checkGlError("Pixel shader compile");

        Log.d(TAG, "fragment shader info log:\n " + GLES20.glGetShaderInfoLog(fragmentShaderHandle) );

        shaderProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(shaderProgram, vertexShaderHandle);
        GLES20.glAttachShader(shaderProgram, fragmentShaderHandle);
        GLES20.glLinkProgram(shaderProgram);
        checkGlError("Shader program compile");

        int[] status = new int[1];
        GLES20.glGetProgramiv(shaderProgram, GLES20.GL_LINK_STATUS, status, 0);
        if (status[0] != GLES20.GL_TRUE) {
            String error = GLES20.glGetProgramInfoLog(shaderProgram);
            Log.e("SurfaceTest", "Error while linking program:\n" + error);
        }
    }


    private void setupVertexBuffer() {
        // Draw list buffer
        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        // Initialize the texture holder
        ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());

        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);
    }


    private void setupTexture(Context context) {
        ByteBuffer texturebb = ByteBuffer.allocateDirect(textureCoords.length * 4);
        texturebb.order(ByteOrder.nativeOrder());

        textureBuffer = texturebb.asFloatBuffer();
        textureBuffer.put(textureCoords);
        textureBuffer.position(0);

        // Generate the actual texture
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glGenTextures(1, textures, 0);
        checkGlError("Texture generate");

        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textures[0]);
        checkGlError("Texture bind");

        videoTexture = new SurfaceTexture(textures[0]);
        videoTexture.setOnFrameAvailableListener(this);
    }



    @Override
    protected boolean draw() {
        synchronized (this) {
            if (frameAvailable) {
                videoTexture.updateTexImage();
                videoTexture.getTransformMatrix(videoTextureTransform);
                frameAvailable = false;
            } else {
                return false;
            }
        }

        if (adjustViewport)
            adjustViewport();

        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        //set shader
        GLES20.glUseProgram(shaderProgram);

        setUniformsAndAttribs();
        drawElements();
        onDrawCleanup();

        return true;
    }

    /**
     * base amount of attributes needed for rendering camera to screen
     */
    protected void setUniformsAndAttribs()
    {
        int textureParamHandle = GLES20.glGetUniformLocation(shaderProgram, "texture");
        int textureTranformHandle = GLES20.glGetUniformLocation(shaderProgram, "textureTransform");
        int aspectRatioHandle = GLES20.glGetUniformLocation(shaderProgram, "aspectRatio");
        textureCoordinateHandle = GLES20.glGetAttribLocation(shaderProgram, "vTexCoordinate");
        positionHandle = GLES20.glGetAttribLocation(shaderProgram, "vPosition");

        GLES20.glUniform1f(aspectRatioHandle, mAspectRatio);

        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 4 * 3, vertexBuffer);

        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textures[0]);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glUniform1i(textureParamHandle, 0);

        GLES20.glEnableVertexAttribArray(textureCoordinateHandle);
        GLES20.glVertexAttribPointer(textureCoordinateHandle, 4, GLES20.GL_FLOAT, false, 0, textureBuffer);

        GLES20.glUniformMatrix4fv(textureTranformHandle, 1, false, videoTextureTransform, 0);
    }

    private void drawElements()
    {
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
    }

    private void onDrawCleanup()
    {
        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(textureCoordinateHandle);
    }

    private void adjustViewport() {
        float surfaceAspect = height / (float) width;
        float videoAspect = videoHeight / (float) videoWidth;

        Log.d(TAG, "adjustViewport() " + surfaceAspect + ", " + videoAspect);

        if (surfaceAspect > videoAspect) {
            float heightRatio = height / (float) videoHeight;
            int newWidth = (int) (width * heightRatio);
            int xOffset = (newWidth - width) / 2;
            GLES20.glViewport(-xOffset, 0, newWidth, height);
        } else {
            float widthRatio = width / (float) videoWidth;
            int newHeight = (int) (height * widthRatio);
            int yOffset = (newHeight - height) / 2;
            GLES20.glViewport(0, -yOffset, width, newHeight);
        }

        adjustViewport = false;
    }

    @Override
    protected void initGLComponents() {
        setupVertexBuffer();
        setupTexture(ctx);
        setupShaders();
    }

    @Override
    protected void deinitGLComponents() {
        GLES20.glDeleteTextures(1, textures, 0);
        GLES20.glDeleteProgram(shaderProgram);
        videoTexture.release();
        videoTexture.setOnFrameAvailableListener(null);
    }

    public void setVideoSize(int width, int height) {
        this.videoWidth = width;
        this.videoHeight = height;
        adjustViewport = true;
    }

    public void checkGlError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("SurfaceTest", op + ": glError " + GLUtils.getEGLErrorString(error));
        }
    }

    public SurfaceTexture getVideoTexture() {
        return videoTexture;
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        synchronized (this) {
            frameAvailable = true;
        }
    }


    public void setAspectRatio(float aspectRatio) {
        mAspectRatio = aspectRatio;
        Log.d(TAG, "setAspectRatio() " + mAspectRatio);
    }

}
