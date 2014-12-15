package com.rukiasoft.androidapps.comunioelpuntal;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.rukiasoft.androidapps.comunioelpuntal.comunication.gcm.GCMBroadcastReceiver;
import com.rukiasoft.androidapps.comunioelpuntal.utils.ActivityTool;

//import android.R;


public class NotNotificationOpenService extends IntentService {
    private static final int NOTIF_ALERTA_ID = 1;
    static String TAG = "NotNotificationOpenService";
    private long[] mVibratePattern = {0, 200, 200, 300};
    // Notification Count
    static private int mNotificationCount;

    public NotNotificationOpenService() {
        super("NotNotificationOpenService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (!extras.isEmpty()) {
            mostrarNotification(extras);

        }
        GCMBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void mostrarNotification(Bundle extras) {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String titulo = getApplicationContext().getString(R.string.gcm_notification);

        Intent notIntent = new Intent(this, NotificationActivity.class);

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_dialog_comunio);

        String tono = ActivityTool.getStringFromPreferences(getApplicationContext(), "option_notification");
        Uri uri;
        if (tono.compareTo("") == 0)
            uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        else
            uri = Uri.parse(tono);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setTicker(getApplicationContext().getString(R.string.gcm_notification_ticker))
                        .setSmallIcon(R.drawable.ic_stat_notify_comunio)
                                //.setLargeIcon(Bitmap.createScaledBitmap(bm, 128, 128, false))
                        .setLargeIcon(bm)
                        .setAutoCancel(true)
                        .setContentTitle(titulo)
                                //.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setSound(uri)
                        .setContentText(extras.getString("title_message")).setNumber(++mNotificationCount);

        //vibrador
        if (ActivityTool.hasVibrator(this))
            Log.d(TAG, "Vibrator: " + ActivityTool.getBooleanFromPreferences(getApplicationContext(), "option_vibrate").toString());
        if (ActivityTool.getBooleanFromPreferences(getApplicationContext(), "option_vibrate")) {
            mBuilder.setVibrate(mVibratePattern);
        }


        PendingIntent contIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                notIntent, PendingIntent.FLAG_CANCEL_CURRENT);


        mBuilder.setContentIntent(contIntent);

        mNotificationManager.notify(NOTIF_ALERTA_ID, mBuilder.build());

    }

    public static void restartCounter() {
        mNotificationCount = 0;
    }

}

