package com.app.deliforcemanager.ModelClass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TaskNotificationModel implements Serializable {

    private int acknowledgementType;

    private String auto;

    private String endDate;

    private String sound;

    private Boolean isPickUp;

    private String OrderId;

    private String title;

    private String isGlympseEnable;

    private String alert;

    private String startTime;

    private String _id;

    private String CustomerName;

    private String endTime;

    private String businessType;

    private String glympseId;

    private String taskStatus;

    private String Startdate;

    private String status;

    private String CustomerAddress;

    private String expiry;

    private String request;
    private String driverStatus;
    private String message;
    private String jobAmount;

    @SerializedName("referenceId")
    private String referenceId;

    public String getJobAmount() {
        return jobAmount;
    }

    public void setJobAmount(String jobAmount) {
        this.jobAmount = jobAmount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getAcknowledgementType() {
        return acknowledgementType;
    }

    public void setAcknowledgementType(int acknowledgementType) {
        this.acknowledgementType = acknowledgementType;
    }

    public String getAuto() {
        return auto;
    }

    public void setAuto(String auto) {
        this.auto = auto;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public Boolean getIsPickUp() {
        return isPickUp;
    }

    public void setIsPickUp(Boolean isPickUp) {
        this.isPickUp = isPickUp;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String OrderId) {
        this.OrderId = OrderId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsGlympseEnable() {
        return isGlympseEnable;
    }

    public void setIsGlympseEnable(String isGlympseEnable) {
        this.isGlympseEnable = isGlympseEnable;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String CustomerName) {
        this.CustomerName = CustomerName;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getGlympseId() {
        return glympseId;
    }

    public void setGlympseId(String glympseId) {
        this.glympseId = glympseId;
    }


    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getStartdate() {
        return Startdate;
    }

    public void setStartdate(String Startdate) {
        this.Startdate = Startdate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomerAddress() {
        return CustomerAddress;
    }

    public void setCustomerAddress(String CustomerAddress) {
        this.CustomerAddress = CustomerAddress;
    }

    public Boolean getPickUp() {
        return isPickUp;
    }

    public void setPickUp(Boolean pickUp) {
        isPickUp = pickUp;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(String driverStatus) {
        this.driverStatus = driverStatus;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }
}
