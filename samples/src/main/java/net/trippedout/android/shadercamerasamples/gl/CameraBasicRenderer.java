package net.trippedout.android.shadercamerasamples.gl;

import android.content.Context;
import android.graphics.SurfaceTexture;

import net.trippedout.android.shadercamera.gl.CameraRenderer;

/**
 * Absolutely no shaders other than the camera feed.
 */
public class CameraBasicRenderer extends CameraRenderer
{
    /**
     * The basic constructor. If used, we will auto-load files from the /assets/ folder, defined
     * as {@link #DEFAULT_FRAGMENT_SHADER} and {@link #DEFAULT_VERTEX_SHADER}.
     *
     * Here you can instead choose to pass in file names to other shaders, as demonstrated in
     * {@link LipServiceRenderer} and it will load those instead.
     */
    public CameraBasicRenderer(Context context, SurfaceTexture texture, int width, int height) {
        super(context, texture, width, height);
        //super(context, texture, width, height, "your_shader.frag", "your_shader.vert"
    }
}
