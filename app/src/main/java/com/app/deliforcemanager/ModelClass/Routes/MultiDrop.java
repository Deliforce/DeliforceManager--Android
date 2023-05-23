
package com.app.deliforcemanager.ModelClass.Routes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MultiDrop {

    @SerializedName("drop_loc_add")
    @Expose
    private String dropLocAdd;
    @SerializedName("rec_name")
    @Expose
    private String recName;
    @SerializedName("rec_phone")
    @Expose
    private String recPhone;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("drop_no")
    @Expose
    private Integer dropNo;
    @SerializedName("delivery_status")
    @Expose
    private Integer deliveryStatus;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;

    public String getDropLocAdd() {
        return dropLocAdd;
    }

    public void setDropLocAdd(String dropLocAdd) {
        this.dropLocAdd = dropLocAdd;
    }

    public String getRecName() {
        return recName;
    }

    public void setRecName(String recName) {
        this.recName = recName;
    }

    public String getRecPhone() {
        return recPhone;
    }

    public void setRecPhone(String recPhone) {
        this.recPhone = recPhone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getDropNo() {
        return dropNo;
    }

    public void setDropNo(Integer dropNo) {
        this.dropNo = dropNo;
    }

    public Integer getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(Integer deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

}
