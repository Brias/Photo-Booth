package de.braeuer.matthias.photobooth;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

/**
 * Created by Matze on 21.06.2016.
 */
public class Connection {

    public static boolean isWifiConnection(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if(info != null){
            if(info.getType() == ConnectivityManager.TYPE_WIFI){
                return true;
            }
        }

        return false;
    }

    public static boolean disconnectFromWifi(Context context){
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        return wm.disconnect();
    }

    public static boolean connectToWifi(Context context){
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        return wm.reconnect();
    }
}
