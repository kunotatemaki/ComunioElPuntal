package com.rukiasoft.androidapps.comunioelpuntal;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;

import com.rukiasoft.androidapps.comunioelpuntal.utils.ActivityTool;

public class BalanceFragment extends Fragment implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final String TAG = "BalanceFragment";
    public static int currentPosition = 0;
    private GamerInformation gamer;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i(TAG, "onAttach()");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.balance_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.i(TAG, "Entered onActivityCreated()");


        TextView ingresosTotales = (TextView) getActivity().findViewById(R.id.balance_header_ingresos_view);
        TextView ingresosPuntos = (TextView) getActivity().findViewById(R.id.balance_ingresos_puntos_view);
        TextView ingresosVentas = (TextView) getActivity().findViewById(R.id.balance_ingresos_ventas_view);
        TextView ingresosBonus = (TextView) getActivity().findViewById(R.id.balance_ingresos_bonus_view);

        TextView gastosTotales = (TextView) getActivity().findViewById(R.id.balance_header_gastos_view);
        TextView gastosFichajes = (TextView) getActivity().findViewById(R.id.balance_gastos_fichajes_view);
        TextView gastosRemos = (TextView) getActivity().findViewById(R.id.balance_gastos_remos_view);

        Integer puntos = gamer.getDineroPuntos();
        ingresosPuntos.setText(ActivityTool.getFormatedCurrencyNumber(puntos) + " €");
        Integer ventas = gamer.getDineroVentas();
        ingresosVentas.setText(ActivityTool.getFormatedCurrencyNumber(ventas) + " €");
        Integer primas = gamer.getDineroPrimasGeneral() + gamer.getDineroPrimasJornada()
                + gamer.getDineroPrimasGoles() + gamer.getDineroPrimasPortero();
        ingresosBonus.setText(ActivityTool.getFormatedCurrencyNumber(primas) + " €");
        Integer incomes = puntos + ventas + primas;
        ingresosTotales.setText(ActivityTool.getFormatedCurrencyNumber(incomes) + " €");
        Integer fichajes = gamer.getDineroFichajes();
        gastosFichajes.setText(ActivityTool.getFormatedCurrencyNumber(fichajes) + " €");
        Integer remos = gamer.getDineroRemoEquipos() + gamer.getDineroRemoJugadores() + gamer.getDineroRemoTrupita();
        gastosRemos.setText(ActivityTool.getFormatedCurrencyNumber(remos) + " €");
        Integer expenses = remos + fichajes;
        gastosTotales.setText(ActivityTool.getFormatedCurrencyNumber(expenses) + " €");


    }


    public void setGamer(GamerInformation gamer) {
        this.gamer = gamer;
    }

}
