package com.app.deliforcemanager.ModelClass.TaskDetailsApi;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParticluarTaskDetails {


    @SerializedName("taskData")
    @Expose
    private TaskOneDetails taskData;

    public TaskOneDetails getTaskData() {
        return taskData;
    }

    public void setTaskData(TaskOneDetails taskData) {
        this.taskData = taskData;
    }

}
