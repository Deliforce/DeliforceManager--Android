package com.app.deliforcemanager.Activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.deliforcemanager.Adapter.EarningsListAdapter;
import com.app.deliforcemanager.BaseActivity.BaseDrawerActivity;
import com.app.deliforcemanager.ModelClass.EarningsChart.EarningsChartData;
import com.app.deliforcemanager.ModelClass.EarningsChart.EarningsList;
import com.app.deliforcemanager.ModelClass.EarningsChart.EarningsRequest;
import com.app.deliforcemanager.R;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/*import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.ColumnChartView;*/
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class EarningsChartOldActivity extends BaseDrawerActivity {

    final static String[] days = new String[]{"M", "T", "W", "T", "F", "S", "S"};

    SimpleDateFormat onlyDay = new SimpleDateFormat("d", Locale.ENGLISH);
    SimpleDateFormat dayMonthYear = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
    SimpleDateFormat apiDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private static int weeekNumber = 0;

   // private ColumnChartView chartBottom;

    private int textColor;
    private Typeface typeface;
    private String dayName;
    private TextView txt_weekName;
    private TextView txt_earnings_no_report,txt_total_payout;
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
        et_date_range = findViewById(R.id.et_date_range);
        rv_Earning.setLayoutManager(new LinearLayoutManager(EarningsChartOldActivity.this));

        typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.normal_font_bold);
        textColor = getResources().getColor(R.color.black_overlay);

        txt_earnings_no_report = findViewById(R.id.txt_earnings_no_report);
        txt_total_payout = findViewById(R.id.txt_total_payout);
        llTotalView = findViewById(R.id.llTotalView);
      //  chartBottom = findViewById(R.id.chart_bottom);

        txt_weekName = findViewById(R.id.txt_month_week);
        ImageView imgPrevious = findViewById(R.id.img_previous);
        ImageView imgNext = findViewById(R.id.img_next);

        imgNext.setOnClickListener(v -> callEarningsAPI(weeekNumber = weeekNumber + 1));
        imgPrevious.setOnClickListener(v -> callEarningsAPI(weeekNumber = weeekNumber - 1));
