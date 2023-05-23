package com.app.deliforcemanager.RawHeaders.TaskInfo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Filter implements Serializable {

    @SerializedName("search")
    private String search;
    @SerializedName("dateRange")
    private List<String> dateRange = null;
    @SerializedName("statusFilter")
    private List<String> statusFilter = null;
    @SerializedName("sortByTIme")
    private int sortByTIme;

    public int getSortByDistance() {
        return sortByDistance;
    }

    public void setSortByDistance(int sortByDistance) {
        this.sortByDistance = sortByDistance;
    }

    @SerializedName("sortByDistance")
    private int sortByDistance;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public List<String> getDateRange() {
        return dateRange;
    }

    public void setDateRange(List<String> dateRange) {
        this.dateRange = dateRange;
    }

    public List<String> getStatusFilter() {
        return statusFilter;
    }

    public void setStatusFilter(List<String> statusFilter) {
        this.statusFilter = statusFilter;
    }

    public int getSortByTIme() {
        return sortByTIme;
    }

    public void setSortByTIme(int sortByTIme) {
        this.sortByTIme = sortByTIme;
    }
}
