package com.app.deliforcemanager.BaseActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.app.deliforcemanager.Activities.AnalyticsActivity;
import com.app.deliforcemanager.Activities.ChangePasswordActivity;
import com.app.deliforcemanager.Activities.EarningsChartActivity;
import com.app.deliforcemanager.Activities.LoginActivity;
import com.app.deliforcemanager.Activities.NavigationActivity;
import com.app.deliforcemanager.Activities.ProfileActivity;
import com.app.deliforcemanager.Activities.RouteActivity;
import com.app.deliforcemanager.Activities.SettingsActivity;
import com.app.deliforcemanager.Adapter.NavigationItemAdapter;
import com.app.deliforcemanager.BuildConfig;
import com.app.deliforcemanager.Cognito.AppHelper;
import com.app.deliforcemanager.CustomDialogView.AlertDialog;
import com.app.deliforcemanager.CustomDialogView.LoginAlertDailog;
import com.app.deliforcemanager.CustomDialogView.UpdateAppDialog;
import com.app.deliforcemanager.ModelClass.DriverStatusApi.DriverStatusUpdate;
import com.app.deliforcemanager.ModelClass.GetProfile.Categories;
import com.app.deliforcemanager.ModelClass.GetProfile.GetProfileApi;
import com.app.deliforcemanager.ModelClass.ManagerProfile.ProfileResponse;
import com.app.deliforcemanager.ModelClass.SettingsApi.NavigationTypes;
import com.app.deliforcemanager.R;
import com.app.deliforcemanager.RawHeaders.DriverStatusInfo.DriverStatus;
import com.app.deliforcemanager.Services.IdleUpdatedService;
import com.app.deliforcemanager.Services.PushNotificationService;
import com.app.deliforcemanager.Utils.AppUtils;
import com.app.deliforcemanager.Utils.DeliforceConstants;
import com.app.deliforcemanager.Utils.DrawerItem;
import com.app.deliforcemanager.Utils.LoginPrefManager;
import com.app.deliforcemanager.glypmseWrapper.EnRouteWrapper;
/*import com.applozic.mobicomkit.Applozic;
import com.applozic.mobicomkit.api.account.register.RegistrationResponse;
import com.applozic.mobicomkit.api.account.user.MobiComUserPreference;
import com.applozic.mobicomkit.api.account.user.User;
import com.applozic.mobicomkit.api.account.user.UserService;
import com.applozic.mobicomkit.contact.AppContactService;
import com.applozic.mobicomkit.listners.AlCallback;
import com.applozic.mobicomkit.listners.AlLoginHandler;
import com.applozic.mobicomkit.listners.AlLogoutHandler;
import com.applozic.mobicomkit.listners.AlPushNotificationHandler;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;
import com.applozic.mobicommons.people.contact.Contact;*/
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.app.deliforcemanager.Services.IdleService.STANDARD_ACCURACY;


public class BaseDrawerActivity extends BaseActivity implements NavigationItemAdapter.ClickEventInterface {
    public static final String TAG = "BaseDrawerActivity";
    UpdateAppDialog updateAppDialog;
    ArrayList<NavigationTypes> navigationTypes;
    AlertDialog alertDialog;
    //public static Bitmap product_image;
    public CircularImageView user_profile;
    protected DrawerLayout mdrawerLayout;
    RecyclerView rvItem;
    TypedArray taIcon;
    DriverStatus driverStatus;
    double current_longitude, current_latitude;
    Snackbar snackbar;
    String username = "";
    LoginAlertDailog loginAlertDailog;
    ImageView img_btn_close;
    NotificationManager notificationManager;
    ActivityManager manager;
    OnLocationUpdatedListener onLocationUpdatedListener = new OnLocationUpdatedListener() {
        @Override
        public void onLocationUpdated(Location location) {
            Timber.i("onLocationUpdated BaseDrawer: " + location.getProvider() + ", Accuracy " + location.getAccuracy() +
                    ", LatLng: " + location.getLatitude() + "," + location.getLongitude() + ", isMock: " + location.isFromMockProvider());
            if (location.getAccuracy() < STANDARD_ACCURACY) {
                current_longitude = location.getLongitude();
                current_latitude = location.getLatitude();
                current_accuraccy = location.getAccuracy();
                AppUtils.setDeviceLocation(location, BaseDrawerActivity.class.getSimpleName());
            }
        }
    };
    private TextView tvMenuName, tv_duty_status;
    private String[] taItemText;
    //GPSTracker gpsTracker;
    private Switch status_switch;
    private CognitoUser user;
    //long back_pressed;
    private ActivityManager activityManager;
    private String password;
    AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
        @Override
        public void onSuccess(CognitoUserSession cognitoUserSession, CognitoDevice device) {
            /*AppHelper.setCurrSession(cognitoUserSession);
            AppHelper.newDevice(device);*/
            closeWaitDialog();

            CognitoAccessToken awsAccessToken = cognitoUserSession.getAccessToken();
            String cognitoToken = awsAccessToken.getJWTToken();
            CognitoIdToken cognitoIdToken = cognitoUserSession.getIdToken();
            String awsIdToken = cognitoIdToken.getJWTToken();
            CognitoRefreshToken cognitoRefreshToken = cognitoUserSession.getRefreshToken();
            String awsRefreshToken = cognitoRefreshToken.getToken();
            loginPrefManager.setAccessToken("" + cognitoToken);
            loginPrefManager.setCogintoToken("" + awsIdToken);
            loginPrefManager.setRefreshTokenToken("" + awsRefreshToken);

            check_current_activity();
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String username) {
            closeWaitDialog();
            Locale.setDefault(Locale.US);
            getUserAuthentication(authenticationContinuation, username);
        }

        @Override
        public void getMFACode(MultiFactorAuthenticationContinuation multiFactorAuthenticationContinuation) {
            closeWaitDialog();
            mfaAuth(multiFactorAuthenticationContinuation);
        }

