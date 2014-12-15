package com.rukiasoft.androidapps.comunioelpuntal;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;

import com.rukiasoft.androidapps.comunioelpuntal.utils.ActivityTool;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;


/**
 * Created by Ruler on 2014.
 */
public class SettingsActivity extends ActionBarActivity {
    //private static String TAG = "SettingsActivity";
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_activity);

        if (!ActivityTool.isForTablet(this))    //una sola pantalla
            ActivityTool.setOrientation(this, MainActivity.Orientation.PORTRAIT);
        else
            ActivityTool.setOrientation(this, MainActivity.Orientation.LANDSCAPE);

        mToolbar = (Toolbar) this.findViewById(R.id.toolbar);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<b>" + getSupportActionBar().getTitle() + "</b>"));
        }
        SmoothProgressBar mProgressBar = (SmoothProgressBar) findViewById(R.id.progress_bar);
        mProgressBar.progressiveStop();
        ActivityTool.HideProgress(mProgressBar, this);
        //setSupportActionBar(MainActivity.getToolbar());
        getFragmentManager().beginTransaction()
                .replace(R.id.settings_fragment_container, new SettingsFragment())
                .commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //Log.d(TAG, "presiono back y vuelvo");
        setResult(RESULT_OK);
        finish();
    }
}
