
package com.app.deliforcemanager.ModelClass.TaskDetailsApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AutoAllocation {

    @SerializedName("nearest")
    @Expose
    private Nearest nearest;
    @SerializedName("sendToAll")
    @Expose
    private SendToAll sendToAll;
    @SerializedName("oneByOne")
    @Expose
    private OneByOne oneByOne;

    public Nearest getNearest() {
        return nearest;
    }

    public void setNearest(Nearest nearest) {
        this.nearest = nearest;
    }

    public SendToAll getSendToAll() {
        return sendToAll;
    }

    public void setSendToAll(SendToAll sendToAll) {
        this.sendToAll = sendToAll;
    }

    public OneByOne getOneByOne() {
        return oneByOne;
    }

    public void setOneByOne(OneByOne oneByOne) {
        this.oneByOne = oneByOne;
    }

}
