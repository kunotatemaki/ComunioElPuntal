package com.rukiasoft.androidapps.comunioelpuntal;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

//import android.util.Log;

public class NavigationDrawerAdapter extends BaseAdapter {

    // List of ToDoItems
    private final List<MainItem> mItems = new ArrayList<>();
    private final Context mContext;
    //private static final String TAG = "AdapterFragment";

    public NavigationDrawerAdapter(Context context) {
        mContext = context;
    }

    // Add a MainItem to the adapter
    // Notify observers that the data set has changed

    public void add(MainItem item) {
        //Log.d(TAG, "metodo add");
        mItems.add(item);
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
    public MainItem getItem(int pos) {
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

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ////////////////////////////////////
        //Log.d(TAG, "metodo getview: " + position);

        final MainItem nItem = getItem(position);

        //item.setLongClickable(true);
        final ViewHolder holder;

        if (convertView == null) {
            //Log.d(TAG, "holder null");
            LayoutInflater inflater = LayoutInflater.from(mContext);
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
/*         
        TypedArray a = mContext.obtainStyledAttributes(new int[] { android.R.attr.activatedBackgroundIndicator });
        int resource = a.getResourceId(0, 0);
        //first 0 is the index in the array, second is the   default value
        a.recycle();

       int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
        	convertView.setBackgroundDrawable(mContext.getResources().getDrawable(resource));
        } else {
        	convertView.setBackground(mContext.getResources().getDrawable(resource));
        }
   */

        return (convertView);

    }

    static class ViewHolder {
        TextView text;
        ImageView image;
    }
}
