package com.example.sofiya.smartshoppinglist;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sofiya.smartshoppinglist.models.EbayItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EbayItemsArrayAdapter extends ArrayAdapter<EbayItem> {

    public EbayItemsArrayAdapter(Context context, ArrayList<EbayItem> ebayItems) {
        super(context, 0, ebayItems);
    }

    // todo viewholder pattern

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final EbayItem ebayItem = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ebay_item, parent, false);
        }
        ImageView itemPhoto = (ImageView) convertView.findViewById(R.id.item_photo);
        TextView title = (TextView) convertView.findViewById(R.id.item_title);
        TextView timeStamp = (TextView) convertView.findViewById(R.id.price);
        if (title != null) {
            title.setText(ebayItem.getTitle());
            timeStamp.setText("$"+ebayItem.getPrice());
            itemPhoto.setImageResource(android.R.color.transparent);
            Picasso.with(getContext()).load(ebayItem.getImageUrl()).into(itemPhoto);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ebayItem.getUrl()));
               getContext().startActivity(browserIntent);
            }
        });

        return convertView;
    }
}
