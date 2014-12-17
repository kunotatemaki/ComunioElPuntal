package com.rukiasoft.androidapps.comunioelpuntal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rukiasoft.androidapps.comunioelpuntal.dataclasses.Puntuacion;
import com.rukiasoft.androidapps.comunioelpuntal.utils.ActivityTool;
import com.rukiasoft.androidapps.comunioelpuntal.utils.ComunioConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GamerFragment extends Fragment implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final String TAG = "GamerFragment";
    private static int currentPosition = 0;
    private ListView lstOpcionesGamer;
    private GamerFragmentSelectionListener listener;
    private GamerInformation gamer;
    private Boolean updateItems = true;

    private AdaptadorGamer mAdapter = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i(TAG, "onAttach()");
        if (mAdapter == null) {
            //Log.d(TAG, "creo el adapter");
            mAdapter = new AdaptadorGamer(this);
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
        return inflater.inflate(R.layout.gamer_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.i(TAG, "Entered onActivityCreated()");
        try {
            listener = (GamerFragmentSelectionListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement SelectionListener");
        }
        lstOpcionesGamer = (ListView) getView().findViewById(R.id.gamer_list);
        lstOpcionesGamer.setAdapter(mAdapter);
        lstOpcionesGamer.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> list, View view, int pos, long id) {
                if (listener != null) {
                    currentPosition = pos;
                    listener.onGamerOptionSelected(
                            (MainItem) lstOpcionesGamer.getAdapter().getItem(pos));
                }
            }
        });

        updateAllFragment();

    }

    public void updateAllFragment() {
        try {
            TextView nombre = (TextView) getView().findViewById(R.id.gamer_item_name_view);
            TextView dineroTotalLabel = (TextView) getView().findViewById(R.id.gamer_item_money_label);
            TextView dineroTotalView = (TextView) getView().findViewById(R.id.gamer_item_money_view);
            TextView numeroJugadoresLabel = (TextView) getView().findViewById(R.id.gamer_item_num_of_players_label);
            TextView numeroJugadoresView = (TextView) getView().findViewById(R.id.gamer_item_num_of_players_view);
            TextView rankingLabel = (TextView) getView().findViewById(R.id.gamer_item_ranking_general_label);
            TextView rankingView = (TextView) getView().findViewById(R.id.gamer_item_ranking_general_view);
            TextView puntosLabel = (TextView) getView().findViewById(R.id.gamer_item_puntos_general_label);
            TextView puntosView = (TextView) getView().findViewById(R.id.gamer_item_puntos_general_view);
            LinearLayout layout = (LinearLayout) getView().findViewById(R.id.gamer_item_layout_total);
            Log.d(TAG, "1");

            nombre.setText(gamer.getParticipante().getNombre());
            nombre.setTextColor(getActivity().getResources().getColor(R.color.text_over_header));
            dineroTotalLabel.setTextColor(getActivity().getResources().getColor(R.color.text_over_header));
            dineroTotalView.setText(ActivityTool.getFormatedCurrencyNumber(gamer.getDineroTotal()) + "€");
            if (gamer.getDineroTotal() < 0)
                dineroTotalView.setTextColor(getActivity().getResources().getColor(R.color.money_negative_over_color_header));
            else if (gamer.getDineroTotal() < ComunioConstants.MIN_MONEY_GREEN)
                dineroTotalView.setTextColor(getActivity().getResources().getColor(R.color.money_positive_under_limit_over_color_header));
            else
                dineroTotalView.setTextColor(getActivity().getResources().getColor(R.color.money_positive_over_color_header));
            numeroJugadoresLabel.setTextColor(getActivity().getResources().getColor(R.color.text_over_header));
            numeroJugadoresView.setText(gamer.getNumeroJugadores().toString());
            if (gamer.getNumeroJugadores().equals(ComunioConstants.MAX_PLAYERS_TEAM))
                numeroJugadoresView.setTextColor(getActivity().getResources().getColor(R.color.players_on_limit_over_color_header));
            else if (gamer.getNumeroJugadores() > ComunioConstants.MAX_PLAYERS_TEAM)
                numeroJugadoresView.setTextColor(getActivity().getResources().getColor(R.color.players_over_limit_over_color_header));
            else
                numeroJugadoresView.setTextColor(getActivity().getResources().getColor(R.color.players_ok_over_color_header));

            if (updateItems)
                loadItems();

            puntosLabel.setTextColor(getActivity().getResources().getColor(R.color.text_over_header));
            puntosView.setTextColor(getActivity().getResources().getColor(R.color.text_over_header));
            puntosView.setText(ActivityTool.getFormatedCurrencyNumber(gamer.getPuntosTotales()));
            rankingLabel.setTextColor(getActivity().getResources().getColor(R.color.text_over_header));
            rankingView.setTextColor(getActivity().getResources().getColor(R.color.text_over_header));
            rankingView.setText(gamer.getCurrentRanking().toString());
            layout.setBackgroundColor(getActivity().getResources().getColor(R.color.color_header));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        if (ActivityTool.isForTablet(getActivity())) {
            if (currentPosition >= 0 && currentPosition < mAdapter.getCount()) {
                lstOpcionesGamer.setItemChecked(currentPosition, true);//mCallback.onItemSelected(mAdapter.getItem(currentPosition));
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        //mAdapter.clear();
    }

    public void resetMarcador() {
        if (null != lstOpcionesGamer)
            lstOpcionesGamer.setItemChecked(0, true);
    }

    void loadItems() {
        mAdapter.clear();
        Log.d(TAG, "actualizo los items");
        mAdapter.add(
                new MainItem(getResources().getString(R.string.my_players),
                        R.drawable.field,
                        new Intent()
                                .putExtra("Name", "MyPlayers")
                                .putExtra("Text", R.string.my_players)
                                .putExtra("Players", (ArrayList<PlayerItem>) gamer.getPlayerItems())
                ));
        mAdapter.add(
                new MainItem(getResources().getString(R.string.signings),
                        R.drawable.signings,
                        new Intent()
                                .putExtra("Name", "Signings")
                                .putExtra("Text", R.string.signings)
                                .putExtra("Players", (ArrayList<PlayerItem>) gamer.getFichajes())
                                .putExtra("Money", gamer.getDineroFichajes())
                ));
        mAdapter.add(
                new MainItem(getResources().getString(R.string.sales),
                        R.drawable.sales,
                        new Intent()
                                .putExtra("Name", "Sales")
                                .putExtra("Text", R.string.sales)
                                .putExtra("Players", (ArrayList<PlayerItem>) gamer.getVentas())
                                .putExtra("Money", gamer.getDineroVentas())
                ));
        mAdapter.add(
                new MainItem(getResources().getString(R.string.remos),
                        R.drawable.remos_reduced,
                        new Intent()
                                .putExtra("Name", "Remos")
                                .putExtra("Text", R.string.remos)
                                .putExtra("Puntuaciones", (ArrayList<Puntuacion>) gamer.getPuntuaciones())
                ));
        mAdapter.add(
                new MainItem(getResources().getString(R.string.bonus),
                        R.drawable.bonus,
                        new Intent()
                                .putExtra("Name", "Bonus")
                                .putExtra("Text", R.string.bonus)
                                .putExtra("Puntuaciones", (ArrayList<Puntuacion>) gamer.getPuntuaciones())
                ));
        mAdapter.add(
                new MainItem(getResources().getString(R.string.balance),
                        R.drawable.scales,
                        new Intent()
                                .putExtra("Name", "Balance")
                                .putExtra("Text", R.string.balance)
                                .putExtra("Gamer", gamer)
                ));
        mAdapter.add(
                new MainItem(getResources().getString(R.string.score),
                        R.drawable.score,
                        new Intent()
                                .putExtra("Name", "Scores")
                                .putExtra("Text", R.string.score)
                                .putExtra("Puntuaciones", (ArrayList<Puntuacion>) gamer.getPuntuaciones())
                ));

	/*	mAdapter.add(
				new MainItem(getResources().getString(R.string.charts_personal), 
				R.drawable.chart2, 
				new Intent()
		.putExtra("Type", "Fragment")		
		.putExtra("Name", "Charts")
		));	*/

        updateItems = false;
        Log.d(TAG, "hecho");

    }

    public GamerInformation getGamer() {
        return gamer;
    }

    public void setGamer(GamerInformation gamer) {
        this.gamer = gamer;
        updateItems = true;
    }

    public List<PlayerItem> getPlayerItemsForTablets() {
        return gamer.getPlayerItems();
    }

    class AdaptadorGamer extends ArrayAdapter<MainItem> {
        final Activity context;

        AdaptadorGamer(Fragment context) {
            super(context.getActivity(), R.layout.main_item);
            this.context = context.getActivity();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final MainItem nItem = getItem(position);
            final ViewHolder holder;

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.main_item, parent, false);

                holder = new ViewHolder();
                holder.text = (TextView) convertView.findViewById(R.id.main_item_view);
                holder.image = (ImageView) convertView.findViewById(R.id.photo_image);
                convertView.setTag(holder);
            } else {
                //Log.d(TAG, "holder NOT null");
                holder = (ViewHolder) convertView.getTag();
            }

            holder.text.setText(nItem.getTitle());
            holder.image.setImageResource(nItem.getPicture());

            return (convertView);

        }
    }

    public interface GamerFragmentSelectionListener {
        void onGamerOptionSelected(MainItem item);
    }

    static class ViewHolder {
        TextView text;
        ImageView image;
    }

}
