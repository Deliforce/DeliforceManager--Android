
package com.app.deliforcemanager.ModelClass.AnalyticsApi;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TaskComplete {

    @SerializedName("taskCompleationList")
    @Expose
    private List<TaskCompleationList> taskCompleationList = null;
    @SerializedName("ontimeCount")
    @Expose
    private Integer ontimeCount;

    @SerializedName("totalSuccessCount")
    @Expose
    private Integer totalSuccessCount;

    public Integer getTotalSuccessCount() {
        return totalSuccessCount;
    }

    public void setTotalSuccessCount(Integer totalSuccessCount) {
        this.totalSuccessCount = totalSuccessCount;
    }


    public List<TaskCompleationList> getTaskCompleationList() {
        return taskCompleationList;
    }

    public void setTaskCompleationList(List<TaskCompleationList> taskCompleationList) {
        this.taskCompleationList = taskCompleationList;
    }

    public Integer getOntimeCount() {
        return ontimeCount;
    }

    public void setOntimeCount(Integer ontimeCount) {
        this.ontimeCount = ontimeCount;
    }

}
