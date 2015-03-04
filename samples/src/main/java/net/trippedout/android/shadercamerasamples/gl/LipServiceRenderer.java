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
    public LipServiceRenderer(Context context, SurfaceTexture texture, int width, int height) {
        super(context, texture, width, height);
    }
}
