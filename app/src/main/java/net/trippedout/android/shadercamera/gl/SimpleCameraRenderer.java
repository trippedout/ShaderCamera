package net.trippedout.android.shadercamera.gl;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Simple implementation of bullshit behind camera rendering
 *
 * If you want to write your vertex and fragment shaders in this file,
 * just uncomment and write whatever you want in the {@link CameraRenderer#fragmentShaderCode}
 * and {@link CameraRenderer#vertexShaderCode} fields below.
 *
 * Alternatively, the two shaders provided in the assets/ folder will be used automatically if
 * the fields are empty, so editing those could work as well.
 */
public class SimpleCameraRenderer extends CameraRenderer
{
    private final static String TAG = SimpleCameraRenderer.class.getSimpleName();

    /**
     * uncomment and use these for in-class shader writing, or edit files
     * in the assets folder which will be loaded automatically if below is null
     */
//    protected String fragmentShaderCode = "write code here";
//    protected String vertexShaderCode = "write code here";

    //variables to update and animate for use in shader
    private static final float CIRCLE_STARTING_SIZE = 0.35f;
    private float mCircleSize = CIRCLE_STARTING_SIZE;
    private float mPercentColor = 0.0f;

    public SimpleCameraRenderer(Context context, SurfaceTexture texture, int width, int height)
    {
        super(context, texture, width, height);

        //other setup if need be done here
    }


    /**
     * call to animate the circle open and also update the color tint
     */
    public void animateCircleOpen()
    {
        ValueAnimator animator = ValueAnimator.ofFloat(CIRCLE_STARTING_SIZE, 0.750f);
        animator.setDuration(400);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPercentColor = animation.getAnimatedFraction();
                mCircleSize = (float)animation.getAnimatedValue();
                updateFrame();
            }
        });
        animator.start();
    }

    public void animateCircleClosed()
    {
        ValueAnimator animator = ValueAnimator.ofFloat(0.750f, CIRCLE_STARTING_SIZE);
        animator.setDuration(400);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPercentColor = 1.0f - animation.getAnimatedFraction();
                mCircleSize = (float)animation.getAnimatedValue();
                updateFrame();
            }
        });
        animator.start();
    }


    /**
     * override here to set up all your extra uniforms and attributes beyond
     * the base 4 that are required for rendering the camera
     */
    @Override
    protected void setUniformsAndAttribs()
    {
        super.setUniformsAndAttribs();

        int percentageHandle = GLES20.glGetUniformLocation(shaderProgram, "percentage");
        int circleSize = GLES20.glGetUniformLocation(shaderProgram, "circleSize");

        GLES20.glUniform1f(percentageHandle, mPercentColor);
        GLES20.glUniform1f(circleSize, mCircleSize);
    }

}
