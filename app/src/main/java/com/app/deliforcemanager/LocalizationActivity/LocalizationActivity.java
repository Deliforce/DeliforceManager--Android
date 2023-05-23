package com.app.deliforcemanager.LocalizationActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
import com.app.deliforcemanager.Activities.LoginActivity;
import com.app.deliforcemanager.Activities.SplashActivity;
import com.app.deliforcemanager.BuildConfig;
import com.app.deliforcemanager.Cognito.AppHelper;
import com.app.deliforcemanager.CustomDialogView.LoginAlertDailog;
import com.app.deliforcemanager.CustomViews.Loader;
import com.app.deliforcemanager.PermissionChecker.PermissionHelper;
import com.app.deliforcemanager.R;
import com.app.deliforcemanager.ServiceApi.APIServiceFactory;
import com.app.deliforcemanager.ServiceApi.ApiService;
import com.app.deliforcemanager.Services.HUD;
import com.app.deliforcemanager.Services.IdleUpdatedService;
import com.app.deliforcemanager.Services.MusicService;
import com.app.deliforcemanager.Services.PushNotificationService;
import com.app.deliforcemanager.Utils.AppUtils;
import com.app.deliforcemanager.Utils.DeliforceConstants;
import com.app.deliforcemanager.Utils.LoginPrefManager;
import com.app.deliforcemanager.Utils.NetworkAvailability;
/*import com.applozic.mobicomkit.Applozic;
import com.applozic.mobicomkit.listners.AlLogoutHandler;*/
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

import static com.app.deliforcemanager.Activities.TaskDetailsActivity.CODE_DRAW_OVER_OTHER_APP_PERMISSION;
import static com.app.deliforcemanager.Services.IdleService.STANDARD_ACCURACY;
import static com.app.deliforcemanager.Services.IdleService.liveLastLocation;
import static com.app.deliforcemanager.Services.IdleService.mCurrentLocation;
import static com.app.deliforcemanager.Services.IdleService.mLastLocation;


public class LocalizationActivity extends AppCompatActivity {

    protected static final String TAG = "LocationOnOff";
    final static int REQUEST_LOCATION = 199;
    // Bundle key
    private static final String KEY_ACTIVIY_LOCALE_CHANGED = "activity_locale_changed";
    private static final String MY_CONNECTIVITY_CHANGE = "connectivity_change";
    /**
     * Code used in requesting runtime permissions.
     */
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    public static double current_longitude;
    public static double current_latitude;
    public static float current_accuraccy;
    public ApiService apiService;
    public PermissionHelper permissionHelper;
    public LoginPrefManager loginPrefManager;
    public ProgressDialog progressDialog;
    public Activity activity;
    public Loader loader;
    public Loader imageUploadLoader;
    public boolean location_enabled;
    public Snackbar snackbar;
    public SimpleDateFormat DEFALUT_FORMAT;
    public double user_lat, user_lng;
    String username = "";
    LoginAlertDailog loginAlertDailog;
    LocationManager mlocManager;
    /*AlertDialog alertDialog;
    BaseDrawerActivity baseDrawerActivity;*/
    LocationRequest mLocationRequest;
    //GoogleApiClient mGoogleApiClient;
    PendingResult<LocationSettingsResult> result;
    // Boolean flag to check that activity was recreated from locale changed.
    private boolean isLocalizationChanged = false;
    // Prepare default language.
    private String currentLanguage = LanguageSetting.getDefaultLanguage();
    private CognitoIdToken cognitoIdToken;

