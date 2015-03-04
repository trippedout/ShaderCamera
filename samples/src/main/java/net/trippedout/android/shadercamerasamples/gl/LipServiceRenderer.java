package net.trippedout.android.shadercamerasamples.gl;

import android.content.Context;
import android.graphics.SurfaceTexture;

import net.trippedout.android.shadercamera.gl.CameraRenderer;

/**
 * lip service!
 *
 * Read the comments in {@link net.trippedout.android.shadercamera.gl.SimpleCameraRenderer} and {@link net.trippedout.android.shadercamera.gl.CameraRenderer}
 * to learn more about how we implement this file and its usage
 */
public class LipServiceRenderer extends CameraRenderer
{
    private static final String TAG = LipServiceRenderer.class.getSimpleName();

    public LipServiceRenderer(Context context, SurfaceTexture texture, int width, int height) {
        super(context, texture, width, height);
    }


    /**
     * override here to set up all your extra uniforms and attributes beyond
     * the base 4 that are required for rendering the camera
     */
    @Override
    protected void setUniformsAndAttribs()
    {
        super.setUniformsAndAttribs();
    }
}
