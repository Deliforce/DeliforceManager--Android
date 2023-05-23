package com.app.deliforcemanager.ModelClass.TaskDetailsApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OTPTaskUpdate {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("statusCode")
    @Expose
    private int statusCode;

    @SerializedName("isOTPVerificationEnable")
    @Expose
    private boolean isOTPVerificationEnable;

    @SerializedName("isEarningsModule")
    @Expose
    private boolean isEarningsModule;

    @SerializedName("otp")
    @Expose
    private String otp;

    @SerializedName("pickUpAddress")
    @Expose
    private String pickUpAddress;

    @SerializedName("deliveryAddress")
    @Expose
    private String deliveryAddress;

    @SerializedName("distanceTravelled")
    @Expose
    private Double distanceTravelled;

    @SerializedName("distance")
    @Expose
    private String distance;

    @SerializedName("travelTime")
    @Expose
    private int travelTime;

    @SerializedName("actualStartedTime")
    @Expose
    private String actualStartedTime;

    @SerializedName("actualCompletedTime")
    @Expose
    private String actualCompletedTime;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getPickUpAddress() {
        return pickUpAddress;
    }

    public void setPickUpAddress(String pickUpAddress) {
        this.pickUpAddress = pickUpAddress;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Double getDistanceTravelled() {
        return distanceTravelled;
    }

    public void setDistanceTravelled(Double distanceTravelled) {
        this.distanceTravelled = distanceTravelled;
    }

    public int getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(int travelTime) {
        this.travelTime = travelTime;
    }

    public String getActualStartedTime() {
        return actualStartedTime;
    }

    public void setActualStartedTime(String actualStartedTime) {
        this.actualStartedTime = actualStartedTime;
    }

    public String getActualCompletedTime() {
        return actualCompletedTime;
    }

    public void setActualCompletedTime(String actualCompletedTime) {
        this.actualCompletedTime = actualCompletedTime;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @SerializedName("amount")
    @Expose
    private String amount;

    @SerializedName("currency")
    @Expose
    private String currency;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatuscode() {
        return statusCode;
    }

    public void setStatuscode(int statuscode) {
        this.statusCode = statuscode;
    }

    public boolean isOTPVerificationEnable() {
        return isOTPVerificationEnable;
    }

    public void setOTPVerificationEnable(boolean OTPVerificationEnable) {
        isOTPVerificationEnable = OTPVerificationEnable;
    }

    public boolean isEarningsModule() {
        return isEarningsModule;
    }

    public void setEarningsModule(boolean earningsModule) {
        isEarningsModule = earningsModule;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

}