package com.app.deliforcemanager.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.deliforcemanager.Adapter.TasksListUpdatedAdapter;
import com.app.deliforcemanager.BaseActivity.BaseDrawerActivity;
import com.app.deliforcemanager.BuildConfig;
import com.app.deliforcemanager.Cognito.AppHelper;
import com.app.deliforcemanager.CustomDialogView.AlertDialog;
import com.app.deliforcemanager.CustomDialogView.UpdateAppDialog;
import com.app.deliforcemanager.ModelClass.CreateTask.CreateNewTask;
import com.app.deliforcemanager.ModelClass.CreateTask.DriverImageOption;

import com.app.deliforcemanager.ModelClass.ManagerProfile.ProfileResponse;
import com.app.deliforcemanager.ModelClass.ManagerTaskList.Doc;
import com.app.deliforcemanager.ModelClass.ManagerTaskList.ManagerTaskResponse;
import com.app.deliforcemanager.ModelClass.TaskDetailsApi.TaskDetails;
import com.app.deliforcemanager.ModelClass.TaskDetailsApi.TaskUpdatedList;
import com.app.deliforcemanager.ModelClass.TaskRoutesApi.Address;
import com.app.deliforcemanager.ModelClass.TaskRoutesApi.Geometry;
import com.app.deliforcemanager.ModelClass.TaskRoutesApi.Location;
import com.app.deliforcemanager.R;
import com.app.deliforcemanager.RawHeaders.TaskInfo.Filter;
import com.app.deliforcemanager.RawHeaders.TaskInfo.FilterMain;
import com.app.deliforcemanager.Services.IdleService;
import com.app.deliforcemanager.Utils.AppUtils;
import com.app.deliforcemanager.glypmseWrapper.EnRouteWrapper;
import com.glympse.android.core.CC;
import com.glympse.android.toolbox.listener.GListener;
import com.glympse.android.toolbox.listener.GSource;
import com.glympse.enroute.android.api.EE;
import com.glympse.enroute.android.api.EnRouteConstants;
import com.glympse.enroute.android.api.GEnRouteManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;


/*, TasksListUpdatedAdapter.TasksListUpdatedListener*/


public class NavigationActivity extends BaseDrawerActivity implements GListener {

    public static final String TAG = "NavigationActivity";
    private static final int TASK_DETAILS_CHECK = 666;
    private static final int NEW_TASK_CREATION = 567;
    private static final int SCAN_TASK_CREATION = 568;
    //GPSTracker gpsTracker;
    Dialog upcDialog;
    List<TaskUpdatedList> taskDetailsUpdated;
    LinearLayout taskListLayout;
    RecyclerView task_view;
    SwipeRefreshLayout pullToRefresh;
    LinearLayout noTasks;
    TextView txt_distance_travelled;
    Filter filter = new Filter();
    Context ctx;
    AlertDialog alertDialog;

    FloatingActionButton fab_newtask, fab_scannewtask, fab_bar_code;
    View distancView;
    UpdateAppDialog updateAppDialog;
    GEnRouteManager manager;
    int limit = 10;
    int page =1;

    Boolean isAPICall = true;

    Boolean isCall = true;

    private LinearLayoutManager layoutManager;

    TasksListUpdatedAdapter tasksListAdapter;

    /**
     * Receiver for broadcasts sent by {@link IdleService}.
     */
    private NewLocationReceiver newLocationReceiver;

    DecimalFormat df = new DecimalFormat("#.##");


    private class NewLocationReceiver extends BroadcastReceiver {
        private float distanceTravelled = 0;

        @Override
        public void onReceive(Context context, Intent intent) {



//            distanceTravelled += intent.getFloatExtra(IdleService.EXTRA_LOCATION, 0.0f);
//            //String gpsStatus = intent.getStringExtra(IdleService.GPS_STATUS);
//            System.out.println("service Distance -- " + distanceTravelled);
//            txt_distance_travelled.setVisibility(View.GONE);
//            if (distanceTravelled != 0.0) {
//                df.setRoundingMode(RoundingMode.CEILING);
//                txt_distance_travelled.setText("Distance: " + (df.format(distanceTravelled / 1000)) + " Km");
//            } else
//                txt_distance_travelled.setText("Distance: 0.0 Km");



        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_navigation);

