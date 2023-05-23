package com.app.deliforcemanager.ModelClass.EarningsChart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EarningDetailsData {

    @SerializedName("taskDetails")
    @Expose
    private List<EarningTaskdetails> taskDetails = null;

    public List<EarningTaskdetails> getTaskDetails() {
        return taskDetails;
    }

    public void setTaskDetails(List<EarningTaskdetails> taskDetails) {
        this.taskDetails = taskDetails;
    }

    public class EarningTaskdetails{
        @SerializedName("_id")
        @Expose
        private String id;

        @SerializedName("earningsDetails")
        @Expose
        private EarningsData earningsDetails;

        public EarningsData getManualEarningsDetails() {
            return manualEarningsDetails;
        }

        public void setManualEarningsDetails(EarningsData manualEarningsDetails) {
            this.manualEarningsDetails = manualEarningsDetails;
        }

        @SerializedName("manualEarningsDetails")
        @Expose
        private EarningsData manualEarningsDetails;

        @SerializedName("completedTimeString")
        @Expose
        private String completedTimeString;
        @SerializedName("taskId")
        @Expose
        private String taskId;
        @SerializedName("taskStatus")
        @Expose
        private Integer taskStatus;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("bussinessTypeName")
        @Expose
        private String bussinessTypeName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public EarningsData getEarningsDetails() {
            return earningsDetails;
        }

        public void setEarningsDetails(EarningsData earningsDetails) {
            this.earningsDetails = earningsDetails;
        }

        public String getCompletedTimeString() {
            return completedTimeString;
        }

        public void setCompletedTimeString(String completedTimeString) {
            this.completedTimeString = completedTimeString;
        }

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public Integer getTaskStatus() {
            return taskStatus;
        }

        public void setTaskStatus(Integer taskStatus) {
            this.taskStatus = taskStatus;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getBussinessTypeName() {
            return bussinessTypeName;
        }

        public void setBussinessTypeName(String bussinessTypeName) {
            this.bussinessTypeName = bussinessTypeName;
        }
    }
}
