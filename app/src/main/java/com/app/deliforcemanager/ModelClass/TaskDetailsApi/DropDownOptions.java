package com.app.deliforcemanager.ModelClass.TaskDetailsApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DropDownOptions {

    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("id")
    @Expose
    private String id;

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return value;
    }
}
