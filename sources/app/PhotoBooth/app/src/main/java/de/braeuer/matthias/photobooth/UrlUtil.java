package de.braeuer.matthias.photobooth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matze on 21.06.2016.
 */
public class UrlUtil {

    //From http://blog.hackerkernel.com/2015/11/30/android-upload-image-to-server/ visited 26.06.2016

    public static String hashMapToUrl(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    /*public static String toUrl(String base64, String email) throws UnsupportedEncodingException {
        String result = "";

        result += URLEncoder.encode("email", "UTF-8");
        result += "=";
        result += URLEncoder.encode(email, "UTF-8");

        result += "&";

        result += "image=" + base64;

        return result;
    }*/
}
