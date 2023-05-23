package com.app.deliforcemanager.BaseActivity;

import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.app.deliforcemanager.LocalizationActivity.LocalizationActivity;
import com.app.deliforcemanager.R;


public class BaseActivity extends LocalizationActivity {

    public Toolbar toolbar;
    public TextView tvTitle;

    /*private CognitoAccessToken awsAccessToken;
    private CognitoIdToken cognitoIdToken;
    private CognitoRefreshToken cognitoRefreshToken;
    private String cognitoToken, awsIdToken, awsRefreshToken;
    private CognitoUser user;
    //Continuations
    private MultiFactorAuthenticationContinuation multiFactorAuthenticationContinuation;
    private ForgotPasswordContinuation forgotPasswordContinuation;
    private NewPasswordContinuation newPasswordContinuation;
    private ChooseMfaContinuation mfaOptionsContinuation;

    // User Details
    private String username;
    private String password;*/

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        bindViews();
    }

    protected void bindViews() {
        toolbar = findViewById(R.id.toolbar);
        tvTitle = findViewById(R.id.toolbar_title);
        setupToolbar();
    }

    public void setContentViewWithoutInject(int layoutResId) {
        super.setContentView(layoutResId);
    }

    protected void setupToolbar() {
        if (toolbar != null) {
            /* for centering the toolbar text */
            toolbar.setContentInsetsAbsolute(toolbar.getContentInsetLeft(), 100);
            setSupportActionBar(toolbar);
            /* setting navgation */
            toolbar.setNavigationIcon(R.drawable.menu);

            getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolbar.getNavigationIcon().setAutoMirrored(true);


        }
    }

    public Toolbar getToolbar() {
        return toolbar;
    }


//    AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
//        @Override
//        public void onSuccess(CognitoUserSession cognitoUserSession, CognitoDevice device) {
//            Log.d("TAG", " -- Auth Success");
//            AppHelper.setCurrSession(cognitoUserSession);
//            AppHelper.newDevice(device);
//            //closeWaitDialog();
//
//            awsAccessToken = cognitoUserSession.getAccessToken();
//            cognitoToken = awsAccessToken.getJWTToken();
//            cognitoIdToken = cognitoUserSession.getIdToken();
//            awsIdToken = cognitoIdToken.getJWTToken();
//            Log.e("TAG", "AWS Id Token--> " + awsIdToken);
//            cognitoRefreshToken = cognitoUserSession.getRefreshToken();
//            awsRefreshToken = cognitoRefreshToken.getToken();
////            postRefreshToken(currentLat, currentLong);
//
//            loginPrefManager.setAccessToken("" + cognitoToken);
//            loginPrefManager.setCogintoToken("" + awsIdToken);
//            loginPrefManager.setRefreshTokenToken("" + awsRefreshToken);
//
//
//            //sendFCMtoken();
//
//        }
//
//        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//        @Override
//        public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String username) {
//            //closeWaitDialog();
//            Locale.setDefault(Locale.US);
//            //getUserAuthentication(authenticationContinuation, username);
//        }
//
//        @Override
//        public void getMFACode(MultiFactorAuthenticationContinuation multiFactorAuthenticationContinuation) {
//            //closeWaitDialog();
//            mfaAuth(multiFactorAuthenticationContinuation);
//        }
//
//        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//        @Override
//        public void onFailure(Exception e) {
//            //closeWaitDialog();
////            TextView label = findViewById(R.id.textViewUserIdMessage);
////            edt_login_enter_pwd.setError("Sign-in failed");
////            edt_login_enter_pwd.setBackground(getDrawable(R.drawable.text_border_error));
////
//////            label = findViewById(R.id.textViewUserIdMessage);
////            edt_login_enter_pwd.setError("Sign-in failed");
////            edt_login_enter_email.setBackground(getDrawable(R.drawable.text_border_error));
//
//            //showDialogMessage("Sign-in failed", AppHelper.formatException(e));
//        }
//
//        @Override
//        public void authenticationChallenge(ChallengeContinuation continuation) {
//            if ("NEW_PASSWORD_REQUIRED".equals(continuation.getChallengeName())) {
//                // This is the first sign-in attempt for an admin created user
//                newPasswordContinuation = (NewPasswordContinuation) continuation;
//                AppHelper.setUserAttributeForDisplayFirstLogIn(newPasswordContinuation.getCurrentUserAttributes(),
//                        newPasswordContinuation.getRequiredAttributes());
//                //closeWaitDialog();
//            } else if ("SELECT_MFA_TYPE".equals(continuation.getChallengeName())) {
//                //closeWaitDialog();
//                mfaOptionsContinuation = (ChooseMfaContinuation) continuation;
//                List<String> mfaOptions = mfaOptionsContinuation.getMfaOptions();
//                //selectMfaToSignIn(mfaOptions, continuation.getParameters());
//            }
//        }
//    };
//
//    private void findCurrent() {
//        user = AppHelper.getPool().getCurrentUser();
//        username = user.getUserId();
//        if (username != null) {
//            AppHelper.setUser(username);
//            //edt_login_enter_email.setText(user.getUserId());
//            user.getSessionInBackground(authenticationHandler);
//        }
//    }
//
//   */
//    private void getUserAuthentication(AuthenticationContinuation continuation, String username) {
//        if (username != null) {
//            this.username = username;
//            AppHelper.setUser(username);
//        }
//        if (this.password == null) {
//            edtEnterPwd.setText(username);
//            password = edtEnterPwd.getText().toString();
//            if (password == null) {
//                edtEnterPwd.setError(getResources().getString(R.string.err_enter_pwd));
//                return;
//            }
//
//            if (password.length() < 1) {
//                edtEnterPwd.setError(getResources().getString(R.string.err_enter_pwd));
//                return;
//            }
//        }
//        AuthenticationDetails authenticationDetails = new AuthenticationDetails(this.username, password, null);
//        continuation.setAuthenticationDetails(authenticationDetails);
//        continuation.continueTask();
//    }
//
//    private void mfaAuth(MultiFactorAuthenticationContinuation continuation) {
//        multiFactorAuthenticationContinuation = continuation;
//    }

}
