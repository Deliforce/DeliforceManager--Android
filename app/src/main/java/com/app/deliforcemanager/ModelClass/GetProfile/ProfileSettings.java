package com.app.deliforcemanager.ModelClass.GetProfile;

import com.app.deliforcemanager.ModelClass.SettingsApi.NavigationTypes;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProfileSettings {

    @SerializedName("language")
    @Expose
    private Integer language;
    @SerializedName("mapStyle")
    @Expose
    private Integer mapStyle;
    @SerializedName("navigation")
    @Expose
    private Integer navigation;
    @SerializedName("navigationHelper")
    @Expose
    private Boolean navigationHelper;
    @SerializedName("powerSavingModel")
    @Expose
    private Boolean powerSavingModel;
    @SerializedName("repeat")
    @Expose
    private Boolean repeat;
    @SerializedName("ringtone")
    @Expose
    private Integer ringtone;
    @SerializedName("vibration")
    @Expose
    private Integer vibration;

    @SerializedName("navigationTypes")
    @Expose
    private ArrayList<NavigationTypes> navigationTypes;

    public ArrayList<NavigationTypes> getNavigationTypes() {
        return navigationTypes;
    }

    public void setNavigationTypes(ArrayList<NavigationTypes> navigationTypes) {
        this.navigationTypes = navigationTypes;
    }

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }

    public Integer getMapStyle() {
        return mapStyle;
    }

    public void setMapStyle(Integer mapStyle) {
        this.mapStyle = mapStyle;
    }

    public Integer getNavigation() {
        return navigation;
    }

    public void setNavigation(Integer navigation) {
        this.navigation = navigation;
    }

    public Boolean getNavigationHelper() {
        return navigationHelper;
    }

    public void setNavigationHelper(Boolean navigationHelper) {
        this.navigationHelper = navigationHelper;
    }

    public Boolean getPowerSavingModel() {
        return powerSavingModel;
    }

    public void setPowerSavingModel(Boolean powerSavingModel) {
        this.powerSavingModel = powerSavingModel;
    }

    public Boolean getRepeat() {
        return repeat;
    }

    public void setRepeat(Boolean repeat) {
        this.repeat = repeat;
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

}
