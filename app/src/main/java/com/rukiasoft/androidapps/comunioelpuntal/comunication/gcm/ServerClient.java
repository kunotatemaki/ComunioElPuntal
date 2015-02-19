package com.rukiasoft.androidapps.comunioelpuntal.comunication.gcm;

/**
 * Created by Ruler on 2014.
 */
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.rukiasoft.androidapps.comunioelpuntal.MainActivity;
import com.rukiasoft.androidapps.comunioelpuntal.R;
import com.rukiasoft.androidapps.comunioelpuntal.StartScreenActivity;
import com.rukiasoft.androidapps.comunioelpuntal.crashlogs.ExceptionHandler;
import com.rukiasoft.androidapps.comunioelpuntal.crashlogs.G;
import com.rukiasoft.androidapps.comunioelpuntal.dataclasses.DatabaseHandler;
import com.rukiasoft.androidapps.comunioelpuntal.utils.ActivityTool;
import com.rukiasoft.androidapps.comunioelpuntal.utils.ComunioConstants;
import com.rukiasoft.androidapps.comunioelpuntal.utils.XMLFactory;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.ParserConfigurationException;


public class ServerClient {

    private String url;
    private String methodNameRegister;
    private String methodNameGetUpdatedDatabase;
    private String methodNameSendLogDeveloper;
    private String senderID;
    private Document doc;

    private Context context;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private static Boolean downloadingApp = false;


    private static final long EXPIRATION_TIME_MS = 1000 * 3600 * 24 * 7;
    private GoogleCloudMessaging gcm;
    private static final String TAG = "ServerClient";

    private String regid;

    //public static String name;
    private String email = "";

    //private static Boolean registradoServer = false;
    public static enum AccessMode {
        SERVICE, ACTIVITY
    }

    public ServerClient(Context context, InputStream file) throws IOException, SAXException, ParserConfigurationException {
        this.doc = XMLFactory.getDocument(file);
        this.context = context;
        if (!LoadParameters())
            Log.i(TAG, "No se ha podido configurar el servidor");
        AccountManager manager = AccountManager.get(context);
        Account[] accounts = manager.getAccountsByType("com.google");
        for (Account account : accounts) {
            if (account.name.endsWith("gmail.com")) {
                email = account.name;
            }
        }

    }

    private Boolean LoadParameters() {

        NodeList list = doc.getDocumentElement().getElementsByTagName("server_gcm");
        try {
            for (int i = 0; i < list.getLength(); i++) {
                Node e = list.item(i);
                String host = e.getAttributes().getNamedItem("host").getNodeValue();
                url = e.getAttributes().getNamedItem("url").getNodeValue();
                String port = e.getAttributes().getNamedItem("port").getNodeValue();
                url = setUrl(url, host, port);

                this.senderID = e.getAttributes().getNamedItem("sender_id").getNodeValue();
                methodNameRegister = e.getAttributes().getNamedItem("method_name_register").getNodeValue();
                methodNameGetUpdatedDatabase = e.getAttributes().getNamedItem("method_name_get_updated_database").getNodeValue();
                methodNameSendLogDeveloper = e.getAttributes().getNamedItem("method_name_send_log_developer").getNodeValue();
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    String setUrl(String _url, String _host, String _port) {
        String url = _url.replaceAll("port", _port);
        url = url.replaceAll("host", _host);
        return url;
    }

    public void conectar(AccessMode mode) {

        //Log.d(TAG, "Intentamos conectar al servidor de google y propio");
        ConnectionDetector cd = new ConnectionDetector(context);
        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            Log.i(TAG, "no hay conexión a internet");
            ActivityTool.showToast(context,
                    context.getResources().getString(R.string.no_connection));
            // stop executing code by return
            return;
        }
        //Chequemos si está instalado Google Play Services
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(context);

            //Obtenemos el Registration ID guardado
            regid = getValidRegistrationIdFromSharedPreferences(context);

            //Si no disponemos de Registration ID comenzamos el registro
            if (regid.equals("")) {
                if (mode == AccessMode.ACTIVITY) {
                    ActivityTool.ShowProgress(MainActivity.getProgressBar(), context);
                    TareaGetGCMIDAndRegisterInComunioServer tarea = new TareaGetGCMIDAndRegisterInComunioServer();
                    tarea.execute(this.email, this.senderID);
                } else if (mode == AccessMode.SERVICE) {
                    ServiceGetGCMIDAndRegisterInSacarinoServer(this.email, this.senderID);
                }
            }
        } else {
            Log.i(TAG, "No se ha encontrado Google Play Services.");
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                if (MainActivity.isCreated())
                    GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity) context,
                            PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            return false;
        }
        return true;
    }

