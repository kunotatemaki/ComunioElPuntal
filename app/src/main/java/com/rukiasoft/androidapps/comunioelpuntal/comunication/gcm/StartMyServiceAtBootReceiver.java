package com.rukiasoft.androidapps.comunioelpuntal.comunication.gcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartMyServiceAtBootReceiver extends BroadcastReceiver {
    static String TAG = "StartMyServiceAtBootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent serviceIntent = new Intent(context, RegisterService.class);
            context.startService(serviceIntent);
        }
    }
}
