package com.app.deliforcemanager.RawHeaders.TaskInfo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DriverImages {

    @SerializedName("_id")
    String superTaskId;
    @SerializedName("driverImages")
    List<String> driverMediaList;
    @SerializedName("image")
    String imageUrl;

    public List<String> getDriverMediaList() {
        return driverMediaList;
    }

    public void setDriverMediaList(List<String> driverMediaList) {
        this.driverMediaList = driverMediaList;
    }

    public String getSuperTaskId() {
        return superTaskId;
    }

    public void setSuperTaskId(String superTaskId) {
        this.superTaskId = superTaskId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
