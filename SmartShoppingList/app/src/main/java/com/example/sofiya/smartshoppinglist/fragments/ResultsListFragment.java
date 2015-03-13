package com.example.sofiya.smartshoppinglist.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sofiya.smartshoppinglist.EbayItemsArrayAdapter;
import com.example.sofiya.smartshoppinglist.EbayRequests;
import com.example.sofiya.smartshoppinglist.EndlessScrollListener;
import com.example.sofiya.smartshoppinglist.R;
import com.example.sofiya.smartshoppinglist.activities.IntroActivity;
import com.example.sofiya.smartshoppinglist.models.EbayItem;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.sofiya.smartshoppinglist.SearchItemsArrayAdapter.startSearchForKeywords;

/**
 * Created by sofiya on 3/13/15.
 */
public class ResultsListFragment extends Fragment {

    protected EbayItemsArrayAdapter mEbayItemsArrayAdapter;
    private ArrayList<EbayItem> ebayItems;
    private ListView lvShoppingList;
    private EditText keywordsEditText, filterEditText;
    protected SwipeRefreshLayout swipeContainer;

    private String filter;

    private int mCurrentPage;
    private String keywords;
    private String mPaginatedUrl;
    private String title;
    private int page;

    // newInstance constructor for creating fragment with arguments
    public static ResultsListFragment newInstance(int page, String title) {
        ResultsListFragment fragmentFirst = new ResultsListFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ebayItems = new ArrayList<EbayItem>();

        mEbayItemsArrayAdapter = new EbayItemsArrayAdapter(getActivity(), ebayItems);
        mCurrentPage = 1;
        if (getArguments()!=null) {
            keywords = getArguments().getString("shoppingitem");
            filter = getArguments().getString("pricefilter");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_results_list, container, false);
        prepareEditText(v);
        prepareFilters(v);
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);

        lvShoppingList = (ListView) v.findViewById(R.id.lv_shopping_list);
        lvShoppingList.setAdapter(mEbayItemsArrayAdapter);




        lvShoppingList.setOnScrollListener(new EndlessScrollListener() {
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

                mEbayItemsArrayAdapter.clear();
                makeRequest(0, keywords);
            }
        });
        makeRequest(0, keywords);
        super.onStart();
    }

    private void prepareFilters(View container) {
        filterEditText = (EditText) container.findViewById(R.id.max_price_text);
        filterEditText.setImeOptions(EditorInfo.IME_ACTION_GO);
        filterEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    String filterText = filterEditText.getText().toString();
                    startSearchForKeywords((IntroActivity)getActivity() , keywordsEditText.getText().toString(), filterText);
                    return true;
                }
                return false;
            }
        });
    }

    private void prepareEditText(View container) {
        keywordsEditText = (EditText) container.findViewById(R.id.i_need);
        keywordsEditText.setFocusableInTouchMode(true);
        keywordsEditText.requestFocus();
        keywordsEditText.setImeOptions(EditorInfo.IME_ACTION_GO);
        keywordsEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    startSearchForKeywords((IntroActivity)getActivity(), keywordsEditText.getText().toString());
                    getView().findViewById(R.id.filters_layout).setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });
    }

    public EbayItemsArrayAdapter getmEbayItemsArrayAdapter() {
        return mEbayItemsArrayAdapter;
    }



    public void customLoadMoreDataFromApi(int offset) {
        mCurrentPage = offset + 1;
        makeRequest(offset, keywords);
    }

    public void makeRequest(final int offset, final String searchKeywords) {
        mEbayItemsArrayAdapter.clear();
        mCurrentPage = offset;

        mPaginatedUrl = EbayRequests.sSearchByKeywordUrl.concat(searchKeywords + EbayRequests.sPageNumberUrl + 1 + EbayRequests.sPaginationUrl + 10);
        if (filter != null) {
            mPaginatedUrl = mPaginatedUrl.concat("&itemFilter(0).name=MaxPrice&itemFilter(0).value=" + filter + "&itemFilter(0).paramName=Currency&itemFilter(0).paramValue=USD");
        }
        Log.i("INFO", "searchUrl is " + mPaginatedUrl);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        if (((IntroActivity)getActivity()).isNetworkAvailable()){

            asyncHttpClient.get(mPaginatedUrl, new JsonHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.i("Debug", "error is "+ errorResponse);
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("DEBUG", response.toString());
                    JSONArray ebayItemsResult;
                    try {
                        ebayItemsResult = response.getJSONArray("findItemsByKeywordsResponse").getJSONObject(0).getJSONArray("searchResult").getJSONObject(0).getJSONArray("item");
//                        if (mCurrentPage == 0) {
//                            imageResults.clear();
//                        }

                        mEbayItemsArrayAdapter.addAll(EbayItem.fromJsonArray(ebayItemsResult));
                        if (mEbayItemsArrayAdapter.isEmpty()) {
//                            getView().findViewById(R.id.no_results).setVisibility(View.VISIBLE);
//                            gvResults.setVisibility(View.GONE);
                        } else {
//                            findViewById(R.id.no_results).setVisibility(View.GONE);
//                            gvResults.setVisibility(View.VISIBLE);
                            swipeContainer.setRefreshing(false);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getActivity(), "Network problem with fetching the data", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
//                    Log.i("INFO", imageResults.toString());
                }
            });
        }
        else {
            Toast.makeText(getActivity(), "No network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}
