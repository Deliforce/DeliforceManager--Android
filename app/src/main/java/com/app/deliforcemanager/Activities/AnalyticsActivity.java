package com.app.deliforcemanager.Activities;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.app.deliforcemanager.Adapter.StatusAdapter;
import com.app.deliforcemanager.BaseActivity.BaseDrawerActivity;
import com.app.deliforcemanager.CustomViews.DonutProgress;
import com.app.deliforcemanager.ModelClass.AnalyticsApi.AnalyticTimeApi;
import com.app.deliforcemanager.ModelClass.AnalyticsApi.IdleTime;
import com.app.deliforcemanager.ModelClass.AnalyticsApi.TaskCompleationList;
import com.app.deliforcemanager.ModelClass.AnalyticsApi.TaskComplete;
import com.app.deliforcemanager.ModelClass.HoursApi.Hours;
import com.app.deliforcemanager.R;
import com.app.deliforcemanager.RawHeaders.Analytics.AnalyticTimeLog;
import com.app.deliforcemanager.RawHeaders.Analytics.HoursFilter;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
/*import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;*/
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;


public class AnalyticsActivity extends BaseDrawerActivity {


    LineChartView  lineChartView, mTimelineView;

    RecyclerView mileview_status, task_status;

    Spinner days_spinner;

    TextView work_txt, task_date, time_task, distance_task, stop_task, onTime, deleay, time_duty, task_completed, idle_time, route_time;

    List<TaskCompleationList> compleationLists;
    List<IdleTime> idleTimeList;

    String[] lineLabels, timeLables;

    List<PointValue> values, time_values;

    LinearLayout route_task_view;
    List<Float> active_report_list;

    TextView day_status, total_task_time, time_status;
    DonutProgress roundProgressBar, timeProgress;

    CircularProgressIndicator circularProgressIndicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        tvTitle.setText(getString(R.string.analytics));
        lineChartView = findViewById(R.id.taskview);
        mTimelineView = findViewById(R.id.mileview);
        mileview_status = findViewById(R.id.mileview_status);
        task_status = findViewById(R.id.task_status);
        days_spinner = findViewById(R.id.days_spinner);
        route_task_view = findViewById(R.id.route_task_view);
        task_date = findViewById(R.id.task_date);

        time_task = findViewById(R.id.time_task);
        distance_task = findViewById(R.id.distance_task);
        stop_task = findViewById(R.id.stop_task);
        work_txt = findViewById(R.id.work_txt);
        onTime = findViewById(R.id.onTime);
        deleay = findViewById(R.id.deleay);
        task_completed = findViewById(R.id.total_task_count);
        day_status = findViewById(R.id.day_status);
        total_task_time = findViewById(R.id.total_task_time);
        time_status = findViewById(R.id.time_status);
        time_duty = findViewById(R.id.time_duty);

        idle_time = findViewById(R.id.idle_time);
        route_time = findViewById(R.id.routetime);
        roundProgressBar = findViewById(R.id.roundProgress);
        timeProgress = findViewById(R.id.timeProgress);
        circularProgressIndicator = findViewById(R.id.circular_progress);

        circularProgressIndicator.setBackgroundColor(ContextCompat.getColor(AnalyticsActivity.this, R.color.transparent));


        circularProgressIndicator.setProgressStrokeWidthDp(5);
        circularProgressIndicator.setProgressBackgroundStrokeWidthDp(2);


        LinearLayoutManager layoutManager = new LinearLayoutManager(AnalyticsActivity.this, LinearLayoutManager.HORIZONTAL, true);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(AnalyticsActivity.this, LinearLayoutManager.HORIZONTAL, true);

        mileview_status.setLayoutManager(layoutManager);
        task_status.setLayoutManager(layoutManager1);
        List<String> status = new ArrayList<>();

        if (loginPrefManager.getLangauge().equalsIgnoreCase("2")) {

            status.add("1A");
            status.add("1M");
            status.add("1S");
        } else {
            status.add("1Y");
            status.add("1M");
            status.add("1W");
        }

