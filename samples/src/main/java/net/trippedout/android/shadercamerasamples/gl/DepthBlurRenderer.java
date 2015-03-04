package net.trippedout.android.shadercamerasamples.gl;

import android.content.Context;
import android.graphics.SurfaceTexture;

import net.trippedout.android.shadercamera.gl.CameraRenderer;

/**
 * DEPTH BLURRRR
 */
public class DepthBlurRenderer extends CameraRenderer
{
    public DepthBlurRenderer(Context context, SurfaceTexture texture, int width, int height) {
        super(context, texture, width, height, "depth_blur.frag", "depth_blur.vert");
    }
}
