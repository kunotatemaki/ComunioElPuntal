package com.rukiasoft.androidapps.comunioelpuntal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

//import android.util.Log;


class ClassificationListAdapter extends BaseAdapter {

    //private static final String TAG = "ClassificationListAdapter";
    private List<ClassificationItem> mItems = new ArrayList<>();
    private final Context mContext;

    public ClassificationListAdapter(Context context) {
        mContext = context;
    }

    public void add(ClassificationItem item) {
        mItems.add(item);
        //refresh();
    }

    void refresh() {
        //Log.d(TAG, "refresh");
        notifyDataSetChanged();
    }

    public void setmItems(List<ClassificationItem> mItems) {
        this.mItems = mItems;
    }

    public void clear() {
        mItems.clear();
        refresh();
    }

    public void remove(int pos) {
        mItems.remove(pos);
        //refresh();
    }

    // Returns the number of ToDoItems

    @Override
    public int getCount() {
        return mItems.size();
    }


    @Override
    public ClassificationItem getItem(int pos) {
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

        ClassificationItem nItem = getItem(position);
        View item = convertView;
        ViewHolder holder;

        if (item == null) {
            //Log.d(TAG, "holder null");
            LayoutInflater inflater = LayoutInflater.from(mContext);
            item = inflater.inflate(R.layout.classification_item, parent, false);

            holder = new ViewHolder();
            holder.position = (TextView) item.findViewById(R.id.classification_item_position);
            holder.name = (TextView) item.findViewById(R.id.classification_item_name);
            holder.points = (TextView) item.findViewById(R.id.classification_item_points);

            item.setTag(holder);

        } else {
            //Log.d(TAG, "holder not null");
            holder = (ViewHolder) item.getTag();
        }

        //Log.d(TAG, "set holder ");
        holder.position.setText(nItem.getPosition());
        holder.name.setText(nItem.getName());
        holder.points.setText(nItem.getPoints());
        //Log.d(TAG, "settecd");
        return (item);

    }

    static class ViewHolder {
        TextView position;
        TextView name;
        TextView points;
    }


}
