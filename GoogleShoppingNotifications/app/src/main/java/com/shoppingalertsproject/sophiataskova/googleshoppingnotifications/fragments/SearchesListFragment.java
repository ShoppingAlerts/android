package com.shoppingalertsproject.sophiataskova.googleshoppingnotifications.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.shoppingalertsproject.sophiataskova.googleshoppingnotifications.R;
import com.shoppingalertsproject.sophiataskova.googleshoppingnotifications.SearchesArrayAdapter;
import com.shoppingalertsproject.sophiataskova.googleshoppingnotifications.models.Search;

import java.util.ArrayList;

/**
 * Created by sophiataskova on 3/7/15.
 */
public class SearchesListFragment extends Fragment {

    protected SearchesArrayAdapter searchesArrayAdapter;
    private ArrayList<Search> searches;
    private ListView lvSearches;
    protected SwipeRefreshLayout swipeContainer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searches = new ArrayList<>();
        searchesArrayAdapter = new SearchesArrayAdapter(getActivity(), searches);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_searches_home, container, false);
//        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);

        lvSearches = (ListView) v.findViewById(R.id.searches_list);
        lvSearches.setAdapter(searchesArrayAdapter);
        return v;
    }

    public void clearAll() {
        searchesArrayAdapter.clear();
    }

    public SwipeRefreshLayout getSwipeContainer() {
        return swipeContainer;
    }

    public SearchesArrayAdapter getSearchesArrayAdapter() {
        return searchesArrayAdapter;
    }
}