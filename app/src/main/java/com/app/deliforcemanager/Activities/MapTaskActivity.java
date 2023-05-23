package com.app.deliforcemanager.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.app.deliforcemanager.BaseActivity.BaseDrawerActivity;
import com.app.deliforcemanager.LocalizationActivity.LocalizationActivity;
import com.app.deliforcemanager.ModelClass.ManagerTaskList.Doc;
import com.app.deliforcemanager.ModelClass.ManagerTaskList.ManagerTaskResponse;
import com.app.deliforcemanager.ModelClass.TaskDetailsApi.Address;
import com.app.deliforcemanager.ModelClass.TaskDetailsApi.TaskDetails;
import com.app.deliforcemanager.ModelClass.TaskDetailsApi.TaskList;
import com.app.deliforcemanager.R;
import com.app.deliforcemanager.RawHeaders.TaskInfo.Filter;
import com.app.deliforcemanager.RawHeaders.TaskInfo.FilterMain;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapTaskActivity extends BaseDrawerActivity implements OnMapReadyCallback {

    public static final String TAG = "TasksLocationFragment";
    private GoogleMap mapForTasksLocation;
    Bitmap mMarkerIcon, Current_location;
    List<Doc> taskDetails;
    Filter filter = new Filter();
    private Marker marker_rder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_task);

        tvTitle.setText(getString(R.string.nav_task));
        toolbar.setTitle("");

        initMap();
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.routes_google_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_location_menus, menu);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (loginPrefManager.getStringValue("filter_values").equals("1")) {
            Gson gson = new Gson();
            String data = loginPrefManager.getStringValue("filter_data");
            filter = gson.fromJson(data, Filter.class);

            FilterMain mainFilter = new FilterMain();
            mainFilter.setFilter(filter);
            callTasks(mainFilter);
        } else {
            getTaskList();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Navigate to Filter task screen
            case R.id.menuSearchTasks:
                startActivity(new Intent(MapTaskActivity.this, FilterActivity.class));
                return true;
            //Navigate to Task location screen
            case R.id.menuGridTasks:
                startActivity(new Intent(MapTaskActivity.this, NavigationActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startHomeScreen();
    }

    private void getTaskList() {

        try {

            List<String> dateFilterList = new ArrayList<>();
            FilterMain mainFilter = new FilterMain();
            Filter filter = new Filter();

            // display tasks wrt today's date
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
            Date date = new Date();
            String currentDate = formatter.format(date);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Log.e("Today's Date", "--> " + sdf.format(new Date()));
            dateFilterList.add(sdf.format(new Date()));
            dateFilterList.add(sdf.format(new Date()));
            filter.setDateRange(dateFilterList);

            filter.setSearch("");
            filter.setStatusFilter(new ArrayList<>());
            mainFilter.setFilter(filter);

            Log.e("filter", "--" + loginPrefManager.getStringValue("filter_values"));

            if (loginPrefManager.getStringValue("filter_values").equals("1")) {
                Gson gson = new Gson();
                String data = loginPrefManager.getStringValue("filter_data");
                filter = gson.fromJson(data, Filter.class);

                FilterMain mainFilter1 = new FilterMain();
                mainFilter.setFilter(filter);
                callTasks(mainFilter1);
                return;

            }
            if (loginPrefManager.getStringValue("filter_values").equalsIgnoreCase("")) {
                callTasks(mainFilter);
            }


        } catch (Exception e) {
            dismiss_loader();
            Log.e("e", e.getMessage());
        }

    }

    private void callTasks(FilterMain mainFilter) {
        try {
            show_loader();

            //Log.e("----", new Gson().toJson(mainFilter));

            if (mapForTasksLocation != null) {
                mapForTasksLocation.clear();
            }
            mainFilter.setTimezone(TimeZone.getDefault().getID());
            apiService.getNewTaskList(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken(), mainFilter).enqueue(new Callback<ManagerTaskResponse>() {
                @Override
                public void onResponse(@NotNull Call<ManagerTaskResponse> call, @NotNull Response<ManagerTaskResponse> response) {
                    //Log.e("response", String.valueOf(response.raw().code()));
                    dismiss_loader();
                    try {
                        if (response.raw().code() == 200) {

                            taskDetails = response.body().getDocs();

                            LatLngBounds.Builder builder = new LatLngBounds.Builder();

                            //Log.e("taskDetails", String.valueOf(taskDetails.size()));
                            List<String> taskIds = new ArrayList<>();

                            if (taskDetails.size() > 0) {
                                for (int i = 0; i < taskDetails.size(); i++) {
                                    double lat = taskDetails.get(i).getAddress().getGeometry().getLocation().getLat();
                                    double longi = taskDetails.get(i).getAddress().getGeometry().getLocation().getLng();

                                    LatLng order_latlng = new LatLng(lat, longi);
                                    int status = taskDetails.get(i).getTaskStatus();

                                    if (status == 9) {
                                        mMarkerIcon = BitmapFactory.decodeResource(getResources(), R.drawable.cancel);
                                    }
                                    if (status == 2) {
                                        mMarkerIcon = BitmapFactory.decodeResource(getResources(), R.drawable.assigned);
                                    }
                                    if (status == 3) {
                                        mMarkerIcon = BitmapFactory.decodeResource(getResources(), R.drawable.accept);
                                    }
                                    if (status == 4) {
                                        taskIds.add(taskDetails.get(i).getId());
                                        mMarkerIcon = BitmapFactory.decodeResource(getResources(), R.drawable.started);
                                    }

                                    if (status == 6) {
                                        mMarkerIcon = BitmapFactory.decodeResource(getResources(), R.drawable.success);
                                    }
                                    if (status == 7) {
                                        mMarkerIcon = BitmapFactory.decodeResource(getResources(), R.drawable.failed);
                                    }
                                    if (status == 10) {
                                        mMarkerIcon = BitmapFactory.decodeResource(getResources(), R.drawable.acknolwledge);
                                    }

                                    marker_rder = mapForTasksLocation.addMarker(new MarkerOptions().position(order_latlng).
                                            icon(BitmapDescriptorFactory.fromBitmap(mMarkerIcon)));
                                    marker_rder.setTag(response.body().getDocs().get(i));
                                    builder.include(marker_rder.getPosition());
                                }

                                if (taskIds.size() > 0) {
                                    Set task_ids = new HashSet<>(taskIds);
                                    loginPrefManager.setTaskIds(task_ids);
                                }
                                LatLng current_lat_ng = new LatLng(LocalizationActivity.current_latitude, LocalizationActivity.current_longitude);
                                Current_location = BitmapFactory.decodeResource(getResources(), R.drawable.driver);
                                marker_rder = mapForTasksLocation.addMarker(new MarkerOptions().position(current_lat_ng).icon(BitmapDescriptorFactory.fromBitmap(Current_location)));
                                marker_rder.setTag("empty");
                                builder.include(marker_rder.getPosition());
                                LatLngBounds bounds = builder.build();


                                int width = getResources().getDisplayMetrics().widthPixels;
                                int height = getResources().getDisplayMetrics().heightPixels;
                                int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen
                                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                                mapForTasksLocation.animateCamera(cu);
                                setCustomwindow();
                            } else {
                                mapForTasksLocation.addMarker(new MarkerOptions().position(new LatLng(LocalizationActivity.current_latitude, LocalizationActivity.current_longitude)).snippet("Current Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.driver)));
                                mapForTasksLocation.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(LocalizationActivity.current_latitude, LocalizationActivity.current_longitude), 16));
                            }


                        }
                        //alert dialog for user already logged in
                        else if (response.code() == 494) {
                            showAlertDialog(MapTaskActivity.this);
                        } else if (response.raw().code() == 401) {
                            findCurrent();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<ManagerTaskResponse> call, Throwable t) {
                    dismiss_loader();
                    Log.e("onFailure", t.getMessage());
                }
            });
        } catch (Exception e) {
            dismiss_loader();

            Log.e("onException", e.getMessage());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapForTasksLocation = googleMap;

        String map_style = loginPrefManager.getStringValue("driver_map_style");

        if (map_style.equalsIgnoreCase("2")) {
            mapForTasksLocation.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.night_mode_style));
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mapForTasksLocation.setMyLocationEnabled(true);

        mapForTasksLocation.getUiSettings().setZoomControlsEnabled(true);
        getTaskList();
    }

    private void setCustomwindow() {
        mapForTasksLocation.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public View getInfoContents(Marker marker) {


                View view = null;

                Log.e("getTag", "" + marker.getTag());

                if (marker.getTag().equals("empty")) {

                } else {
                    TaskList detailDto = (TaskList) marker.getTag();
                    view = View.inflate(MapTaskActivity.this, R.layout.custom_window, null);
                    TextView tvTaskCompletionTime, tvTaskType, textView, tvCustName, tvCustLocation;
                    tvTaskCompletionTime = view.findViewById(R.id.tv_task_time);
                    tvTaskType = view.findViewById(R.id.tv_task_name);
                    textView = view.findViewById(R.id.tv_task_status);
                    tvCustName = view.findViewById(R.id.tv_cust_name);
                    tvCustLocation = view.findViewById(R.id.tv_cust_address);
                    if (detailDto != null) {
                        tvCustName.setText(detailDto.getName());
                        Address details = detailDto.getAddress();
                        String custAddress = details.getFormattedAddress();
                        tvCustLocation.setText(custAddress);
                        int taskStatus = detailDto.getTaskStatus();
                        setStatus(taskStatus, textView);

                    }
                    boolean isPickUp;

                    String actualDate = detailDto.getDate();

                    Log.e("actualDate", detailDto.getDate());
                    String formattedTime = (actualDate.length() == 22) ? actualDate.substring(11, 16) : "0" + actualDate.substring(11, 16);
                    String timeAmPm = (actualDate.length() == 22) ? actualDate.substring(19, 22) : actualDate.substring(19, 21);
                    String dateToDisplay = formattedTime + timeAmPm;
                    tvTaskCompletionTime.setText(dateToDisplay);

                    int bussType = detailDto.getBusinessType();

                    switch (bussType) {

                        case 1:
                            isPickUp = detailDto.getIsPickup();
                            if (isPickUp) {
                                tvTaskType.setText(getString(R.string.task_type_pick_up));
                            } else {
                                tvTaskType.setText(getString(R.string.task_type_delivery));
                            }
                            break;
                        case 2:
                            isPickUp = Boolean.parseBoolean(null);
                            tvTaskType.setText(getString(R.string.task_type_appointments));
                            break;
                        case 3:
                            isPickUp = Boolean.parseBoolean(null);
                            tvTaskType.setText(getString(R.string.task_type_field_work));
                            break;
                    }
                }

                return view;
            }

        });

        mapForTasksLocation.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                loginPrefManager.setStringValue("activity", "map");

                TaskList detailDto = (TaskList) marker.getTag();
                startActivity(new Intent(MapTaskActivity.this, TaskDetailsActivity.class)
                        .putExtra("task_id", detailDto.getId())
                        .putExtra("activity", "map")
                        .putExtra("glympse_id", ""+detailDto.getGlympseId()));
                finish();

            }
        });
    }

    private void setStatus(int postion, TextView textView) {
        switch (postion) {
            case 2:
                textView.setTextColor(ContextCompat.getColor(MapTaskActivity.this, R.color.colorTaskAssigned));
                textView.setText(getString(R.string.task_status_assigned));
                break;
            case 3:
                textView.setTextColor(ContextCompat.getColor(MapTaskActivity.this, R.color.colorTaskAssigned));
                textView.setText(getString(R.string.task_status_accepted));
                break;
            case 4:
                textView.setTextColor(ContextCompat.getColor(MapTaskActivity.this, R.color.colorTaskStarted));
                textView.setText(getString(R.string.task_status_started));
                break;
            case 5:
                textView.setText(getString(R.string.task_status_in_progress));
                break;
            case 6:
                textView.setTextColor(ContextCompat.getColor(MapTaskActivity.this, R.color.colorPrimary));
                textView.setText(getString(R.string.task_status_success));
                break;
            case 7:
                textView.setTextColor(ContextCompat.getColor(MapTaskActivity.this, R.color.colorTaskCancelled));
                textView.setText(getString(R.string.task_status_failed));
                break;
            case 9:
                textView.setTextColor(ContextCompat.getColor(MapTaskActivity.this, R.color.colorTaskCancelled));
                textView.setText(getString(R.string.task_status_cancelled));
                break;
            case 10:
                textView.setTextColor(ContextCompat.getColor(MapTaskActivity.this, R.color.colorTaskAcknowledged));
                textView.setText(getString(R.string.task_status_acknowledge));
                break;
              /*  default:
                    textView.setTextColor(ContextCompat.getColor(context, R.color.colorTaskAssigned));
                    textView.setText(getString(R.string.task_status_assigned));*/
        }

    }

}
