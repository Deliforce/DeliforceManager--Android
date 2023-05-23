package com.app.deliforcemanager.ModelClass.DriverStatusApi;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverStatusUpdate {


    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

