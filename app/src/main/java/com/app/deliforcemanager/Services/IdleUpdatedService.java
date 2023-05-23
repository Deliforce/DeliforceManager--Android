package com.app.deliforcemanager.Services;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.StrictMode;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.amazonaws.mobile.auth.userpools.MFAActivity;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.tokens.CognitoAccessToken;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.tokens.CognitoIdToken;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.tokens.CognitoRefreshToken;
import com.app.deliforcemanager.Activities.SplashActivity;
import com.app.deliforcemanager.BuildConfig;
import com.app.deliforcemanager.Cognito.AppHelper;
import com.app.deliforcemanager.LocalizationActivity.LocalizationActivity;
import com.app.deliforcemanager.ModelClass.DriverStatusApi.DriverStatusUpdate;
import com.app.deliforcemanager.ModelClass.IdleDistance.AnalyticsApi;
import com.app.deliforcemanager.ModelClass.TrackingRecord.LiveTrackingModelData;
import com.app.deliforcemanager.ModelClass.TrackingRecord.TrackingModelData;
import com.app.deliforcemanager.R;
import com.app.deliforcemanager.RawHeaders.Analytics.AnalyticsData;
import com.app.deliforcemanager.RawHeaders.DriverStatusInfo.DriverStatus;
import com.app.deliforcemanager.ServiceApi.APIServiceFactory;
import com.app.deliforcemanager.ServiceApi.ApiService;
import com.app.deliforcemanager.Utils.AppUtils;
import com.app.deliforcemanager.Utils.DeliforceConstants;
import com.app.deliforcemanager.Utils.LoginPrefManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class IdleUpdatedService extends Service {

    private static final String CHANNEL_ID = "NOTIFY_LOCATION";
    NotificationCompat.Builder notificationBuilder;
    NotificationChannel notificationChannel;
    NotificationManager notificationManager;
    NotificationChannel channel;
    Notification notification;
    private FusedLocationProviderClient fusedLocationProviderClient = null;
    int NOTIFICATION_ID = 90;


    LoginPrefManager loginPrefManager;
    CountDownTimer driver_idle_timer, driver_change_status_timer;
    Date start_date, end_date;
    public static Location liveLastLocation, mLastLocation, mCurrentLocation,mLastIdleLocation;
    long minutes;
    DriverStatus driverStatus;

    private static final String TAG = IdleUpdatedService.class.getSimpleName();
    boolean onStart = true;

    private float driver_travelld_distance = 0;
    private ApiService apiService;
    private int LOCATION_IDLE_SERVICE_TIME;
    private int DRIVER_CHANGE_STATUS_UPDATE;
    private boolean status_timer;
    private boolean idle_timer;
    private int LOCATION_SERVICE_TIME;
    Boolean idleCall = true;
    //private double computeDistance;

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);


            Location lastLocation = locationResult.getLastLocation();
            if (lastLocation != null) {
                Log.e("location_call","11");
                onNewLocation(lastLocation);
            }
        }
    };

    void createNotification() {
//        if (loginPrefManager.getDriverID().equals("null") || loginPrefManager.getDriverID().equals("")) {
//            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.cancel(NOTIFICATION_ID);
//            stopForeground(true);
//            stopSelf();
//        } else {
//            showNotification();
//        }
    }

    void unregisterForLocationUpdates() {
        if (fusedLocationProviderClient != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }

    private void showNotification() {
        int requestID = (int) System.currentTimeMillis();

        String notificationTitle = getString(R.string.you_are_in_online);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
            notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(notificationChannel);
        } else {
            notificationBuilder = new NotificationCompat.Builder(this);
        }
        Intent notificationIntent = new Intent(this, SplashActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = null;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){

            pendingIntent = PendingIntent.getActivity(this, requestID, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        }else{
            pendingIntent = PendingIntent.getActivity(this, requestID, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
        }
        notificationBuilder
                .setContentText(notificationTitle)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.notification_ic))
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setSmallIcon(R.mipmap.ic_launcher);

        notificationBuilder.setLights(Color.YELLOW, 1000, 300);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID,
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            notification = new Notification.Builder(getApplicationContext(), CHANNEL_ID).build();
            startForeground(1, notification);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Timber.i("onBind()");
        return null;
    }

    @Override
    public int onStartCommand(Intent intentValue, int flags, int startId) {
        if (intentValue != null && intentValue.getExtras() != null) {
            if (intentValue.getExtras().getBoolean("Trip_Completed", true)) {
                AppUtils.clearLiveLatLngData();
            }
        }
        createLocationRequest();
        getLastLocation();
        createNotification();
        return Service.START_STICKY;
    }

    private ScheduledThreadPoolExecutor scheduledThreadPool;


    @Override
    public void onCreate() {
        super.onCreate();
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        apiService = APIServiceFactory.getRetrofit().create(ApiService.class);
        loginPrefManager = new LoginPrefManager(getBaseContext());

        locationPrefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        LOCATION_IDLE_SERVICE_TIME = Integer.parseInt(loginPrefManager.getStringValue("idle_location_time")) * 1000;
        DRIVER_CHANGE_STATUS_UPDATE = Integer.parseInt(loginPrefManager.getStringValue("idle_distance_update_frequency")) * 1000;

        LOCATION_SERVICE_TIME = Integer.parseInt(loginPrefManager.getStringValue("mqtt_time")) * 1000;

        Timber.tag(TAG).e("LIVE_LOCATION_TIME: %s", LOCATION_SERVICE_TIME);
        Timber.tag(TAG).e("IDLE_SERVICE_TIME------%s", LOCATION_IDLE_SERVICE_TIME);
        Timber.tag(TAG).e("IDLE_STATUS_TIME------%s", DRIVER_CHANGE_STATUS_UPDATE);

        loginPrefManager.setStringValue("stop_service", "1");

        status_timer = true;
        idle_timer = true;
        DriverIdleTimer();
        DriverStatusTimer();
        driver_change_status_timer.start();

        if (loginPrefManager.getStringValue("analytic_date").isEmpty()) {
            getCurrentDate();
        } else {
            try {
                if (checkCurrentDate()) {
                    driver_idle_timer.start();
                } else {
                    loginPrefManager.setStringValue("analytic_date", "");
                    loginPrefManager.setStringValue("current_date", "");
                    getCurrentDate();

                    //loginPrefManager.setStringValue("idle_distance", "");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        scheduledThreadPool = new ScheduledThreadPoolExecutor(2);
        scheduledThreadPool.scheduleWithFixedDelay(new LiveTrackingDriver(), 0, LOCATION_SERVICE_TIME, TimeUnit.MILLISECONDS);
    }

    private void getLastLocation() {

//        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
//                    if (task.isSuccessful() && task.getResult() != null) {
//                        Log.e("location_call","22");
//                        onNewLocation(task.getResult());
//                    }
//                });

      }

    private void createLocationRequest() {
        FusedLocationProviderClient locationProviderClient = getFusedLocationProviderClient();
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(DeliforceConstants.LOCATION_UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(DeliforceConstants.NOTIFICATION_COUNTDOWN_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        Looper looper = Looper.myLooper();
       // locationProviderClient.requestLocationUpdates(mLocationRequest, locationCallback, looper);
    }

    private FusedLocationProviderClient getFusedLocationProviderClient() {
        if (fusedLocationProviderClient == null) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        }
        return fusedLocationProviderClient;
    }

    private static final String PACKAGE_NAME = "com.app.deliforce.Services.IdleService";

    public static final String ACTION_BROADCAST = PACKAGE_NAME + ".broadcast";
    public static final String EXTRA_LOCATION = PACKAGE_NAME + ".location";
    public static final String GPS_STATUS = PACKAGE_NAME + ".gps_status";

    private void onNewLocation(Location location) {
       /* if (location.hasAccuracy())
            if (location.getAccuracy() > STANDARD_ACCURACY)
                return;*/

        if (location.isFromMockProvider()) {
            if (!BuildConfig.DEBUG) {
                Toast.makeText(this, "Mock location is not allowed. Kindly turn OFF. Reporting to Admin", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (mLastLocation == null) {
            mLastLocation = location;
            onStart = false;
        }

        if (liveLastLocation == null)
            liveLastLocation = location;

        AppUtils.setDeviceLocation(location, IdleUpdatedService.class.getSimpleName());

        LocalizationActivity.current_longitude = location.getLongitude();
        LocalizationActivity.current_latitude = location.getLatitude();
        LocalizationActivity.current_accuraccy = location.getAccuracy();

        mCurrentLocation = location;
        loginPrefManager.setDoubleLatitude(Float.parseFloat("" + location.getLatitude()));
        loginPrefManager.setDoubleLongitude(Float.parseFloat("" + location.getLongitude()));
        loginPrefManager.setAccuracy(location.getAccuracy());

        float speed = 0;
        if (location.hasSpeed()) {
            speed = location.getSpeed(); //speed in meter/minute
            speed = (speed * 3600) / 1000;      // speed in km/minute
        }

        if (loginPrefManager.getStringValue("driver_status").equals("2"))
            AppUtils.setLiveTransitLatLng(new LatLng(location.getLatitude(), location.getLongitude()));

       /* if (location.getAccuracy() < STANDARD_ACCURACY && mLastLocation.getAccuracy() < STANDARD_ACCURACY
                && speed > 0) {*/
        Timber.tag(TAG).e("LiveLogDistance %s", getDistance(location.getLatitude(), location.getLongitude(),
                mLastLocation.getLatitude(), mLastLocation.getLongitude()));

        String driverStatus = loginPrefManager.getStringValue("driver_status");

        Log.e("driverStatus",driverStatus);

        if (driverStatus.equals("1")) {
            if (idleCall) {
                idleCall = false;
                float finalSpeed = speed;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        idleCall = true;
                        Log.e("idle_received","true");
                        createAIdleLog(location, getDistance(location.getLatitude(), location.getLongitude(),
                                mLastLocation.getLatitude(), mLastLocation.getLongitude()), finalSpeed);
                    }
                },  Integer.parseInt(loginPrefManager.getStringValue("idle_location_time"))+240 * 1000);
            }else{
                Log.e("idle_received","false");
            }

        } else if (driverStatus.equals("2")) {
            createALiveLog(location, getDistance(location.getLatitude(), location.getLongitude(),
                    mLastLocation.getLatitude(), mLastLocation.getLongitude()), speed);
        }
//        }

//        if (location.getAccuracy() < STANDARD_ACCURACY && mLastLocation.getAccuracy() < STANDARD_ACCURACY) {
        float[] results = new float[1];
        Location.distanceBetween(location.getLatitude(), location.getLongitude(),
                mLastLocation.getLatitude(), mLastLocation.getLongitude(), results);
        driver_travelld_distance += results[0]; // return in meters
//        }
        mLastLocation = location;
    }

    private void DriverStatusTimer() {
        driver_change_status_timer = new CountDownTimer(LOCATION_IDLE_SERVICE_TIME, 1000) {
            @Override
            public void onTick(long millis) {
                status_timer = true;
                String idleTimer = String.format(Locale.ENGLISH, "%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(millis),
                        TimeUnit.MILLISECONDS.toMinutes(millis) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), // The change is in this line
                        TimeUnit.MILLISECONDS.toSeconds(millis) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
               // Timber.tag("driver status part: ").w(idleTimer);
                if (!idle_timer && driver_idle_timer != null) {
                    idle_timer = true;
                }
            }

            @Override
            public void onFinish() {
                status_timer = false;
                Timber.tag(TAG).d("Change status finished with status-->  %s", loginPrefManager.getStringValue("driver_status"));
                if (loginPrefManager.getStringValue("driver_status").equals("1")) {
                    int admin_min_distance = loginPrefManager.getIntValue("idleDistanceUpdate");
                    Timber.tag(TAG).e("admin_min_distance---%s", admin_min_distance);
                    Timber.tag(TAG).e("driver_travelld_dis---%s", driver_travelld_distance);

                    if (admin_min_distance < 0) {
                        Timber.tag(TAG).e("admingtthan0 true");
                        admin_min_distance = 20;
                    }
//                    if (driver_travelld_distance >= admin_min_distance)
//                        updateDriverStatus();
//                    else {
//                        Timber.tag(TAG).e("skip drv status update---%s", driver_travelld_distance);
//                        driver_change_status_timer.start();
//                    }
                } else {
                    Timber.v("Restarting the Driver status countdown");
                    driver_change_status_timer.start();
                }
            }
        };
    }

    private void DriverIdleTimer() {
        driver_idle_timer = new CountDownTimer(DRIVER_CHANGE_STATUS_UPDATE, 1000) {
            @Override
            public void onTick(long millis) {
                idle_timer = true;
                String idleTimer = String.format(Locale.ENGLISH, "%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(millis),
                        TimeUnit.MILLISECONDS.toMinutes(millis) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), // The change is in this line
                        TimeUnit.MILLISECONDS.toSeconds(millis) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                //Timber.tag("driver idle part: ").i(idleTimer);
                if (!status_timer && driver_change_status_timer != null) {
                    Timber.tag("Status time not running");
                }
            }

            @Override
            public void onFinish() {
                idle_timer = false;
                Timber.tag(TAG).d("Idle part finished with status--> %s", loginPrefManager.getStringValue("driver_status"));
                try {
                    if (loginPrefManager.getStringValue("driver_status").equals("1")) {
                        if (!loginPrefManager.getStringValue("current_date").isEmpty()) {
                            if (checkCurrentDate()) {
                                getTimerDate();
                            } else {
                                loginPrefManager.setStringValue("analytic_date", "");
                                loginPrefManager.setStringValue("current_date", "");
                                getCurrentDate();
                            }
                        } else {
                            getTimerDate();
                        }
                    } else {
                        Timber.tag(TAG).v("Restarting the Idle status countdown");
                        driver_idle_timer.start();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    getCurrentDate();
                }
            }
        };
    }

    private void updateDriverLiveStatus() {
        try {
            driverStatus = new DriverStatus();
            driverStatus.setTimezone(TimeZone.getDefault().getID());
            driverStatus.setDriverStatus(Integer.parseInt(loginPrefManager.getStringValue("driver_status")));
            driverStatus.setCurrentLat(mCurrentLocation.getLatitude());
            driverStatus.setCurrentLan(mCurrentLocation.getLongitude());
            driverStatus.setTopic("livetracking");
          //  driverStatus.setDriver_id(loginPrefManager.getDriverID());
            String liveLocData;
            try {
                Gson gson = new Gson();
                Type type = new TypeToken<List<LiveTrackingModelData>>() {}.getType();
                liveLocData = locationPrefs.getString("live_location_array", null);
                Log.e("liveData", new Gson().toJson(liveLocData));
                List<LiveTrackingModelData> trackRecord = gson.fromJson(liveLocData, type);


                if (trackRecord != null && trackRecord.size() > 0)
                    driverStatus.setDriverLiveLog(trackRecord);
                else
                    driverStatus.setDriverLiveLog(Collections.emptyList());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Set manager = loginPrefManager.getAdminList();
            List<String> mainList = new ArrayList<String>(manager);
            driverStatus.setAdminArray(mainList);

            String formattedAddress = AppUtils.getCompleteAddressString(this, mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            driverStatus.setAddress(formattedAddress);
            driverStatus.setBatteryStatus(AppUtils.getBatteryPercentage(getBaseContext()));
            driverStatus.setTaskIds(loginPrefManager.getTaskIds());


            //Log.e("driver_distance",driverStatus.getDistance());

            Log.e("idlescreen_livelatlong",mCurrentLocation.getLatitude()+",,"+mCurrentLocation.getLongitude());

            /*String location = loginPrefManager.getStringValue("live_latlong");

            if (location==null||location.isEmpty()){
                loginPrefManager.setStringValue("live_latlong",mCurrentLocation.getLatitude()+","+mCurrentLocation.getLongitude());
            }else{
                if (!location.contains(mCurrentLocation.getLatitude()+","+mCurrentLocation.getLongitude())) {
                    location = location + ":" + mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLatitude();
                    loginPrefManager.setStringValue("live_latlong", location);
                }
            }*/

            //Log.e("collected_locations",loginPrefManager.getStringValue("live_latlong"));


            ApiService apiService = APIServiceFactory.getRetrofit().create(ApiService.class);
            apiService.updateDriverLivetrack(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken(), driverStatus).enqueue(new Callback<DriverStatusUpdate>() {
                @Override
                public void onResponse(@NonNull Call<DriverStatusUpdate> call, @NonNull Response<DriverStatusUpdate> response) {
                    try {
                        if (response.raw().code() == 200) {
                            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                            editor.putString("live_location_array", null);
                            editor.apply();
                        } else if (response.raw().code() == 401) {
                            try {
                                if (AppHelper.getPool().getCurrentUser() != null) {
                                    findCurrentToken();
                                } else {
                                    getCurrentDate();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DriverStatusUpdate> call, @NotNull Throwable t) {
                    Timber.tag(TAG).e("onFailure%s", t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkCurrentDate() throws ParseException {
        Timber.tag(TAG).e("current_date%s", loginPrefManager.getStringValue("current_date"));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String old_date = loginPrefManager.getStringValue("current_date");
        Date date1 = formatter.parse(old_date);
//        String date = formatter.format(Date.parse(String.valueOf(new Date())));
        String date = formatter.format(new Date());
        Date date2 = formatter.parse(date);
        return date1.equals(date2);
    }

    private void getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat current_date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String formattedDate = df.format(c);
        loginPrefManager.setStringValue("current_date", current_date.format(c));
        loginPrefManager.setStringValue("analytic_date", formattedDate);
        if (driver_idle_timer != null)
            driver_idle_timer.start();
    }

    private void getTimerDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        String formattedDate = df.format(c);
        convert_date(formattedDate, loginPrefManager.getStringValue("analytic_date"));
    }

    private void convert_date(String date, String enddate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        try {
            start_date = format.parse(date);
            end_date = format.parse(enddate);

            printDifference(end_date, start_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void printDifference(Date startDate, Date endDate) {
        System.out.println(startDate);
        System.out.println(endDate);
        long different = endDate.getTime() - startDate.getTime();
        System.out.println(different);
        minutes = TimeUnit.MILLISECONDS.toMinutes(different);
        System.out.println(minutes);
        if (loginPrefManager.isIdleTimerEnabled()) {
            updateIdleTime();
        } else {
            resetIdleData();
            getCurrentDate();
        }
    }

    private SharedPreferences locationPrefs;

    private void updateIdleTime() {
        try {
            AnalyticsData analyticsData = new AnalyticsData();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            analyticsData.setDate(sdf.format(new Date()));
            analyticsData.setIdleTime(Double.parseDouble(String.valueOf(minutes)));
            analyticsData.setBatteryState(AppUtils.getBatteryPercentage(getBaseContext()));
            analyticsData.setCurrentVersion(loginPrefManager.getCurrentVersionName());

            if (loginPrefManager.getStringValue("idle_distance").equalsIgnoreCase("")) {
                loginPrefManager.setStringValue("idle_distance", "0.0");
            }

            float distance = Float.parseFloat(loginPrefManager.getStringValue("idle_distance"));
            Timber.tag(TAG).e("floatdistance1 %s", distance);
            float km = 1000f;
            distance = distance / km;

            double finalDistance = distance;

            Timber.tag(TAG).e("floatdistance2 %s", distance / 1000);
            Timber.tag(TAG).e("floatdistance3 %s", finalDistance);

            analyticsData.setIdleDist(finalDistance);

            String token = loginPrefManager.getCogintoToken();

            if (token.equalsIgnoreCase("")) {
                token = loginPrefManager.getStringValue("idle_token");
            }

            try {
                Gson gson = new Gson();
                Type type = new TypeToken<List<TrackingModelData>>() {
                }.getType();
                List<TrackingModelData> trackRecord = gson.fromJson(locationPrefs.getString("location_array", null), type);
                if (trackRecord != null && trackRecord.size() > 0)
                    analyticsData.setDriverIdleLog(trackRecord);
                else
                    analyticsData.setDriverIdleLog(Collections.emptyList());

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                ApiService apiService = APIServiceFactory.getRetrofit().create(ApiService.class);
                apiService.sendIdleDetails(token, loginPrefManager.getDeviceToken(), analyticsData).enqueue(new Callback<AnalyticsApi>() {
                    @Override
                    public void onResponse(@NonNull Call<AnalyticsApi> call, @NonNull Response<AnalyticsApi> response) {
                        try {
                            if (response.raw().code() == 200) {
                                resetIdleData();
                            } else if (response.raw().code() == 401) {
                                try {
                                    if (AppHelper.getPool().getCurrentUser() != null) {
                                        findCurrentToken();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            getCurrentDate();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<AnalyticsApi> call, @NonNull Throwable t) {
                        try {
                            Timber.tag(TAG).e("onFailure%s", t.getMessage());
                            getCurrentDate();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            getCurrentDate();
            Timber.e("exception%s", e.getMessage());
        }
    }

    private void resetIdleData() {
        loginPrefManager.setStringValue("idle_distance", "");
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("location_array", null);
        editor.apply();
    }

    public class LocalBinder extends Binder {
        IdleUpdatedService getseService() {
            return IdleUpdatedService.this;
        }
    }

    @Override
    public void onDestroy() {
        if (driver_change_status_timer != null) {
            driver_change_status_timer.cancel();
            //driver_change_status_timer = null;
        }

        if (driver_idle_timer != null) {
            driver_idle_timer.cancel();
            driver_idle_timer = null;
        }

        if (scheduledThreadPool != null)
            scheduledThreadPool.shutdown();
        loginPrefManager.setStringValue("stop_service", "0");
        try {
            getTimerDate();
            if (loginPrefManager.getCogintoToken() != null) {
                Intent i = new Intent("willneverkillme");
                i.setClass(this, SensorRestarterBroadcastReceiver.class);
                this.sendBroadcast(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        stopService();
        super.onDestroy();
    }

    private void stopService() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(NOTIFICATION_ID);
                stopForeground(true);
                stopSelf();
            } else {
                stopSelf();
            }
            unregisterForLocationUpdates();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        // workaround for kitkat: set an alarm service to trigger service again
        Intent intent = new Intent(getApplicationContext(), SensorRestarterBroadcastReceiver.class);
        intent.setAction("Task removed and setting alarm now");

        PendingIntent pendingIntent = null;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){

            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        }else{
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }


       // PendingIntent pendingIntent = PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, pendingIntent);
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Timber.i("onLowMemory()");
    }

//    private void updateDriverStatus1(String distances) {
//        try {
//            driverStatus = new DriverStatus();
//            driverStatus.setDistance(distances);
//            driverStatus.setTimezone(TimeZone.getDefault().getID());
//            int onlineStatus = Integer.parseInt(loginPrefManager.getStringValue("driver_status"));
//            driverStatus.setDriverStatus(onlineStatus);
//
//            if (onlineStatus == DeliforceConstants.DRIVER_STATUS_IDLE || onlineStatus == DeliforceConstants.DRIVER_STATUS_OFFLINE) {
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
//                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
//                driverStatus.setOfflineDate(sdf.format(new Date()));
//            }
//
//            int batteryPercent = AppUtils.getBatteryPercentage(this);
//            driverStatus.setBatteryStatus(batteryPercent);
//
//            String formattedAddress;
//            if (mCurrentLocation != null && mCurrentLocation.getLatitude() != 0 && mCurrentLocation.getLatitude() != 0.0) {
//                driverStatus.setCurrentLat(mCurrentLocation.getLatitude());
//                driverStatus.setCurrentLan(mCurrentLocation.getLongitude());
//                formattedAddress = AppUtils.getCompleteAddressString(this, mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
//            } else {
//                Timber.tag(TAG).v("using saved location: " + loginPrefManager.getDoubleLatitude() + ", " + loginPrefManager.getDoubleLongitude());
//                driverStatus.setCurrentLat(loginPrefManager.getDoubleLatitude());
//                driverStatus.setCurrentLan(loginPrefManager.getDoubleLongitude());
//                formattedAddress = AppUtils.getCompleteAddressString(this, loginPrefManager.getDoubleLatitude(), loginPrefManager.getDoubleLongitude());
//            }
//
//            driverStatus.setCurrentVersion(loginPrefManager.getCurrentVersionName());
//
//            driverStatus.setAddress(formattedAddress);
//
//            driverStatus.setTopic("driver");
//            if (loginPrefManager.getDriverID().equals(""))
//                onDestroy();
//            else
//                driverStatus.setDriver_id(loginPrefManager.getDriverID());
//
//            Set manager = loginPrefManager.getAdminList();
//            List<String> mainList = new ArrayList<String>(manager);
//            driverStatus.setAdminArray(mainList);
//
//            ApiService apiService = APIServiceFactory.getRetrofit().create(ApiService.class);
//
//            apiService.updateDriverStatus(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken(), driverStatus).enqueue(new Callback<DriverStatusUpdate>() {
//                @Override
//                public void onResponse(@NonNull Call<DriverStatusUpdate> call, @NonNull Response<DriverStatusUpdate> response) {
//                    if (response.raw().code() == 200) {
//                        System.out.println("driver status updated");
//                    } else if (response.raw().code() == 401) {
//                        try {
//                            if (AppHelper.getPool().getCurrentUser() != null) {
//                                findCurrentToken();
//                            }
//                        } catch (Exception e) {
//                            Timber.e("exceptionLocation%s", e.getMessage());
//                        }
//                    }
//                    driver_change_status_timer.start();
//                    driver_travelld_distance = 0;
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<DriverStatusUpdate> call, @NotNull Throwable t) {
//                    driver_change_status_timer.start();
//                }
//            });
//
//        } catch (Exception e) {
//            driver_change_status_timer.start();
//            Timber.e("Exception%s", e.getMessage());
//        }
//    }

//    private void updateDriverStatus() {
//        try {
//            driverStatus = new DriverStatus();
//            driverStatus.setTimezone(TimeZone.getDefault().getID());
//            int onlineStatus = Integer.parseInt(loginPrefManager.getStringValue("driver_status"));
//            driverStatus.setDriverStatus(onlineStatus);
//
//            if (onlineStatus == DeliforceConstants.DRIVER_STATUS_IDLE || onlineStatus == DeliforceConstants.DRIVER_STATUS_OFFLINE) {
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
//                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
//                driverStatus.setOfflineDate(sdf.format(new Date()));
//            }
//
//            int batteryPercent = AppUtils.getBatteryPercentage(this);
//            driverStatus.setBatteryStatus(batteryPercent);
//
//            String formattedAddress;
//            if (mCurrentLocation != null && mCurrentLocation.getLatitude() != 0 && mCurrentLocation.getLatitude() != 0.0) {
//                driverStatus.setCurrentLat(mCurrentLocation.getLatitude());
//                driverStatus.setCurrentLan(mCurrentLocation.getLongitude());
//                formattedAddress = AppUtils.getCompleteAddressString(this, mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
//            } else {
//                Timber.tag(TAG).v("using saved location: " + loginPrefManager.getDoubleLatitude() + ", " + loginPrefManager.getDoubleLongitude());
//                driverStatus.setCurrentLat(loginPrefManager.getDoubleLatitude());
//                driverStatus.setCurrentLan(loginPrefManager.getDoubleLongitude());
//                formattedAddress = AppUtils.getCompleteAddressString(this, loginPrefManager.getDoubleLatitude(), loginPrefManager.getDoubleLongitude());
//            }
//
//            driverStatus.setCurrentVersion(loginPrefManager.getCurrentVersionName());
//
//            driverStatus.setAddress(formattedAddress);
//
//            driverStatus.setTopic("driver");
//            if (loginPrefManager.getDriverID().equals(""))
//                onDestroy();
//            else
//                driverStatus.setDriver_id(loginPrefManager.getDriverID());
//
//            Set manager = loginPrefManager.getAdminList();
//            List<String> mainList = new ArrayList<String>(manager);
//            driverStatus.setAdminArray(mainList);
//
//            ApiService apiService = APIServiceFactory.getRetrofit().create(ApiService.class);
//
//            apiService.updateDriverStatus(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken(), driverStatus).enqueue(new Callback<DriverStatusUpdate>() {
//                @Override
//                public void onResponse(@NonNull Call<DriverStatusUpdate> call, @NonNull Response<DriverStatusUpdate> response) {
//                    if (response.raw().code() == 200) {
//                        System.out.println("driver status updated");
//                    } else if (response.raw().code() == 401) {
//                        try {
//                            if (AppHelper.getPool().getCurrentUser() != null) {
//                                findCurrentToken();
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    driver_change_status_timer.start();
//                    driver_travelld_distance = 0;
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<DriverStatusUpdate> call, @NotNull Throwable t) {
//                    driver_change_status_timer.start();
//                }
//            });
//
//        } catch (Exception e) {
//            driver_change_status_timer.start();
//            Timber.e("Exception%s", e.getMessage());
//        }
//    }

    String username = "";
    private String password;

    private void findCurrentToken() {
        CognitoUser user = AppHelper.getPool().getCurrentUser();
        username = user.getUserId();
        Timber.tag("token username").e(username);
        if (username != null) {
            AppHelper.setUser(username);
            user.getSessionInBackground(authenticationHandler);
        }
    }

    AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
        @Override
        public void onSuccess(CognitoUserSession cognitoUserSession, CognitoDevice device) {
            Timber.tag(TAG).d(" -- Auth Success");
            //AppHelper.setCurrSession(cognitoUserSession);
            //AppHelper.newDevice(device);
            CognitoAccessToken awsAccessToken = cognitoUserSession.getAccessToken();
            String cognitoToken = awsAccessToken.getJWTToken();
            CognitoIdToken cognitoIdToken = cognitoUserSession.getIdToken();
            String awsIdToken = cognitoIdToken.getJWTToken();
            Timber.tag(TAG).e("AWS Id Token--> %s", awsIdToken);
            CognitoRefreshToken cognitoRefreshToken = cognitoUserSession.getRefreshToken();
            String awsRefreshToken = cognitoRefreshToken.getToken();
            loginPrefManager.setAccessToken("" + cognitoToken);
            loginPrefManager.setCogintoToken("" + awsIdToken);
            loginPrefManager.setRefreshTokenToken("" + awsRefreshToken);
        }

        @Override
        public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String username) {
            Locale.setDefault(Locale.US);
            getUserAuthentication(authenticationContinuation, username);
        }

        @Override
        public void getMFACode(MultiFactorAuthenticationContinuation multiFactorAuthenticationContinuation) {
            mfaAuth(multiFactorAuthenticationContinuation);
        }

        @Override
        public void onFailure(Exception e) {
        }

        @Override
        public void authenticationChallenge(ChallengeContinuation continuation) {
        }
    };

    private void mfaAuth(MultiFactorAuthenticationContinuation continuation) {
        Intent mfaActivity = new Intent(this, MFAActivity.class);
        mfaActivity.putExtra("mode", continuation.getParameters().getDeliveryMedium());
    }

    private void getUserAuthentication(AuthenticationContinuation continuation, String username) {
        if (username != null) {
            this.username = username;
            AppHelper.setUser(username);
        }
        if (this.password == null) {
            password = loginPrefManager.getStringValue("driverPassword");
        }
        AuthenticationDetails authenticationDetails = new AuthenticationDetails(this.username, password, null);
        continuation.setAuthenticationDetails(authenticationDetails);
        continuation.continueTask();
    }

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
    public static final String MY_PREFS_NAME = "LOCATION_ARRAY";

    private void createALiveLog(Location location, float distance, float speed) {
        try {
            JSONObject obj = new JSONObject();
            JSONArray liveLocationArray = new JSONArray();
            obj.put("live_latitude", location.getLatitude());
            obj.put("live_longitude", location.getLongitude());
            obj.put("live_accuracy", location.getAccuracy());
            obj.put("live_speed", speed);
            obj.put("live_date_time", simpleDateFormat.format(new Date()));
            obj.put("live_distance_travelled", distance);

           String existingDistance = loginPrefManager.getStringValue("manual_distance");


           if (existingDistance==null||existingDistance.isEmpty()||Float.parseFloat(existingDistance)==0||Float.parseFloat(existingDistance)==0.0){

               loginPrefManager.setStringValue("manual_distance",String.valueOf(distance));
           }else {
               Double dis = Double.parseDouble(existingDistance)+distance;
               loginPrefManager.setStringValue("manual_distance",String.valueOf(dis));
              // Toast.makeText(this,"Live Location : "+String.valueOf(dis),Toast.LENGTH_LONG).show();
           }

            String live_location_array = locationPrefs.getString("live_location_array", null);
            if (live_location_array != null)
                liveLocationArray = new JSONArray(live_location_array);

            liveLocationArray.put(obj);

            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("live_location_array", liveLocationArray.toString());
            editor.apply();

            Timber.d(liveLocationArray.toString());
            Timber.d("Live record %s", liveLocationArray.length());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;


       /* double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));*/

        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));



        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return dist;
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private void createAIdleLog(Location location, float distance, float speed) {
        try {
            JSONObject obj = new JSONObject();
            JSONArray locationJsonArray = new JSONArray();
            obj.put("idle_latitude", location.getLatitude());
            obj.put("idle_longitude", location.getLongitude());
            obj.put("idle_accuracy", location.getAccuracy());
            obj.put("idle_speed", speed);
            obj.put("idle_date_time", simpleDateFormat.format(new Date()));
            obj.put("idle_distance_travelled", distance);

            String location_array = locationPrefs.getString("location_array", null);
            if (location_array != null) {
                locationJsonArray = new JSONArray(location_array);
            }

            locationJsonArray.put(obj);

            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("location_array", locationJsonArray.toString());
            editor.apply();

            if (mLastIdleLocation==null){
                mLastIdleLocation=location;
               // updateDriverStatus1(String.valueOf(distance));
            }else{
               /* Double latitude1 = mLastIdleLocation.getLatitude();
                Double longitude1 = mLastIdleLocation.getLongitude();
                Double latitude2 = location.getLatitude();
                Double longitude2 = location.getLongitude();*/
                //double distance1 =  getDistance(latitude1, longitude1,latitude2, longitude2);
                mLastIdleLocation=location;
               // updateDriverStatus1(String.valueOf(distance));
            }
            Timber.d(locationJsonArray.toString());
            Timber.d("Idle record %s", locationJsonArray.length());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean isRunning = false;

    class LiveTrackingDriver implements Runnable {

        @Override
        public void run() {
            try {
                isRunning = true;
                Timber.tag(TAG).e("Live tracking part");
                if (loginPrefManager.getStringValue("driver_status").equals("2")) {//busy
                    System.out.println("Live track part:" + mCurrentLocation + ", " + liveLastLocation);
                    if (mCurrentLocation != null && liveLastLocation != null) {
//                        if (mCurrentLocation.getAccuracy() < 100)
                        getDistance(mCurrentLocation, liveLastLocation);
                    } else if (liveLastLocation == null) {
                        liveLastLocation = mCurrentLocation;
                        Timber.tag(TAG).e("live last location is null and updated now");
                    }
                } else {
                 //   Log.e("latlong_idle",String.valueOf(mCurrentLocation.getLatitude()));
                    Timber.tag(TAG).v("Restarting the Live status countdown " + LOCATION_SERVICE_TIME);
                }
            } catch (Exception e) {
                e.printStackTrace();
                isRunning = false;
            }
        }
    }

    private float liveDistance = 0f;

    private void getDistance(Location currentLocation, Location lastLocation) {
        float[] results = new float[1];
        Timber.tag(TAG).d(currentLocation.getLatitude() + ", " + currentLocation.getLongitude() + ", " + lastLocation.getLatitude() + ", " + lastLocation.getLongitude());
        Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(), lastLocation.getLatitude(), lastLocation.getLongitude(), results);
        liveDistance += results[0];//in meters


        int admin_min_distance = loginPrefManager.getIntValue("getLiveTrackingDistanceUpdate");

        if (admin_min_distance < 0) {
            admin_min_distance = 20;// if no value from admin we are setting default minimum distance in meters
        }

        if (liveDistance >= admin_min_distance) {
            updateDriverLiveStatus();
            liveDistance = 0;
        }

        liveLastLocation = currentLocation;
    }
    private float getDistance(double startLat, double startLng, double endLat, double endLng) {
        float[] results = new float[1];
        Location.distanceBetween(startLat, startLng, endLat, endLng, results);
        float distance = results[0]; // return in meters

        double current_distance;
        String idle_Saved_Distance = loginPrefManager.getStringValue("idle_distance");
        if (!idle_Saved_Distance.equalsIgnoreCase("")) {
            current_distance = distance + Double.parseDouble(idle_Saved_Distance);
        } else {
            current_distance = distance;
        }

        if (BuildConfig.DEBUG) {
            Intent intent = new Intent(ACTION_BROADCAST);
            intent.putExtra(EXTRA_LOCATION, distance);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        }

        if (loginPrefManager.getStringValue("driver_status").equals("1")) {
            loginPrefManager.setStringValue("idle_distance", "" + current_distance);
        } else if (loginPrefManager.getStringValue("driver_status").equals("2")) {
            float total_LiveDistance = loginPrefManager.getTotalLiveDistance();
            total_LiveDistance += distance;//in meters
            loginPrefManager.setTotalLiveDistance(total_LiveDistance);
        }
        return distance;
    }

}