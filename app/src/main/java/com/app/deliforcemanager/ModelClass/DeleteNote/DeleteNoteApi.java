package com.app.deliforcemanager.ModelClass.DeleteNote;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteNoteApi {

    @SerializedName("n")
    @Expose
    private Integer n;
    @SerializedName("nModified")
    @Expose
    private Integer nModified;
    @SerializedName("electionId")
    @Expose
    private String electionId;
    @SerializedName("ok")
    @Expose
    private Integer ok;
    @SerializedName("operationTime")
    @Expose
    private String operationTime;

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Integer getNModified() {
        return nModified;
    }

    public void setNModified(Integer nModified) {
        this.nModified = nModified;
    }

    public String getElectionId() {
        return electionId;
    }

    public void setElectionId(String electionId) {
        this.electionId = electionId;
    }

    public Integer getOk() {
        return ok;
    }

    public void setOk(Integer ok) {
        this.ok = ok;
    }

    public String getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(String operationTime) {
        this.operationTime = operationTime;
    }

}