        mileview_status.setAdapter(new StatusAdapter(AnalyticsActivity.this, status, timevalue -> {
            if (timevalue.equals("1W") || timevalue.equalsIgnoreCase("1S")) {
                time_status.setText(R.string.last_week);
                getActivetime(2);
            }
            if (timevalue.equals("1M")) {
                time_status.setText(R.string.last_month);
                getActivetime(3);
            }
            if (timevalue.equals("1Y") || timevalue.equalsIgnoreCase("1A")) {
                time_status.setText(R.string.last_year);
                getActivetime(4);
            }
        }));
        task_status.setAdapter(new StatusAdapter(AnalyticsActivity.this, status, timevalue -> {

            if (timevalue.equals("1W") || timevalue.equalsIgnoreCase("1S")) {
                day_status.setText(R.string.last_week);
                getTaskComplete(2);
            }
            if (timevalue.equals("1M")) {
                day_status.setText(R.string.last_month);
                getTaskComplete(3);
            }
            if (timevalue.equals("1Y") || timevalue.equalsIgnoreCase("1A")) {
                day_status.setText(R.string.last_year);
                getTaskComplete(4);
            }
        }));

        days_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                View v = days_spinner.getSelectedView();
                ((TextView) v).setTextColor(Color.WHITE);

                getHoursList(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }


    private void setViewport() {

        int maxNumberOfLines = 1;
        int numberOfPoints = lineLabels.length;
        ValueShape shape = ValueShape.CIRCLE;
        float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];
        for (int i = 0; i < maxNumberOfLines; ++i) {
            for (int j = 0; j < numberOfPoints; ++j) {
                randomNumbersTab[i][j] = (float) Math.random() * 0.1f;
            }
        }


        List<Line> lines = new ArrayList<>();
        List<AxisValue> axisValues = new ArrayList<AxisValue>();


        Line line = new Line(values);
        line.setColor(Color.parseColor("#0abe51"));

        line.setShape(shape);
        line.setPointRadius(5);
        line.setStrokeWidth(2);
        line.setCubic(false);
        line.setFilled(false);
        line.setHasLabels(true);
        line.setHasLabelsOnlyForSelected(true);
        line.setHasLines(true);
        line.setHasPoints(true);
        line.setCubic(true);
        lines.add(line);

        LineChartData data = new LineChartData(lines);
        for (int i = 0; i < numberOfPoints; i++) {
            axisValues.add(new AxisValue(i).setLabel(lineLabels[i]));
        }
        Axis axisX = new Axis(axisValues).setMaxLabelChars(5);
        axisX.setTextColor(ContextCompat.getColor(AnalyticsActivity.this, R.color.colorPrimary)).setTextSize(10).setHasTiltedLabels(false).setHasSeparationLine(false);
        data.setAxisXBottom(axisX);
        Axis axisY = new Axis().setHasSeparationLine(true).setMaxLabelChars(3).setTextColor(ContextCompat.getColor(AnalyticsActivity.this, R.color.transparent)).setTextSize(11).setHasLines(false).setLineColor(Color.parseColor("#28ffffff")).setHasTiltedLabels(false);
        data.setAxisYLeft(axisY);


        data.setValueLabelsTextColor(Color.parseColor("#bdbdbd"));
        data.setValueLabelBackgroundEnabled(false);
        lineChartView.setLineChartData(data);


        lineChartView.setZoomEnabled(true);
        lineChartView.setScrollEnabled(true);
        lineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChartView.setValueSelectionEnabled(true);
        lineChartView.setViewportCalculationEnabled(true);


        lineChartView.setOnValueTouchListener(new LineChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int i, int i1, PointValue pointValue) {

//                double active_time = idleTimeList.get((int) pointValue.getX()).getActiveTime();
//                double idle_time = idleTimeList.get((int) pointValue.getX()).getIdleTime();


                int success_count = compleationLists.get((int) pointValue.getX()).getSuccessCount();
                int delay_count = compleationLists.get((int) pointValue.getX()).getDelayTaskPerDay();

                String id = compleationLists.get((int) pointValue.getX()).getId();


                setTImeonDuty(success_count, delay_count, id);
            }