    private String getValidRegistrationIdFromSharedPreferences(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String registrationId = prefs.getString(ComunioConstants.PROPERTY_REG_ID, "");

        if (registrationId.length() == 0) {
            Log.d(TAG, "Registro GCM no encontrado.");
            return "";
        }

        String registeredUser =
                prefs.getString(ComunioConstants.PROPERTY_EMAIL, "email");

        int registeredVersion =
                prefs.getInt(ComunioConstants.PROPERTY_APP_VERSION, Integer.MIN_VALUE);

        long expirationTime =
                prefs.getLong(ComunioConstants.PROPERTY_EXPIRATION_TIME, -1);


        int currentVersion = ActivityTool.getAppVersion(context);

        if (registeredVersion != currentVersion) {
            Log.d(TAG, "Nueva versión de la aplicación.");
            return "";
        } else if (System.currentTimeMillis() > expirationTime) {
            Log.d(TAG, "Registro GCM expirado.");
            return "";
        } else if (!email.equals(registeredUser)) {
            Log.d(TAG, "Nuevo nombre de usuario.");
            return "";
        }
        //Log.d(TAG, "el registro que devuelvo es: " + registrationId);
        return registrationId;
    }

    private class TareaGetGCMIDAndRegisterInComunioServer extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            Log.i(TAG, "Tarea de registro from context");
            return GetGCMIDAndRegisterInSacarinoServer(params[0], params[1]);

        }

        @Override
        protected void onPostExecute(String result) {
            if (result.compareTo("") != 0) {
                Log.d(TAG, "Voy a mandar un Toast de error de conexión al servidor");
                ActivityTool.showToast(context, result);
                //Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                //registradoServer = false;
            } else {
                Log.d(TAG, "Registro en servidor satisfactiorio");
                //registradoServer = true;
            }
            ActivityTool.HideProgress(MainActivity.getProgressBar(), context);
        }
    }

    private void ServiceGetGCMIDAndRegisterInSacarinoServer(String user, String id) {
        Log.i(TAG, "Tarea de registro from service");
        GetGCMIDAndRegisterInSacarinoServer(user, id);
        Log.d(TAG, "Finaliza el service de registro al iniciar");
    }

    private String GetGCMIDAndRegisterInSacarinoServer(String user, String id) {
        String msg = "";
        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(context);
            }
            //Nos registramos en los servidores de GCM
            regid = gcm.register(id);
            Log.d(TAG, "Registrado en GCM: registration_id=" + regid);
            //Nos registramos en nuestro servidor
            boolean registrado = registerGCMIDInComunioServer(user, regid);
            //Guardamos los datos del registro
            if (!registrado) {    //ha fallado
                user = "";
                msg = context.getResources().getString(R.string.error_server_GCM);
            }
            saveClientRegistrationData(context, user, regid);
        } catch (IOException ex) {
            Log.d(TAG, "Error registro en GCM:" + ex.getMessage());
            msg = context.getResources().getString(R.string.error_server_GCM);
        } catch (Exception e) {
            Log.d(TAG, "algo ha fallado, posiblemente con las suscripciones");
        }
        return msg;
    }

    public void connectToDownloadDatabase(String fecha, Boolean initialScreen) {

        Log.d(TAG, "Empiezo a descargar la database");
        String type;
        if (initialScreen) {
            type = StartScreenActivity.class.getSimpleName();
            Log.d(TAG, "estoy en la pantalla inicial: " + StartScreenActivity.class.getSimpleName());
        } else {
            ActivityTool.ShowProgress(MainActivity.getProgressBar(), context);
            type = MainActivity.class.getSimpleName();
            Log.d(TAG, "estoy dentro de la aplicacion: " + MainActivity.class.getSimpleName());
        }
        TareaDownloadDatabase tareaDownloadDatabase = new TareaDownloadDatabase();
        tareaDownloadDatabase.execute(fecha, type);
    }

    private class TareaDownloadDatabase extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            return downloadDataBase(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(String type) {
            Intent intent = new Intent(ComunioConstants.DATABASE_DOWNLOADED_ACTION_INTENT);
            intent = intent.putExtra("type", type);
            Log.d(TAG, "Mando el broadcast");
            context.sendBroadcast(intent, null);
        }
    }

    private String downloadDataBase(String fecha, String type) {
        try {
            MainActivity.setDatabaseDownloading(true);
            // registramos las notificaciones
            JSONObject respuesta = downloadDatabaseFromComunioServer(fecha);

            //Guardamos los la fecha del registro
            if (respuesta.getInt("error_code") == 0) {
                //Almaceno los datos en la base de datos
                DatabaseHandler dbHandler = new DatabaseHandler(context);
                long resultado = dbHandler.storeData(respuesta);
                if (resultado >= 0) {
                    String formattedDate = ActivityTool.getCurrentDate(context);
                    //Log.d(TAG, "salvo: " + formattedDate);
                    ActivityTool.savePreferences(context, ComunioConstants.PROPERTY_LAST_UPDATED, formattedDate);
                }
            }
        } catch (Exception ex) {
            Log.d(TAG, "Error descargando base de datos ComunioServer:" + ex.getMessage());
            MainActivity.setDatabaseDownloading(false);
        }
        return type;
    }

    private void saveClientRegistrationData(Context context, String user, String regId) {
        int appVersion = ActivityTool.getAppVersion(context);
        //Log.d(TAG, "Salvo preferencias GCM: " + user + appVersion + regId);
        ActivityTool.savePreferences(context, ComunioConstants.PROPERTY_EMAIL, user);
        ActivityTool.savePreferences(context, ComunioConstants.PROPERTY_REG_ID, regId);
        ActivityTool.savePreferences(context, ComunioConstants.PROPERTY_APP_VERSION, appVersion);
        ActivityTool.savePreferences(context, ComunioConstants.PROPERTY_EXPIRATION_TIME, System.currentTimeMillis() + EXPIRATION_TIME_MS);
    }

    private boolean registerGCMIDInComunioServer(String usuario, String regId) {

        //Log.i(TAG, "registering device (regId = " + regId + ")");
        JSONObject paramsJSON = new JSONObject();
        try {
            String myName = ActivityTool.getStringFromPreferences(context,
                    ComunioConstants.PROPERTY_MY_TEAM);

            paramsJSON.put("name", myName);
            paramsJSON.put("email", usuario);
            paramsJSON.put("gcm_regid", regId);
            paramsJSON.put("imei", G.IMEI);
        } catch (Exception e) {
            Log.d(TAG, "excepción al crear el JSON");
        }
        String endpoint;
        endpoint = url;
        endpoint = endpoint + methodNameRegister;
        //Log.d(TAG, "notif: " + endpoint);
        try {
            restfulPOST(endpoint, paramsJSON);
            return true;
        } catch (IOException e) {
            Log.e(TAG, "falla al registrar");
            return false;
        }
    }

    private JSONObject downloadDatabaseFromComunioServer(String fecha) throws Exception {

        //Log.i(TAG, "registering suscriptions");
        JSONObject paramsJSON = new JSONObject();
        JSONObject response;

        try {
            paramsJSON.put("fecha", fecha);
        } catch (Exception e) {
            Log.d(TAG, "excepción al crear el JSON");
        }
        String endpoint = url;

        endpoint = endpoint + methodNameGetUpdatedDatabase;
        try {
            response = restfulPOST(endpoint, paramsJSON);
        } catch (IOException e) {
            Log.e(TAG, "falla al descargar la base de datos");
            throw new Exception(e.getMessage());
        }
        return response;
    }

    private JSONObject restfulPOST(String endpoint, JSONObject params) throws IOException {

        JSONObject response;
        HttpClient client = new DefaultHttpClient();
        String responseBody;

        try {
            HttpPost post = new HttpPost(endpoint);
            Log.d(TAG, "posting to: " + endpoint);
            StringEntity se = new StringEntity(params.toString());
            //Log.d(TAG, "json mandado: " + se);
            post.setEntity(se);
            post.setHeader(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setHeader("Content-type", "application/json");
            //Log.e(TAG, "webservice request executing");

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            responseBody = client.execute(post, responseHandler);
            response = new JSONObject(responseBody);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
        return response;
    }

    public Boolean connectToDownloadApp(String DownloadUrl) {

        Log.d(TAG, "Empiezo a descargar la app");
        Boolean ret;
        setDownloadingApp(true);
        ret = ServiceDownloadFromUrl(DownloadUrl);

        return ret;
    }

    private Boolean ServiceDownloadFromUrl(String DownloadUrl) {
        Boolean result = DownloadFromUrl(DownloadUrl);
        setDownloadingApp(false);
        if (result) {
            Log.d(TAG, "en service pongo download a false, e install a true");
            ActivityTool.savePreferences(context, ComunioConstants.PROPERTY_DOWNLOAD_UPDATED_APP, false);
            ActivityTool.savePreferences(context, ComunioConstants.PROPERTY_INSTALL_UPDATED_APP, true);
        }
        return result;

    }

    private Boolean DownloadFromUrl(String DownloadUrl) {
        try {
            File root = android.os.Environment.getExternalStorageDirectory();

            File dir = new File(root.getAbsolutePath() + ComunioConstants.DIRECTORY_APP);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            URL url = new URL(DownloadUrl); //you can write here any link
            File file = new File(dir, ComunioConstants.NOMBRE_APP);

            long startTime = System.currentTimeMillis();
            Log.d(TAG, "download url:" + url);

            // Open a connection to that URL.
            URLConnection ucon = url.openConnection();

            // Define InputStreams to read from the URLConnection.

            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

            //Read bytes to the Buffer until there is nothing more to read(-1).
            ByteArrayBuffer baf = new ByteArrayBuffer(5000);
            int current;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }

            // Convert the Bytes read to a String.
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baf.toByteArray());
            fos.flush();
            fos.close();
            Log.d("DownloadManager", "download ready in: " + ((System.currentTimeMillis() - startTime) / 1000) + " sec");

        } catch (IOException e) {
            Log.d("DownloadManager", "Error: " + e);
            return false;
        }
        return true;
    }

    public static Boolean isDownloadingApp() {
        return downloadingApp;
    }

    private static void setDownloadingApp(Boolean downloadingApp) {
        ServerClient.downloadingApp = downloadingApp;
    }

    public void connectToSendLogDeveloper(JSONObject paramsJSON, String path) {

        Log.d(TAG, "Mando el log");
        TareaSendLogDeveloper tareaSendLogDeveloper = new TareaSendLogDeveloper();
        tareaSendLogDeveloper.setPath(path);
        tareaSendLogDeveloper.execute(paramsJSON);
    }

    private class TareaSendLogDeveloper extends AsyncTask<JSONObject, Integer, JSONObject> {
        private String path;

        public void setPath(String path) {
            this.path = G.FILES_PATH + "/" + path;
        }

        @Override
        protected JSONObject doInBackground(JSONObject... params) {
            String endpoint = url;
            JSONObject response = new JSONObject();
            endpoint = endpoint + methodNameSendLogDeveloper;
            try {
                params[0].put("email", email);
                params[0].put("timestamp", ActivityTool.getCurrentDateLog(context));
                response = restfulPOST(endpoint, params[0]);
            } catch (IOException e) {
                Log.e(TAG, "falla al enviar el log");
                try {
                    response.put("error_code", ComunioConstants.ERROR_NO_PUSH);
                    response.put("error_description", "Falla al mandar el log");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                    throw new RuntimeException(e1);
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
                throw new RuntimeException(e1);
            }
            return response;
        }

        @Override
        protected void onPostExecute(JSONObject respuesta) {
            try {
                if (respuesta.getInt("error_code") == 0) {
                    String[] list = ExceptionHandler.searchForStackTraces();
                    for (String aList : list) {
                        Log.d(TAG, "Borro archivo: " + path);
                        File file = new File(path);
                        file.delete();
                    }
                } else
                    Log.d(TAG, "el error ha sido: " + respuesta.getString("error_description"));
            } catch (JSONException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}



