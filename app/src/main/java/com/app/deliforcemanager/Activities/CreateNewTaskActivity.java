package com.app.deliforcemanager.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.app.deliforcemanager.LocalizationActivity.LocalizationActivity;
import com.app.deliforcemanager.ModelClass.AllDriverData.FetchAllDriverResponse;
import com.app.deliforcemanager.ModelClass.CreateTask.CreateNewTask;
import com.app.deliforcemanager.ModelClass.CreateTask.DriverImageOption;
import com.app.deliforcemanager.ModelClass.FetchTeamResponse;

import com.app.deliforcemanager.ModelClass.TaskRoutesApi.Address;
import com.app.deliforcemanager.ModelClass.TaskRoutesApi.Geometry;
import com.app.deliforcemanager.ModelClass.TaskRoutesApi.Location;
import com.app.deliforcemanager.R;

import com.app.deliforcemanager.ServiceApi.GeocodingTask;
import com.app.deliforcemanager.ServiceApi.LatLng;
import com.glympse.android.hal.gms.common.ConnectionResult;
import com.glympse.android.hal.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.hbb20.CountryCodePicker;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class CreateNewTaskActivity extends LocalizationActivity implements GooglePlayServicesClient.ConnectionCallbacks,GooglePlayServicesClient.OnConnectionFailedListener,GeocodingTask.GeocodingListener {

    private static final int AUTOCOMPLETE_REQUEST_CODE = 693;
    private Spinner acs_task_type;
    private EditText edt_user_latitude;
    private EditText edt_user_longitude;
    private EditText edt_name_newtask;
    private EditText edt_mobile_newtask;
    private EditText edt_email_newtask;
    private EditText edt_orderid_newtask;
    private RadioGroup radioGroup_newtask;
    private AppCompatTextView txt_select_task_address;
    private EditText edt_search_task_date_from_newtask;
    private EditText edt_search_task_date_to_newtask;
    private EditText edt_new_task_pincode;
    private LinearLayout ll_newTask_Pincode;
    private EditText edt_description_newtask;
    private TextView txt_select_task_create;
    private TextView txt_select_task_cancel;
    private String selectedWorkType;
    private int businessType;
    private CountryCodePicker newtask_country_code;

    ImageView location_edit_iv,current_location_iv;

    Spinner spin_team, spin_driver;

    String driverId="",teamId="";

    String driverName = "";

    ArrayList<String> filter_list = null;


    ArrayAdapter<String> driverAdapter = null;

    ArrayAdapter<String> teamAdapter = null;


    ArrayList<FetchTeamResponse> listTeams = new ArrayList<>();

    ArrayList<FetchAllDriverResponse> listDrivers = new ArrayList<>();


    ArrayList<FetchAllDriverResponse> filterDrivers = new ArrayList<>();

    private boolean gps_enable = false;
    private boolean network_enable = false;
    public LocationManager locationManager;
    public LocationListener locationListener = new MyLocationListener();


    AppCompatRadioButton rb_pickip_new_task,rb_delivery_newtask;

    String lat, longi;
    //to generate Address
    Geocoder geocoder;
    List<android.location.Address> myAddress;
    private boolean isPickOrDelivery = true;
    Boolean isDriver = true;

    RadioButton radioButtonManual,radioButtonAuto;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_newtask);

        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        inittoolbar();
        initView();

        if (loginPrefManager.getStringValue("lang_postion").equalsIgnoreCase("2")) {
            //Locale spanishLocale = new Locale("es", "ES");
            newtask_country_code.changeDefaultLanguage(CountryCodePicker.Language.SPANISH);
        }


        getTeam();

