package com.rukiasoft.androidapps.comunioelpuntal;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.rukiasoft.androidapps.comunioelpuntal.dataclasses.DatabaseHandler;

import java.io.Serializable;
import java.util.List;


public class NotificationFragment extends ListFragment implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private NotificationListAdapter mAdapter = null;
    private static final String TAG = "NotificationFragment";
    private DatabaseHandler dbHandler = null;
    private final NotificationOpenReceiver mReceiver = new NotificationOpenReceiver(this);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //setRetainInstance(true);
        //Log.d(TAG, "onCreate setListAdapter");
        if (mAdapter == null)
            mAdapter = new NotificationListAdapter(getActivity());
        if (dbHandler == null)
            dbHandler = new DatabaseHandler(getActivity());
        setListAdapter(mAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(R.string.detailed_notification_description);
        alertDialogBuilder.setMessage(mAdapter.getItem(position).getMessage());
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                mAdapter.refresh();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        setItemRead(position);
        // show alert
        alertDialog.show();
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        Log.i(TAG, "onActivityCreated() ");
        getListView().setDivider(new ColorDrawable(getResources().getColor(R.color.color_list_divider_notification)));
        getListView().setDividerHeight(2);
        getListView().setBackgroundColor(getResources().getColor(R.color.color_header));
        loadItems();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.notification_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = MainActivity.ismDrawerOpen();
        menu.findItem(R.id.menu_delete_notifications).setVisible(!drawerOpen);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {
            case R.id.menu_delete_notifications:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle(R.string.confirmation);
                alertDialogBuilder.setMessage(R.string.confirm_delete_notifications);
                // set positive button: Yes message
                alertDialogBuilder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        deleteItemsDataBase();
                    }
                });
                alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show alert
                alertDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        IntentFilter intentFilter = new IntentFilter("ruler.elpuntal.comunio.androidapp.NOTIFICATION");
        intentFilter.setPriority(3);
        getActivity().registerReceiver(mReceiver, intentFilter);
        NotNotificationOpenService.restartCounter();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        getActivity().unregisterReceiver(mReceiver);
    }

    public void loadItems() {
        if (dbHandler == null) {
            //Log.d(TAG, "no estaba creado el lector de base de datos");
            return;
        }
        List<NotificationItem> notifications = dbHandler.loadNotifications();
        if (mAdapter == null) {
            //Log.d(TAG, "no estaba creado el adapter");
            return;
        } else
            mAdapter.clear();
        for (int i = 0; i < notifications.size(); i++) {
            mAdapter.add(notifications.get(i));
            mAdapter.refresh();
        }
    }

    private void setItemRead(int position) {
        int id = mAdapter.getItem(position).getID();
        dbHandler.setNotificationItemRead(id);
        mAdapter.getItem(position).alreadyRead();
    }

    void deleteItemsDataBase() {

        if (mAdapter != null)
            mAdapter.clear();
        dbHandler.deleteNotificationItemsDataBase();
    }


}