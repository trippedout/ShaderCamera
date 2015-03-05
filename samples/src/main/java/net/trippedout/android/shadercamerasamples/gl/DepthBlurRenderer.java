package net.trippedout.android.shadercamerasamples.gl;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;

import net.trippedout.android.shadercamera.gl.CameraRenderer;
import net.trippedout.android.shadercamerasamples.R;

/**
 * DEPTH BLURRRR with COLORRRR
 */
public class DepthBlurRenderer extends CameraRenderer
{
    private float mBlurSize = 10.0f;
    private float[] mBlurCenter = new float[2];

    public DepthBlurRenderer(Context context, SurfaceTexture texture, int width, int height)
    {
        super(context, texture, width, height, "depth_blur.frag", "depth_blur.vert");

        mBlurCenter[0] = .5f;
        mBlurCenter[1] = .5f;
    }

    @Override
    protected void onSetupComplete() {
        super.onSetupComplete();

        addTexture(R.drawable.color, "color");
    }

    @Override
    protected void setUniformsAndAttribs() {
        super.setUniformsAndAttribs();

        int blurSizeLocation = GLES20.glGetUniformLocation(shaderProgram, "blurSize");
        int blurCenterLocation = GLES20.glGetUniformLocation(shaderProgram, "blurCenter");

        GLES20.glUniform1f(blurSizeLocation, mBlurSize);
        GLES20.glUniform2f(blurCenterLocation, mBlurCenter[0], mBlurCenter[1]);
    }

    public void setBlurSize(float size)
    {
        this.mBlurSize = size;
    }
}
