package com.app.deliforcemanager.ModelClass.SettingsApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NavigationTypes {
    @SerializedName("default")
    @Expose
    private int isDefault;
    @SerializedName("map")
    @Expose
    private String map;

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }
}
