package com.app.deliforcemanager.RawHeaders.TaskInfo;

import com.google.gson.annotations.SerializedName;

public class DeleteNote {

    @SerializedName("id")
    private String id;
    @SerializedName("taskId")
    private String taskId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }


}
