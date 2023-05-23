package com.app.deliforcemanager.Activities;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.amazonaws.mobile.auth.userpools.MFAActivity;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.NewPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.tokens.CognitoAccessToken;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.tokens.CognitoIdToken;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.tokens.CognitoRefreshToken;
import com.app.deliforcemanager.Cognito.AppHelper;
import com.app.deliforcemanager.CustomDialogView.LoginAlertDailog;
import com.app.deliforcemanager.LocalizationActivity.LocalizationActivity;
import com.app.deliforcemanager.ModelClass.TokenRegisterApi.TokenRegister;
import com.app.deliforcemanager.R;
import com.app.deliforcemanager.RawHeaders.LocationInfo.CurrentLocation;
import com.app.deliforcemanager.RawHeaders.LocationInfo.LocationUpdate;
import com.app.deliforcemanager.Services.IdleUpdatedService;
import com.app.deliforcemanager.Utils.AppUtils;
import com.app.deliforcemanager.Utils.DeliforceConstants;
import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class LoginActivity extends LocalizationActivity {

    //GPSTracker gpsTracker;
    private final String TAG = "LoginActivity";
    EditText edt_login_enter_email, edt_login_enter_pwd;
    TextView tvForgotPwd;
    LoginAlertDailog loginAlertDailog;
    TextInputLayout password_view;
    //CountryCodePicker countryCodePicker;
    TextView mob_no_header;
    ActivityManager manager;
    String glympseUserName = "", glympsePassword = "";
    String managerId = "";
    // User Details
    private String username;
    private String password;
    AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
        @Override
        public void onSuccess(CognitoUserSession cognitoUserSession, CognitoDevice device) {
            Timber.d(" -- Auth Success");

            dismiss_loader();
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


            Log.e("cognito_token",loginPrefManager.getCogintoToken());

            Log.e("refresh_token",loginPrefManager.getRefreshToken());



            Timber.e(loginPrefManager.getCogintoToken());

            if (snackbar != null) {
                snackbar.dismiss();
            }
            sendFCMtoken();
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
            dismiss_loader();
            Timber.e(AppHelper.formatException(e));


            if (AppHelper.formatException(e).contains("User does not exist.")) {
                showDialogMessage(getString(R.string.error_signin), getString(R.string.user_not_found));
            } else if (AppHelper.formatException(e).contains("Incorrect username or password.")) {
                showDialogMessage(getString(R.string.error_signin), getString(R.string.invalid_user_found));
            } else {
                showDialogMessage(getString(R.string.error_signin), AppHelper.formatException(e));
            }

        }

        @Override
        public void authenticationChallenge(ChallengeContinuation continuation) {

            if ("NEW_PASSWORD_REQUIRED".equals(continuation.getChallengeName())) {
                NewPasswordContinuation newPasswordContinuation = (NewPasswordContinuation) continuation;
                AppHelper.setUserAttributeForDisplayFirstLogIn(newPasswordContinuation.getCurrentUserAttributes(),
                        newPasswordContinuation.getRequiredAttributes());
                closeWaitDialog();
            } else if ("SELECT_MFA_TYPE".equals(continuation.getChallengeName())) {
                closeWaitDialog();
                /*ChooseMfaContinuation mfaOptionsContinuation = (ChooseMfaContinuation) continuation;
                List<String> mfaOptions = mfaOptionsContinuation.getMfaOptions();
                selectMfaToSignIn(mfaOptions, continuation.getParameters());*/
            }
        }
    };

    /*private void AskCameraPermissions() {
        permissionHelper.check(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .withDialogBeforeRun(R.string.dialog_before_run_title, R.string.dialog_location_before_run_message, R.string.dialog_positive_button)
                .setDialogPositiveButtonColor(R.color.colorPrimary)
                .onSuccess(this::onSuccessProfile)
                .onDenied(this::onDeniedProfile)
                .onNeverAskAgain(this::onNeverAskAgainProfile)
                .run();
    }*/

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hide Status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        setContentView(R.layout.activity_login);
        AppHelper.init(getApplicationContext());
        //initialize the glympse SDK
