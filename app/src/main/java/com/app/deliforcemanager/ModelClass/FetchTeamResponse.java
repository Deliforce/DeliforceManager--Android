package com.app.deliforcemanager.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FetchTeamResponse {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("accuracy")
    @Expose
    private Integer accuracy;
    @SerializedName("isDeleted")
    @Expose
    private Integer isDeleted;
    @SerializedName("teamName")
    @Expose
    private String teamName;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("clientId")
    @Expose
    private String clientId;



    @SerializedName("__v")
    @Expose
    private Integer v;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Integer accuracy) {
        this.accuracy = accuracy;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }





    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }
}
