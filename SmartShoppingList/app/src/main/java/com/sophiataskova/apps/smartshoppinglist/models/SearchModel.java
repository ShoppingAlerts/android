package com.sophiataskova.apps.smartshoppinglist.models;

import com.orm.SugarRecord;

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
}