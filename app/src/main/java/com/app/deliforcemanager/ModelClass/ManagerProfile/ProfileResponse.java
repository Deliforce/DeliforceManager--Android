package com.app.deliforcemanager.ModelClass.ManagerProfile;

import com.app.deliforcemanager.ModelClass.GetProfile.Categories;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProfileResponse {

    @SerializedName("_id")
    @Expose
    private String id;


    @SerializedName("role")
    @Expose
    private Integer role;


    @SerializedName("cognitoSub")
    @Expose
    private String cognitoSub;


    public List<Categories> getCategoriesList() {
        return categoriesList;
    }

    public void setCategoriesList(List<Categories> categoriesList) {
        this.categoriesList = categoriesList;
    }

    @SerializedName("category")
    @Expose
    private List<Categories> categoriesList;



    @SerializedName("name")
    @Expose
    private String name;


    @SerializedName("email")
    @Expose
    private String email;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getCognitoSub() {
        return cognitoSub;
    }

    public void setCognitoSub(String cognitoSub) {
        this.cognitoSub = cognitoSub;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @SerializedName("phone")
    @Expose
    private String phone;








}
