
package com.app.deliforcemanager.ModelClass.Routes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RoutesApi {

    @SerializedName("httpCode")
    @Expose
    private Integer httpCode;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("details")
    @Expose
    private Details details;

    public Integer getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(Integer httpCode) {
        this.httpCode = httpCode;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
    }

}
