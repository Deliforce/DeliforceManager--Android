
package com.app.deliforcemanager.ModelClass.IdleDistance;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnalyticsApi {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("idleDist")
    @Expose
    private double idleDist;
    @SerializedName("idleTime")
    @Expose
    private double minutes;
    @SerializedName("dateStamp")
    @Expose
    private String dateStamp;
    @SerializedName("driverId")
    @Expose
    private String driverId;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public double getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public String getDateStamp() {
        return dateStamp;
    }

    public void setDateStamp(String dateStamp) {
        this.dateStamp = dateStamp;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

}
