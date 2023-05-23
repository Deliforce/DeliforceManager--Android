
package com.app.deliforcemanager.ModelClass.TaskDetailsApi;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TaskDetails {

    @SerializedName("taskList")
    @Expose
    private List<TaskList> taskList = null;

    public List<TaskList> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<TaskList> taskList) {
        this.taskList = taskList;
    }

}
