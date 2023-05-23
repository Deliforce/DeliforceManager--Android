package com.app.deliforcemanager.ModelClass.AnalyticsApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IdleTime {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("idleTime")
    @Expose
    private Integer idleTime;
    @SerializedName("activeTime")
    @Expose
    private Integer activeTime;
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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getIdleTime() {
        return idleTime;
    }

    public void setIdleTime(Integer idleTime) {
        this.idleTime = idleTime;
    }

    public Integer getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Integer activeTime) {
        this.activeTime = activeTime;
    }


}