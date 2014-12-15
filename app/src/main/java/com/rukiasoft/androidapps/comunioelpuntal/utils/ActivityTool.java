package com.rukiasoft.androidapps.comunioelpuntal.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import com.rukiasoft.androidapps.comunioelpuntal.MainActivity.Orientation;
import com.rukiasoft.androidapps.comunioelpuntal.R;


public class ActivityTool {

    static final String TAG = "ActivityTool";
    static public String ACTIVITY = "activity";
    static private Toast toast = null;

    public static String getStringFromString(Resources r, String string, String packageName) {
        String result;

        try {
            result = r.getString(r.getIdentifier(string, "string", packageName));
        } catch (Resources.NotFoundException e) {
            return string;
        }
        return result;
    }

    public static Drawable getDrawableFromString(Resources r, String string, String packageName) {
        return r.getDrawable(getDrawableIdFromString(r, string, packageName));
    }

    public static int getDrawableIdFromString(Resources r, String string, String packageName) {
        int result;

        try {
            result = r.getIdentifier(string, "drawable", packageName);
        } catch (Resources.NotFoundException e) {
            return R.drawable.ic_launcher;
        }
        if (result == 0)
            return R.drawable.ic_launcher;
        return result;
    }

    public static void showToast(final Context context, final String text, final int duration) {

        //sólo quiero mostrar si viene de una activity
        if (!(context instanceof Activity)) {
            Log.d(TAG, "no venía de una activity");
            return;
        }
        try {
            if (toast != null) {
                if (toast.getView().isShown()) {
                    toast.setText(text);
                    return;
                }
            }
            toast = Toast.makeText(context, text, duration);
            toast.show();
        } catch (Exception e) {
            Log.e(TAG, "error en showToast: " + e.getMessage());
        }
    }

    public static void savePreferences(Context context, String name, String value) {

        //SharedPreferences preferences = context.getSharedPreferences("sacarino",Context.MODE_PRIVATE);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor ed = preferences.edit();
        ed.putString(name, value);
        ed.apply();

    }

    public static void savePreferences(Context context, String name, int value) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor ed = preferences.edit();
        ed.putInt(name, value);
        ed.apply();

    }

    public static void savePreferences(Context context, long value) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor ed = preferences.edit();
        ed.putLong(ComunioConstants.PROPERTY_EXPIRATION_TIME, value);
        ed.apply();

    }

    public static void savePreferences(Context context, String name, Boolean value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor ed = preferences.edit();
        ed.putBoolean(name, value);
        ed.apply();

    }

    public static String getStringFromPreferences(Context context, String name) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(name, "");

    }

    public static ArrayList<String> getArrayStringFromPreferences(Context context, String name) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> valores = preferences.getStringSet(name, null);
        ArrayList<String> options = new ArrayList<>();
        options.clear();
        for (String str : valores) {
            options.add(str);
        }
        return options;

    }

    public static String getStringFromArrayStringFromPreferences(Context context, String name) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> valores = preferences.getStringSet(name, null);
        if (valores == null)
            return "";
        ArrayList<String> options = new ArrayList<>();
        options.clear();
        for (String str : valores) {
            options.add(str);
        }
        return TextUtils.join(":", options);

    }

    public static Boolean getBooleanFromPreferences(Context context, String name) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(name, false);

    }

    public static Integer getIntegerFromPreferences(Context context, String name) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(name, 0);

    }

    public static Boolean hasVibrator(Context context) {
        String vs = Context.VIBRATOR_SERVICE;
        Vibrator mVibrator = (Vibrator) context.getSystemService(vs);
        return mVibrator.hasVibrator();
    }

    public static void ShowProgress(SmoothProgressBar progressBar, Context context) {
        if (null == progressBar)
            return;
        //progressBar.setVisibility(View.VISIBLE);
        Log.d(TAG, "muestro");
        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics());
        LayoutParams params = progressBar.getLayoutParams();
        params.height = (int) pixels;
        progressBar.setLayoutParams(params);
        //progressBar.getLayoutParams().height = 8;
        progressBar.progressiveStart();
    }

    public static void HideProgress(SmoothProgressBar progressBar, Context context) {
        if (null == progressBar)
            return;
        progressBar.progressiveStop();
        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, context.getResources().getDisplayMetrics());
        LayoutParams params = progressBar.getLayoutParams();
        params.height = (int) pixels;
        progressBar.setLayoutParams(params);

        //progressBar.getLayoutParams().height = 0;
        //progressBar.setVisibility(View.INVISIBLE);

    }

    public static Boolean isForTablet(Activity activity) {

        return activity.findViewById(R.id.mode_tablet) != null;

    }

    public static String getCurrentDate(Context context) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(ComunioConstants.FORMAT_DATE_TIME,
                context.getResources().getConfiguration().locale);
        return df.format(c.getTime());
    }

    public static String getCurrentDateLog(Context context) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(ComunioConstants.FORMAT_DATE_TIME_LOG,
                context.getResources().getConfiguration().locale);
        return df.format(c.getTime());
    }

    public static Date getDateFromString(Context context, String fecha) {
        SimpleDateFormat form = new SimpleDateFormat(ComunioConstants.FORMAT_DATE_TIME, context.getResources().getConfiguration().locale);
        Date date = null;
        try {
            date = form.parse(fecha);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getFormatedCurrencyNumber(Integer number) {
        DecimalFormat svSE = new DecimalFormat("#,###");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("es", "ES"));
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        svSE.setDecimalFormatSymbols(symbols);
        return svSE.format(number);
    }

    public static String getStringFromDouble(Double num) {
        if (num % 1 == 0) {
            Integer intNum = num.intValue();
            return intNum.toString();
        } else
            return num.toString();
    }

    public static boolean needToLoadDatabase(Context context) {
        Calendar c = Calendar.getInstance();
        Date today = c.getTime();
        String fechaDB = ActivityTool.getStringFromPreferences(context, ComunioConstants.PROPERTY_LAST_UPDATED);
        if (fechaDB.compareTo("") == 0)
            return true;
        Date dateDB = ActivityTool.getDateFromString(context, fechaDB);
        //Log.d(TAG, "base de datos: " + dateDB.toString());
        Date newDate = new Date(dateDB.getTime() + ComunioConstants.DATABASE_REFRESH);
        //Log.d(TAG, "expira: " + newDate.toString());
        //Log.d(TAG, "hoy: " + today.toString());
        return today.after(newDate);
    }

    public static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            throw new RuntimeException("Error al obtener versión: " + e);
        }
    }

    public static void setOrientation(Activity activity, Orientation orientation) {
        //Log.d(TAG, "he llamado desde: " + activity.getClass().getSimpleName());
        if (orientation == Orientation.PORTRAIT)
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else if (orientation == Orientation.LANDSCAPE)
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
}
