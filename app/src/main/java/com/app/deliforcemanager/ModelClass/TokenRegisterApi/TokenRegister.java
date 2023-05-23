
package com.app.deliforcemanager.ModelClass.TokenRegisterApi;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenRegister {

    @SerializedName("adminList")
    @Expose
    private List<String> adminList = null;

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    @SerializedName("managerId")
    @Expose
    private String managerId;

    @SerializedName("isGlympseEnable")
    @Expose
    private Boolean isGlympseEnable;

    @SerializedName("user")
    @Expose
    private GlympseUser user;

    public Boolean getGlympseEnable() {
        return isGlympseEnable;
    }

    public void setGlympseEnable(Boolean glympseEnable) {
        isGlympseEnable = glympseEnable;
    }

    public List<String> getAdminList() {
        return adminList;
    }

    public void setAdminList(List<String> adminList) {
        this.adminList = adminList;
    }


    public GlympseUser getUser() {
        return user;
    }

    public void setUser(GlympseUser user) {
        this.user = user;
    }

    public class GlympseUser {

        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("password")
        @Expose
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

    }
}
