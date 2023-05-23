package com.app.deliforcemanager.CustomDialogView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.app.deliforcemanager.R;
import com.app.deliforcemanager.Utils.LoginPrefManager;

import java.util.Objects;

public class UpdateAppDialog extends Dialog {

    private Context context;
    private LoginPrefManager loginPrefManager;
    private Button btn_update, btn_skip;
    private boolean app_status;

    public UpdateAppDialog(@NonNull Context context, int themeResId, boolean app_status) {
        super(context, themeResId);
        this.context = context;
        this.app_status = app_status;
        loginPrefManager = new LoginPrefManager(context);
    }

    @RequiresApi(api = 28)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.app_update);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;


        btn_update = findViewById(R.id.btn_update);
        btn_skip = findViewById(R.id.btn_skip);
        TextView msg_txt = findViewById(R.id.msg_txt);

        if (app_status) {
            btn_skip.setVisibility(View.GONE);
            findViewById(R.id.skip_view).setVisibility(View.GONE);
            msg_txt.setText(context.getString(R.string.mandatory_app_update));
        } else {
            msg_txt.setText(context.getString(R.string.optional_update));
        }
        onClickevents();

    }

    private void onClickevents() {
        btn_skip.setOnClickListener(v -> {
            dismiss();
            loginPrefManager.setSkipversion(true);
        });

        btn_update.setOnClickListener(v -> {
            dismiss();
            final String appPackageName = context.getPackageName(); // package name of the app
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                ((Activity) context).finish();
            } catch (android.content.ActivityNotFoundException anfe) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                ((Activity) context).finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}
