
package com.app.deliforcemanager.ModelClass.TaskDetailsApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Started {

    @SerializedName("web")
    @Expose
    private Boolean web;
    @SerializedName("sms")
    @Expose
    private Boolean sms;
    @SerializedName("email")
    @Expose
    private Boolean email;
    @SerializedName("smsTemp")
    @Expose
    private String smsTemp;
    @SerializedName("mailTemp")
    @Expose
    private String mailTemp;

    public Boolean getWeb() {
        return web;
    }

    public void setWeb(Boolean web) {
        this.web = web;
    }

    public Boolean getSms() {
        return sms;
    }

    public void setSms(Boolean sms) {
        this.sms = sms;
    }

    public Boolean getEmail() {
        return email;
    }

    public void setEmail(Boolean email) {
        this.email = email;
    }

    public String getSmsTemp() {
        return smsTemp;
    }

    public void setSmsTemp(String smsTemp) {
        this.smsTemp = smsTemp;
    }

    public String getMailTemp() {
        return mailTemp;
    }

    public void setMailTemp(String mailTemp) {
        this.mailTemp = mailTemp;
    }

}
