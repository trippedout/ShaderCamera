package net.trippedout.android.shadercamera.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * General i/o helpers and such
 */
public class AndroidUtils
{
    /**
     * Helper for getting strings from any file type in /assets/ folder. Primarily used for shaders.
     *
     * @param ctx Context to use
     * @param filename name of the file, including any folders, inside of the /assets/ folder.
     * @return String of contents of file, lines separated by <code>\n</code>
     * @throws java.io.IOException if file is not found
     */
    public static String getStringFromFileInAssets(Context ctx, String filename) throws IOException {
        return getStringFromFileInAssets(ctx, filename, true);
    }

    public static String getStringFromFileInAssets(Context ctx, String filename, boolean useNewline) throws IOException
    {
        InputStream is = ctx.getAssets().open(filename);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder builder = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null)
        {
            builder.append(line + (useNewline ? "\n" : ""));
        }
        is.close();
        return builder.toString();
    }

}
