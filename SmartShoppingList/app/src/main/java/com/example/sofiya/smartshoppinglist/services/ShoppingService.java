package com.example.sofiya.smartshoppinglist.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.sofiya.smartshoppinglist.EbayRequests;
import com.example.sofiya.smartshoppinglist.R;
import com.example.sofiya.smartshoppinglist.RequestQueueSingleton;
import com.example.sofiya.smartshoppinglist.ShoppingAlarmReceiver;
import com.example.sofiya.smartshoppinglist.activities.IntroActivity;
import com.example.sofiya.smartshoppinglist.models.EbayItem;
import com.example.sofiya.smartshoppinglist.models.SearchItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import static com.android.volley.Request.Method.GET;


public class ShoppingService extends IntentService {
    public ShoppingService() {
        super("ShoppingService");
    }

    public static final String TAG = "Schedule Alerts";

    private String bestPrice, bestPriceUrl;

    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    @Override
    protected void onHandleIntent(Intent intent) {
        String keyword = "", filter = "";

        List<SearchItem> allSearchModels = SearchItem.listAll(SearchItem.class);

        for (SearchItem searchModel1 : allSearchModels) {
            keyword = searchModel1.getSearchKeywords();
            filter = searchModel1.getWantPrice();

            try {
                loadFromNetwork(keyword);
            } catch (IOException e) {
                Log.i(TAG, "Connection error");
            }
            if (bestPrice != null) {
                if (Double.parseDouble(bestPrice) <= Double.parseDouble(filter)) {
                    createNotification((int) System.currentTimeMillis(), R.drawable.ic_launcher, "Shopping Alerts", keyword + "'s price has dropped to "+ bestPrice +". Buy now?", bestPriceUrl);
                }
            }
        }

        ShoppingAlarmReceiver.completeWakefulIntent(intent);

    }

    public void createNotification(int nId, int iconRes, String title, String body, String url) {

        Intent intent = new Intent(this, IntroActivity.class);
        int flags = PendingIntent.FLAG_CANCEL_CURRENT;

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        PendingIntent pendingBrowserIntent = PendingIntent.getActivity(this, nId, browserIntent, flags);

        PendingIntent pIntent = PendingIntent.getActivity(this, nId, intent, flags);
        Notification notification =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(iconRes)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setContentIntent(pIntent)
                        .addAction(0, "Shop", pendingBrowserIntent)
                        .addAction(0, "Edit searches", pIntent).build();

        NotificationManager mNotificationManager =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(nId, notification);
    }

    private void loadFromNetwork(String keywordString) throws IOException {
        String url = EbayRequests.sSearchByKeywordUrl.concat(keywordString + EbayRequests.sPageNumberUrl + 1 + EbayRequests.sPaginationUrl + 1);
        downloadUrl(url);
    }


    private void downloadUrl(String urlString) throws IOException {

        final Response.Listener<JSONObject> jsonObjectListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
                JSONArray ebayItemsResult;
                try {
                    ebayItemsResult = response.getJSONArray("findItemsByKeywordsResponse").getJSONObject(0).getJSONArray("searchResult").getJSONObject(0).getJSONArray("item");
                        EbayItem bestPriceItem = EbayItem.fromJsonArray(ebayItemsResult).get(0);
                        bestPrice = String.valueOf(bestPriceItem.getPrice());
                        bestPriceUrl = bestPriceItem.getUrl();
                        Log.i("debug", "bestPrice is "+ bestPrice);
                        Log.i("debug", "bestPriceUrl is "+ bestPriceUrl);
                } catch (final JSONException e) {
                    Log.i("debug", "jsonexception " +e);
                }
            }
        };

        final Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                Log.i("debug", "volley error response" +error);
            }
        };

        Log.i("debug", "urlString is " + urlString);

        final Request<?> request = new JsonObjectRequest(GET, urlString, jsonObjectListener, errorListener ) {

            @Override
            public Priority getPriority() {
                return Priority.HIGH;
            }
        };

        RequestQueueSingleton.getInstance(this).addToRequestQueue(request);
    }

}
