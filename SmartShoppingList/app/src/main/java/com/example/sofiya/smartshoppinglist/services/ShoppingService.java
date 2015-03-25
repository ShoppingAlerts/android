package com.example.sofiya.smartshoppinglist.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.sofiya.smartshoppinglist.EbayRequests;
import com.example.sofiya.smartshoppinglist.models.EbayItem;
import com.example.sofiya.smartshoppinglist.models.SearchItem;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by sofiya on 3/25/15.
 */
public class ShoppingService extends IntentService {
    public ShoppingService() {
        super("MyTestService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Do the task here
        List<SearchItem> allSearchModels = SearchItem.listAll(SearchItem.class);
        for (SearchItem searchModel1 : allSearchModels) {
//            startSavedSearch(searchModel1);
            Log.i("DEBUG", "keyword is " + searchModel1.getSearchKeywords());
        }
        Log.i("MyTestService", "Service running");
    }

    public static void startSavedSearch(final SearchItem searchItem) {
        String url = EbayRequests.sSearchByKeywordUrl.concat(searchItem.getSearchKeywords() + EbayRequests.sPageNumberUrl + 1 + EbayRequests.sPaginationUrl + 10+"&itemFilter(0).name=MaxPrice&itemFilter(0).value=" + searchItem.getWantPrice() + "&itemFilter(0).paramName=Currency&itemFilter(0).paramValue=USD&sortOrder=PricePlusShippingLowest");
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
//        if (context.isNetworkAvailable()){

            asyncHttpClient.get(url, new JsonHttpResponseHandler() {

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
                        EbayItem bestPriceItem = null;
                            for (int i = 0; i< ebayItemsResult.length(); i++) {
                                if (EbayItem.fromJsonArray(ebayItemsResult).get(i).getUrl() != null) {
                                    bestPriceItem = EbayItem.fromJsonArray(ebayItemsResult).get(i);
                                    break;
                                }
                            }
                        String newBestPrice, newBestUrl;
                            if (bestPriceItem != null) {
                                newBestPrice = String.valueOf(bestPriceItem.getPrice());
                                newBestUrl = bestPriceItem.getUrl();
                                if (newBestPrice != null && newBestUrl!=null) {
                                    updatePriceAndUrlForSearchItem(searchItem.getId(), newBestPrice, newBestUrl);
                                }
                            }


//
//                        mEbayItemsArrayAdapter.addAll(EbayItem.fromJsonArray(ebayItemsResult));
//                        if (mEbayItemsArrayAdapter.isEmpty()) {
//
////                            getView().findViewById(R.id.no_results).setVisibility(View.VISIBLE);
////                            gvResults.setVisibility(View.GONE);
//                        } else {
////                            findViewById(R.id.no_results).setVisibility(View.GONE);
////                            gvResults.setVisibility(View.VISIBLE);
//                            mSwipeContainer.setRefreshing(false);
//                        }
                    } catch (JSONException e) {
//                        Toast.makeText(getActivity(), "Network problem with fetching the data", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
//                    Log.i("INFO", imageResults.toString());
                }
            });
//        }
    }

    private static void updatePriceAndUrlForSearchItem(long id, String newBestPrice, String newBestUrl) {
        SearchItem searchItem = SearchItem.findById(SearchItem.class, id);
        searchItem.setBestPrice(newBestPrice);
        searchItem.setBestPriceUrl(newBestUrl);
        searchItem.save();
    }
}
