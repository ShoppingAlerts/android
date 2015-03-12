package com.sophiataskova.apps.smartshoppinglist;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sophiataskova.apps.smartshoppinglist.fragments.ShoppingResultsListFragment;
import com.sophiataskova.apps.smartshoppinglist.models.SearchItem;

public class SearchItemsArrayAdapter extends ArrayAdapter<SearchItem>  {
    public SearchItemsArrayAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SearchItem searchItem = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_item, parent, false);
        }
        TextView keyWords = (TextView) convertView.findViewById(R.id.keywords);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() instanceof ActionBarActivity) {
                    FragmentManager fragmentManager = ((ActionBarActivity) getContext()).getSupportFragmentManager();

                    ShoppingResultsListFragment searchResultsFragment = new ShoppingResultsListFragment();
                    Bundle args = new Bundle();
                    args.putString("shoppingitem", searchItem.getSearchKeywords());
                    searchResultsFragment.setArguments(args);
                    fragmentManager.beginTransaction()
                            .replace(R.id.shopping_list_placeholder, searchResultsFragment, "searchresultsfragment").addToBackStack("searchresultsfragment").commit();
                }
            }
        });
        if (searchItem.getSearchKeywords() != null) {
            keyWords.setText(searchItem.getSearchKeywords());
        }
        return convertView;
    }


}
