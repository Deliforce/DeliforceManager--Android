package com.app.deliforcemanager.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.deliforcemanager.Adapter.FilterStatusAdpater;
import com.app.deliforcemanager.LocalizationActivity.LocalizationActivity;
import com.app.deliforcemanager.ModelClass.StatusApi.DefalutStatus;
import com.app.deliforcemanager.ModelClass.StatusApi.Status;
import com.app.deliforcemanager.R;
import com.app.deliforcemanager.RawHeaders.TaskInfo.Filter;
import com.app.deliforcemanager.Utils.AppUtils;
import com.app.deliforcemanager.Utils.DeliforceConstants;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class FilterActivity extends LocalizationActivity {

    EditText edtSearchTask, edtSearchTaskDateFrom, edtSearchTaskDateTo;
    Button btnClearAllFields, btnSendSearchFilter;
    CheckBox chkFilterAll, chkFilterAccepted, chkFilterStarted, chkFilterArrived, chkFilterSuccessful, chkFilterFailed, chkFilterCancelled, chkFilterDeclined;

    boolean isFilterAll, isFilterStarted, isFilterSuccess, isFilterArrived, isFilterDeclined, isFilterCancelled, isFilterAccecpted, isFilterFailed;
    private int year, month, day;
    List<String> dateFilterList = new ArrayList<>();
    Filter filter = new Filter();

    RadioGroup sortby_grp;
    RadioButton SortByTime, SortByDistance;
    String from_date, to_date;

    private List<Status> statusList;
    RecyclerView status_view;

    int filter_status = 0;

    FilterStatusAdpater filterStatusAdpater;
    final Calendar fromDateCal = Calendar.getInstance();
    final Calendar toDateCal = Calendar.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tasks);

        inittoolbar();
        initView();

        setFilterStatusValues();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initView() {

        edtSearchTask = findViewById(R.id.edt_search_task);
        edtSearchTaskDateFrom = findViewById(R.id.edt_search_task_date_from);
        edtSearchTaskDateTo = findViewById(R.id.edt_search_task_date_to);
        btnClearAllFields = findViewById(R.id.btn_clear_fields);
        chkFilterAll = findViewById(R.id.chk_search_task_status_all);
        chkFilterAccepted = findViewById(R.id.chk_search_task_status_accepted);
        chkFilterStarted = findViewById(R.id.chk_search_task_status_started);
        chkFilterArrived = findViewById(R.id.chk_search_task_status_arrived);
        chkFilterSuccessful = findViewById(R.id.chk_search_task_status_successful);
        chkFilterFailed = findViewById(R.id.chk_search_task_status_failed);
        chkFilterCancelled = findViewById(R.id.chk_search_task_status_cancelled);
        chkFilterDeclined = findViewById(R.id.chk_search_task_status_declined);
        SortByTime = findViewById(R.id.SortByTime);
        SortByDistance = findViewById(R.id.SortByDistance);
        btnSendSearchFilter = findViewById(R.id.btn_search_send);
        sortby_grp = findViewById(R.id.sortby_grp);
        status_view = findViewById(R.id.status_view);

        status_view.setLayoutManager(new GridLayoutManager(FilterActivity.this, 2));
        status_view.setHasFixedSize(true);

        if (loginPrefManager.getStringValue("filter_values").equalsIgnoreCase("1")) {
            filter_status = 1;
        } else {
            filter_status = 0;
        }
        getStatus();

        onClickevents();
        onCheckBoxchanges();

    }

    private void onCheckBoxchanges() {
        chkFilterAll.setOnCheckedChangeListener((compoundButton, status) -> {
            if (status) {
                chkFilterSuccessful.setChecked(true);
                chkFilterAccepted.setChecked(true);
                chkFilterFailed.setChecked(true);
                chkFilterStarted.setChecked(true);
                chkFilterArrived.setChecked(true);
                chkFilterCancelled.setChecked(true);
                chkFilterDeclined.setChecked(true);
                chkFilterAll.setChecked(true);
            }
        });

        chkFilterSuccessful.setOnCheckedChangeListener((compoundButton, b) -> checkone());
        chkFilterAccepted.setOnCheckedChangeListener((compoundButton, b) -> checkone());
        chkFilterFailed.setOnCheckedChangeListener((compoundButton, b) -> checkone());
        chkFilterStarted.setOnCheckedChangeListener((compoundButton, b) -> checkone());
        chkFilterArrived.setOnCheckedChangeListener((compoundButton, b) -> checkone());
        chkFilterDeclined.setOnCheckedChangeListener((compoundButton, b) -> checkone());
        chkFilterCancelled.setOnCheckedChangeListener((compoundButton, b) -> checkone());


        sortby_grp.setOnCheckedChangeListener((radioGroup, i) -> {
            if (SortByTime.isChecked()) {
                Timber.e("true");
                filter.setSortByTIme(1);
                loginPrefManager.setStringValue("chk_search_task_sort_by_time", "1");
                filter.setSortByDistance(0);
                Timber.e("---");
                loginPrefManager.setStringValue("chk_search_task_sort_by_distance", "0");

            }
            if (SortByDistance.isChecked()) {

                Timber.e("---");
                filter.setSortByTIme(0);
                loginPrefManager.setStringValue("chk_search_task_sort_by_time", "0");
                filter.setSortByDistance(1);
                loginPrefManager.setStringValue("chk_search_task_sort_by_distance", "1");
                Timber.e("true");
            }

        });
    }

    private void onClickevents() {

        edtSearchTaskDateFrom.setOnClickListener(view -> selectTaskFilterDateFromClick());
        edtSearchTaskDateTo.setOnClickListener(view -> selectTaskFilterDateToClick());
        btnSendSearchFilter.setOnClickListener(view -> {
            try {
                String taskStartDate = edtSearchTaskDateFrom.getText().toString();
                String taskEndDate = edtSearchTaskDateTo.getText().toString();

                if (!compare_date()) {
                    return;
                }
                loginPrefManager.setFromDate(edtSearchTaskDateFrom.getText().toString());
                loginPrefManager.setToDate(edtSearchTaskDateTo.getText().toString());

                ArrayList<String> selectedList = new ArrayList<>();
                if(statusList == null) {
                    getStatus();
                    return;
                }
                for (int i = 0; i < statusList.size(); i++) {
                    if (statusList.get(i).isChecked()) {
                        selectedList.add(statusList.get(i).getId() + "");
                    }
                }
                loginPrefManager.setStatusFilterList(selectedList.toString().replace("[", "").replace(" ", "").replace("]", ""));

                SimpleDateFormat currentDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);

                SimpleDateFormat modifiedDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

                String fromDate = null, toDate = null;
                try {
                    fromDate = modifiedDateFormat.format(currentDateFormat.parse(from_date));
                    toDate = modifiedDateFormat.format(currentDateFormat.parse(to_date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                dateFilterList.add(fromDate);
                dateFilterList.add(toDate);

                filter.setDateRange(dateFilterList);
                filter.setStatusFilter(selectedList);
                filter.setSearch(edtSearchTask.getText().toString());


                loginPrefManager.setStringValue("search_txt", edtSearchTask.getText().toString());
                loginPrefManager.setFilterList(filter);
                loginPrefManager.setStringValue("filter_values", "1");
                filterAndSortData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        btnClearAllFields.setOnClickListener(view -> {
            try {
                loginPrefManager.setStatusFilterList("[]");
                loginPrefManager.setStringValue("filter_values", "");
                loginPrefManager.setStringValue("search_txt", "");
                filter_status = 0;
                clearFilters();
                addTodaysDate();
                for (int i = 0; i < statusList.size(); i++) {
                    statusList.get(i).setChecked(false);
                }
                filterStatusAdpater.notifyDataSetChanged(statusList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void filterAndSortData() {
        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        finish();
    }


    private void inittoolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView toolbar_text = findViewById(R.id.toolbar_title);

        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        toolbar_text.setText(getString(R.string.title_task_filter));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        toolbar.setNavigationOnClickListener(view -> finish());
    }

    public void selectTaskFilterDateToClick() {
        setDateAccordingToLang();
        year = toDateCal.get(Calendar.YEAR);
        month = toDateCal.get(Calendar.MONTH);
        day = toDateCal.get(Calendar.DAY_OF_MONTH);
        //final String month_name = month_date.format(toDateCal.getTime());

        DatePickerDialog datePickerDialog1 = new DatePickerDialog(FilterActivity.this, (view, year, month, dayOfMonth) -> {

            Calendar cal1 = Calendar.getInstance();
            SimpleDateFormat month_date1 = new SimpleDateFormat("MMMM", Locale.ENGLISH);

            cal1.set(Calendar.MONTH, month);
            //String month_name1 = month_date1.format(cal1.getTime());
            String month_name1 = getMonthName(month);

            change_to_date_format(dayOfMonth + " " + month_name1 + " " + year);
            toDateCal.set(Calendar.YEAR, year);
            toDateCal.set(Calendar.MONTH, month);
            toDateCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        }, year, month, day);
        datePickerDialog1.show();
    }

    public void selectTaskFilterDateFromClick() {

        setDateAccordingToLang();

        year = fromDateCal.get(Calendar.YEAR);
        month = fromDateCal.get(Calendar.MONTH);
        day = fromDateCal.get(Calendar.DAY_OF_MONTH);
        //final String month_name = month_date.format(fromDateCal.getTime());

        DatePickerDialog datePickerDialog = new DatePickerDialog(FilterActivity.this, (view, year, month, dayOfMonth) -> {
            try {
                Calendar cal1 = Calendar.getInstance();
                SimpleDateFormat month_date1 = new SimpleDateFormat("MMMM",Locale.ENGLISH);
                cal1.set(Calendar.MONTH, month);

                //String month_name1 = month_date1.format(cal1.getTime());
                String month_name1 = getMonthName(month);
                fromDateCal.set(Calendar.YEAR, year);
                fromDateCal.set(Calendar.MONTH, month);
                fromDateCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                change_from_dateformat(dayOfMonth + " " + month_name1 + " " + year);

            } catch (Exception e) {
                e.printStackTrace();

            }
        }, year, month, day);
        datePickerDialog.show();
    }

    public String getMonthName(int month){
        String monthName = "";

        switch (month){
            case 0 : monthName = "January";
                break;
            case 1 : monthName = "February";
                break;
            case 2 : monthName = "March";
                break;
            case 3 : monthName = "April";
                break;
            case 4 : monthName = "May";
                break;
            case 5 : monthName = "June";
                break;
            case 6 : monthName = "July";
                break;
            case 7 : monthName = "August";
                break;
            case 8 : monthName = "September";
                break;
            case 9 : monthName = "October";
                break;
            case 10 : monthName = "November";
                break;
            case 11 : monthName = "December";
                break;
        }

        return monthName;

    }


    private void setDateAccordingToLang() {
        Locale locale = getResources().getConfiguration().locale;
        Locale.setDefault(locale);
    }


    private void checkone() {
        if (chkFilterAll.isChecked()) {
            chkFilterAll.setChecked(false);
        }
    }

    private void clearFilters() {
        edtSearchTask.setText("");
        edtSearchTaskDateFrom.setText("");
        edtSearchTaskDateTo.setText("");

//        chkFilterAll.setChecked(false);
//        chkFilterAccepted.setChecked(false);
//        chkFilterStarted.setChecked(false);
//        chkFilterArrived.setChecked(false);
//        chkFilterSuccessful.setChecked(false);
//        chkFilterFailed.setChecked(false);
//        chkFilterCancelled.setChecked(false);
//        chkFilterDeclined.setChecked(false);

        SortByTime.setChecked(false);
        SortByDistance.setChecked(false);

        loginPrefManager.remove(DeliforceConstants.PREF_FILTER_ALL);
        loginPrefManager.remove(DeliforceConstants.PREF_FILTER_ACCEPTED);
        loginPrefManager.remove(DeliforceConstants.PREF_FILTER_STRATED);
        loginPrefManager.remove(DeliforceConstants.PREF_FILTER_SUCCESS);
        loginPrefManager.remove(DeliforceConstants.PREF_FILTER_FAILED);
        loginPrefManager.remove(DeliforceConstants.PREF_FILTER_CANCELLED);
        loginPrefManager.remove(DeliforceConstants.PREF_FILTER_START_DATE);
        loginPrefManager.remove(DeliforceConstants.PREF_FILTER_END_DATE);
        loginPrefManager.remove(DeliforceConstants.PREF_FILTER_DECLINED);
        loginPrefManager.setStringValue("chk_search_task_sort_by_time", "");
        loginPrefManager.setStringValue("chk_search_task_sort_by_distance", "");
        loginPrefManager.setFromDate("");
        loginPrefManager.setToDate("");
        filter.setSortByDistance(0);
        filter.setSortByTIme(0);
    }


    private void setFilterStatusValues() {
        isFilterAll = loginPrefManager.getBooleanValue(DeliforceConstants.PREF_FILTER_ALL);
        isFilterStarted = loginPrefManager.getBooleanValue(DeliforceConstants.PREF_FILTER_STRATED);
        isFilterAccecpted = loginPrefManager.getBooleanValue(DeliforceConstants.PREF_FILTER_ACCEPTED);
        isFilterArrived = loginPrefManager.getBooleanValue(DeliforceConstants.PREF_FILTER_ARRIVED);
        isFilterDeclined = loginPrefManager.getBooleanValue(DeliforceConstants.PREF_FILTER_DECLINED);
        isFilterSuccess = loginPrefManager.getBooleanValue(DeliforceConstants.PREF_FILTER_SUCCESS);
        isFilterCancelled = loginPrefManager.getBooleanValue(DeliforceConstants.PREF_FILTER_CANCELLED);
        isFilterFailed = loginPrefManager.getBooleanValue(DeliforceConstants.PREF_FILTER_FAILED);
        String filterStartDate = loginPrefManager.getStringValue(DeliforceConstants.PREF_FILTER_START_DATE);
        String filterEndDate = loginPrefManager.getStringValue(DeliforceConstants.PREF_FILTER_END_DATE);

        if (isFilterAll) {
            chkFilterAll.setChecked(true);
            chkFilterStarted.setChecked(true);
            chkFilterAccepted.setChecked(true);
            chkFilterSuccessful.setChecked(true);
            chkFilterFailed.setChecked(true);
            chkFilterCancelled.setChecked(true);
        }
        if (isFilterStarted) {
            chkFilterStarted.setChecked(true);
        }
        if (isFilterAccecpted) {
            chkFilterAccepted.setChecked(true);
        }
        if (isFilterSuccess) {
            chkFilterSuccessful.setChecked(true);
        }
        if (isFilterDeclined) {
            chkFilterDeclined.setChecked(true);
        }
        if (isFilterFailed) {
            chkFilterFailed.setChecked(true);
        }
        if (isFilterCancelled) {
            chkFilterCancelled.setChecked(true);
        }
        edtSearchTaskDateFrom.setText(filterStartDate);
        edtSearchTaskDateTo.setText(filterEndDate);

        if (loginPrefManager.getStringValue("chk_search_task_sort_by_time").equals("1")) {
            SortByTime.setChecked(true);
        }
        if (loginPrefManager.getStringValue("chk_search_task_sort_by_distance").equals("1")) {
            SortByDistance.setChecked(true);
        }
        if (!loginPrefManager.getStringValue("search_txt").equals("")) {
            edtSearchTask.setText(loginPrefManager.getStringValue("search_txt"));
        }

        if (!loginPrefManager.getFromDate().equals("")) {


            change_from_dateformat(loginPrefManager.getFromDate());
        }
        if (!loginPrefManager.getToDate().equals("")) {

            change_to_date_format(loginPrefManager.getToDate());
        }

        if (loginPrefManager.getFromDate().equals("") || loginPrefManager.getToDate().equals("")) {
            addTodaysDate();
        }
    }

    private void addTodaysDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        String from_time = DEFALUT_FORMAT.format(new Date());
        loginPrefManager.setStringValue("from_date", sdf.format(new Date()));
        loginPrefManager.setStringValue("to_date", sdf.format(new Date()));

        from_date = sdf.format(new Date());
        to_date = sdf.format(new Date());
        edtSearchTaskDateFrom.setText(from_time);
        edtSearchTaskDateTo.setText(from_time);
    }


    private void change_from_dateformat(String mytime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
//        Locale spanishLocale = new Locale("es", "ES");

        Date myDate = null;
        try {
            myDate = dateFormat.parse(mytime);
        } catch (ParseException e) {
            e.printStackTrace();

        }

        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        from_date = timeFormat.format(myDate);

        edtSearchTaskDateFrom.setText(DEFALUT_FORMAT.format(myDate));
        loginPrefManager.setFromDate(edtSearchTaskDateFrom.getText().toString());
    }

    private void change_to_date_format(String mytime) {
        try {
            String date = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(Calendar.getInstance().getTime());

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.ENGLISH);

            Date myDate = null;
            try {
                myDate = dateFormat.parse(mytime + " " + date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
            String finalDate = timeFormat.format(myDate);
            to_date = finalDate;

            edtSearchTaskDateTo.setText(DEFALUT_FORMAT.format(myDate));
            loginPrefManager.setToDate(edtSearchTaskDateTo.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean compare_date() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        Date start, end;
        try {
            start = sdf.parse(from_date);
            end = sdf.parse(to_date);
            if (start.after(end)) {
                show_error_response("Please select correct date range");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void getStatus() {
        try {
            show_loader();

            String lang = "en";
            if (loginPrefManager.getLangauge().equalsIgnoreCase("1")) {
                lang = "en";
            }
            if (loginPrefManager.getLangauge().equalsIgnoreCase("2")) {
                lang = "es";
            }
            if (loginPrefManager.getLangauge().equalsIgnoreCase("3")) {
                lang = "fr";
            }
            if (loginPrefManager.getLangauge().equalsIgnoreCase("4")) {
                lang = "pt";
            }
            if (loginPrefManager.getLangauge().equalsIgnoreCase("5")) {
                lang = "ms";
            }

            apiService.getStatusList(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken(), lang).enqueue(new Callback<DefalutStatus>() {
                @Override
                public void onResponse(@NotNull Call<DefalutStatus> call, @NotNull Response<DefalutStatus> response) {

                    dismiss_loader();
                    try {
                        if (response.raw().code() == 200) {
                            statusList = response.body().getStatus();
                            for (int i = 0; i < statusList.size(); i++) {
                                if(!loginPrefManager.isAdminStatusEnabled()) {
                                    statusList.get(i).setStatusname(AppUtils.getStaticStatus(FilterActivity.this, statusList.get(i).getId()));
                                }
                            }
                            if(loginPrefManager.getStringValue("filter_values").equalsIgnoreCase("1")) {
                                for (int i = 0; i < statusList.size(); i++) {
                                    if(loginPrefManager.getStatusFilterList().contains(statusList.get(i).getId()+"")) {
                                        statusList.get(i).setChecked(true);
                                    }
                                }
                            }
                            filterStatusAdpater = new FilterStatusAdpater(FilterActivity.this, statusList, new FilterStatusAdpater.OnClickListener() {
                                @Override
                                public void onClick(int position) {
                                    statusList.get(position).setChecked(!statusList.get(position).isChecked());
                                    filterStatusAdpater.notifyDataSetChanged(statusList);
                                }
                            });

                            status_view.setAdapter(filterStatusAdpater);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(@NotNull Call<DefalutStatus> call, @NotNull Throwable t) {
                    dismiss_loader();
                }
            });

        } catch (Exception e) {
            dismiss_loader();
        }

    }

}