package edu.gatech.cs2340.app.model;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import edu.gatech.cs2340.app.R;

/**
 * This class implements searching the shelter list.
 */
public class ShelterAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private final Collection<Shelter> shelterList;
    private final List<Shelter> searchList;

    /**
     * Constructor for shelterAdapter which takes previous context and the shelterList.
     * @param context Previous context.
     * @param shelterList List of shelters.
     */
    public ShelterAdapter(Context context, ArrayList<Shelter> shelterList) {
        this.shelterList = new ArrayList<>(shelterList);
        this.searchList = new ArrayList<>(shelterList);
        this.inflater = LayoutInflater.from(context);
    }
    class ViewHolder {
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
        Shelter shelter = searchList.get(position);
        return shelter.getUniqueKey();
    }
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        View mView = view;
        if (mView == null) {
            holder = new ViewHolder();
            mView = inflater.inflate(R.layout.activity_list_view_items, parent, false);
            // Locate the TextViews in listview_item.xml
            holder.name = mView.findViewById(R.id.name);
            holder.restrictions = mView.findViewById(R.id.restrictions);
            mView.setTag(holder);
        } else {
            holder = (ViewHolder) mView.getTag();
        }
        // Set the results into TextViews
        Shelter shelter = searchList.get(position);
        holder.name.setText(shelter.getName());
        holder.restrictions.setText(shelter.getSearchRestrictions());
        return mView;
    }

    /**
     * Does all work on finding all shelters that fit specified search.
     * @param charText What has been searched for.
     */
    public void filter(String charText) {
        String lowerCaseCharText = charText.toLowerCase(Locale.getDefault());
        searchList.clear();
        if (lowerCaseCharText.isEmpty()) {
            searchList.addAll(shelterList);
        } else {
            for (Shelter shelter : shelterList) {
                String searchTerms = shelter.getSearchTerms();
                if (searchTerms.contains(lowerCaseCharText)) {
                    if ("men".equals(lowerCaseCharText)) {
                        //this is so all the women shelters don't show up when "men" is searched for
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
