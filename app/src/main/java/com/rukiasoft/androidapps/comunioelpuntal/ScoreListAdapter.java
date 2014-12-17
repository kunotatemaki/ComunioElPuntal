package com.rukiasoft.androidapps.comunioelpuntal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


class ScoreListAdapter extends BaseAdapter {

    @SuppressWarnings("unused")
    private static final String TAG = "ScoreListAdapter";
    private List<ScoreItem> mItems = new ArrayList<>();
    private final Context mContext;

    public ScoreListAdapter(Context context) {
        mContext = context;
    }

    public void add(ScoreItem item) {
        mItems.add(item);
        refresh();
    }

    void refresh() {
        //Log.d(TAG, "refresh");
        notifyDataSetChanged();
    }

    public void setmItems(List<ScoreItem> mItems) {
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
    public ScoreItem getItem(int pos) {
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

        ScoreItem nItem = getItem(position);
        View item = convertView;
        ViewHolder holder;

        if (item == null) {
            //Log.d(TAG, "holder null");
            LayoutInflater inflater = LayoutInflater.from(mContext);
            item = inflater.inflate(R.layout.score_item, parent, false);

            holder = new ViewHolder();
            holder.rankingRound = (TextView) item.findViewById(R.id.score_ranking_round_view);
            holder.rankingGeneral = (TextView) item.findViewById(R.id.score_ranking_general_view);
            holder.pointsGeneral = (TextView) item.findViewById(R.id.score_points_general_view);
            holder.pointsRound = (TextView) item.findViewById(R.id.score_points_round_view);
            holder.jornada = (TextView) item.findViewById(R.id.score_jornada_view);

            item.setTag(holder);

        } else {
            holder = (ViewHolder) item.getTag();
        }
        holder.rankingGeneral.setText(nItem.getRankingGeneral());
        holder.rankingRound.setText(nItem.getRankingRound());
        holder.pointsGeneral.setText(nItem.getPointsGeneral());
        holder.pointsRound.setText(nItem.getPointsRound());
        holder.jornada.setText(nItem.getJornada());
        return (item);

    }

    static class ViewHolder {
        TextView rankingRound;
        TextView rankingGeneral;
        TextView pointsRound;
        TextView pointsGeneral;
        TextView jornada;
    }


}
