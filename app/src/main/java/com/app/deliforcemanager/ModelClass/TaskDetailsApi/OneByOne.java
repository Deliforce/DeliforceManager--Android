
package com.app.deliforcemanager.ModelClass.TaskDetailsApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OneByOne {

    @SerializedName("current")
    @Expose
    private Boolean current;
    @SerializedName("expiry")
    @Expose
    private Integer expiry;
    @SerializedName("retries")
    @Expose
    private Integer retries;

    public Boolean getCurrent() {
        return current;
    }

    public void setCurrent(Boolean current) {
        this.current = current;
    }

    public Integer getExpiry() {
        return expiry;
    }

    public void setExpiry(Integer expiry) {
        this.expiry = expiry;
    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

}
