
package com.app.deliforcemanager.ModelClass.StatusApi;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DefalutStatus {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("status")
    @Expose
    private List<Status> status = null;
    @SerializedName("sortby")
    @Expose
    private List<Sortby> sortby = null;
    @SerializedName("vehicleType")
    @Expose
    private List<VehicleType> vehicleType = null;
    @SerializedName("language")
    @Expose
    private String language;

    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Status> getStatus() {
        return status;
    }

    public void setStatus(List<Status> status) {
        this.status = status;
    }

    public List<Sortby> getSortby() {
        return sortby;
    }

    public void setSortby(List<Sortby> sortby) {
        this.sortby = sortby;
    }

    public List<VehicleType> getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(List<VehicleType> vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
