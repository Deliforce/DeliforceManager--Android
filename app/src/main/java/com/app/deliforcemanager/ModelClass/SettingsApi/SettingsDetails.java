package com.app.deliforcemanager.ModelClass.SettingsApi;

import com.google.gson.annotations.SerializedName;

public class SettingsDetails {

    @SerializedName("_id")
    private String id;
    @SerializedName("settings")
    private SettingsInfo settingsInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SettingsInfo getSettingsInfo() {
        return settingsInfo;
    }

    public void setSettingsInfo(SettingsInfo settingsInfo) {
        this.settingsInfo = settingsInfo;
    }
}
