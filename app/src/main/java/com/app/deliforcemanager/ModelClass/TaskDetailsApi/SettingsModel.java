
package com.app.deliforcemanager.ModelClass.TaskDetailsApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SettingsModel {

    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("businessType")
    @Expose
    private Integer businessType;

    @SerializedName("isCurrent")
    @Expose
    private Boolean isCurrent;

    @SerializedName("actionBlock")
    @Expose
    private ActionBlock actionBlock;

    @SerializedName("acknowledgementType")
    @Expose
    private Integer acknowledgementType;

    @SerializedName("notifications")
    @Expose
    private Notifications notifications;

    @SerializedName("autoAllocation")
    @Expose
    private AutoAllocation autoAllocation;

    @SerializedName("clientId")
    @Expose
    private String clientId;

    @SerializedName("__v")
    @Expose
    private Integer v;

    @SerializedName("enableArrivedStatus")
    @Expose
    private Boolean enableArrivedStatus;

    public Boolean getEnableArrivedStatus() {
        return enableArrivedStatus;
    }

    public void setEnableArrivedStatus(Boolean enableArrivedStatus) {
        this.enableArrivedStatus = enableArrivedStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public Boolean getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(Boolean isCurrent) {
        this.isCurrent = isCurrent;
    }

    public ActionBlock getActionBlock() {
        return actionBlock;
    }

    public void setActionBlock(ActionBlock actionBlock) {
        this.actionBlock = actionBlock;
    }

    public Integer getAcknowledgementType() {
        return acknowledgementType;
    }

    public void setAcknowledgementType(Integer acknowledgementType) {
        this.acknowledgementType = acknowledgementType;
    }

    public Notifications getNotifications() {
        return notifications;
    }

    public void setNotifications(Notifications notifications) {
        this.notifications = notifications;
    }

    public AutoAllocation getAutoAllocation() {
        return autoAllocation;
    }

    public void setAutoAllocation(AutoAllocation autoAllocation) {
        this.autoAllocation = autoAllocation;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

}
