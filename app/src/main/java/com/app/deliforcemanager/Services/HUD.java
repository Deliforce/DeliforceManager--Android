package com.app.deliforcemanager.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.app.deliforcemanager.Activities.TaskDetailsActivity;
import com.app.deliforcemanager.R;
import com.app.deliforcemanager.Utils.LoginPrefManager;


public class HUD extends Service {
    View mView;
    LayoutInflater inflate;
    TextView t;
    Button b;
    LoginPrefManager loginPrefManager;
    WindowManager wm;
    Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        loginPrefManager = new LoginPrefManager(getBaseContext());

        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.LEFT | Gravity.CENTER;
        params.setTitle("Load Average");

        inflate = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mView = inflate.inflate(R.layout.tracking_over_lay, null);

        b = mView.findViewById(R.id.button1);
        b.setOnClickListener(v -> {

            HUD.this.stopSelf();

            if (mView != null) {
                ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(mView);
                mView = null;
            }
            stopService(new Intent(getBaseContext(), HUD.class));
            Intent in = new Intent(getBaseContext(), TaskDetailsActivity.class);
            in.putExtra("task_id", loginPrefManager.getStringValue("task_id"));
            in.putExtra("glympse_id", loginPrefManager.getStringValue("GlympseID"));
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
        });
        wm.addView(mView, params);
    }


    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mView != null) {
            ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(mView);
            mView = null;
        }
    }
}