package com.app.deliforcemanager.CustomDialogView;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.app.deliforcemanager.R;

public class AlertDialogOTP extends Dialog {

    private final String otpCodeVer;
    private Context context;
    private EditText edt_otp1, edt_otp2, edt_otp3, edt_otp4;
    BlockedInterface blockedInterface;
    private TextView txt_didnt_get_otp;
    private Button btn_cancel_otp, btn_submit_otp;


    public AlertDialogOTP(Context context, int themeResId, String otpCode, BlockedInterface blockedInterface) {
        super(context, themeResId);
        this.context = context;
        this.blockedInterface = blockedInterface;
        this.otpCodeVer = otpCode;
    }

    @RequiresApi(api = 28)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.activity_otp_verification);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;

        edt_otp1 = findViewById(R.id.edt_otp1);
        edt_otp2 = findViewById(R.id.edt_otp2);
        edt_otp3 = findViewById(R.id.edt_otp3);
        edt_otp4 = findViewById(R.id.edt_otp4);

        edt_otp1.addTextChangedListener(new GenericTextWatcher(edt_otp1));
        edt_otp2.addTextChangedListener(new GenericTextWatcher(edt_otp2));
        edt_otp3.addTextChangedListener(new GenericTextWatcher(edt_otp3));
        edt_otp4.addTextChangedListener(new GenericTextWatcher(edt_otp4));

        txt_didnt_get_otp = findViewById(R.id.txt_didnt_get_otp);
        txt_didnt_get_otp.setOnClickListener(v -> {
            if (blockedInterface != null) {
                blockedInterface.resendOTP();
            }
        });

        btn_cancel_otp = findViewById(R.id.btn_cancel_otp);
        btn_submit_otp = findViewById(R.id.btn_submit_otp);

        onClickevents();

    }

    private class GenericTextWatcher implements TextWatcher {
        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            switch (view.getId()) {

                case R.id.edt_otp1:
                    if (text.length() == 1)
                        edt_otp2.requestFocus();
                    break;
                case R.id.edt_otp2:
                    if (text.length() == 1)
                        edt_otp3.requestFocus();
                    /*else if (text.length() == 0)
                        edt_otp1.requestFocus();*/
                    break;
                case R.id.edt_otp3:
                    if (text.length() == 1)
                        edt_otp4.requestFocus();
                    /*else if (text.length() == 0)
                        edt_otp2.requestFocus();*/
                    break;
                case R.id.edt_otp4:
                    /*if (text.length() == 0)
                        edt_otp3.requestFocus();*/
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }
    }

    private void onClickevents() {
        btn_cancel_otp.setOnClickListener(view -> {
            dismiss();
            if (blockedInterface != null) {
                blockedInterface.onclickOTP(false, 0);
            }
        });
        btn_submit_otp.setOnClickListener(view -> {
            if (edt_otp1.getText().toString().trim().length() > 0 && edt_otp2.getText().toString().trim().length() > 0
                    && edt_otp3.getText().toString().trim().length() > 0 && edt_otp4.getText().toString().trim().length() > 0) {

                if (blockedInterface != null) {
                    String otpCode = edt_otp1.getText().toString() + edt_otp2.getText().toString() + edt_otp3.getText().toString() + edt_otp4.getText().toString();
                    if (otpCode.equals(otpCodeVer)) {
                        blockedInterface.onclickOTP(true, Integer.parseInt(otpCode));
                        dismiss();
                    } else {
                        Toast toast = Toast.makeText(context, "Invalid OTP Code", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }
            } else
                Toast.makeText(context, "All fields are mandatory", Toast.LENGTH_SHORT).show();
        });
    }

    public interface BlockedInterface {
        void onclickOTP(boolean status, int otpCode);

        void resendOTP();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
