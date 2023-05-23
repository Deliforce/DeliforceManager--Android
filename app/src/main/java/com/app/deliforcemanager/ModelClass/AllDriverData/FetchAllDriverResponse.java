package com.app.deliforcemanager.ModelClass.AllDriverData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FetchAllDriverResponse {

    @SerializedName("role")
    @Expose
    private Integer role;
    @SerializedName("driverStatus")
    @Expose
    private Integer driverStatus;
    @SerializedName("isDeleted")
    @Expose
    private Integer isDeleted;
    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("taskLimit")
    @Expose
    private Integer taskLimit;
    @SerializedName("multipleImages")
    @Expose
    private List<Object> multipleImages;
    @SerializedName("driverType")
    @Expose
    private Integer driverType;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("assignTeam")
    @Expose
    private AssignTeam assignTeam;
    @SerializedName("domain")
    @Expose
    private String domain;

    @SerializedName("clientId")
    @Expose
    private String clientId;
    @SerializedName("cognitoSub")
    @Expose
    private String cognitoSub;
    @SerializedName("userRole")
    @Expose
    private Integer userRole;
    @SerializedName("user")
    @Expose
    private String user;

    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("appVersion")
    @Expose
    private String appVersion;
    @SerializedName("batteryState")
    @Expose
    private Integer batteryState;
    @SerializedName("currentAddress")
    @Expose
    private String currentAddress;
    @SerializedName("deviceName")
    @Expose
    private String deviceName;
    @SerializedName("deviceToken")
    @Expose
    private String deviceToken;
    @SerializedName("endpointArn")
    @Expose
    private String endpointArn;
    @SerializedName("timezone")
    @Expose
    private String timezone;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("isBlocked")
    @Expose
    private Integer isBlocked;

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Integer getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(Integer driverStatus) {
        this.driverStatus = driverStatus;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }





    public Integer getTaskLimit() {
        return taskLimit;
    }

    public void setTaskLimit(Integer taskLimit) {
        this.taskLimit = taskLimit;
    }

    public List<Object> getMultipleImages() {
        return multipleImages;
    }

    public void setMultipleImages(List<Object> multipleImages) {
        this.multipleImages = multipleImages;
    }

    public Integer getDriverType() {
        return driverType;
    }

    public void setDriverType(Integer driverType) {
        this.driverType = driverType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AssignTeam getAssignTeam() {
        return assignTeam;
    }

    public void setAssignTeam(AssignTeam assignTeam) {
        this.assignTeam = assignTeam;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }



    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getCognitoSub() {
        return cognitoSub;
    }

    public void setCognitoSub(String cognitoSub) {
        this.cognitoSub = cognitoSub;
    }

    public Integer getUserRole() {
        return userRole;
    }

    public void setUserRole(Integer userRole) {
        this.userRole = userRole;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public Integer getBatteryState() {
        return batteryState;
    }

    public void setBatteryState(Integer batteryState) {
        this.batteryState = batteryState;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getEndpointArn() {
        return endpointArn;
    }

    public void setEndpointArn(String endpointArn) {
        this.endpointArn = endpointArn;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Integer isBlocked) {
        this.isBlocked = isBlocked;
    }

}
