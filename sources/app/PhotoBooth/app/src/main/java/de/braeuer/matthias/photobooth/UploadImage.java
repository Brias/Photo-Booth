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

    private Image image;
    private final String serverURL;
    private ProgressDialog pd;
    private OnHttpRequestDoneListener httpListener;

    public UploadImage(OnHttpRequestDoneListener httpListener, ProgressDialog pd, Image image, String serverURL) {
        this.image = image;
        this.serverURL = serverURL;
        this.pd = pd;
        this.httpListener = httpListener;
    }

    @Override
    protected void onPreExecute() {
        pd.show();
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            String dataToSend = UrlUtil.toUrl(Image.toBase64(BitmapHolder.bm), image.getEmail());
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
                httpListener.onHttpRequestError(s);
            }
        }

        pd.dismiss();
    }
}
