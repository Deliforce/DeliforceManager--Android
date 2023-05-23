package com.app.deliforcemanager.ModelClass.TaskDetailsApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Note {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("isDeleted")
    @Expose
    private Integer isDeleted;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("taskId")
    @Expose
    private String taskId;
    @SerializedName("driverCognitoSub")
    @Expose
    private String driverCognitoSub;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getDriverCognitoSub() {
        return driverCognitoSub;
    }

    public void setDriverCognitoSub(String driverCognitoSub) {
        this.driverCognitoSub = driverCognitoSub;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

}