        newLocationReceiver = new NewLocationReceiver();

        fab_bar_code = findViewById(R.id.fab_bar_code);
        fab_newtask = findViewById(R.id.fab_newtask);
        fab_scannewtask = findViewById(R.id.fab_scannewtask);
        fab_newtask.setVisibility(View.VISIBLE);
        fab_scannewtask.setVisibility(View.GONE);
        fab_bar_code.setVisibility(View.GONE);
        fab_newtask.setOnClickListener(v -> createNewTask());
        fab_scannewtask.setOnClickListener(v -> createNewTaskFromScan());
        fab_bar_code.setOnClickListener(v -> showUniversalProductDialog());

        ctx = this;

        AppHelper.init(getApplicationContext());

        //gpsTracker = new GPSTracker(NavigationActivity.this);

        initView();
        initBroadCast();
        get_CurrentDate();
    }

    private void showUniversalProductDialog() {
        upcDialog = new Dialog(NavigationActivity.this);
        upcDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        upcDialog.setContentView(R.layout.dialog_bar_code);
        EditText etUPC = upcDialog.findViewById(R.id.etUPC);
        Button btn_cancel = upcDialog.findViewById(R.id.btn_cancel);
        Button btn_update = upcDialog.findViewById(R.id.btn_update);

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etUPC.getText().toString().isEmpty()) {
                    etUPC.setError(getString(R.string.err_upc));
                } else {
                    createNewDeliveryTask(etUPC.getText().toString());
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upcDialog.dismiss();
            }
        });

        upcDialog.show();
        Window window = upcDialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void createNewTask() {
        Intent in = new Intent(NavigationActivity.this, CreateNewTaskActivity.class);
        startActivityForResult(in, NEW_TASK_CREATION);
    }

    private void createNewTaskFromScan() {
        Intent in = new Intent(NavigationActivity.this, BarCodeActivity.class);
        in.putExtra("from", "new_task_scan");
        startActivityForResult(in, SCAN_TASK_CREATION);
    }

    private void createNewDeliveryTask(String barcodeValue) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, 3);

        CreateNewTask createNewTask = new CreateNewTask();
        createNewTask.setName("Guest");
        createNewTask.setPhone("+91 9876543210");
        createNewTask.setEmail("guest@mailinator.com");
        createNewTask.setOrderId(barcodeValue);

        Address address = new Address();
        String driverAddress = AppUtils.getCompleteAddressString(NavigationActivity.this, loginPrefManager.getDoubleLatitude(), loginPrefManager.getDoubleLongitude());
        if (driverAddress.isEmpty())
            address.setFormattedAddress("Coimbatore, Tamil Nadu, India");
        else
            address.setFormattedAddress(driverAddress);

        Location location = new Location();

        location.setLat(Double.valueOf(loginPrefManager.getDoubleLatitude()));
        location.setLng(Double.valueOf(loginPrefManager.getDoubleLongitude()));

        Geometry geometry = new Geometry();
        geometry.setLocation(location);
        address.setGeometry(geometry);

        createNewTask.setAddress(address);

        DriverImageOption driverImageOption = new DriverImageOption();
        driverImageOption.setExist(false);
        driverImageOption.setMandatory(false);

        createNewTask.setDriverImageOption(driverImageOption);
        createNewTask.setImages(null);

        createNewTask.setDescription("");
        String fromDate = simpleDateFormat.format(calendar.getTime());
        String toDate = "";

        createNewTask.setDate(fromDate);
        createNewTask.setEndDate(toDate);

        createNewTask.setManager(loginPrefManager.getManagerID());
        createNewTask.setPinCode(loginPrefManager.getPinCode());
        createNewTask.setColor("#f57f17");

        TimeZone timeZone = calendar.getTimeZone();
        createNewTask.setTimezone(timeZone.getID());

        createNewTask.setBusinessType(1);
        createNewTask.setManual(true);
        createNewTask.setTaskListing(true);
        createNewTask.setPickup(true);

        show_loader();

        apiService.createNewTask(loginPrefManager.getCogintoToken(),createNewTask).enqueue(new Callback<CreateNewTask>() {
            @Override
            public void onResponse(@NotNull Call<CreateNewTask> call, @NotNull Response<CreateNewTask> response) {
                dismiss_loader();
                try {
                    if (response.code() == 200) {
                        upcDialog.dismiss();
                        Toast.makeText(NavigationActivity.this, getString(R.string.task_created), Toast.LENGTH_SHORT).show();
                      if (isAPICall) {
                          getTaskList();
                      }


                    } else if (response.raw().code() == 401) {
                        findCurrent();
                    } else if (response.raw().code() == 429) {
                        show_error_response(getString(R.string.error_exist_mobile));
                    } else if (response.code() == 494) {
                        showAlertDialog(NavigationActivity.this);
                    } else if (response.code() == 421) {
                        show_error_response(response.errorBody().string());
                    } else {
                        show_error_response(response.body().getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<CreateNewTask> call, @NotNull Throwable t) {
                dismiss_loader();
                showShortMessage(t.getMessage());
            }
        });
    }

    private void initView() {
        View distancView = findViewById(R.id.inc_distance_view);
        txt_distance_travelled = distancView.findViewById(R.id.txt_distance_travelled);
        if (BuildConfig.DEBUG)
            //here
            distancView.setVisibility(View.GONE);

        noTasks = findViewById(R.id.llNoTasksMsg);
        task_view = findViewById(R.id.task_list);
        pullToRefresh = findViewById(R.id.pull_refresh_taskList);
        pullToRefresh.setOnRefreshListener(() -> {
            getDriverDetails();
            pullToRefresh.setRefreshing(false);
        });


        layoutManager = new LinearLayoutManager(NavigationActivity.this);

        task_view.setLayoutManager(layoutManager);
        tvTitle.setText(getString(R.string.nav_task));
        toolbar.setTitle("");
        taskListLayout = findViewById(R.id.layout_task_list);


        task_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // Check if we've reached the end of the current list

                Timber.tag("item_positions").e(layoutManager.findLastVisibleItemPosition() + ",," + tasksListAdapter.getItemCount());
                if (layoutManager.findLastVisibleItemPosition() == tasksListAdapter.getItemCount() - 1&&isCall&&isAPICall) {
                    // Call your API to fetch the next page of data
                    //fetchNextPageData();
                    page=page+1;
                    Timber.tag("calling_api").e("true");

                   // if(isAPICall) {
                        getTaskList();
                   // }

                }
            }
        });




    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_menus, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Navigate to Filter task screen
            case R.id.menuFilterTask:
