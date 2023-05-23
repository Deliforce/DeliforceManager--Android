package com.app.deliforcemanager.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.app.deliforcemanager.LocalizationActivity.LocalizationActivity;
import com.app.deliforcemanager.ModelClass.TaskDetailsApi.OTPTaskUpdate;
import com.app.deliforcemanager.R;
import com.google.gson.Gson;

public class TripFareDetailsActivity extends LocalizationActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fare_calculation);

        inittoolbar();

        if (getIntent() != null) {
            String jsonData = getIntent().getStringExtra("jsonData");
            initViews(jsonData);
        } else
            initViews("");
    }

    private void inittoolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_fare_screen);
        TextView toolbar_text = findViewById(R.id.toolbar_title);

        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        toolbar_text.setText(getString(R.string.title_fare_details));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        toolbar.setNavigationOnClickListener(view -> {
            startActivity(new Intent(TripFareDetailsActivity.this, NavigationActivity.class));
            finish();
        });
    }

    private void initViews(String jsonData) {
        TextView txt_pickup_address = findViewById(R.id.txt_fare_screen_pickup_address);
        TextView txt_fare_screen_drop_address = findViewById(R.id.txt_fare_screen_drop_address);
        TextView txt_fare_screen_distance_trav = findViewById(R.id.txt_fare_screen_distance_trav);
        TextView txt_fare_screen_tavel_time = findViewById(R.id.txt_fare_screen_tavel_time);
        TextView txt_fare_screen_datetime = findViewById(R.id.txt_fare_screen_datetime);
        TextView txt_fare_screen_amount = findViewById(R.id.txt_fare_screen_amount);

        TextView txt_back_to_tasklist = findViewById(R.id.txt_back_to_tasklist);
        txt_back_to_tasklist.setOnClickListener(v -> {
            startActivity(new Intent(TripFareDetailsActivity.this, NavigationActivity.class));
            finish();
        });

        try {
            if (jsonData != null && !jsonData.equals("") && jsonData.contains("{")) {
                Gson gson = new Gson();
                OTPTaskUpdate otpTaskUpdate = gson.fromJson(jsonData, OTPTaskUpdate.class);
                txt_pickup_address.setText(otpTaskUpdate.getPickUpAddress().replaceAll("\n", ""));
                txt_fare_screen_drop_address.setText(otpTaskUpdate.getDeliveryAddress().replaceAll("\n", ""));
                txt_fare_screen_distance_trav.setText(otpTaskUpdate.getDistanceTravelled()+" "+otpTaskUpdate.getDistance());

                txt_fare_screen_tavel_time.setText(otpTaskUpdate.getTravelTime() + " min(s)");
                txt_fare_screen_datetime.setText(otpTaskUpdate.getActualStartedTime() + " - " + otpTaskUpdate.getActualCompletedTime());
                txt_fare_screen_amount.setText(otpTaskUpdate.getAmount() + " " + loginPrefManager.getCurrency());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
