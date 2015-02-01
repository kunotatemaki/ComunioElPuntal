package com.rukiasoft.androidapps.comunioelpuntal;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
    private ProgressBar horizontalProgressBar;
    private ProgressBar indefiniteProgressBar;
    private TextView descripcion;
    private AnimationDrawable frameAnimation;
    private Activity activity;

    protected class ProgressListener extends BroadcastReceiver {

        Activity activity;

        ProgressListener(Activity _activity){
            this.activity = _activity;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ComunioConstants.SET_PROGRESS_BAR_ACTION_INTENT)) {
                Integer progress = intent.getExtras().getInt("progress");
                ((StartScreenActivity) activity).setProgress(progress);
            }else if (intent.getAction().equals(ComunioConstants.SET_TEXT_PROGRESS_ACTION_INTENT)) {
                String text = intent.getExtras().getString("text");
                ((StartScreenActivity) activity).setTextProgress(text);
            }else if (intent.getAction().equals(ComunioConstants.START_LOAD_ON_START_SCREEN)) {
                ((StartScreenActivity) activity).startLoad();
            }else if (intent.getAction().equals(ComunioConstants.FINISH_LOAD_ON_START_SCREEN)) {
                ((StartScreenActivity) activity).finishLoad();
            }

        }
    }

    private ProgressListener mListener;

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

        mListener = new ProgressListener(this);
        registerReceiver(mListener, new IntentFilter(ComunioConstants.SET_PROGRESS_BAR_ACTION_INTENT));
        registerReceiver(mListener, new IntentFilter(ComunioConstants.SET_TEXT_PROGRESS_ACTION_INTENT));
        registerReceiver(mListener, new IntentFilter(ComunioConstants.START_LOAD_ON_START_SCREEN));
        registerReceiver(mListener, new IntentFilter(ComunioConstants.FINISH_LOAD_ON_START_SCREEN));

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
        } else {
            Intent intent = new Intent(ComunioConstants.DATABASE_DOWNLOADED_ACTION_INTENT);
            intent = intent.putExtra("type", StartScreenActivity.class.getSimpleName());
            sendBroadcast(intent);
            //loadData();
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus)
            frameAnimation.start();
        else
            frameAnimation.stop();
    }

    void finalizarActivity(){
        if (activity != null) {
            Log.d(TAG, "pongo ok y termino");
            activity.setResult(Activity.RESULT_OK);
            unregisterReceiver(mListener);
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

    private void setTextProgress(String text) {
        descripcion.setText(text);
    }

    private void setProgress(Integer porcentaje) {
        horizontalProgressBar.setProgress(porcentaje);
    }

    private void startLoad() {
        indefiniteProgressBar.setVisibility(View.INVISIBLE);
        horizontalProgressBar.setVisibility(View.VISIBLE);
    }

    private void finishLoad() {
        finalizarActivity();
    }

}