//                startActivity(new Intent(NavigationActivity.this, FilterActivity.class));

                loginPrefManager.setStringValue("filter", "1");
                Intent in = new Intent(NavigationActivity.this, FilterActivity.class);
                startActivityForResult(in, 35);
                return true;
            //Navigate to Task location screen
//            case R.id.menuShowLocation:
//                startActivity(new Intent(NavigationActivity.this, MapTaskActivity.class));
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initBroadCast() {
        Timber.e("init");
        BroadcastReceiver status_update_reciver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String fav_type = Objects.requireNonNull(intent.getExtras()).getString("start_order");
                    if (fav_type != null) {

                        Timber.e("fav_type %s", fav_type);

                        check_current_activity();
                        getDriverDetails();
                    }
                }
            }

        };

        LocalBroadcastManager.getInstance(NavigationActivity.this).registerReceiver(status_update_reciver, new IntentFilter("updated"));
    }


//    @Override
//    public void taskItemClick(TaskList taskList) {
////        loginPrefManager.setGlympseID("glympseID", "" + taskList.getDocs());
////
////        loginPrefManager.setStringValue("activity", "navigation");
////        int taskStatus = taskList.getDocs().getTaskStatus();
////        if (taskStatus == 2 || taskStatus == 3 || taskStatus == 10)
////            startActivityForResult(new Intent(this, TaskDetailsActivity.class)
////                    .putExtra("task_id", taskList.getDocs().get(0).getId())
////                    .putExtra("glympse_id", taskList.getGlympseId())
////                    .putExtra("activity", "navigation"), TASK_DETAILS_CHECK);
////        else
////            startActivity(new Intent(this, TaskDetailsActivity.class)
////                    .putExtra("task_id", taskList.getId())
////                    .putExtra("activity", "navigation")
////                    .putExtra("glympse_id", taskList.getGlympseId()));
//
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("onActivityResult: " + requestCode);
        page=1;
        limit=10;
        if (requestCode == TASK_DETAILS_CHECK) {
            getDriverDetails();
        } else if (requestCode == NEW_TASK_CREATION) {
            if (isAPICall) {
                getTaskList();
            }
        } else if (requestCode == SCAN_TASK_CREATION && resultCode == RESULT_OK) {
            if (isAPICall) {
                getTaskList();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    public void getDriverDetails() {
        try {

            Timber.tag("navigation_profile").e("profile");

            if (!isFinishing()) show_loader();
            apiService.getManagerProfileDetails(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken()).enqueue(new Callback<ProfileResponse>() {
                @Override
                public void onResponse(@NonNull Call<ProfileResponse> call, @NonNull Response<ProfileResponse> response) {
                    if (!isFinishing()) dismiss_loader();
                    try {
                        if (response.raw().code() == 200) {

                            loginPrefManager.setCategoryValue(new Gson().toJson(response.body().getCategoriesList()));


                            loginPrefManager.setStringValue("driver_name", "" + response.body().getName());

                           // Log.e("notes_field_response",String.valueOf(response.body().isCustomerNotesEnabled()));




                           // fetchProfileDetails(loginPrefManager, response.body());
                            setAdapter();
//                            if (response.body().isDriverCreateOwnTask()) {
//                                fab_newtask.setVisibility(View.VISIBLE);
//                            } else {
//                                fab_newtask.setVisibility(View.GONE);
//                            }
//                            if (response.body().isDriverCreateBarcodeTask()) {
//                                fab_scannewtask.setVisibility(View.VISIBLE);
//                                fab_bar_code.setVisibility(View.VISIBLE);
//                            } else {
//                                fab_scannewtask.setVisibility(View.GONE);
//                                fab_bar_code.setVisibility(View.GONE);
//                            }
                            LocalBroadcastManager.getInstance(NavigationActivity.this).sendBroadcast(new Intent("changeByAdmin").putExtra("profile", "3"));
                           // connectToAppLogic(response.body().getAppLozicContactList());
                           if (isAPICall) {
                               getTaskList();
                           }
                          //alert dialog for user already logged in
                        } else {
                            findCurrent();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (!isFinishing()) dismiss_loader();
                    }

                }

                @Override
                public void onFailure(@NotNull Call<ProfileResponse> call, @NotNull Throwable t) {
                    if (!isFinishing()) dismiss_loader();
                    Timber.e(t);

                }
            });

        } catch (Exception e) {

            if (!isFinishing()) dismiss_loader();

            Timber.e(e);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("login", "----" + loginPrefManager.getGlympseEnabled());

        setGlympseDetails();

    }

    public void setGlympseDetails() {

        Log.e("userName", "" + loginPrefManager.getGlympseUserName());
        Log.e("userpswd", "" + loginPrefManager.getGlympseUserPswd());

        if (!loginPrefManager.getGlympseUserName().isEmpty() && !loginPrefManager.getGlympseUserPswd().isEmpty()) {
            EnRouteWrapper.instance().init(getApplicationContext());
            int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            if (result != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = new String[1];
                permissions[0] = Manifest.permission.ACCESS_FINE_LOCATION;
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
            startManager();
        }
    }

    private void startManager() {
        manager = EnRouteWrapper.instance().manager();
        manager.overrideLoggingLevels(CC.NONE, CC.INFO);
        manager.addListener(this);
        manager.setAuthenticationMode(EnRouteConstants.AUTH_MODE_CREDENTIALS);
        manager.start();


    }


    public void eventsOccurred(GSource source, int listener, int events, Object param1, Object param2) {
        if (EE.LISTENER_ENROUTE_MANAGER == listener) {
            if (0 != (EE.ENROUTE_MANAGER_STARTED & events)) {
                System.out.print("En Route Event: ENROUTE_MANAGER_STARTED");
            }

            if (0 != (EE.ENROUTE_MANAGER_AUTHENTICATION_NEEDED & events)) {
                // We need to wait for the user to login.
                handleLogin();
            }
            if (0 != (EE.ENROUTE_MANAGER_LOGIN_COMPLETED & events)) {
                System.out.print("En Route Event: ENROUTE_MANAGER_LOGIN_COMPLETED");
            }

            if (0 != (EE.ENROUTE_MANAGER_SYNCED & events)) {
                System.out.print("En Route Event: ENROUTE_MANAGER_SYNCED");
            }


            if (0 != (EE.ENROUTE_MANAGER_LOGGED_OUT & events)) {
                System.out.print("En Route Event: ENROUTE_MANAGER_LOGGED_OUT");

                // Either the user logged out manually ()
                // (reason == EnRouteConstants.LOGOUT_REASON_USER_ACTION),
                // or entered invalid credentials
                // (reason == EnRouteConstants.LOGOUT_REASON_INVALID_CREDENTIALS).
                long reason = ((Long) param1).longValue();
                loggedOut(reason);
            }
            if (0 != (EE.ENROUTE_MANAGER_STOPPED & events)) {
                System.out.print("En Route Event: ENROUTE_MANAGER_STOPPED");

                startManager();
            }

            if (0 != (EE.TASKS_TASK_LIST_CHANGED & events)) {
                System.out.print("En Route Event: TASKS_TASK_LIST_CHANGED");


            }
        }
    }

    public void handleLogin() {
        Log.e("userName", "" + loginPrefManager.getGlympseUserName());
        Log.e("userpswd", "" + loginPrefManager.getGlympseUserPswd());
        EnRouteWrapper.instance().manager().loginWithCredentials(loginPrefManager.getGlympseUserName(),
                loginPrefManager.getGlympseUserPswd());
    }

    public void loggedOut(long reason) {
//            showInvalidCredentialsDialog();

    }

    private void showInvalidCredentialsDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Invalid Credentials");
        builder.setMessage("Please try again.");
        builder.setCancelable(true);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SharedPreferences preferences = getSharedPreferences("autter_prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();

                Intent intent = new Intent(NavigationActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    protected void onPause() {
        if (BuildConfig.DEBUG)
            LocalBroadcastManager.getInstance(this).unregisterReceiver(newLocationReceiver);
        super.onPause();
        if (loginPrefManager.getGlympseEnabled()) {
            manager.setActive(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        limit = 10;
        page = 1;
        isCall = true;
        if (loginPrefManager.getGlympseEnabled()) {
            manager.setActive(true);
        }

        if (loginPrefManager.getStringValue("filter_values").equals("1")) {
            Gson gson = new Gson();
            String data = loginPrefManager.getStringValue("filter_data");
            filter = gson.fromJson(data, Filter.class);

            FilterMain mainFilter = new FilterMain();
            mainFilter.setFilter(filter);
            callTasks(mainFilter);

        }/*else {
            getDriverDetails(); //ramesh babu // commented for calling two times
        }*/

//        if (BuildConfig.DEBUG)
//            LocalBroadcastManager.getInstance(this).registerReceiver(newLocationReceiver, new IntentFilter(IdleService.ACTION_BROADCAST));

    }


    public void getTaskList() {
        try {
            isAPICall= false;
            List<String> dateFilterList = new ArrayList<>();
            FilterMain mainFilter = new FilterMain();
            Filter filter = new Filter();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

            dateFilterList.add(sdf.format(new Date()));
            dateFilterList.add(sdf.format(new Date()));
            filter.setDateRange(dateFilterList);
            filter.setSortByTIme(0);
            filter.setSortByDistance(0);

            filter.setSearch("");
            filter.setStatusFilter(new ArrayList<>());
            mainFilter.setFilter(filter);

            if (loginPrefManager.getStringValue("filter_values").equals("1")) {
                Gson gson = new Gson();
                String data = loginPrefManager.getStringValue("filter_data");
                filter = gson.fromJson(data, Filter.class);
                FilterMain mainFilter1 = new FilterMain();
                mainFilter1.setFilter(filter);
                callTasks(mainFilter1);
            } else {
                callTasks(mainFilter);
            }
        } catch (Exception e) {
            dismiss_loader();
            Timber.e(e);
        }
    }


    private void callTasks(FilterMain mainFilter) {
        try {
            if (!isFinishing()) show_loader();
            mainFilter.setTimezone(TimeZone.getDefault().getID());

            mainFilter.setLimit(10);
            mainFilter.setPage(page);


            Timber.tag("requestPage").e(String.valueOf(page));

            Log.e("mainfilter", new Gson().toJson(mainFilter));

            apiService.getNewTaskList(loginPrefManager.getCogintoToken(),loginPrefManager.getDeviceToken(), mainFilter).enqueue(new Callback<ManagerTaskResponse>() {
                @Override
                public void onResponse(@NotNull Call<ManagerTaskResponse> call, @NotNull Response<ManagerTaskResponse> response) {

                    if (!isFinishing()) dismiss_loader();
                    try {
                        isAPICall= true;
                        if (response.raw().code() == 200) {
                            List<Doc> taskDetails = response.body().getDocs();
                            taskDetailsUpdated = new ArrayList<>();

                           /* for (int i = 0; i < taskDetails.size(); i++) {
                                if(taskDetails.get(i).getReferenceId()!=null) {
                                    taskDetailsWithRefId.add(taskDetails.get(i));
                                } else {
                                    taskDetailsWithOutRefId = new ArrayList<>();
                                    taskDetailsWithOutRefId.add(taskDetails.get(i));
                                    TaskUpdatedList taskUpdatedList = new TaskUpdatedList();
                                    taskUpdatedList.setTaskDetails(taskDetailsWithOutRefId);
                                    taskDetailsUpdated.add(taskUpdatedList);
                                }
                            }*/

//                            if(!taskDetailsWithRefId.isEmpty()) {
//                                getFilteredTaskList(taskDetailsWithRefId);
//                            }

                          //  if (taskDetails.size() > 0) {

                                //getFilteredTaskList(taskDetails);
                                if (page==1) {
                                    if (taskDetails.size()>0) {
                                        isCall = true;
                                        noTasks.setVisibility(View.GONE);
                                        task_view.setVisibility(View.VISIBLE);

                                        Collections.sort(taskDetails, new Comparator<Doc>() {
                                            @Override
                                            public int compare(Doc item1, Doc item2) {
                                                int statusKey1 = item1.getTaskStatus();
                                                int statusKey2 = item2.getTaskStatus();


                                                return Integer.compare(statusKey2, statusKey1);
                                            }
                                        });

                                        Collections.reverse(taskDetails);

                                        tasksListAdapter = new TasksListUpdatedAdapter(NavigationActivity.this, taskDetails);
                                        // tasksListAdapter.setAdapterClickInterface(NavigationActivity.this);
                                        task_view.setAdapter(tasksListAdapter);
                                    }else {
                                        isCall = false;
                                        task_view.setVisibility(View.GONE);
                                        noTasks.setVisibility(View.VISIBLE);
                                    }
                                }else{
                                    if (taskDetails.size()>0) {
                                        isCall = true;

                                        Collections.sort(taskDetails, new Comparator<Doc>() {
                                            @Override
                                            public int compare(Doc item1, Doc item2) {
                                                int statusKey1 = item1.getTaskStatus();
                                                int statusKey2 = item2.getTaskStatus();


                                                return Integer.compare(statusKey2, statusKey1);
                                            }
                                        });

                                        Collections.reverse(taskDetails);

                                        tasksListAdapter.addData(taskDetails);
                                    }else{
                                        isCall = false;
                                    }
                                }
                            //}
                        } else if (response.raw().code() == 401) {
                            findCurrent();
                        }//alert dialog for user already logged in
                        else if (response.code() == 494) {
                            showAlertDialog(NavigationActivity.this);
                        } else {
                            show_error_response(getString(R.string.error_response));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ManagerTaskResponse> call, @NotNull Throwable t) {
                    if (!isFinishing()) dismiss_loader();
                }
            });
        } catch (Exception e) {
            if (!isFinishing()) dismiss_loader();
        }
    }

    private void getFilteredTaskList(List<Doc> taskDetails) {
        TaskUpdatedList model = new TaskUpdatedList();
        List<Doc> tempTaskDetails = new ArrayList<>();
//        if (taskDetails.get(0).getReferenceId() != null) {
//            String referenceId = taskDetails.get(0).getReferenceId();
//            tempTaskDetails.add(taskDetails.get(0));
//            taskDetails.remove(taskDetails.get(0));
//            for (int i = 0; i < taskDetails.size(); i++) {
//                if (taskDetails.get(i).getReferenceId() != null) {
//                    if (referenceId.equals(taskDetails.get(i).getReferenceId())) {
//                        tempTaskDetails.add(taskDetails.get(i));
//                        taskDetails.remove(taskDetails.get(i));
//                    }
//                }
//            }
//            model.setTaskDetails(tempTaskDetails);
//            taskDetailsUpdated.add(model);
//        } else {
//            tempTaskDetails = new ArrayList<>();
//            tempTaskDetails.add(taskDetails.get(0));
//            taskDetails.remove(taskDetails.get(0));
//            TaskUpdatedList taskUpdatedList = new TaskUpdatedList();
//            taskUpdatedList.setTaskDetails(tempTaskDetails);
//            taskDetailsUpdated.add(taskUpdatedList);
//        }
        if (!taskDetails.isEmpty()) {
            getFilteredTaskList(taskDetails);
        }
    }

//    private void getNormalTaskList(List<TaskList> taskDetails) {
////        for (int i = 0; i < taskDetails.size(); i++) {
////            if (taskDetails.get(i).getReferenceId() == null) {
////                TaskUpdatedList taskUpdatedList = new TaskUpdatedList();
////                taskUpdatedList.setTaskDetails(taskDetails);
////                taskDetailsUpdated.add(taskUpdatedList);
////            }
////        }
//    }


    @Override
    public void onBackPressed() {
        exit_app();
    }


    private void showDriverStateChanged(String msg) {
        Snackbar snackbar = Snackbar.make(taskListLayout, msg, Snackbar.LENGTH_LONG).
                setAction("", null);
        snackbar.show();
    }


    private void get_CurrentDate() {

        String local_date = loginPrefManager.getStringValue("current_date");

        if (!local_date.equals("")) {

            Date date = new Date();
            String current_date = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss", Locale.ENGLISH).format(date);

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss", Locale.ENGLISH);
            SimpleDateFormat current_dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date convertedDate;
            Date convertedDate2;
            try {
                convertedDate = current_dateFormat.parse(local_date);
                convertedDate2 = dateFormat.parse(current_date);
                if (convertedDate2.equals(convertedDate)) {
                    long difference = Math.abs(convertedDate.getTime() - convertedDate2.getTime());
                    long differenceDates = difference / (24 * 60 * 60 * 1000);

                    //Convert long to String
                    String dayDifference = Long.toString(differenceDates);

                    Timber.e("HERE: %s", dayDifference);
                } else if (convertedDate2.before(convertedDate)) {

                } else {
                    printDifference(convertedDate, convertedDate2);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {

            Date date = new Date();
            String current_date = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss", Locale.ENGLISH).format(date);

            loginPrefManager.setStringValue("current_date", current_date);


        }


    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        assert manager != null;
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public void printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;


        long minutes = (different / 1000) / 60;

        Timber.e("-%s", minutes);

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf("%d days, %d hours, %d minutes, %d seconds%n", elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
    }

    private void check_current_activity() {
        ActivityManager activityManager = (ActivityManager) getApplication().getSystemService(Activity.ACTIVITY_SERVICE);

     if (activityManager.getRunningTasks(26).size()>0) {
         ActivityManager.RunningTaskInfo runningTaskInfo = activityManager.getRunningTasks(26).get(0);
         String activity = runningTaskInfo.topActivity.getClassName();

         Timber.e(activity);
         if (activity.equals("com.app.deliforcemanager.Activities.NewTaskActivity")) {
             finish();
         }
     }

    }
}