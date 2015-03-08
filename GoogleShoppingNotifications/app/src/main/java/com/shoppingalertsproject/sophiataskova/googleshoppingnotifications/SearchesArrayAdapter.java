package com.shoppingalertsproject.sophiataskova.googleshoppingnotifications;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shoppingalertsproject.sophiataskova.googleshoppingnotifications.models.Search;

import java.util.ArrayList;

public class SearchesArrayAdapter extends ArrayAdapter<Search> {

    public SearchesArrayAdapter(Context context, ArrayList<Search> tweets) {
        super(context, 0, tweets);
    }

    // todo viewholder pattern

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Search tweet = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_search, parent, false);
        }
//        ImageView profilePic = (ImageView) convertView.findViewById(R.id.profile_photo);
        TextView userName = (TextView) convertView.findViewById(R.id.user);
        TextView name = (TextView) convertView.findViewById(R.id.user_name);
        TextView body = (TextView) convertView.findViewById(R.id.body);
        TextView timeStamp = (TextView) convertView.findViewById(R.id.time_stamp);
        if (userName != null) {
            userName.setText("@" + tweet.getUser().getScreenName());

            name.setText(tweet.getUser().getName());
            body.setText(tweet.getBody());
            profilePic.setImageResource(android.R.color.transparent);
            timeStamp.setText(tweet.getCreatedAt());
            Picasso.with(getContext()).load(tweet.getUser().getProfilePicUrl()).into(profilePic);
        }

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                intent.putExtra("username", tweet.getUser().getScreenName());
                getContext().startActivity(intent);
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TweetDetailsActivity.class);
                intent.putExtra("id", tweet.getUid());
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }
}
