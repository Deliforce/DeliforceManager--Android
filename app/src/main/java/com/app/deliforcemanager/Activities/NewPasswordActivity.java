package com.app.deliforcemanager.Activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import com.app.deliforcemanager.LocalizationActivity.LocalizationActivity;
import com.app.deliforcemanager.ModelClass.updatepasswordtobackend.UpdatePassword;
import com.app.deliforcemanager.R;
import com.app.deliforcemanager.RawHeaders.UpdationPassword;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPasswordActivity extends LocalizationActivity {

    EditText verification_code, edt_new_pwd;
    Button btn_send, btn_cancel;
    TextInputLayout password_view;
    TextView toolbar_title;
    Toolbar toolbar;
    String phoneNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_new_password);


        init();
    }

    private void init() {
        verification_code = findViewById(R.id.verification_code);
        edt_new_pwd = findViewById(R.id.edt_new_pwd);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_send = findViewById(R.id.btn_send);
        password_view = findViewById(R.id.verify_password);

        toolbar = findViewById(R.id.toolbar);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(getString(R.string.new_password_txt));

        toolbar_title.setGravity(GravityCompat.START);

        onClickevents();
        toolbar.setVisibility(View.GONE);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("destination")) {
                String dest = extras.getString("destination");
                String delMed = extras.getString("deliveryMed");
                 phoneNumber = extras.getString("phone_number");
//                String textToDisplay = "Code to set a new password was sent to " + dest + " via "+delMed;

                showShortMessage(getString(R.string.verify_pass) + dest + getString(R.string.via) + delMed);

            }
        }

    }

    private void onClickevents() {

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validVerification()) {
                    return;
                }
                if (!validPassword()) {
                    return;
                }

                apiCallForUpdatePassword();




            }
        });

        edt_new_pwd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                edt_new_pwd.setError(null);

                password_view.setEndIconTintMode(PorterDuff.Mode.MULTIPLY);
                return false;
            }
        });

    }

    /*create model class and hit the api to update the pswd*/
    private void apiCallForUpdatePassword() {

        try {
            UpdationPassword updationPassword=new UpdationPassword();
            updationPassword.setPhone(""+phoneNumber);
            updationPassword.setNewPassword(edt_new_pwd.getText().toString());

            apiService.updatePasswordToServer(updationPassword).enqueue(new Callback<UpdatePassword>() {
                @Override
                public void onResponse(@NotNull Call<UpdatePassword> call, @NotNull Response<UpdatePassword> response) {
                    //Log.e("response",""+response.raw().code());
                    try {
                        if (response.raw().code()==200) {
                            finishActivity(edt_new_pwd.getText().toString(), verification_code.getText().toString());
                        } else if (response.raw().code() == 401) {
                            findCurrent();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<UpdatePassword> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validVerification() {

        String verification = verification_code.getText().toString();

        if (verification.isEmpty() || verification.equals("")) {

            verification_code.setError(getString(R.string.error_verification_code));
            verification_code.requestFocus();
            return false;
        }


        return true;
    }

    private boolean validPassword() {
        String password = edt_new_pwd.getText().toString();

        if (password.isEmpty() || password.equals("")) {
            password_view.setEndIconTintMode(PorterDuff.Mode.CLEAR);
            edt_new_pwd.setError(getString(R.string.empty_password));
            edt_new_pwd.requestFocus();
            return false;
        } else if (password.length() < 6) {
            password_view.setEndIconTintMode(PorterDuff.Mode.CLEAR);
            edt_new_pwd.setError(getString(R.string.password_length));
            edt_new_pwd.requestFocus();
            return false;
        }
        return true;
    }

    private void finishActivity(String newPass, String code) {
        Intent intent = new Intent();
        if (newPass == null || code == null) {
            newPass = "";
            code = "";
        }
        intent.putExtra("newPass", newPass);
        intent.putExtra("code", code);
        setResult(RESULT_OK, intent);
        finish();
    }


}
