package com.app.deliforcemanager.ModelClass.CreateTask;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverImageOption {

    @SerializedName("exist")
    @Expose
    private boolean exist;

    @SerializedName("isMandatory")
    @Expose
    private boolean isMandatory;

    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public void setMandatory(boolean mandatory) {
        isMandatory = mandatory;
    }

}
