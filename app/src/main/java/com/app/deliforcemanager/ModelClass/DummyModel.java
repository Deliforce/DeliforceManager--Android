package com.app.deliforcemanager.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DummyModel {

    @SerializedName("auth")
    @Expose
    private Boolean auth;
    @SerializedName("accessToken")
    @Expose
    private Object accessToken;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getAuth() {
        return auth;
    }

    public void setAuth(Boolean auth) {
        this.auth = auth;
    }

    public Object getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(Object accessToken) {
        this.accessToken = accessToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}