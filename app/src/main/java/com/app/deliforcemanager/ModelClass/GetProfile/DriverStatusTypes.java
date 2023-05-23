
package com.app.deliforcemanager.ModelClass.GetProfile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverStatusTypes {

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("mobileStatus")
    @Expose
    private String mobileStatus;
    @SerializedName("id")
    @Expose
    private Integer id;

    public String getMobileStatus() {
        return mobileStatus;
    }

    public void setMobileStatus(String mobileStatus) {
        this.mobileStatus = mobileStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