    //private Switch status_switch;
    private String cognitoToken, awsIdToken, awsRefreshToken;
    private String password;
    AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
        @Override
        public void onSuccess(CognitoUserSession cognitoUserSession, CognitoDevice device) {
            Timber.d(" -- Auth Success");
            /*AppHelper.setCurrSession(cognitoUserSession);
            AppHelper.newDevice(device);*/
            closeWaitDialog();


            CognitoAccessToken awsAccessToken = cognitoUserSession.getAccessToken();
            cognitoToken = awsAccessToken.getJWTToken();
            cognitoIdToken = cognitoUserSession.getIdToken();
            awsIdToken = cognitoIdToken.getJWTToken();
            Timber.e("AWS Id Token--> %s", awsIdToken);
            CognitoRefreshToken cognitoRefreshToken = cognitoUserSession.getRefreshToken();
            awsRefreshToken = cognitoRefreshToken.getToken();
//            postRefreshToken(currentLat, currentLong);

            loginPrefManager.setAccessToken("" + cognitoToken);
            loginPrefManager.setCogintoToken("" + awsIdToken);
            loginPrefManager.setRefreshTokenToken("" + awsRefreshToken);


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

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onFailure(Exception e) {
            closeWaitDialog();
            if (!isFinishing())
                showDialogMessage("Sign-in failed", AppHelper.formatException(e));
        }

        @Override
        public void authenticationChallenge(ChallengeContinuation continuation) {
            /**
             * For Custom authentication challenge, implement your logic to present challenge to the
             * user and pass the user's responses to the continuation.
             */
//            if ("NEW_PASSWORD_REQUIRED".equals(continuation.getChallengeName())) {
//                // This is the first sign-in attempt for an admin created user
//                newPasswordContinuation = (NewPasswordContinuation) continuation;
//                AppHelper.setUserAttributeForDisplayFirstLogIn(newPasswordContinuation.getCurrentUserAttributes(),
//                        newPasswordContinuation.getRequiredAttributes());
//                closeWaitDialog();
//            } else if ("SELECT_MFA_TYPE".equals(continuation.getChallengeName())) {
//                closeWaitDialog();
//                mfaOptionsContinuation = (ChooseMfaContinuation) continuation;
//                List<String> mfaOptions = mfaOptionsContinuation.getMfaOptions();
//                selectMfaToSignIn(mfaOptions, continuation.getParameters());
//            }
        }
    };
    private ActivityManager activityManager;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private boolean calledByNetworkReciever = true;
    private NetworkAvailability networkAvailability;
    private BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)) {
                Timber.d("network not available");
                showNointerntView(getString(R.string.no_internet_conn_msg_txt));
            } else {
                Timber.d("network is available");
                syncData();
            }
        }
    };
    private BroadcastReceiver mGpsSwitchStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Timber.e("-%s", intent.getAction());

            if (!mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                startLocationUpdates();

            /*Log.e("location_enabled", String.valueOf(location_enabled));

            if (!location_enabled) {
                onCreateBuild();
            }*/


        }
    };

    public static boolean isValidPattern(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    /*@RequiresApi(Build.VERSION_CODES.N)
    private void createChangeConnectivityMonitor() {
        final Intent intent = new Intent(MY_CONNECTIVITY_CHANGE);
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            connectivityManager.registerNetworkCallback(
                    new NetworkRequest.Builder().build(),
                    new ConnectivityManager.NetworkCallback() {
                        @Override
                        public void onAvailable(Network network) {
                            Timber.d("On available network");
                            sendBroadcast(intent);
                        }

                        @Override
                        public void onLost(Network network) {
                            Timber.d("On not available network");
                            sendBroadcast(intent);
                        }
                    });
        }
    }*/

    public static boolean isValidURLPattern(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.WEB_URL.matcher(target).matches());
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static int getNavBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(DeliforceConstants.AUTO_ALLOCATION_NEAREST_AVAILABLE);
        mLocationRequest.setFastestInterval(DeliforceConstants.AUTO_ALLOCATION_NEAREST_AVAILABLE);
    }

    /**
     * Requests location updates from the FusedLocationApi. Note: we don't call this unless location
     * runtime permission has been granted.
     */
    private void startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, locationSettingsResponse -> {
                    Timber.i("All location settings are satisfied.");

                    if (ActivityCompat.checkSelfPermission(LocalizationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocalizationActivity.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                })
                .addOnFailureListener(this, e -> {
                    int statusCode = ((ApiException) e).getStatusCode();
                    switch (statusCode) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            Timber.i("Location settings are not satisfied. Attempting to upgrade location settings ");
                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the
                                // result in onActivityResult().
                                if (getLocationMode(LocalizationActivity.this) < 3) {
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(LocalizationActivity.this, REQUEST_LOCATION);
                                }
                            } catch (IntentSender.SendIntentException sie) {
                                Timber.i("PendingIntent unable to execute request.");
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            String errorMessage = "Location settings are inadequate, and cannot be " +
                                    "fixed here. Fix in SettingsModel.";
                            if (getLocationMode(LocalizationActivity.this) < 3) {
                                showAlertForHighAccuracy();
                            }
                            Timber.e(errorMessage);
                            break;
                    }
                });
    }

    /**
     * Creates a callback for receiving location events.
     */
    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                if (locationResult.getLastLocation() != null) {
                    Location location = locationResult.getLastLocation();
                    Timber.i("onLocationUpdated Localisation2222: " + location.getProvider() + ", Accuracy " + location.getAccuracy() +
                            ", LatLng: " + location.getLatitude() + "," + location.getLongitude() + ", isMock: " + location.isFromMockProvider());
                    if (location.getAccuracy() < STANDARD_ACCURACY) {
                        mCurrentLocation = location;
                        mLastLocation = location;
                        liveLastLocation = location;
                        user_lng = current_longitude = location.getLongitude();
                        user_lat = current_latitude = location.getLatitude();
                        current_accuraccy = location.getAccuracy();
                        AppUtils.setDeviceLocation(location, LocalizationActivity.class.getSimpleName());
                    }
                }
            }
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setupLanguage();
        checkBeforeLocaleChanging();
        super.onCreate(savedInstanceState);
        loginPrefManager = new LoginPrefManager(LocalizationActivity.this);
        progressDialog = new ProgressDialog(LocalizationActivity.this);

        networkAvailability = NetworkAvailability.getInstance();

        loader = new Loader(LocalizationActivity.this);
        imageUploadLoader = new Loader(LocalizationActivity.this);

        setupPermissionHelper();

        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();

        apiService = APIServiceFactory.getRetrofit().create(ApiService.class);

        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        DEFALUT_FORMAT = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        //SmartLocation.with(LocalizationActivity.this).location().start(onLocationUpdatedListener);
        location_enabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        registerReceiver(mGpsSwitchStateReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
        activityManager = (ActivityManager) getApplication().getSystemService(Activity.ACTIVITY_SERVICE);
    }

    // Get current language
    protected final String getLanguage() {
        return LanguageSetting.getLanguage();
    }

    // Provide method to set application language by country name.
    protected final void setLanguage(String language) {
        if (!isDuplicatedLanguageSetting(language)) {
            LanguageSetting.setLanguage(LocalizationActivity.this, language);
            notifyLanguageChanged();
        }
    }

    // Provide method to set application language by locale.
    public final void setLanguage(Locale locale) {
        setLanguage(locale.getLanguage());
    }

    // Check that bundle come from locale change.
    // If yes, bundle will obe remove and set boolean flag to "true".
    private void checkBeforeLocaleChanging() {
        boolean isLocalizationChanged = getIntent().getBooleanExtra(KEY_ACTIVIY_LOCALE_CHANGED, false);
        if (isLocalizationChanged) {
            this.isLocalizationChanged = true;
            getIntent().removeExtra(KEY_ACTIVIY_LOCALE_CHANGED);
        }
    }

    // Setup language to locale and language preference.
    // This method will called before onCreate.
    private void setupLanguage() {
        Locale locale = LanguageSetting.getLocale(LocalizationActivity.this);
        setupLocale(locale);
        currentLanguage = locale.getLanguage();
        LanguageSetting.setLanguage(LocalizationActivity.this, locale.getLanguage());
    }

    // Set locale configuration.
    private void setupLocale(Locale locale) {
        updateLocaleConfiguration(LocalizationActivity.this, locale);
    }

    private void updateLocaleConfiguration(Context context, Locale locale) {
        Configuration config = new Configuration();
        config.locale = locale;
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        context.getResources().updateConfiguration(config, dm);
    }

    // Avoid duplicated setup
    private boolean isDuplicatedLanguageSetting(String language) {
        return language.toLowerCase(Locale.getDefault()).equals(LanguageSetting.getLanguage());
    }

    // Let's take it change! (Using recreate method that available on API 11 or more.
    private void notifyLanguageChanged() {
        getIntent().putExtra(KEY_ACTIVIY_LOCALE_CHANGED, true);
        callDummyActivity();
        recreate();
    }


    /*Global permission helper class */

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }


    /*Global Short toast message method*/

    // If activity is run to backstack. So we have to check if this activity is resume working.
    @Override
    public void onResume() {
        super.onResume();

        networkAvailability.registerNetworkAvailability(this, networkReceiver);

        /*IntentFilter filter = new IntentFilter();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Timber.d("using registerNetworkCallback");
            createChangeConnectivityMonitor();
            filter.addAction(MY_CONNECTIVITY_CHANGE);
        } else {
            Timber.d("using old broadcast receiver");
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        }

        registerReceiver(this.mConnReceiver, filter);*/


        if (checkPermissions()) {
            startLocationUpdates();
        } else if (!checkPermissions() && !calledByNetworkReciever) {
            requestPermissions();
        }

        if (isMyServiceRunning(HUD.class)) {
            stopService(new Intent(LocalizationActivity.this, HUD.class));
        }
        if (isMyServiceRunning(MusicService.class)) {
            stopService(new Intent(LocalizationActivity.this, MusicService.class));
        }

        new Handler().post(() -> {
            checkLocaleChange();
            checkAfterLocaleChanging();
        });

    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Timber.i("Displaying permission rationale to provide additional context.");
            // Request permission
            ActivityCompat.requestPermissions(LocalizationActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        } else {
            Timber.i("Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(LocalizationActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mGpsSwitchStateReceiver);

        dismiss_loader();
    }

    // Check if locale has change while this activity was run to backstack.
    private void checkLocaleChange() {
        if (!LanguageSetting.getLanguage().toLowerCase(Locale.getDefault())
                .equals(currentLanguage.toLowerCase(Locale.getDefault()))) {
            callDummyActivity();
            recreate();
        }
    }

    // Call override method if local is really changed
    private void checkAfterLocaleChanging() {
        if (isLocalizationChanged) {
            isLocalizationChanged = false;
        }
    }

    private void callDummyActivity() {
        startActivity(new Intent(LocalizationActivity.this, BlankDummyActivity.class));
    }

    private void setupPermissionHelper() {
        permissionHelper = new PermissionHelper(this);
    }

    public void showShortToastMessage(String message) {
        Toast.makeText(LocalizationActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    private void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        System.out.println("removed Location Updates");
                    }
                });
    }

    public void showShortMessage(String msg) {
        Toast.makeText(LocalizationActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    public boolean validEmail(EditText email_edit) {

        String email = email_edit.getText().toString();

        if (email.equalsIgnoreCase("")) {

            email_edit.setError(getString(R.string.error_email_id));
            email_edit.requestFocus();

            return false;
        } else {
            email_edit.setError(null);
        }

        return true;
    }

    public boolean emailpattern(EditText editText) {
        if (!isValidPattern(editText.getText().toString())) {
            editText.setError(getString(R.string.error_email_id));
            editText.requestFocus();

            return false;
        }
        return true;
    }

    public boolean emailPattern(EditText editText) {
        return isValidPattern(editText.getText().toString());
    }

    public boolean urlPattern(EditText editText) {
        return isValidURLPattern(editText.getText().toString());
    }

    public boolean validMobileno(EditText mobile_text) {

        String phone_no = mobile_text.getText().toString();

        if (phone_no.isEmpty() || phone_no.length() < 0 || phone_no.length() > 10) {

            mobile_text.setError(getString(R.string.error_phone));
            mobile_text.requestFocus();
            return false;
        } else {
            mobile_text.setError(null);
        }

        return true;
    }

    public boolean validFirstName(EditText mobile_text) {

        String phone_no = mobile_text.getText().toString();

        if (phone_no.isEmpty() || phone_no.length() < 0 || phone_no.length() > 10) {

            mobile_text.setError(getString(R.string.error_first_name));
            mobile_text.requestFocus();
            return false;
        } else {
            mobile_text.setError(null);
        }

        return true;
    }

    public boolean validLastName(EditText mobile_text) {

        String phone_no = mobile_text.getText().toString();

        if (phone_no.isEmpty() || phone_no.length() < 0 || phone_no.length() > 10) {

            mobile_text.setError(getString(R.string.error_last_name));
            mobile_text.requestFocus();
            return false;
        } else {
            mobile_text.setError(null);
        }

        return true;
    }

    public boolean validLocation(TextView location_text) {

        String phone_no = location_text.getText().toString();

        if (phone_no.isEmpty() || phone_no.length() < 0) {

            location_text.setError(getString(R.string.error_location));
            location_text.requestFocus();
            return false;
        } else {
            location_text.setError(null);
        }

        return true;
    }

    public boolean validPassword(EditText password_edit) {

        String password = password_edit.getText().toString();
        if (password.isEmpty() || password.length() < 0) {

            password_edit.setError(getString(R.string.error_password));
            password_edit.requestFocus();

            return false;

        } else {
            password_edit.setError(null);
        }

        return true;
    }

    public boolean validPasswordWithError(EditText password_edit, TextInputLayout textInputLayout) {

        String password = password_edit.getText().toString();
        if (password.isEmpty() || password.length() < 0) {

            password_edit.setError(getString(R.string.error_password));
            textInputLayout.setEndIconTintMode(PorterDuff.Mode.CLEAR);
            return false;

        } else {
            password_edit.setError(null);
        }

        return true;
    }

    public boolean validConfirmPassword(EditText new_password, EditText confirm_password, TextInputLayout textInputLayout) {


        if (confirm_password.getText().toString().equalsIgnoreCase("")) {
            confirm_password.setError(getString(R.string.error_confirm));
            textInputLayout.setEndIconTintMode(PorterDuff.Mode.CLEAR);
            return false;
        } else if (!new_password.getText().toString().equals(confirm_password.getText().toString())) {

            confirm_password.setError(getString(R.string.password_mismatch));
            textInputLayout.setEndIconTintMode(PorterDuff.Mode.CLEAR);

            return false;
        }


        return true;
    }

    public boolean validaname(EditText name_edit) {

        String name = name_edit.getText().toString();

        if (name.isEmpty() || name.length() < 0) {

            name_edit.setError(getString(R.string.error_name));
            name_edit.requestFocus();
            return false;
        }

        return true;
    }

    public void taskUpdateLoader() {
        try {
            if (!isFinishing()) {
                if (imageUploadLoader != null && !imageUploadLoader.isShowing()) {
                    imageUploadLoader.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void taskUpdateDismissLoader() {
        try {
            if (!isFinishing()) {
                if (imageUploadLoader != null) {
                    imageUploadLoader.dismiss();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show_loader() {
        try {
            if (!isFinishing()) {
                if (loader != null && !loader.isShowing()) {
                    loader.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismiss_loader() {
        try {
            if (!isFinishing()) {
                if (loader != null) {
                    loader.dismiss();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * -------------------------- Cognito Refresh---------------
     */
    public void findCurrent() {
        CognitoUser user = AppHelper.getPool().getCurrentUser();
        username = user.getUserId();
        if (username != null) {
            AppHelper.setUser(username);
            //edt_login_enter_email.setText(user.getUserId());
            user.getSessionInBackground(authenticationHandler);
        }
    }

    private void showDialogMessage(String title, String body) {
        loginAlertDailog = new LoginAlertDailog(LocalizationActivity.this, R.style.MyDialogStyle, body);
        loginAlertDailog.show();
    }

    private void closeWaitDialog() {
        try {
            loginAlertDailog.dismiss();
        } catch (Exception e) {
            //
        }
    }

    /*private void onCreateBuild() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
    }*/

    /*@Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(DeliforceConstants.AUTO_ALLOCATION_NEAREST_AVAILABLE);
        mLocationRequest.setFastestInterval(DeliforceConstants.AUTO_ALLOCATION_NEAREST_AVAILABLE);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);


        result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(result -> {
            final Status status = result.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    if (ActivityCompat.checkSelfPermission(LocalizationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocalizationActivity.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                        location_enabled = true;
                    }
                    if (mGoogleApiClient.isConnected()) {
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, LocalizationActivity.this);
                    } else mGoogleApiClient.connect();
                    //...
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    // Location settings are not satisfied. But could be fixed by showing the user
                    // a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        status.startResolutionForResult(
                                LocalizationActivity.this,
                                REQUEST_LOCATION);
                    } catch (IntentSender.SendIntentException e) {
                        // Ignore the error.
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    // Location settings are not satisfied. However, we have no way to fix the
                    // settings so we won't show the dialog.
                    //...
                    break;
            }
        });

    }*/

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

    private void mfaAuth(MultiFactorAuthenticationContinuation continuation) {
        MultiFactorAuthenticationContinuation multiFactorAuthenticationContinuation = continuation;
        Intent mfaActivity = new Intent(this, MFAActivity.class);
        mfaActivity.putExtra("mode", multiFactorAuthenticationContinuation.getParameters().getDeliveryMedium());
        startActivityForResult(mfaActivity, 5);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == REQUEST_LOCATION) {
                switch (resultCode) {
                    case Activity.RESULT_OK: {
                        try {
                            // All required changes were successfully made
                            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            startLocationUpdates();

                            ActivityManager.RunningTaskInfo runningTaskInfo = activityManager.getRunningTasks(26).get(0);
                            if (runningTaskInfo.topActivity.getClassName().contains(this.getClass().getSimpleName())) {
                                Timber.e(runningTaskInfo.topActivity.getClassName());

                                if (runningTaskInfo.topActivity.getClassName().equals("com.app.deliforcemanager.Activities.SplashActivity")) {
                                    new Handler().postDelayed(SplashActivity.runnable, 3000);
                                }
                            }

                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                                location_enabled = true;
                            }
                            break;
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        }
                    }
                    case Activity.RESULT_CANCELED: {
                        if (getLocationMode(LocalizationActivity.this) < 3) {
                            showAlertForHighAccuracy();
                        }
                        break;
                    }
                    default: {
                        break;
                    }
                }
            } else if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
                if (resultCode == Activity.RESULT_OK) {
                    System.out.println(resultCode);
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    /*public OnLocationUpdatedListener onLocationUpdatedListener = new OnLocationUpdatedListener() {
        @Override
        public void onLocationUpdated(Location location) {
            Timber.i("onLocationUpdated Localisation11111: " + location.getProvider() + ", Accuracy " + location.getAccuracy() +
                    ", LatLng: " + location.getLatitude() + "," + location.getLongitude() + ", isMock: " + location.isFromMockProvider());
            if (location.getAccuracy() < STANDARD_ACCURACY) {
                current_longitude = location.getLongitude();
                current_latitude = location.getLatitude();
                current_accuraccy = location.getAccuracy();
                AppUtils.setDeviceLocation(location, LocalizationActivity.class.getSimpleName());
            }
        }
    };*/

    /*private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            Timber.d("triggering on connectivity change");
            if (context != null && isNetworkAvailable(context)) {
                System.out.println("triggering on connection back");
                syncData();
            } else {
                showNointerntView(getString(R.string.no_internet_conn_msg_txt));
            }
        }
    };

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }*/

    private void showAlertForHighAccuracy() {
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(LocalizationActivity.this)
                .setTitle(getString(R.string.alert))
                .setCancelable(false)
                .setMessage(getString(R.string.gps_disabled))
                .setPositiveButton(getString(R.string.ok),
                        (dialog, which) -> {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }).create();
        alertDialog.show();

    }

    /*get the location mode*/
    public int getLocationMode(Context context) {
        int locationMode = 0;

        try {
            locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }


        return locationMode;
    }

    private void syncData() {
        try {
            if (snackbar != null) {
                snackbar.dismiss();
            }

            ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = activityManager.getRunningAppProcesses();

            for (ActivityManager.RunningAppProcessInfo processInfo : runningAppProcessInfos) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        String packageName = getPackageName();
                        if (activeProcess.equals(packageName)) {
                            String currentClassName = this.getClass().getSimpleName();
                            packageName += ".Activities.SplashActivity";
                            if (packageName.contains(currentClassName))
                                AskLocationPermission();
                            else
                                calledByNetworkReciever = false;
                        } else
                            System.out.println("process name: " + activeProcess);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        networkAvailability.unregisterNetworkAvailability(this, networkReceiver);
        super.onPause();

        Timber.d("triggering on connectivity onDestroyed");
        //unregisterReceiver(mConnReceiver);

        stopLocationUpdates();
        dismiss_loader();
    }

    public void showNointerntView(String msg) {
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
        layout.addView(snackView, objLayoutParams);
        snackbar.show();
    }

    public void show_error_response(String msg) {
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
        layout.addView(snackView, objLayoutParams);
        snackbar.setDuration(5000);
        snackbar.show();
    }

    public void AskLocationPermission() {
        permissionHelper.check(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .withDialogBeforeRun(R.string.dialog_before_run_title, R.string.dialog_location_before_run_message, R.string.dialog_positive_button)
                .setDialogPositiveButtonColor(R.color.colorPrimary)
                .onSuccess(this::onSuccess)
                .onDenied(this::onDenied)
                .onNeverAskAgain(this::onNeverAskAgain)
                .run();
    }

    public void onSuccess() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            location_enabled = true;
        }

        Timber.e("------" + "/" + location_enabled);
        if (location_enabled) {
            calledByNetworkReciever = false;
            new Handler().postDelayed(SplashActivity.runnable, 3000);
        }
    }

    private void onNeverAskAgain() {
        showNointerntView(getString(R.string.enable_permission));
        snackbar.show();
    }

    private void onDenied() {
        calledByNetworkReciever = true;
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissionHelper != null)
            permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Timber.i("onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Timber.i("User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Timber.i("Permission granted, updates requested, starting location updates");
                startLocationUpdates();
            } else {
                android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(LocalizationActivity.this)
                        .setTitle(getString(R.string.alert))
                        .setCancelable(false)
                        .setMessage(getString(R.string.user_already_logged))
                        .setPositiveButton(getString(R.string.settings),
                                (dialog, which) -> {
                                    Intent intent = new Intent();
                                    intent.setAction(
                                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package",
                                            BuildConfig.APPLICATION_ID, null);
                                    intent.setData(uri);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    dialog.dismiss();

                                }).create();
                alertDialog.show();
            }
        }
    }

    private void appLozicSignOut() {
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

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /*public void ShowBlockedDialog() {
        alertDialog = new AlertDialog(this, R.style.MyDialogStyle, 100, new AlertDialog.BlockedInterface() {
            @Override
            public void onclick(boolean status) {
                baseDrawerActivity.logoutonCall();
                alertDialog.dismiss();
            }

            @Override
            public void reDirectToSpecifPage(String pageDeterminationValue) {

            }
        });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }

    public void ShowWarningDialog() {
        ActivityManager.RunningTaskInfo runningTaskInfo = activityManager.getRunningTasks(26).get(0);
        String activity = runningTaskInfo.topActivity.getClassName();

        Log.e("activty", activity);
        if (!activity.equalsIgnoreCase("com.app.deliforce.Activities.SplashActivity")) {
            alertDialog = new AlertDialog(LocalizationActivity.this, R.style.MyDialogStyle, 100, new AlertDialog.BlockedInterface() {
                @Override
                public void onclick(boolean status) {
                    alertDialog.dismiss();
                }

                @Override
                public void reDirectToSpecifPage(String pageDeterminationValue) {

                }
            });
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
    }*/

    /*@Override
    public void onLocationChanged(Location location) {
        Timber.i("onLocationUpdated Localisation2222: " + location.getProvider() + ", Accuracy " + location.getAccuracy() +
                ", LatLng: " + location.getLatitude() + "," + location.getLongitude() + ", isMock: " + location.isFromMockProvider());
        if (location.getAccuracy() < STANDARD_ACCURACY) {
            mCurrentLocation = location;
            mLastLocation = location;
            liveLastLocation = location;
            user_lng = current_longitude = location.getLongitude();
            user_lat = current_latitude = location.getLatitude();
            current_accuraccy = location.getAccuracy();
            AppUtils.setDeviceLocation(location, LocalizationActivity.class.getSimpleName());
        }
    }*/

    /*if user is already login in device and he is trying to login in someOther
     device in this scenario need to show popup when error code is 494
    */
    public void showAlertDialog(Context context) {
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(context)
                .setTitle(getString(R.string.alert))
                .setCancelable(false)
                .setMessage(getString(R.string.user_already_logged))
                .setPositiveButton(context.getString(R.string.ok),
                        (dialog, which) -> {
                            logoutAction();
                            dialog.dismiss();

                        }).create();
        alertDialog.show();
    }


    /**
     * logout alert
     *
     * @param activity
     */
    public void showAlertDialogLogout(Activity activity) {
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(activity)
                .setTitle(getString(R.string.alert))
                .setCancelable(false)
                .setMessage(getString(R.string.logout_alert))
                .setPositiveButton(activity.getString(R.string.ok),
                        (dialog, which) -> {
                            loginPrefManager.setStringValue("idle_token", loginPrefManager.getCogintoToken());
                            String token = loginPrefManager.getStringValue("idle_token");
                            Timber.e("%s", token);
                            logoutAction();
                            dialog.dismiss();

                        })
                .setNegativeButton(activity.getString(R.string.cancel_btn),
                        ((dialog, which) ->
                        {
                            dialog.dismiss();
                        }))
                .create();
        alertDialog.show();
    }

    public void logoutAction() {
        status_logout();
    }

    public void status_logout() {
        if (loginPrefManager.getManagerID() != null) {
            loginPrefManager.setMangerID("");

            //stopService(new Intent(this, LiveTrackingService.class));
            loginPrefManager.setStringValue("image", "");
            loginPrefManager.setStringValue("idle_distance", "");
            loginPrefManager.setRefreshTokenToken("");

            loginPrefManager.setCogintoToken("");
            loginPrefManager.setAccessToken("");
            loginPrefManager.setStringValue("user", "");
            loginPrefManager.setStringValue("filter_values", "");
            loginPrefManager.setFromDate("");
            loginPrefManager.setToDate("");
            loginPrefManager.setStringValue("chk_search_task_sort_by_time", "0");
            loginPrefManager.setStringValue("chk_search_task_sort_by_distance", "0");
            checkUser();
        }
    }

    private void checkUser() {
        CognitoUser user = AppHelper.getPool().getCurrentUser();
        username = user.getUserId();
        Timber.e("-%s", username);

        if (username != null) {
            signOut();
        }
    }

    private void signOut() {
        CognitoUser user = AppHelper.getPool().getCurrentUser();
        user.signOut();
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
//        if (isMyServiceRunning(IdleUpdatedService.class)) {
//            stopService(new Intent(this, IdleUpdatedService.class));
//        }
        if (isMyServiceRunning(PushNotificationService.class)) {
            stopService(new Intent(this, PushNotificationService.class));
        }

        loginPrefManager.setStringValue("image_url", "");
        startActivity(new Intent(this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        finish();
    }


}
