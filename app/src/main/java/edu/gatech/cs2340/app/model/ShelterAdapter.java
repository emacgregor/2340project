package edu.gatech.cs2340.app.model;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import edu.gatech.cs2340.app.R;

public class ShelterAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private final ArrayList<Shelter> shelterList;
    private ArrayList<Shelter> searchList = null;

    public ShelterAdapter(Context context, ArrayList<Shelter> shelterList) {
        Context mContext = context;
        this.shelterList = shelterList;
        this.searchList = new ArrayList<>(shelterList);
        this.inflater = LayoutInflater.from(mContext);
    }
    public class ViewHolder {
        TextView name;
        TextView restrictions;
    }
    @Override
    public int getCount() {
        return searchList.size();
    }
    @Override
    public Shelter getItem(int position) {
        return searchList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return searchList.get(position).getUniqueKey();
    }
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_list_view_items, null);
            // Locate the TextViews in listview_item.xml
            holder.name = view.findViewById(R.id.name);
            holder.restrictions = view.findViewById(R.id.restrictions);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(searchList.get(position).getName());
        holder.restrictions.setText(searchList.get(position).getSearchRestrictions());
        return view;
    }
    public void filterByName(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        searchList.clear();
        if (charText.length() == 0) {
            searchList.addAll(shelterList);
        } else {
            for (Shelter shelter : shelterList) {
                if ((shelter.getName().toLowerCase(Locale.getDefault()).contains(charText)
                        || shelter.getSearchRestrictions().toLowerCase(Locale.getDefault()).contains(charText))) {
                    if (charText.equals("men")) { //this is so all the women shelters don't show up when "men" is searched for
                        if (shelter.allowsMen()) {
                            searchList.add(shelter);
                        }
                    } else {
                        searchList.add(shelter);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }
}
