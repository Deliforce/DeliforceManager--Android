package com.app.deliforcemanager.ModelClass.EarningsChart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EarningsChartData {


    @SerializedName("dateArr")
    @Expose
    private List<EarningDateArray> dateArr = null;
    @SerializedName("taskDetails")
    @Expose
    private List<EarningsList> taskDetails = null;
    @SerializedName("totalCount")
    @Expose
    private String totalCount;

    public List<EarningDateArray> getDateArr() {
        return dateArr;
    }

    public void setDateArr(List<EarningDateArray> dateArr) {
        this.dateArr = dateArr;
    }

    public List<EarningsList> getTaskDetails() {
        return taskDetails;
    }

    public void setTaskDetails(List<EarningsList> taskDetails) {
        this.taskDetails = taskDetails;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

}
