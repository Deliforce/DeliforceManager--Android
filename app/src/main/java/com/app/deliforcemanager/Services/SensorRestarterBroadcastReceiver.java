package com.app.deliforcemanager.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.app.deliforcemanager.AppController.DeliForce;
import com.app.deliforcemanager.Utils.LoginPrefManager;

import timber.log.Timber;

/**
 * Created by fabio on 24/01/2016.
 */
public class SensorRestarterBroadcastReceiver extends BroadcastReceiver {

    LoginPrefManager loginPrefManager;


    @Override
    public void onReceive(Context context, Intent intent) {
        Timber.e(SensorRestarterBroadcastReceiver.class.getSimpleName(), "Service Stopped!!!!! Restarting again");

        try {
            String action = intent.getAction();
            System.out.println(action);
        } catch (Exception e) {
            e.printStackTrace();
        }

        loginPrefManager = new LoginPrefManager(DeliForce.getContext());
//        if (!loginPrefManager.getDriverID().equals("")) {
//            if (!loginPrefManager.getStringValue("driver_status").equals("3")) {
//                try {
//                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
//                        context.startService(new Intent(context, IdleUpdatedService.class));
//                    } else {
//                        context.startForegroundService(new Intent(context, IdleUpdatedService.class));
//                    }
//                    Timber.v("Restarted Idle Service");
//                } catch (Exception e) {
//                    Timber.e("Exception%s", e.getMessage());
//                }
//            }
//        }
        Timber.e("Driver id empty and stopping");
    }

}
