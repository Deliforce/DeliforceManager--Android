package com.app.deliforcemanager.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.app.deliforcemanager.LocalizationActivity.LocalizationActivity;
import com.app.deliforcemanager.R;
import com.app.deliforcemanager.Utils.DeliforceConstants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Objects;

//import com.crashlytics.android.Crashlytics;

//import io.fabric.sdk.android.Fabric;

public class SplashActivity extends LocalizationActivity {

    public static Runnable runnable;

    String currentVersion;


    @SuppressLint("TimberArgCount")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);


//        ArrayList<Integer> list = new ArrayList<>();
//        list.add(1);
//        list.add(2);
//        list.add(4);
//        list.add(5);
//        list.add(1);
//        list.add(2);
//        list.add(12);
//        list.add(14);
//        list.add(12);
//        list.add(4);
//        list.add(2);
//        list.add(4);
//        list.add(1);
//
//        ArrayList<Integer> all = new ArrayList<Integer>();
//
//
//        ArrayList<Integer> duplicate = new ArrayList<Integer>();
//
//
//
//        for(int i=0;i<list.size();i++){
//
//            Integer value = list.get(i);
//
//            if(!all.contains(value)){
//                all.add(list.get(i));
//            }else{
//                duplicate.add(list.get(i));
//
//
//
//                int size = 1;
//
//                for(int j=0;j<duplicate.size();j++){
//
//                    Integer value1 = duplicate.get(j);
//
//                    if (Objects.equals(value, value1)){
//                        size=size+1;
//
//                    }
//
//                }
//
//                Log.e("duplicated_values",value+" duplicated "+ size);
//              //  Toast.makeText(SplashActivity.this,value+ "repeated"+size,Toast.LENGTH_LONG).show();
//
//            }
//
//        }
//
//        Log.d("all_values",all.toString());
//
//        Log.d("duplicates",duplicate.toString());



//        FirebaseCrash.report(new Exception("Exception"));
//        Fabric.with(this, new Crashlytics());
        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            loginPrefManager.setCurrentVersionName(currentVersion);
            loginPrefManager.setSkipversion(false);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

       // String deviceToken = loginPrefManager.getDeviceToken();

        removeFilters();

      //  Timber.e(loginPrefManager.getCogintoToken());

        loginPrefManager.setDriverBlock("");
        loginPrefManager.setStatusFilterList("[]");
        loginPrefManager.setStringValue("filter_values", "");
        loginPrefManager.setStringValue("bar_Code_value", "");
        loginPrefManager.setStringValue("image", "");
        loginPrefManager.setStringValue("chk_search_task_sort_by_time", "");
        runnable = () -> {
            loginPrefManager.setFromDate("");
            loginPrefManager.setToDate("");

            if (loginPrefManager.getManagerID().equals("null") || loginPrefManager.getManagerID().equals("")) {
                Intent menu_intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(menu_intent);
                finish();
            } else {
                Intent menu_intent = new Intent(SplashActivity.this, NavigationActivity.class);
                startActivity(menu_intent);
                finish();
            }

        };

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(SplashActivity.this, instanceIdResult -> {
            String newToken = instanceIdResult.getToken();
            //Timber.e(newToken);
            loginPrefManager.setDeviceToken(newToken);
        });

        String device_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        loginPrefManager.setStringValue("device_id", "" + device_id);

//        loginPrefManager.setFilterSeletedList();

        loginPrefManager.setStringValue("notification_count", "0");
        loginPrefManager.setStringValue("order_id", "");
    }

    private void removeFilters() {

        loginPrefManager.remove(DeliforceConstants.PREF_FILTER_ALL);
        loginPrefManager.remove(DeliforceConstants.PREF_FILTER_ACCEPTED);
        loginPrefManager.remove(DeliforceConstants.PREF_FILTER_STRATED);
        loginPrefManager.remove(DeliforceConstants.PREF_FILTER_SUCCESS);
        loginPrefManager.remove(DeliforceConstants.PREF_FILTER_FAILED);
        loginPrefManager.remove(DeliforceConstants.PREF_FILTER_CANCELLED);
        loginPrefManager.remove(DeliforceConstants.PREF_FILTER_START_DATE);
        loginPrefManager.remove(DeliforceConstants.PREF_FILTER_END_DATE);
        loginPrefManager.remove(DeliforceConstants.PREF_FILTER_DECLINED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissionHelper != null)
            permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                finish();
            }
            return false;
        }
        return true;
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
