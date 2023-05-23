package com.app.deliforcemanager.RawHeaders.TaskInfo;

import com.google.gson.JsonArray;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UpdateTaskStatus {

    @SerializedName("_id")
    private String taskId;


    @SerializedName("taskStatus")
    private Integer status;


    @SerializedName("reason")
    private String cancelTaskReason;


    public String getTotalTravelledDistance() {
        return totalTravelledDistance;
    }

    public void setTotalTravelledDistance(String totalTravelledDistance) {
        this.totalTravelledDistance = totalTravelledDistance;
    }

    @SerializedName("totalTravelledDistance")
    private String totalTravelledDistance;



    @SerializedName("completedTime")
    private String taskSuccessCompletionTime;
    @SerializedName("startedTime")
    private String startedTime;
    @SerializedName("arrivedTime")
    private String arrivedTime;
    @SerializedName("activeDist")
    private double travelledDistance;
    @SerializedName("topic")
    private String topic;
    @SerializedName("status")
    private int driverStatus;

    public String getManager_id() {
        return manager_id;
    }

    public void setManager_id(String manager_id) {
        this.manager_id = manager_id;
    }

    @SerializedName("managerId")
    private String manager_id;

    @SerializedName("adminArray")
    private List<String> adminArray = null;
    @SerializedName("batteryState")
    private int batteryStatus;
    @SerializedName("formattedAddress")
    private String address;
    @SerializedName("driverName")
    private String driver_name;

    @SerializedName("startLat")
    private double startLat;

    @SerializedName("startLng")
    private double startLng;

    @SerializedName("imgUrl")
    private String imgUrl;

    @SerializedName("id")
    private String id;

    @SerializedName("fieldName")
    private String fieldName;

    @SerializedName("fieldValue")
    private String fieldValue;

    @SerializedName("dataType")
    private String dataType;

    @SerializedName("reSendOtp")
    private boolean reSendOtp = false;

    @SerializedName("selectedValues")
    private JsonArray selectedValues;

    @SerializedName("deviceType")
    private int deviceType;

    @SerializedName("otp")
    private int otp;

    @SerializedName("order")
    private int order;

    @SerializedName("templateName")
    private String templateName;

    @SerializedName("glympseTrackingURL")
    private String glympseTrackingURL;

    @SerializedName("referenceId")
    private String referenceId;

    @SerializedName("isPickup")
    private boolean isPickup;

    @SerializedName("captureLocation")
    private com.app.deliforcemanager.ModelClass.TaskDetailsApi.CaptureLocation CaptureLocation;

    @SerializedName("reason_id")
    @Expose
    private String reason_id;

    public String getReason_id() {
        return reason_id;
    }

    public void setReason_id(String reason_id) {
        this.reason_id = reason_id;
    }

    public com.app.deliforcemanager.ModelClass.TaskDetailsApi.CaptureLocation getCaptureLocation() {
        return CaptureLocation;
    }

    public void setCaptureLocation(com.app.deliforcemanager.ModelClass.TaskDetailsApi.CaptureLocation captureLocation) {
        CaptureLocation = captureLocation;
    }

    public String getGlympseTrackingURL() {
        return glympseTrackingURL;
    }

    public void setGlympseTrackingURL(String glympseTrackingURL) {
        this.glympseTrackingURL = glympseTrackingURL;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public int getOrder() {
        return order;
    }


    public void setOrder(int order) {
        this.order = order;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }


    public double getStartLng() {
        return startLng;
    }

    public void setStartLng(double startLng) {
        this.startLng = startLng;
    }


    public double getStartLat() {
        return startLat;
    }

    public void setStartLat(double startLat) {
        this.startLat = startLat;
    }


    public int getBatteryStatus() {
        return batteryStatus;
    }

    public void setBatteryStatus(int batteryStatus) {
        this.batteryStatus = batteryStatus;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }





    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public String getCancelTaskReason() {
        return cancelTaskReason;
    }

    public void setCancelTaskReason(String cancelTaskReason) {
        this.cancelTaskReason = cancelTaskReason;
    }

    public String getTaskSuccessCompletionTime() {
        return taskSuccessCompletionTime;
    }

    public void setTaskSuccessCompletionTime(String taskSuccessCompletionTime) {
        this.taskSuccessCompletionTime = taskSuccessCompletionTime;
    }

    public String getStartedTime() {
        return startedTime;
    }

    public void setStartedTime(String startedTime) {
        this.startedTime = startedTime;
    }


    public String getArrivedTime() {
        return arrivedTime;
    }

    public void setArrivedTime(String arrivedTime) {
        this.arrivedTime = arrivedTime;
    }

    public double getTravelledDistance() {
        return travelledDistance;
    }

    public void setTravelledDistance(double travelledDistance) {
        this.travelledDistance = travelledDistance;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(int driverStatus) {
        this.driverStatus = driverStatus;
    }

    public List<String> getAdminArray() {
        return adminArray;
    }

    public void setAdminArray(List<String> adminArray) {
        this.adminArray = adminArray;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldValues(String fieldValues) {
        this.fieldValue = fieldValues;
    }

    public String getFieldValues() {
        return fieldValue;
    }

    public void setFieldSelectedValues(JsonArray fieldSelectedValues) {
        this.selectedValues = fieldSelectedValues;
    }

    public JsonArray getFieldSelectedValues() {
        return selectedValues;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public int getOTP() {
        return otp;
    }

    public void setOTP(int otps) {
        this.otp = otps;
    }

    public boolean getRetryOTPCode() {
        return reSendOtp;
    }

    public void setRetryOTPCode(boolean otpCode) {
        this.reSendOtp = otpCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public boolean isReSendOtp() {
        return reSendOtp;
    }

    public void setReSendOtp(boolean reSendOtp) {
        this.reSendOtp = reSendOtp;
    }

    public JsonArray getSelectedValues() {
        return selectedValues;
    }

    public void setSelectedValues(JsonArray selectedValues) {
        this.selectedValues = selectedValues;
    }

    public int getOtp() {
        return otp;
    }

    public void setOtp(int otp) {
        this.otp = otp;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public boolean isPickup() {
        return isPickup;
    }

    public void setPickup(boolean pickup) {
        isPickup = pickup;
    }
}