        @Override
        public void onFailure(Exception e) {
            try {
                closeWaitDialog();
                if (!((Activity) getApplicationContext()).isFinishing())
                    showDialogMessage(AppHelper.formatException(e));
            } catch (Exception es) {
                es.printStackTrace();
            }
        }

        @Override
        public void authenticationChallenge(ChallengeContinuation continuation) {
        }
    };
    //ExpireInterface expireInterface;
    private BroadcastReceiver Serverreceiver;

    public static int getNavBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentViewWithoutInject(R.layout.activity_drawer_view);
        ViewGroup viewGroup = findViewById(R.id.content_frame);
        LayoutInflater.from(this).inflate(layoutResID, viewGroup, true);
        bindViews();
        initView();

        getDriverDetails();

        //location library
        SmartLocation.with(BaseDrawerActivity.this).location().start(onLocationUpdatedListener);

        AppHelper.init(getApplicationContext());

        manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        //gpsTracker = new GPSTracker(BaseDrawerActivity.this);
        initBroadCast();
        initServiceBroadcast();
        activityManager = (ActivityManager) getApplication().getSystemService(Activity.ACTIVITY_SERVICE);
        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
    }

   /* private void loginUser(ArrayList<GetProfileApi.AppLogicContactList> appLozicContactList) {
        User user = new User();
        user.setUserId(loginPrefManager.getDriverID()); //userId it can be any unique user identifier NOTE : +,*,? are not allowed chars in userId.
        user.setDisplayName(loginPrefManager.getStringValue("first_name"));
        user.setEmail(loginPrefManager.getEmail());
        user.setAuthenticationTypeId(User.AuthenticationType.APPLOZIC.getValue());  //User.AuthenticationType.APPLOZIC.getValue() for password verification from Applozic server and User.AuthenticationType.CLIENT.getValue() for access Token verification from your server set access token as password
        user.setPassword(""); //optional, leave it blank for testing purpose, read this if you want to add additional security by verifying password from your server https://www.applozic.com/docs/configuration.html#access-token-url
        user.setImageLink("");//optional, set your image link if you have

        Applozic.connectUser(getApplicationContext(), user, new AlLoginHandler() {
            @Override
            public void onSuccess(RegistrationResponse registrationResponse, Context context) {
                updateToken(appLozicContactList);

                *//*String  response =   UserService.getInstance(getApplicationContext())
                        .processUpdateUserPassword("asma@123",loginPrefManager.getDriverID());

                if(!TextUtils.isEmpty(response) && MobiComKitConstants.SUCCESS.equals(response)){
                    //Password updated successfuly
                }else{
                    //Password update failed
                }*//*
            }

            @Override
            public void onFailure(RegistrationResponse registrationResponse, Exception exception) {

            }
        });
    }*/

    private void updateToken(ArrayList<GetProfileApi.AppLogicContactList> appLozicContactList) {

       /* Applozic.getInstance(this).setDeviceRegistrationId(loginPrefManager.getStringValue("device_token"));
        if (MobiComUserPreference.getInstance(getApplicationContext()).isRegistered()) {
            Applozic.registerForPushNotification(getApplicationContext(),
                    Applozic.getInstance(this).getDeviceRegistrationId(), new AlPushNotificationHandler() {
                        @Override
                        public void onSuccess(RegistrationResponse registrationResponse) {

                        }

                        @Override
                        public void onFailure(RegistrationResponse registrationResponse, Exception exception) {

                        }
                    });

            addContacts(appLozicContactList);
        }*/
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("oncreate", "oncreateCalled");


    }

    public void getDriverDetails() {
        try {
          //  show_loader();
            /*double version = 1;
            try {
                PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                version = Double.parseDouble(pInfo.versionName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }*/
            Timber.tag("base_profile").e("profile");

            apiService.getManagerProfileDetails(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken()).enqueue(new Callback<ProfileResponse>() {
                @Override
                public void onResponse(@NonNull Call<ProfileResponse> call, @NonNull Response<ProfileResponse> response) {
                   // dismiss_loader();

                    try {
                        if (response.raw().code() == 200) {
                          //  fetchProfileDetails(loginPrefManager, response.body());


                            loginPrefManager.setCategoryValue(new Gson().toJson(response.body().getCategoriesList()));

                            Log.e("profile_received","true");

                            loginPrefManager.setStringValue("driver_name", "" + response.body().getName());
                            loginPrefManager.setStringValue("first_name", "" + response.body().getName());

                            setAdapter();

                        } else  {
                            findCurrent();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ProfileResponse> call, @NotNull Throwable t) {
                 //   dismiss_loader();
                    Log.e("profile_error",t.getMessage());
                    Timber.tag("onFailure").e(t);
                }
            });

        } catch (Exception e) {
            dismiss_loader();
            Timber.tag("Exception").e(e);
        }
    }

    public void fetchProfileDetails(LoginPrefManager loginPrefManager, GetProfileApi body) {
        if (body != null) {
            loginPrefManager.setTaskImageMaxCount("task_image_upload_limit", body.getTaskImageCount());
            loginPrefManager.setIdleLogEnable("isIdleLogEnable", body.getIdleLogEnable());
            loginPrefManager.setTransitRoadAPI("isTransitRoadApi", body.getTransitRoadApi());
            loginPrefManager.setTaskPinCode("isTaskPinCode", body.getTaskPinCode());
            loginPrefManager.setPODImageCompress("isPODImageCompress", body.isPODImageCompress());
            loginPrefManager.setIsEarningEnabled("isEarning", body.getIsEarningsModule());
            loginPrefManager.setBussinessType("businessType",body.getBussinessType());


            List<Categories>  categoriesList =  new ArrayList<>();


            for (int i  =0 ; i<body.getCategoriesList().size();i++){
                if (body.getBussinessType().equals(body.getCategoriesList().get(i).getId())){
                    categoriesList.add(body.getCategoriesList().get(i));
                }
            }


            for (int i  =0 ; i<body.getCategoriesList().size();i++){
                if (!body.getBussinessType().equals(body.getCategoriesList().get(i).getId())){
                    categoriesList.add(body.getCategoriesList().get(i));
                }
            }


            loginPrefManager.setIsCrmEnabled(body.isCrmEnable());
            loginPrefManager.setAppLogicEnabled(body.isApplozicEnable());
            loginPrefManager.setIsAdditionalStatusEnabled(body.isAdditionalStatus());
            loginPrefManager.setIsFailedOrCancelled(body.isFailedOrCancelled());
            loginPrefManager.setIsActualCustomer(body.isActualCustomer());
            loginPrefManager.setOTPVerificationEnable(body.isOTPVerificationEnable());
            loginPrefManager.setBooleanValue("driver_call_option_need", body.isDriverCallOption());
            loginPrefManager.setDriverMobile("" + body.getPhone());
            loginPrefManager.isAgentChangePassword(body.isAgentChangePassword());
            loginPrefManager.isCaptureLocationEnabled(body.isCaptureLocationEnable());
            loginPrefManager.isIdleTimerEnabled(body.isIdleTimerEnabled());
            loginPrefManager.isWayBillEnabled(body.isWayBillEnabled());
            loginPrefManager.isCustomFailedReason(body.isCustomFailedReason());
            loginPrefManager.setFailedReasons(new Gson().toJson(body.getFailedReason()));


            loginPrefManager.setCategoryValue(new Gson().toJson(categoriesList));



            loginPrefManager.isAdminStatusEnabled(body.isAdminStatusEnabled());
            loginPrefManager.isCustomerNotesEnabled(body.isCustomerNotesEnabled());
            loginPrefManager.isCustomerCategoryEnabled(body.isCategoryConfigEnable());

            loginPrefManager.setDeliveryText(body.getDeliveryText());
            loginPrefManager.setPickupText(body.getPickupText());
            loginPrefManager.setAppointmentText(body.getAppointmentText());
            loginPrefManager.setFieldWorkForceText(body.getFieldWorkForceText());
            loginPrefManager.isMultiStartEnable(body.isMultipleTaskStartEnabled());


            
            Log.e("multi_start",String.valueOf(body.isMultipleTaskStartEnabled()));

            loginPrefManager.setCurrency(body.getCurrency());
            loginPrefManager.setSupportEmail(body.getSupportEmail());
            if (body.getSettings().getLanguage() == Integer.parseInt(DeliforceConstants.LANGUAGE_ENGLISH)) {
                if (!getResources().getConfiguration().locale.toString().equals(DeliforceConstants.LANGUAGE_CODE_ENGLISH)) {
                    setLanguage(DeliforceConstants.LANGUAGE_CODE_ENGLISH);
                    loginPrefManager.setStringValue("Lang", DeliforceConstants.LANGUAGE_CODE_ENGLISH);
                }
            }
            if (body.getSettings().getLanguage() == Integer.parseInt(DeliforceConstants.LANGUAGE_SPANISH)) {
                if (!getResources().getConfiguration().locale.toString().equals(DeliforceConstants.LANGUAGE_CODE_SPANISH)) {
                    setLanguage(DeliforceConstants.LANGUAGE_CODE_SPANISH);
                    loginPrefManager.setStringValue("Lang", DeliforceConstants.LANGUAGE_CODE_SPANISH);
                }
            }
            if (body.getSettings().getLanguage() == Integer.parseInt(DeliforceConstants.LANGUAGE_FRENCH)) {
                if (!getResources().getConfiguration().locale.toString().equals(DeliforceConstants.LANGUAGE_CODE_FRENCH)) {
                    setLanguage(DeliforceConstants.LANGUAGE_CODE_FRENCH);
                    loginPrefManager.setStringValue("Lang", DeliforceConstants.LANGUAGE_CODE_FRENCH);
                }
            }
            if (body.getSettings().getLanguage() == Integer.parseInt(DeliforceConstants.LANGUAGE_PORTUGUESE)) {
                if (!getResources().getConfiguration().locale.toString().equals(DeliforceConstants.LANGUAGE_CODE_PORTUGUESE)) {
                    setLanguage(DeliforceConstants.LANGUAGE_CODE_PORTUGUESE);
                    loginPrefManager.setStringValue("Lang", DeliforceConstants.LANGUAGE_CODE_PORTUGUESE);
                }
            }
            if (body.getSettings().getLanguage() == Integer.parseInt(DeliforceConstants.LANGUAGE_MALAY)) {
                if (!getResources().getConfiguration().locale.toString().equals(DeliforceConstants.LANGUAGE_CODE_MALAY)) {
                    setLanguage(DeliforceConstants.LANGUAGE_CODE_MALAY);
                    loginPrefManager.setStringValue("Lang", DeliforceConstants.LANGUAGE_CODE_MALAY);
                }
            }
            if (body.getSettings().getLanguage() == Integer.parseInt(DeliforceConstants.LANGUAGE_ARABIC)) {
                if (!getResources().getConfiguration().locale.toString().equals(DeliforceConstants.LANGUAGE_CODE_ARABIC)) {
                    setLanguage(DeliforceConstants.LANGUAGE_CODE_ARABIC);
                    loginPrefManager.setStringValue("Lang", DeliforceConstants.LANGUAGE_CODE_ARABIC);
                }
            }

            loginPrefManager.setLangauge(String.valueOf(body.getSettings().getLanguage()));

            loginPrefManager.setIntValue("getLiveTrackingDistanceUpdate", body.getLiveTrackingDistanceUpdate());

            loginPrefManager.setStringValue("driver_map_style", String.valueOf(body.getSettings().getMapStyle()));

            loginPrefManager.setIntValue("idleDistanceUpdate", body.getIdleDistanceUpdate());

            loginPrefManager.setStringValue("image_url", body.getImage());

            String frist_name = body.getName();

            frist_name = frist_name.substring(0, 1).toUpperCase() + frist_name.substring(1);

            String last_name = body.getLastname();

            String driver_name;
            driver_name = frist_name + " " + last_name;





            loginPrefManager.setEmail(body.getEmail());

            if (body.getMqttCount() != null) {
                loginPrefManager.setStringValue("mqtt_time", String.valueOf(body.getMqttCount()));
            }
            if (body.getMqttCount() == null) {
                loginPrefManager.setStringValue("mqtt_time", "15");
            }

            loginPrefManager.setStringValue("idle_location_time", String.valueOf(body.getIdleUpdateCount()));

            loginPrefManager.setStringValue("idle_distance_update_frequency", String.valueOf(body.getIdleDistanceUpdateFrequency()));
            loginPrefManager.setIntValue(DeliforceConstants.PREF_SETTINGS_VIBRATE_LONG, body.getSettings().getVibration());

            /*To calculate the idle time when driver is online state*/
//            if (body.getDriverStatus() != DeliforceConstants.DRIVER_STATUS_OFFLINE) {
////                if (!isMyServiceRunning(IdleUpdatedService.class)) {
////                    startService(new Intent(BaseDrawerActivity.this, IdleUpdatedService.class));
////                }
//            }
            double store_version;
            boolean isAndroidVerUpdate;
            if(BuildConfig.FLAVOR.equals("xquaraAgent")) {
                isAndroidVerUpdate = body.isXquaraAndroidVerUpdate();
                store_version = body.getXquaraAndroidVer();
            } else {
                isAndroidVerUpdate = body.isAndroidVerUpdate();
                store_version = body.getAndroidVer();
            }

            if (store_version > getAppVersion()) {
                updateAppDialog = new UpdateAppDialog(BaseDrawerActivity.this, R.style.MyDialogStyle, isAndroidVerUpdate);

                if (!loginPrefManager.getSkipversion()) {
                    updateAppDialog.show();
                    updateAppDialog.setCanceledOnTouchOutside(false);
                }
            }

           // String driver_status = String.valueOf(body.getDriverStatus());


//            if (driver_status.equalsIgnoreCase("4")) {
//                loginPrefManager.setDriverBlock(driver_status);
//                showAlert();
//                return;
//            }
//
//            loginPrefManager.setStringValue("driver_status", driver_status);

            try {
                AppUtils.setDriverStatusType(body.getStatusList());
                loginPrefManager.isNavigationTypeEnabled(body.isNavigationTypeEnabled());
                if (body.isNavigationTypeEnabled()) {
                    navigationTypes = body.getSettings().getNavigationTypes();
                    updateNavigation(navigationTypes);
                } else {
                    loginPrefManager.setNavigation(DeliforceConstants.NavigationGoogleMap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateNavigation(ArrayList<NavigationTypes> navigationTypes) {
        if (navigationTypes != null) {
            for (int i = 0; i < navigationTypes.size(); i++) {
                if (navigationTypes.get(i).getIsDefault() == 1) {
                    loginPrefManager.setNavigation(navigationTypes.get(i).getMap());
                    break;
                }
            }
        } else {
            loginPrefManager.setNavigation(DeliforceConstants.NavigationGoogleMap);
        }
    }

    private void showAlert() {
        alertDialog = new AlertDialog(BaseDrawerActivity.this, R.style.MyDialogStyle, 100, new com.app.deliforcemanager.CustomDialogView.AlertDialog.BlockedInterface() {
            @Override
            public void onclick(boolean status) {
                logoutonCall();
                alertDialog.dismiss();
            }

            @Override
            public void reDirectToSpecifPage(String pageDeterminationValue) {

            }
        });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void connectToAppLogic(ArrayList<GetProfileApi.AppLogicContactList> appLozicContactList) {
      /*  try {
            if (Applozic.isConnected(getApplicationContext())) {
                updateUser(appLozicContactList);
            } else {
                loginUser(appLozicContactList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

   /* private void updateUser(ArrayList<GetProfileApi.AppLogicContactList> appLozicContactList) {
        User user = new User();
        user.setDisplayName(loginPrefManager.getStringValue("first_name"));
        user.setEmail(loginPrefManager.getEmail());
        UserService.getInstance(this).updateUser(user, new AlCallback() {
            @Override
            public void onSuccess(Object response) {
                Log.i("User", "Update success ");
            }

            @Override
            public void onError(Object error) {
                Log.i("User", "Update failed ");
            }
        });
        updateToken(appLozicContactList);
    }*/

    /*@SuppressLint("ClickableViewAccessibility")
    private void onSwitchTouch() {
        status_switch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (loginPrefManager.getStringValue("driver_status").equalsIgnoreCase("2")) {
                        closeDrawerLayout();
                        show_ride_exist(getString(R.string.task_progress));
                    }
                }
                return true;
            }
        });
    }*/

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        if (toolbar != null) {
            if (getToolbar() != null) {
                getToolbar().setNavigationOnClickListener(v -> mdrawerLayout.openDrawer(GravityCompat.START));

            }
        }
    }

    private void initView() {
        mdrawerLayout = findViewById(R.id.drawerLayout);
        user_profile = findViewById(R.id.profile_pic);
        tvMenuName = findViewById(R.id.tv_agent_name);
        rvItem = findViewById(R.id.recyclerItem);

        tv_duty_status = findViewById(R.id.tv_duty_status);
        status_switch = findViewById(R.id.sw_driver_duty_status);
        img_btn_close = findViewById(R.id.img_btn_close);
//        tvMenuName.setText(loginPrefManager.getStringValue("driver_name"));

      //  loginPrefManager.setStringValue("driver_name");






        //Log.e("earningenabled", "---------" + loginPrefManager.getEarningEnabled());

        setAdapter();
        NavigationView navigationView = findViewById(R.id.nav_view);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) navigationView.getLayoutParams();
        params.width = metrics.widthPixels;
        navigationView.setLayoutParams(params);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        rvItem.setLayoutManager(gridLayoutManager);
        rvItem.setHasFixedSize(false);


//        try {
//            if (!loginPrefManager.getStringValue("driver_status").equals("")) {
//                //change_status(Integer.parseInt(loginPrefManager.getStringValue("driver_status")));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

       // status_change();
        onClickevetns();

        if (!loginPrefManager.getStringValue("image_url").equals("")) {
            getImageUrl(loginPrefManager.getStringValue("image_url"));
        }
    }

    public void setAdapter() {

//        if (loginPrefManager.getEarningEnabled()) {
//            taItemText = getResources().getStringArray(R.array.array_menu_item);
//            taIcon = getResources().obtainTypedArray(R.array.array_icon_bg);
//        } else {
//            taItemText = getResources().getStringArray(R.array.array_without_earning);
//            taIcon = getResources().obtainTypedArray(R.array.array_icon_without_earning);
//        }
        ArrayList<DrawerItem> drawerItems = prepareData();
     //   taIcon.recycle();
        rvItem.setAdapter(new NavigationItemAdapter(BaseDrawerActivity.this, drawerItems, BaseDrawerActivity.this));


    }

    private void onClickevetns() {
        img_btn_close.setOnClickListener(view -> closeDrawerLayout());
    }

    private ArrayList<DrawerItem> prepareData() {
        ArrayList<DrawerItem> drawerItems = new ArrayList<>();

        DrawerItem dataModel = new DrawerItem();
        dataModel.setTitle(getString(R.string.nav_task));
        dataModel.setIcon(R.drawable.task);
        drawerItems.add(dataModel);

//        dataModel = new DrawerItem();
//        dataModel.setTitle(getString(R.string.nav_route));
//        dataModel.setIcon(R.drawable.route);
//        drawerItems.add(dataModel);

//        if (loginPrefManager.getEarningEnabled()) {
//            dataModel = new DrawerItem();
//            dataModel.setTitle(getString(R.string.nav_earnings));
//            dataModel.setIcon(R.drawable.ic_earnings);
//            drawerItems.add(dataModel);
//        }
//        dataModel = new DrawerItem();
//        dataModel.setTitle(getString(R.string.nav_analytics));
//        dataModel.setIcon(R.drawable.analytics);
//        drawerItems.add(dataModel);

        dataModel = new DrawerItem();
        dataModel.setTitle(getString(R.string.nav_profile));
        dataModel.setIcon(R.drawable.account);
        drawerItems.add(dataModel);
//
//        if (!loginPrefManager.isAgentChangePassword()) {
//            dataModel = new DrawerItem();
//            dataModel.setTitle(getString(R.string.nav_password));
//            dataModel.setIcon(R.drawable.change_pswd);
//            drawerItems.add(dataModel);
//        }

//        if(!BuildConfig.FLAVOR.equals("xquaraAgent")) {
//        dataModel = new DrawerItem();
//        dataModel.setTitle(getString(R.string.nav_settings));
//        dataModel.setIcon(R.drawable.setting);
//        drawerItems.add(dataModel);
//        }

//        if (loginPrefManager.isAppLogicEnabled()) {
//            dataModel = new DrawerItem();
//            dataModel.setTitle(getString(R.string.nav_chat_with_us));
//            dataModel.setIcon(R.drawable.help);
//            drawerItems.add(dataModel);
//        }

//        dataModel = new DrawerItem();
//        dataModel.setTitle(getString(R.string.nav_help));
//        dataModel.setIcon(R.drawable.help);
//        drawerItems.add(dataModel);

        dataModel = new DrawerItem();
        dataModel.setTitle(getString(R.string.nav_logout));
        dataModel.setIcon(R.drawable.logout);
        drawerItems.add(dataModel);

        /*for (int i = 0; i < taItemText.length; i++) {
            DrawerItem dataModel = new DrawerItem();
            dataModel.setTitle(taItemText[i]);
            dataModel.setIcon(taIcon.getResourceId(i, -1));
            drawerItems.add(dataModel);
        }*/
        return drawerItems;
    }

    @Override
    public void selectedItem(String title) {
        selectActivity(title);
    }

    private void selectActivity(String title) {
        if (title.equals(getString(R.string.nav_task))) {
            closeDrawerLayout();
            moveWithDelay(NavigationActivity.class);
        } else if (title.equals(getString(R.string.nav_route))) {
            closeDrawerLayout();
            moveWithDelay(RouteActivity.class);
        } else if (title.equals(getString(R.string.nav_earnings))) {
            closeDrawerLayout();
            moveWithDelay(EarningsChartActivity.class);
        } else if (title.equals(getString(R.string.nav_analytics))) {
            closeDrawerLayout();
            moveWithDelay(AnalyticsActivity.class);
        } else if (title.equals(getString(R.string.nav_profile))) {
            closeDrawerLayout();
            moveWithDelay(ProfileActivity.class);
        } else if (title.equals(getString(R.string.nav_password))) {
            closeDrawerLayout();
            moveWithDelay(ChangePasswordActivity.class);
        } else if (title.equals(getString(R.string.nav_settings))) {
            closeDrawerLayout();
            moveWithDelay(SettingsActivity.class);
        } else if (title.equals(getString(R.string.nav_chat_with_us))) {
            closeDrawerLayout();
            startChat();
        } else if (title.equals(getString(R.string.nav_help))) {
            closeDrawerLayout();
            sendMailToAdmin();
        } else if (title.equals(getString(R.string.nav_logout))) {
            closeDrawerLayout();
//            if (loginPrefManager.getStringValue("driver_status").equalsIgnoreCase("2")) {
//                show_ride_exist(getString(R.string.task_update_logout));
//            } else {
                loginPrefManager.setStringValue("idle_token", loginPrefManager.getCogintoToken());
               // String token = loginPrefManager.getStringValue("idle_token");
                status_logout();
           // }
        }
    }

    private void startChat() {
   /*     Intent intent = new Intent(this, ConversationActivity.class);
        intent.putExtra(ConversationUIService.USER_ID, loginPrefManager.getAppLogicSupportUserId());
        intent.putExtra(ConversationUIService.DISPLAY_NAME, loginPrefManager.getAppLogicSupportUserName()); //put it for displaying the title.
        intent.putExtra(ConversationUIService.TAKE_ORDER, true); //Skip chat list for showing on back press
        startActivity(intent);*/
      //  Intent intent = new Intent(this, ConversationActivity.class);
      //  startActivity(intent);
    }

   /* private void addContacts(ArrayList<GetProfileApi.AppLogicContactList> appLozicContactList) {
        AppContactService appContactService = new AppContactService(getApplicationContext());
        List<Contact> contacts = appContactService.getAll();
        for (int i = 0; i < contacts.size(); i++) {
            appContactService.deleteContact(contacts.get(i));
        }
        if (appLozicContactList != null && !appLozicContactList.isEmpty()) {
            for (int i = 0; i < appLozicContactList.size(); i++) {
                Contact contact = new Contact();
                contact.setUserId(appLozicContactList.get(i).getAppLozicId());
                contact.setFullName(appLozicContactList.get(i).getName());
                appContactService.add(contact);
            }
        }
    }*/


    private void moveWithDelay(Class movingClassName) {
        new Handler().postDelayed(() -> {
            if (!loginPrefManager.getManagerID().isEmpty()) {
                startActivity(new Intent(BaseDrawerActivity.this, movingClassName));
                finish();
            }
        }, 100);
    }

    private void closeDrawerLayout() {
        mdrawerLayout.closeDrawer(GravityCompat.START);
    }

    public void logoutonCall() {
        if (loginPrefManager.getManagerID() != null) {
            loginPrefManager.setMangerID("");

            //stopService(new Intent(this, LiveTrackingService.class));
            loginPrefManager.setStringValue("image", "");
            //loginPrefManager.setStringValue("idle_distance", "");
            loginPrefManager.setRefreshTokenToken("");

            loginPrefManager.setCogintoToken("");
            loginPrefManager.setAccessToken("");
            loginPrefManager.setStringValue("user", "");
            loginPrefManager.setStringValue("filter_values", "");
            loginPrefManager.setFromDate("");
            loginPrefManager.setToDate("");
            loginPrefManager.setStringValue("chk_search_task_sort_by_time", "0");
            loginPrefManager.setStringValue("chk_search_task_sort_by_distance", "0");
            loginPrefManager.setDriverMobile("");
            loginPrefManager.setStringValue("taskId", "");
            checkUser();
        } else {
            dismiss_loader();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e("onResume", "onResume");
       // Log.e("onResumeEarning", "---" + loginPrefManager.getEarningEnabled());
        setAdapter();


        if (loginPrefManager.getStringValue("driver_status").equalsIgnoreCase("2") || loginPrefManager.getStringValue("driver_status").equalsIgnoreCase("1")) {
            status_switch.setChecked(true);
        } else {
            tv_duty_status.setText(getString(R.string.nav_offline));
            tv_duty_status.setTextColor(getResources().getColor(R.color.cancle_btn_color));
        }
    }

    public void change_status(int status) {
        if (status == 2 || status == 1) {
            status_switch.setChecked(true);
            tv_duty_status.setText(getString(R.string.nav_online));
            tv_duty_status.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else {
            status_switch.setChecked(false);
            tv_duty_status.setText(getString(R.string.nav_offline));
            tv_duty_status.setTextColor(getResources().getColor(R.color.cancle_btn_color));
        }
    }

    public void status_logout() {
       // show_loader();
        logoutonCall();
      //  updateDriverStatus(DeliforceConstants.DRIVER_STATUS_OFFLINE, 1);
    }

    private void status_change() {
        status_switch.setOnCheckedChangeListener((compoundButton, status) -> {
            String driver_status = loginPrefManager.getStringValue("driver_status");
            Timber.tag("driver_status_change").e("%s", driver_status);

            if (status_switch.isChecked()) {
                if (driver_status.equals("2")) {
                    status_switch.setChecked(true);
                    show_ride_exist(getString(R.string.task_progress));
                } else {

                    loginPrefManager.setStringValue("driver_status", String.valueOf(DeliforceConstants.DRIVER_STATUS_IDLE));
                    tv_duty_status.setText(getString(R.string.nav_online));
                    tv_duty_status.setTextColor(getResources().getColor(R.color.colorPrimary));
                 //   updateDriverStatus(DeliforceConstants.DRIVER_STATUS_IDLE, 0);
                    LocalBroadcastManager.getInstance(BaseDrawerActivity.this).sendBroadcast(new Intent("service_status").putExtra("server_profile", "101"));
                }
            } else {
                if (driver_status.equals("2")) {
                    status_switch.setChecked(true);
                    show_ride_exist(getString(R.string.task_progress));
                } else {
                    LocalBroadcastManager.getInstance(BaseDrawerActivity.this).sendBroadcast(new Intent("service_status").putExtra("server_profile", "100"));

                    loginPrefManager.setStringValue("driver_status", String.valueOf(DeliforceConstants.DRIVER_STATUS_OFFLINE));
                    tv_duty_status.setText(getString(R.string.nav_offline));
                    tv_duty_status.setTextColor(getResources().getColor(R.color.cancle_btn_color));
                   // updateDriverStatus(DeliforceConstants.DRIVER_STATUS_OFFLINE, 0);


                }

            }

        });
    }

//    private void updateDriverStatus(int position, int type) {
//        try {
//            driverStatus = new DriverStatus();
//            driverStatus.setTimezone(TimeZone.getDefault().getID());
//            driverStatus.setDriverStatus(position);
//            if(position == DeliforceConstants.DRIVER_STATUS_IDLE || position == DeliforceConstants.DRIVER_STATUS_OFFLINE) {
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
//                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
//                driverStatus.setOfflineDate(sdf.format(new Date()));
//            }
//            /*driverStatus.setCurrentLan(gpsTracker.getLongitude());
//            driverStatus.setCurrentLat(gpsTracker.getLatitude());
//
//            if (gpsTracker.getLatitude() == 0.0) {
//                driverStatus.setCurrentLan(current_longitude);
//                driverStatus.setCurrentLat(current_latitude);
//            }*/
//
//            driverStatus.setCurrentLan(current_longitude);
//            driverStatus.setCurrentLat(current_latitude);
//
//            int batteryPercent = AppUtils.getBatteryPercentage(this);
//            //String deviceModel = AppUtils.getDeviceName();
////            Log.e(TAG, "Battery Percent--> " + batteryPercent + "% " + "Model Number--> " + deviceModel);
//            driverStatus.setBatteryStatus(batteryPercent);
//            String formattedAddress = AppUtils.getCompleteAddressString(BaseDrawerActivity.this, driverStatus.getCurrentLat(), driverStatus.getCurrentLan());
//            Timber.tag(TAG).e("Current Address--> %s", formattedAddress);
//            driverStatus.setAddress(formattedAddress);
//            driverStatus.setDriverStatus(position);
//
//            driverStatus.setTopic("driver");
//            driverStatus.setDriver_id(loginPrefManager.getDriverID());
//
//            Set manager = loginPrefManager.getAdminList();
//            List<String> mainList = new ArrayList<String>(manager);
//            driverStatus.setAdminArray(mainList);
//
//
//            if (type == 1) {
//                driverStatus.setLogOut(true);
//            }
//
//            //Log.e("driverStatus", new Gson().toJson(driverStatus));
//
//            apiService.updateDriverStatus(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken(), driverStatus).enqueue(new Callback<DriverStatusUpdate>() {
//                @Override
//                public void onResponse(@NotNull Call<DriverStatusUpdate> call, @NotNull Response<DriverStatusUpdate> response) {
//
//                    try {
//                        if (response.raw().code() == 200) {
//                            //when he is in offline no need to call service
////                            if (position == 3) {
////                                if (isMyServiceRunning(IdleUpdatedService.class)) {
////                                    stopService(new Intent(BaseDrawerActivity.this, IdleUpdatedService.class));
////                                }
////                            }
//
//
//                            if (type == 1) {
//                                logoutonCall();
//                            }
//                        } else if (response.raw().code() == 401) {
//                            try {
//                                if (AppHelper.getPool().getCurrentUser() != null) {
//                                    findCurrent();
//                                } else {
//                                    if (type == 1) {
//                                        logoutonCall();
//                                    }
//                                }
//                            } catch (Exception e) {
//                                Timber.tag(TAG).e("exxceptioninLocation%s", e.getMessage());
//                            }
//
//                        }
//                        //alert dialog for user already logged in
//                        else if (response.code() == 494) {
//                            showAlertDialog(BaseDrawerActivity.this);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        dismiss_loader();
//                    }
//                }
//
//                @Override
//                public void onFailure(@NotNull Call<DriverStatusUpdate> call, @NotNull Throwable t) {
//                    dismiss_loader();
//                }
//            });
//
//        } catch (Exception e) {
//            dismiss_loader();
//        }
//    }

    public void startHomeScreen() {
        startActivity(new Intent(BaseDrawerActivity.this, NavigationActivity.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
    }

    //for help module, open mail compose screen
    private void sendMailToAdmin() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + loginPrefManager.getSupportEmail()));
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject));
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.help_content));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void findCurrent() {
        user = AppHelper.getPool().getCurrentUser();
        username = user.getUserId();

        if (username != null) {
            AppHelper.setUser(username);
            //edt_login_enter_email.setText(user.getUserId());
            user.getSessionInBackground(authenticationHandler);

        } else {
            logoutonCall();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    private void checkUser() {
        //here logged out code for Glympse
        if (loginPrefManager.getGlympseEnabled()) {
            if (!loginPrefManager.getGlympseUserName().isEmpty() && !loginPrefManager.getGlympseUserPswd().isEmpty()) {
                //set glympse user
                loginPrefManager.setGlympseUserName("glympseUserName", "");
                loginPrefManager.setGlympseUserPswd("glympsePassword", "");
                EnRouteWrapper.instance().manager().logout(2); // default value

            }
        }

        user = AppHelper.getPool().getCurrentUser();
        username = user.getUserId();

        Timber.tag("user_name").e("-%s", username);

        if (username != null) {
            appLozicLogout();
        } else if (loginPrefManager.getManagerID().equals("")) {
            appLozicLogout();
        } else {
            dismiss_loader();
        }

    }

    private void showDialogMessage(String body) {
        /*loginAlertDailog = new LoginAlertDailog(BaseDrawerActivity.this, R.style.MyDialogStyle, body);
        loginAlertDailog.show();*/
        new android.app.AlertDialog.Builder(BaseDrawerActivity.this)
                .setTitle("Failed to authenticate user")
                .setMessage(body)
                .setPositiveButton(R.string.login_again, (dialog, which) -> {
                    logoutonCall();
                })
                .show();
    }

    private void closeWaitDialog() {
        try {
            loginAlertDailog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getUserAuthentication(AuthenticationContinuation continuation, String
            username) {
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

    private void mfaAuth(MultiFactorAuthenticationContinuation continuation) {
        MultiFactorAuthenticationContinuation multiFactorAuthenticationContinuation = continuation;
        Intent mfaActivity = new Intent(this, MFAActivity.class);
        mfaActivity.putExtra("mode", multiFactorAuthenticationContinuation.getParameters().getDeliveryMedium());
        startActivityForResult(mfaActivity, 5);
    }

    private void check_current_activity() {
        ActivityManager.RunningTaskInfo runningTaskInfo = activityManager.getRunningTasks(26).get(0);
        String activity = runningTaskInfo.topActivity.getClassName();

        Log.e("activityName", "" + activity);

        switch (activity) {
            case "com.app.deliforcemanager.Activities.NavigationActivity":
                update_api(1);
                break;
            case "com.app.deliforcemanager.Activities.AnalyticsActivity":
                update_api(2);
                break;
            case "com.app.deliforcemanager.Activities.ProfileActivity":
                update_api(3);
                break;
            case "com.app.deliforcemanager.Activities.ChangePasswordActivity":
                update_api(4);
                break;
            case "com.app.deliforcemanager.Activities.SettingsActivity":
                update_api(5);
                break;
        }

    }

    private void update_api(int posistion) {

        switch (posistion) {
            case 1:
                LocalBroadcastManager.getInstance(BaseDrawerActivity.this).sendBroadcast(new Intent("updated").putExtra("start_order", "1"));
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
        }
    }

    private void initBroadCast() {

        BroadcastReceiver driverStatusChangedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    Timber.tag("intent").e("-----");

                    if (intent.getExtras() != null) {
                        String fav_type = intent.getExtras().getString("profile", "");
                        Timber.tag("fav_type").e("-----%s", fav_type);
                        if (fav_type.equals("3")) {
                            tvMenuName.setText(loginPrefManager.getStringValue("driver_name"));
                           // change_status(Integer.parseInt(loginPrefManager.getStringValue("driver_status")));
                        }
                    }

                    if (!loginPrefManager.getStringValue("image_url").equals("")) {
                        getImageUrl(loginPrefManager.getStringValue("image_url"));
                    }
                }
            }

        };

        LocalBroadcastManager.getInstance(this).registerReceiver(driverStatusChangedReceiver, new IntentFilter("changeByAdmin"));
    }

    private void initServiceBroadcast() {
        Serverreceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Timber.e("servicetriggered-----");

                if (intent != null && intent.getExtras() != null) {
                    String fav_type = intent.getExtras().getString("server_profile", "");
                    Timber.e("fav_type: %s", fav_type);
                    /*if (fav_type.equals("100")) {
                        if (isMyServiceRunning(IdleService.class)) {
                            stopService(new Intent(BaseDrawerActivity.this, IdleService.class));
                        }
                    } else */
                    Timber.e("%s%s", "favType", "" + fav_type);

//                    if (fav_type.equals("101")) {
//                        if (!isMyServiceRunning(IdleUpdatedService.class)) {
//                            startService(new Intent(BaseDrawerActivity.this, IdleUpdatedService.class));
//                        }
//                    } else



                        if (fav_type.equalsIgnoreCase("102")) {
                        Log.e("facvType", "102");
                        startActivity(new Intent(BaseDrawerActivity.this, NavigationActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        finish();
                    }
                }

            }
        };

        LocalBroadcastManager.getInstance(BaseDrawerActivity.this).registerReceiver(Serverreceiver, new IntentFilter("service_status"));
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {

        assert manager != null;
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Timber.e("%s", true);
                return true;
            }
        }
        Timber.i("%s", false);
        return false;
    }

    private void appLozicLogout() {
        exit();
       /* Applozic.logoutUser(getApplicationContext(), new AlLogoutHandler() {
            @Override
            public void onSuccess(Context context) {
                exit();
            }

            @Override
            public void onFailure(Exception exception) {
                exit();
            }
        });*/
    }

    private void exit() {
        dismiss_loader();

//        if (isMyServiceRunning(IdleUpdatedService.class)) {
//            stopService(new Intent(BaseDrawerActivity.this, IdleUpdatedService.class));
//        }


        if (isMyServiceRunning(PushNotificationService.class)) {
            stopService(new Intent(BaseDrawerActivity.this, PushNotificationService.class));
        }

        loginPrefManager.setStringValue("image_url", "");
        startActivity(new Intent(this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        finish();
    }

    public void getImageUrl(String url) {
        Glide.with(getApplicationContext()).asBitmap().load(url).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                user_profile.setImageBitmap(resource);
            }
        });
    }

    public void exit_app() {
        if (mdrawerLayout.isDrawerOpen(GravityCompat.START)) {
            if (mdrawerLayout!=null) {
                mdrawerLayout.closeDrawer(Gravity.LEFT);
            }
        } else {
            Handler handler = new Handler();
            Runnable runnable = () -> snackbar.dismiss();
            if (snackbar != null && snackbar.isShown()) {
                super.onBackPressed();
                handler.removeCallbacks(runnable);
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                showexitbar();
                handler.postDelayed(runnable, 2000);
            }
        }

    }

    private void showexitbar() {
        LinearLayout.LayoutParams objLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        snackbar = Snackbar.make(this.findViewById(android.R.id.content), getString(R.string.app_name), Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        int navbarHeight = getNavBarHeight(this);
        FrameLayout.LayoutParams parentParams = (FrameLayout.LayoutParams) layout.getLayoutParams();
        parentParams.setMargins(0, 0, 0, 0 - navbarHeight + 100);
        layout.setLayoutParams(parentParams);
        layout.setPadding(0, 0, 0, 0);
        layout.setLayoutParams(parentParams);
        View snackView = getLayoutInflater().inflate(R.layout.custom_snack_bar, null);
        tv_duty_status = snackView.findViewById(R.id.message_text_view);
        status_switch = snackView.findViewById(R.id.sw_driver_duty_status);
        LinearLayout driver_status = snackView.findViewById(R.id.driver_status);
        driver_status.setVisibility(View.GONE);
        tv_duty_status.setText(getString(R.string.exit_app));
        status_switch.setVisibility(View.GONE);
        layout.addView(snackView, objLayoutParams);
        snackbar.show();
    }

    public void show_ride_exist(String msg) {
        LinearLayout.LayoutParams objLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        snackbar = Snackbar.make(this.findViewById(android.R.id.content), getString(R.string.app_name), Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        int navbarHeight = getNavBarHeight(this);
        FrameLayout.LayoutParams parentParams = (FrameLayout.LayoutParams) layout.getLayoutParams();
        parentParams.setMargins(0, 0, 0, 0 - navbarHeight + 100);
        layout.setLayoutParams(parentParams);
        layout.setPadding(0, 0, 0, 0);
        layout.setLayoutParams(parentParams);
        View snackView = getLayoutInflater().inflate(R.layout.no_internet_view, null);
        TextView status = snackView.findViewById(R.id.message_text_view);
        status.setText(msg);
        status.setTextSize(13);
        layout.addView(snackView, objLayoutParams);
        snackbar.setDuration(5000);
        snackbar.show();
    }

    public double getAppVersion() {
        double version = 1;
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = Double.parseDouble(pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(BaseDrawerActivity.this).unregisterReceiver(Serverreceiver);
    }

}