package com.example.sofiya.smartshoppinglist;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.sofiya.smartshoppinglist.activities.IntroActivity;
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
        ImageButton editButton = (ImageButton) convertView.findViewById(R.id.edit);
        ImageButton deleteButton = (ImageButton) convertView.findViewById(R.id.delete);
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
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EbayItemsArrayAdapter.showComposeDialog(((IntroActivity) getContext()), "Edit alert", searchItem.getSearchKeywords());
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long idToDelete = searchItem.getId();
                SearchItemsArrayAdapter.this.remove(searchItem); //.notifyDataSetChanged();
                SearchItem searchItemToDelete = SearchItem.findById(SearchItem.class, idToDelete);
                searchItemToDelete.delete();
                if (SearchItemsArrayAdapter.this.isEmpty()) {
                    View noItemsLeft = ((IntroActivity)getContext()).findViewById(R.id.no_items);
                    if (noItemsLeft != null) {
                        noItemsLeft.setVisibility(View.VISIBLE);
                    }
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
//                ((IntroActivity)getContext()).createNotification(123, R.drawable.ic_launcher, "Shopping Alerts", searchItem.getSearchKeywords() + "'s price has dropped! Buy now?", searchItem.getBestPriceUrl());
            } else {
                bestPriceLabel.setTextColor(getContext().getResources().getColor(R.color.red));
                bestPriceTextView.setTextColor(getContext().getResources().getColor(R.color.red));
            }}
        }
        return convertView;
    }



}
