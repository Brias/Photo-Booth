package de.braeuer.matthias.photobooth;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by Matze on 21.06.2016.
 */
public class Image {
    private String name;
    private String email;
    private Bitmap bm;

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

    public Bitmap getBitmap(){
        return this.bm;
    }

    public void setBitmap(Bitmap bm){
        this.bm = bm;
    }

    public String toString(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }
}
