package com.rukiasoft.androidapps.comunioelpuntal.wifi;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.rukiasoft.androidapps.comunioelpuntal.comunication.gcm.AppDownloaderIntentService;
import com.rukiasoft.androidapps.comunioelpuntal.utils.ActivityTool;
import com.rukiasoft.androidapps.comunioelpuntal.utils.ComunioConstants;

public class WifiReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String TAG = "WifiReceiver";
        Log.d(TAG, "onReceive");
        final String action = intent.getAction();
        if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            Log.d(TAG, "conectado a Wifi: " + WifiHandler.IsWifiConnected(context));
            if (WifiHandler.IsWifiConnected(context)) {
                Boolean downloadWithWifi = ActivityTool.getBooleanFromPreferences(context, "option_update_wifi");
                if (!downloadWithWifi) {
                    Log.d(TAG, "no hay que hacer nada porque ya se habrá descargado en su momento");
                    return;
                }
                if (ActivityTool.getBooleanFromPreferences(context, ComunioConstants.PROPERTY_DOWNLOAD_UPDATED_APP)) {
                    Log.d(TAG, "wifi conectada, voy a descargar aplicacion");
                    ComponentName comp = new ComponentName(context.getPackageName(),
                            AppDownloaderIntentService.class.getName());
                    startWakefulService(context, (intent.setComponent(comp)));
                }
            }
        }
    }
}
