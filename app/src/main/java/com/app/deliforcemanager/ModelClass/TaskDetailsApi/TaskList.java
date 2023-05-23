
package com.app.deliforcemanager.ModelClass.TaskDetailsApi;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TaskList {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("isPickup")
    @Expose
    private Boolean isPickup;
    @SerializedName("priority")
    @Expose
    private Integer priority;
    @SerializedName("taskId")
    @Expose
    private String taskId;
    @SerializedName("isDeleted")
    @Expose
    private Integer isDeleted;
    @SerializedName("delay")
    @Expose
    private Integer delay;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("FlatNo")
    @Expose
    private String FlatNo ="";
    @SerializedName("images")
    @Expose
    private List<Object> images = null;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("orderId")
    @Expose
    private String orderId;
    @SerializedName("driver")
    @Expose
    private String driver;
    @SerializedName("taskColor")
    @Expose
    private String color;
    @SerializedName("timezone")
    @Expose
    private String timezone;
    @SerializedName("address")
    @Expose
    private Address address;
    @SerializedName("businessType")
    @Expose
    private Integer businessType;
    @SerializedName("userRole")
    @Expose
    private Integer userRole;
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("clientId")
    @Expose
    private String clientId;
    @SerializedName("taskStatus")
    @Expose
    private Integer taskStatus;
    @SerializedName("glympseId")
    @Expose
    private String glympseId;



    @SerializedName("settings")
    @Expose
    private SettingsModel settings;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public String getFlatNo() {
        return FlatNo;
    }

    public void setFlatNo(String flatNo) {
        FlatNo = flatNo;
    }

    @SerializedName("referenceId")
    @Expose
    private String referenceId;

    public String getId() {
        return id;
    }

    public String getGlympseId() {
        return glympseId;
    }

    public void setGlympseId(String glympseId) {
        this.glympseId = glympseId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getIsPickup() {
        return isPickup;
    }

    public void setIsPickup(Boolean isPickup) {
        this.isPickup = isPickup;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Object> getImages() {
        return images;
    }

    public void setImages(List<Object> images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
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

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    public SettingsModel getSettings() {
        return settings;
    }

    public void setSettings(SettingsModel settings) {
        this.settings = settings;
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

    public Boolean getPickup() {
        return isPickup;
    }

    public void setPickup(Boolean pickup) {
        isPickup = pickup;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }
}
