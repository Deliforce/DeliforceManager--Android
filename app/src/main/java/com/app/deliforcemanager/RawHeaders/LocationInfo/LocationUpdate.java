package com.app.deliforcemanager.RawHeaders.LocationInfo;

import com.google.gson.annotations.SerializedName;

public class LocationUpdate {

    @SerializedName("deviceToken")
    private String fcmRefreshToken;
    @SerializedName("location")
    CurrentLocation currentLocation;
    @SerializedName("batteryState")
    private int batteryStatus;
    @SerializedName("deviceName")
    private String deviceModelName;
    @SerializedName("appVersion")
    private String appVersion;
    @SerializedName("timezone")
    private String timezone;
    @SerializedName("driverStatus")
    private int driverStatus;

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    @SerializedName("deviceType")
    private int deviceType;

    public int getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(int driverStatus) {
        this.driverStatus = driverStatus;
    }

    public String getFcmRefreshToken() {
        return fcmRefreshToken;
    }

    public void setFcmRefreshToken(String fcmRefreshToken) {
        this.fcmRefreshToken = fcmRefreshToken;
    }

    public CurrentLocation getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(CurrentLocation currentLocation) {
        this.currentLocation = currentLocation;
    }

    public int getBatteryStatus() {
        return batteryStatus;
    }

    public void setBatteryStatus(int batteryStatus) {
        this.batteryStatus = batteryStatus;
    }

    public String getDeviceModelName() {
        return deviceModelName;
    }

    public void setDeviceModelName(String deviceModelName) {
        this.deviceModelName = deviceModelName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
}
