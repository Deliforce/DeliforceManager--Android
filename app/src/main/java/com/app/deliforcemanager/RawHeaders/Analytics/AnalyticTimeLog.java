package com.app.deliforcemanager.RawHeaders.Analytics;

import com.google.gson.annotations.SerializedName;

public class AnalyticTimeLog {

    @SerializedName("dateFilter")
    private int dateFilter;

    public int getDateFilter() {
        return dateFilter;
    }

    public void setDateFilter(int dateFilter) {
        this.dateFilter = dateFilter;
    }


}
