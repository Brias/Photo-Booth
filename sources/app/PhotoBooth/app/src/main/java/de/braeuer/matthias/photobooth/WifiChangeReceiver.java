package de.braeuer.matthias.photobooth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Matze on 21.06.2016.
 */
public class WifiChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Connection.isWifiConnection(context)) {
            startUploadLocalImageService(context);
        } else {
            stopUploadLocalImageService(context);
        }
    }

    private void startUploadLocalImageService(Context context) {
        Intent intent = new Intent(context, UploadLocalImagesService.class);
        context.startService(intent);
    }

    private void stopUploadLocalImageService(Context context) {
        Intent intent = new Intent(context, UploadLocalImagesService.class);
        context.stopService(intent);
    }
}
