package com.rukiasoft.androidapps.comunioelpuntal.comunication.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.res.AssetManager;
import android.util.Log;

import com.rukiasoft.androidapps.comunioelpuntal.crashlogs.ExceptionHandler;

import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

public class RegisterService extends IntentService {
    static String TAG = "RegisterService";

    public RegisterService() {
        super("RegisterService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            AssetManager am = getAssets();
            ServerClient cliente = new ServerClient(getApplicationContext(), am.open("application.xml"));
            Log.d(TAG, "Intento conectar el cliente desde el servicio");
            ExceptionHandler.register(this);
            cliente.conectar(ServerClient.AccessMode.SERVICE);

        } catch (IOException e) {
            Log.i(TAG, "Excepción crear cliente IOException");
        } catch (SAXException e) {
            Log.i(TAG, "Excepción crear cliente SAXException");
        } catch (ParserConfigurationException e) {
            Log.i(TAG, "Excepción crear cliente ParserConfigurationException");
        } catch (Exception e) {
            Log.i(TAG, "Excepción no controlada al crear cliente");
        }


    }


}

