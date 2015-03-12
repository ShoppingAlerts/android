package com.sophiataskova.apps.smartshoppinglist.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.sophiataskova.apps.smartshoppinglist.R;
import com.sophiataskova.apps.smartshoppinglist.SearchItemsArrayAdapter;
import com.sophiataskova.apps.smartshoppinglist.models.SearchItem;
import com.sophiataskova.apps.smartshoppinglist.models.SearchModel;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListFragment extends Fragment {

    private ArrayList<SearchItem> searchItems;



    protected SearchItemsArrayAdapter mSearchItemsArrayAdapter;
    private ListView searchItemsListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        mSearchItemsArrayAdapter =
                new SearchItemsArrayAdapter(getActivity(), android.R.layout.simple_list_item_1);

        searchItemsListView = (ListView) view.findViewById(R.id.lv_shopping_list);
        searchItemsListView.setAdapter(mSearchItemsArrayAdapter);
        SearchItem itemToAdd = new SearchItem(getArguments().getString("shoppingitem").toString());
        mSearchItemsArrayAdapter.add(itemToAdd);
        retrieveSearchesFromDB();
        persistSearch(itemToAdd);
        // Setup handles to view objects here
        // etFoo = (EditText) view.findViewById(R.id.etFoo);
        return view;
    }

    public void persistSearches() {
        Log.i("DEBUG", "testing search persist");
        for (int i = 0; i < getmSearchItemsArrayAdapter().getCount(); i++) {
            SearchItem searchItem = getmSearchItemsArrayAdapter().getItem(i);
            SearchModel searchModel = new SearchModel(searchItem.getSearchKeywords(), false); // Todo unhardcode alerts from false
            searchModel.save();
        }
    }

    private void persistSearch(SearchItem itemToAdd) {
        SearchModel searchModel = new SearchModel(itemToAdd.getSearchKeywords(), false); // Todo unhardcode alerts from false
        searchModel.save();
    }

    private void retrieveSearchesFromDB() {
        List<SearchModel> allSearchModels = SearchModel.listAll(SearchModel.class);

        for (SearchModel searchModel1 : allSearchModels) {
            mSearchItemsArrayAdapter.add(SearchItem.fromSearchModel(searchModel1));
            Log.i("DEBUG", "keyword is " + SearchItem.fromSearchModel(searchModel1).getSearchKeywords());

        }
    }

    public SearchItemsArrayAdapter getmSearchItemsArrayAdapter() {
        return mSearchItemsArrayAdapter;
    }
}