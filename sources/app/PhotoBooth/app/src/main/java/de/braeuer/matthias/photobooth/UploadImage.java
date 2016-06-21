package de.braeuer.matthias.photobooth;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import de.braeuer.matthias.photobooth.listener.OnHttpRequestDoneListener;

/**
 * Created by Matze on 09.06.2016.
 */
public class UploadImage extends AsyncTask<Void, Void, String> {

    private Bitmap bm;
    private final String serverURL;
    private ProgressDialog pd;
    private OnHttpRequestDoneListener httpListener;

    public UploadImage(OnHttpRequestDoneListener httpListener, ProgressDialog pd, Bitmap bm, String serverURL) {
        this.bm = bm;
        this.serverURL = serverURL;
        this.pd = pd;
        this.httpListener = httpListener;
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

        detail.put("image", encodeImage);
        detail.put("email", EmailAddressManager.addressesToString());

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
        if (s != null && s.equals("200")) {
            if (httpListener != null) {
                httpListener.onHttpRequestSuccess();
            }
        } else {
            if (httpListener != null) {
                httpListener.onHttpRequestError(bm, s);
            }
        }

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
