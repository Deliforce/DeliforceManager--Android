package com.app.deliforcemanager.ModelClass;

import com.google.gson.annotations.SerializedName;

public class MyQueryParams {

    public Boolean getEnableBlocked() {
        return enableBlocked;
    }

    public void setEnableBlocked(Boolean enableBlocked) {
        this.enableBlocked = enableBlocked;
    }

    @SerializedName("enableBlocked")
    private Boolean enableBlocked;


    // Add getters and setters

    public MyQueryParams(Boolean enableBlocked) {
        this.enableBlocked = enableBlocked;

    }
}
