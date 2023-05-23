package com.app.deliforcemanager.Utils;

import android.content.res.Resources;

public class DeliforceConstants {

    //    public static final int LOCATION_INTERVAL_FOR_SERVER_CALL = 1000 * 60 * 3; //server time interval
//    public static final int LOCATION_INTERVAL_FOR_MQTT_PUBLISH = 10000; // MQTT time interval
    public static final int LOCATION_INTERVAL_FOR_API_TIME = 120000; //API CALL INTERVEL TIME
    public static final int LOCATION_INTERVAL_FOR_ETA_TIME = 20000; // MQTT time interval
    public static final int LOCATION_TRACK_MIN_TIME = 1; // MQTT time interval
    public static final int IDLE_TIME_UPDATE = 300000;//3600000; // MQTT time interval
//    public static final int IDLE_TIME_UPDATE = 5000; // MQTT time interval

    //Driver status
    public static final int DRIVER_STATUS_IDLE = 1;
    public static final int DRIVER_STATUS_IN_TRANSIT = 2;
    public static final int DRIVER_STATUS_OFFLINE = 3;


    //task Status related
    public static final int TASK_ASSIGNED = 2;
    public static final int TASK_ACCEPTED = 3;
    public static final int TASK_STARTED = 4;
    public static final int TASK_ARRIVED = 5;
    public static final int TASK_SUCCESS = 6;
    public static final int TASK_FAILED = 7;
    public static final int TASK_DECLINED = 8;
    public static final int TASK_STATUS_CANCELLED = 9;
    public static final int TASK_STATUS_ACKNOWLEDGE = 10;
    public static final int TASK_STATUS_CUSTOMER_NOT_AVAILABLE = 30;
    public static int TASK_TEMPLATE_STATUS= 21;
    public static final int TASK_ADD_TEMPLATE_STATUS= 22;

    public static final int IS_TASK_ACKNOWLEDGE_NEEDED = 1;
    public static final int IS_TASK_ACCEPT_REJECT_NEEDED = 2;
    public static final int IS_TASK_START_NEEDED = 3;

    //dialog type
    public static final int DIALOG_USER_LOGOUT = 201;


    public static final int ACCEPT_NOTIFICATION_TIMER = 30000;
    public static final int NOTIFICATION_COUNTDOWN_INTERVAL = 1000;

    //ACK Type related
    public static final int ACK_TYPE_ACKNOWLEDGEMENT = 1;
    public static final int ACK_TYPE_ACCEPT_DECLINE = 2;
    public static final int ACK_TYPE_START = 3;
    public static final int AUTO_ALLOCATION_ONE_BY_ONE = 4;
    public static final int AUTO_ALLOCATION_NEAREST_AVAILABLE = 5 * NOTIFICATION_COUNTDOWN_INTERVAL;


    //filter status
    public static final int FILTER_ALL = 2;
    public static final int FILTER_ACCEPTED = 3;
    public static final int FILTER_STARTED = 4;
    public static final int FILTER_SUCCESS = 6;
    public static final int FILTER_FAILED = 7;
    public static final int FILTER_DECLINED = 8;
    public static final int FILTER_CANCELLED = 9;

    //filter pref values
    public static final String PREF_FILTER_ALL = "ALL";
    public static final String PREF_FILTER_STRATED = "STARTED";
    public static final String PREF_FILTER_ACCEPTED = "ACCEPTED";
    public static final String PREF_FILTER_ARRIVED = "ARRIVED ";
    public static final String PREF_FILTER_SUCCESS = "SUCCESS";
    public static final String PREF_FILTER_FAILED = "FAILED";
    public static final String PREF_FILTER_DECLINED = "DECLINED";
    public static final String PREF_FILTER_CANCELLED = "CANCELLED";
    public static final String PREF_FILTER_START_DATE = "START_DATE";
    public static final String PREF_FILTER_END_DATE = "END_DATE";

    //settings related
    public static final String PREF_SETTINGS_VIBRATE_LONG = "LONG_VIBRATE";
    public static final String PREF_SETTINGS_VIBRATE_SYSTEM = "SYSTEM_VIBRATE";
    public static final String PREF_SETTINGS_REPEAT_RING = "REPEAT_RING";
    public static final String PREF_SETTINGS_SHOW_TRAFFIC = "IS_SHOW_TAFFIC_ENABLED";
    public static final String PREF_SETTINGS_MAP_STYLE = "MAP_STYLE";


    //    Vehicle types
    public static final String PREF_SETTINGS_VEHICLE_TRUCK = "1";
    public static final String PREF_SETTINGS_VEHICLE_CAR = "2";
    public static final String PREF_SETTINGS_VEHICLE_BIKE = "3";
    public static final String PREF_SETTINGS_VEHICLE_SCOOTER = "4";
    public static final String PREF_SETTINGS_VEHICLE_CYCLE = "5";
    public static final String PREF_SETTINGS_VEHICLE_WALK = "6";

    //business type
    public static final int BUSSINESS_TYPE_PICKUP_DROP = 1;

    //    Vehicle types
    public static final String NotesPage = "1";
    public static final String ImagesPage = "2";
    public static final String SignaturePage = "3";
    public static final String BarcodePage = "4";
    public static final float SMALLEST_DISPLACEMENT = 10;
    public static final long LOCATION_UPDATE_INTERVAL = 5000;
    public static  boolean isfromNewTask =false;
    public static  boolean isfromTaskDetail =false;

    public static final int isActualCustomer = 101;
    public static final int isCaptureLocation = 102;

    public static int SIGNATURE_REQUEST_CODE= 111;
    public static int ROUTE_RESULT_CODE= 112;

    public static String NavigationGoogleMap = "Google Map";
    public static String NavigationWaze = "Waze";


    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float sp2px(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    //language
    public static String LANGUAGE_ENGLISH = "1";
    public static String LANGUAGE_SPANISH = "2";
    public static String LANGUAGE_FRENCH = "3";
    public static String LANGUAGE_PORTUGUESE = "4";
    public static String LANGUAGE_MALAY = "5";
    public static String LANGUAGE_ARABIC = "6";

    //language code
    public static String LANGUAGE_CODE_ENGLISH = "en";
    public static String LANGUAGE_CODE_SPANISH = "es";
    public static String LANGUAGE_CODE_FRENCH = "fr";
    public static String LANGUAGE_CODE_PORTUGUESE = "pt";
    public static String LANGUAGE_CODE_MALAY = "ms";
    public static String LANGUAGE_CODE_ARABIC = "ar";
}

