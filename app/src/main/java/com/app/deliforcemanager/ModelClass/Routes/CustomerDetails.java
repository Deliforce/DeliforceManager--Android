
package com.app.deliforcemanager.ModelClass.Routes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerDetails {

    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("customer_phone")
    @Expose
    private String customerPhone;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

}
