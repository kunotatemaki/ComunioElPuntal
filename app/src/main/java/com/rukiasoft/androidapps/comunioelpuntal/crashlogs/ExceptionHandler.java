/*
Copyright (c) 2009 nullwire aps

Permission is hereby granted, free of charge, to any person
obtaining a copy of this software and associated documentation
files (the "Software"), to deal in the Software without
restriction, including without limitation the rights to use,
copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the
Software is furnished to do so, subject to the following
conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

Contributors: 
Mads Kristiansen, mads.kristiansen@nullwire.com
Glen Humphrey
Evan Charlton
Peter Hewitt
*/

package com.rukiasoft.androidapps.comunioelpuntal.crashlogs;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.os.Build;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;

import com.rukiasoft.androidapps.comunioelpuntal.MainActivity;
import com.rukiasoft.androidapps.comunioelpuntal.comunication.gcm.ServerClient;
import com.rukiasoft.androidapps.comunioelpuntal.utils.ActivityTool;

public class ExceptionHandler {

    private static final String TAG = "com.nullwire.trace.ExceptionsHandler";
    private static Context context;
    private static String[] stackTraceFileList = null;


    public static boolean register(Context _context) {
        Log.i(TAG, "Registering default exceptions handler");
        context = _context;
        // Get information about the Package
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi;
            // Version
            pi = pm.getPackageInfo(context.getPackageName(), 0);
            G.APP_VERSION = pi.versionName;
            // Package name
            G.APP_PACKAGE = pi.packageName;
            // Files dir for storing the stack traces
            G.FILES_PATH = context.getFilesDir().getAbsolutePath();
            // Device model
            G.PHONE_MODEL = android.os.Build.MODEL;
            // Android version
            G.ANDROID_VERSION = android.os.Build.VERSION.RELEASE;
            //Board
            G.BOARD = Build.BOARD;
            // Brand
            G.BRAND = Build.BRAND;
            // Device
            G.DEVICE = Build.DEVICE;
            // FingerPrint
            G.FINGERPRINT = Build.FINGERPRINT;
            // Manufacturer
            G.MANUFACTURER = Build.MANUFACTURER;
            // Product
            G.PRODUCT = Build.PRODUCT;
            // Serial
            G.SERIAL = Build.SERIAL;

            //IMEI
            G.IMEI = ActivityTool.getImei(context);

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        boolean stackTracesFound = false;
        // We'll return true if any stack traces were found
        if (searchForStackTraces().length > 0) {
            stackTracesFound = true;
        }

        new Thread() {
            @Override
            public void run() {
                // First of all transmit any stack traces that may be lying around
                submitStackTraces();
                UncaughtExceptionHandler currentHandler = Thread.getDefaultUncaughtExceptionHandler();
                if (currentHandler != null) {
                    Log.d(TAG, "current handler class=" + currentHandler.getClass().getName());
                }
                // don't register again if already registered
                if (!(currentHandler instanceof DefaultExceptionHandler)) {
                    Log.d(TAG, "Register default exceptions handler");
                    Thread.setDefaultUncaughtExceptionHandler(
                            new DefaultExceptionHandler(currentHandler));
                }
            }
        }.start();

        return stackTracesFound;
    }



    public static String[] searchForStackTraces() {
        if (stackTraceFileList != null) {
            return stackTraceFileList;
        }
        File dir = new File(G.FILES_PATH + "/");
        // Try to create the files folder if it doesn't exist
        dir.mkdir();
        // Filter for ".stacktrace" files
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".stacktrace");
            }
        };
        return (stackTraceFileList = dir.list(filter));
    }

    /**
     * Look into the files folder to see if there are any "*.stacktrace" files.
     * If any are present, submit them to the trace server.
     */
    private static void submitStackTraces() {
        try {
            Log.d(TAG, "Looking for exceptions in: " + G.FILES_PATH);
            String[] list = searchForStackTraces();
            if (list != null && list.length > 0) {
                Log.d(TAG, "Found " + list.length + " stacktrace(s)");
                for (String aList : list) {
                    String filePath = G.FILES_PATH + "/" + aList;
                    // Extract the version from the filename: "packagename-version-...."
                    String version = aList.split("-")[0];
                    Log.d(TAG, "Stacktrace in file '" + filePath + "' belongs to version " + version);
                    // Read contents of stacktrace
                    StringBuilder contents = new StringBuilder();
                    BufferedReader input = new BufferedReader(new FileReader(filePath));
                    String line;
                    String androidVersion = null;
                    String phoneModel = null;
                    while ((line = input.readLine()) != null) {
                        if (androidVersion == null) {
                            androidVersion = line;
                            continue;
                        } else if (phoneModel == null) {
                            phoneModel = line;
                            continue;
                        }
                        contents.append(line);
                        contents.append(System.getProperty("line.separator"));
                    }
                    input.close();
                    String stacktrace;
                    stacktrace = contents.toString();
                    Log.d(TAG, "Transmitting stack trace: ");
                    // Transmit stack trace with POST request
                    JSONObject paramsJSON = new JSONObject();
                    try {
                        paramsJSON.put("packageName", G.APP_PACKAGE);
                        paramsJSON.put("appVersion", version);
                        paramsJSON.put("phoneModel", phoneModel);
                        paramsJSON.put("androidVersion", androidVersion);
                        paramsJSON.put("manufacturer", G.MANUFACTURER);
                        paramsJSON.put("imei", G.IMEI);
                        paramsJSON.put("stacktrace", stacktrace);
                    } catch (Exception e) {
                        Log.d(TAG, "excepción al crear el JSON");
                    }

                    ServerClient cliente = MainActivity.GetServerClient();
                    if (cliente == null) {
                        AssetManager am = context.getAssets();
                        cliente = new ServerClient(context.getApplicationContext(), am.open("application.xml"));
                    }
                    cliente.connectToSendLogDeveloper(paramsJSON, aList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
