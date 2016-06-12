package de.braeuer.matthias.photobooth;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matze on 09.06.2016.
 */
public class UploadImage extends AsyncTask<Void, Void, String> {

    private Bitmap bm;
    private String name;
    private final String serverURL;
    private ProgressDialog pd;

    public UploadImage(ProgressDialog pd, Bitmap bm, String name, String serverURL) {
        this.bm = bm;
        this.name = name;
        this.serverURL = serverURL;
        this.pd = pd;
    }

    @Override
    protected void onPreExecute() {
        pd.show();
    }

    //From http://blog.hackerkernel.com/2015/11/30/android-upload-image-to-server/
    @Override
    protected String doInBackground(Void... params) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        String encodeImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

        HashMap<String, String> detail = new HashMap<>();

        detail.put("name", name);
        detail.put("image", encodeImage);

        try {
            String dataToSend = hashMapToUrl(detail);
            return Request.post(serverURL, dataToSend);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        pd.dismiss();
    }

    //From http://blog.hackerkernel.com/2015/11/30/android-upload-image-to-server/
    private String hashMapToUrl(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
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
}