            @Override
            public void onValueDeselected() {

            }
        });


        //Let the layout slide horizontally to set setCurrentViewport smaller than setMaximumViewport
        Viewport v = new Viewport(lineChartView.getMaximumViewport());
        float min, max;
        min = max = randomNumbersTab[0][0];


        for (int i = 0; i < numberOfPoints; i++) {

            if (randomNumbersTab[0][i] > max)   // Judging the maximum
                max = randomNumbersTab[0][i];
            if (randomNumbersTab[0][i] < min)   // Judgment minimum
                min = randomNumbersTab[0][i];
        }
        v.bottom = (int) min - 5;
        v.top = (int) max + 20;
        v.left = 0;
        v.right = numberOfPoints - 1 + 0.5f;
        lineChartView.setMaximumViewport(v);
        v.left = 0;
        v.right = Math.min(7.5f, numberOfPoints - 1 + 0.5f);
        lineChartView.setCurrentViewport(v);


    }

    private void setmViewport() {

        int maxNumberOfLines = 1;
        int numberOfPoints1 = timeLables.length;
        ValueShape shape = ValueShape.CIRCLE;
        float[][] randomNumbersTab1 = new float[maxNumberOfLines][numberOfPoints1];
        for (int i = 0; i < maxNumberOfLines; ++i) {
            for (int j = 0; j < numberOfPoints1; ++j) {
                randomNumbersTab1[i][j] = (float) Math.random() * 0.1f;
            }
        }


        List<Line> lines = new ArrayList<>();
        List<AxisValue> axisValues = new ArrayList<AxisValue>();


        Line line = new Line(time_values);
        line.setColor(Color.parseColor("#0abe51"));

        line.setShape(shape);
        line.setPointRadius(5);
        line.setStrokeWidth(2);
        line.setCubic(false);
        line.setFilled(false);
        line.setHasLabels(true);
        line.setHasLabelsOnlyForSelected(true);
        line.setHasLines(true);
        line.setHasPoints(true);
        line.setCubic(true);
        lines.add(line);

        LineChartData data = new LineChartData(lines);
        for (int i = 0; i < numberOfPoints1; i++) {
            axisValues.add(new AxisValue(i).setLabel(timeLables[i]));
        }
        Axis axisX = new Axis(axisValues).setMaxLabelChars(5);
        axisX.setTextColor(ContextCompat.getColor(AnalyticsActivity.this, R.color.colorPrimary)).setTextSize(10).setHasTiltedLabels(false).setHasSeparationLine(false);
        data.setAxisXBottom(axisX);
        Axis axisY = new Axis().setHasSeparationLine(true).setMaxLabelChars(3).setTextColor(ContextCompat.getColor(AnalyticsActivity.this, R.color.transparent)).setTextSize(11).setHasLines(false).setLineColor(Color.parseColor("#28ffffff")).setHasTiltedLabels(false);
        data.setAxisYLeft(axisY);


        data.setValueLabelsTextColor(Color.parseColor("#bdbdbd"));
        data.setValueLabelBackgroundEnabled(false);

        mTimelineView.setOnValueTouchListener(new LineChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int i, int i1, PointValue pointValue) {


                double active_time = idleTimeList.get((int) pointValue.getX()).getActiveTime();
                double idle_time = idleTimeList.get((int) pointValue.getX()).getIdleTime();

                String id = idleTimeList.get((int) pointValue.getX()).getId();

                getTimeTaskDetails(active_time, idle_time, id);
            }

            @Override
            public void onValueDeselected() {

            }
        });


        mTimelineView.setLineChartData(data);


        mTimelineView.setZoomEnabled(true);
        mTimelineView.setScrollEnabled(true);
        mTimelineView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        mTimelineView.setValueSelectionEnabled(true);
        mTimelineView.setViewportCalculationEnabled(true);


        //Let the layout slide horizontally to set setCurrentViewport smaller than setMaximumViewport
        Viewport v = new Viewport(mTimelineView.getMaximumViewport());
        float min, max;
        min = max = randomNumbersTab1[0][0];

        double min_bottom = getEarningSum();


        for (int i = 0; i < numberOfPoints1; i++) {

            if (randomNumbersTab1[0][i] > max)
                max = randomNumbersTab1[0][i];
            if (randomNumbersTab1[0][i] < min)
                min = randomNumbersTab1[0][i];
        }


        Timber.e(String.valueOf(min_bottom));

        if (min_bottom > 1) {

            float max_value = Collections.max(active_report_list);

            Timber.e(max_value + "/" + max);

            if (max_value < 10) {
                v.bottom = (float) (min - 1.5);
            } else if (max_value < 50 && max_value > 10) {
                v.bottom = (float) ((min - 3.5) - min);
            } else if (max_value < 100 && max_value > 50) {
                v.bottom = (float) ((min - 7.5) - min);
            } else if (max_value < 150 && max_value > 100) {
                v.bottom = (float) ((min - 10.5) - min);
            } else if (max_value < 200 && max_value > 150) {
                v.bottom = (float) ((min - 15.5) - min);
            } else if (max_value < 250 && max_value > 200) {
                v.bottom = (float) ((min - 20.5) - min);
            } else if (max_value < 300 && max_value > 250) {
                v.bottom = (float) ((min - 50.5) - min);
            } else if (max_value < 500 && max_value > 300) {
                v.bottom = (float) ((min - 50.5) - min);
            } else if (max_value <= 700 && max_value >= 500) {
                v.bottom = (float) ((min - 50.5) - min);
            } else if (max_value <= 1000 && max_value >= 700) {
                v.bottom = (float) ((min - 75.5) - min);
            } else if (max_value <= 5000 && max_value > 1000) {
                v.bottom = (float) ((min - 130.5) - min);
            } else if (max_value > 5000) {
                v.bottom = (float) ((min - 200.0) - min);
            }
            Timber.e(String.valueOf(v.bottom));
        } else {
            v.bottom = (float) (min - 0.2);
            Timber.e(String.valueOf(v.bottom));
        }

        max = Collections.max(active_report_list);
        v.top = (int) max + 100;
        v.left = 0;
        v.right = numberOfPoints1 - 1 + 0.5f;
        mTimelineView.setMaximumViewport(v);
        v.left = 0;
        v.right = Math.min(7.5f, numberOfPoints1 - 1 + 0.5f);
        mTimelineView.setCurrentViewport(v);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startHomeScreen();
    }

    private void getHoursList(int position) {
        Timber.e("-%s", position);

        try {
            HoursFilter hoursFilter = new HoursFilter();
            hoursFilter.setStatus(position);

            show_loader();

            apiService.getHoursDetails(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken(), hoursFilter).enqueue(new Callback<Hours>() {
                @Override
                public void onResponse(@NotNull Call<Hours> call, @NotNull Response<Hours> response) {
                    dismiss_loader();

                    try {
                        if (response.raw().code() == 200) {
                            time_task.setText(response.body().getAvgTimeForTask());
                            stop_task.setText("-------");
                            distance_task.setText(response.body().getAvgDistanceForTask());

                            Double mins_work = response.body().getTotalMinutes();


                            if (mins_work < 0) {
                                work_txt.setText(String.valueOf(mins_work));
                            } else {
                                double converted_mins = mins_work / 60;

                                if (converted_mins > 1) {
                                    double total_hours = 8;

                                    if (position > 0) {
                                        total_hours = total_hours * position;
                                    }

                                    double progress = converted_mins / total_hours;

                                    circularProgressIndicator.setProgress(progress, 100);

                                    work_txt.setText(loginPrefManager.GetEngDecimalFormatValues(converted_mins) + '\n' + getString(R.string.Hours));
                                } else {

                                    double total_hours = 8;

                                    if (position > 0) {
                                        total_hours = total_hours * position;
                                    }

                                    double progress = converted_mins / total_hours;

                                    circularProgressIndicator.setProgress(progress, 100);
                                    work_txt.setText(loginPrefManager.GetEngDecimalFormatValues(mins_work) + '\n' + "Mins");
                                }

                            }

                        }    //alert dialog for user already logged in
                        else if (response.code() == 494) {
                            showAlertDialog(AnalyticsActivity.this);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<Hours> call, Throwable t) {
                    dismiss_loader();

                    Timber.e(t);

                }
            });

        } catch (Exception e) {
            Timber.e(e);
        }

    }


    private void getTaskComplete(int position) {
        try {

            show_loader();

            HoursFilter hoursFilter = new HoursFilter();

            hoursFilter.setStatus(position);

            apiService.getTaskCompleteDetails(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken(), hoursFilter).enqueue(new Callback<TaskComplete>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NotNull Call<TaskComplete> call, @NotNull Response<TaskComplete> response) {
                    dismiss_loader();

                    try {
                        if (response.raw().code() == 200) {
                            compleationLists = response.body().getTaskCompleationList();
                            lineLabels = new String[compleationLists.size()];
                            values = new ArrayList<>();
                            int success_count = response.body().getTotalSuccessCount();
                            int on_time_count = response.body().getOntimeCount();
                            int delay_count = success_count - on_time_count;

                            int progress;
                            if (success_count > 0) {
                                roundProgressBar.setVisibility(View.VISIBLE);
                                progress = 360 * on_time_count / success_count;

                            } else {
                                progress = 0;
                            }


                            if (delay_count == 0 && on_time_count == 0) {
                                roundProgressBar.setVisibility(View.GONE);
                            }

                            if (delay_count > on_time_count) {

                                roundProgressBar.setUnfinishedStrokeColor(Color.parseColor("#1793fd"));
                                roundProgressBar.setFinishedStrokeColor(Color.parseColor("#0abe51"));

                            } else {
                                roundProgressBar.setFinishedStrokeColor(Color.parseColor("#0abe51"));
                                roundProgressBar.setUnfinishedStrokeColor(Color.parseColor("#1793fd"));
                            }

                            time_duty.setText("");
                            roundProgressBar.setDonut_progress(String.valueOf(progress));
                            deleay.setText(delay_count + " " + getString(R.string.title_task));

                            onTime.setText(response.body().getOntimeCount() + " " + getString(R.string.title_task));
                            task_completed.setText("" + response.body().getTotalSuccessCount() + " " + getString(R.string.task_complete));
                            if (position == 2) {
                                for (int i = 0; i < compleationLists.size(); i++) {

                                    values.add(new PointValue(i, compleationLists.get(i).getSuccessCount()));
                                    String input_date = compleationLists.get(i).getId();
                                    SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                    Date date;
                                    try {
                                        date = dateformat.parse(input_date);
                                        DateFormat dayFormate = new SimpleDateFormat("EEEE", Locale.ENGLISH);

                                        Locale spanishLocale = new Locale("es", "ES");

                                        DateFormat spanish_dayFormate = new SimpleDateFormat("EEEE", spanishLocale);
                                        String dayFromDate = dayFormate.format(date);
                                        if (loginPrefManager.getLangauge().equalsIgnoreCase("2")) {
                                            dayFromDate = spanish_dayFormate.format(date);
                                        }

                                        dayFromDate = dayFromDate.substring(0, 3);

                                        lineLabels[i] = dayFromDate;

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                }

                                setViewport();

                            }
                            if (position == 3 || position == 4) {

                                for (int i = 0; i < compleationLists.size(); i++) {

                                    values.add(new PointValue(i, compleationLists.get(i).getSuccessCount()));
                                    String input_date = compleationLists.get(i).getId();
                                    SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                    Date date;
                                    try {
                                        date = dateformat.parse(input_date);
                                        DateFormat dayFormate = new SimpleDateFormat("MM/dd", Locale.ENGLISH);
                                        String dayFromDate = dayFormate.format(date);
                                        lineLabels[i] = dayFromDate;

                                    } catch (ParseException e) {

                                        Timber.e(e);
                                        e.printStackTrace();
                                    }
                                }

                                setViewport();

                            }


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(Call<TaskComplete> call, Throwable t) {

                    dismiss_loader();
                    Timber.e(t);

                }
            });

        } catch (Exception e) {

            dismiss_loader();
            Timber.e(e);
        }
    }

    private void getTimeTaskDetails(double a_time, double i_time, String id) {
        try {

            double activeTime = a_time;
            double idleTime = i_time;
            double delay_count = activeTime + idleTime;
            int progress;

            route_task_view.setVisibility(View.VISIBLE);

            convertDateformat(id);


            if (activeTime <= 0) {
                timeProgress.setFinishedStrokeColor(Color.parseColor("#0abe51"));
                timeProgress.setUnfinishedStrokeColor(Color.parseColor("#0abe51"));
                timeProgress.setProgress(360);
            } else if (idleTime <= 0) {
                timeProgress.setFinishedStrokeColor(Color.parseColor("#1793fd"));
                timeProgress.setUnfinishedStrokeColor(Color.parseColor("#1793fd"));
                timeProgress.setProgress(360);
            } else if (activeTime != 0 && idleTime != 0) {

                progress = (int) (360 * activeTime / delay_count);
                timeProgress.setProgress(progress);
                timeProgress.setFinishedStrokeColor(Color.parseColor("#0abe51"));
                timeProgress.setUnfinishedStrokeColor(Color.parseColor("#1793fd"));
            }

            route_time.setText(activeTime + " " + getString(R.string.title_time));
            idle_time.setText(idleTime + " " + getString(R.string.title_time));

        } catch (Exception e) {
            Timber.e(e);
        }
    }

    private void convertDateformat(String id) {

        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        try {
            Date oneWayTripDate = input.parse(id);

            task_date.setText(" " + "[ " + output.format(oneWayTripDate) + " ]");// parse input
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void convertTimeDateformat(String id) {

        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        try {
            Date oneWayTripDate = input.parse(id);

            time_duty.setText(" " + "[ " + output.format(oneWayTripDate) + " ]");// parse input
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    private void getActivetime(int type) {
        try {
            show_loader();
            AnalyticTimeLog analyticTimeLog = new AnalyticTimeLog();
            analyticTimeLog.setDateFilter(type);

            apiService.getAnalyticTImesCall(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken(), analyticTimeLog).enqueue(new Callback<AnalyticTimeApi>() {
                @Override
                public void onResponse(@NotNull Call<AnalyticTimeApi> call, @NotNull Response<AnalyticTimeApi> response) {

                    dismiss_loader();
                    try {
                        if (response.raw().code() == 200) {
                            idleTimeList = response.body().getJsonData();

                            if (type == 2) {


                                if (idleTimeList.size() > 0) {
                                    active_report_list = new ArrayList<>();
                                    time_values = new ArrayList<>();
                                    timeLables = new String[idleTimeList.size()];
                                    total_task_time.setText(response.body().getTotalAciveTime() + " " + "Mins");
                                    for (int i = 0; i < idleTimeList.size(); i++) {
                                        active_report_list.add(Float.valueOf(idleTimeList.get(i).getActiveTime()));
                                        time_values.add(new PointValue(i, idleTimeList.get(i).getActiveTime()));
                                        String input_date = idleTimeList.get(i).getId();
                                        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                        Date date;
                                        try {
                                            date = dateformat.parse(input_date);
                                            DateFormat dayFormate = new SimpleDateFormat("EEEE", Locale.ENGLISH);

                                            Locale spanishLocale = new Locale("es", "ES");
                                            DateFormat spanish_dayFormate = new SimpleDateFormat("EEEE", spanishLocale);

                                            String dayFromDate = dayFormate.format(date);
                                            if (loginPrefManager.getLangauge().equalsIgnoreCase("2")) {
                                                dayFromDate = spanish_dayFormate.format(date);
                                            }

                                            dayFromDate = dayFromDate.substring(0, 3);
                                            timeLables[i] = dayFromDate;

                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                    setmViewport();

                                }


                            }
                            if (type == 3 || type == 4) {

                                Timber.e("33");

                                time_values = new ArrayList<>();
                                timeLables = new String[idleTimeList.size()];
                                active_report_list = new ArrayList<>();
                                total_task_time.setText(response.body().getTotalAciveTime() + " " + "Mins");
                                for (int i = 0; i < idleTimeList.size(); i++) {
                                    time_values.add(new PointValue(i, idleTimeList.get(i).getActiveTime()));
                                    String input_date = idleTimeList.get(i).getId();
                                    active_report_list.add(Float.valueOf(idleTimeList.get(i).getActiveTime()));
                                    SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                    Date date;
                                    try {
                                        date = dateformat.parse(input_date);
                                        DateFormat dayFormate = new SimpleDateFormat("MM/dd", Locale.ENGLISH);
                                        String dayFromDate = dayFormate.format(date);
                                        timeLables[i] = dayFromDate;

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                        Timber.e(e);
                                    }
                                }

                                setmViewport();

                            }


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(Call<AnalyticTimeApi> call, Throwable t) {
                    dismiss_loader();

                    Timber.e(t);

                }
            });

        } catch (Exception e) {
            Timber.e(e);
        }
    }

    private double getEarningSum() {
        double esum = 0;
        for (Float d : active_report_list)
            esum += d;
        return esum;
    }


    private void setTImeonDuty(int s_count, int o_count, String id) {


        convertTimeDateformat(id);


        int on_time_count = s_count - o_count;

        int progress;
        if (s_count > 0) {
            roundProgressBar.setVisibility(View.VISIBLE);
            progress = 360 * on_time_count / s_count;

        } else {
            progress = 0;
        }


        if (o_count == 0 && on_time_count == 0) {
            roundProgressBar.setVisibility(View.GONE);
        }

        if (o_count > on_time_count) {

            roundProgressBar.setUnfinishedStrokeColor(Color.parseColor("#1793fd"));
            roundProgressBar.setFinishedStrokeColor(Color.parseColor("#0abe51"));

        } else {
            roundProgressBar.setFinishedStrokeColor(Color.parseColor("#0abe51"));
            roundProgressBar.setUnfinishedStrokeColor(Color.parseColor("#1793fd"));
        }

        roundProgressBar.setDonut_progress(String.valueOf(progress));
        deleay.setText(o_count + " " + getString(R.string.title_task));

        onTime.setText(on_time_count + " " + getString(R.string.title_task));
    }

}
