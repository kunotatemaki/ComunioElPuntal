package com.rukiasoft.androidapps.comunioelpuntal;

import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rukiasoft.androidapps.comunioelpuntal.comunication.gcm.GCMBroadcastReceiver;
import com.rukiasoft.androidapps.comunioelpuntal.dataclasses.Participante;
import com.rukiasoft.androidapps.comunioelpuntal.dataclasses.Player;
import com.rukiasoft.androidapps.comunioelpuntal.utils.ActivityTool;
import com.rukiasoft.androidapps.comunioelpuntal.utils.ComunioConstants;

import java.util.List;

//import android.R;


public class LoadDatabaseService extends IntentService {
    private static final String TAG = "LoadDatabaseService";
    private Boolean mostrarProgreso = false;
    public LoadDatabaseService() {
        super("LoadDatabaseService");
    }
    private TextView textView= null;
    private ProgressBar progressBar = null;

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();


        if (!extras.isEmpty()) {
            String type = extras.getString("type");
            if (type.compareTo(MainActivity.class.getSimpleName()) == 0)
                mostrarProgreso = false;
            else if (type.compareTo(StartScreenActivity.class.getSimpleName()) == 0)
                mostrarProgreso = true;
            else if(type.compareTo(SelectGamerActivity.class.getSimpleName()) == 0){
                Intent i = new Intent();
                i.setAction(ComunioConstants.START_SELECT_PLAYER_ACTIVITY);
                sendBroadcast(i);
                return;
            }
        }
        loadDatabase();

        GCMBroadcastReceiver.completeWakefulIntent(intent);
    }

    public void loadDatabase() {
        Log.d(TAG, "loadDatabaseService");
        try {
            Log.d(TAG, "leyendo participantes");
            String texto = getResources().getString(R.string.load_gamers);
            if(mostrarProgreso) {
                startLoad();
                setTextProgress(texto);
            }
            List<Participante> participantes = MainActivity.getdbHandler().getAllGamers();
            if(mostrarProgreso)
                setProgress(10);
            MainActivity.getGamers().clear();
            MainActivity.getGamerFragment().setGamer(null);
            //Log.d(TAG, "cargando participantes");
            for (int i = 0; i < participantes.size(); i++) {
                GamerInformation gamer = new GamerInformation();
                gamer.setGamerInfo(participantes.get(i));
                MainActivity.getGamers().add(gamer);
                if(mostrarProgreso) {
                    setTextProgress(texto + " " + gamer.getParticipante().getNombre());
                    setProgress(10 + i * 70 / participantes.size());
                }
            }

            Log.d(TAG, "leyendo players");
            texto = getResources().getString(R.string.load_players);
            if(mostrarProgreso)
                setTextProgress(texto);


            MainActivity.getPlayersFragment().clearAdapterWithoutRefresh();

            List<Player> jugadores = MainActivity.getdbHandler().getPlayerList(null, null);

            for (int i = 0; i < 16; i++) {
                PlayerItem item = new PlayerItem(jugadores.get(i), MainActivity.getContext());
                MainActivity.getPlayersFragment().addItemWithoutRefresh(item);
                if(1%10 == 0 && mostrarProgreso)
                    setProgress(80 + i * 20 / jugadores.size());
            }

            ((Activity)MainActivity.getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MainActivity.getPlayersFragment().refresh();
                }
            });

            MainActivity.setPlayers(MainActivity.getPlayersFragment().getPlayerItems());
            if(mostrarProgreso) {
                setProgress(100);
                finishLoad();
            }else{
                ((Activity)MainActivity.getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ActivityTool.HideProgress(MainActivity.getProgressBar(), MainActivity.getContext());
                    }
                });
            }
            MainActivity.setDatabaseDownloading(false);
            MainActivity.setDatabaseLoaded(true);
            MainActivity.resetJornadasJSON();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void setTextProgress(String textProgress){
        Intent i = new Intent();
        i.setAction(ComunioConstants.SET_TEXT_PROGRESS_ACTION_INTENT);
        i.putExtra("text", textProgress);
        sendBroadcast(i);
    }

    private void setProgress(Integer progress){
        Intent i = new Intent();
        i.setAction(ComunioConstants.SET_PROGRESS_BAR_ACTION_INTENT);
        i.putExtra("progress", progress);
        sendBroadcast(i);
    }

    private void startLoad(){
        Intent i = new Intent();
        i.setAction(ComunioConstants.START_LOAD_ON_START_SCREEN);
        sendBroadcast(i);
    }

    private void finishLoad(){
        Intent i = new Intent();
        i.setAction(ComunioConstants.FINISH_LOAD_ON_START_SCREEN);
        sendBroadcast(i);
    }

}
