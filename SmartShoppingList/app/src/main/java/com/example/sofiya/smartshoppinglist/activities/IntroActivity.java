package com.example.sofiya.smartshoppinglist.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.astuetz.PagerSlidingTabStrip;
import com.example.sofiya.smartshoppinglist.R;
import com.example.sofiya.smartshoppinglist.fragments.ResultsListFragment;
import com.example.sofiya.smartshoppinglist.fragments.ShoppingListFragment;
import com.example.sofiya.smartshoppinglist.models.SearchItem;


public class IntroActivity extends FragmentActivity {


    FragmentPagerAdapter adapterViewPager;



    ViewPager viewPager;

    public static ShoppingListFragment getmShoppingListFragment() {
        return mShoppingListFragment;
    }

    private static ShoppingListFragment mShoppingListFragment;


    private static ResultsListFragment mResultsListFragment;

    public FragmentPagerAdapter getAdapterViewPager() {
        return adapterViewPager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Debug", "onCreate IntroActivity");
        setContentView(R.layout.activity_intro);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapterViewPager = new ShoppingPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActionBar().hide();
        }

        PagerSlidingTabStrip pagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pagerSlidingTabStrip.setViewPager(viewPager);
        mResultsListFragment = new ResultsListFragment();
        mShoppingListFragment = new ShoppingListFragment();
//        Bundle args = new Bundle();
//        if (getIntent().hasExtra("shoppingitem")) {
//            args.putString("shoppingitem", getIntent().getExtras().get("shoppingitem").toString());
//            mShoppingListFragment.setArguments(args);
//        }
    }

    public void createNotification(int nId, int iconRes, String title, String body, String url) {
        Intent intent = new Intent(this, IntroActivity.class);
// Next, let's turn this into a PendingIntent using
//   public static PendingIntent getActivity(Context context, int requestCode,
//       Intent intent, int flags)
        int flags = PendingIntent.FLAG_CANCEL_CURRENT; // cancel old intent and create new one

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        PendingIntent pendingBrowserIntent = PendingIntent.getActivity(this, nId, browserIntent, flags);

        int requestID = (int) System.currentTimeMillis(); //unique requestID to differentiate between various notification with same NotifId
        PendingIntent pIntent = PendingIntent.getActivity(this, nId, intent, flags);
        Notification noti =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(iconRes)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setContentIntent(pIntent)
                        .addAction(0, "Shop", pendingBrowserIntent)
                        .addAction(0, "Edit searches", pIntent).build();

// Hide the notification after its selected
//        noti.setAutoCancel(true);

        NotificationManager mNotificationManager =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(0, noti);
    }


    public static void persistSearch(SearchItem itemToAdd) {
        SearchItem searchItem = new SearchItem(itemToAdd.getSearchKeywords(), itemToAdd.getWantPrice(), itemToAdd.getBestPrice(), itemToAdd.getBestPriceUrl()); // Todo unhardcode alerts from false
        searchItem.save();
    }

    public static void deleteSearch(SearchItem itemToDelete) {
        SearchItem searchItem = SearchItem.findById(SearchItem.class, (long)1);
        searchItem.delete();
    }
    public static class ShoppingPagerAdapter extends FragmentPagerAdapter {
        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
        private static int NUM_ITEMS = 2;
        private String tabTitles[] = {"SEARCH", "ALERTS"};

        public ShoppingPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return mResultsListFragment;
            } else if (position == 1) {
                return mShoppingListFragment;
            } else {
                return null;
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

    }

    public Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public void dismissKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public ResultsListFragment getmResultsListFragment() {
        return mResultsListFragment;
    }


}

