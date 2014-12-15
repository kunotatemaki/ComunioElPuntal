package com.rukiasoft.androidapps.comunioelpuntal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class TeamPlayersListAdapter extends BaseAdapter {

    @SuppressWarnings("unused")
    private static final String TAG = "BunusListAdapter";
    private List<BonusItem> mItems = new ArrayList<>();
    private final Context mContext;

    public TeamPlayersListAdapter(Context context) {
        mContext = context;
    }

    public void add(BonusItem item) {
        mItems.add(item);
        refresh();
    }

    public void refresh() {
        //Log.d(TAG, "refresh");
        notifyDataSetChanged();
    }

    public void setmItems(List<BonusItem> mItems) {
        this.mItems = mItems;
    }

    public void clear() {
        mItems.clear();
        refresh();
    }

    public void remove(int pos) {
        mItems.remove(pos);
        refresh();
    }

    // Returns the number of ToDoItems

    @Override
    public int getCount() {
        return mItems.size();
    }


    @Override
    public BonusItem getItem(int pos) {
        //Log.d(TAG, "metodo getitem");
        return mItems.get(pos);
    }

    // In this case it's just the position
    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        BonusItem nItem = getItem(position);
        View item = convertView;
        ViewHolder holder;

        if (item == null) {
            //Log.d(TAG, "holder null");
            LayoutInflater inflater = LayoutInflater.from(mContext);
            item = inflater.inflate(R.layout.bonus_item, parent, false);

            holder = new ViewHolder();
            holder.goles = (TextView) item.findViewById(R.id.bonus_goles_view);
            holder.portero = (TextView) item.findViewById(R.id.bonus_portero_view);
            holder.torpeJornada = (TextView) item.findViewById(R.id.bonus_ultimo_jornada_view);
            holder.torpeGeneral = (TextView) item.findViewById(R.id.bonus_ultimo_general_view);
            holder.jornada = (TextView) item.findViewById(R.id.bonus_jornada_view);

            item.setTag(holder);

        } else {
            holder = (ViewHolder) item.getTag();
        }
        holder.goles.setText(nItem.getGoles());
        holder.portero.setText(nItem.getPortero());
        holder.torpeJornada.setText(nItem.getTorpeJornada());
        holder.torpeGeneral.setText(nItem.getTorpeGeneral());
        holder.jornada.setText(nItem.getJornada());
        return (item);

    }

    static class ViewHolder {
        TextView jornada;
        TextView goles;
        TextView portero;
        TextView torpeJornada;
        TextView torpeGeneral;
    }


}
