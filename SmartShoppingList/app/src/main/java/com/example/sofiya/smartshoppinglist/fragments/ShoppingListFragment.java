package com.example.sofiya.smartshoppinglist.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.sofiya.smartshoppinglist.R;
import com.example.sofiya.smartshoppinglist.SearchItemsArrayAdapter;
import com.example.sofiya.smartshoppinglist.models.SearchItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sofiya on 3/13/15.
 */
public class ShoppingListFragment extends Fragment {
    private ArrayList<SearchItem> searchItems;
    private CreateSearchDialogFragment mComposeDialog;
//    private FloatingActionButton mFloatingActionButton;
    private View mNoItemsYet;

    protected SearchItemsArrayAdapter mSearchItemsArrayAdapter;
    private ListView searchItemsListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("debug", "onCreate shoppinglistfragment");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        Log.i("debug", "onCreateView shoppinglistfragment");
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        mSearchItemsArrayAdapter =
                new SearchItemsArrayAdapter(getActivity(), android.R.layout.simple_list_item_1);

        searchItemsListView = (ListView) view.findViewById(R.id.lv_shopping_list);

        mNoItemsYet = view.findViewById(R.id.no_items);
        searchItemsListView.setAdapter(mSearchItemsArrayAdapter);

        retrieveSearchesFromDB();
        if (!mSearchItemsArrayAdapter.isEmpty()) {
            mNoItemsYet.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i("debug", "onAttach shoppinglistfragment");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("debug", "onstart shoppinglistfragment");
    }

    public void retrieveSearchesFromDB() {
        if (SearchItem.exists()) {
            List<SearchItem> allSearchModels = SearchItem.listAll(SearchItem.class);
            if (mSearchItemsArrayAdapter != null) {
                mSearchItemsArrayAdapter.clear();
                for (SearchItem searchModel1 : allSearchModels) {
                    mSearchItemsArrayAdapter.add(searchModel1);
                    Log.i("DEBUG", "keyword is " + searchModel1.getSearchKeywords());
                }
            }

            if (!allSearchModels.isEmpty()) {
                mNoItemsYet.setVisibility(View.GONE);
            }
        }
    }
}