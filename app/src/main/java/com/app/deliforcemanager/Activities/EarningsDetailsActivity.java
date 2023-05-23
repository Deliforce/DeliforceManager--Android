
package com.app.deliforcemanager.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.app.deliforcemanager.LocalizationActivity.LocalizationActivity;
import com.app.deliforcemanager.ModelClass.EarningsChart.EarningDetailsData;
import com.app.deliforcemanager.R;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EarningsDetailsActivity extends LocalizationActivity {

    private TextView toolbar_text;
    private String taskID;
    private TextView txt_earnings_details_time, txt_earnings_details_status, txt_earnings_details_amount, txt_earnings_details_id, txt_earnings_details_minutes;
    private TextView txt_earnings_details_basefare, txt_earnings_details_duration_fare, txt_earnings_details_distancefare, txt_earnings_details_waitingfare,
            txt_earnings_details_deductionfare,txt_tips;
    SimpleDateFormat ApiDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);

    SimpleDateFormat dateFormat =new SimpleDateFormat("dd MMMM",Locale.ENGLISH);
    SimpleDateFormat timeFormat =new SimpleDateFormat("hh:mm a",Locale.ENGLISH);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earnings_details);

        if (getIntent() != null) {
            taskID = Objects.requireNonNull(getIntent().getExtras()).getString("task_id", null);
        }

        initViews();
        requestEarningDetails();
    }

    private void initViews() {
        txt_earnings_details_time = findViewById(R.id.txt_earnings_details_time);
        txt_earnings_details_status = findViewById(R.id.txt_earnings_details_status);
        txt_earnings_details_amount = findViewById(R.id.txt_earnings_details_amount);
        txt_earnings_details_id = findViewById(R.id.txt_earnings_details_id);
        txt_earnings_details_minutes = findViewById(R.id.txt_earnings_details_minutes);
        txt_earnings_details_basefare = findViewById(R.id.txt_earnings_details_basefare);
        txt_earnings_details_duration_fare = findViewById(R.id.txt_earnings_details_duration_fare);
        txt_earnings_details_distancefare = findViewById(R.id.txt_earnings_details_distancefare);
        txt_earnings_details_waitingfare = findViewById(R.id.txt_earnings_details_waitingfare);
        txt_earnings_details_deductionfare = findViewById(R.id.txt_earnings_details_deductionfare);
        txt_tips = findViewById(R.id.txt_tips);
    }

    private void requestEarningDetails() {
//        EarningDetails earningDetails = new EarningDetails();
//        earningDetails.setOrderID(order_id);

        String addedQuoteTaskID="\"" + taskID + "\"";

        Log.e("addedQuote",""+addedQuoteTaskID);

        show_loader();

        apiService.requestEarningDetails(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken(),
                taskID).enqueue(new Callback<EarningDetailsData>() {
            @Override
            public void onResponse(@NotNull Call<EarningDetailsData> call, @NotNull Response<EarningDetailsData> response) {
                dismiss_loader();
                try {
                    if (response.code() == 200) {
                        inittoolbar(""+response.body().getTaskDetails().get(0).getTaskId());


                        if(response.body().getTaskDetails().get(0).getManualEarningsDetails()==null) {
                            txt_earnings_details_time.setText(timeFormat.format(ApiDateFormat.parse("" + response.body().getTaskDetails().get(0).getDate())) + " - " + response.body().getTaskDetails().get(0).getBussinessTypeName());
                            txt_earnings_details_amount.setText("" + response.body().getTaskDetails().get(0)
                                    .getEarningsDetails().getTotalSum() + " " + loginPrefManager.getCurrency());
                            txt_earnings_details_id.setText("" + response.body().getTaskDetails().get(0).getTaskId());
                            txt_earnings_details_minutes.setText("" + response.body().getTaskDetails().get(0).getCompletedTimeString());
                            txt_earnings_details_basefare.setText("" + response.body().getTaskDetails()
                                    .get(0).getEarningsDetails().getBaseFare() + " " + loginPrefManager.getCurrency());
                            txt_earnings_details_duration_fare.setText("" + response.body().getTaskDetails()
                                    .get(0).getEarningsDetails().getBaseDuration() + " " + loginPrefManager.getCurrency());
                            txt_earnings_details_distancefare.setText("" + response.body().getTaskDetails()
                                    .get(0).getEarningsDetails().getBaseDistance() + " " + loginPrefManager.getCurrency());
                            txt_earnings_details_deductionfare.setText("" + response.body().getTaskDetails()
                                    .get(0).getEarningsDetails().getDeduction() + " " + loginPrefManager.getCurrency());
                            txt_tips.setText("" + response.body().getTaskDetails()
                                    .get(0).getEarningsDetails().getTips() + " " + loginPrefManager.getCurrency());
                        }else {
                            txt_earnings_details_time.setText(timeFormat.format(ApiDateFormat.parse("" + response.body().getTaskDetails().get(0).getDate())) + " - " + response.body().getTaskDetails().get(0).getBussinessTypeName());
                            txt_earnings_details_amount.setText("" + response.body().getTaskDetails().get(0)
                                    .getManualEarningsDetails().getTotalSum() + " " + loginPrefManager.getCurrency());
                            txt_earnings_details_id.setText("" + response.body().getTaskDetails().get(0).getTaskId());
                            txt_earnings_details_minutes.setText("" + response.body().getTaskDetails().get(0).getCompletedTimeString());
                            txt_earnings_details_basefare.setText("" + response.body().getTaskDetails()
                                    .get(0).getManualEarningsDetails().getBaseFare() + " " + loginPrefManager.getCurrency());
                            txt_earnings_details_duration_fare.setText("" + response.body().getTaskDetails()
                                    .get(0).getManualEarningsDetails().getBaseDuration() + " " + loginPrefManager.getCurrency());
                            txt_earnings_details_distancefare.setText("" + response.body().getTaskDetails()
                                    .get(0).getManualEarningsDetails().getBaseDistance() + " " + loginPrefManager.getCurrency());
                            txt_earnings_details_deductionfare.setText("" + response.body().getTaskDetails()
                                    .get(0).getManualEarningsDetails().getDeduction() + " " + loginPrefManager.getCurrency());
                            txt_tips.setText("" + response.body().getTaskDetails()
                                    .get(0).getManualEarningsDetails().getTips() + " " + loginPrefManager.getCurrency());
                        }


                    } else if (response.raw().code() == 401) {
                        findCurrent();
                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<EarningDetailsData> call, @NotNull Throwable t) {
                dismiss_loader();
                showShortMessage(t.getMessage());
            }
        });
    }

    private void inittoolbar(String order_id) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar_text = findViewById(R.id.toolbar_title);

        toolbar.setTitle("");
        toolbar_text.setText("#" + order_id);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        toolbar.setNavigationOnClickListener(view -> finish());
    }

}
