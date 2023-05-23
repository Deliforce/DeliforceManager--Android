package com.app.deliforcemanager.ModelClass.TaskDetailsApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddSingature {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("driverSignature")
    @Expose
    private String driverSignature;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDriverSignature() {
        return driverSignature;
    }

    public void setDriverSignature(String driverSignature) {
        this.driverSignature = driverSignature;
    }

}