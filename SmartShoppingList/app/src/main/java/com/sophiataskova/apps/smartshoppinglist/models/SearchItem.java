package com.sophiataskova.apps.smartshoppinglist.models;

import android.os.Parcel;
import android.os.Parcelable;

public class SearchItem implements Parcelable{
    public String getSearchKeywords() {
        return searchKeywords;
    }

    public SearchItem(String searchKeywords) {
        this.searchKeywords = searchKeywords;
    }


    public SearchItem(Parcel in){
        String[] data = new String[3];

        in.readStringArray(data);
        this.searchKeywords = data[0];
    }
    private String searchKeywords;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.searchKeywords});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public SearchItem createFromParcel(Parcel in) {
            return new SearchItem(in);
        }

        public SearchItem[] newArray(int size) {
            return new SearchItem[size];
        }
    };


    public static SearchItem fromSearchModel(SearchModel searchModel) {
        return new SearchItem(searchModel.keywords);
    }
}
