package com.rukiasoft.androidapps.comunioelpuntal;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;


public class TeamPlayersFragment extends Fragment implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    PlayersListAdapter mAdapter = null;
    private static final String TAG = "TeamPlayersFragment";
    String cabecera = "";
    TextView textCabecera;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i(TAG, "onAttach()");
        if (mAdapter == null) {
            mAdapter = new PlayersListAdapter();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.team_player_fragment, container, false);
    }


    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        Log.i(TAG, "onActivityCreated() ");

        ListView listView = (ListView) getView().findViewById(R.id.team_players_list);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                String playerName = mAdapter.getItem(position).getName();
                DialogFragment newFragment = PlayerSigningDialogFragment.newInstance(playerName);
                newFragment.show(getFragmentManager(), "player_dialog");
            }
        });
        textCabecera = (TextView) getView().findViewById(R.id.team_players_header);

        textCabecera.setText(cabecera);


        mAdapter.orderByPosition();
        mAdapter.refresh();

    }

    public void setPlayerList(Context context, List<PlayerItem> playerItems) {
        if (mAdapter == null) {
            mAdapter = new PlayersListAdapter();
        }
        mAdapter.clear();    //lo borro y actualizo
        for (int i = 0; i < playerItems.size(); i++) {
            mAdapter.add(playerItems.get(i));
        }
    }

    public void setCabeceraView(String header) {
        if (textCabecera != null) {
            textCabecera.setText(header);
        }
        cabecera = header;
    }
}