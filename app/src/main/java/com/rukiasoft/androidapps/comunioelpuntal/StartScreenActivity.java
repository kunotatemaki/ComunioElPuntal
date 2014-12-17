package com.rukiasoft.androidapps.comunioelpuntal;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rukiasoft.androidapps.comunioelpuntal.utils.ActivityTool;
import com.rukiasoft.androidapps.comunioelpuntal.utils.ComunioConstants;

/**
 * Created by Ruler on 2014.
 */
public class StartScreenActivity extends Activity {
    private final static String TAG = "StartScreenActivity";
    private static ProgressBar horizontalProgressBar;
    private static ProgressBar indefiniteProgressBar;
    private static TextView descripcion;
    private static AnimationDrawable frameAnimation;
    private static Activity activity;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.d(TAG, "onCreate");
        activity = this;
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //ExceptionHandler.register(this); 
        setContentView(R.layout.start_screen_layout);
        if (!ActivityTool.isForTablet(this))    //una sola pantalla
            ActivityTool.setOrientation(this, MainActivity.Orientation.PORTRAIT);
        else
            ActivityTool.setOrientation(this, MainActivity.Orientation.LANDSCAPE);
        ImageView img = (ImageView) findViewById(R.id.start_screen_image_view);
        horizontalProgressBar = (ProgressBar) findViewById(R.id.start_screen_horizontal_progress_bar);
        indefiniteProgressBar = (ProgressBar) findViewById(R.id.start_screen_indefinite_progress_bar);
        descripcion = (TextView) findViewById(R.id.start_screen_text);
        horizontalProgressBar.setVisibility(View.INVISIBLE);
        indefiniteProgressBar.setVisibility(View.INVISIBLE);
        if (!ActivityTool.isForTablet(this))    //una sola pantalla
            img.setBackgroundResource(R.drawable.homer_animation);
        else
            img.setBackgroundResource(R.drawable.homer_animation);

        // Get the background, which has been compiled to an AnimationDrawable object.
        frameAnimation = (AnimationDrawable) img.getBackground();

        if (ActivityTool.needToLoadDatabase(getApplicationContext())) {    //hay fecha, por tanto, descargo la base de datos
            String fecha = ActivityTool.getStringFromPreferences(this, ComunioConstants.PROPERTY_LAST_UPDATED);
            if (fecha.compareTo("") == 0)
                fecha = "null";
            Log.d(TAG, "comienzo a descargar la base de datos");
            indefiniteProgressBar.setVisibility(View.VISIBLE);
            descripcion.setText(getResources().getString(R.string.database_download));
            MainActivity.GetServerClient().connectToDownloadDatabase(fecha, true);
        } else
            loadData();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus)
            frameAnimation.start();
        else
            frameAnimation.stop();
    }

    public static void loadData() {
        Log.d(TAG, "entro en loadData");
        indefiniteProgressBar.setVisibility(View.INVISIBLE);
        horizontalProgressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                MainActivity.loadDatabase(horizontalProgressBar, descripcion);
                finalizarActivity(RESULT_OK);
                /*if (activity != null) {
                    Log.d(TAG, "pongo ok y termino");
                    activity.setResult(RESULT_OK);
                    activity.finish();
                }*/
            }
        }).start();
    }

    public static void finalizarActivity(int mode){
        if (activity != null) {
            Log.d(TAG, "pongo ok y termino");
            activity.setResult(mode);
            activity.finish();
        }
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");

    }


}
