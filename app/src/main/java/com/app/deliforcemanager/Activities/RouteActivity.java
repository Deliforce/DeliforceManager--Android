package com.app.deliforcemanager.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.app.deliforcemanager.BaseActivity.BaseDrawerActivity;
import com.app.deliforcemanager.LocalizationActivity.LocalizationActivity;
import com.app.deliforcemanager.ModelClass.TaskRoutesApi.Address;
import com.app.deliforcemanager.ModelClass.TaskRoutesApi.TaskList;
import com.app.deliforcemanager.ModelClass.TaskRoutesApi.TaskRoutes;
import com.app.deliforcemanager.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.app.deliforcemanager.Utils.DeliforceConstants.ROUTE_RESULT_CODE;

public class RouteActivity extends BaseDrawerActivity implements OnMapReadyCallback {

    GoogleMap map;

    List<LatLng> balanced_drop_array;

    List<TaskList> taskLists;
    List<TaskList> total_task_list;

    LatLng last_drop;

    List<Integer> way_point;

    private Marker marker_rder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        init_toolbar();

        initMap_view();
    }

    private void initMap_view() {

        try {
            SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.routes_google_map);
            supportMapFragment.getMapAsync(RouteActivity.this);

        } catch (Exception e) {
            Timber.e(e);
        }
    }

    private void init_toolbar() {
        tvTitle.setText(getString(R.string.route));
        toolbar.setTitle("");

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        try {
            map = googleMap;

        } catch (Exception e) {
            Timber.e(e);
        }

        way_point = new ArrayList<>();
        getTaskroutes();

    }

    public static BitmapDrawable writeOnDrawable(Context context, int drawableId, String text) {


        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), drawableId).copy(Bitmap.Config.ARGB_8888, true);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        paint.setTextAlign(Paint.Align.CENTER);
        Canvas canvas = new Canvas(bm);

        canvas.drawText(text, bm.getWidth() / 2 //x position
                , bm.getHeight() / 2  // y position
                , paint);
        return new BitmapDrawable(context.getResources(), bm);
    }

    private void getTaskroutes() {
        try {
            show_loader();
            apiService.getRoutes(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken(), TimeZone.getDefault().getID()).enqueue(new Callback<TaskRoutes>() {
                @Override
                public void onResponse(@NotNull Call<TaskRoutes> call, @NotNull Response<TaskRoutes> response) {
                    try {
                        if (response.code() == 200) {
                            taskLists = new ArrayList<>();
                            total_task_list = new ArrayList<>();
                            taskLists = response.body().getTaskList();
                            total_task_list = response.body().getTaskList();
                            balanced_drop_array = new ArrayList<>();

                            if (taskLists.size() > 1) {

                                for (int i = 0; i < taskLists.size(); i++) {
                                    balanced_drop_array.add(new LatLng(taskLists.get(i).getAddress().getGeometry().getLocation().getLat(), taskLists.get(i).getAddress().getGeometry().getLocation().getLng()));
                                }
                                int last = taskLists.size() - 1;
                                last_drop = new LatLng(taskLists.get(last).getAddress().getGeometry().getLocation().getLat(), taskLists.get(last).getAddress().getGeometry().getLocation().getLng());


                                draw_multi_route();

                                dismiss_loader();

                            } else if (taskLists.size() == 1) {
                                LatLng current = new LatLng(LocalizationActivity.current_latitude, LocalizationActivity.current_longitude);

                                LatLng destination = new LatLng(taskLists.get(0).getAddress().getGeometry().getLocation().getLat(), taskLists.get(0).getAddress().getGeometry().getLocation().getLng());
                                single_route(current, destination);

                            } else {
                                dismiss_loader();
                                map.addMarker(new MarkerOptions().position(new LatLng(LocalizationActivity.current_latitude, LocalizationActivity.current_longitude)).snippet("Current Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.driver)));
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(LocalizationActivity.current_latitude, LocalizationActivity.current_longitude), 16));
                            }


                        }    //alert dialog for user already logged in
                        else if (response.code() == 494) {
                            showAlertDialog(RouteActivity.this);
                        } else {

                            dismiss_loader();
                            showShortMessage(getString(R.string.empty_tasks));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                @Override
                public void onFailure(Call<TaskRoutes> call, Throwable t) {

                    dismiss_loader();

                }
            });

        } catch (Exception e) {
            dismiss_loader();
            Timber.e(e);
        }
    }

    private void draw_multi_route() {
        map.clear();

        Timber.e(String.valueOf(last_drop));


        LatLng from = new LatLng(LocalizationActivity.current_latitude,LocalizationActivity.current_longitude);





        GoogleDirection.withServerKey(getString(R.string.google_places_api_key))
                .from(from)
                .and(balanced_drop_array)
                .to(last_drop)
                .optimizeWaypoints(true)
                .transportMode(TransportMode.DRIVING)
                .execute(new DirectionCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {

                        Timber.e(direction.getStatus() + "/" + direction.getErrorMessage());




                        if (direction.getStatus().equalsIgnoreCase("ZERO_RESULTS")) {
                            showShortMessage(getString(R.string.unble_tasks));
                            map.addMarker(new MarkerOptions().position(new LatLng(LocalizationActivity.current_latitude, LocalizationActivity.current_longitude)).snippet("Current Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.driver)));
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(LocalizationActivity.current_latitude, LocalizationActivity.current_longitude), 16));
                        }
                        if (direction.isOK()) {
                            // Do something


                            way_point = direction.getRouteList().get(0).getWaypointOrderList();

                            com.akexorcist.googledirection.model.Route route = direction.getRouteList().get(0);

                            int legCount = route.getLegList().size();


                            for (int index = 0; index < legCount; index++) {

                                Leg leg = route.getLegList().get(index);
                                int position = index;
                                if (index == 0) {
                                    marker_rder = map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.driver)).position(leg.getStartLocation().getCoordination()));
                                    marker_rder.setTag("empty");
                                } else {
                                    marker_rder = map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(writeOnDrawable(RouteActivity.this, R.drawable.location_map, String.valueOf(position)).getBitmap())).position(leg.getStartLocation().getCoordination()));
                                    marker_rder.setTag(total_task_list.get(way_point.get(index - 1)));
                                }



                                List<Step> stepList = leg.getStepList();
                                ArrayList<PolylineOptions> polylineOptionList = DirectionConverter.createTransitPolyline(RouteActivity.this, stepList, 5, Color.RED, 3, Color.BLUE);
                                for (PolylineOptions polylineOption : polylineOptionList) {
                                    polylineOption.color(ContextCompat.getColor(RouteActivity.this, R.color.colorPrimary));
                                    polylineOption.width(8);
                                    map.addPolyline(polylineOption);
                                }
                            }
                            setCustomwindow();
                            setCameraWithCoordinationBounds(route);

                        } else {
                            // Do something
                            map.clear();
                            showShortMessage(getString(R.string.unble_tasks));
                            map.addMarker(new MarkerOptions().position(new LatLng(LocalizationActivity.current_latitude, LocalizationActivity.current_longitude)).snippet("Current Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.driver)));
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(LocalizationActivity.current_latitude, LocalizationActivity.current_longitude), 16));
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        // Do something
                        showShortMessage(t.getMessage());
                    }
                });
    }

    private void setCameraWithCoordinationBounds(com.akexorcist.googledirection.model.Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 15));
        dismiss_loader();
    }

    @Override
    public void onBackPressed() {

        startHomeScreen();
        finish();
    }

    private void single_route(LatLng origin, LatLng destination) {
        GoogleDirection.withServerKey(getString(R.string.google_places_api_key))
                .from(origin)
                .to(destination)
                .transportMode(TransportMode.DRIVING)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {

                        Timber.e(direction.getStatus() + "/" + direction.getErrorMessage());

                        if (direction.isOK()) {
                            Route route = direction.getRouteList().get(0);
                            marker_rder = map.addMarker(new MarkerOptions().position(origin).icon((BitmapDescriptorFactory.fromResource(R.drawable.driver))));
                            marker_rder.setTag("empty");
                            marker_rder = map.addMarker(new MarkerOptions().position(destination).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_map)));

                            marker_rder.setTag(total_task_list.get(0));
                            setCustomwindow();
                            ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
                            map.addPolyline(DirectionConverter.createPolyline(RouteActivity.this, directionPositionList, 3, ContextCompat.getColor(RouteActivity.this, R.color.colorPrimary)));
                            setCameraWithCoordinationBounds(route);


                        } else {
                            map.clear();
                            showShortMessage(getString(R.string.unble_tasks));
                            map.addMarker(new MarkerOptions().position(new LatLng(LocalizationActivity.current_latitude, LocalizationActivity.current_longitude)).snippet("Current Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.driver)));
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(LocalizationActivity.current_latitude, LocalizationActivity.current_longitude), 16));
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        // Do something
                        showShortMessage(t.getMessage());
                    }
                });

    }


    private void setCustomwindow() {
        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public View getInfoContents(Marker marker) {


                View view = null;

                Timber.e("" + marker.getTag());

                if (marker.getTag() != null && marker.getTag().equals("empty")) {

                } else {
                    TaskList detailDto = (TaskList) marker.getTag();

                    view = View.inflate(RouteActivity.this, R.layout.custom_window, null);
                    TextView tvTaskCompletionTime, tvTaskType, textView, tvCustName, tvCustLocation;
                    ImageView inform = view.findViewById(R.id.ic_inform);
                    inform.setImageDrawable(getDrawable(R.drawable.ic_arrow));
                    tvTaskCompletionTime = view.findViewById(R.id.tv_task_time);
                    tvTaskType = view.findViewById(R.id.tv_task_name);
                    textView = view.findViewById(R.id.tv_task_status);
                    tvCustName = view.findViewById(R.id.tv_cust_name);
                    tvCustLocation = view.findViewById(R.id.tv_cust_address);
                    if (detailDto != null) {
                        tvCustName.setText(detailDto.getName());
                        Address address = detailDto.getAddress();
//                        Address address = detailDto.getAddress();
                        String custAddress = address.getFormattedAddress();
                        tvCustLocation.setText(custAddress);
                        int taskStatus = detailDto.getTaskStatus();
                        setStatus(taskStatus, textView);
                        if (detailDto.getColor() != null) {
                            textView.setTextColor(Color.parseColor(detailDto.getColor()));
                        }
                    }
                    boolean isPickUp;

                    String actualDate = detailDto.getDate();

                    Timber.e(detailDto.getDate());
                    String formattedTime = (actualDate.length() == 22) ? actualDate.substring(11, 16) : "0" + actualDate.substring(11, 16);
                    String timeAmPm = (actualDate.length() == 22) ? actualDate.substring(19, 22) : actualDate.substring(19, 21);
                    String dateToDisplay = formattedTime + timeAmPm;
                    tvTaskCompletionTime.setText(dateToDisplay);
                    int bussType = detailDto.getBusinessType();



                    switch (bussType) {

                        case 1:

                            isPickUp = detailDto.getIsPickup();


                            if (loginPrefManager.isCustomerCategoryEnabled()){
                                if(isPickUp){
                                    tvTaskType.setText(loginPrefManager.getPickupText());
                                }else{
                                    tvTaskType.setText(loginPrefManager.getDeliveryText());
                                }
                            }else {
                                if (isPickUp) {
                                    tvTaskType.setText(getString(R.string.task_type_pick_up));
                                } else {
                                    tvTaskType.setText(getString(R.string.task_type_delivery));
                                }
                            }
                            break;
                        case 2:

                            if (loginPrefManager.isCustomerCategoryEnabled()){
                                isPickUp = Boolean.parseBoolean(null);
                                tvTaskType.setText(loginPrefManager.getAppointmentText());
                            }else {
                                isPickUp = Boolean.parseBoolean(null);
                                tvTaskType.setText(getString(R.string.task_type_appointments));
                            }

                            break;

                        case 3:

                            if (loginPrefManager.isCustomerCategoryEnabled()){
                                isPickUp = Boolean.parseBoolean(null);
                                tvTaskType.setText(loginPrefManager.getFieldWorkForceText());
                            }else {


                                isPickUp = Boolean.parseBoolean(null);
                                tvTaskType.setText(getString(R.string.task_type_field_work));
                            }

                            break;
                    }
                }


                return view;
            }

        });

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                loginPrefManager.setStringValue("activity", "route");
                TaskList detailDto = (TaskList) marker.getTag();
                startActivityForResult(new Intent(RouteActivity.this, TaskDetailsActivity.class)
                        .putExtra("task_id", detailDto.getId())
                        .putExtra("activity", "route")
                        .putExtra("glympse_id", ""+detailDto.getGlympseId()), ROUTE_RESULT_CODE);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== ROUTE_RESULT_CODE && resultCode== RESULT_OK) {
            if (map!=null){
                map.clear();

                getTaskroutes();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Timber.e("onResume");
        //suganya hided
//
//        if (map != null) {
//            map.clear();
//        }
    }

    private void setStatus(int postion, TextView textView) {
        switch (postion) {
            case 2:

                textView.setText(getString(R.string.task_status_assigned));
                break;
            case 3:

                textView.setText(getString(R.string.task_status_accepted));
                break;
            case 4:
                textView.setText(getString(R.string.task_status_started));
                break;
            case 5:
                textView.setText(getString(R.string.task_status_in_progress));
                break;
            case 6:
                textView.setText(getString(R.string.task_status_success));
                break;
            case 7:
                textView.setText(getString(R.string.task_status_failed));
                break;
            case 9:
                textView.setText(getString(R.string.task_status_cancelled));
                break;
            case 10:
                textView.setText(getString(R.string.task_status_acknowledge));
                break;

        }

    }


}
