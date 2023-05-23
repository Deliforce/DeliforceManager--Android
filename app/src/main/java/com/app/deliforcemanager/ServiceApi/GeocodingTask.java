package com.app.deliforcemanager.ServiceApi;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeocodingTask extends AsyncTask<String, Void, LatLng> {
    private Context context;
    private GeocodingListener listener;

    public GeocodingTask(Context context, GeocodingListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected LatLng doInBackground(String... params) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String addressString = params[0];

        try {
            List<Address> addresses = geocoder.getFromLocationName(addressString, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                double latitude = address.getLatitude();
                double longitude = address.getLongitude();
                return new LatLng(latitude, longitude);
            }
        } catch (IOException e) {
            Log.e("GeocodingTask", "Error geocoding address", e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(LatLng latLng) {
        if (latLng != null) {
            listener.onGeocodingSuccess(latLng);
        } else {
            listener.onGeocodingFailure();
        }
    }

    public interface GeocodingListener {
        void onGeocodingSuccess(LatLng latLng);

        void onGeocodingFailure();
    }
}
