package com.app.deliforcemanager.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.app.deliforcemanager.ModelClass.GetProfile.DriverStatusTypes;
import com.app.deliforcemanager.R;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AppUtils {
    static String strAdd = "";
    private static Location lastKnowLocation;

    /**
     * read device's battery percentage
     */
    public static int getBatteryPercentage(Context context) {
        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, iFilter);

        int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
        int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

        float batteryPct = level / (float) scale;

        return (int) (batteryPct * 100);
    }

    private static List<DriverStatusTypes> driverStatusType = new ArrayList<>();

    private static List<LatLng> liveDataLatLng = new ArrayList<>();

    public static void setLiveTransitLatLng(LatLng latLngs) {
        liveDataLatLng.add(latLngs);
    }

    public static List<LatLng> getLiveDataLatLng() {
        return liveDataLatLng;
    }

    public static void setDriverStatusType(List<DriverStatusTypes> latLngs) {
        driverStatusType = new ArrayList<>();
        driverStatusType.addAll(latLngs);
    }

    public static List<DriverStatusTypes> getDriverStatusType() {
        return driverStatusType;
    }

    public static void clearLiveLatLngData() {
        liveDataLatLng.clear();
    }

    /**
     * Returns the consumer friendly device name
     */
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }


    /**
     * based on lat and lang, get formatted address
     */
    public static String getCompleteAddressString(Context con, double LATITUDE, double LONGITUDE) {
        Log.e("LATITUDE", "--" + LATITUDE);
        Log.e("LONGITUDE", "--" + LONGITUDE);
        Geocoder geocoder = new Geocoder(con, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null && addresses.size() > 0) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction--> ", strReturnedAddress.toString());
            } else {
                Log.w("Current loction add--> ", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My loction address--> ", "Canont get Address!");
        }
        return strAdd;

//        new Thread(() -> {
//            Geocoder gCoder = new Geocoder(con,Locale.getDefault());
//            if (Geocoder.isPresent())
//            try {
//                final List<Address> list = gCoder.getFromLocation(
//                        LATITUDE, LONGITUDE, 1);
//                if (list != null && list.size() > 0) {
//                    Address address = list.get(0);
//                    StringBuilder sb = new StringBuilder();
//                    if (address.getAddressLine(0) != null) {
//                        sb.append(address.getAddressLine(0)).append("\n");
//                    }
////                    sb.append(address.getLocality()).append(",");
////                    sb.append(address.getPostalCode()).append(",");
////                    sb.append(address.getCountryName());
//                    strAdd = sb.toString();
//
//                }
//
//            } catch (IOException exc) {
//                exc.printStackTrace();
//
//                Log.e("exception",""+ exc.getMessage());
//            }
//        }).start();
//        return strAdd;
    }


    public static LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    public static void turnGPSOn(Context con) {
        String provider = Settings.Secure.getString(con.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);


        if (provider!=null) {
            if (!provider.contains("gps")) { //if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                con.sendBroadcast(poke);
            }
        }
    }

    public static void setDeviceLocation(Location location, String simpleName) {
        System.out.println("Device Location from " + simpleName + "; " + location);
        lastKnowLocation = location;
    }

    public static Location getDeviceLocation() {
        return lastKnowLocation;
    }

    public static void setDeviceLocationDatas(float doubleLatitude, float doubleLongitude, float accuracy) {
        if (lastKnowLocation == null) {
            lastKnowLocation = new Location("");
            lastKnowLocation.setAccuracy(accuracy);
            lastKnowLocation.setLatitude(doubleLatitude);
            lastKnowLocation.setLongitude(doubleLongitude);
        }
    }

    public static String getStatus(Context context, int location) {
        LoginPrefManager loginPrefManager = new LoginPrefManager(context);
        if(loginPrefManager.isAdminStatusEnabled()) {
           return getAdminStatus(context,location);
        } else {
            return getStaticStatus(context, location);
        }
    }

    public static String getButtonStatus(Context context, int location) {
        LoginPrefManager loginPrefManager = new LoginPrefManager(context);
        if(loginPrefManager.isAdminStatusEnabled()) {
           return getAdminStatus(context,location);
        } else {
            return getStaticButtonStatus(context, location);
        }
    }

    public static String getAdminStatus(Context context, int location) {
        List<DriverStatusTypes> dataStatusList = AppUtils.getDriverStatusType();
        for (DriverStatusTypes driverStatusTypes : dataStatusList) {
            if (driverStatusTypes != null && driverStatusTypes.getId() == location) {
                return driverStatusTypes.getMobileStatus();
            }
        }
        return context.getString(R.string.task_status_success);
    }

    public static String getStaticStatus(Context context, int location) {
        switch (location) {
            case 1:
                return "UnAssigned";
            case 2:
                return context.getString(R.string.status_two_past);
            case 3:
                return context.getString(R.string.status_three_past);
            case 4:
                return context.getString(R.string.status_four_past);
            case 5:
                return context.getString(R.string.status_five_past);
            case 6:
                return context.getString(R.string.status_six_past);
            case 7:
                return context.getString(R.string.status_seven_past);
            case 8:
                return context.getString(R.string.status_eight_past);
            case 9:
                return context.getString(R.string.status_nine_past);
            case 10:
                return context.getString(R.string.status_ten_past);
            case 11:
                return context.getString(R.string.status_eleven_past);
            default:
                return context.getString(R.string.task_status_success);
        }
    }

    public static String getStaticButtonStatus(Context context, int location) {
        switch (location) {
            case 1:
                return "UnAssigned";
            case 2:
                return context.getString(R.string.status_two_future);
            case 3:
                return context.getString(R.string.status_three_future);
            case 4:
                return context.getString(R.string.status_four_future);
            case 5:
                return context.getString(R.string.status_five_future);
            case 6:
                return context.getString(R.string.status_six_future);
            case 7:
                return context.getString(R.string.status_seven_future);
            case 8:
                return context.getString(R.string.status_eight_future);
            case 9:
                return context.getString(R.string.status_nine_future);
            case 10:
                return context.getString(R.string.status_ten_future);
            case 11:
                return context.getString(R.string.status_eleven_future);
            default:
                return context.getString(R.string.task_status_success);
        }
    }
}
