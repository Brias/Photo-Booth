package de.braeuer.matthias.photobooth;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * This class saves/deletes/gets Images from local Storage
 *
 * LICENSE: This file is subject of the GNU GENERAL PUBLIC LICENSE Version 3, 29 June 2007
 *
 * @author Matthias Bräuer
 * @version $Id: AccessStorage.java,v 1.0 2016/09/29 16:56:00 Exp $
 */
public class AccessStorage {
    public static String saveImageToInternalStorage(Context context, Bitmap bm){
        String filename = System.currentTimeMillis() + ".jpg";

        FileOutputStream fos;

        try {
            fos = context.openFileOutput(filename, Context.MODE_PRIVATE);

            bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            fos.close();

            return filename;
        } catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public static Bitmap getImageFromInternalStorage(Context context, String name)
    {
        Bitmap bm = null;

        try {
            FileInputStream fis = context.openFileInput(name);
            bm = BitmapFactory.decodeStream(fis);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bm;
    }

    public static boolean deleteImageFromInternalStorage(Context context, String name){
        return context.deleteFile(name);
    }
}
