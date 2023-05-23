package com.app.deliforcemanager.RawHeaders.AutoAllocationInfo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AutoAllocationData {

    @SerializedName("_id")
    private String id;
    @SerializedName("taskStatus")
    private int taskStatus;
    @SerializedName("driverName")
    private String driver_name;

    @SerializedName("imgUrl")
    private String imgUrl;

    @SerializedName("glympseTrackingURL")
    private String glympseTrackingURL;


    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @SerializedName("reason")
    private String reason;


    public List<String> getAdminArray() {
        return adminArray;
    }

    public void setAdminArray(List<String> adminArray) {
        this.adminArray = adminArray;
    }

    @SerializedName("adminArray")
    private List<String> adminArray = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTaskStatus() {
        return taskStatus;
    }


    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }
    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
    }


    public String getGlympseTrackingURL() {
        return glympseTrackingURL;
    }

    public void setGlympseTrackingURL(String glympseTrackingURL) {
        this.glympseTrackingURL = glympseTrackingURL;
    }
}
