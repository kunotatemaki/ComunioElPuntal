package com.rukiasoft.androidapps.comunioelpuntal;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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


    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();


        if (!extras.isEmpty()) {
            String type = extras.getString("type");
            if (type.compareTo(MainActivity.class.getSimpleName()) == 0) {
                mostrarProgreso = false;
            }else if (type.compareTo(StartScreenActivity.class.getSimpleName()) == 0)
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

    void loadDatabase() {
        MainActivity.setDatabaseLoaded(false);
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

            for (int i = 0; i < jugadores.size(); i++) {
                PlayerItem item = new PlayerItem(jugadores.get(i), MainActivity.getContext());
                MainActivity.getPlayersFragment().addItemWithoutRefresh(item);
                if(i%30 == 0 && mostrarProgreso)
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
                        restartInterface();
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

    private void restartInterface(){
        Intent i = new Intent();
        i.setAction(ComunioConstants.RESTART_INTEFACE_IN_MY_TEAM);
        sendBroadcast(i);
    }

}

