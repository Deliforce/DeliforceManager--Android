package com.app.deliforcemanager.RawHeaders;

import com.google.gson.annotations.SerializedName;

public class UpdationPassword {

    @SerializedName("phone")
    private String phone;
    @SerializedName("newPassword")
    private String newPassword;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }


}
