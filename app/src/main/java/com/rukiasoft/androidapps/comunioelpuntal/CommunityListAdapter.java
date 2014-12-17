package com.rukiasoft.androidapps.comunioelpuntal;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rukiasoft.androidapps.comunioelpuntal.utils.ActivityTool;
import com.rukiasoft.androidapps.comunioelpuntal.utils.ComunioConstants;

import java.util.ArrayList;
import java.util.List;

//import android.util.Log;

class CommunityListAdapter extends BaseAdapter {

    // List of ToDoItems
    private final List<GamerInformation> mItems = new ArrayList<>();
    private final Context mContext;

    public CommunityListAdapter(Context context) {
        mContext = context;
    }

    // Add a MainItem to the adapter
    // Notify observers that the data set has changed

    public void add(GamerInformation item) {
        mItems.add(item);
        refresh();
    }

    void refresh() {
        //Log.d(TAG, "refresh");
        notifyDataSetChanged();
    }

    // Clears the list adapter of all items.

    public void clear() {
        //Log.d(TAG, "metodo clear");
        mItems.clear();
        notifyDataSetChanged();
    }

    public void remove(int pos) {
        //Log.d(TAG, "metodo remove");

        mItems.remove(pos);
        notifyDataSetChanged();
    }

    // Returns the number of ToDoItems

    @Override
    public int getCount() {
        //Log.d(TAG, "metodo count: " + mItems.size());

        return mItems.size();
    }

    // Retrieve the number of ToDoItems

    @Override
    public GamerInformation getItem(int pos) {
        //Log.d(TAG, "metodo getitem");

        return mItems.get(pos);
    }

    // Get the ID for the ToDoItem
    // In this case it's just the position

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    //Create a View to display the ToDoItem
    // at specified position in mItems

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ////////////////////////////////////
        final GamerInformation nItem = getItem(position);

        //item.setLongClickable(true);
        final ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.gamer_item, parent, false);

            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.gamer_item_name_view);
            holder.moneyView = (TextView) convertView.findViewById(R.id.gamer_item_money_view);
            holder.numOfPlayersView = (TextView) convertView.findViewById(R.id.gamer_item_num_of_players_view);
            holder.pointsView = (TextView) convertView.findViewById(R.id.gamer_item_puntos_general_view);
            holder.rankingView = (TextView) convertView.findViewById(R.id.gamer_item_ranking_general_view);
            holder.imagen = (ImageView) convertView.findViewById(R.id.gamer_item_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String ownName = ActivityTool.getStringFromPreferences(mContext, ComunioConstants.PROPERTY_MY_TEAM);
        holder.name.setText(nItem.getParticipante().getNombre());
        if (ownName.compareTo(nItem.getParticipante().getNombre()) == 0) {
            holder.imagen.setImageResource(R.drawable.chicho);
        } else {
            holder.imagen.setImageResource(0);
        }
        holder.moneyView.setText(ActivityTool.getFormatedCurrencyNumber(nItem.getDineroTotal()) + "€");
        if (nItem.getDineroTotal() < 0)
            holder.moneyView.setTextColor(mContext.getResources().getColor(R.color.money_negative));
        else if (nItem.getDineroTotal() < ComunioConstants.MIN_MONEY_GREEN)
            holder.moneyView.setTextColor(mContext.getResources().getColor(R.color.money_positive_under_limit));
        else {
            holder.moneyView.setTextColor(mContext.getResources().getColor(R.color.money_positive));
        }
        holder.numOfPlayersView.setText(nItem.getNumeroJugadores().toString());
        if (nItem.getNumeroJugadores().equals(ComunioConstants.MAX_PLAYERS_TEAM))
            holder.numOfPlayersView.setTextColor(mContext.getResources().getColor(R.color.players_on_limit));
        else if (nItem.getNumeroJugadores() > ComunioConstants.MAX_PLAYERS_TEAM)
            holder.numOfPlayersView.setTextColor(mContext.getResources().getColor(R.color.players_over_limit));
        else {
            holder.numOfPlayersView.setTextColor(mContext.getResources().getColor(R.color.players_ok));
        }
        holder.pointsView.setText(ActivityTool.getFormatedCurrencyNumber(nItem.getPuntosTotales()));
        holder.rankingView.setText(nItem.getCurrentRanking().toString());

        return (convertView);

    }

    static class ViewHolder {
        TextView name;
        TextView moneyView;
        TextView numOfPlayersView;
        TextView pointsView;
        TextView rankingView;
        ImageView imagen;
    }
}
