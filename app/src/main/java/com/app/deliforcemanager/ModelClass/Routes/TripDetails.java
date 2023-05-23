
package com.app.deliforcemanager.ModelClass.Routes;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TripDetails {

    @SerializedName("driver_name")
    @Expose
    private String driverName;
    @SerializedName("trip_id")
    @Expose
    private String tripId;
    @SerializedName("driver_id")
    @Expose
    private String driverId;
    @SerializedName("customer_id")
    @Expose
    private String customerId;
    @SerializedName("booking_key")
    @Expose
    private String bookingKey;
    @SerializedName("multi_drop")
    @Expose
    private List<MultiDrop> multiDrop = null;
    @SerializedName("total_trip_duration")
    @Expose
    private Double totalTripDuration;
    @SerializedName("total_trip_distance")
    @Expose
    private Double totalTripDistance;
    @SerializedName("pickup_latitude")
    @Expose
    private Double pickupLatitude;
    @SerializedName("pickup_longitude")
    @Expose
    private Double pickupLongitude;
    @SerializedName("payment_status")
    @Expose
    private Integer paymentStatus;
    @SerializedName("payment_type")
    @Expose
    private Integer paymentType;
    @SerializedName("booking_type")
    @Expose
    private Integer bookingType;
    @SerializedName("rating_points_customer")
    @Expose
    private Double ratingPointsCustomer;
    @SerializedName("sender_name")
    @Expose
    private String senderName;
    @SerializedName("sender_phone")
    @Expose
    private String senderPhone;
    @SerializedName("customer_rating_status")
    @Expose
    private Integer customerRatingStatus;
    @SerializedName("rating_points_driver")
    @Expose
    private Double ratingPointsDriver;
    @SerializedName("driver_rating_status")
    @Expose
    private Integer driverRatingStatus;
    @SerializedName("trip_status")
    @Expose
    private Integer tripStatus;
    @SerializedName("trip_notification_status")
    @Expose
    private Integer tripNotificationStatus;
    @SerializedName("driver_reply_status")
    @Expose
    private Integer driverReplyStatus;
    @SerializedName("billed_wallet")
    @Expose
    private Integer billedWallet;
    @SerializedName("discount_pooling_fare")
    @Expose
    private Integer discountPoolingFare;
    @SerializedName("discount_promo_fare")
    @Expose
    private Integer discountPromoFare;
    @SerializedName("discount_offer_fare")
    @Expose
    private Integer discountOfferFare;
    @SerializedName("distance_fare")
    @Expose
    private Double distanceFare;
    @SerializedName("base_fare")
    @Expose
    private Integer baseFare;
    @SerializedName("distance_unit")
    @Expose
    private String distanceUnit;
    @SerializedName("total_fare")
    @Expose
    private Double totalFare;
    @SerializedName("actual_time")
    @Expose
    private Double actualTime;
    @SerializedName("actual_distance")
    @Expose
    private Integer actualDistance;
    @SerializedName("actual_drop_time")
    @Expose
    private String actualDropTime;
    @SerializedName("actual_pickup_time")
    @Expose
    private String actualPickupTime;
    @SerializedName("pickup_time")
    @Expose
    private String pickupTime;
    @SerializedName("payment_id")
    @Expose
    private String paymentId;
    @SerializedName("customer_rating_date")
    @Expose
    private String customerRatingDate;
    @SerializedName("pickup_location")
    @Expose
    private String pickupLocation;
    @SerializedName("shipment_information")
    @Expose
    private String shipmentInformation;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("customer_avg_ratings")
    @Expose
    private Double customerAvgRatings;

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getBookingKey() {
        return bookingKey;
    }

    public void setBookingKey(String bookingKey) {
        this.bookingKey = bookingKey;
    }

    public List<MultiDrop> getMultiDrop() {
        return multiDrop;
    }

    public void setMultiDrop(List<MultiDrop> multiDrop) {
        this.multiDrop = multiDrop;
    }

    public Double getTotalTripDuration() {
        return totalTripDuration;
    }

    public void setTotalTripDuration(Double totalTripDuration) {
        this.totalTripDuration = totalTripDuration;
    }

    public Double getTotalTripDistance() {
        return totalTripDistance;
    }

    public void setTotalTripDistance(Double totalTripDistance) {
        this.totalTripDistance = totalTripDistance;
    }

    public Double getPickupLatitude() {
        return pickupLatitude;
    }

    public void setPickupLatitude(Double pickupLatitude) {
        this.pickupLatitude = pickupLatitude;
    }

    public Double getPickupLongitude() {
        return pickupLongitude;
    }

    public void setPickupLongitude(Double pickupLongitude) {
        this.pickupLongitude = pickupLongitude;
    }

    public Integer getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Integer paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getBookingType() {
        return bookingType;
    }

    public void setBookingType(Integer bookingType) {
        this.bookingType = bookingType;
    }

    public Double getRatingPointsCustomer() {
        return ratingPointsCustomer;
    }

    public void setRatingPointsCustomer(Double ratingPointsCustomer) {
        this.ratingPointsCustomer = ratingPointsCustomer;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    public Integer getCustomerRatingStatus() {
        return customerRatingStatus;
    }

    public void setCustomerRatingStatus(Integer customerRatingStatus) {
        this.customerRatingStatus = customerRatingStatus;
    }

    public Double getRatingPointsDriver() {
        return ratingPointsDriver;
    }

    public void setRatingPointsDriver(Double ratingPointsDriver) {
        this.ratingPointsDriver = ratingPointsDriver;
    }

    public Integer getDriverRatingStatus() {
        return driverRatingStatus;
    }

    public void setDriverRatingStatus(Integer driverRatingStatus) {
        this.driverRatingStatus = driverRatingStatus;
    }

    public Integer getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(Integer tripStatus) {
        this.tripStatus = tripStatus;
    }

    public Integer getTripNotificationStatus() {
        return tripNotificationStatus;
    }

    public void setTripNotificationStatus(Integer tripNotificationStatus) {
        this.tripNotificationStatus = tripNotificationStatus;
    }

    public Integer getDriverReplyStatus() {
        return driverReplyStatus;
    }

    public void setDriverReplyStatus(Integer driverReplyStatus) {
        this.driverReplyStatus = driverReplyStatus;
    }

    public Integer getBilledWallet() {
        return billedWallet;
    }

    public void setBilledWallet(Integer billedWallet) {
        this.billedWallet = billedWallet;
    }

    public Integer getDiscountPoolingFare() {
        return discountPoolingFare;
    }

    public void setDiscountPoolingFare(Integer discountPoolingFare) {
        this.discountPoolingFare = discountPoolingFare;
    }

    public Integer getDiscountPromoFare() {
        return discountPromoFare;
    }

    public void setDiscountPromoFare(Integer discountPromoFare) {
        this.discountPromoFare = discountPromoFare;
    }

    public Integer getDiscountOfferFare() {
        return discountOfferFare;
    }

    public void setDiscountOfferFare(Integer discountOfferFare) {
        this.discountOfferFare = discountOfferFare;
    }

    public Double getDistanceFare() {
        return distanceFare;
    }

    public void setDistanceFare(Double distanceFare) {
        this.distanceFare = distanceFare;
    }

    public Integer getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(Integer baseFare) {
        this.baseFare = baseFare;
    }

    public String getDistanceUnit() {
        return distanceUnit;
    }

    public void setDistanceUnit(String distanceUnit) {
        this.distanceUnit = distanceUnit;
    }

    public Double getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(Double totalFare) {
        this.totalFare = totalFare;
    }

    public Double getActualTime() {
        return actualTime;
    }

    public void setActualTime(Double actualTime) {
        this.actualTime = actualTime;
    }

    public Integer getActualDistance() {
        return actualDistance;
    }

    public void setActualDistance(Integer actualDistance) {
        this.actualDistance = actualDistance;
    }

    public String getActualDropTime() {
        return actualDropTime;
    }

    public void setActualDropTime(String actualDropTime) {
        this.actualDropTime = actualDropTime;
    }

    public String getActualPickupTime() {
        return actualPickupTime;
    }

    public void setActualPickupTime(String actualPickupTime) {
        this.actualPickupTime = actualPickupTime;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getCustomerRatingDate() {
        return customerRatingDate;
    }

    public void setCustomerRatingDate(String customerRatingDate) {
        this.customerRatingDate = customerRatingDate;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getShipmentInformation() {
        return shipmentInformation;
    }

    public void setShipmentInformation(String shipmentInformation) {
        this.shipmentInformation = shipmentInformation;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public Double getCustomerAvgRatings() {
        return customerAvgRatings;
    }

    public void setCustomerAvgRatings(Double customerAvgRatings) {
        this.customerAvgRatings = customerAvgRatings;
    }

}