//        et_date_range.setOnClickListener(v -> );
        btn_clear_date.setOnClickListener(v -> {
            startDate = getCurrentDate();
            endDate = getCurrentDate();
        });
        callEarningsAPI(weeekNumber);
    }

    private void callEarningsAPI(int weekCount) {
        Timber.e("weekCount =  %s", weekCount);

        show_loader();

        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.WEEK_OF_YEAR, calendar.get(Calendar.WEEK_OF_YEAR) + weekCount);

        dayName = onlyDay.format(calendar.getTime());

        String startWeekDay = apiDateFormat.format(calendar.getTime());
        //String startOnlyDay = onlyDay.format(calendar.getTime());

        calendar.add(Calendar.DAY_OF_MONTH, 6);

        String endWeekDay = apiDateFormat.format(calendar.getTime());

        txt_weekName.setText(dayName + "-" + dayMonthYear.format(calendar.getTime()));

        calendar.add(Calendar.DAY_OF_MONTH, -6);


        Timber.e("endWeekDay =  %s", endWeekDay);
        Timber.e("startWeekDay =  %s", startWeekDay);


        EarningsRequest createNewTask = new EarningsRequest();
        createNewTask.setStartDate(startWeekDay);
        createNewTask.setEndDate(endWeekDay);

        Log.e("earning_re",new Gson().toJson(createNewTask));

        try {
            apiService.requestEarningsData(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken(), createNewTask).enqueue(new Callback<EarningsChartData>() {
                @Override
                public void onResponse(@NotNull Call<EarningsChartData> call, @NotNull Response<EarningsChartData> response) {
                    dismiss_loader();
                    try {
                        if (response.code() == 200) {
                            if (response.body().getDateArr().size()>0) {
                             //   generateColumnData(calendar, response.body().getDateArr());
                                earningsList = response.body().getTaskDetails();
                                showEarningsList(getEarningsList(earningsList,getCurrentDate()));
                                showview(loginPrefManager.GetEngDecimalFormatValues(Double.parseDouble(response.body().getTotalCount())));
                            }else{
                                hideview();
                            }
                        } else if (response.code() == 401) {
                            findCurrent();
                        }else if (response.code()==500){
                           hideview();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        //chartBottom.setVisibility(View.GONE);
                        txt_earnings_no_report.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(@NotNull Call<EarningsChartData> call, @NotNull Throwable t) {
                    dismiss_loader();
                    showShortMessage(t.getMessage());
                    Log.e("failure",""+t.getMessage());

                  //  generateColumnData(calendar, new ArrayList<>());
                   // chartBottom.setVisibility(View.VISIBLE);
                    txt_earnings_no_report.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getCurrentDate() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        return df.format(date);
    }

    private List<EarningsList> getEarningsList(List<EarningsList> earningsList,String date) {
        List<EarningsList> earnings = new ArrayList<>();
        for(EarningsList events : earningsList) {
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
            if(currentDate.equals(listDate)) {
                earnings.add(events);
            }
        }
        return earnings;
    }

    private void showview(String totalCount) {
        //chartBottom.setVisibility(View.VISIBLE);
        txt_earnings_no_report.setVisibility(View.GONE);
        rv_Earning.setVisibility(View.VISIBLE);
        llTotalView.setVisibility(View.VISIBLE);
        txt_total_payout.setText(getString(R.string.total_payout) + " (" + "" + totalCount + ")");

    }

    private void hideview() {
       // chartBottom.setVisibility(View.GONE);
        txt_earnings_no_report.setVisibility(View.VISIBLE);
        llTotalView.setVisibility(View.VISIBLE);
        rv_Earning.setVisibility(View.GONE);
        llTotalView.setVisibility(View.GONE);
        txt_total_payout.setText(getString(R.string.total_payout));
    }

    private void showEarningsList(List<EarningsList> earningDetails) {
        if(rv_Earning.getAdapter() == null) {
            earningsListAdapter = new EarningsListAdapter(EarningsChartOldActivity.this, earningDetails);
            rv_Earning.setAdapter(earningsListAdapter);
        } else {
            earningsListAdapter.notifyDataSetChanged(earningDetails);
        }
    }

    /*private void generateColumnData(Calendar calendar, List<EarningDateArray> dateArrays) {
        int numSubcolumns = 1;
        int numColumns = days.length;

        List<AxisValue> axisValues = new ArrayList<>();
        List<Column> columns = new ArrayList<>();
        List<SubcolumnValue> values,count;

        int dayCount = 1;
        for (int i = 0; i < dateArrays.size(); ++i) {

            values = new ArrayList<>();
            for (int j = 0; j < numSubcolumns; ++j) {

                values.add(new SubcolumnValue(Float.parseFloat(dateArrays.get(i).getCount()), getResources().getColor(R.color.colorPrimary)));
            }

            axisValues.add(new AxisValue(i).setLabel(days[i] + " " + dayName));

            columns.add(new Column(values).setHasLabels(true));


            calendar.add(Calendar.DAY_OF_MONTH, dayCount);
            dayName = onlyDay.format(calendar.getTime());
        }

        ColumnChartData columnData = new ColumnChartData(columns);
        columnData.setValueLabelBackgroundEnabled(false);
        columnData.setValueLabelsTextColor(textColor);
        columnData.setValueLabelTypeface(typeface);

        columnData.setAxisXBottom(new Axis(axisValues).setName("Week days").setTypeface(typeface).setTextColor(textColor).setHasLines(false));
        columnData.setAxisYLeft(new Axis().setTypeface(typeface).setName("Amount").setTextColor(textColor).setHasLines(true).setMaxLabelChars(5));

        chartBottom.setColumnChartData(columnData);

        // Set selection mode to keep selected month column highlighted.
        chartBottom.setValueSelectionEnabled(true);
        chartBottom.setValueTouchEnabled(true);

        chartBottom.setZoomEnabled(false);

        final Viewport v = new Viewport(chartBottom.getMaximumViewport());
        float fivePercent = v.height() * 0.3f;
        v.top = v.top + fivePercent;
        //v.bottom = v.bottom - fivePercent;
        chartBottom.setMaximumViewport(v);
        chartBottom.setCurrentViewport(v);

        chartBottom.setOnValueTouchListener(new ColumnChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int i, int i1, SubcolumnValue subcolumnValue) {
                showEarningsList(getEarningsList(earningsList,dateArrays.get(i).getDate()));
            }

            @Override
            public void onValueDeselected() {

            }
        });
    }*/

}
