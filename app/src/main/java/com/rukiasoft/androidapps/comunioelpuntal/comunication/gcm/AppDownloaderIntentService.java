package com.rukiasoft.androidapps.comunioelpuntal.comunication.gcm;


import android.app.IntentService;
import android.content.Intent;
import android.content.res.AssetManager;
import android.util.Log;

import com.rukiasoft.androidapps.comunioelpuntal.MainActivity;
import com.rukiasoft.androidapps.comunioelpuntal.utils.ActivityTool;
import com.rukiasoft.androidapps.comunioelpuntal.utils.ComunioConstants;

import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

public class AppDownloaderIntentService extends IntentService {
    static final String CUSTOM_INTENT = "ruler.elpuntal.comunio.androidapp.NOTIFICATION";

    static String TAG = "AppDownloaderIntentService";
    //DataBase


    public AppDownloaderIntentService() {
        super("AppDownloaderIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        Log.d(TAG, "toca descargar");
        ServerClient cliente = MainActivity.GetServerClient();
        if (cliente == null) {
            Log.d(TAG, "no se había creado el cliente, lo creo aquí");
            AssetManager am = getAssets();
            try {
                cliente = new ServerClient(getApplicationContext(), am.open("application.xml"));
            } catch (IOException | SAXException | ParserConfigurationException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        Integer versionInstalled = ActivityTool.getIntegerFromPreferences(getApplicationContext(), ComunioConstants.PROPERTY_VERSION_APP_DOWNLOADED);

        Integer versionForDownload = ActivityTool.getIntegerFromPreferences(getApplicationContext(), ComunioConstants.PROPERTY_VERSION_APP_FOR_DOWNLOADING);

        Log.d(TAG, "había: " + versionInstalled + " - recibido: " + versionForDownload);

        if (versionInstalled >= versionForDownload) {
            Log.d(TAG, "misma o inferior version");
            GCMBroadcastReceiver.completeWakefulIntent(intent);
            return;
        }
        if (ServerClient.isDownloadingApp()) {
            Log.d(TAG, "ya estaba descargando la app");
            GCMBroadcastReceiver.completeWakefulIntent(intent);
            return;
        }
        if (cliente.connectToDownloadApp(ActivityTool.getStringFromPreferences(getApplicationContext(),
                ComunioConstants.PROPERTY_PATH_DOWNLOAD_APP))) {
            Log.d(TAG, "connectToDownloadApp devuelve true");
            ActivityTool.savePreferences(getApplicationContext(), ComunioConstants.PROPERTY_VERSION_APP_DOWNLOADED, versionForDownload);
        }
        GCMBroadcastReceiver.completeWakefulIntent(intent);
    }

}

