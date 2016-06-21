package de.braeuer.matthias.photobooth;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Matze on 21.06.2016.
 */
public class AccessStorage {
    public static String saveImageToInternalStorage(Context context, Bitmap bm){
        ContextWrapper cw = new ContextWrapper(context);

        File dir = cw.getDir("imageDir", Context.MODE_PRIVATE);

        File path = new File(dir, "df.jpg");

        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(path);

            bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return dir.getAbsolutePath();
    }

    private static Bitmap getImageFromInternalStorage(String path)
    {
        try {
            File f = new File(path, "profile.jpg");
            Bitmap bm = BitmapFactory.decodeStream(new FileInputStream(f));
            return bm;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }

    }
}
