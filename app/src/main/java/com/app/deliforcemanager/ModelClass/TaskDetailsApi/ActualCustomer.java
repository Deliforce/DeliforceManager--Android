package com.app.deliforcemanager.ModelClass.TaskDetailsApi;

/**
 * Created by nextbrain on 22/1/20.
 */
public class ActualCustomer {
    private String isDeleted,_id,taskId,actualCustomerId,actualCustomerName,driverCognitoSub;

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
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

    public String getDriverCognitoSub() {
        return driverCognitoSub;
    }

    public void setDriverCognitoSub(String driverCognitoSub) {
        this.driverCognitoSub = driverCognitoSub;
    }
}
