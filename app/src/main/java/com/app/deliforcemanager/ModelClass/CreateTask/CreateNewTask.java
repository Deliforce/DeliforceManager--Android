
package com.app.deliforcemanager.ModelClass.CreateTask;

import com.app.deliforcemanager.ModelClass.TaskRoutesApi.Address;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreateNewTask {

    @SerializedName("images")
    private List<String> images = null;

    @SerializedName("driverImageOption")
    private DriverImageOption driverImageOption;


    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    @SerializedName("driver")
    @Expose
    private String driver;


    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    @SerializedName("team")
    @Expose
    private String team;




    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("endDate")
    @Expose
    private String endDate;

    @SerializedName("isPickup")
    @Expose
    private boolean isPickup;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("orderId")
    @Expose
    private String orderId;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("address")
    @Expose
    private Address address;

    @SerializedName("description")
    @Expose
    private String description;

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    @SerializedName("manager")
    @Expose
    private String manager;

    @SerializedName("pinCode")
    @Expose
    private String pinCode;

    @SerializedName("color")
    @Expose
    private String color;

    @SerializedName("timezone")
    @Expose
    private String timezone;

    @SerializedName("businessType")
    @Expose
    private Integer businessType;

    @SerializedName("manual")
    @Expose
    private boolean manual;

    @SerializedName("message")
    private String message;

    @SerializedName("isTaskListing")
    private boolean isTaskListing;

    public boolean isTaskListing() {
        return isTaskListing;
    }

    public void setTaskListing(boolean taskListing) {
        isTaskListing = taskListing;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean isPickup() {
        return isPickup;
    }

    public void setPickup(boolean pickup) {
        isPickup = pickup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public boolean isManual() {
        return manual;
    }

    public void setManual(boolean manual) {
        this.manual = manual;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public DriverImageOption getDriverImageOption() {
        return driverImageOption;
    }

    public void setDriverImageOption(DriverImageOption driverImageOption) {
        this.driverImageOption = driverImageOption;
    }

}
