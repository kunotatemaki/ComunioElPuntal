package com.rukiasoft.androidapps.comunioelpuntal.wifi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiHandler {

    private static final String TAG = "WifiHandler";
    //private static boolean isConectedToSacarinoWifi = false;


    public static boolean IsWifiEnabled(final Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

    public static void SetWifiEnabled(final Context context, boolean state) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        Log.d(TAG, "pongo wifi: " + state);
        wifiManager.setWifiEnabled(state);
    }

    public static boolean IsWifiConnected(final Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi =
                connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifi.isAvailable() && wifi.getDetailedState() == DetailedState.CONNECTED;
    }

    public static boolean IsMobileConnected(final Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo mobile =
                connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return mobile.isAvailable() && mobile.getDetailedState() == DetailedState.CONNECTED;
    }

    public static String GetSSIDWifiConnected(final Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        android.net.wifi.WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getSSID();
    }

}


