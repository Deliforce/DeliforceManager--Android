package com.app.deliforcemanager.ModelClass.TaskDetailsApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TaskOneDetails {

    @SerializedName("startLocation")
    @Expose
    private StartLocation startLocation;
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
    private String FlatNo = "";

    @SerializedName("images")
    @Expose
    private List<String> images = null;
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
    @SerializedName("settings")
    @Expose
    private SettingsModel settings;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public String getCustomerNotes() {
        return customerNotes;
    }

    public void setCustomerNotes(String customerNotes) {
        this.customerNotes = customerNotes;
    }

    @SerializedName("customerNotes")
    @Expose
    private String customerNotes;

    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("notes")
    @Expose
    private List<Note> notes = new ArrayList<>();

    @SerializedName("driverImages")
    @Expose
    private List<String> driverImages = null;
    @SerializedName("driverSignature")
    @Expose
    private String driverSignature;



    @SerializedName("actualStartedTime")
    @Expose
    private String actualStartedTime;

    @SerializedName("actualCompletedTime")
    @Expose
    private String actualCompletedTime;

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

    public String getTrackingLink() {
        return trackingLink;
    }

    public void setTrackingLink(String trackingLink) {
        this.trackingLink = trackingLink;
    }

    @SerializedName("trackingLink")
    @Expose
    private String trackingLink;

    @SerializedName("description")
    private String taskDescription;





    @SerializedName("templateName")
    private String templateName;

    @SerializedName("templateLength")
    private int templateLength;

    @SerializedName("referenceId")
    private String referenceId;

    @SerializedName("isCaptureLocation")
    private boolean isCaptureLocation;

    @SerializedName("captureLocation")
    private CaptureLocation CaptureLocation;

    public String getFlatNo() {
        return FlatNo;
    }

    public void setFlatNo(String flatNo) {
        FlatNo = flatNo;
    }

    public CaptureLocation getCaptureLocation() {
        return CaptureLocation;
    }

    public void setCaptureLocation(CaptureLocation captureLocation) {
        CaptureLocation = captureLocation;
    }

    public boolean isCaptureLocation() {
        return isCaptureLocation;
    }

    public void setCaptureLocation(boolean captureLocation) {
        isCaptureLocation = captureLocation;
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

    public int getTemplateLength() {
        return templateLength;
    }

    public void setTemplateLength(int templateLength) {
        this.templateLength = templateLength;
    }

    @SerializedName("templates")
    @Expose
    private List<TemplatesDynamic> templates;

    public List<TemplatesDynamic> getTemplates() {
        return templates;
    }

    @SerializedName("isRepeat")
    private Boolean isRepeat;

    @SerializedName("isDriverTemplateRepeat")
    private Boolean isDriverTemplateRepeat = true;

    public Boolean getDriverTemplateRepeat() {
        return isDriverTemplateRepeat;
    }

    public void setDriverTemplateRepeat(Boolean driverTemplateRepeat) {
        isDriverTemplateRepeat = driverTemplateRepeat;
    }

    public Boolean getRepeat() {
        return isRepeat;
    }

    public void setRepeat(Boolean repeat) {
        isRepeat = repeat;
    }

    public void setTemplates(List<TemplatesDynamic> templates) {
        this.templates = templates;
    }

    public List<Barcode_Data> getBarcodes() {
        return barcodes;
    }

    public void setBarcodes(List<Barcode_Data> barcodes) {
        this.barcodes = barcodes;
    }

    @SerializedName("barcodes")
    @Expose
    private List<Barcode_Data> barcodes = new ArrayList<>();

    @SerializedName("actualcustomers")
    @Expose
    private List<ActualCustomer> actualcustomers = new ArrayList<>();


    public String getId() {
        return id;
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

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public List<String> getDriverImages() {
        return driverImages;
    }

    public void setDriverImages(List<String> driverImages) {
        this.driverImages = driverImages;
    }

    public String getDriverSignature() {
        return driverSignature;
    }

    public void setDriverSignature(String driverSignature) {
        this.driverSignature = driverSignature;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getImages() {
        return images;
    }

    public StartLocation getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(StartLocation startLocation) {
        this.startLocation = startLocation;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public List<ActualCustomer> getActualcustomers() {
        return actualcustomers;
    }

    public void setActualcustomers(List<ActualCustomer> actualcustomers) {
        this.actualcustomers = actualcustomers;
    }
}