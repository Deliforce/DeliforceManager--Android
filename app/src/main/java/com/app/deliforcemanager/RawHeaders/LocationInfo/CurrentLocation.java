package com.app.deliforcemanager.RawHeaders.LocationInfo;

import com.google.gson.annotations.SerializedName;

public class CurrentLocation {


    @SerializedName("lat")
    private Double lattitude;
    @SerializedName("lng")
    private Double longitude;
    @SerializedName("formattedAddress")
    private String address;


    public Double getLattitude() {
        return lattitude;
    }

    public void setLattitude(Double lattitude) {
        this.lattitude = lattitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
