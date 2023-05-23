package com.app.deliforcemanager.Utils;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

public class NetworkAvailability {

    private static final String NETWORK_AVAILABILITY_ACTION = "com.deliforce.NETWORK_AVAILABILITY_ACTION";

    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallback;

    private static NetworkAvailability instance;

    private NetworkAvailability() {
    }

    public static NetworkAvailability getInstance() {
        if (instance == null) {
            instance = new NetworkAvailability();
        }
        return instance;
    }

    /*private static boolean isAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }*/

    public void registerNetworkAvailability(final Context context, BroadcastReceiver networkAvailabilityReceiver) {

        context.registerReceiver(networkAvailabilityReceiver, new IntentFilter(NETWORK_AVAILABILITY_ACTION));

        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager!=null) {
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            networkCallback = new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(@NotNull Network network) {
                    context.sendBroadcast(getNetworkAvailabilityIntent(true));
                }

                @Override
                public void onLost(@NotNull Network network) {
                    context.sendBroadcast(getNetworkAvailabilityIntent(false));
                }
            };
            connectivityManager.registerNetworkCallback(builder.build(), networkCallback);
        }
    }

    public void unregisterNetworkAvailability(Context context, BroadcastReceiver networkAvailabilityReceiver) {
        if(networkCallback!=null)
            connectivityManager.unregisterNetworkCallback(networkCallback);
        context.unregisterReceiver(networkAvailabilityReceiver);
    }

    @NonNull
    private Intent getNetworkAvailabilityIntent(boolean isNetworkAvailable) {
        Intent intent = new Intent(NETWORK_AVAILABILITY_ACTION);
        intent.putExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, !isNetworkAvailable);
        return intent;
    }
}