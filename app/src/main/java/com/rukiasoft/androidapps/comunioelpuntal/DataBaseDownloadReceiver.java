package com.rukiasoft.androidapps.comunioelpuntal;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.rukiasoft.androidapps.comunioelpuntal.utils.ActivityTool;

public class DataBaseDownloadReceiver extends BroadcastReceiver {

    private final String TAG = "DataBaseDownloadReceiver";
    private Boolean libre = true;


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(TAG, "notificacion recibida");
        Bundle extras = intent.getExtras();
        if (extras == null) {
            Log.d(TAG, "no han llegado extras");
            return;
        }
        String type = extras.getString("type");
        if (type.compareTo(MainActivity.class.getSimpleName()) == 0) {
            if (libre) {
                TareaLoadDataBase tarea = new TareaLoadDataBase();
                tarea.execute();
            }
        } else if (type.compareTo(StartScreenActivity.class.getSimpleName()) == 0) {
            Log.d(TAG, "estoy en startScreen");
            if(!MainActivity.isGamerSelected()) //no existe player, lo elijo
                StartScreenActivity.finalizarActivity(MainActivity.RESULT_LOAD_SELECT);
            else
                StartScreenActivity.loadData();
        }

    }

    private class TareaLoadDataBase extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            libre = false;
            //MainActivity.loadDatabase(null, null);
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            ActivityTool.HideProgress(MainActivity.getProgressBar(), MainActivity.getContext());
            Log.d(TAG, "ocultado");
            libre = true;
        }
    }


}
