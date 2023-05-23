package com.app.deliforcemanager.ModelClass.EarningsChart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EarningsData {
    @SerializedName("baseFare")
    @Expose
    private String baseFare;
    @SerializedName("baseDuration")
    @Expose
    private String baseDuration;
    @SerializedName("baseDistance")
    @Expose
    private String baseDistance;
    @SerializedName("deduction")
    @Expose
    private String deduction;
    @SerializedName("tips")
    @Expose
    private String tips;
    @SerializedName("totalSum")
    @Expose
    private String totalSum;

    public String getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(String baseFare) {
        this.baseFare = baseFare;
    }

    public String getBaseDuration() {
        return baseDuration;
    }

    public void setBaseDuration(String baseDuration) {
        this.baseDuration = baseDuration;
    }

    public String getBaseDistance() {
        return baseDistance;
    }

    public void setBaseDistance(String baseDistance) {
        this.baseDistance = baseDistance;
    }

    public String getDeduction() {
        return deduction;
    }

    public void setDeduction(String deduction) {
        this.deduction = deduction;
    }

    public String getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(String totalSum) {
        this.totalSum = totalSum;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }
}
