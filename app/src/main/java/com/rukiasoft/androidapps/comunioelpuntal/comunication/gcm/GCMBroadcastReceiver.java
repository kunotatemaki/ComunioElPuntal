package com.rukiasoft.androidapps.comunioelpuntal.comunication.gcm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.rukiasoft.androidapps.comunioelpuntal.utils.ActivityTool;
import com.rukiasoft.androidapps.comunioelpuntal.utils.ComunioConstants;
import com.rukiasoft.androidapps.comunioelpuntal.wifi.WifiHandler;


public class GCMBroadcastReceiver extends WakefulBroadcastReceiver {

    private static final String TAG = "GCMBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        Log.d(TAG, "entro");

        if (extras.isEmpty())
            return;
        String type = extras.getString("type");
        if (type == null)
            return;
        Log.d(TAG, "type = " + type);
        if (type.compareTo(ComunioConstants.MENSAJE_CAPORAL) == 0) {
            Log.d(TAG, "notificacion caporal");
            ComponentName comp = new ComponentName(context.getPackageName(),
                    GCMIntentService.class.getName());
            startWakefulService(context, (intent.setComponent(comp)));
        } else if (type.compareTo(ComunioConstants.MENSAJE_RULER) == 0) {
            Log.d(TAG, "notification ruler: download true, path = " + extras.getString("url_app"));
            ActivityTool.savePreferences(context, ComunioConstants.PROPERTY_DOWNLOAD_UPDATED_APP, true);
            ActivityTool.savePreferences(context, ComunioConstants.PROPERTY_INSTALL_UPDATED_APP, false);
            ActivityTool.savePreferences(context, ComunioConstants.PROPERTY_PATH_DOWNLOAD_APP, extras.getString("url_app"));
            Integer versionForDownload;
            try {
                versionForDownload = Integer.parseInt(intent.getStringExtra("version"));
            } catch (NumberFormatException e) {
                versionForDownload = 0;
            }
            ActivityTool.savePreferences(context, ComunioConstants.PROPERTY_VERSION_APP_FOR_DOWNLOADING, versionForDownload);

            Boolean downloadWithWifi = ActivityTool.getBooleanFromPreferences(context, "option_update_wifi");
            if (downloadWithWifi && !WifiHandler.IsWifiConnected(context)) {
                Log.d(TAG, "hay que bajar con wifi y no está conectado");

            } else {
                ComponentName comp = new ComponentName(context.getPackageName(),
                        AppDownloaderIntentService.class.getName());
                startWakefulService(context, (intent.setComponent(comp)));
            }
        }

        setResultCode(Activity.RESULT_OK);
    }
}


