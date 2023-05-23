
package com.app.deliforcemanager.ModelClass.UpdateImagesApi;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverImage {

    @SerializedName("driverImages")
    @Expose
    private List<String> driverImages = null;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("driverSignature")
    @Expose
    private String driverSignature;
    @SerializedName("templateUrl")
    @Expose
    private String templateUrl;

    public List<String> getDriverImages() {
        return driverImages;
    }

    public void setDriverImages(List<String> driverImages) {
        this.driverImages = driverImages;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDriverSignature() {
        return driverSignature;
    }

    public void setDriverSignature(String driverSignature) {
        this.driverSignature = driverSignature;
    }

    public String getTemplateUrl() {
        return templateUrl;
    }

    public void setTemplateUrl(String templateUrl) {
        this.templateUrl = templateUrl;
    }
}
