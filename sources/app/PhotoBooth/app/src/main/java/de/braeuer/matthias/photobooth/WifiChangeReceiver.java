package de.braeuer.matthias.photobooth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Matze on 21.06.2016.
 */
public class WifiChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, final Intent intent) {
        if(Connection.isWifiConnection(context)){
            Toast.makeText(context, "RECEIVER CONNECTED", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "RECEIVER DISCONNECTED", Toast.LENGTH_LONG).show();
        }
    }
}
