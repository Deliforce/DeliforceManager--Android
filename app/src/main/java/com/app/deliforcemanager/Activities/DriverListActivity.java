package com.app.deliforcemanager.Activities;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.app.deliforcemanager.LocalizationActivity.LocalizationActivity;
import com.app.deliforcemanager.R;

public class DriverListActivity extends LocalizationActivity {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driverlist);
        initToolbar();
    }

    private void initToolbar() {
       Toolbar toolbar = findViewById(R.id.toolbar);
        TextView toolbar_text = findViewById(R.id.toolbar_title);

        toolbar_text.setText("Drivers");


        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        toolbar.setNavigationOnClickListener(v -> {
           onBackPressed();
           finish();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
