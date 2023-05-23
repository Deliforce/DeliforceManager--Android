package com.app.deliforcemanager.ModelClass.TaskDetailsApi;

import java.util.ArrayList;
import java.util.List;

public class TaskUpdatedList {
    private List<TaskList> taskDetails = new ArrayList<>();

    public List<TaskList> getTaskDetails() {
        return taskDetails;
    }

    public void setTaskDetails(List<TaskList> taskDetails) {
        this.taskDetails = taskDetails;
    }
}
