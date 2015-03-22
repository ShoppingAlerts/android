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

    public static void setPriceFilter(String priceFilter) {
        EbayItemsArrayAdapter.priceFilter = priceFilter;
    }

    private static String priceFilter = "";

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
        View shopButton = convertView.findViewById(R.id.buy);
        View alertButton = convertView.findViewById(R.id.add_alert);

//        final ImageButton addButton = (ImageButton) convertView.findViewById(R.id.add_button);
        if (title != null) {
            title.setText(ebayItem.getTitle());
            timeStamp.setText("$"+ebayItem.getPrice());
            itemPhoto.setImageResource(android.R.color.transparent);
            Picasso.with(getContext()).load(ebayItem.getImageUrl()).into(itemPhoto);
        }
        shopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ebayItem.getUrl()));
               getContext().startActivity(browserIntent);
            }
        });

        return convertView;
    }

//    @Override
//    public EbayItem onCreateViewHolder(ViewGroup parent, int viewType) {
//        return null;
//    }
//
//    @Override
//    public void onBindViewHolder(EbayItem holder, int position) {
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return 0;
//    }
//

//    public final static class VH extends RecyclerView.ViewHolder {
//        final View rootView;
//        final ImageView imageView;
//        final TextView tvName;
//        final TextView tvPrice;
//        final ImageButton shopButton;
//        final ImageButton alertButton;
//
//        public VH(View itemView, final Context context) {
//            super(itemView);
//            rootView = itemView;
//            imageView = (ImageView)itemView.findViewById(R.id.item_photo);
//            tvName = (TextView)itemView.findViewById(R.id.item_title);
//            tvPrice = (TextView) itemView.findViewById(R.id.price);
//            shopButton = (ImageButton) itemView.findViewById(R.id.buy);
//            alertButton = (ImageButton) itemView.findViewById(R.id.add_alert);
//
//            shopButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    final EbayItem ebayItem = (EbayItem)v.getTag();
//                    if (ebayItem != null) {
//                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ebayItem.getUrl()));
//                        context.startActivity(browserIntent);
//                    }
//                }
//            });
//            if (tvName != null) {
//                final EbayItem ebayItem = (EbayItem)itemView.getTag();
//                if (ebayItem != null) {
//                    tvName.setText(ebayItem.getTitle());
//                    tvPrice.setText("$" + ebayItem.getPrice());
//                    imageView.setImageResource(android.R.color.transparent);
//                    Picasso.with(context).load(ebayItem.getImageUrl()).into(imageView);
//                }
//            }
//        }
//    }

}
