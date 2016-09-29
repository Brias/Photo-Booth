package de.braeuer.matthias.photobooth;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * This class represents a image and its data
 *
 * LICENSE: This file is subject of the GNU GENERAL PUBLIC LICENSE Version 3, 29 June 2007
 *
 * @author Matthias Bräuer
 * @version $Id: Image.java,v 1.0 2016/09/29 17:07:00 Exp $
 */
public class Image {
    private String name;
    private String email;

    public static String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
