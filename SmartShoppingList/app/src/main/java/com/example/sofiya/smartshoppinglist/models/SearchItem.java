package com.example.sofiya.smartshoppinglist.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

import static com.orm.SugarApp.getSugarContext;

public class SearchItem extends SugarRecord<SearchItem> implements Parcelable {
    private String searchKeywords;
    private String maxPrice;


    public String getSearchKeywords() {
        return searchKeywords;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public SearchItem(String searchKeywords) {
        this.searchKeywords = searchKeywords;
    }

    public SearchItem(){}

    public SearchItem(String searchKeywords, String price) {
        this.searchKeywords = searchKeywords;
        this.maxPrice = price;
    }
    public SearchItem(Parcel in){
        String[] data = new String[2];

        in.readStringArray(data);
        this.searchKeywords = data[0];
        this.maxPrice = data[1];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.searchKeywords, this.maxPrice});
    }

    public static final Creator CREATOR = new Creator() {
        public SearchItem createFromParcel(Parcel in) {
            return new SearchItem(in);
        }

        public SearchItem[] newArray(int size) {
            return new SearchItem[size];
        }
    };

    public static boolean exists() {
        return (getSugarContext()!=null);
    }
}
