package com.app.deliforcemanager.ModelClass.HoursApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Hours {

    @SerializedName("totalMinutes")
    @Expose
    private Double totalMinutes;
    @SerializedName("totalDistance")
    @Expose
    private Double totalDistance;
    @SerializedName("totalTasks")
    @Expose
    private Double totalTasks;
    @SerializedName("avgTimeForTask")
    @Expose
    private String avgTimeForTask;
    @SerializedName("avgDistanceForTask")
    @Expose
    private String avgDistanceForTask;

    public Double getTotalMinutes() {
        return totalMinutes;
    }

    public void setTotalMinutes(Double totalMinutes) {
        this.totalMinutes = totalMinutes;
    }

    public Double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(Double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public Double getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(Double totalTasks) {
        this.totalTasks = totalTasks;
    }

    public String getAvgTimeForTask() {
        return avgTimeForTask;
    }

    public void setAvgTimeForTask(String avgTimeForTask) {
        this.avgTimeForTask = avgTimeForTask;
    }

    public String getAvgDistanceForTask() {
        return avgDistanceForTask;
    }

    public void setAvgDistanceForTask(String avgDistanceForTask) {
        this.avgDistanceForTask = avgDistanceForTask;
    }

}