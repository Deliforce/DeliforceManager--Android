package com.app.deliforcemanager.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.view.GravityCompat;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.app.deliforcemanager.BaseActivity.BaseDrawerActivity;
import com.app.deliforcemanager.Cognito.AppHelper;
import com.app.deliforcemanager.ModelClass.updatepasswordtobackend.UpdatePassword;
import com.app.deliforcemanager.R;
import com.app.deliforcemanager.RawHeaders.UpdationPassword;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class ChangePasswordActivity extends BaseDrawerActivity {

    EditText current_password, new_password, confirm_password;
    Button update_btn, cancel_btn;
    TextInputLayout current_pass_layout, new_pass_layout, confirm_pass_layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        inittoolbar();
        initView();

        findCurrent();
        checkdrawer();
    }

    private void checkdrawer() {
        getToolbar().setNavigationOnClickListener(view -> {
            current_password.setError(null);
            new_password.setError(null);
            confirm_password.setError(null);
            current_pass_layout.setEndIconTintMode(PorterDuff.Mode.MULTIPLY);
            new_pass_layout.setEndIconTintMode(PorterDuff.Mode.MULTIPLY);
            confirm_pass_layout.setEndIconTintMode(PorterDuff.Mode.MULTIPLY);
            mdrawerLayout.openDrawer(GravityCompat.START);
        });
    }

    private void initView() {

        current_password = findViewById(R.id.edt_current_pwd);
        new_password = findViewById(R.id.edt_new_pwd);
        confirm_password = findViewById(R.id.edt_retype_pwd);

        update_btn = findViewById(R.id.btn_update_pwd);
        cancel_btn = findViewById(R.id.btn_cancel_update_pwd);
        current_pass_layout = findViewById(R.id.old_pass);
        new_pass_layout = findViewById(R.id.new_pass_layout);
        confirm_pass_layout = findViewById(R.id.confirm_pass_layout);

        onClickevents();

    }

    private void inittoolbar() {
        tvTitle.setText(getString(R.string.change_password));
    }


    @SuppressLint("ClickableViewAccessibility")
    private void onClickevents() {
        update_btn.setOnClickListener(view -> {

            if (!validPasswordWithError(current_password, current_pass_layout) && !validPasswordWithError(new_password, new_pass_layout) && !validPasswordWithError(confirm_password, confirm_pass_layout)) {
                return;
            }
            if (!validPasswordWith(current_password, current_pass_layout)) {
                return;
            }
            if (!validPasswordWith(new_password, new_pass_layout)) {
                return;
            }
            if (!validretype(new_password, confirm_password, confirm_pass_layout)) {
                return;
            }
            Timber.e(AppHelper.getCurrUser());
            AppHelper.getPool().getUser(AppHelper.getCurrUser()).changePasswordInBackground(current_password.getText().toString(), new_password.getText().toString(), callback);
        });

        cancel_btn.setOnClickListener(view -> {
            startHomeScreen();
            finish();
        });
        current_password.setOnTouchListener((view, motionEvent) -> {
            current_password.setError(null);

            current_pass_layout.setEndIconTintMode(PorterDuff.Mode.MULTIPLY);
            return false;
        });
        new_password.setOnTouchListener((view, motionEvent) -> {
            new_password.setError(null);

            new_pass_layout.setEndIconTintMode(PorterDuff.Mode.MULTIPLY);
            return false;
        });
        confirm_password.setOnTouchListener((view, motionEvent) -> {
            confirm_password.setError(null);

            confirm_pass_layout.setEndIconTintMode(PorterDuff.Mode.MULTIPLY);
            return false;
        });
    }

    GenericHandler callback = new GenericHandler() {
        @Override
        public void onSuccess() {
            apiCallForUpdatePassword();
        }

        @Override
        public void onFailure(Exception exception) {
            String msg = AppHelper.formatException(exception);

            Timber.e(AppHelper.formatException(exception) + "/" + msg);

            if (msg.contains("User-ID is null")) {
                msg = getString(R.string.error_old_password);
                showShortMessage(msg);
            } else if (msg.contains("Incorrect username or password")) {
                showShortMessage(getString(R.string.invalid_current_password));
            } else if (msg.contains("Attempt limit exceeded, please try after some time.")) {
                showShortMessage(getString(R.string.max_attempts));

            } else {
                showShortMessage(getString(R.string.password_fail_change) + msg);
            }
        }
    };

    /*create model class and hit the api to update the pswd*/
    private void apiCallForUpdatePassword() {
        try {
            show_loader();
            UpdationPassword updationPassword = new UpdationPassword();
            updationPassword.setPhone("" + loginPrefManager.getDriverMobileNumber());
            updationPassword.setNewPassword(new_password.getText().toString());

            apiService.updatePasswordToServer(updationPassword).enqueue(new Callback<UpdatePassword>() {
                @Override
                public void onResponse(@NotNull Call<UpdatePassword> call, @NotNull Response<UpdatePassword> response) {
                    dismiss_loader();
                    try {
                        if (response.raw().code() == 200) {
                            Toast.makeText(getApplicationContext(), getString(R.string.password_success), Toast.LENGTH_LONG).show();
                            startActivity(new Intent(ChangePasswordActivity.this, NavigationActivity.class));
                            finish();

                        } else if (response.raw().code() == 401) {
                            findCurrent();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<UpdatePassword> call, @NotNull Throwable t) {
                    Timber.e("onFailure %s", t.getMessage());
                    dismiss_loader();

                }
            });
        } catch (Exception e) {
            dismiss_loader();
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startHomeScreen();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private boolean validPasswordWith(EditText password_edit, TextInputLayout textInputLayout) {

        String password = password_edit.getText().toString();
        if (password.isEmpty()) {
            password_edit.setError(getString(R.string.error_password));
            password_edit.requestFocus();
            textInputLayout.setEndIconTintMode(PorterDuff.Mode.CLEAR);
            return false;
        } else if (!isValidPassword(password)) {
            password_edit.setError(getString(R.string.error_passowrd_pattern));
            password_edit.requestFocus();
            textInputLayout.setEndIconTintMode(PorterDuff.Mode.CLEAR);
            return false;
        } else {
            password_edit.setError(null);
        }

        return true;
    }

    private boolean validretype(EditText new_password, EditText confirm_password, TextInputLayout textInputLayout) {


        if (confirm_password.getText().toString().equalsIgnoreCase("")) {
            confirm_password.setError(getString(R.string.error_confirm));
            textInputLayout.setEndIconTintMode(PorterDuff.Mode.CLEAR);
            confirm_password.requestFocus();
            return false;
        } else if (!new_password.getText().toString().equals(confirm_password.getText().toString())) {

            confirm_password.requestFocus();
            confirm_password.setError(getString(R.string.error_confirm));
            textInputLayout.setEndIconTintMode(PorterDuff.Mode.CLEAR);

            return false;
        }


        return true;
    }

    public boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

}
