package com.rukiasoft.androidapps.comunioelpuntal;


import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ListFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.io.Serializable;
import java.util.List;


public class PlayersFragment extends ListFragment implements OnQueryTextListener, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    PlayersListAdapter mAdapter = null;
    private static final String TAG = "PlayersFragment";

    public void createAdapter() {
        if (mAdapter == null) {
            Log.d(TAG, "creo el adapter, constructor");
            mAdapter = new PlayersListAdapter();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i(TAG, "onAttach()");
        if (mAdapter == null) {
            Log.d(TAG, "creo el adapter onAtach");
            mAdapter = new PlayersListAdapter();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        Log.d(TAG, "onCreate setListAdapter");


    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String playerName = mAdapter.getItem(position).getName();
        DialogFragment newFragment = PlayerSigningDialogFragment.newInstance(playerName);
        newFragment.show(getFragmentManager(), "player_dialog");
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        Log.i(TAG, "onActivityCreated() ");

        setListAdapter(mAdapter);
        getListView().setDivider(new ColorDrawable(getResources().getColor(R.color.color_list_divider)));
        getListView().setDividerHeight(2);
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mAdapter.refresh();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.player_menu, menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        //SearchView searchView = (SearchView) menu.findItem(R.id.menu_item_search).getActionView();
        MenuItem mSearchMenuItem = menu.findItem(R.id.menu_item_search);
        SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(mSearchMenuItem);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setOnQueryTextListener(this);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = MainActivity.ismDrawerOpen();
        menu.findItem(R.id.menu_item_search).setVisible(!drawerOpen);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_search:
                getActivity().onSearchRequested();
                return true;
            case R.id.menu_sort_name:
                mAdapter.orderByName();
                return true;
            case R.id.menu_sort_team:
                mAdapter.orderByTeam();
                return true;
            case R.id.menu_sort_owner:
                mAdapter.orderByOwner();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.getFilter().filter(newText);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    public void clearAdapter() {
        mAdapter.clear();
    }

    public void addItem(PlayerItem item) {
        mAdapter.add(item);
    }

    public void refresh() {
        mAdapter.refresh();
    }

    public Boolean hasInformation() {
        return mAdapter != null && mAdapter.getTotalItems() != 0;
    }

    public List<PlayerItem> getPlayerItems() {
        return mAdapter.getmItems();
    }

    public void setPlayerItems(List<PlayerItem> players) {
        mAdapter.setmItems(players);
        mAdapter.refresh();
    }
}