package com.app.deliforcemanager.RawHeaders.TaskInfo;

import com.google.gson.annotations.SerializedName;

public class SuperTask extends FilterMain {

    @SerializedName("_id")
    String superTaskId;

    public String getSuperTaskId() {
        return superTaskId;
    }

    public void setSuperTaskId(String superTaskId) {
        this.superTaskId = superTaskId;
    }
}
