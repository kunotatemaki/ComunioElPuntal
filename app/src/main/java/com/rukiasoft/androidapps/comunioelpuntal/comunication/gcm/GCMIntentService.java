package com.rukiasoft.androidapps.comunioelpuntal.comunication.gcm;


import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.rukiasoft.androidapps.comunioelpuntal.dataclasses.DatabaseHandler;
import com.rukiasoft.androidapps.comunioelpuntal.utils.ComunioConstants;

public class GCMIntentService extends IntentService {

    private static final String TAG = "GCMIntentService";
    //DataBase


    public GCMIntentService() {
        super("GCMIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);
        Bundle extras = intent.getExtras();
        Intent second_intent = new Intent(ComunioConstants.NOTIFICATION_ACTION_INTENT);
        second_intent = second_intent.putExtras(extras);
        //Log.d(TAG, "Mando el intent GCM: " + messageType);
        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                //Lo almacenamos en la base de datos y mando el intent
                try {
                    DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
                    long resultado = dbHandler.insertNotification(extras);
                    if (resultado != -1) {
                        Log.d(TAG, "ha grabado bien en la base de datos");
                        sendOrderedBroadcast(second_intent, null);
                    } else
                        Log.d(TAG, "no ha grabado bien en la base de datos");
                } catch (Exception e) {
                    Log.d(TAG, "No se pudo almacenar en la base de datos, no lanzo notif");
                    GCMBroadcastReceiver.completeWakefulIntent(intent);
                    return;
                }
                //Log.d(TAG,"ha llegado algo de tipo: " + second_intent.getExtras().getString("type") );

            }
        }
        GCMBroadcastReceiver.completeWakefulIntent(intent);
    }

}

