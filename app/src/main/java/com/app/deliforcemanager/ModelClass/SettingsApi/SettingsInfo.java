package com.app.deliforcemanager.ModelClass.SettingsApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SettingsInfo {

    @SerializedName("transportType")
    private Integer vehicleType;
    @SerializedName("ringtone")
    private Integer ringtone;
    @SerializedName("vibration")
    private Integer vibration;
    @SerializedName("repeat")
    private Boolean repeat;
    @SerializedName("language")
    private Integer language;
    @SerializedName("navigation")
    private Integer navigation;
    @SerializedName("mapStyle")
    private Integer mapStyle;
    @SerializedName("showTraffic")
    private Boolean showTraffic;
    @SerializedName("powerSavingModel")
    private Boolean powerSavingModel;
    @SerializedName("navigationHelper")
    private Boolean navigationHelper;
    @SerializedName("navigationTypes")
    @Expose
    private ArrayList<NavigationTypes> navigationTypes;

    public ArrayList<NavigationTypes> getNavigationTypes() {
        return navigationTypes;
    }

    public void setNavigationTypes(ArrayList<NavigationTypes> navigationTypes) {
        this.navigationTypes = navigationTypes;
    }

    public Integer getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(Integer vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Integer getRingtone() {
        return ringtone;
    }

    public void setRingtone(Integer ringtone) {
        this.ringtone = ringtone;
    }

    public Integer getVibration() {
        return vibration;
    }

    public void setVibration(Integer vibration) {
        this.vibration = vibration;
    }

    public Boolean getRepeat() {
        return repeat;
    }

    public void setRepeat(Boolean repeat) {
        this.repeat = repeat;
    }

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }

    public Integer getNavigation() {
        return navigation;
    }

    public void setNavigation(Integer navigation) {
        this.navigation = navigation;
    }

    public Integer getMapStyle() {
        return mapStyle;
    }

    public void setMapStyle(Integer mapStyle) {
        this.mapStyle = mapStyle;
    }

    public Boolean getShowTraffic() {
        return showTraffic;
    }

    public void setShowTraffic(Boolean showTraffic) {
        this.showTraffic = showTraffic;
    }

    public Boolean getPowerSavingModel() {
        return powerSavingModel;
    }

    public void setPowerSavingModel(Boolean powerSavingModel) {
        this.powerSavingModel = powerSavingModel;
    }

    public Boolean getNavigationHelper() {
        return navigationHelper;
    }

    public void setNavigationHelper(Boolean navigationHelper) {
        this.navigationHelper = navigationHelper;
    }
}
