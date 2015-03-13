package com.example.sofiya.smartshoppinglist.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

import static com.orm.SugarApp.getSugarContext;

public class SearchItem extends SugarRecord<SearchItem> implements Parcelable {
    private String searchKeywords;
    private boolean getAlerts;

    public String getSearchKeywords() {
        return searchKeywords;
    }

    public SearchItem(String searchKeywords) {
        this.searchKeywords = searchKeywords;
        getAlerts = false;
    }

    public SearchItem(){}

    public SearchItem(String searchKeywords, boolean getAlerts) {
        this.searchKeywords = searchKeywords;
        this.getAlerts = getAlerts;
    }
    public SearchItem(Parcel in){
        String[] data = new String[3];

        in.readStringArray(data);
        this.searchKeywords = data[0];
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.searchKeywords});
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

//
//    public static SearchItem fromSearchModel(SearchModel searchModel) {
//        return new SearchItem(searchModel.keywords);
//    }
}
