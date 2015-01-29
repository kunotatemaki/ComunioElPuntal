package com.rukiasoft.androidapps.comunioelpuntal;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.rukiasoft.androidapps.comunioelpuntal.dataclasses.Puntuacion;
import com.rukiasoft.androidapps.comunioelpuntal.utils.ActivityTool;
import com.rukiasoft.androidapps.comunioelpuntal.utils.ComunioConstants;

import java.io.Serializable;
import java.util.List;


public class RemosFragment extends Fragment implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final String TAG = "RemosFragment";
    private List<Puntuacion> puntuaciones;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.remos_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        Log.i(TAG, "onActivityCreated() ");
        int layout = android.R.layout.simple_list_item_activated_1;
        ListView listView = (ListView) getView().findViewById(R.id.remos_list);
        listView.setAdapter(new ArrayAdapter<Spanned>(getActivity(), layout));
        listView.setOnItemClickListener(null);
        TextView textCabecera = (TextView) getView().findViewById(R.id.remos_header);
        Integer sancion = loadItems(listView);
        textCabecera.setText(ActivityTool.getFormatedCurrencyNumber(sancion) + "€");


    }

    @SuppressWarnings("unchecked")
    Integer loadItems(ListView listView) {
        Integer money = 0;
        if (puntuaciones == null)
            return money;
        String patron = getActivity().getApplicationContext().getResources().getString(R.string.remos_pattern);
        for (int i = 0; i < puntuaciones.size(); i++) {
            String precio;
            if (puntuaciones.get(i).getRemo_jugadores()) {
                money += MainActivity.getdbHandler().getOption(ComunioConstants.REMO_MAX_PLAYERS);
                precio = ActivityTool.getFormatedCurrencyNumber(MainActivity.getdbHandler().getOption(ComunioConstants.REMO_MAX_PLAYERS));
                String frase = patron.replace("_sancion_", "<font color='darkred'>" + precio + "</font>");
                frase = frase.replace("€", "<font color='darkred'>€</font>");
                frase = frase.replace("_motivo_", "<font color='blue'>" + ComunioConstants.TIPOS_SANCIONES[0] + "</font>");
                frase = frase.replace("_jornada_", "" + ActivityTool.getRoundNameFromRoundValue(MainActivity.getJornadasJSON(), puntuaciones.get(i).getJornada()) + "");
                ((ArrayAdapter<Spanned>) listView.getAdapter()).add(Html.fromHtml("<b>" + frase + "</b>"));
            }
            if (puntuaciones.get(i).getRemo_equipo()) {
                money += MainActivity.getdbHandler().getOption(ComunioConstants.REMO_MAX_TEAMS);
                precio = ActivityTool.getFormatedCurrencyNumber(MainActivity.getdbHandler().getOption(ComunioConstants.REMO_MAX_TEAMS));
                String frase = patron.replace("_sancion_", "<font color='darkred'>" + precio + "</font>");
                frase = frase.replace("€", "<font color='darkred'>€</font>");
                frase = frase.replace("_motivo_", "<font color='blue'>" + ComunioConstants.TIPOS_SANCIONES[1] + "</font>");
                frase = frase.replace("_jornada_", "" + ActivityTool.getRoundNameFromRoundValue(MainActivity.getJornadasJSON(), puntuaciones.get(i).getJornada()) + "");
                ((ArrayAdapter<Spanned>) listView.getAdapter()).add(Html.fromHtml("<b>" + frase + "</b>"));
            }
            if (puntuaciones.get(i).getRemo_trupita()) {
                money += MainActivity.getdbHandler().getOption(ComunioConstants.REMO_TRUPITAS);
                precio = ActivityTool.getFormatedCurrencyNumber(MainActivity.getdbHandler().getOption(ComunioConstants.REMO_TRUPITAS));
                String frase = patron.replace("_sancion_", "<font color='darkred'>" + precio + "</font>");
                frase = frase.replace("€", "<font color='darkred'>€</font>");
                frase = frase.replace("_motivo_", "<font color='blue'>" + ComunioConstants.TIPOS_SANCIONES[2] + "</font>");
                frase = frase.replace("_jornada_", "" + ActivityTool.getRoundNameFromRoundValue(MainActivity.getJornadasJSON(), puntuaciones.get(i).getJornada()) + "");
                ((ArrayAdapter<Spanned>) listView.getAdapter()).add(Html.fromHtml("<b>" + frase + "</b>"));
            }

        }
        if (listView.getAdapter().getCount() == 0)
            ((ArrayAdapter<Spanned>) listView.getAdapter()).add(Html.fromHtml("<b>" + getActivity().getResources().getString(R.string.no_remos_pattern) + "</b>"));
        return money;
    }

    public void setPuntuaciones(List<Puntuacion> puntuaciones) {
        this.puntuaciones = puntuaciones;

    }
}