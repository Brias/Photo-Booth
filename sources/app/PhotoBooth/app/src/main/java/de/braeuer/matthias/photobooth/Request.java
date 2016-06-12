package de.braeuer.matthias.photobooth;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Matze on 12.06.2016.
 */

//From http://blog.hackerkernel.com/2015/11/30/android-upload-image-to-server/
public class Request {

    private static final int CONNECT_TIMEOUT = 1000 * 30;
    private static final int READ_TIMEOUT = 1000 * 30;

    public static String post(String serverUrl, String dataToSend) {
        try {
            URL url = new URL(serverUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setConnectTimeout(CONNECT_TIMEOUT);
            con.setReadTimeout(READ_TIMEOUT);

            con.setRequestMethod("POST");
            con.setDoInput(true);

            OutputStream os = con.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            writer.write(dataToSend);
            writer.flush();
            writer.close();
            os.close();

            int responseCode = con.getResponseCode();

            Log.d("UPLOADIMAGE", ""+responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                StringBuilder sb = new StringBuilder();

                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;

                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }

                return sb.toString();
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
