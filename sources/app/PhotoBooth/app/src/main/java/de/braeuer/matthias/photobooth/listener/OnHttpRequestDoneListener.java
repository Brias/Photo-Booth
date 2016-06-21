package de.braeuer.matthias.photobooth.listener;

import de.braeuer.matthias.photobooth.Image;

/**
 * Created by Matze on 13.06.2016.
 */
public interface OnHttpRequestDoneListener {
    void onHttpRequestError(Image image, String errorMsg);
    void onHttpRequestSuccess();
}
