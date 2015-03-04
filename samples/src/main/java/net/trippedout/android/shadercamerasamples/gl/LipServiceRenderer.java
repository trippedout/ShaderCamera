package net.trippedout.android.shadercamerasamples.gl;

import android.content.Context;
import android.graphics.SurfaceTexture;

import net.trippedout.android.shadercamera.gl.CameraRenderer;
import net.trippedout.android.shadercamerasamples.R;

/**
 * lip service!
 *
 * Read the comments in {@link net.trippedout.android.shadercamera.gl.SimpleCameraRenderer} and {@link net.trippedout.android.shadercamera.gl.CameraRenderer}
 * to learn more about how we implement this file and its usage.
 *
 * This file adds a texture of my stupid face with the mouth cut out, and draws it on top of the forward
 * facing camera feed, for a throwback Conan O'Brien and new age Jimmy Fallon experience.
 */
public class LipServiceRenderer extends CameraRenderer
{
    private static final String TAG = LipServiceRenderer.class.getSimpleName();

    public LipServiceRenderer(Context context, SurfaceTexture texture, int width, int height)
    {
        //use new vert and frag shaders
        super(context, texture, width, height, "lip_service.frag", "lip_service.vert");
    }

    @Override
    protected void onSetupComplete() {
        super.onSetupComplete();

        //remember when you add a texture to make sure that u have the proper
        //sampler2D setup in your fragment shader with the same id!
        addTexture(R.drawable.mouth, "mouth");
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
