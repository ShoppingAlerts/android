package com.example.sofiya.smartshoppinglist.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

import static com.orm.SugarApp.getSugarContext;

public class SearchItem extends SugarRecord<SearchItem> implements Parcelable {
    private String searchKeywords;
    private String wantPrice;
    private String bestPriceUrl;
    private String bestPrice;

    public void setBestPriceUrl(String bestPriceUrl) {
        this.bestPriceUrl = bestPriceUrl;
    }

    public String getBestPriceUrl() {
        return bestPriceUrl;
    }

    public void setBestPrice(String bestPrice) {
        this.bestPrice = bestPrice;
    }

    public String getSearchKeywords() {
        return searchKeywords;
    }

    public String getWantPrice() {
        return wantPrice;
    }

    public String getBestPrice() {
        return bestPrice;
    }

    public SearchItem(String searchKeywords) {
        this.searchKeywords = searchKeywords;
    }

    public SearchItem(){}

    public SearchItem(String searchKeywords, String wantPrice, String bestPrice, String bestPriceUrl) {
        this.searchKeywords = searchKeywords;
        this.wantPrice = wantPrice;
        this.bestPrice = bestPrice;
        this.bestPriceUrl = bestPriceUrl;
    }
    public SearchItem(Parcel in){
        String[] data = new String[4];

        in.readStringArray(data);
        this.searchKeywords = data[0];
        this.wantPrice = data[1];
        this.bestPrice = data[2];
        this.bestPrice = data[3];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.searchKeywords, this.wantPrice, this.bestPrice, this.bestPriceUrl});
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
