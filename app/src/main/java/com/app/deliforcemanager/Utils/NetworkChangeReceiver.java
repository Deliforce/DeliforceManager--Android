package com.app.deliforcemanager.Utils;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


public class NetworkChangeReceiver extends BroadcastReceiver {

    private AlertDialog.Builder alertDialogBuilder = null;
    private AlertDialog networkAlertDialog = null;

    private static boolean firstConnect = true;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() != null && intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {


            if (intent.getExtras() != null) {
                NetworkInfo ni = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
                if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
                    Log.e("myLogs", "Network " + ni.getTypeName() + " connected");
                } else {
                    Log.e("myLogs", "Network " + ni.getTypeName() + " connected");
                }
            }

            if (intent.getExtras().getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                Log.e("myLogs", "There's no network connectivity");
            }
        }
    }
}
