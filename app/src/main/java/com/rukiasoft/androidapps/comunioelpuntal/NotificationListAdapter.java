package com.rukiasoft.androidapps.comunioelpuntal;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
//import android.util.Log;


class NotificationListAdapter extends BaseAdapter {

    @SuppressWarnings("unused")
    private static final String TAG = "NotificationListAdapter";
    // List of ToDoItems
    private final List<NotificationItem> mItems = new ArrayList<>();
    private final Context mContext;
    //private static final String TAG = "remotesacarino.NotificationListAdapter";

    public NotificationListAdapter(Context context) {
        mContext = context;
    }

    // Add a NotificationItem to the adapter
    // Notify observers that the data set has changed

    public void add(NotificationItem item) {
        //Log.d(TAG, "metodo add");
        mItems.add(0, item);
        refresh();
    }

    public void refresh() {
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
    public NotificationItem getItem(int pos) {
        //Log.d(TAG, "metodo getitem");

        return mItems.get(pos);
    }

    // Get the ID for the ToDoItem
    // In this case it's just the position

    @Override
    public long getItemId(int pos) {
        return pos;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ////////////////////////////////////
        //Log.d(TAG, "metodo getview: " + position);

        NotificationItem nItem = getItem(position);
        View item = convertView;
        //item.setLongClickable(true);
        ViewHolder holder;

        if (item == null) {
            //Log.d(TAG, "holder null");
            LayoutInflater inflater = LayoutInflater.from(mContext);
            item = inflater.inflate(R.layout.notification_item, parent, false);

            holder = new ViewHolder();
            holder.dateLabel = (TextView) item.findViewById(R.id.notification_item_date_label);
            holder.dateView = (TextView) item.findViewById(R.id.notification_item_date_view);
            holder.timeLabel = (TextView) item.findViewById(R.id.notification_item_time_label);
            holder.timeView = (TextView) item.findViewById(R.id.notification_item_time_view);
            holder.stateLabel = (TextView) item.findViewById(R.id.notification_item_state_label);
            holder.stateView = (TextView) item.findViewById(R.id.notification_item_state_view);
            holder.subjectLabel = (TextView) item.findViewById(R.id.notification_item_subject_label);
            holder.subjectView = (TextView) item.findViewById(R.id.notification_item_subject_view);
            holder.layout = (RelativeLayout) item.findViewById(R.id.notification_item_layout);
            item.setTag(holder);

        } else {
            holder = (ViewHolder) item.getTag();
        }

        holder.dateView.setText(nItem.getDateSent());
        holder.timeView.setText(nItem.getTimeSent());
        holder.subjectView.setText(nItem.getShortMessage());

        if (nItem.isRead()) {
            //Log.d(TAG, "sí leido: " + position);
            holder.dateLabel.setTextColor(mContext.getResources().getColor(R.color.notification_read));
            holder.dateView.setTextColor(mContext.getResources().getColor(R.color.notification_read));
            holder.timeLabel.setTextColor(mContext.getResources().getColor(R.color.notification_read));
            holder.timeView.setTextColor(mContext.getResources().getColor(R.color.notification_read));
            holder.subjectLabel.setTextColor(mContext.getResources().getColor(R.color.notification_read));
            holder.subjectView.setTextColor(mContext.getResources().getColor(R.color.notification_read));
            holder.stateLabel.setTextColor(mContext.getResources().getColor(R.color.notification_read));
            holder.stateView.setTextColor(mContext.getResources().getColor(R.color.notification_read));
            holder.stateView.setText(mContext.getResources().getString(R.string.read));
            holder.dateLabel.setTypeface(null, Typeface.NORMAL);
            holder.dateView.setTypeface(null, Typeface.NORMAL);
            holder.timeLabel.setTypeface(null, Typeface.NORMAL);
            holder.timeView.setTypeface(null, Typeface.NORMAL);
            holder.stateLabel.setTypeface(null, Typeface.NORMAL);
            holder.subjectLabel.setTypeface(null, Typeface.NORMAL);
            holder.subjectView.setTypeface(null, Typeface.NORMAL);
        } else {
            //Log.d(TAG, "no leido: " + position);
            holder.dateLabel.setTextColor(mContext.getResources().getColor(R.color.notification_not_read));
            holder.dateView.setTextColor(mContext.getResources().getColor(R.color.notification_not_read));
            holder.timeLabel.setTextColor(mContext.getResources().getColor(R.color.notification_not_read));
            holder.timeView.setTextColor(mContext.getResources().getColor(R.color.notification_not_read));
            holder.subjectLabel.setTextColor(mContext.getResources().getColor(R.color.notification_not_read));
            holder.subjectView.setTextColor(mContext.getResources().getColor(R.color.notification_not_read));
            holder.stateLabel.setTextColor(mContext.getResources().getColor(R.color.notification_not_read));
            holder.stateView.setTextColor(mContext.getResources().getColor(R.color.notification_not_read));
            holder.stateView.setText(mContext.getResources().getString(R.string.read_not));
            holder.dateLabel.setTypeface(null, Typeface.BOLD);
            holder.dateView.setTypeface(null, Typeface.BOLD);
            holder.timeLabel.setTypeface(null, Typeface.BOLD);
            holder.timeView.setTypeface(null, Typeface.BOLD);
            holder.stateLabel.setTypeface(null, Typeface.BOLD);
            holder.stateView.setTypeface(null, Typeface.BOLD);
            holder.subjectLabel.setTypeface(null, Typeface.BOLD);
            holder.subjectView.setTypeface(null, Typeface.BOLD);
        }
        return (item);
    }

    static class ViewHolder {
        TextView dateLabel;
        TextView timeLabel;
        TextView subjectLabel;
        TextView dateView;
        TextView timeView;
        TextView subjectView;
        TextView stateLabel;
        TextView stateView;
        RelativeLayout layout;
    }


}
