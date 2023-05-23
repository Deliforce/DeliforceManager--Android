package com.app.deliforcemanager.RawHeaders.Analytics;

import com.app.deliforcemanager.ModelClass.TrackingRecord.TrackingModelData;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AnalyticsData {

    @SerializedName("date")
    private String date;

    @SerializedName("idleDist")
    private double idleDist;

    @SerializedName("idleTime")
    private double idleTime;

    @SerializedName("driverIdleLog")
    private List<TrackingModelData> driverIdleLog;

    public int getBatteryState() {
        return batteryState;
    }

    public void setBatteryState(int batteryState) {
        this.batteryState = batteryState;
    }

    @SerializedName("versionName")
    private String versionName;

    public String getCurrentVersion() {
        return versionName;
    }

    public void setCurrentVersion(String versionName) {
        this.versionName = versionName;
    }

    @SerializedName("batteryState")
    private int batteryState;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getIdleDist() {
        return idleDist;
    }

    public void setIdleDist(double idleDist) {
        this.idleDist = idleDist;
    }

    public double getIdleTime() {
        return idleTime;
    }

    public void setIdleTime(double idleTime) {
        this.idleTime = idleTime;
    }

    public void setDriverIdleLog(List<TrackingModelData> driverIdleLog) {
        this.driverIdleLog = driverIdleLog;
    }

    public List<TrackingModelData> getDriverIdleLog() {
        return driverIdleLog;
    }
}
