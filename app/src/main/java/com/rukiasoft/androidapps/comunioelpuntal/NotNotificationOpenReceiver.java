package com.rukiasoft.androidapps.comunioelpuntal;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;


public class NotNotificationOpenReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String TAG = "NotNotificationOpenReceiver";
        Log.i(TAG, "notificacion recibida en segundo plano");
        ComponentName comp =
                new ComponentName(context.getPackageName(),
                        NotNotificationOpenService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));

        setResultCode(Activity.RESULT_OK);


    }

}


