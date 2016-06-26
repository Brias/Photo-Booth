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

    //From: http://sajithforu.blogspot.de/2014/09/encode-large-size-image-to-base64.html, visited: 26.06.2016
    public static String toBase64(Bitmap bitmap){

        ByteArrayOutputStream baos=new  ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);

        byte [] b=baos.toByteArray();

        String temp=null;

        try{

            System.gc();

            temp= Base64.encodeToString(b, Base64.DEFAULT);

        }catch(Exception e){

            e.printStackTrace();

        }catch(OutOfMemoryError e){

            baos=new  ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG,50, baos);
            b=baos.toByteArray();

            temp=Base64.encodeToString(b, Base64.DEFAULT);

            Log.e("EWN", "Out of memory error catched");

        }

        return temp;

    }

}
