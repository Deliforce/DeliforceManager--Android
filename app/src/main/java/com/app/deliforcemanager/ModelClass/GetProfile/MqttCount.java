package com.app.deliforcemanager.ModelClass.GetProfile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MqttCount {

    @SerializedName("mqttCount")
    @Expose
    private Integer mqttCount;

    public Integer getMqttCount() {
        return mqttCount;
    }

    public void setMqttCount(Integer mqttCount) {
        this.mqttCount = mqttCount;
    }

}