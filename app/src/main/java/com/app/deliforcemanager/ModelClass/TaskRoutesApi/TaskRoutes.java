
package com.app.deliforcemanager.ModelClass.TaskRoutesApi;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TaskRoutes {

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