//        if (loginPrefManager.getGlympseEnabled()){
//mirafre tech
//
//        }


        initApp();
        clearInput();
        onClickEvent();

        onServicerunning();

        if (loginPrefManager.getStringValue("lang_postion").equalsIgnoreCase("2")) {
            //Locale spanishLocale = new Locale("es", "ES");
            //countryCodePicker.changeDefaultLanguage(CountryCodePicker.Language.SPANISH);
        }

    }

    private void onServicerunning() {
//        if (isMyServiceRunning(IdleUpdatedService.class)) {
//            stopService(new Intent(LoginActivity.this, IdleUpdatedService.class));
//        }
        /*if (isMyServiceRunning(LiveTrackingService.class)) {
            stopService(new Intent(LoginActivity.this, LiveTrackingService.class));
        }*/
    }

    private void initApp() {

        edt_login_enter_email = findViewById(R.id.edt_login_enter_email);
        edt_login_enter_pwd = findViewById(R.id.edt_login_enter_pwd);
        tvForgotPwd = findViewById(R.id.tv_forgot_pwd);
        password_view = findViewById(R.id.password_view);
        //countryCodePicker = findViewById(R.id.country_code);
        AppUtils.turnGPSOn(LoginActivity.this);
        //gpsTracker = new GPSTracker(LoginActivity.this);
        mob_no_header = findViewById(R.id.mob_no_header);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void onClickEvent() {
        tvForgotPwd.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class)));

          edt_login_enter_pwd.setOnTouchListener((view, motionEvent) -> {
            edt_login_enter_pwd.setError(null);

            password_view.setEndIconTintMode(PorterDuff.Mode.MULTIPLY);
            return false;
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void clearInput() {
        edt_login_enter_email.setText("");
        edt_login_enter_email.requestFocus();
        edt_login_enter_pwd.setText("");
    }

    private void sendFCMtoken() {
        try {
            int batteryPercent = AppUtils.getBatteryPercentage(this);
            String deviceModel = AppUtils.getDeviceName();
            LocationUpdate locationUpdate = new LocationUpdate();
            locationUpdate.setFcmRefreshToken(loginPrefManager.getStringValue("device_token"));
            locationUpdate.setBatteryStatus(batteryPercent);
            locationUpdate.setDeviceModelName(deviceModel);
            locationUpdate.setDeviceType(0);
            double version = 1;
            try {
                PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                version = Double.parseDouble(pInfo.versionName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            locationUpdate.setTimezone(TimeZone.getDefault().getID());
            locationUpdate.setAppVersion(String.valueOf(version));
            locationUpdate.setDriverStatus(DeliforceConstants.DRIVER_STATUS_IDLE);

            CurrentLocation currentLocation = new CurrentLocation();
            currentLocation.setLattitude(current_latitude);
            currentLocation.setLongitude(current_longitude);


            /*if (current_latitude == 0.0) {
                currentLocation.setLattitude(gpsTracker.getLatitude());
                currentLocation.setLongitude(gpsTracker.getLongitude());
            }*/

            String formattedAddress = AppUtils.getCompleteAddressString(LoginActivity.this, currentLocation.getLattitude(), currentLocation.getLongitude());
            Timber.e("formatted_address ---%s", formattedAddress);

            if (formattedAddress.isEmpty()) {
                formattedAddress = AppUtils.getCompleteAddressString(LoginActivity.this, currentLocation.getLattitude(), currentLocation.getLongitude());
            }
            currentLocation.setAddress(formattedAddress);
            locationUpdate.setCurrentLocation(currentLocation);

            show_loader();


            apiService.sendManagerLogin(loginPrefManager.getCogintoToken(), locationUpdate).enqueue(new Callback<TokenRegister>() {
                @Override
                public void onResponse(@NotNull Call<TokenRegister> call, @NotNull Response<TokenRegister> response) {
                    dismiss_loader();
                    try {
                        if (response.code() == 201) {
                            show_error_response(getString(R.string.error_block_user));
                        } else if (response.code() == 200) {
                            List<String> adminlist = response.body().getAdminList();
                            Set admin_list = new HashSet(adminlist);



                            managerId = response.body().getManagerId();

                            loginPrefManager.setAdminList(admin_list);

                            loginPrefManager.setStringValue("user", AppHelper.getCurrUser());

                            loginPrefManager.setIsGlympseEnabled("isGlympse", response.body().getGlympseEnable());

                            //glympse registeration
                            //while login add this in aws login screen
                            if (loginPrefManager.getGlympseEnabled()) {
                                if (response.body().getUser() != null) {
                                    glympseUserName = response.body().getUser().getUsername();
                                    glympsePassword = response.body().getUser().getPassword();

                                    loginPrefManager.setGlympseUserName("glympseUserName", glympseUserName);
                                    loginPrefManager.setGlympseUserPswd("glympsePassword", glympsePassword);
                                    launchUser();
                                } else {
                                    showShortMessage("Missing glympse user details");
                                }
                                // The Glympse En Route Manager should be started right away. If there are no stored credentials,
                                // the EnRouteEvents.ENROUTE_MANAGER_AUTHENTICATION_NEEDED event will be fired. If there are stored
                                // credentials, EnRouteEvents.ENROUTE_MANAGER_LOGIN_COMPLETED and EnRouteEvents.ENROUTE_MANAGER_SYNCED
                                // will be fired.
//                                startManager();
                            } else {
                                launchUser();
                            }
                        }
                        //alert dialog for user already logged in
                        else if (response.code() == 494) {
                            showAlertDialog(LoginActivity.this);
                        } else {
                            show_error_response(getString(R.string.error_response));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<TokenRegister> call, @NotNull Throwable t) {
                    dismiss_loader();
                    Timber.e(t);
                    showShortMessage(t.getMessage());
                }
            });

        } catch (Exception e) {
            dismiss_loader();
            Timber.e(e);
        }
    }


    private void showDialogMessage(String title, String body) {

        showShortMessage(body);

    }

    private void closeWaitDialog() {
        try {
            loginAlertDailog.dismiss();
        } catch (Exception e) {
            //
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getUserAuthentication(AuthenticationContinuation continuation, String username) {
        if (username != null) {
            this.username = username;
            AppHelper.setUser(username);
        }
        if (this.password == null) {
            password = edt_login_enter_pwd.getText().toString();
            if (password == null) {
                edt_login_enter_pwd.setError(edt_login_enter_pwd.getHint() + " enter password");
                return;
            }

            if (password.length() < 1) {
                edt_login_enter_pwd.setError(edt_login_enter_pwd.getHint() + " enter password");
                return;
            }
        }

        Log.e("received_emailpswd",this.username+",,"+password);
        AuthenticationDetails authenticationDetails = new AuthenticationDetails(this.username, password, null);
        continuation.setAuthenticationDetails(authenticationDetails);
        continuation.continueTask();
    }

    private void launchUser() {

        loginPrefManager.setMangerID(managerId);

        loginPrefManager.setSkipversion(false);
        Intent userActivity = new Intent(this, NavigationActivity.class);
        userActivity.putExtra("name", username);
        startActivityForResult(userActivity, 4);
        finish();

    }

    private void mfaAuth(MultiFactorAuthenticationContinuation continuation) {
        //Continuations
        MultiFactorAuthenticationContinuation multiFactorAuthenticationContinuation = continuation;
        Intent mfaActivity = new Intent(this, MFAActivity.class);
        mfaActivity.putExtra("mode", multiFactorAuthenticationContinuation.getParameters().getDeliveryMedium());
        startActivityForResult(mfaActivity, 5);
    }


    // Register user - start process

    // Login if a user is already present
    public void logIn(View view) {
        signInUser();
    }

    // Forgot password processing

    @SuppressLint("NewApi")
    private void signInUser() {
        username = edt_login_enter_email.getText().toString().trim();
        Timber.e("" + username);
    //    Timber.e("-----" + countryCodePicker.getSelectedCountryCodeWithPlus());

       // loginPrefManager.setStringValue("selected_country", "" + countryCodePicker.getSelectedCountryCodeWithPlus());
        loginPrefManager.setStringValue("driverUserName", username);
        if (username == null || username.length() < 1) {
            edt_login_enter_email.setError(getString(R.string.error_mobile));
            edt_login_enter_email.requestFocus();
            return;
        }

        AppHelper.setUser(username);

        password = edt_login_enter_pwd.getText().toString();
        loginPrefManager.setStringValue("driverPassword", password);
        if (password == null || password.length() < 1) {
            edt_login_enter_pwd.setError(getString(R.string.empty_password));
            edt_login_enter_pwd.requestFocus();
            password_view.setEndIconTintMode(PorterDuff.Mode.CLEAR);
            return;
        }

        //showWaitDialog("Signing in...");

        show_loader();


        Log.e("received_email",username);

        //selected country code should show
        AppHelper.getPool().getUser(username).getSessionInBackground(authenticationHandler);


    }


    @Override
    public void onBackPressed() {

        ActivityCompat.finishAffinity(this);

        finish();

        super.onBackPressed();
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


    @Override
    protected void onStart() {
        super.onStart();


    }


    @Override
    public void onPause() {
        super.onPause();
//        if (loginPrefManager.getGlympseEnabled()) {
//            EnRouteWrapper.instance().manager().setActive(false);
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (loginPrefManager.getGlympseEnabled()) {
//            EnRouteWrapper.instance().manager().setActive(true);
//        }
    }

//    private void startManager()
//    {
//        show_loader();
//
//        GEnRouteManager manager = EnRouteWrapper.instance().manager();
//        manager.overrideLoggingLevels(CC.NONE, CC.INFO);
//        manager.addListener(this);
//        manager.setAuthenticationMode(EnRouteConstants.AUTH_MODE_CREDENTIALS);
//        manager.start();
//    }
//
//    /**
//     * GListener section
//     */
//    @Override
//    public void eventsOccurred(GSource gSource, int listener, int events, Object param1, Object o1) {
//        if ( EE.LISTENER_ENROUTE_MANAGER == listener )
//        {
//            if ( 0 != ( EE.ENROUTE_MANAGER_STARTED & events ) )
//            {
//            }
//            if ( 0 != ( EE.ENROUTE_MANAGER_AUTHENTICATION_NEEDED & events ) )
//            {
//                System.out.print("En Route Event: ENROUTE_MANAGER_AUTHENTICATION_NEEDED");
//
//                Log.e("Login_ENROUTE_MANAGE","ENROUTE_MANAGER_AUTHENTICATION_NEEDED");
//
//                // We need to wait for the user to login.
//                authenticationNeeded();
//            }
//            if ( 0 != ( EE.ENROUTE_MANAGER_LOGIN_COMPLETED & events ) )
//
//            {
//                Log.e("LOGIN_LOGIN_COMPLETED","ENROUTE_MANAGER_LOGIN_COMPLETED");
//
//                launchUser();
//                System.out.print("En Route Event: ENROUTE_MANAGER_LOGIN_COMPLETED");
//            }
//            if ( 0 != ( EE.ENROUTE_MANAGER_SYNCED & events ) )
//            {
//                Log.e("Login_MANAGER_SYNCED","ENROUTE_MANAGER_SYNCED");
//
//                System.out.print("En Route Event: ENROUTE_MANAGER_SYNCED");
//
//                // Either we had stored credentials for the user, or the user provided valid credentials
//                // The EnRouteManager is now fully intialized and ready to be used.
////                managerSynced();
//            }
//            if ( 0 != ( EE.ENROUTE_MANAGER_LOGGED_OUT & events ) )
//            {
//
//                Log.e("LOGGED_OUT", "ENROUTE_MANAGER_LOGGED_OUT");
//                // Either the user logged out manually ()
//                // (reason == EnRouteConstants.LOGOUT_REASON_USER_ACTION),
//                // or entered invalid credentials
//                // (reason == EnRouteConstants.LOGOUT_REASON_INVALID_CREDENTIALS).
////                long reason = ((Long)param1).longValue();
////                loggedOut(reason);
//            }
//            if ( 0 != ( EE.ENROUTE_MANAGER_STOPPED & events ) )
//            {
//                stopped();
//            }
//        }
//    }
//    public void loggedOut(long reason)
//    {
//        showInvalidCredentialsDialog();
//    }
//
//    private void authenticationNeeded() {
//        dismiss_loader();
//        Log.e("glypseUserrName",loginPrefManager.getGlympseUserName()+","+loginPrefManager.getGlympseUserPswd());
//        EnRouteWrapper.instance().manager().loginWithCredentials(loginPrefManager.getGlympseUserName(),
//                loginPrefManager.getGlympseUserPswd());
//
//    }
//
//    public void stopped()
//    {
//        dismiss_loader();
//
//        startManager();
//    }
//
//
//
//    private void showInvalidCredentialsDialog()
//    {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Invalid Credentials");
//        builder.setMessage("Please try again.");
//        builder.setCancelable(true);
//        builder.setPositiveButton("Ok", (dialog, id) -> {
////            dialog.dismiss();
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }
//    public void managerSynced()
//    {
//        dismiss_loader();
//        launchUser();
//    }

}