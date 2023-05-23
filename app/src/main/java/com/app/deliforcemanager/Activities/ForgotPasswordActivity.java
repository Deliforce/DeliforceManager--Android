package com.app.deliforcemanager.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler;
import com.app.deliforcemanager.Cognito.AppHelper;
import com.app.deliforcemanager.CustomDialogView.LoginAlertDailog;
import com.app.deliforcemanager.LocalizationActivity.LocalizationActivity;
import com.app.deliforcemanager.R;
import com.hbb20.CountryCodePicker;

import timber.log.Timber;

public class ForgotPasswordActivity extends LocalizationActivity {

    private EditText user_id;
    private Button cancle_btn, send_btn;
    private ForgotPasswordContinuation forgotPasswordContinuation;
    TextView toolbar_title;
    Toolbar toolbar;
    LoginAlertDailog loginAlertDailog;
    CountryCodePicker countryCodePicker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_forgot_password);


        AppHelper.init(getApplicationContext());

        init();

        initBroadcast();
    }

    private void initBroadcast() {

        BroadcastReceiver forgotPassword = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent != null) {
                    finish();
                }
            }
        };
        LocalBroadcastManager.getInstance(ForgotPasswordActivity.this).registerReceiver(forgotPassword, new IntentFilter("forgot_password"));
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(getString(R.string.title_forgot_pass));

        toolbar.setVisibility(View.GONE);

        toolbar_title.setGravity(Gravity.LEFT);
        user_id = findViewById(R.id.user_id);
        cancle_btn = findViewById(R.id.btn_cancel);
        send_btn = findViewById(R.id.btn_send);
        countryCodePicker = findViewById(R.id.country_code);

        onClickevents();
    }

    private void onClickevents() {

        send_btn.setOnClickListener(view -> {
            if (!validatePhone()) {
                return;
            }

            show_loader();
            //selected countryshould show here
            AppHelper.getPool().getUser(countryCodePicker.getSelectedCountryCodeWithPlus() + user_id.getText().toString()).forgotPasswordInBackground(forgotPasswordHandler);

        });
        cancle_btn.setOnClickListener(view -> finish());
    }

    private boolean validatePhone() {
        String user = user_id.getText().toString();
        if (user.isEmpty()) {
            user_id.setError(getString(R.string.error_mobile));
            user_id.requestFocus();
            return false;
        }
        AppHelper.setUser(user);
        return true;
    }

    ForgotPasswordHandler forgotPasswordHandler = new ForgotPasswordHandler() {
        @Override
        public void onSuccess() {
            Timber.e("onSuccess");

            user_id.setText("");
            dismiss_loader();

            showShortMessage(getString(R.string.success_password));

            finish();
            AppHelper.setUser("");
        }

        @Override
        public void getResetCode(ForgotPasswordContinuation forgotPasswordContinuation) {
            dismiss_loader();
            getForgotPasswordCode(forgotPasswordContinuation);


        }

        @Override
        public void onFailure(Exception e) {
            dismiss_loader();
            showDialogMessage(getString(R.string.password_fail), AppHelper.formatException(e));
        }
    };


    private void getForgotPasswordCode(ForgotPasswordContinuation forgotPasswordContinuation) {
        this.forgotPasswordContinuation = forgotPasswordContinuation;
        Intent intent = new Intent(this, NewPasswordActivity.class);
        intent.putExtra("destination", forgotPasswordContinuation.getParameters().getDestination());
        intent.putExtra("deliveryMed", forgotPasswordContinuation.getParameters().getDeliveryMedium());
        intent.putExtra("phone_number", "" + countryCodePicker.getSelectedCountryCodeWithPlus() + " " + user_id.getText().toString());
        startActivityForResult(intent, 3);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                String newPass = data.getStringExtra("newPass");
                String code = data.getStringExtra("code");
                if (newPass != null && code != null) {
                    if (!newPass.isEmpty() && !code.isEmpty()) {
                        show_loader();
                        if (forgotPasswordContinuation != null) {
                            forgotPasswordContinuation.setPassword(newPass);
                            forgotPasswordContinuation.setVerificationCode(code);
                            forgotPasswordContinuation.continueTask();
                        }
                    }
                }
            }
        }
    }

    private void showDialogMessage(String title, String body) {

        Timber.e("body", body);

        if (body.equalsIgnoreCase(getString(R.string.invalid_verification))) {
            body = getString(R.string.invalid_verification_spanish);
        }

        loginAlertDailog = new LoginAlertDailog(ForgotPasswordActivity.this, R.style.MyDialogStyle, body);
        loginAlertDailog.show();
    }

}
