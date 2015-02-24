package com.rukiasoft.androidapps.comunioelpuntal;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.rukiasoft.androidapps.comunioelpuntal.dataclasses.Puntuacion;
import com.rukiasoft.androidapps.comunioelpuntal.utils.ActivityTool;

import java.io.Serializable;
import java.util.List;


public class ScoreFragment extends Fragment implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private ScoreListAdapter mAdapter = null;
    private static final String TAG = "ScoreFragment";
    private List<Puntuacion> puntuaciones;
    private Integer pointsGeneral = 0;
    private Integer rankingGeneral = 0;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i(TAG, "onAttach()");
        if (mAdapter == null) {
            //Log.d(TAG, "creo el adapter");
            mAdapter = new ScoreListAdapter(activity.getApplicationContext());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.score_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        Log.i(TAG, "onActivityCreated() ");
        ListView listView = (ListView) getView().findViewById(R.id.score_list);
        listView.setOnItemClickListener(null);
        if (mAdapter.getCount() == 0)
            loadItems();
        //LayoutInflater inflater = getActivity().getLayoutInflater();
        //View headerView = inflater.inflate(R.layout.score_header, listView, false);
        //listView.addHeaderView(headerView, null, false);
        //listView.setDivider(new ColorDrawable(Color.rgb(1, 129, 86)));
        //listView.setDividerHeight(2);
        TextView pointsView = (TextView) getView().findViewById(R.id.score_header_points_view);
        TextView rankingView = (TextView) getView().findViewById(R.id.score_header_ranking_view);
        pointsView.setText(ActivityTool.getFormatedCurrencyNumber(pointsGeneral));
        rankingView.setText(ActivityTool.getFormatedCurrencyNumber(rankingGeneral));

        listView.setAdapter(mAdapter);

    }

    void loadItems() {
        pointsGeneral = 0;
        rankingGeneral = 0;

        if (puntuaciones == null)
            return;
        for (int i = 0; i < puntuaciones.size(); i++) {
            if(puntuaciones.get(i).getPosicion_general() == null)
                continue;
            ScoreItem item = new ScoreItem();
            item.setJornada(ActivityTool.getRoundNameFromRoundValue(MainActivity.getJornadasJSON(), puntuaciones.get(i).getJornada()));
            item.setPointsGeneral(puntuaciones.get(i).getPuntuacion_general().toString());
            if (puntuaciones.get(i).getPuntuacion_jornada() != null)
                item.setPointsRound(puntuaciones.get(i).getPuntuacion_jornada().toString());
            else
                item.setPointsRound("-");
            item.setPointsGeneral(puntuaciones.get(i).getPuntuacion_general().toString());
            item.setRankingGeneral(puntuaciones.get(i).getPosicion_general().toString());
            item.setRankingRound(puntuaciones.get(i).getPosicion_jornada().toString());
            pointsGeneral = puntuaciones.get(i).getPuntuacion_general();
            rankingGeneral = puntuaciones.get(i).getPosicion_general();
            mAdapter.add(item);
        }
    }

    public void setPuntuaciones(List<Puntuacion> puntuaciones) {
        this.puntuaciones = puntuaciones;
        if (mAdapter != null)
            mAdapter.clear();
    }
}