package edu.gatech.cs2340.app.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.cs2340.app.R;
import edu.gatech.cs2340.app.model.Shelter;
import edu.gatech.cs2340.app.model.Model;

/**
 * An activity representing a list of Shelters. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ShelterDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ShelterListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RecyclerView recyclerView;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        //adapter = new ShelterAdapter(this, shelters);

        if (findViewById(R.id.shelter_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        Model.getSheltersFromDB();

        recyclerView = findViewById(R.id.shelter_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(Model.getShelters(),
                mTwoPane));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        //private final ItemListActivity mParentActivity;
        private final List<Shelter> mValues;
        private final boolean mTwoPane;

        SimpleItemRecyclerViewAdapter(List<Shelter> items, boolean twoPane) {
            mValues = new ArrayList<>(items);
            //mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.shelter_list_content, parent, false);
            /*View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.shelter_list_content, parent, false);*/
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            final int uniqueKey = holder.mItem.getUniqueKey();
            holder.mIdView.setText(String.valueOf(uniqueKey));
            holder.mContentView.setText(holder.mItem.getName());

            holder.itemView.setTag(mValues.get(position));
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putInt(ShelterDetailFragment.ARG_ITEM_ID,
                                uniqueKey);
                        ShelterDetailFragment fragment = new ShelterDetailFragment();
                        fragment.setArguments(arguments);

                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction
                                = fragmentManager.beginTransaction();
                        fragmentTransaction
                                = fragmentTransaction.add(R.id.shelter_detail_container, fragment);
                        fragmentTransaction.commit();
                        /*getSupportFragmentManager().beginTransaction()
                                .replace(R.id.shelter_detail_container, fragment)
                                .commit();*/
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ShelterDetailActivity.class);
                        intent.putExtra(ShelterDetailFragment.ARG_ITEM_ID,
                                uniqueKey);

                        Model.setCurrentShelter(holder.mItem);
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final View mView;
            final TextView mIdView;
            final TextView mContentView;
            Shelter mItem;
            ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = view.findViewById(R.id.id_text);
                mContentView = view.findViewById(R.id.content);
            }
        }
    }


}
