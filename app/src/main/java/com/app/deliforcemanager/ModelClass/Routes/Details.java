
package com.app.deliforcemanager.ModelClass.Routes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Details {

    @SerializedName("trip_details")
    @Expose
    private TripDetails tripDetails;
    @SerializedName("customer_details")
    @Expose
    private CustomerDetails customerDetails;

    public TripDetails getTripDetails() {
        return tripDetails;
    }

    public void setTripDetails(TripDetails tripDetails) {
        this.tripDetails = tripDetails;
    }

    public CustomerDetails getCustomerDetails() {
        return customerDetails;
    }

    public void setCustomerDetails(CustomerDetails customerDetails) {
        this.customerDetails = customerDetails;
    }

}
