package com.rukiasoft.androidapps.comunioelpuntal;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.rukiasoft.androidapps.comunioelpuntal.utils.ActivityTool;

import java.io.Serializable;
import java.util.List;

public class CommunityFragment extends ListFragment implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final String TAG = "CommunityFragment";
    private static int currentPosition = 0;
    private CommunityListAdapter mAdapter = null;

    public interface CommunityFragmentSelectionListener {
        public void onCommunityFragmentItemSelected(GamerInformation item);
    }

    private CommunityFragmentSelectionListener mCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        // Set the list adapter for this ListFragment
        //Log.d(TAG, "onCreate");

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (mAdapter == null) {
            Log.d(TAG, "onAttach() creo el adapter");
            mAdapter = new CommunityListAdapter(activity.getApplicationContext());
        }

        // Make sure that the hosting Activity has implemented
        // the SelectionListener callback interface. We need this
        // because when an item in this ListFragment is selected,
        // the hosting Activity's onItemSelected() method will be called.
        try {
            mCallback = (CommunityFragmentSelectionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement SelectionListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.i(TAG, "Entered onActivityCreated()");
        setListAdapter(mAdapter);
        if (mAdapter.getCount() == 0) {
            Log.d(TAG, "onCreate relleno el adapter");
            try {
                loadItems();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        // When using two-pane layout, configure the ListView to highlight the
        // selected list item

        getListView().setDivider(new ColorDrawable(getResources().getColor(R.color.color_list_divider)));
        getListView().setDividerHeight(2);
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        if (ActivityTool.isForTablet(getActivity())) {
            //Log.d(TAG, "is in two pane mode, pongo a single: " + currentPosition);
            //getListView().setSelector(R.drawable.menu_item_background_selector);

            if (currentPosition >= 0 && currentPosition < mAdapter.getCount()) {
                getListView().setItemChecked(currentPosition, true);//mCallback.onItemSelected(mAdapter.getItem(currentPosition));
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
    }

    @Override
    public void onListItemClick(ListView l, View view, int position, long id) {

        // Notify the hosting Activity that a selection has been made.
        //Log.d(TAG, "onListItemClick: " + position);
        currentPosition = position;

        mCallback.onCommunityFragmentItemSelected(mAdapter.getItem(position));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        mAdapter.clear();
    }

    private void loadItems() {

        List<GamerInformation> gamers = MainActivity.getGamers();
        if (mAdapter == null) {
            mAdapter = new CommunityListAdapter(getActivity().getApplicationContext());
            setListAdapter(mAdapter);
        }
        for (int i = 0; i < gamers.size(); i++) {
            GamerInformation gamer = gamers.get(i);
            mAdapter.add(gamer);
        }
    }

}
