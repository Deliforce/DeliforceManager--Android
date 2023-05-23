package com.app.deliforcemanager.CustomDialogView;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.app.deliforcemanager.R;

import java.util.Objects;

public class LoginAlertDailog extends Dialog {

    private String type;

    public LoginAlertDailog(@NonNull Context context, int themeResId, String type) {
        super(context, themeResId);
        this.type = type;
    }

    @RequiresApi(api = 28)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.login_alert_dailog);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;

        TextView msg_txt = findViewById(R.id.msg_txt);

        msg_txt.setText(type);

        onClickevents();
    }

    private void onClickevents() {
        Button update_btn = findViewById(R.id.btn_update);
        update_btn.setOnClickListener(view -> dismiss());

        /*update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });*/
    }


}
