package de.braeuer.matthias.photobooth;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * Created by Matze on 21.06.2016.
 */
public class Image {
    private String name;
    private String email;

    public String getEmail(){
        return this.email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public static String toBase64(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }
}
