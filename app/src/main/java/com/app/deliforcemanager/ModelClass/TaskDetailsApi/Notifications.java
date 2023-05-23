
package com.app.deliforcemanager.ModelClass.TaskDetailsApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notifications {

    @SerializedName("Received")
    @Expose
    private Received received;
    @SerializedName("Started")
    @Expose
    private Started started;
    @SerializedName("Arrived")
    @Expose
    private Arrived arrived;
    @SerializedName("Sucess")
    @Expose
    private Sucess sucess;
    @SerializedName("Failed")
    @Expose
    private Failed failed;

    public Received getReceived() {
        return received;
    }

    public void setReceived(Received received) {
        this.received = received;
    }

    public Started getStarted() {
        return started;
    }

    public void setStarted(Started started) {
        this.started = started;
    }

    public Arrived getArrived() {
        return arrived;
    }

    public void setArrived(Arrived arrived) {
        this.arrived = arrived;
    }

    public Sucess getSucess() {
        return sucess;
    }

    public void setSucess(Sucess sucess) {
        this.sucess = sucess;
    }

    public Failed getFailed() {
        return failed;
    }

    public void setFailed(Failed failed) {
        this.failed = failed;
    }

}
