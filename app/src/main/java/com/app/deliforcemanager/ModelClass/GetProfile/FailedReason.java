package com.app.deliforcemanager.ModelClass.GetProfile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FailedReason {
    @SerializedName("reason_id")
    @Expose
    private
    String reason_id;
    @SerializedName("reason")
    @Expose
    private
    String reason;

    public String getReason_id() {
        return reason_id;
    }

    public void setReason_id(String reason_id) {
        this.reason_id = reason_id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return reason;
    }
}
