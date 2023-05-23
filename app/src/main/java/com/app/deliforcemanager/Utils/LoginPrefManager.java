package com.app.deliforcemanager.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.app.deliforcemanager.Activities.SplashActivity;
import com.app.deliforcemanager.ModelClass.GetProfile.Categories;
import com.app.deliforcemanager.ModelClass.GetProfile.FailedReason;
import com.app.deliforcemanager.RawHeaders.TaskInfo.Filter;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


public class LoginPrefManager {

    private static final String PREF_NAME = "AndroidCustomerPracleoPref";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String Check_login = "0";
    private static AlertDialog.Builder alertDialogBuilder = null;
    private static AlertDialog networkAlertDialog = null;
    private static JSONObject jsonObject;
    private final Context _context;
    public SimpleDateFormat SERVER_NOTES_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.ENGLISH);
    public SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy hh:mm:ss a", Locale.ENGLISH);
    public SimpleDateFormat DEFAULT_NOTIFICATION_DATE_FORMAT = new SimpleDateFormat("dd,MMM,yyyy", Locale.ENGLISH);
    public SimpleDateFormat NOTIFICATION_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    public SimpleDateFormat SERVER_NOTIFICATIONS_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
    Set<String> fav_list;
    private SharedPreferences pref;
    private Editor editor;
    private String pickupaddress_latitude;
    private float totalLiveDistance;
    private float savedLatitude;
    private float savedLongitude;
    private float accuracy;
    private String pinCode;

    public LoginPrefManager(Context context) {
        this._context = context;
        int PRIVATE_MODE = 0;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
    }

    public static JSONObject getJsonObject() {
        return jsonObject;
    }

    public static void setJsonObject(JSONObject jsonObject) {
        LoginPrefManager.jsonObject = jsonObject;
    }

    public void setLoginPrefData(String text, String data) {
        editor.putString(text, data).commit();
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(Check_login, "1").commit();
        editor.commit();
    }

    public void setPrefData(String text, String data) {
        editor.putString(text, data).commit();
        editor.commit();
    }

    public String getPrefData(String text) {
        return pref.getString(text, "");
    }

    public Editor getEditor() {
        return editor;
    }

    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    public SharedPreferences getShaPref() {
        return pref;
    }

    public void setMyPref(SharedPreferences pref) {
        this.pref = pref;
    }

    public void setIntValue(String keyName, int value) {
        pref.edit().putInt(keyName, value).apply();
    }


    public void setFloatValue(String keyName, Float value) {
        pref.edit().putFloat(keyName, value).apply();
    }

    public Float getFloatValue(String keyName) {
        return pref.getFloat(keyName, 0);
    }




    public int getIntValue(String keyName) {
        return pref.getInt(keyName, 0);
    }

    public void setTaskImageMaxCount(String keyName, int value) {
        pref.edit().putInt(keyName, value).apply();
    }

    public int getTaskImageMaxCount(String keyName) {
        return pref.getInt(keyName, 5);
    }



    public void setBussinessType(String keyName, int value) {
        pref.edit().putInt(keyName, value).apply();
    }

    public int getBussinessType(String keyName) {
        return pref.getInt(keyName, 1);
    }

    public void setStringValue(String keyName, String value) {
        pref.edit().putString(keyName, value).apply();

    }

    public String getStringValue(String keyName) {

        return pref.getString(keyName, "");

    }

    public void setEmail(String value) {
        pref.edit().putString("driver_email", value).apply();

    }

    public String getEmail() {
        return pref.getString("driver_email", "");

    }

    public void setSupportEmail(String value) {
        pref.edit().putString("supportEmail", value).apply();

    }

    public String getSupportEmail() {
        return pref.getString("supportEmail", "");

    }

    public void setDeviceToken(String value) {
        pref.edit().putString("device_token", value).apply();
    }

    public String getDeviceToken() {
        return pref.getString("device_token", "");
    }

    public String getDropLatitude() {

        return pref.getString("drop_latitude", "");

    }

    public void setDropLatitude(String value) {
        pref.edit().putString("drop_latitude", value).apply();

    }

    public String getDropLongitude() {

        return pref.getString("drop_longitude", "");

    }

    public void setDropLongitude(String value) {
        pref.edit().putString("drop_longitude", value).apply();

    }

    public String getCurrency() {
        return pref.getString("Currency", "");
    }

    public void setCurrency(String value) {
        pref.edit().putString("Currency", value).apply();
    }

    public String getNavigation() {
        return pref.getString("Navigation", DeliforceConstants.NavigationGoogleMap);

    }

    public void setNavigation(String value) {
        pref.edit().putString("Navigation", value).apply();

    }

    public void setBooleanValue(String keyName, boolean value) {
        pref.edit().putBoolean(keyName, value).apply();
    }

    public Boolean getBooleanValue(String keyName) {
        return pref.getBoolean(keyName, false);
    }

    public void setOTPVerificationEnable(boolean value) {
        pref.edit().putBoolean("isOTPVerificationEnable", value).apply();
    }

    public Boolean isOTPVerificationEnabled() {
        return pref.getBoolean("isOTPVerificationEnable", false);
    }

    public void isAdminStatusEnabled(boolean value) {
        pref.edit().putBoolean("isAdminStatusEnabled", value).apply();
    }



    public Boolean isAdminStatusEnabled() {
        return pref.getBoolean("isAdminStatusEnabled", false);
    }

    public void isCustomerNotesEnabled(boolean value) {
        pref.edit().putBoolean("isCustomerNotesEnabled", value).apply();
    }

    public Boolean isCustomerNotesEnabled() {
        return pref.getBoolean("isCustomerNotesEnabled", false);
    }


    public void isCustomerCategoryEnabled(boolean value) {
        pref.edit().putBoolean("isCategoryConfig", value).apply();
    }

    public Boolean isCustomerCategoryEnabled() {
        return pref.getBoolean("isCategoryConfig", false);
    }


    public void remove(String key) {
        pref.edit().remove(key).apply();
    }

    public boolean clear() {
        return pref.edit().clear().commit();
    }

    public void LogOutClearDataMethod() {

        pref.edit().putString("customer_id", "").apply();
        pref.edit().putString("notification_count", "0").apply();


    }

    public void chagepasswordcleraMethod() {

        pref.edit().putString("username", "").apply();
        pref.edit().putString("password1", "").apply();

    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkPrefLogin() {
        if (!this.isLoggedIn()) {
            Intent i = new Intent(_context, SplashActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        editor.clear();
        editor.putBoolean(IS_LOGIN, false);
        editor.commit();
        Intent logi = new Intent(_context, SplashActivity.class);
        logi.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        logi.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(logi);
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    private boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    //Pic a City details
    public void setCityIDandName(String City_Id, String City_Name) {
        pref.edit().putString("Pic_City_Id", City_Id).apply();
        pref.edit().putString("Pic_City_Name", City_Name).apply();
    }

    public String getCityID() {
        return pref.getString("Pic_City_Id", "");
    }

    public String getCityName() {
        return pref.getString("Pic_City_Name", "");
    }

    // Pic a Location Details
    public void setLocIDandName(String Loc_Id, String Loc_Name) {
        pref.edit().putString("Pic_loc_id", Loc_Id).apply();
        pref.edit().putString("Pic_loc_name", Loc_Name).apply();
    }

    public String getLocID() {
        return pref.getString("Pic_loc_id", "");
    }

    public String getLocName() {
        return pref.getString("Pic_loc_name", "");
    }

    public String getCurrencySide() {
        return pref.getString("currency_side", "");
    }

    public String getCurrencySymbole() {
        return pref.getString("currency_symbol", "");
    }

    public String getCurrencyName() {
        return pref.getString("currency_name", "");
    }

    public String getUserToken() {
        return pref.getString("user_token", "");
    }

    public void setUserToken(String userToken) {
        pref.edit().putString("user_token", userToken).apply();
    }

    public String getDefaultLang() {
        return pref.getString("def_lang", "");
    }

    public void setDefaultLang(String keyName) {
        pref.edit().putString("def_lang", keyName).apply();
    }

    public String getManagerID() {
        return pref.getString("manager_id", "");
    }

    public void setMangerID(String driver_id) {
        pref.edit().putString("manager_id", driver_id).apply();
    }

    public Boolean getSkipversion() {
        return pref.getBoolean("skip", false);
    }

    public void setSkipversion(boolean status) {
        pref.edit().putBoolean("skip", status).apply();
    }

    public String getCurrentVersionName() {
        return pref.getString("versionName", "1.55");
    }

    public void setCurrentVersionName(String versionName) {
        pref.edit().putString("versionName", versionName).apply();
    }

    public String getLangauge() {
        return pref.getString("driver_lang", "");
    }

    public void setLangauge(String lang) {
        pref.edit().putString("driver_lang", lang).apply();
    }

    public String getGender() {
        return pref.getString("user_gender", "");
    }

    public void setGender(String gender) {

        pref.edit().putString("user_gender", gender).apply();
    }


    /*public String getFormatCurrencyValue(String Currency) {
        if (getCurrencySide().equals("1")) {
            return String.format("%s %s", "" + getCurrencySymbole(), "" + Currency);
        } else {
            return String.format("%s %s", "" + Currency, "" + getCurrencySymbole());
        }
    }*/

    public String getAccessToken() {
        return pref.getString("access_token", "");
    }

    public void setAccessToken(String accessToken) {
        pref.edit().putString("access_token", accessToken).apply();
    }

    public String getFromDate() {
        return pref.getString("from_date_filter", "");
    }

    public void setFromDate(String fromdate) {
        pref.edit().putString("from_date_filter", fromdate).apply();
    }

    public String getToDate() {
        return pref.getString("to_date_filter", "");
    }

    public void setToDate(String fromdate) {
        pref.edit().putString("to_date_filter", fromdate).apply();
    }

    public String getCogintoToken() {
        return pref.getString("cognito_token", "");
    }

    public void setCogintoToken(String accessToken) {
        pref.edit().putString("cognito_token", accessToken).apply();
    }

    public void setRefreshTokenToken(String accessToken) {
        pref.edit().putString("refresh_token", accessToken).apply();
    }

    public String getRefreshToken() {
        return pref.getString("refresh_token", "");
    }

    public String getDriverBlock() {
        return pref.getString("driver_block", "");
    }

    public void setDriverBlock(String block) {
        pref.edit().putString("driver_block", block).apply();
    }

    public boolean isNavigationTypeEnabled() {
        return pref.getBoolean("isNavigationTypeEnabled", false);
    }

    public void isNavigationTypeEnabled(boolean value) {
        pref.edit().putBoolean("isNavigationTypeEnabled", value).apply();
    }

    public boolean isIdleTimerEnabled() {
        return pref.getBoolean("isIdleTimerEnabled", false);
    }

    public void isIdleTimerEnabled(boolean value) {
        pref.edit().putBoolean("isIdleTimerEnabled", value).apply();
    }

    public boolean isWayBillEnabled() {
        return pref.getBoolean("isWayBillEnabled", false);
    }

    public void isWayBillEnabled(boolean value) {
        pref.edit().putBoolean("isWayBillEnabled", value).apply();
    }

    /*save driver mobile number*/
    public void setDriverMobile(String driverMobile) {
        pref.edit().putString("driver_phone", driverMobile).apply();

    }
    public String getDriverMobileNumber() {
        return pref.getString("driver_phone", "");
    }


    public String getPickupText() {
        return pref.getString("pickup_text", "");
    }


    public void setPickupText(String pickupText) {
        pref.edit().putString("pickup_text", pickupText).apply();

    }

    public String getDeliveryText() {
        return pref.getString("delivery_text", "");
    }


    public void setDeliveryText(String pickupText) {
        pref.edit().putString("delivery_text", pickupText).apply();
    }

    public String getAppointmentText() {
        return pref.getString("AppointmentText", "");
    }


    public void setAppointmentText(String AppointmentText) {
        pref.edit().putString("AppointmentText", AppointmentText).apply();
    }

    public String getFieldWorkForceText() {
        return pref.getString("FieldWorkForceText", "");
    }


    public void setFieldWorkForceText(String FieldWorkForceText) {
        pref.edit().putString("FieldWorkForceText", FieldWorkForceText).apply();
    }


    public void isMultiStartEnable(boolean value) {
        pref.edit().putBoolean("isMultitaskStart", value).apply();

    }

    public boolean getMultiStart() {
        return pref.getBoolean("isMultitaskStart", false);
    }








    // based on this show or hide change password
    public void isAgentChangePassword(boolean value) {
        pref.edit().putBoolean("isAgentChangePassword", value).apply();

    }

    public boolean isAgentChangePassword() {
        return pref.getBoolean("isAgentChangePassword", false);
    }

    // based on this show or hide change password
    public void isCaptureLocationEnabled(boolean value) {
        pref.edit().putBoolean("isCaptureLocationEnabled", value).apply();

    }

    public boolean isCaptureLocationEnabled() {
        return pref.getBoolean("isCaptureLocationEnabled", false);
    }

    public void isCustomFailedReason(boolean value) {
        pref.edit().putBoolean("isCustomFailedReason", value).apply();

    }

    public boolean isCustomFailedReason() {
        return pref.getBoolean("isCustomFailedReason", false);
    }

    public void setFailedReasons(String value) {
        pref.edit().putString("failedReason", value).apply();
    }

    public void setCategoryValue(String value){
        pref.edit().putString("categoryList",value).apply();
    }

    public List<Categories> getCategoryList(){
        String value = pref.getString("categoryList", "[]");
        List<Categories> categoriesList = new Gson().fromJson(value, new TypeToken<List<Categories>>(){}.getType());
        return categoriesList;
    }

    public List<FailedReason> getFailedReasons() {
        String value = pref.getString("failedReason", "[]");
        List<FailedReason> reasons = new Gson().fromJson(value, new TypeToken<List<FailedReason>>(){}.getType());
        return reasons;
    }

    public void saveMap(Map<String, LatLng> inputMap) {
        if (pref != null) {
            JSONObject jsonObject = new JSONObject(inputMap);
            String jsonString = jsonObject.toString();
            editor.putString("start_location", jsonString).apply();
        }
    }


    public Map<String, LatLng> loadMap() {
        Map<String, LatLng> outputMap = new HashMap<String, LatLng>();
        try {
            if (pref != null) {
                String jsonString = pref.getString("start_location", (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while (keysItr.hasNext()) {
                    String key = keysItr.next();
                    LatLng value = (LatLng) jsonObject.get(key);
                    outputMap.put(key, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputMap;
    }

    public Set getTaskIds() {
        return pref.getStringSet("task_ids", null);
    }

    public void setTaskIds(Set taskIds) {
        pref.edit().putStringSet("task_ids", taskIds).apply();
    }

    public void setFilterSeletedList(Set stringList) {
        pref.edit().putStringSet("filter_list", stringList).apply();
    }

    public void setStatusFilterList(String value) {
        pref.edit().putString("trip_filter_list", value).apply();
    }

    public List<String> getStatusFilterList() {
        List<String> statusList = new ArrayList<>();
        String values = pref.getString("trip_filter_list", "[]");
        if(values.isEmpty()) {
            values = "[]";
        }
        String[] myArray = values.split(",");
        return Arrays.asList(myArray);
    }

    public Set getAdminList() {
        return pref.getStringSet("manager_list", null);
    }

    public void setAdminList(Set stringList) {
        pref.edit().putStringSet("manager_list", stringList).apply();
    }

    public Set getFilterSelectedList() {
        return pref.getStringSet("filter_list", null);
    }


    public String GetEngDecimalFormatValues(double value) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat) nf;
        df.applyPattern("##0.00");
        return df.format(value);
    }

    public void setFilterList(Filter filter) {
        Gson gson = new Gson();
        String json = gson.toJson(filter); // myObject - instance of MyObject
        pref.edit().putString("filter_data", json).apply();
    }

    public String changeDateForamat(String old_date) {

        String new_date = null;
        Date date;

        try {
            date = SERVER_NOTES_FORMAT.parse(old_date);
            new_date = DEFAULT_DATE_FORMAT.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new_date;
    }

    public String changeUtcDateForamat(String old_date) {

        String new_date = null;
        Date date;

        try {
            date = SERVER_NOTIFICATIONS_FORMAT.parse(old_date);
            new_date = DEFAULT_DATE_FORMAT.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new_date;
    }

    public String changeNotificationDateForamat(String old_date) throws ParseException {

//        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
//        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);

        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        DateFormat outputFormat = new SimpleDateFormat("dd MMM, ", Locale.ENGLISH);
        Date date = inputFormat.parse(old_date);
        String outputDateStr = outputFormat.format(date);
        return outputDateStr;
    }

    public String changeNotificationTimeForamat(String old_date) throws ParseException {

//        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
//        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);

        DateFormat inputFormat = new SimpleDateFormat("hh:mm:ss a", Locale.ENGLISH);
        DateFormat outputFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        Date date = inputFormat.parse(old_date);
        String outputDateStr = outputFormat.format(date);
        return outputDateStr;
    }

    public String changeNotificationActivityDateForamat(String old_date) throws ParseException {

        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a", Locale.ENGLISH);
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss a", Locale.ENGLISH);
        Date date = inputFormat.parse(old_date);
        String outputDateStr = outputFormat.format(date);
        return outputDateStr;
    }


    /* it will return decimal format with appending .0 */
    public String GetLatDecimalFormatValues(double value) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat) nf;
        df.applyPattern("#.0000");
        return df.format(value);
    }

    public float getTotalLiveDistance() {
        return pref.getFloat("total_live_distance", 0.0f);
        //return totalLiveDistance;
    }

    public void setTotalLiveDistance(float totalLiveDistance) {
        pref.edit().putFloat("total_live_distance", totalLiveDistance).apply();
        this.totalLiveDistance = totalLiveDistance;
    }

    public float getDoubleLatitude() {
        return pref.getFloat("saved_lat", 0.0f);
    }

    public void setDoubleLatitude(float savedLatitude) {
        pref.edit().putFloat("saved_lat", savedLatitude).apply();
        this.savedLatitude = savedLatitude;
    }

    public float getDoubleLongitude() {
        return pref.getFloat("saved_lng", 0.0f);
    }

    public void setDoubleLongitude(float savedLongitude) {
        pref.edit().putFloat("saved_lng", savedLongitude).apply();
        this.savedLongitude = savedLongitude;
    }

    public void setRadiusDistance(String keyName, String value) {
        pref.edit().putString(keyName, value).apply();
    }

    public String getRadiusDistance() {
        return pref.getString("radiusDistance", "0.0");
    }

    public void setRadiusValidation(String keyName, boolean value) {
        pref.edit().putBoolean(keyName, value).apply();
    }

    public boolean getRadiusValidation() {
        return pref.getBoolean("radiusValidation", false);
    }

    public void setIdleLogEnable(String keyName, boolean value) {
        pref.edit().putBoolean(keyName, value).apply();
    }

    public boolean getIdleLogEnable() {
        return pref.getBoolean("isIdleLogEnable", false);
    }

    public void setTransitRoadAPI(String keyName, boolean value) {
        pref.edit().putBoolean(keyName, value).apply();
    }

    public boolean getTransitRoadAPI() {
        return pref.getBoolean("isTransitRoadApi", false);
    }

    public void setTaskPinCode(String keyName, boolean value) {
        pref.edit().putBoolean(keyName, value).apply();
    }

    public boolean getTaskPinCode() {
        return pref.getBoolean("isTaskPinCode", false);
    }

    public void setDriverStatusType(String keyName, String stringList) {
        pref.edit().putString(keyName, stringList).apply();
    }

    public Set getDriverStatusType() {
        return pref.getStringSet("driverStatusList", null);
    }

    public float getAccuracy() {
        return pref.getFloat("saved_accu", 0.0f);
    }

    public void setAccuracy(float accuracy) {
        pref.edit().putFloat("saved_accu", accuracy).apply();
        this.accuracy = accuracy;
    }

    public String getPinCode() {
        return pref.getString("task_pincode", "");
    }

    public void setPinCode(String pinCodes) {
        pref.edit().putString("task_pincode", pinCodes).apply();
        pinCode = pinCodes;
    }

    public void setPODImageCompress(String keyName, boolean value) {
        pref.edit().putBoolean(keyName, value).apply();
    }


    public boolean getPODImageCompress() {
        return pref.getBoolean("isPODImageCompress", false);
    }

    public void setIsEarningEnabled(String keyName, boolean value) {
        pref.edit().putBoolean(keyName, value).apply();
    }

    public boolean isAppLogicEnabled() {
        return pref.getBoolean("isAppLogicEnabled", false);
    }

    public void setIsAdditionalStatusEnabled(boolean value) {
        pref.edit().putBoolean("isAdditionalStatusEnabled", value).apply();
    }

    public boolean isAdditionalStatusEnabled() {
        return pref.getBoolean("isAdditionalStatusEnabled", false);
    }

    public void setIsFailedOrCancelled(boolean value) {
        pref.edit().putBoolean("isFailedOrCancelled", value).apply();
    }

    public boolean isFailedOrCancelled() {
        return pref.getBoolean("isFailedOrCancelled", false);
    }

    public void setIsActualCustomer(boolean value) {
        pref.edit().putBoolean("isActualCustomer", value).apply();
    }

    public boolean isActualCustomer() {
        return pref.getBoolean("isActualCustomer", false);
    }

    public void setAppLogicEnabled(boolean value) {
        pref.edit().putBoolean("isAppLogicEnabled", value).apply();
    }

    public boolean getEarningEnabled() {
        return pref.getBoolean("isEarning", false);
    }

    public void setIsCrmEnabled(boolean value) {
        pref.edit().putBoolean("isCrmEnabled", value).apply();
    }

    public boolean isCrmEnabled() {
        return pref.getBoolean("isCrmEnabled", false);
    }


    public void setIsGlympseEnabled(String keyName, boolean value) {
        pref.edit().putBoolean(keyName, value).apply();
    }

    public boolean getGlympseEnabled() {
        return pref.getBoolean("isGlympse", false);
    }


    public void setGlympseUserName(String keyName, String Value) {
        pref.edit().putString(keyName, Value).apply();
    }

    public String getGlympseUserName() {
        return pref.getString("glympseUserName", "");
    }

    public void setGlympseUserPswd(String keyName, String Value) {
        pref.edit().putString(keyName, Value).apply();
    }

    public String getGlympseUserPswd() {
        return pref.getString("glympsePassword", "");
    }

    public void setGlympseID(String glympseID, String glympseTaskID) {
        pref.edit().putString(glympseID, glympseTaskID).apply();
    }

    public String getGlympseID() {
        return pref.getString("glympseID", "");
    }

}
