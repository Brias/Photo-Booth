package de.braeuer.matthias.photobooth.listener;

import android.graphics.Bitmap;

/**
 * Created by Matze on 13.06.2016.
 */
public interface OnHttpRequestDoneListener {
    void onHttpRequestError(Bitmap bm, String errorMsg);
    void onHttpRequestSuccess();
}
