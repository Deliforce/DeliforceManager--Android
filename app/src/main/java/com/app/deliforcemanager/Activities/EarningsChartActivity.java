package com.app.deliforcemanager.Activities;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.deliforcemanager.Adapter.EarningsListAdapter;
import com.app.deliforcemanager.BaseActivity.BaseDrawerActivity;
import com.app.deliforcemanager.ModelClass.EarningsChart.EarningDateArray;
import com.app.deliforcemanager.ModelClass.EarningsChart.EarningsChartData;
import com.app.deliforcemanager.ModelClass.EarningsChart.EarningsList;
import com.app.deliforcemanager.ModelClass.EarningsChart.EarningsRequest;
import com.app.deliforcemanager.R;
import com.archit.calendardaterangepicker.customviews.CalendarListener;
import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.ColumnChartView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EarningsChartActivity extends BaseDrawerActivity {

    SimpleDateFormat onlyDay = new SimpleDateFormat("d", Locale.ENGLISH);
    SimpleDateFormat dayMonthYear = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
    SimpleDateFormat apiDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    private ColumnChartView chartBottom;

    private int textColor;
    private Typeface typeface;
    private TextView txt_earnings_no_report, txt_total_payout;
    private LinearLayout llTotalView;
    RecyclerView rv_Earning;
    List<EarningsList> earningsList;
    EarningsListAdapter earningsListAdapter;
    EditText et_date_range;
    Button btn_clear_date;
    String endDate = "", startDate = "";

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startHomeScreen();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earnings_chart);

        tvTitle.setText(getString(R.string.title_earnings));

        rv_Earning = findViewById(R.id.rv_earnings);
        btn_clear_date = findViewById(R.id.btn_clear_date);
        et_date_range = findViewById(R.id.et_date_range);
        rv_Earning.setLayoutManager(new LinearLayoutManager(EarningsChartActivity.this));

        typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.normal_font_bold);
        textColor = getResources().getColor(R.color.black_overlay);

        txt_earnings_no_report = findViewById(R.id.txt_earnings_no_report);
        txt_total_payout = findViewById(R.id.txt_total_payout);
        llTotalView = findViewById(R.id.llTotalView);
        chartBottom = findViewById(R.id.chart_bottom);

        et_date_range.setOnClickListener(v -> showPicker() );
        btn_clear_date.setOnClickListener(v -> {
            startDate = getCurrentDate();
            endDate = getCurrentDate();
            callEarningsRangeAPI();
        });
        startDate = getCurrentDate();
        endDate = getCurrentDate();
        callEarningsRangeAPI();
    }

    private void showPicker() {
        try {
            Calendar startDayCalendar = Calendar.getInstance();
            startDayCalendar.setTime(apiDateFormat.parse(startDate));
            Calendar endDayCalendar = Calendar.getInstance();
            endDayCalendar.setTime(apiDateFormat.parse(endDate));

            final Dialog dialog = new Dialog(EarningsChartActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_date_range_picker);
            DateRangeCalendarView dateRangePicker =  dialog.findViewById(R.id.dateRangePicker);
            dateRangePicker.setSelectedDateRange(startDayCalendar,endDayCalendar);
            dateRangePicker.setNavLeftImage(ContextCompat.getDrawable(EarningsChartActivity.this,R.drawable.ic_left_white_padding));
            dateRangePicker.setNavRightImage(ContextCompat.getDrawable(EarningsChartActivity.this,R.drawable.ic_right_white_padding));
            dateRangePicker.setCalendarListener(new CalendarListener() {
                @Override
                public void onFirstDateSelected(@NonNull Calendar startDate) {

                }

                @Override
                public void onDateRangeSelected(@NonNull Calendar startDate, @NonNull Calendar endDate) {
                    try {
                        EarningsChartActivity.this.startDate = apiDateFormat.format(startDate.getTime());
                        EarningsChartActivity.this.endDate = apiDateFormat.format(endDate.getTime());
                        dialog.dismiss();
                        callEarningsRangeAPI();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callEarningsRangeAPI() {
        show_loader();
        try {
            et_date_range.setText(dayMonthYear.format(apiDateFormat.parse(startDate))
                    + " to "
                    + dayMonthYear.format(apiDateFormat.parse(endDate)));
            EarningsRequest createNewTask = new EarningsRequest();
            createNewTask.setStartDate(startDate);
            createNewTask.setEndDate(endDate);
            createNewTask.setTimezone(TimeZone.getDefault().getID());
            apiService.requestEarningsData(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken(), createNewTask).enqueue(new Callback<EarningsChartData>() {
                @Override
                public void onResponse(@NotNull Call<EarningsChartData> call, @NotNull Response<EarningsChartData> response) {
                    try {
                        if (response.code() == 200) {
                            if (response.body().getDateArr().size() > 0) {
                                earningsList = response.body().getTaskDetails();
                                showEarningsList(earningsList);
                                showview(loginPrefManager.GetEngDecimalFormatValues(Double.parseDouble(response.body().getTotalCount())));
                                generateColumnData(response.body().getDateArr());
                            } else {
                                dismiss_loader();
                                hideview();
                            }
                        } else if (response.code() == 401) {
                            dismiss_loader();
                            findCurrent();
                        } else if (response.code() == 500) {
                            dismiss_loader();
                            hideview();
                        } else {
                            dismiss_loader();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        dismiss_loader();
                        chartBottom.setVisibility(View.GONE);
                        txt_earnings_no_report.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(@NotNull Call<EarningsChartData> call, @NotNull Throwable t) {
                    dismiss_loader();
                    showShortMessage(t.getMessage());
                    Log.e("failure", "" + t.getMessage());

                    generateColumnData(new ArrayList<>());
                    chartBottom.setVisibility(View.VISIBLE);
                    txt_earnings_no_report.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getCurrentDate() {
        return apiDateFormat.format(Calendar.getInstance().getTime());
    }

    private List<EarningsList> getEarningsList(List<EarningsList> earningsList, String date) {
        List<EarningsList> earnings = new ArrayList<>();
        for (EarningsList events : earningsList) {
            SimpleDateFormat apiDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
            SimpleDateFormat currentDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date listDate = null;
            Date currentDate = null;
            try {
                listDate = currentDateFormat.parse(currentDateFormat.format(apiDateFormat.parse(events.getDate())));
                currentDate = currentDateFormat.parse(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (currentDate.equals(listDate)) {
                earnings.add(events);
            }
        }
        return earnings;
    }

    private void showview(String totalCount) {
        chartBottom.setVisibility(View.VISIBLE);
        txt_earnings_no_report.setVisibility(View.GONE);
        rv_Earning.setVisibility(View.VISIBLE);
        llTotalView.setVisibility(View.VISIBLE);
        txt_total_payout.setText(getString(R.string.total_payout) + " (" + "" + totalCount +" "+loginPrefManager.getCurrency()+ ")");

    }

    private void hideview() {
        chartBottom.setVisibility(View.GONE);
        txt_earnings_no_report.setVisibility(View.VISIBLE);
        rv_Earning.setVisibility(View.GONE);
        llTotalView.setVisibility(View.GONE);
        txt_total_payout.setText(getString(R.string.total_payout));
    }

    private void showEarningsList(List<EarningsList> earningDetails) {
        if (rv_Earning.getAdapter() == null) {
            earningsListAdapter = new EarningsListAdapter(EarningsChartActivity.this, earningDetails);
            rv_Earning.setAdapter(earningsListAdapter);
        } else {
            earningsListAdapter.notifyDataSetChanged(earningDetails);
        }
    }

    private void generateColumnData(List<EarningDateArray> dateArrays) {
        try {
            int numSubcolumns = 1;

            List<AxisValue> axisValues = new ArrayList<>();
            List<Column> columns = new ArrayList<>();
            List<SubcolumnValue> values;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(apiDateFormat.parse(startDate));
            int dayCount = 1;
            for (int i = 0; i < dateArrays.size(); ++i) {

                values = new ArrayList<>();
                for (int j = 0; j < numSubcolumns; ++j) {

                    values.add(new SubcolumnValue(Float.parseFloat(dateArrays.get(i).getCount()), getResources().getColor(R.color.colorPrimary)));
                }
                if (i != 0)
                    calendar.add(Calendar.DAY_OF_MONTH, dayCount);
                axisValues.add(new AxisValue(i).setLabel(onlyDay.format(calendar.getTime())));
                columns.add(new Column(values).setHasLabels(true));
            }

            ColumnChartData columnData = new ColumnChartData(columns);
            columnData.setValueLabelBackgroundEnabled(false);
            columnData.setValueLabelsTextColor(textColor);
            columnData.setValueLabelTypeface(typeface);

            columnData.setAxisXBottom(new Axis(axisValues).setName("Days").setTypeface(typeface).setTextColor(textColor).setHasLines(false));
            columnData.setAxisYLeft(new Axis().setTypeface(typeface).setName("Amount").setTextColor(textColor).setHasLines(true).setMaxLabelChars(5));

            chartBottom.setColumnChartData(columnData);

            // Set selection mode to keep selected month column highlighted.
            chartBottom.setValueSelectionEnabled(false);
            chartBottom.setValueTouchEnabled(false);

            chartBottom.setZoomEnabled(false);

            final Viewport v = new Viewport(chartBottom.getMaximumViewport());
            float fivePercent = v.height() * 0.3f;
            v.top = v.top + fivePercent;
            //v.bottom = v.bottom - fivePercent;
            chartBottom.setMaximumViewport(v);
            chartBottom.setCurrentViewport(v);

            /*chartBottom.setOnValueTouchListener(new ColumnChartOnValueSelectListener() {
                @Override
                public void onValueSelected(int i, int i1, SubcolumnValue subcolumnValue) {
                    showEarningsList(getEarningsList(earningsList, dateArrays.get(i).getDate()));
                }

                @Override
                public void onValueDeselected() {
                    showEarningsList(earningsList);
                }
            });*/
            if(dateArrays.isEmpty())
                dismiss_loader();
            else {
                new Handler().postDelayed(this::dismiss_loader,2000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
