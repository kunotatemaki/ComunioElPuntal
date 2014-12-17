package com.rukiasoft.androidapps.comunioelpuntal;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;

import com.rukiasoft.androidapps.comunioelpuntal.utils.ActivityTool;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class NotificationActivity extends ActionBarActivity {

    // Add a ToDoItem Request Code
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
    private static final String TAG = "NotificationActivity";
    private NotificationFragment fNotification;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_activity);
        // Create a new TodoListAdapter for this ListActivity's ListView
        if (!ActivityTool.isForTablet(this))    //una sola pantalla
            ActivityTool.setOrientation(this, MainActivity.Orientation.PORTRAIT);
        else
            ActivityTool.setOrientation(this, MainActivity.Orientation.LANDSCAPE);

        Toolbar mToolbar = (Toolbar) this.findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<b>" + getSupportActionBar().getTitle() + "</b>"));
        }
        SmoothProgressBar mProgressBar = (SmoothProgressBar) findViewById(R.id.progress_bar);
        mProgressBar.progressiveStop();
        ActivityTool.HideProgress(mProgressBar, this);

        fNotification = new NotificationFragment();

        fNotification = new NotificationFragment();
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.notification_fragment_container, fNotification);
        mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        mFragmentTransaction.commit();
        mFragmentManager.executePendingTransactions();
        load();

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        //outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar().getSelectedNavigationIndex());
    }

    // Do not modify below here

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
    }

    void load() {
        //Log.d(TAG, "load");
        fNotification.loadItems();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //Log.d(TAG, "onNewIntent");
        load();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
            case R.id.menu_delete_notifications:
                fNotification.onOptionsItemSelected(item);
        }
        return true;
    }

}