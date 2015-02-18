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
    private final List<ClassificationItem> participantesMostrados = new ArrayList<>();
    private ClassificationListAdapter mAdapter = null;
    private Comparator<ClassificationItem> comparator = null;
    private final ArrayList<String> datosSpinner = new ArrayList<>();
    private Integer index = 0;
    private List<Double> valoresJornadas = null;


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

        if (order == OrderType.GENERAL || order == OrderType.LAST_ROUND) {
            selectedRound = ActivityTool.getValorJornadaActual(MainActivity.getContext());
        }

        participantesMostrados.clear();
        Integer points = 0;
        for (int i = 0; i < MainActivity.getGamers().size(); i++) {
            ClassificationItem item = new ClassificationItem();
            if(selectedRound.doubleValue() == 0.0){
                item.setName(MainActivity.getGamers().get(i).getParticipante().getNombre());
                item.setPoints("-");
                item.setGamerInformation(MainActivity.getGamers().get(i));
                Integer pos = i+1;
                item.setPosition(pos.toString());
                mAdapter.add(item);
            }else if (MainActivity.getGamers().get(i).getParticipante().getJ_final() >= selectedRound &&
                    MainActivity.getGamers().get(i).getParticipante().getJ_inicio() <= selectedRound) {
                item.setName(MainActivity.getGamers().get(i).getParticipante().getNombre());
                if (order == OrderType.GENERAL) {
                    points = MainActivity.getGamers().get(i).getPuntosTotales();
                }else {
                    for (int j = 0; j < MainActivity.getGamers().get(i).getPuntuaciones().size(); j++) {
                        if (MainActivity.getGamers().get(i).getPuntuaciones().get(j).getJornada().compareTo(selectedRound) == 0) {
                            points = MainActivity.getGamers().get(i).getPuntuaciones().get(j).getPuntuacion_jornada();
                            break;
                        }
                    }
                }
                if(points != null)
                    item.setPoints(points.toString());
                else
                    item.setPoints("-");
                item.setGamerInformation(MainActivity.getGamers().get(i));
                participantesMostrados.add(item);
            }
        }

        if(selectedRound == null)
            return;

        Collections.sort(participantesMostrados, comparator);


        for (int i = 0; i < participantesMostrados.size(); i++) {
            Integer position = i+1;
            participantesMostrados.get(i).setPosition(position.toString());
            if(participantesMostrados.get(i).getPoints().compareTo("-") != 0)
                participantesMostrados.get(i).setPoints(ActivityTool.getFormatedCurrencyNumber(Integer.parseInt(participantesMostrados.get(i).getPoints())));
            mAdapter.add(participantesMostrados.get(i));
        }
        //Log.d(TAG, "hay: " + mAdapter.getCount());

    }

    public void setGamerList(OrderType order) {

        this.order = order;
        if (order == OrderType.ROUND) {
            setSpinner();
        }
        comparator = new ClassificationComparator();
    }

    private void setSpinner(){
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
        if (MainActivity.getGamers().size() > 0) {
            for (int i = 0; i < valores.size(); i++) {
                datosSpinner.add(ActivityTool.getRoundNameFromRoundValue(jornadasJSON, valores.get(i)));
            }
        }
    }

    private void refresh() {
        Log.d(TAG, "refresh");
        loadItems();
    }

    public void resetFragment(){
        setSpinner();
        refresh();
    }

    private class ClassificationComparator implements java.util.Comparator<ClassificationItem> {
        @Override
        public int compare(ClassificationItem p1, ClassificationItem p2) {
            Integer v1=0, v2=0;
            try {
                v1 = Integer.parseInt(p1.getPoints());
            }catch(NumberFormatException e){
                return 1;
            }
            try {
                v2 = Integer.parseInt(p2.getPoints());
            }catch(NumberFormatException e){
                return -1;
            }
            return v2.compareTo(v1);
        }
    }




}
