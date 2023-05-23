package com.app.deliforcemanager.ModelClass.TaskDetailsApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Barcode_Data {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("isDeleted")
    @Expose
    private Integer isDeleted;
    @SerializedName("taskId")
    @Expose
    private String taskId;
    @SerializedName("barcode")
    @Expose
    private String barcode;
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

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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
