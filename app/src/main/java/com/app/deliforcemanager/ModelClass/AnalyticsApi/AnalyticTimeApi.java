package com.app.deliforcemanager.ModelClass.AnalyticsApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AnalyticTimeApi {
    @SerializedName("json_data")
    @Expose
    private List<IdleTime> jsonData = null;

    @SerializedName("totalAciveTime")
    @Expose
    private Integer totalAciveTime;
    @SerializedName("totalIdleTime")
    @Expose
    private Integer totalIdleTime;

    public Integer getTotalIdleTime() {
        return totalIdleTime;
    }

    public void setTotalIdleTime(Integer totalIdleTime) {
        this.totalIdleTime = totalIdleTime;
    }


    public Integer getTotalAciveTime() {
        return totalAciveTime;
    }

    public void setTotalAciveTime(Integer totalAciveTime) {
        this.totalAciveTime = totalAciveTime;
    }


    public List<IdleTime> getJsonData() {
        return jsonData;
    }

    public void setJsonData(List<IdleTime> jsonData) {
        this.jsonData = jsonData;
    }


}