//        try {
//            getDrivers();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


    }



    private void inittoolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView toolbar_text = findViewById(R.id.toolbar_title);

        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        toolbar_text.setText(getString(R.string.title_new_task));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        toolbar.setNavigationOnClickListener(view -> finish());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);

                txt_select_task_address.setText(place.getAddress());
                try {
                    edt_user_latitude.setText("" + place.getLatLng().latitude);
                    edt_user_longitude.setText("" + place.getLatLng().longitude);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
            } else if (resultCode == RESULT_CANCELED) {
                System.out.println("cancelled");
            }
        }
    }

    private void initView() {

        try{
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            checkLocationPermission();
        }catch (Exception e){
           // Toast.makeText(CreateNewTaskActivity.this, "CreateTaskException : "+e.toString(), Toast.LENGTH_SHORT).show();
        }
        Places.initialize(CreateNewTaskActivity.this, getString(R.string.google_places_api_key));

        newtask_country_code = findViewById(R.id.newtask_country_code);

        ll_newTask_Pincode = findViewById(R.id.ll_newTask_Pincode);
        if (loginPrefManager.getTaskPinCode())
            ll_newTask_Pincode.setVisibility(View.VISIBLE);
        else
            ll_newTask_Pincode.setVisibility(View.GONE);

        edt_new_task_pincode = findViewById(R.id.edt_user_add_pincode);

        edt_user_latitude = findViewById(R.id.edt_user_latitude);
        edt_user_longitude = findViewById(R.id.edt_user_longitude);

        edt_name_newtask = findViewById(R.id.edt_newtask_user_name);
        edt_mobile_newtask = findViewById(R.id.edt_user_mobilenumber);
        edt_email_newtask = findViewById(R.id.edt_user_emailid);
        edt_orderid_newtask = findViewById(R.id.edt_user_orderid);

        radioGroup_newtask = findViewById(R.id.radioGroup_newtask);
        edt_description_newtask = findViewById(R.id.edt_description_newtask);

        txt_select_task_create = findViewById(R.id.txt_select_task_create);
        txt_select_task_cancel = findViewById(R.id.txt_select_task_cancel);

        location_edit_iv = findViewById(R.id.location_edit_iv);
        current_location_iv = findViewById(R.id.current_location_iv);

        rb_pickip_new_task = findViewById(R.id.rb_pickip_new_task);

        rb_delivery_newtask = findViewById(R.id.rb_delivery_newtask);

        txt_select_task_cancel.setOnClickListener(v -> finish());

        txt_select_task_address = findViewById(R.id.txt_newtask_address);

        spin_team = findViewById(R.id.spin_team);

        spin_driver = findViewById(R.id.spin_driver);

        radioButtonManual = findViewById(R.id.radioButtonManual);

        radioButtonAuto = findViewById(R.id.radioButtonAuto);


        radioButtonManual.setChecked(true);

        txt_select_task_address.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(CreateNewTaskActivity.this);
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        });

        rb_delivery_newtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPickOrDelivery =  false;
            }
        });

        rb_pickip_new_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPickOrDelivery = true;

            }
        });


        edt_search_task_date_from_newtask = findViewById(R.id.edt_search_task_date_from_newtask);



        SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat, Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());



        Date oldDate = new Date();


        Date newDate = new Date((oldDate.getTime() +  TimeUnit.MINUTES.toMillis((30))));


        String endDateTime = sdf.format(newDate);


        edt_search_task_date_from_newtask.setText(currentDateandTime);

        edt_search_task_date_from_newtask.setOnClickListener(v -> showDateTimePicker(1, edt_search_task_date_from_newtask));

        edt_search_task_date_to_newtask = findViewById(R.id.edt_search_task_date_to_newtask);


        edt_search_task_date_to_newtask.setText(endDateTime);


        edt_search_task_date_to_newtask.setOnClickListener(v -> {
            if (edt_search_task_date_from_newtask.getText().toString().contains("-"))
                showDateTimePicker(2, edt_search_task_date_to_newtask);
            else
                showAlertMsg("Message", "Kindly select the From date time");
        });

        location_edit_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAddressDialogue();
            }
        });

        current_location_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_loader();
                getMyLocation();
            }
        });


        spin_driver.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position1, long l) {
                if (position1==0){
                    driverId = "";
                    Timber.tag("driver").e("0received");
                    if (!teamId.isEmpty()&&isDriver){
                        teamId = "";

                        teamAdapter.clear();
                        ArrayList<String> list_team = new ArrayList<>();
                        list_team.add("Select Team");

                        for (int i=0; i<listTeams.size();i++){
                            list_team.add(listTeams.get(i).getTeamName());
                        }
                        teamAdapter.addAll(list_team);
                        teamAdapter.notifyDataSetChanged();
                         spin_team.setSelection((teamAdapter).getPosition("Select Team"));
                        isDriver = false;
                    }else{
                        isDriver = true;
                    }

                }else{

                    isDriver= true;
                    Timber.tag("driver").e("1received");

                    driverId = filterDrivers.get(position1-1).getId();

                    driverName = filterDrivers.get(position1-1).getName();

                    Timber.tag("selectedDriverName").e(filterDrivers.get(position1 - 1).getName());

                    if (teamId.isEmpty()){
                        String teamName =null;
                        ArrayList<String> list = new ArrayList<String>();
                        list.add("Select Driver");
                        for(int j=0;j<filterDrivers.size();j++){
                            if (driverId.equals(filterDrivers.get(j).getId())){
                                teamId=filterDrivers.get(j).getAssignTeam().getId();
                                //String driverName = filterDrivers.get(j).getName();
                                //list.add(driverName);
                                teamName = filterDrivers.get(j).getAssignTeam().getTeamName();
                            }
                        }

                        for (int k=0;k<filterDrivers.size();k++){
                            if (teamId.equals(filterDrivers.get(k).getAssignTeam().getId())){
                                String driverName = filterDrivers.get(k).getName();
                                list.add(driverName);
                            }
                        }
                        Timber.tag("filtered_drivers").e(list.toString());
                        spin_team.setSelection((teamAdapter).getPosition(teamName));
                        isDriver = false;
                        driverAdapter.clear();
                        driverAdapter.addAll(list);
                        driverAdapter.notifyDataSetChanged();
                        spin_driver.setSelection((driverAdapter).getPosition(driverName));
                        isDriver = false;

                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spin_team.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position==0){
                    if (isDriver) {
                        teamId = "";
                        Timber.tag("team").e("0received");
                        filterDrivers = listDrivers;
                        //if (!driverId.isEmpty()) {
                            driverId = "";
                            driverSpinnerClearUpdate();
                        //}
                    }else{
                        isDriver = true;
                    }
                }else {
                    Timber.tag("team").e(position+"received");
                    teamId = listTeams.get(position-1).getId();
                    Timber.tag("selectedTeam").e(listTeams.get(position - 1).getTeamName());
                   // if (driverId.isEmpty()) {
                    if (isDriver){
                        driverSpinnerClearUpdate();
                    }else{
                        isDriver =true;
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        txt_select_task_create.setOnClickListener(v -> {
            String countryCode = newtask_country_code.getSelectedCountryCodeWithPlus();
            String customerName = edt_name_newtask.getText().toString();
            String customerMobile = edt_mobile_newtask.getText().toString();
            String customerEmail = edt_email_newtask.getText().toString();
            String customerOrderId = edt_orderid_newtask.getText().toString();
            String customerAddress = txt_select_task_address.getText().toString();
            String customerLat = edt_user_latitude.getText().toString();
            String customerLng = edt_user_longitude.getText().toString();
            String description = edt_description_newtask.getText().toString();
            String fromDate = edt_search_task_date_from_newtask.getText().toString();
            String toDate = edt_search_task_date_to_newtask.getText().toString();
            String pinCode = edt_new_task_pincode.getText().toString();

            loginPrefManager.setPinCode(pinCode);

            if (!customerMobile.equals("") && customerMobile.length() > 0) {
                if (!customerAddress.equals("") && customerAddress.length() > 0 && !customerAddress.equals("Address")) {
                    if (!fromDate.equals("") && fromDate.contains("-")) {
                        customerMobile = countryCode + " " + customerMobile;
                        if (businessType == 1) {
                            createNewTask(customerName, customerMobile, customerEmail, customerOrderId,
                                    customerAddress, customerLat, customerLng, description, fromDate, "", isPickOrDelivery);
                        } else if (!toDate.equals("") && toDate.contains("-")) {
                            createNewTask(customerName, customerMobile, customerEmail, customerOrderId,
                                    customerAddress, customerLat, customerLng, description, fromDate, toDate, false);
                        } else {
                            showAlertMsg("Message", "To date is mandatory");
                        }
                    } else {
                        showAlertMsg("Message", "From date is mandatory");
                    }
                } else {
                    showAlertMsg("Message", "Address is mandatory");
                }

            }else {
                showAlertMsg("Message", "Mobile number is mandatory");
            }
        });

        RadioGroup radioGroup_newtask = findViewById(R.id.radioGroup_newtask);
        acs_task_type = findViewById(R.id.acs_task_type);
        acs_task_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                selectedWorkType = parent.getItemAtPosition(position).toString();

                businessType = loginPrefManager.getCategoryList().get(position).getId();

                if (businessType==1) {
                    radioGroup_newtask.setVisibility(View.VISIBLE);
                    edt_search_task_date_to_newtask.setVisibility(View.INVISIBLE);
                } else {
                    radioGroup_newtask.setVisibility(View.GONE);
                    edt_search_task_date_to_newtask.setVisibility(View.VISIBLE);
                }



             //   businessType = loginPrefManager.getCategoryList().get(position).getId();



               /* if (position == 0)
                    businessType = 3;
                else if (position == 1)
                    businessType = 2;
                else
                    businessType = 1;*/

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> workType = new ArrayList<>();

        for (int i = 0; i<loginPrefManager.getCategoryList().size(); i++){
            workType.add(loginPrefManager.getCategoryList().get(i).getText());
        }
       /* workType.add("Field Workforce");
        workType.add("Appointment");
        workType.add("Pickup & Delivery");*/

        ArrayAdapter<String> workTypeAdapter = new ArrayAdapter<>(this, R.layout.simple_spinner_item, workType);
        workTypeAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        acs_task_type.setAdapter(workTypeAdapter);
    }



    private void createNewTask(String customerName, String customerMobile, String customerEmail, String customerOrderId,
                               String customerAddress, String customerLat, String customerLng, String description, String fromDate,
                               String toDate, boolean isPickDelivery) {
        try {
            if (!isPickDelivery) {
                SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat, Locale.ENGLISH);
                Date fromTaskDate = sdf.parse(fromDate);
                if (businessType!=1) {
                    Date toTaskDate = sdf.parse(toDate);
                    Calendar calFrom = Calendar.getInstance();
                    Calendar calTo = Calendar.getInstance();
                    calFrom.setTime(fromTaskDate);
                    calTo.setTime(toTaskDate);
                    if (calFrom.after(calTo)) {
                        showAlertMsg("Warning", "Invalid To Date Time");
                        edt_search_task_date_to_newtask.setText("To");
                    } else if (calTo.before(calFrom)) {
                        showAlertMsg("Warning", "Invalid To Date Time");
                        edt_search_task_date_to_newtask.setText("To");
                    } else if (calFrom.equals(calTo)) {
                        showAlertMsg("Warning", "Both Date and Time cannot be the same");
                    } else {
                        callCreateTask(customerName, customerMobile, customerEmail, customerOrderId, customerAddress, customerLat,
                                customerLng, description, fromDate, toDate, isPickDelivery);
                    }
                }else{
                    callCreateTask(customerName, customerMobile, customerEmail, customerOrderId, customerAddress, customerLat,
                            customerLng, description, fromDate, toDate, isPickDelivery);
                }
            } else {
              //  toDate = "";
                callCreateTask(customerName, customerMobile, customerEmail, customerOrderId, customerAddress, customerLat,
                        customerLng, description, fromDate, toDate, isPickDelivery);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);

    private void callCreateTask(String customerName, String customerMobile, String customerEmail, String customerOrderId,
                                String customerAddress, String customerLat, String customerLng, String description, String fromDate,
                                String toDate, boolean isPickDelivery) {
        CreateNewTask createNewTask = new CreateNewTask();
        createNewTask.setName(customerName);
        createNewTask.setPhone(customerMobile);
        createNewTask.setEmail(customerEmail);
        createNewTask.setOrderId(customerOrderId);

        Address address = new Address();
        address.setFormattedAddress(customerAddress);

        if (driverId.isEmpty()){
          //  createNewTask.setDriver("");
            Log.e("driver","empty");
        }else{
            createNewTask.setDriver(driverId);
            createNewTask.setTeam(teamId);
        }

        Location location = new Location();
        if (customerLat.equals("")) {
            customerLat = "0";
            customerLng = "0";
        }
        location.setLat(Double.valueOf(customerLat));
        location.setLng(Double.valueOf(customerLng));

        Geometry geometry = new Geometry();
        geometry.setLocation(location);
        address.setGeometry(geometry);

        createNewTask.setAddress(address);

        DriverImageOption driverImageOption = new DriverImageOption();
        driverImageOption.setExist(false);
        driverImageOption.setMandatory(false);

        createNewTask.setDriverImageOption(driverImageOption);
        createNewTask.setImages(null);

        createNewTask.setDescription(description);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat, Locale.ENGLISH);
            Date fromDatee = sdf.parse(fromDate);
            fromDate = simpleDateFormat.format(fromDatee.getTime());

            if (!isPickDelivery) {
                Date toDatee = sdf.parse(toDate);
                toDate = simpleDateFormat.format(toDatee.getTime());
            } else
                toDate = "";
        } catch (Exception e) {
            e.printStackTrace();
        }

        createNewTask.setDate(fromDate);
        createNewTask.setEndDate(toDate);

        createNewTask.setManager(loginPrefManager.getManagerID());
        createNewTask.setPinCode(loginPrefManager.getPinCode());
        createNewTask.setColor("#f57f17");

        Calendar calendar = Calendar.getInstance();
        TimeZone timeZone = calendar.getTimeZone();
        createNewTask.setTimezone(timeZone.getID());

        createNewTask.setBusinessType(businessType);
        if (radioButtonManual.isChecked()){
            createNewTask.setManual(true);
        }else{
            createNewTask.setManual(false);
        }

        createNewTask.setPickup(isPickDelivery);
        createNewTask.setTaskListing(true);

        show_loader();

        apiService.createNewTask(loginPrefManager.getCogintoToken(), createNewTask).enqueue(new Callback<CreateNewTask>() {
            @Override
            public void onResponse(@NotNull Call<CreateNewTask> call, @NotNull Response<CreateNewTask> response) {
                dismiss_loader();
                try {
                    if (response.code() == 200) {
                        show_error_response(response.body().getMessage());
                        Toast.makeText(CreateNewTaskActivity.this, getString(R.string.task_created), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        findCurrent();
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

    private void driverSpinnerClearUpdate(){
        if (driverAdapter!=null) {
            driverAdapter.clear();
            ArrayList<String> list_drivers = new ArrayList<>();
            filterDrivers = new ArrayList<>();
            list_drivers.add("Select Driver");

            for (int i = 0; i < listDrivers.size(); i++) {
                if (teamId.equals(listDrivers.get(i).getAssignTeam().getId())) {
                    filterDrivers.add(listDrivers.get(i));
                    list_drivers.add(listDrivers.get(i).getName());
                } else if (teamId.isEmpty()) {
                    list_drivers.add(listDrivers.get(i).getName());
                    filterDrivers.add(listDrivers.get(i));
                }
            }
            Log.e("filter_list1",list_drivers.toString());
            driverAdapter.addAll(list_drivers);
            driverAdapter.notifyDataSetChanged();
            spin_driver.setSelection((driverAdapter).getPosition("Select Driver"));

        }
    }


    private void getTeam() {

        apiService.getTeam(loginPrefManager.getCogintoToken()).enqueue(new Callback<ArrayList<FetchTeamResponse>>() {
            @Override
            public void onResponse(@NotNull Call<ArrayList<FetchTeamResponse>> call, @NotNull Response<ArrayList<FetchTeamResponse>> response) {
                dismiss_loader();
                try {
                    if (response.code() == 200) {
                        getDrivers();
                        listTeams = response.body();
                        setTeamSpinner(listTeams);
                    } else  {
                        findCurrent();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ArrayList<FetchTeamResponse>> call, @NotNull Throwable t) {
                dismiss_loader();
                showShortMessage(t.getMessage());
            }
        });


    }

    private void setTeamSpinner(ArrayList<FetchTeamResponse> listTeams) {

        ArrayList<String> list_team = new ArrayList<>();
        list_team.add("Select Team");

        for (int i=0; i<listTeams.size();i++){
            list_team.add(listTeams.get(i).getTeamName());
        }

        teamAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, list_team);
        teamAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spin_team.setAdapter(teamAdapter);

    }


    private void getDrivers() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("enableBlocked", false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("data", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        apiService.getAllDrivers(loginPrefManager.getCogintoToken(),jsonObject1).enqueue(new Callback<ArrayList<FetchAllDriverResponse>>() {
            @Override
            public void onResponse(@NotNull Call<ArrayList<FetchAllDriverResponse>> call, @NotNull Response<ArrayList<FetchAllDriverResponse>> response) {
                dismiss_loader();
                try {
                    if (response.code() == 200) {
                        listDrivers = response.body();
                        setDrivers(listDrivers);
                    } else  {
                        findCurrent();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(@NotNull Call<ArrayList<FetchAllDriverResponse>> call, @NotNull Throwable t) {
                dismiss_loader();
                showShortMessage(t.getMessage());
            }
        });


    }

    private void setDrivers(ArrayList<FetchAllDriverResponse> listDrivers) {
         filterDrivers = new ArrayList<>();
         filter_list = new ArrayList<>();
         filter_list.add("Select Driver");

        for (int i=0; i<listDrivers.size();i++){
                if (teamId.isEmpty()) {
                    filter_list.add(listDrivers.get(i).getName());
                    filterDrivers.add(listDrivers.get(i));
                }else if(teamId.equals(listDrivers.get(i).getAssignTeam().getId())){
                    filter_list.add(listDrivers.get(i).getName());
                    filterDrivers.add(listDrivers.get(i));
                }
        }
        driverAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, filter_list);
        driverAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spin_driver.setAdapter(driverAdapter);

    }



    public void editAddressDialogue() {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(CreateNewTaskActivity.this).inflate(R.layout.edit_address_dialogue, (ViewGroup) findViewById(16908290), false);
        builder2.setCancelable(false);
        builder2.setView(inflate);
        final android.app.AlertDialog create = builder2.create();
        final EditText editText = (EditText) inflate.findViewById(R.id.address_til);
        editText.setText(txt_select_task_address.getText().toString());
       // location_edit_iv.setVisibility(View.GONE);
        ((TextView) inflate.findViewById(R.id.address_update_cancel)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                create.dismiss();
            }
        });
        ((TextView) inflate.findViewById(R.id.address_update)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (editText.getText().toString().isEmpty()) {
                    Toast.makeText(CreateNewTaskActivity.this, "Please enter your address.", Toast.LENGTH_LONG).show();
                    return;
                }
                txt_select_task_address.setText(editText.getText().toString());


                new GeocodingTask(CreateNewTaskActivity.this, CreateNewTaskActivity.this).execute(editText.getText().toString());






                create.dismiss();
            }
        });
        create.show();
    }

    @SuppressLint("MissingPermission")
    public void getMyLocation() {

        try {
            gps_enable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {

        }

        try {
            network_enable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!gps_enable && !network_enable) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateNewTaskActivity.this);
            builder.setTitle("Attention");
            builder.setMessage("Sorry location is not avaiable, please enable service location...");

            builder.create().show();
        }

        if (gps_enable) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }

        if (network_enable){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    @Override
    public void onGeocodingSuccess(LatLng latLng) {


        double latitude = latLng.getLatitude();
        double longitude = latLng.getLongitude();


        edt_user_latitude.setText(String.valueOf(latitude));
        edt_user_longitude.setText(String.valueOf(longitude));


        Log.d("MainActivity", "Latitude: " + latitude + ", Longitude: " + longitude);

    }

    @Override
    public void onGeocodingFailure() {

        Toast.makeText(this, "Failed to geocode address", Toast.LENGTH_SHORT).show();

    }

    class MyLocationListener implements LocationListener{

        @Override
        public void onLocationChanged(@NonNull android.location.Location location) {

            if (location != null) {
                locationManager.removeUpdates(locationListener);

                lat = "" + location.getLatitude();
                longi = "" + location.getLongitude();

                edt_user_latitude.setText(lat);
                edt_user_longitude.setText(longi);

                geocoder = new Geocoder(CreateNewTaskActivity.this, Locale.getDefault());

                try {
                    myAddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    dismiss_loader();
                }catch (IOException e){
                    e.printStackTrace();
                }

                String myAddresss = myAddress.get(0).getAddressLine(0);
                txt_select_task_address.setText(myAddresss);
                location_edit_iv.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            LocationListener.super.onStatusChanged(provider, status, extras);
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            LocationListener.super.onProviderEnabled(provider);
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            LocationListener.super.onProviderDisabled(provider);
        }
    }
    private boolean checkLocationPermission(){

        int location = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int location2 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        List<String> listPermission = new ArrayList<>();

        if (location != PackageManager.PERMISSION_GRANTED){
            listPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (location2 != PackageManager.PERMISSION_GRANTED){
            listPermission.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!listPermission.isEmpty()){
            ActivityCompat.requestPermissions(this, listPermission.toArray(new String[listPermission.size()]), 1);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    final Calendar myDateTimeFutureCalendar_From = Calendar.getInstance();
    final Calendar myDateTimeFutureCalendar_To = Calendar.getInstance();
    final String dateTimeFormat = "dd-MMM-yyyy hh:mm a";
    private static final String UTC_PATTERN_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    Dialog dialog;
    androidx.appcompat.app.AlertDialog.Builder builder;

    private void showAlertMsg(String title, String msg) {
        builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, id) -> {
                });
        dialog = builder.create();
        dialog.show();
    }

    private void showDateTimePicker(final int picketType, EditText edt_DateTimePicker) {
        final Calendar calendar = Calendar.getInstance();
        final TimePickerDialog timePickerDialog = new TimePickerDialog(CreateNewTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar myCalendar;
                if (picketType == 1) {
                    myCalendar = updateFutureTimeCalender(myDateTimeFutureCalendar_From, hourOfDay, minute);
                    edt_search_task_date_to_newtask.setText("To");
                } else
                    myCalendar = updateFutureTimeCalender(myDateTimeFutureCalendar_To, hourOfDay, minute);

                Calendar cal = Calendar.getInstance();
                if (myCalendar.getTimeInMillis() >= cal.getTimeInMillis()) {

                    SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat, Locale.ENGLISH);
                    System.out.println(sdf.format(myCalendar.getTime()));

                    edt_DateTimePicker.setText(sdf.format(myCalendar.getTime()));

                    SimpleDateFormat sdfUTC = new SimpleDateFormat(UTC_PATTERN_DATE_TIME, Locale.ENGLISH);
                    sdfUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
                    String templateFieldValue = sdfUTC.format(myCalendar.getTime());
                    System.out.println(templateFieldValue);
                } else {
                    showAlertMsg("Message", "Invalid Time");
                }
            }

            private Calendar updateFutureTimeCalender(Calendar myTimeFutureCalendar, int hourOfDay, int minute) {
                myTimeFutureCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myTimeFutureCalendar.set(Calendar.MINUTE, minute);
                return myTimeFutureCalendar;
            }

        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);


        Calendar current_date = Calendar.getInstance();

        int years = current_date.get(Calendar.YEAR);
        int days = current_date.get(Calendar.DAY_OF_MONTH);
        int months = current_date.get(Calendar.MONTH);

        if (picketType == 1) {
            years = myDateTimeFutureCalendar_From.get(Calendar.YEAR);
            days = myDateTimeFutureCalendar_From.get(Calendar.DAY_OF_MONTH);
            months = myDateTimeFutureCalendar_From.get(Calendar.MONTH);
        } else if (picketType == 2) {
            /*if (!edt_search_task_date_to_newtask.getText().toString().contains("-")) {
                myDateTimeFutureCalendar_To.set(Calendar.YEAR, myDateTimeFutureCalendar_From.get(Calendar.YEAR));
                myDateTimeFutureCalendar_To.set(Calendar.DAY_OF_MONTH, myDateTimeFutureCalendar_From.get(Calendar.DAY_OF_MONTH));
                myDateTimeFutureCalendar_To.set(Calendar.MONTH, myDateTimeFutureCalendar_From.get(Calendar.MONTH));
            }*/
            years = myDateTimeFutureCalendar_To.get(Calendar.YEAR);
            days = myDateTimeFutureCalendar_To.get(Calendar.DAY_OF_MONTH);
            months = myDateTimeFutureCalendar_To.get(Calendar.MONTH);
        }

        final DatePickerDialog datePickerDialogW = new DatePickerDialog(CreateNewTaskActivity.this, (view, year, month, dayOfMonth) -> {
            if (picketType == 1) {
                myDateTimeFutureCalendar_From.set(Calendar.YEAR, year);
                myDateTimeFutureCalendar_From.set(Calendar.MONTH, month);
                myDateTimeFutureCalendar_From.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                edt_search_task_date_to_newtask.setText("To");
            } else {
                myDateTimeFutureCalendar_To.set(Calendar.YEAR, year);
                myDateTimeFutureCalendar_To.set(Calendar.MONTH, month);
                myDateTimeFutureCalendar_To.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            }
            timePickerDialog.show();
        }, years, months, days);

        if (picketType == 1) {
            datePickerDialogW.getDatePicker().setMinDate(System.currentTimeMillis());
            /*if (edt_search_task_date_to_newtask.getText().toString().contains("-"))
                datePickerDialogW.getDatePicker().setMaxDate(myDateTimeFutureCalendar_To.getTimeInMillis());*/
        } else if (picketType == 2) {
            datePickerDialogW.getDatePicker().setMinDate(myDateTimeFutureCalendar_From.getTimeInMillis());
        }

        /*datePickerDialogW.setCancelable(false);
        datePickerDialogW.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                System.out.println("canceled");
            }
        });*/
        datePickerDialogW.show();
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onNetworkLost() {

    }

    @Override
    public void onResume() {
        super.onResume();
        isDriver = true;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
