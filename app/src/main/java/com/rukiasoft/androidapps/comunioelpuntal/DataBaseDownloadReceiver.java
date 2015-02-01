package com.rukiasoft.androidapps.comunioelpuntal;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class DataBaseDownloadReceiver extends WakefulBroadcastReceiver {

    private final String TAG = "DataBaseDownloadReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(TAG, "notificacion recibida de base de datos descargada");
        Bundle extras = intent.getExtras();
        if (extras == null) {
            Log.d(TAG, "no han llegado extras");
            return;
        }
        String type = extras.getString("type");
        if (type.compareTo(MainActivity.class.getSimpleName()) == 0) {
            if (MainActivity.getDatabaseDownloading() == false) {
                ComponentName comp =
                        new ComponentName(context.getPackageName(),
                                LoadDatabaseService.class.getName());
                startWakefulService(context, (intent.setComponent(comp)));
            }
        } else if (type.compareTo(StartScreenActivity.class.getSimpleName()) == 0) {
            Log.d(TAG, "estoy en startScreen");
            if(!MainActivity.isGamerSelected()) { //no existe player, lo elijo
                intent.putExtra("type", SelectGamerActivity.class.getSimpleName());
                ComponentName comp =
                        new ComponentName(context.getPackageName(),
                                LoadDatabaseService.class.getName());
                startWakefulService(context, (intent.setComponent(comp)));
            }else {
                ComponentName comp =
                        new ComponentName(context.getPackageName(),
                                LoadDatabaseService.class.getName());
                startWakefulService(context, (intent.setComponent(comp)));
            }
        }
    }
}
