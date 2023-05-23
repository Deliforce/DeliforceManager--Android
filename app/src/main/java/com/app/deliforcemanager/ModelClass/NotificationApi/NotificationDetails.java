package com.app.deliforcemanager.ModelClass.NotificationApi;

import com.app.deliforcemanager.ModelClass.TaskDetailsApi.TaskList;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationDetails {

    @SerializedName("notifications")
   private List<TaskList> notificationList = null;

    public List<TaskList> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<TaskList> notificationList) {
        this.notificationList = notificationList;
    }
}
