package com.app.deliforcemanager.ModelClass.ManagerTaskList;

import com.app.deliforcemanager.ModelClass.TaskDetailsApi.Address;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Doc {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("isDeleted")
    @Expose
    private Integer isDeleted;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("address")
    @Expose
    private Address address;
    @SerializedName("taskStatus")
    @Expose
    private Integer taskStatus;
    @SerializedName("timezone")
    @Expose
    private String timezone;


    public String getTaskColor() {
        return taskColor;
    }

    public void setTaskColor(String taskColor) {
        this.taskColor = taskColor;
    }

    @SerializedName("taskColor")
    @Expose
    private String taskColor;



    @SerializedName("businessType")
    @Expose
    private Integer businessType;
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("clientId")
    @Expose
    private String clientId;
    @SerializedName("taskId")
    @Expose
    private String taskId;
    @SerializedName("team")
    @Expose
    private String team;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("driver")
    @Expose
    private String driver;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("isPickup")
    @Expose
    private Boolean isPickup;

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

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getIsPickup() {
        return isPickup;
    }

    public void setIsPickup(Boolean isPickup) {
        this.isPickup = isPickup;
    }
}
