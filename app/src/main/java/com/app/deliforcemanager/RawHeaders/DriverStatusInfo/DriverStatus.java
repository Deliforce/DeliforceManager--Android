package com.app.deliforcemanager.RawHeaders.DriverStatusInfo;

import com.app.deliforcemanager.ModelClass.TrackingRecord.LiveTrackingModelData;
import com.app.deliforcemanager.ModelClass.TrackingRecord.TrackingModelData;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Set;

public class DriverStatus {

    @SerializedName("status")
    private int driverStatus;
    @SerializedName("location_lat")
    private double currentLat;
    @SerializedName("location_lng")
    private double currentLan;
    @SerializedName("batteryState")
    private int batteryStatus;
    @SerializedName("deviceName")
    private String deviceModelName;
    @SerializedName("driverLiveLog")
    private List<LiveTrackingModelData> driverLiveLog;
    @SerializedName("taskids")
    private Set<String> taskIds = null;

    public String getAddress() {
        return address;
    }

    @SerializedName("formattedAddress")
    private String address;
    @SerializedName("adminArray")
    private List<String> adminArray = null;
    @SerializedName("topic")
    private String topic;
    @SerializedName("driverId")
    private String driver_id;
    @SerializedName("distance")
    private String distance;
    @SerializedName("eta")
    private String eta;
    @SerializedName("timezone")
    private String timezone;

    @SerializedName("driverIdleLog")
    private List<TrackingModelData> driverIdleLog;

    @SerializedName("versionName")
    private String versionName;
    @SerializedName("offlineDate")
    private String offlineDate;

    public String getOfflineDate() {
        return offlineDate;
    }

    public void setOfflineDate(String offlineDate) {
        this.offlineDate = offlineDate;
    }

    public int getBatteryStatus() {
        return batteryStatus;
    }

    public String getDeviceModelName() {
        return deviceModelName;
    }

    public String getTopic() {
        return topic;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getCurrentVersion() {
        return versionName;
    }

    public void setCurrentVersion(String versionName) {
        this.versionName = versionName;
    }

    public boolean isLogOut() {
        return isLogOut;
    }

    public void setLogOut(boolean logOut) {
        isLogOut = logOut;
    }

    @SerializedName("isLogOut")
    private boolean isLogOut;

    public int getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(int driverStatus) {
        this.driverStatus = driverStatus;
    }

    public double getCurrentLat() {
        return currentLat;
    }

    public void setCurrentLat(double currentLat) {
        this.currentLat = currentLat;
    }

    public double getCurrentLan() {
        return currentLan;
    }

    public void setCurrentLan(double currentLan) {
        this.currentLan = currentLan;
    }

    public void setBatteryStatus(int batteryStatus) {
        this.batteryStatus = batteryStatus;
    }

    public void setDeviceModelName(String deviceModelName) {
        this.deviceModelName = deviceModelName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getAdminArray() {
        return adminArray;
    }

    public void setAdminArray(List<String> adminArray) {
        this.adminArray = adminArray;
    }

    public void setDriverIdleLog(List<TrackingModelData> driverIdleLog) {
        this.driverIdleLog = driverIdleLog;
    }

    public List<TrackingModelData> getDriverIdleLog() {
        return driverIdleLog;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    public void setDriverLiveLog(List<LiveTrackingModelData> driverLiveLog) {
        this.driverLiveLog = driverLiveLog;
    }

    public List<LiveTrackingModelData> getDriverLiveLog() {
        return driverLiveLog;
    }

    public void setTaskIds(Set taskIds) {
        this.taskIds = taskIds;
    }

    public Set getTaskIds() {
        return taskIds;
    }
}
