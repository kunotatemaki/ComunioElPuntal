package com.rukiasoft.androidapps.comunioelpuntal;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.rukiasoft.androidapps.comunioelpuntal.utils.PlayerNameComparator;
import com.rukiasoft.androidapps.comunioelpuntal.utils.PlayerOwnerComparator;
import com.rukiasoft.androidapps.comunioelpuntal.utils.PlayerTeamComparator;
import com.rukiasoft.androidapps.comunioelpuntal.utils.PositionComparator;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


class PlayersListAdapter extends BaseAdapter implements Filterable {

    @SuppressWarnings("unused")
    private static final String TAG = "PlayersListAdapter";
    private List<PlayerItem> mItems = new ArrayList<>();
    private List<PlayerItem> showedItems = new ArrayList<>();
    //private final Context mContext;
    private Comparator<PlayerItem> comparator = new PlayerNameComparator();
    private final Filter myFilter = new Filter() {
        @SuppressLint("DefaultLocale")
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            ArrayList<PlayerItem> tempList = new ArrayList<>();
            //constraint is the result from text you want to filter against.
            //objects is your data set you will filter from
            if (constraint != null && mItems != null) {
                String normalized = Normalizer.normalize(constraint, Normalizer.Form.NFD);
                String entrada = normalized.replaceAll("[^\\p{ASCII}]", "");
                entrada = entrada.toLowerCase();
                //Log.d(TAG, constraint.toString() +  " en ASCII: " + entrada);
                for (int i = 0; i < mItems.size(); i++) {
                    PlayerItem item = mItems.get(i);
                    normalized = Normalizer.normalize(item.getName(), Normalizer.Form.NFD);
                    String nombre = normalized.replaceAll("[^\\p{ASCII}]", "");
                    nombre = nombre.toLowerCase();
                    //Log.d(TAG, item.getName() +  " convertido a: " + nombre);
                    if (nombre.contains(entrada))
                        tempList.add(item);
                }
                //following two lines is very important
                //as publish result can only take FilterResults objects
                filterResults.values = tempList;
                filterResults.count = tempList.size();
            }
            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence contraint, FilterResults results) {
            showedItems = (ArrayList<PlayerItem>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    };


    // Add a NotificationItem to the adapter
    // Notify observers that the data set has changed

    public void add(PlayerItem item) {
        addWithoutRefresh(item);
        refresh();
    }

    public void addWithoutRefresh(PlayerItem item) {
        mItems.add(item);
    }

    public void refresh() {
        //Log.d(TAG, "refresh");
        showedItems = mItems;
        Collections.sort(mItems, comparator);
        notifyDataSetChanged();
    }

    public void setmItems(List<PlayerItem> mItems) {
        this.mItems = mItems;
    }

    public void clear() {
        //Log.d(TAG, "metodo clear");
        clearWithoutRefresh();
        refresh();
    }

    public void clearWithoutRefresh() {
        //Log.d(TAG, "metodo clear");
        mItems.clear();
    }

    public void remove(int pos) {
        //Log.d(TAG, "metodo remove");

        mItems.remove(pos);
        refresh();
    }

    public List<PlayerItem> getmItems() {
        return mItems;
    }


    // Returns the number of ToDoItems

    @Override
    public int getCount() {
        return showedItems.size();
    }

    public int getTotalItems() {
        return mItems.size();
    }


    @Override
    public PlayerItem getItem(int pos) {
        //Log.d(TAG, "metodo getitem");
        return showedItems.get(pos);
    }

    // In this case it's just the position
    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PlayerItem nItem = getItem(position);
        View item = convertView;
        //item.setLongClickable(true);
        ViewHolder holder;

        if (item == null) {
            //Log.d(TAG, "holder null");
            LayoutInflater inflater = LayoutInflater.from(MainActivity.getContext());
            item = inflater.inflate(R.layout.player_item, parent, false);

            holder = new ViewHolder();
            holder.nameLabel = (TextView) item.findViewById(R.id.player_name);
            holder.positionLabel = (TextView) item.findViewById(R.id.player_position_textView);
            holder.ownerLabel = (TextView) item.findViewById(R.id.player_owner_textView);
            holder.teamLabel = (TextView) item.findViewById(R.id.team_textView);
            holder.shield = (ImageView) item.findViewById(R.id.shield_view);
            item.setTag(holder);

        } else {
            //Log.d(TAG, "holder NOT null");
            holder = (ViewHolder) item.getTag();
        }
        holder.nameLabel.setText(nItem.getName());
        holder.positionLabel.setText(nItem.getPosition());
        holder.ownerLabel.setText(nItem.getOwner());
        holder.teamLabel.setText(nItem.getTeam());
        holder.shield.setImageResource(nItem.getShield());
        return (item);

    }

    static class ViewHolder {
        TextView nameLabel;
        TextView positionLabel;
        TextView ownerLabel;
        TextView teamLabel;
        ImageView shield;
    }

    @Override
    public Filter getFilter() {
        return myFilter;
    }

    public void orderByName() {
        comparator = new PlayerNameComparator();
        refresh();
    }

    public void orderByTeam() {
        comparator = new PlayerTeamComparator();
        refresh();
    }

    public void orderByOwner() {
        comparator = new PlayerOwnerComparator();
        refresh();
    }

    public void orderByPosition() {
        comparator = new PositionComparator();
        refresh();
    }
}
