package com.shoppingalertsproject.sophiataskova.googleshoppingnotifications.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.shoppingalertsproject.sophiataskova.googleshoppingnotifications.R;
import com.shoppingalertsproject.sophiataskova.googleshoppingnotifications.models.Search;
import com.shoppingalertsproject.sophiataskova.googleshoppingnotifications.util.EndlessScrollListener;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sophiataskova on 3/7/15.
 */
public class SearchesHomeFragment extends SearchesListFragment{

    private GoogleApiClient client;

    private ArrayList<Search> tweets;
    private ListView lvTweets;
    private int mCurrentPage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentPage = 1;
        client = getRestClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        lvTweets = (ListView) v.findViewById(R.id.lv_tweets);
        lvTweets.setAdapter(getSearchesArrayAdapter());
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                customLoadMoreDataFromApi(page);
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                searchesArrayAdapter.clear();
                populateTimeLine(0);
            }
        });
        populateTimeLine(0);
        super.onStart();
    }

    private void populateTimeLine(int page) {
        client.getHomeTimeline(page, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("Debug", response.toString());
                getSearchesArrayAdapter().addAll(Search.fromJSONArray(response));
//                persistTweets();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if (errorResponse != null) {
                    Log.d("Debug", errorResponse.toString());
                }
            }
        });
    }

    public void customLoadMoreDataFromApi(int offset) {
        mCurrentPage = offset + 1;
        populateTimeLine(offset);

    }

}
