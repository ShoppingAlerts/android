package com.example.sofiya.smartshoppinglist.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.search, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Debug", "onCreate IntroActivity");
        setContentView(R.layout.activity_intro);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapterViewPager = new ShoppingPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);


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
        private String tabTitles[] = {"SEARCH", "WISHLIST"};

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

    public ViewPager getViewPager() {
        return viewPager;
    }

    public ResultsListFragment getmResultsListFragment() {
        return mResultsListFragment;
    }
//    public static int calculateInSampleSize (
//            BitmapFactory.Options options, int reqWidth, int reqHeight) {
//        // Raw height and width of image
//        final int height = options.outHeight;
//        final int width = options.outWidth;
//        int inSampleSize = 1;
//
//        if (height > reqHeight || width > reqWidth) {
//
//            final int halfHeight = height / 2;
//            final int halfWidth = width / 2;
//
//            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
//            // height and width larger than the requested height and width.
//            while ((halfHeight / inSampleSize) > reqHeight
//                    && (halfWidth / inSampleSize) > reqWidth) {
//                inSampleSize *= 2;
//            }
//        }
//
//        return inSampleSize;
//    }

//    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
//                                                         int reqWidth, int reqHeight) {
//
//        // First decode with inJustDecodeBounds=true to check dimensions
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeResource(res, resId, options);
//
//        // Calculate inSampleSize
//        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
//
//        // Decode bitmap with inSampleSize set
//        options.inJustDecodeBounds = false;
//        return BitmapFactory.decodeResource(res, resId, options);
//    }


}

