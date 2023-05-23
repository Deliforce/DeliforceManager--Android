package com.app.deliforcemanager.RawHeaders.TaskInfo;

import com.google.gson.annotations.SerializedName;

public class AddActualCustomer {

    @SerializedName("actualCustomerId")
    private String actualCustomerId;

    @SerializedName("actualCustomerName")
    private String actualCustomerName;

    @SerializedName("taskId")
    private String taskId;

    @SerializedName("_id")
    private String _id;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getActualCustomerId() {
        return actualCustomerId;
    }

    public void setActualCustomerId(String actualCustomerId) {
        this.actualCustomerId = actualCustomerId;
    }

    public String getActualCustomerName() {
        return actualCustomerName;
    }

    public void setActualCustomerName(String actualCustomerName) {
        this.actualCustomerName = actualCustomerName;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
