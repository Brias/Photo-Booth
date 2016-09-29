package de.braeuer.matthias.photobooth;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class sends data to a server
 *
 * LICENSE: This file is subject of the GNU GENERAL PUBLIC LICENSE Version 3, 29 June 2007
 *
 * @author Matthias Bräuer
 * @version $Id: Request.java,v 1.0 2016/09/29 16:56:00 Exp $
 */
//From http://blog.hackerkernel.com/2015/11/30/android-upload-image-to-server/, visited: 26.06.2016
public class Request {

    private static final int CONNECT_TIMEOUT = 1000 * 10;
    private static final int READ_TIMEOUT = 1000 * 10;

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

            if (responseCode == HttpURLConnection.HTTP_OK) {
                StringBuilder sb = new StringBuilder();

                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;

                while ((line = reader.readLine()) != null) {
                    if (sb.length() > 0) {
                        sb.append("\n");
                    }

                    sb.append(line);
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
