package com.rukiasoft.androidapps.comunioelpuntal;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationOpenReceiver extends BroadcastReceiver {

    private static NotificationFragment notifications = null;

    public NotificationOpenReceiver(NotificationFragment fragment) {
        notifications = fragment;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String TAG = "NotificationOpenReceiver";
        Log.i(TAG, "notificacion recibida en activity");

        if (isOrderedBroadcast()) {
            Log.i(TAG, "Calling abortBroadcast()");
            abortBroadcast();
        }

        if (notifications != null) {
            Log.d(TAG, "cargo los datos");
            notifications.loadItems();
        }

    }
}
