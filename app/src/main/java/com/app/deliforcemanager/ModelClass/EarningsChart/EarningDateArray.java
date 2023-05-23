package com.app.deliforcemanager.ModelClass.EarningsChart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EarningDateArray {


    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("count")
    @Expose
    private String count;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
