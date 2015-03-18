package com.example.sofiya.smartshoppinglist;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sofiya.smartshoppinglist.activities.IntroActivity;
import com.example.sofiya.smartshoppinglist.fragments.ResultsListFragment;
import com.example.sofiya.smartshoppinglist.models.SearchItem;

public class SearchItemsArrayAdapter extends ArrayAdapter<SearchItem> {
    public SearchItemsArrayAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SearchItem searchItem = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_item, parent, false);
        }
        TextView keyWordsTextView = (TextView) convertView.findViewById(R.id.keywords);
        TextView wantPriceTextView = (TextView) convertView.findViewById(R.id.want_price);
        TextView bestPriceTextView = (TextView) convertView.findViewById(R.id.best_price_value);
        TextView bestPriceLabel = (TextView) convertView.findViewById(R.id.best_price_text);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() instanceof IntroActivity) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(searchItem.getBestPriceUrl()));
                    getContext().startActivity(i);
                }
            }
        });
        if (searchItem.getSearchKeywords() != null) {
            keyWordsTextView.setText(searchItem.getSearchKeywords());
        }
        if (searchItem.getWantPrice() != null) {
            wantPriceTextView.setText(searchItem.getWantPrice());
        }
        String bestPriceString = searchItem.getBestPrice();
        if (bestPriceString != null) {
            bestPriceTextView.setText(bestPriceString);
            if (!searchItem.getWantPrice().equals("") && !bestPriceString.equals("")){
            if (Double.parseDouble(bestPriceString) <= Double.parseDouble(searchItem.getWantPrice())) {
                bestPriceLabel.setTextColor(getContext().getResources().getColor(R.color.green));
                bestPriceTextView.setTextColor(getContext().getResources().getColor(R.color.green));
            } else {
                bestPriceLabel.setTextColor(getContext().getResources().getColor(R.color.red));
                bestPriceTextView.setTextColor(getContext().getResources().getColor(R.color.red));
            }}
        }
        return convertView;
    }

    public static void startSearchForKeywords(IntroActivity context, String searchKeywords) {

        FragmentPagerAdapter fragmentPagerAdapter = context.getAdapterViewPager();
        for (int i = 0; i < fragmentPagerAdapter.getCount(); i++) {
            String name = makeFragmentName(context.getViewPager().getId(), i);
            Fragment viewPagerFragment = context.getSupportFragmentManager().findFragmentByTag(name);
            if (viewPagerFragment != null) {
                if (viewPagerFragment instanceof ResultsListFragment && viewPagerFragment.isResumed()) {
                    ((ResultsListFragment) viewPagerFragment).makeRequest(0, searchKeywords);

                }
            }
        }
    }

    public static void startSearchForKeywords(IntroActivity context, String searchKeywords, String filter) {

        FragmentPagerAdapter fragmentPagerAdapter = context.getAdapterViewPager();
        for (int i = 0; i < fragmentPagerAdapter.getCount(); i++) {
            String name = makeFragmentName(context.getViewPager().getId(), i);
            Fragment viewPagerFragment = context.getSupportFragmentManager().findFragmentByTag(name);
            if (viewPagerFragment != null) {
                if (viewPagerFragment instanceof ResultsListFragment && viewPagerFragment.isResumed()) {
                    ((ResultsListFragment) viewPagerFragment).setFilter(filter);
                    ((ResultsListFragment) viewPagerFragment).makeRequest(0, searchKeywords);
                }
            }
        }
    }

    private static String makeFragmentName(int viewId, int position) {
        return "android:switcher:" + viewId + ":" + position;
    }

}
