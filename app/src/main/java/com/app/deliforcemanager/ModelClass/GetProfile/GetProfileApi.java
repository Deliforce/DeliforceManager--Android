package com.app.deliforcemanager.ModelClass.GetProfile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetProfileApi {

    @SerializedName("driverStatus")
    @Expose
    private Integer driverStatus;

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("lastName")
    @Expose
    private String lastname;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("settings")
    @Expose
    private ProfileSettings settings;
    @SerializedName("idleDistanceUpdateFrequency")
    @Expose
    private Integer idleDistanceUpdateFrequency;

    @SerializedName("isRadiusValidation")
    @Expose
    private boolean radiusValidation;

    @SerializedName("isIdleLogEnable")
    @Expose
    private boolean idleLogEnable;


    public boolean isMultipleTaskStartEnabled() {
        return isMultipleTaskStartEnabled;
    }

    public void setMultipleTaskStartEnabled(boolean multipleTaskStartEnabled) {
        isMultipleTaskStartEnabled = multipleTaskStartEnabled;
    }

    @SerializedName("isMultipleTaskStartEnabled")
    @Expose
    private boolean isMultipleTaskStartEnabled;

    @SerializedName("isCrmEnable")
    @Expose
    private boolean isCrmEnable;

    private boolean isAdditionalStatus;

    private boolean isFailedOrCancelled;

    private boolean isActualCustomer;

    @SerializedName("isTransitRoadApi")
    @Expose
    private boolean isTransitRoadApi;


    public String getGoogleMapKey() {
        return googleMapKey;
    }

    public void setGoogleMapKey(String googleMapKey) {
        this.googleMapKey = googleMapKey;
    }

    @SerializedName("googleMapKey")
    @Expose
    private String googleMapKey;



    @SerializedName("isCustomerNotesEnabled")
    @Expose
    private boolean isCustomerNotesEnabled;


    public boolean isCategoryConfigEnable() {
        return isCategoryConfigEnable;
    }

    public void setCategoryConfigEnable(boolean categoryConfigEnable) {
        isCategoryConfigEnable = categoryConfigEnable;
    }

    @SerializedName("isCategoryConfigEnable")
    @Expose
    private boolean isCategoryConfigEnable;


    public String getPickupText() {
        return PickupText;
    }

    public void setPickupText(String pickupText) {
        PickupText = pickupText;
    }

    public String getDeliveryText() {
        return DeliveryText;
    }

    public void setDeliveryText(String deliveryText) {
        DeliveryText = deliveryText;
    }

    public String getAppointmentText() {
        return AppointmentText;
    }

    public void setAppointmentText(String appointmentText) {
        AppointmentText = appointmentText;
    }

    public String getFieldWorkForceText() {
        return FieldWorkForceText;
    }

    public void setFieldWorkForceText(String fieldWorkForceText) {
        FieldWorkForceText = fieldWorkForceText;
    }

    @SerializedName("PickupText")
    @Expose
    private String PickupText;

    @SerializedName("DeliveryText")
    @Expose
    private String DeliveryText;

    @SerializedName("AppointmentText")
    @Expose
    private String AppointmentText;

    @SerializedName("FieldWorkForceText")
    @Expose
    private String FieldWorkForceText;






    @SerializedName("isTaskPinCode")
    @Expose
    private boolean isTaskPinCode;
    @SerializedName("isEarningsModule")
    @Expose
    private Boolean isEarningsModule;
    @SerializedName("isPODImageCompress")
    @Expose
    private boolean isPODImageCompress;


    @SerializedName("taskStatusLists")
    @Expose
    private List<DriverStatusTypes> statusList;


    public List<Categories> getCategoriesList() {
        return categoriesList;
    }

    public void setCategoriesList(List<Categories> categoriesList) {
        this.categoriesList = categoriesList;
    }

    @SerializedName("category")
    @Expose
    private List<Categories> categoriesList;


    @SerializedName("androidVerUpdate")
    @Expose
    private boolean androidVerUpdate;
    @SerializedName("xquaraAndroidVerUpdate")
    @Expose
    private boolean xquaraAndroidVerUpdate;
    @SerializedName("isDriverCreateOwnTask")
    @Expose
    private boolean isDriverCreateOwnTask;
    @SerializedName("isDriverCreateBarcodeTask")
    @Expose
    private boolean isDriverCreateBarcodeTask;
    @SerializedName("isGlympseEnable")
    @Expose
    private boolean isGlympseEnable;
    @SerializedName("isApplozicEnable")
    @Expose
    private boolean isApplozicEnable;

    @SerializedName("appLozicContactList")
    @Expose
    private ArrayList<AppLogicContactList> appLozicContactList = new ArrayList<>();
    @SerializedName("isEnableDriverCallOption")
    @Expose
    private boolean isEnableDriverCallOption;
    @SerializedName("androidVer")
    @Expose
    private double androidVer;
    @SerializedName("xquaraAndroidVer")
    @Expose
    private double xquaraAndroidVer;
    @SerializedName("message")
    private String message;
    @SerializedName("mqttCount")
    @Expose
    private Integer mqttCount;

    @SerializedName("taskImagesCount")
    @Expose
    private Integer taskImagesCount;


    public Integer getBussinessType() {
        return BussinessType;
    }

    public void setBussinessType(Integer bussinessType) {
        BussinessType = bussinessType;
    }

    @SerializedName("BussinessType")
    @Expose
    private Integer BussinessType;


    @SerializedName("radiusDistance")
    @Expose
    private int radiusDistance;

    @SerializedName("idleUpdateCount")
    @Expose
    private Integer idleUpdateCount;

    @SerializedName("idleDistanceUpdate")
    @Expose
    private int idleDistanceUpdate;
    @SerializedName("liveTrackingDistanceUpdate")
    @Expose
    private int liveTrackingDistanceUpdate;
    @SerializedName("isOTPVerificationEnable")
    @Expose
    private boolean isOTPVerificationEnable;
    @SerializedName("isAgentChangePassword")
    @Expose
    private boolean isAgentChangePassword;
    @SerializedName("isCaptureLocationEnable")
    @Expose
    private boolean isCaptureLocationEnable;
    @SerializedName("isNavigationTypeEnabled")
    @Expose
    private boolean isNavigationTypeEnabled;
    @SerializedName("isIdleTimerEnabled")
    @Expose
    private boolean isIdleTimerEnabled;
    @SerializedName("isAdminStatusEnabled")
    @Expose
    private boolean isAdminStatusEnabled;

    @SerializedName("isCustomFailedReason")
    @Expose
    private boolean isCustomFailedReason;

    @SerializedName("isWayBillEnabled")
    @Expose
    private boolean isWayBillEnabled;

    @SerializedName("failedReason")
    @Expose
    private ArrayList<FailedReason> failedReason;

    @SerializedName("Currency")
    @Expose
    private String Currency;
    @SerializedName("supportEmail")
    @Expose
    private String supportEmail;

    public boolean isDriverCreateBarcodeTask() {
        return isDriverCreateBarcodeTask;
    }

    public void setDriverCreateBarcodeTask(boolean driverCreateBarcodeTask) {
        isDriverCreateBarcodeTask = driverCreateBarcodeTask;
    }

    public boolean isCustomerNotesEnabled() {
        return isCustomerNotesEnabled;
    }

    public void setCustomerNotesEnabled(boolean customerNotesEnabled) {
        isCustomerNotesEnabled = customerNotesEnabled;
    }

    public boolean isWayBillEnabled() {
        return isWayBillEnabled;
    }

    public void setWayBillEnabled(boolean wayBillEnabled) {
        isWayBillEnabled = wayBillEnabled;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public ArrayList<FailedReason> getFailedReason() {
        return failedReason;
    }

    public void setFailedReason(ArrayList<FailedReason> failedReason) {
        this.failedReason = failedReason;
    }

    public boolean isCustomFailedReason() {
        return isCustomFailedReason;
    }

    public void setCustomFailedReason(boolean customFailedReason) {
        isCustomFailedReason = customFailedReason;
    }

    public boolean isAdminStatusEnabled() {
        return isAdminStatusEnabled;
    }

    public void setAdminStatusEnabled(boolean adminStatusEnabled) {
        isAdminStatusEnabled = adminStatusEnabled;
    }

    public boolean isIdleTimerEnabled() {
        return isIdleTimerEnabled;
    }

    public void setIdleTimerEnabled(boolean idleTimerEnabled) {
        isIdleTimerEnabled = idleTimerEnabled;
    }

    public boolean isNavigationTypeEnabled() {
        return isNavigationTypeEnabled;
    }

    public void setNavigationTypeEnabled(boolean navigationTypeEnabled) {
        isNavigationTypeEnabled = navigationTypeEnabled;
    }

    public boolean isCaptureLocationEnable() {
        return isCaptureLocationEnable;
    }

    public void setCaptureLocationEnable(boolean captureLocationEnable) {
        isCaptureLocationEnable = captureLocationEnable;
    }

    public boolean isPODImageCompress() {
        return isPODImageCompress;
    }

    public void setPODImageCompress(boolean PODImageCompress) {
        isPODImageCompress = PODImageCompress;
    }

    public boolean isAndroidVerUpdate() {
        return androidVerUpdate;
    }

    public void setAndroidVerUpdate(boolean androidVerUpdate) {
        this.androidVerUpdate = androidVerUpdate;
    }

    public boolean isDriverCallOption() {
        return isEnableDriverCallOption;
    }

    public void setDriverCallOption(boolean driverCallOption) {
        this.isEnableDriverCallOption = driverCallOption;
    }

    public double getAndroidVer() {
        return androidVer;
    }

    public void setAndroidVer(double androidVer) {
        this.androidVer = androidVer;
    }

    public Integer getTaskImageCount() {
        return taskImagesCount;
    }




    public void setTaskImageCount(Integer taskImagesCount) {
        this.taskImagesCount = taskImagesCount;
    }

    public int getRadiusDistance() {
        return radiusDistance;
    }

    public void setRadiusDistance(int radiusDistance) {
        this.radiusDistance = radiusDistance;
    }

    public Integer getIdleUpdateCount() {
        return idleUpdateCount;
    }

    public void setIdleUpdateCount(Integer idleUpdateCount) {
        this.idleUpdateCount = idleUpdateCount;
    }

    public int getLiveTrackingDistanceUpdate() {
        return liveTrackingDistanceUpdate;
    }

    public void setLiveTrackingDistanceUpdate(int liveTrackingDistanceUpdate) {
        this.liveTrackingDistanceUpdate = liveTrackingDistanceUpdate;
    }

    public int getIdleDistanceUpdate() {
        return idleDistanceUpdate;
    }

    public void setIdleDistanceUpdate(int idleDistanceUpdate) {
        this.idleDistanceUpdate = idleDistanceUpdate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ProfileSettings getSettings() {
        return settings;
    }

    public void setSettings(ProfileSettings settings) {
        this.settings = settings;
    }

    public Integer getMqttCount() {
        return mqttCount;
    }

    public void setMqttCount(Integer mqttCount) {
        this.mqttCount = mqttCount;
    }

    public Integer getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(Integer driverStatus) {
        this.driverStatus = driverStatus;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Integer getIdleDistanceUpdateFrequency() {
        return idleDistanceUpdateFrequency;
    }

    public void setIdleDistanceUpdateFrequency(Integer idleDistanceUpdateFrequency) {
        this.idleDistanceUpdateFrequency = idleDistanceUpdateFrequency;
    }

    public boolean getRadiusValidation() {
        return radiusValidation;
    }

    public boolean getIdleLogEnable() {
        return idleLogEnable;
    }

    public boolean getTransitRoadApi() {
        return isTransitRoadApi;
    }

    public List<DriverStatusTypes> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<DriverStatusTypes> statusList) {
        this.statusList = statusList;
    }

    public boolean isDriverCreateOwnTask() {
        return isDriverCreateOwnTask;
    }

    public void setDriverCreateOwnTask(boolean driverCreateOwnTask) {
        isDriverCreateOwnTask = driverCreateOwnTask;
    }

    public boolean getTaskPinCode() {
        return isTaskPinCode;
    }

    public Boolean getIsEarningsModule() {
        return isEarningsModule;
    }

    public void setIsEarningsModule(Boolean isEarningsModule) {
        this.isEarningsModule = isEarningsModule;
    }

    public boolean isGlympseEnable() {
        return isGlympseEnable;
    }

    public void setGlympseEnable(boolean glympseEnable) {
        isGlympseEnable = glympseEnable;
    }

    public boolean isRadiusValidation() {
        return radiusValidation;
    }

    public void setRadiusValidation(boolean radiusValidation) {
        this.radiusValidation = radiusValidation;
    }

    public boolean isIdleLogEnable() {
        return idleLogEnable;
    }

    public void setIdleLogEnable(boolean idleLogEnable) {
        this.idleLogEnable = idleLogEnable;
    }

    public boolean isCrmEnable() {
        return isCrmEnable;
    }

    public void setCrmEnable(boolean crmEnable) {
        isCrmEnable = crmEnable;
    }

    public boolean isTransitRoadApi() {
        return isTransitRoadApi;
    }

    public void setTransitRoadApi(boolean transitRoadApi) {
        isTransitRoadApi = transitRoadApi;
    }

    public boolean isTaskPinCode() {
        return isTaskPinCode;
    }

    public void setTaskPinCode(boolean taskPinCode) {
        isTaskPinCode = taskPinCode;
    }

    public Boolean getEarningsModule() {
        return isEarningsModule;
    }

    public void setEarningsModule(Boolean earningsModule) {
        isEarningsModule = earningsModule;
    }

    public boolean isEnableDriverCallOption() {
        return isEnableDriverCallOption;
    }

    public void setEnableDriverCallOption(boolean enableDriverCallOption) {
        isEnableDriverCallOption = enableDriverCallOption;
    }

    public Integer getTaskImagesCount() {
        return taskImagesCount;
    }

    public void setTaskImagesCount(Integer taskImagesCount) {
        this.taskImagesCount = taskImagesCount;
    }

    public boolean isApplozicEnable() {
        return isApplozicEnable;
    }

    public void setApplozicEnable(boolean applozicEnable) {
        isApplozicEnable = applozicEnable;
    }

    public ArrayList<AppLogicContactList> getAppLozicContactList() {
        return appLozicContactList;
    }

    public void setAppLozicContactList(ArrayList<AppLogicContactList> appLozicContactList) {
        this.appLozicContactList = appLozicContactList;
    }

    public class AppLogicContactList {
        String name;
        String appLozicId;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAppLozicId() {
            return appLozicId;
        }

        public void setAppLozicId(String appLozicId) {
            this.appLozicId = appLozicId;
        }
    }

    public boolean isAdditionalStatus() {
        return isAdditionalStatus;
    }

    public void setAdditionalStatus(boolean additionalStatus) {
        isAdditionalStatus = additionalStatus;
    }

    public boolean isFailedOrCancelled() {
        return isFailedOrCancelled;
    }

    public void setFailedOrCancelled(boolean failedOrCancelled) {
        isFailedOrCancelled = failedOrCancelled;
    }

    public boolean isActualCustomer() {
        return isActualCustomer;
    }

    public void setActualCustomer(boolean actualCustomer) {
        isActualCustomer = actualCustomer;
    }

    public boolean isOTPVerificationEnable() {
        return isOTPVerificationEnable;
    }

    public void setOTPVerificationEnable(boolean OTPVerificationEnable) {
        isOTPVerificationEnable = OTPVerificationEnable;
    }

    public boolean isAgentChangePassword() {
        return isAgentChangePassword;
    }

    public void setAgentChangePassword(boolean agentChangePassword) {
        isAgentChangePassword = agentChangePassword;
    }

    public boolean isXquaraAndroidVerUpdate() {
        return xquaraAndroidVerUpdate;
    }

    public void setXquaraAndroidVerUpdate(boolean xquaraAndroidVerUpdate) {
        this.xquaraAndroidVerUpdate = xquaraAndroidVerUpdate;
    }

    public double getXquaraAndroidVer() {
        return xquaraAndroidVer;
    }

    public void setXquaraAndroidVer(double xquaraAndroidVer) {
        this.xquaraAndroidVer = xquaraAndroidVer;
    }

    public String getSupportEmail() {
        return supportEmail;
    }

    public void setSupportEmail(String supportEmail) {
        this.supportEmail = supportEmail;
    }
}
