package com.app.deliforcemanager.ModelClass.GetProfile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Categories {

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("text")
    @Expose
    private String text;


}
