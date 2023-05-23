package com.app.deliforcemanager.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class NetworkStatus {

    private static final int TYPE_WIFI = 1;
    private static final int TYPE_MOBILE = 2;
    private static final int TYPE_NOT_CONNECTED = 0;


    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (NetworkInfo anInfo : info)
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    private static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = NetworkStatus.getConnectivityStatus(context);
        String status = null;
        if (conn == NetworkStatus.TYPE_WIFI) {
            status = "Wifi enabled";
        } else if (conn == NetworkStatus.TYPE_MOBILE) {
            status = "Mobile data enabled";
        } else if (conn == NetworkStatus.TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
        }
        return status;
    }

    public static int getConnectivityStatusInt(Context context) {
        int conn = NetworkStatus.getConnectivityStatus(context);
        int status = 0;
        if (conn == NetworkStatus.TYPE_WIFI) {
            status = 1;
        } else if (conn == NetworkStatus.TYPE_MOBILE) {
            status = 1;
        } else if (conn == NetworkStatus.TYPE_NOT_CONNECTED) {
            status = 0;
        }
        return status;
    }


    public static boolean getConnectivityStatusBoolean(Context context){
        ConnectivityManager conect_manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = conect_manager.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null && activeNetwork.isConnectedOrConnecting());

        return isConnected;
//        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

}
