package com.example.sofiya.smartshoppinglist.models;

import com.orm.SugarRecord;

import static com.orm.SugarApp.getSugarContext;

public class SearchModel extends SugarRecord<SearchModel> {
     String keywords;
     boolean getAlerts;

    public SearchModel() {
        keywords = "";
        getAlerts = false;
    }

    public SearchModel(String keywords, boolean getAlerts) {
        this.keywords = keywords;
        this.getAlerts = getAlerts;
    }

    public static boolean exists(){
        return (getSugarContext()!=null);
    }
}