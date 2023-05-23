package com.app.deliforcemanager.ModelClass.TaskDetailsApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateSignature {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("driverSignature")
    @Expose
    private String driverSignature;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDriverSignature() {
        return driverSignature;
    }

    public void setDriverSignature(String driverSignature) {
        this.driverSignature = driverSignature;
    }

}