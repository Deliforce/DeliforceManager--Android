
package com.app.deliforcemanager.ModelClass.AnalyticsApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TaskCompleationList {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("successCount")
    @Expose
    private Integer successCount;

    public Integer getDelayTaskPerDay() {
        return delayTaskPerDay;
    }

    public void setDelayTaskPerDay(Integer delayTaskPerDay) {
        this.delayTaskPerDay = delayTaskPerDay;
    }

    @SerializedName("delayTaskPerDay")
    @Expose
    private Integer delayTaskPerDay;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

}
