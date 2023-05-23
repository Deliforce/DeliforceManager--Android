package com.app.deliforcemanager.ModelClass.EarningsChart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EarningsList {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("taskId")
    @Expose
    private String taskId;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("taskStatus")
    @Expose
    private Integer taskStatus;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("completedTimeString")
    @Expose
    private String completedTimeString;
    @SerializedName("individualAmount")
    @Expose
    private String individualAmount;
    @SerializedName("bussinessTypeName")
    @Expose
    private String bussinessTypeName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCompletedTimeString() {
        return completedTimeString;
    }

    public void setCompletedTimeString(String completedTimeString) {
        this.completedTimeString = completedTimeString;
    }

    public String getIndividualAmount() {
        return individualAmount;
    }

    public void setIndividualAmount(String individualAmount) {
        this.individualAmount = individualAmount;
    }

    public String getBussinessTypeName() {
        return bussinessTypeName;
    }

    public void setBussinessTypeName(String bussinessTypeName) {
        this.bussinessTypeName = bussinessTypeName;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
