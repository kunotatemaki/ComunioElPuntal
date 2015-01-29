package com.rukiasoft.androidapps.comunioelpuntal;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.rukiasoft.androidapps.comunioelpuntal.utils.ActivityTool;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class ClassificationFragment extends Fragment implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final String TAG = "ClassificationFragment";
    private final List<GamerInformation> participantes = new ArrayList<>();
    private ClassificationListAdapter mAdapter = null;
    private Comparator<GamerInformation> comparator = null;
    private final ArrayList<String> datosSpinner = new ArrayList<>();
    private Integer index = 0;


    public static enum OrderType {
        GENERAL, LAST_ROUND, ROUND
    }

    private OrderType order = OrderType.GENERAL;
    private Double selectedRound = (double) 0;

    public interface ClassificationFragmentSelectionListener {
        public void onClassificationFragmentItemSelected(GamerInformation item);
    }

    private ClassificationFragmentSelectionListener mCallback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i(TAG, "onAttach()");
        if (mAdapter == null) {
            mAdapter = new ClassificationListAdapter(getActivity());
        }

        try {
            mCallback = (ClassificationFragmentSelectionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement SelectionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        return inflater.inflate(R.layout.classification_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.i(TAG, "Entered onActivityCreated(): " + order);
        Spinner jornada = (Spinner) getView().findViewById(R.id.classification_spinner);
        TextView label = (TextView) getView().findViewById(R.id.classification_label);
        ListView listGamers = (ListView) getView().findViewById(R.id.classification_list);
        listGamers.setAdapter(mAdapter);
        listGamers.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listGamers.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                mCallback.onClassificationFragmentItemSelected(mAdapter.getItem(position).getGamerInformation());
            }
        });


        if (mAdapter.getCount() == 0)
            loadItems();
        label.setText(getResources().getString(R.string.classification));
        if (order == OrderType.ROUND) {
            jornada.setVisibility(View.VISIBLE);
        } else {
            jornada.setVisibility(View.INVISIBLE);
            return;
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getActivity(),
                        R.layout.spinner_main_item, datosSpinner);
        /*ArrayAdapter<String> adaptador =
                new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_item, datosSpinner);

        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/
        jornada.setAdapter(adapter);
        jornada.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, android.view.View v,
                                               int position, long id) {
                        selectedRound = ActivityTool.getRoundValueFromRoundName(MainActivity.getJornadasJSON(), datosSpinner.get(position));
                        //Log.d(TAG, "selectedRound: " + selectedRound);
                        refresh();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }

                });
        jornada.setSelection(0);

    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        //mAdapter.clear();
    }

    void loadItems() {
        Log.d(TAG, "actualizo los items");
        if (mAdapter == null) {
            //Log.d(TAG,  "era null");
            mAdapter = new ClassificationListAdapter(getActivity());
        } else {
            //Log.d(TAG,  "No era null");
            mAdapter.clear();
        }
        //Log.d(TAG, "hay: " + mAdapter.getCount());
        Collections.sort(participantes, comparator);
        for (int i = 0; i < participantes.size(); i++) {
            Integer posicion = i + 1;
            ClassificationItem item = new ClassificationItem();
            item.setPosition(posicion.toString());
            item.setName(participantes.get(i).getParticipante().getNombre());
            if (order == OrderType.GENERAL) {
                item.setPoints(ActivityTool.getFormatedCurrencyNumber(participantes.get(i).getPuntosTotales()));
            } else {
                Integer localIndex = 0;
                if (order == OrderType.LAST_ROUND)
                    localIndex = participantes.get(i).getPuntuaciones().size() - 1;
                else if (order == OrderType.ROUND)
                    localIndex = index;
                Integer points = participantes.get(i).getPuntuaciones().get(localIndex).getPuntuacion_jornada();
                if (points != null)
                    item.setPoints(ActivityTool.getFormatedCurrencyNumber(points));
                else
                    item.setPoints("-");
            }
            item.setGamerInformation(participantes.get(i));
            mAdapter.add(item);
        }
        //Log.d(TAG, "hay: " + mAdapter.getCount());

    }

    public void setGamerList(List<GamerInformation> gamers, OrderType order) {


        participantes.clear();
        for (int i = 0; i < gamers.size(); i++) {
            participantes.add(gamers.get(i));
        }


        this.order = order;
        if (order == OrderType.GENERAL) {
            comparator = new GeneralComparator();
        } else if (order == OrderType.LAST_ROUND) {
            comparator = new LastRoundComparator();
        } else if (order == OrderType.ROUND) {
            JSONObject jornadasJSON = MainActivity.getJornadasJSON();
            Iterator<?> keys = jornadasJSON.keys();
            List<Double> valores = new ArrayList<>();
            while( keys.hasNext() ){
                String key = (String)keys.next();
                try {
                    valores.add(jornadasJSON.getDouble(key));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Collections.sort(valores);

            datosSpinner.clear();
            if (gamers.size() > 0) {
                for (int i = 0; i < valores.size(); i++) {
                    datosSpinner.add(ActivityTool.getRoundNameFromRoundValue(jornadasJSON, valores.get(i)));
                }
            }
            comparator = new RoundComparator();
        } else {
            this.order = OrderType.GENERAL;
            comparator = new GeneralComparator();
        }
    }


    private void refresh() {
        //Collections.sort(this.participantes, this.comparator);
        Log.d(TAG, "refresh");
        loadItems();
    }

    private class GeneralComparator implements java.util.Comparator<GamerInformation> {
        @Override
        public int compare(GamerInformation p1, GamerInformation p2) {
            return p1.getCurrentRanking().compareTo(p2.getCurrentRanking());
        }
    }

    private class LastRoundComparator implements java.util.Comparator<GamerInformation> {
        @Override
        public int compare(GamerInformation p1, GamerInformation p2) {
            return p1.getPuntuaciones().get(p1.getPuntuaciones().size() - 1).getPosicion_jornada().compareTo(
                    p2.getPuntuaciones().get(p2.getPuntuaciones().size() - 1).getPosicion_jornada());
        }
    }

    private class RoundComparator implements java.util.Comparator<GamerInformation> {
        @Override
        public int compare(GamerInformation p1, GamerInformation p2) {
            //busco la jornada
            Integer size;
            if (p1.getPuntuaciones().size() <= p2.getPuntuaciones().size())
                size = p1.getPuntuaciones().size();
            else
                size = p2.getPuntuaciones().size();

            for (int i = 0; i < size; i++) {
                if (p1.getPuntuaciones().get(i).getJornada().compareTo(selectedRound) == 0) {
                    index = i;
                    break;
                }
            }

            return p1.getPuntuaciones().get(index).getPosicion_jornada().compareTo(
                    p2.getPuntuaciones().get(index).getPosicion_jornada());
        }
    }

}
