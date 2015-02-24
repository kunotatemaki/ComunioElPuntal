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

import java.io.Serializable;
import java.util.List;

import com.rukiasoft.androidapps.comunioelpuntal.dataclasses.Puntuacion;
import com.rukiasoft.androidapps.comunioelpuntal.utils.ActivityTool;
import com.rukiasoft.androidapps.comunioelpuntal.utils.ComunioConstants;


public class BonusFragment extends Fragment implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private BonusListAdapter mAdapter = null;
    private static final String TAG = "BonusFragment";
    private List<Puntuacion> puntuaciones;
    private Integer totalMoneyGoles = 0;
    private Integer totalMoneyPortero = 0;
    private Integer totalMoneyTorpeJornada = 0;
    private Integer totalMoneyTorpeGeneral = 0;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i(TAG, "onAttach()");
        if (mAdapter == null) {
            //Log.d(TAG, "creo el adapter");
            mAdapter = new BonusListAdapter(activity.getApplicationContext());
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
        return inflater.inflate(R.layout.bonus_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        Log.i(TAG, "onActivityCreated() ");
        ListView listView = (ListView) getView().findViewById(R.id.bonus_list);
        if(listView != null) {
            listView.setAdapter(mAdapter);
            if (mAdapter.getCount() == 0)
                loadItems();
            TextView golesView = (TextView) getActivity().findViewById(R.id.bonus_header_goles_view);
            TextView porteroView = (TextView) getActivity().findViewById(R.id.bonus_header_portero_view);
            TextView torpeJornadaView = (TextView) getActivity().findViewById(R.id.bonus_header_ultimo_jornada_view);
            TextView torpeGeneralView = (TextView) getActivity().findViewById(R.id.bonus_header_ultimo_general_view);
            TextView total = (TextView) getActivity().findViewById(R.id.bonus_header_view);
            golesView.setText(ActivityTool.getFormatedCurrencyNumber(totalMoneyGoles) + "€");
            porteroView.setText(ActivityTool.getFormatedCurrencyNumber(totalMoneyPortero) + "€");
            torpeJornadaView.setText(ActivityTool.getFormatedCurrencyNumber(totalMoneyTorpeJornada) + "€");
            torpeGeneralView.setText(ActivityTool.getFormatedCurrencyNumber(totalMoneyTorpeGeneral) + "€");
            total.setText(ActivityTool.getFormatedCurrencyNumber(totalMoneyGoles + totalMoneyPortero +
                    totalMoneyTorpeJornada + totalMoneyTorpeGeneral) + "€");
        }

    }

    void loadItems() {
        totalMoneyGoles = 0;
        totalMoneyPortero = 0;
        totalMoneyTorpeJornada = 0;
        totalMoneyTorpeGeneral = 0;
        if (puntuaciones == null)
            return;
        for (int i = 0; i < puntuaciones.size(); i++) {
            if(puntuaciones.get(i).getPuntuacion_general() == null)
                continue;
            BonusItem item = new BonusItem();
            item.setJornada(ActivityTool.getRoundNameFromRoundValue(MainActivity.getJornadasJSON(), puntuaciones.get(i).getJornada()));
            Integer publicado = 0;
            if(puntuaciones.get(i).getPublicado() && puntuaciones.get(i).getPuntuacion_jornada() != null)
                publicado = 1;
            totalMoneyGoles += publicado * puntuaciones.get(i).getGoles() * MainActivity.getdbHandler().getOption(ComunioConstants.BONUS_GOAL);
            item.setGoles(ActivityTool.getFormatedCurrencyNumber(publicado * puntuaciones.get(i).getGoles() * MainActivity.getdbHandler().getOption(ComunioConstants.BONUS_GOAL)) + "€");

            if (puntuaciones.get(i).getPortero()) {
                totalMoneyPortero += publicado * MainActivity.getdbHandler().getOption(ComunioConstants.BONUS_GOALKEEPER);
                item.setPortero(ActivityTool.getFormatedCurrencyNumber(publicado * MainActivity.getdbHandler().getOption(ComunioConstants.BONUS_GOALKEEPER)) + "€");
            } else
                item.setPortero("0€");

            if (puntuaciones.get(i).getPrima_jornada()) {
                totalMoneyTorpeJornada += MainActivity.getdbHandler().getOption(ComunioConstants.BONUS_LAST_IN_ROUND);
                item.setTorpeJornada(ActivityTool.getFormatedCurrencyNumber(MainActivity.getdbHandler().getOption(ComunioConstants.BONUS_LAST_IN_ROUND)) + "€");
            } else
                item.setTorpeJornada("0€");

            if (puntuaciones.get(i).getPrima_general().equals(ComunioConstants.CODIGO_SI_COBRA_PRIMA)) {
                totalMoneyTorpeGeneral += MainActivity.getdbHandler().getOption(ComunioConstants.BONUS_LAST_IN_CLASSIFICATION);
                item.setTorpeGeneral(ActivityTool.getFormatedCurrencyNumber(MainActivity.getdbHandler().getOption(ComunioConstants.BONUS_LAST_IN_CLASSIFICATION)) + "€");
            } else if (puntuaciones.get(i).getPrima_general().equals(ComunioConstants.CODIGO_NO_COBRA_PRIMA))
                item.setTorpeGeneral("0€");
            else if (puntuaciones.get(i).getPrima_general().equals(ComunioConstants.CODIGO_NO_COBRA_PRIMA_POR_REITERACION))
                item.setTorpeGeneral(getActivity().getResources().getString(R.string.ultimo_general_not_allowed));
            mAdapter.add(item);
        }
    }

    public void setPuntuaciones(List<Puntuacion> puntuaciones) {
        this.puntuaciones = puntuaciones;
        if (mAdapter != null)
            mAdapter.clear();
    }
}