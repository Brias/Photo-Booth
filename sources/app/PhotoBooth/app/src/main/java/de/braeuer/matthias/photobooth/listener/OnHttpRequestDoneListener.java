package de.braeuer.matthias.photobooth.listener;

/**
 * Created by Matze on 13.06.2016.
 */
public interface OnHttpRequestDoneListener {
    void onHttpRequestError(String errorMsg);

    void onHttpRequestSuccess();
}
