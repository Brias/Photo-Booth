package de.braeuer.matthias.photobooth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matze on 21.06.2016.
 */
public class UrlUtil {

    public static String toUrl(String base64, String email) throws UnsupportedEncodingException {
        String result = "";

        result += URLEncoder.encode("email", "UTF-8");
        result += "=";
        result += URLEncoder.encode(email, "UTF-8");

        result += "&";

        result += "image=" + base64;

        return result;
    }
}
