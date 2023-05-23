package com.app.deliforcemanager.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.deliforcemanager.Adapter.AddImagesAdpater;
import com.app.deliforcemanager.BuildConfig;
import com.app.deliforcemanager.Cognito.AppHelper;
import com.app.deliforcemanager.CustomDialogView.AddActualCustomerDialog;
import com.app.deliforcemanager.CustomDialogView.AlertDialog;
import com.app.deliforcemanager.CustomDialogView.AlertDialogOTP;
import com.app.deliforcemanager.CustomDialogView.CancelDialog;
import com.app.deliforcemanager.CustomDialogView.DropDownDialog;
import com.app.deliforcemanager.CustomDialogView.TaskDetailEditTextDialog;
import com.app.deliforcemanager.LocalizationActivity.LocalizationActivity;
import com.app.deliforcemanager.ModelClass.DeleteImageApi.DeleteImage;
import com.app.deliforcemanager.ModelClass.DriverStatusApi.DriverStatusUpdate;
import com.app.deliforcemanager.ModelClass.TaskDetailsApi.ActionBlock;
import com.app.deliforcemanager.ModelClass.TaskDetailsApi.ActualCustomer;
import com.app.deliforcemanager.ModelClass.TaskDetailsApi.ActualCustomerOutput;
import com.app.deliforcemanager.ModelClass.TaskDetailsApi.Address;
import com.app.deliforcemanager.ModelClass.TaskDetailsApi.Barcode_Data;
import com.app.deliforcemanager.ModelClass.TaskDetailsApi.CaptureLocation;
import com.app.deliforcemanager.ModelClass.TaskDetailsApi.CrmData;
import com.app.deliforcemanager.ModelClass.TaskDetailsApi.CrmRequest;
import com.app.deliforcemanager.ModelClass.TaskDetailsApi.DropDownOptions;
import com.app.deliforcemanager.ModelClass.TaskDetailsApi.Note;
import com.app.deliforcemanager.ModelClass.TaskDetailsApi.OTPTaskUpdate;
import com.app.deliforcemanager.ModelClass.TaskDetailsApi.ParticluarTaskDetails;
import com.app.deliforcemanager.ModelClass.TaskDetailsApi.SettingsModel;
import com.app.deliforcemanager.ModelClass.TaskDetailsApi.StartLocation;
import com.app.deliforcemanager.ModelClass.TaskDetailsApi.TaskOneDetails;
import com.app.deliforcemanager.ModelClass.TaskDetailsApi.TemplatesDynamic;
import com.app.deliforcemanager.ModelClass.TaskDetailsApi.ZohoItemInfo;
import com.app.deliforcemanager.ModelClass.TrackingRecord.LiveTrackingModelData;
import com.app.deliforcemanager.ModelClass.UpdateImagesApi.DriverImage;
import com.app.deliforcemanager.R;
import com.app.deliforcemanager.RawHeaders.DriverStatusInfo.DriverStatus;
import com.app.deliforcemanager.RawHeaders.TaskInfo.AddActualCustomer;
import com.app.deliforcemanager.RawHeaders.TaskInfo.DriverImages;
import com.app.deliforcemanager.RawHeaders.TaskInfo.SuperTask;
import com.app.deliforcemanager.RawHeaders.TaskInfo.UpdateTaskStatus;
import com.app.deliforcemanager.ServiceApi.APIServiceFactory;
import com.app.deliforcemanager.ServiceApi.ApiService;
import com.app.deliforcemanager.Services.HUD;
import com.app.deliforcemanager.Utils.AppUtils;
import com.app.deliforcemanager.Utils.DeliforceConstants;
import com.app.deliforcemanager.glypmseWrapper.EnRouteWrapper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fxn.pix.Pix;
import com.glympse.android.core.CoreConstants;
import com.glympse.android.toolbox.listener.GListener;
import com.glympse.android.toolbox.listener.GSource;
import com.glympse.enroute.android.api.EE;
import com.glympse.enroute.android.api.EnRouteConstants;
import com.glympse.enroute.android.api.GTask;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.SphericalUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;

import id.zelory.compressor.Compressor;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import ng.max.slideview.SlideView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.OnClickListener;
import static android.view.View.VISIBLE;
import static com.app.deliforcemanager.Services.IdleService.MY_PREFS_NAME;
import static com.app.deliforcemanager.Services.IdleService.STANDARD_ACCURACY;
import static com.app.deliforcemanager.Utils.AppUtils.getCompleteAddressString;
import static com.app.deliforcemanager.Utils.DeliforceConstants.SIGNATURE_REQUEST_CODE;
import static com.app.deliforcemanager.Utils.DeliforceConstants.TASK_ACCEPTED;
import static com.app.deliforcemanager.Utils.DeliforceConstants.TASK_ARRIVED;
import static com.app.deliforcemanager.Utils.DeliforceConstants.TASK_ASSIGNED;
import static com.app.deliforcemanager.Utils.DeliforceConstants.TASK_DECLINED;
import static com.app.deliforcemanager.Utils.DeliforceConstants.TASK_FAILED;
import static com.app.deliforcemanager.Utils.DeliforceConstants.TASK_STARTED;
import static com.app.deliforcemanager.Utils.DeliforceConstants.TASK_STATUS_ACKNOWLEDGE;
import static com.app.deliforcemanager.Utils.DeliforceConstants.TASK_STATUS_CANCELLED;
import static com.app.deliforcemanager.Utils.DeliforceConstants.TASK_STATUS_CUSTOMER_NOT_AVAILABLE;
import static com.app.deliforcemanager.Utils.DeliforceConstants.TASK_SUCCESS;

public class TaskDetailsActivity extends LocalizationActivity implements GListener {

    public static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 250;
    public static final String BAR_CODE = "barcode";
    public static final String NOT_MANDATORY = "Not-Mandatory";
    public static final String MANDATORY = "Mandatory";
    public static final String READ_AND_WRITE = "Read and Write";
    private static final String TAG = "TaskDetailsActivity";
    private static final String DATE_FORMAT = "dd-MMM-yyyy";
    private static final String UTC_PATTERN_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String GOOGLE_MAP_PACKAGE = "com.google.android.apps.maps";
    private static final String EDITING_NOT_ALLOWED = "Editing not allowed";
    private static final int DATE_TIME_PICKER = 0;
    private static final int DATE_TIME_PICKER_FUTURE = 1;
    private static final int DATE_TIME_PICKER_PAST = 2;
    private static final int DATE_PICKER = 100;
    private static final int DATE_PICKER_FUTURE = 101;
    private static final int DATE_PICKER_PAST = 102;
    private static final String CHECK_BOX = "checkbox";
    private static final String EDIT_TEXT = "text";
    private static final String CHECK_LIST = "checklist";
    private static final String DATE = "date";
    private static final String DATE_FUTURE = "dateFuture";
    private static final String DATE_PAST = "datePast";
    private static final String DATE_TIME = "dateTime";
    private static final String DATE_TIME_FUTURE = "dateTimeFuture";
    private static final String DATE_TIME_PAST = "dateTimePast";
    private static final String DROP_DOWN = "dropdown";
    private static final String EMAIL_FIELD = "email";
    private static final String NUMBER_FIELD = "number";
    private static final String URL_FIELD = "url";
    private static final String IMAGE = "image";
    //pick type and api parm for upload
    private static final String TASK_IMAGE = "image";
    private static final String CANCEL_IMAGE = "cancel_image";
    private static final String TEMPLATE_IMAGE = "template_image";
    private static int IMAGE_COUNT = 5;
    private String track_url;
    final Calendar myCalendar = Calendar.getInstance();
    final Calendar myPastCalendar = Calendar.getInstance();
    final Calendar myFutureCalendar = Calendar.getInstance();
    final Calendar myDateTimeCalendar = Calendar.getInstance();
    final Calendar myDateTimePastCalendar = Calendar.getInstance();
    final Calendar myDateTimeFutureCalendar = Calendar.getInstance();
    final String dateTimeFormat = "dd-MMM-yyyy hh:mm a";
    Snackbar snackbar;
    private MenuItem menuTaskLocationUpdate;
    TextView toolbar_text, taskDetailTitle, tv_add_notes, tv_bar_code, image_count, notes_count, tvTaskCompletionTime, custName, taskName, custLocation, tvTaskDetailsStatus, addSign, addImages, tvTaskDescription;
    LinearLayout layoutAddOtherDetails, task_image_view, arrived_view,
            track_layout, layout_add_barcode, layoutAddNewNote, layoutAddImages,
            layoutAddSign, acknowledge_view, accept_view, start_view, success_view, layout_actual_customer;
    ImageView imgShowCustLocation, imgMakeCustCall, bar_code_added, signature_enable, actual_customer_added;
    LinearLayout taskInformation;
    RecyclerView taskInfoRecycler;
    View view;
    TaskOneDetails taskOneDetails;
    CancelDialog cancelDialog;
    //GPSTracker gpsTracker;
    RecyclerView images_view;
    boolean images_need = false, note_need = false, signature_need = false, Bar_code_need = false;
    AddImagesAdpater addImagesAdpater;
    SlideView acknowledge_slide, arrived_slide, arrived_cancel_slide, accept_slide, decline_slide, start_slide, cancel_slide, success_slide, failed_slide, customer_unavailable_slide;
    int imageCount = 0;
    double driver_latitude, driver_longitude;
    String phone_no;
    List<Note> notes_list;
    List<Barcode_Data> barcode_data;
    AlertDialog alertDialog;
    AlertDialogOTP alertDialogOTP;
    DriverStatus driverStatus;
    NotificationManager notificationManager;
    RelativeLayout desc_view;
    Map<String, LatLng> multiMap = new HashMap<>();
    LatLng start_latLng;
    String activity_name;
    ApiService apiService1;
    JsonArray selectedArray = new JsonArray();
    JsonObject selectedObj = new JsonObject();
    int order = 0;
    String liveLogData = null;
    androidx.appcompat.app.AlertDialog.Builder alertdialogbuilder;
    AddActualCustomerDialog addActualCustomerDialog;
    String dateFormat = "dd-MMM-yyyy";
    List<String> ItemsIntoList;
    int year = 2019;
    int month = 1;
    int day = 1;
    Dialog dialog;
    androidx.appcompat.app.AlertDialog.Builder builder;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
    OnLocationUpdatedListener onLocationChangeListener = new OnLocationUpdatedListener() {
        @Override
        public void onLocationUpdated(Location location) {
            if (location.getAccuracy() < STANDARD_ACCURACY) {
                driver_longitude = location.getLongitude();
                driver_latitude = location.getLatitude();
                current_accuraccy = location.getAccuracy();
                AppUtils.setDeviceLocation(location, TaskDetailsActivity.class.getSimpleName());
            }
        }
    };
    String crmRefId = "";
    String crmFieldId = "";
    LinearLayout lrCustomerUnAvailable;
    boolean isCaptureLocation = false;
    boolean isActualCustomerAdded = false;
    private String task_id, signature_added;
    private List<String> file_path;
    private Toolbar toolbar;
    private List<String> selected_images;
    private List<String> cancelled_images;
    private List<String> cancelled_images_path;
    private UpdateTaskStatus updateTaskStatus;
    private TextView tv_duty_status;
    private Switch status_switch;
    private LinearLayout ll_main_view;
    private LinearLayout ll_order_id, llRefId;
    private View view_order_id;
    private TextView txt_order_id_no;
    private LayoutInflater layoutInflator;
    private Button btn_track;
    private int mandatoryCount = 0;
    private String templateFieldName = "";
    private String templateFieldValue = "";
    private String templateFieldId = "";
    private String templateFieldQty = "0";
    private String orderValue = "";
    private boolean[] boxBooleans;
    private SharedPreferences locationPrefs;
    private int TASK_TEMPLATE_STATUS = DeliforceConstants.TASK_TEMPLATE_STATUS;
    private LinearLayout llTemplateView;
    private TextView tvTemplateName, txt_ref_id_no;
    private ImageView imvTemplateAdd;
    private View TemplateView, belowTemplateView;
    private View checkBoxLine;
    private String glympseTaskID = "";
    private GTask glympTask;
    private boolean APIhitsuccess = true;
    private boolean APIhitCompletesuccess = true;
    private boolean showLoader = true;
    private String cancelTaskMessage;
    private String mSelectedValues = "";
    private double updateLat, updateLng;
    private String cancelId = "";
    String imageType = "";
    private  View view_notes;
    private LinearLayout layout_notes;

    TextView tv_end_time,tv_start_time;

    private TextView tv_task_notes;

    public static int getNavBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        //glympse listener
        if (loginPrefManager.getGlympseEnabled()) {
            if (getIntent().hasExtra("glympse_id")) {
                glympseTaskID = getIntent().getStringExtra("glympse_id");
            }
            if (glympseTaskID != null) {
                if (BuildConfig.DEBUG) {
                    EnRouteWrapper.instance().manager().overrideLoggingLevels(CoreConstants.INFO, CoreConstants.INFO);
                }
            }
        }

        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        locationPrefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        view_order_id = findViewById(R.id.view_order_id);
        txt_order_id_no = findViewById(R.id.txt_order_id_no);
        ll_main_view = findViewById(R.id.ll_main_view);
        llTemplateView = findViewById(R.id.llTemplateView);
        tvTemplateName = findViewById(R.id.tvTemplateName);
        imvTemplateAdd = findViewById(R.id.imvTemplateAdd);
        TemplateView = findViewById(R.id.templateView);
        belowTemplateView = findViewById(R.id.belowTemplateView);
        ll_order_id = findViewById(R.id.ll_order_id);
        txt_ref_id_no = findViewById(R.id.txt_ref_id_no);
        llRefId = findViewById(R.id.llRefId);

        view_notes=findViewById(R.id.view_notes);
        layout_notes=findViewById(R.id.layout_notes);
        tv_task_notes=findViewById(R.id.tv_task_notes);

        tv_start_time = findViewById(R.id.tv_start_time);
        tv_end_time = findViewById(R.id.tv_end_time);
        btn_track= findViewById(R.id.btn_track);


        ll_main_view.setVisibility(GONE);
        TemplateView.setVisibility(GONE);
        belowTemplateView.setVisibility(GONE);
        llTemplateView.setVisibility(GONE);
        view_order_id.setVisibility(GONE);
        ll_order_id.setVisibility(GONE);
        llRefId.setVisibility(GONE);

        cancelled_images = new ArrayList<>();
        cancelled_images_path = new ArrayList<>();
        layoutInflator = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (getIntent() != null) {
            task_id = Objects.requireNonNull(getIntent().getExtras()).getString("task_id", "");

            loginPrefManager.setStringValue("taskId", "" + task_id);
            activity_name = loginPrefManager.getStringValue("activity");
        }

        initView();

        inittoolbar();

//        buildBraodCast();
        getStartLocations();
        SmartLocation.with(TaskDetailsActivity.this).location().start(onLocationChangeListener);

        //gpsTracker = new GPSTracker(TaskDetailsActivity.this);

        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private void createDynamicView(List<TemplatesDynamic> templates, int taskStatus) {

        for (int tempCount = 0; tempCount < templates.size(); tempCount++) {
            View tempView = layoutInflator.inflate(R.layout.view_dynamic, null);
            TextView textViewTitle = tempView.findViewById(R.id.textViewTitle);
            final TextView textViewSub = tempView.findViewById(R.id.textViewSub);
            final LinearLayout llView = tempView.findViewById(R.id.llView);
            final ImageView imvAdd = tempView.findViewById(R.id.imvAdd);
            final ImageView imgTemplate = tempView.findViewById(R.id.imgTemplate);
            final ImageView ivImgLock = tempView.findViewById(R.id.ivImgLock);
            final RelativeLayout rlImage = tempView.findViewById(R.id.rlImage);
            final View viewLine = tempView.findViewById(R.id.viewLine);
//            textViewSub.setTag(templates.get(tempCount).getFieldName());
            int addedCount = tempCount + 1;
            if (addedCount == templates.size()) {
                viewLine.setVisibility(GONE);
            } else {
                if (addedCount % taskOneDetails.getTemplates().size() == 0) {
                    viewLine.setVisibility(VISIBLE);
                } else {
                    viewLine.setVisibility(GONE);
                }
            }


            String dataType = templates.get(tempCount).getDataType();

            try {
                String fieldName = templates.get(tempCount).getFieldName();
                String fieldValue = templates.get(tempCount).getFieldValue();
                crmRefId = "";
                if (templates.get(tempCount).getCrmRefId() != null) {
                    crmRefId = templates.get(tempCount).getCrmRefId();
                }

                int order = templates.get(tempCount).getOrder();

                final String permitAgent = templates.get(tempCount).getPermitAgent();
                final String mandatoryFields = templates.get(tempCount).getMandatoryFields();
                final JsonArray selectedValues = templates.get(tempCount).getSelectedValues();

                List<DropDownOptions> optionsArray = null;
                try {
                    if (templates.get(tempCount).getOptions() != null && templates.get(tempCount).getOptions().size() > 0)
                        optionsArray = templates.get(tempCount).getOptions();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (mandatoryFields.equals(NOT_MANDATORY)) {
                    textViewTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
                } else {
                    mandatoryCount += 1;
                }
                textViewTitle.setText(fieldName);
                textViewSub.setText(fieldValue);
                /**
                 * check crm enabled & update selected quantity into template quantity
                 */
               /* if (dataType.equals(DROP_DOWN) && !fieldValue.isEmpty() && loginPrefManager.isCrmEnabled()
                        && templates.get(tempCount).getZohoItemInfo() != null) {
                    for (int i = 0; i < templates.get(tempCount).getZohoItemInfo().size(); i++) {
                        if (templates.get(tempCount).getZohoItemInfo().get(i).getName().equals(fieldValue)) {
                            templateFieldQty = templates.get(tempCount).getZohoItemInfo().get(i).getQuantity();
                        }
                    }
                }*/

                //task is stated then show the add sysmbol
                if (taskStatus == TASK_STARTED) {
                    imvAdd.setVisibility(GONE);
                } else {
                    imvAdd.setVisibility(GONE);
                }

                int finalTempCount = tempCount;
                imvAdd.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        orderValue = String.valueOf(templates.get(finalTempCount).getOrder() + 1);
                        templateFieldName = fieldName;
                        templateFieldValue = fieldValue;

                        upDateTask(DeliforceConstants.TASK_ADD_TEMPLATE_STATUS, "");

                    }
                });


//                if (addbuttonClickied){
//                    String  orderConverted =""+ templates.get(tempCount).getOrder();
//                    order = Integer.parseInt(orderConverted+1);
//                    addbuttonClickied=false;
//                    TASK_TEMPLATE_STATUS = 22;
//                }else{
//                    order= templates.get(tempCount).getOrder();
//                    TASK_TEMPLATE_STATUS = 21;
//                }

                int finalOrder = order;

                int image = R.drawable.ic_qr_code;
                switch (dataType) {
                    case BAR_CODE:
                        image = R.drawable.ic_qr_code;
                        prepareDynamicViewIcons(rlImage, textViewSub, taskStatus, image, true);
                        textViewSub.setOnClickListener(v -> {
                            if (permitAgent.equals(READ_AND_WRITE))
                                barCodeScreenCall(fieldName, true, finalOrder);
                            else
                                showAlertMsg("Message", EDITING_NOT_ALLOWED);
                        });
                        break;
                    case DROP_DOWN:
                        image = R.drawable.ic_text;
                        if (mandatoryFields.equals(MANDATORY))
                            textViewTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_star_black_24dp, 0, R.drawable.ic_go_front, 0);
                        else
                            textViewTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_go_front, 0);
                        prepareDynamicViewIcons(rlImage, textViewSub, taskStatus, image, false);
                        final List<DropDownOptions> finalOptionsArray = optionsArray;
                        final ArrayList<ZohoItemInfo> finalZohoArray = templates.get(tempCount).getZohoItemInfo();

                        for (int i = 0; i < finalOptionsArray.size(); i++) {
                            if (finalOptionsArray.get(i).getValue().equalsIgnoreCase("Select")) {
                                finalOptionsArray.remove(finalOptionsArray.get(i));
                            }
                        }
                        llView.setOnClickListener(v -> {

                            try {
                                if (permitAgent.equals(READ_AND_WRITE))

                                    showDropDown(fieldName, finalOptionsArray, finalZohoArray, textViewSub, finalOrder);
                                else
                                    showAlertMsg("Message", EDITING_NOT_ALLOWED);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                        break;
                    case CHECK_LIST:
                        if (boxBooleans == null || boxBooleans.length == 0)
                            boxBooleans = new boolean[fieldValue.split(",").length];
                        final String[] items = fieldValue.split(",");

                        try {
                            if (selectedValues != null && selectedValues.size() > 0) {
                                StringBuilder txtSelectedValues = new StringBuilder();
                                for (int count = 0; count < selectedValues.size(); count++) {
                                    txtSelectedValues.append(selectedValues.get(count)).append(",");
                                }
                                textViewSub.setText(txtSelectedValues.toString().substring(0, txtSelectedValues.length() - 1));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (mandatoryFields.equals(MANDATORY))
                            textViewTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_star_black_24dp, 0, R.drawable.ic_go_front, 0);
                        else
                            textViewTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_go_front, 0);
                        prepareDynamicViewIcons(rlImage, textViewSub, taskStatus, image, false);

                        textViewTitle.setOnClickListener(v -> {
                            if (permitAgent.equals(READ_AND_WRITE)) {
                                showCheckBox(fieldValue, fieldName, boxBooleans, items, textViewSub, finalOrder);
                            } else
                                showAlertMsg("Message", EDITING_NOT_ALLOWED);
                        });
                        break;
                    case CHECK_BOX:
                        textViewSub.setVisibility(GONE);
                        @SuppressLint("InflateParams") View tempViewCheckBox = layoutInflator.inflate(R.layout.view_dynamic_checkbox, null);
                        final CheckBox checkBox = tempViewCheckBox.findViewById(R.id.checkBox);
                        final ImageView imvAddInsideCheck = tempViewCheckBox.findViewById(R.id.imvAdd);
                        checkBoxLine = tempViewCheckBox.findViewById(R.id.CheckBoxviewLine);

                        if (taskStatus == TASK_STARTED) {
                            imvAddInsideCheck.setVisibility(GONE);
                        } else {
                            imvAddInsideCheck.setVisibility(GONE);
                        }

                        int addedCount1 = tempCount + 1;
                        if (addedCount1 == templates.size()) {
                            checkBoxLine.setVisibility(GONE);
                        } else {
                            if (addedCount % taskOneDetails.getTemplateLength() == 0) {
                                checkBoxLine.setVisibility(VISIBLE);
                            } else {
                                checkBoxLine.setVisibility(GONE);
                            }
                        }
                        imvAddInsideCheck.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                orderValue = String.valueOf(templates.get(finalTempCount).getOrder() + 1);

                                templateFieldName = fieldName;
                                templateFieldValue = fieldValue;


                                upDateTask(DeliforceConstants.TASK_ADD_TEMPLATE_STATUS, "");
                            }
                        });

                        if (textViewSub.getText().toString().equals("true"))
                            checkBox.setChecked(true);
                        else
                            checkBox.setChecked(false);
                        TextView textViewSubCB = tempViewCheckBox.findViewById(R.id.textViewTitleCB);
                        textViewSubCB.setText(fieldName);

                        if (mandatoryFields.equals(MANDATORY))
                            textViewSubCB.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_star_black_24dp, 0, 0, 0);

                        checkBox.setOnClickListener(v -> {
                            if (permitAgent.equals(READ_AND_WRITE))
                                updateCheckBox(fieldName, checkBox.isChecked() ? "true" : "false", finalOrder);
                            else
                                showAlertMsg("Message", EDITING_NOT_ALLOWED);
                        });
                        ll_main_view.addView(tempViewCheckBox);
                        break;
                    case DATE:
                        if (!fieldValue.equals("") && fieldValue.contains("T"))
                            textViewSub.setText(showDateInOurLocalTime(fieldValue));
                        else
                            textViewSub.setText(getString(R.string.choose_date));
                        image = R.drawable.ic_calendar;
                        prepareDynamicViewIcons(rlImage, textViewSub, taskStatus, image, true);
                        textViewSub.setOnClickListener(v -> {
                            if (permitAgent.equals(READ_AND_WRITE))
                                showDatePicker(fieldValue, fieldName, textViewSub, DATE_PICKER, finalOrder);
                            else
                                showAlertMsg("Message", EDITING_NOT_ALLOWED);
                        });
                        break;
                    case DATE_FUTURE:
                        if (!fieldValue.equals("") && fieldValue.contains("T"))
                            textViewSub.setText(showDateInOurLocalTime(fieldValue));
                        else
                            textViewSub.setText(getString(R.string.choose_date));
                        image = R.drawable.ic_calendar;
                        prepareDynamicViewIcons(rlImage, textViewSub, taskStatus, image, true);
                        textViewSub.setOnClickListener(v -> {
                            if (permitAgent.equals(READ_AND_WRITE))
                                showDatePicker(fieldValue, fieldName, textViewSub, DATE_PICKER_FUTURE, finalOrder);
                            else
                                showAlertMsg("Message", EDITING_NOT_ALLOWED);
                        });
                        break;
                    case DATE_PAST:
                        if (!fieldValue.equals("") && fieldValue.contains("T"))
                            textViewSub.setText(showDateInOurLocalTime(fieldValue));
                        else
                            textViewSub.setText(getString(R.string.choose_date));
                        image = R.drawable.ic_calendar;
                        prepareDynamicViewIcons(rlImage, textViewSub, taskStatus, image, true);
                        textViewSub.setOnClickListener(v -> {
                            if (permitAgent.equals(READ_AND_WRITE))
                                showDatePicker(fieldValue, fieldName, textViewSub, DATE_PICKER_PAST, finalOrder);
                            else
                                showAlertMsg("Message", EDITING_NOT_ALLOWED);
                        });
                        break;
                    case DATE_TIME:
                        if (!fieldValue.equals("") && fieldValue.contains("T"))
                            textViewSub.setText(showDateTimeInOurLocalTime(fieldValue));
                        else
                            textViewSub.setText(getString(R.string.choose_date_time));
                        image = R.drawable.ic_calendar;
                        prepareDynamicViewIcons(rlImage, textViewSub, taskStatus, image, true);
                        textViewSub.setOnClickListener(v -> {
                            if (permitAgent.equals(READ_AND_WRITE))
                                showDateTimePicker(fieldValue, fieldName, textViewSub, DATE_TIME_PICKER, finalOrder);
                            else
                                showAlertMsg("Message", EDITING_NOT_ALLOWED);
                        });
                        break;
                    case DATE_TIME_FUTURE:
                        if (!fieldValue.equals("") && fieldValue.contains("T"))
                            textViewSub.setText(showDateTimeInOurLocalTime(fieldValue));
                        else
                            textViewSub.setText(getString(R.string.choose_date_time));
                        image = R.drawable.ic_calendar;
                        prepareDynamicViewIcons(rlImage, textViewSub, taskStatus, image, true);
                        textViewSub.setOnClickListener(v -> {
                            if (permitAgent.equals(READ_AND_WRITE))
                                showDateTimePicker(fieldValue, fieldName, textViewSub, DATE_TIME_PICKER_FUTURE, finalOrder);
                            else
                                showAlertMsg("Message", EDITING_NOT_ALLOWED);
                        });
                        break;
                    case DATE_TIME_PAST:
                        if (!fieldValue.equals("") && fieldValue.contains("T"))
                            textViewSub.setText(showDateTimeInOurLocalTime(fieldValue));
                        else
                            textViewSub.setText(getString(R.string.choose_date_time));
                        image = R.drawable.ic_calendar;
                        prepareDynamicViewIcons(rlImage, textViewSub, taskStatus, image, true);
                        textViewSub.setOnClickListener(v -> {
                            if (permitAgent.equals(READ_AND_WRITE))
                                showDateTimePicker(fieldValue, fieldName, textViewSub, DATE_TIME_PICKER_PAST, finalOrder);
                            else
                                showAlertMsg("Message", EDITING_NOT_ALLOWED);
                        });
                        break;

                    case EDIT_TEXT:
                        image = R.drawable.ic_text;
                        prepareDynamicViewIcons(rlImage, textViewSub, taskStatus, image, true);
                        textViewSub.setOnClickListener(v -> {
                            if (permitAgent.equals(READ_AND_WRITE))
                                showEditText(textViewSub, fieldName, fieldValue, crmRefId,
                                        InputType.TYPE_CLASS_TEXT, EDIT_TEXT, finalOrder);
                            else
                                showAlertMsg("Message", EDITING_NOT_ALLOWED);
                        });
                        break;
                    case EMAIL_FIELD:
                        image = R.drawable.ic_envelope;
                        prepareDynamicViewIcons(rlImage, textViewSub, taskStatus, image, true);
                        textViewSub.setOnClickListener(v -> {
                            if (permitAgent.equals(READ_AND_WRITE))
                                showEditText(textViewSub, fieldName, fieldValue, crmRefId, InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS, EMAIL_FIELD, finalOrder);
                            else
                                showAlertMsg("Message", EDITING_NOT_ALLOWED);
                        });
                        break;
                    case NUMBER_FIELD:
                        image = R.drawable.ic_number;
                        prepareDynamicViewIcons(rlImage, textViewSub, taskStatus, image, true);

                        textViewSub.setOnClickListener(v -> {
                            if (permitAgent.equals(READ_AND_WRITE))
                                showEditText(textViewSub, fieldName, fieldValue, crmRefId, InputType.TYPE_CLASS_NUMBER, NUMBER_FIELD, finalOrder);
                            else
                                showAlertMsg("Message", EDITING_NOT_ALLOWED);
                        });
                        break;
                    case URL_FIELD:
                        image = R.drawable.ic_world_ide_web;
                        prepareDynamicViewIcons(rlImage, textViewSub, taskStatus, image, true);
                        textViewSub.setOnClickListener(v -> {
                            if (permitAgent.equals(READ_AND_WRITE))
                                showEditText(textViewSub, fieldName, fieldValue, crmRefId, 31, URL_FIELD, finalOrder);
                            else
                                showAlertMsg("Message", EDITING_NOT_ALLOWED);
                        });
                        break;
                    case IMAGE:
                        textViewSub.setVisibility(GONE);
                        rlImage.setVisibility(VISIBLE);
                        if (taskStatus == DeliforceConstants.TASK_ARRIVED || taskStatus == TASK_STARTED) {
                            ivImgLock.setVisibility(GONE);
                        } else {
                            ivImgLock.setVisibility(VISIBLE);
                        }
                        Glide.with(getApplicationContext()).load(fieldValue).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(imgTemplate);

                        imgTemplate.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (permitAgent.equals(READ_AND_WRITE)) {
                                    orderValue = String.valueOf(order);
                                    templateFieldName = fieldName;
                                    imageType = TEMPLATE_IMAGE;
                                    AskCameraPermissions();
                                } else {
                                    showAlertMsg("Message", EDITING_NOT_ALLOWED);
                                }

                            }
                        });
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!dataType.equals(CHECK_BOX))
                ll_main_view.addView(tempView);
        }
    }

    private void prepareDynamicViewIcons(RelativeLayout rlImage, TextView textViewSub, int taskStatus, int image, boolean isPadLockEnabled) {
       /* if (taskOneDetails.getSettings().getEnableArrivedStatus()) {
            if (taskStatus == DeliforceConstants.TASK_ARRIVED) {
                textViewSub.setCompoundDrawablesRelativeWithIntrinsicBounds(image, 0, 0, 0);
            } else if (isPadLockEnabled) {
                textViewSub.setCompoundDrawablesRelativeWithIntrinsicBounds(image, 0, R.drawable.ic_padlock, 0);
            }
        } else {
            if (taskStatus == DeliforceConstants.TASK_STARTED) {
                textViewSub.setCompoundDrawablesRelativeWithIntrinsicBounds(image, 0, 0, 0);
            } else if (isPadLockEnabled) {
                textViewSub.setCompoundDrawablesRelativeWithIntrinsicBounds(image, 0, R.drawable.ic_padlock, 0);
            }
        }*/
        textViewSub.setVisibility(VISIBLE);
        rlImage.setVisibility(GONE);
        if (taskStatus == DeliforceConstants.TASK_ARRIVED || taskStatus == TASK_STARTED) {
            textViewSub.setCompoundDrawablesRelativeWithIntrinsicBounds(image, 0, 0, 0);
        } else if (isPadLockEnabled) {
            textViewSub.setCompoundDrawablesRelativeWithIntrinsicBounds(image, 0, R.drawable.ic_padlock, 0);
        }
    }

    private void showCompoundIcons(TextView textViewSub, int taskStatus) {
        if (taskStatus == 4)
            textViewSub.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_calendar, 0, 0, 0);
        else
            textViewSub.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_calendar, 0, R.drawable.ic_padlock, 0);
    }

    private void updateCheckBox(String fieldName, String checkStatus, int order) {
        templateFieldName = fieldName;
        templateFieldValue = checkStatus;
        orderValue = String.valueOf(order);
        upDateTask(TASK_TEMPLATE_STATUS, CHECK_BOX);
    }

    private void showDatePicker(String fieldValue, String fieldName, final TextView textViewSub, int typePicker, int order) {
        if (typePicker == DATE_PICKER_FUTURE) {
            updateCalender(myFutureCalendar, fieldValue);
        } else if (typePicker == DATE_PICKER_PAST) {
            updateCalender(myPastCalendar, fieldValue);
        } else {
            updateCalender(myCalendar, fieldValue);
        }
        final DatePickerDialog datePickerDialogW = new DatePickerDialog(TaskDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (typePicker == DATE_PICKER_FUTURE) {
                    showSelectedDate(myFutureCalendar, year, month, dayOfMonth, DATE_PICKER_FUTURE);
                } else if (typePicker == DATE_PICKER_PAST) {
                    showSelectedDate(myPastCalendar, year, month, dayOfMonth, DATE_PICKER_PAST);
                } else {
                    showSelectedDate(myCalendar, year, month, dayOfMonth, 103);
                }
            }

            private void showSelectedDate(Calendar myCalendar, int year, int month, int dayOfMonth, int dateTimePicker) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
                textViewSub.setText(sdf.format(myCalendar.getTime()));

                SimpleDateFormat sdfUTC = new SimpleDateFormat(UTC_PATTERN_DATE_TIME, Locale.ENGLISH);
                sdfUTC.setTimeZone(TimeZone.getTimeZone("UTC"));

                templateFieldName = fieldName;
                templateFieldValue = sdfUTC.format(myCalendar.getTime());
                orderValue = String.valueOf(order);

                if (dateTimePicker == DATE_PICKER_FUTURE)
                    upDateTask(TASK_TEMPLATE_STATUS, DATE_FUTURE);
                else if (dateTimePicker == DATE_PICKER_PAST)
                    upDateTask(TASK_TEMPLATE_STATUS, DATE_PAST);
                else
                    upDateTask(TASK_TEMPLATE_STATUS, DATE);
            }
        }, year, month, day);
        if (typePicker == DATE_PICKER_FUTURE)
            datePickerDialogW.getDatePicker().setMinDate(System.currentTimeMillis());
        else if (typePicker == DATE_PICKER_PAST)
            datePickerDialogW.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialogW.show();
    }

    private void updateCalender(Calendar myCalender, String fieldValue) {
        if (!fieldValue.equals("") && fieldValue.contains("-")) {
            System.out.println(fieldValue);
            /*SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
            Date date;
            try {
                date = sdf.parse(fieldValue);
                myCalender.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }*/
            SimpleDateFormat sdf = new SimpleDateFormat(UTC_PATTERN_DATE_TIME, Locale.ENGLISH);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date UTCDate;
            try {
                UTCDate = sdf.parse(fieldValue);
                myCalender.setTime(UTCDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        year = myCalender.get(Calendar.YEAR);
        month = myCalender.get(Calendar.MONTH);
        day = myCalender.get(Calendar.DAY_OF_MONTH);
    }

    private void setDateTimeCalender(Calendar myCalender, String fieldValue) {
        if (!fieldValue.equals("") && fieldValue.contains("-") && fieldValue.contains(":")) {
            System.out.println(fieldValue);
            SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat, Locale.ENGLISH);
            Date date;
            try {
                date = sdf.parse(fieldValue);
                myCalender.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        year = myCalender.get(Calendar.YEAR);
        month = myCalender.get(Calendar.MONTH);
        day = myCalender.get(Calendar.DAY_OF_MONTH);
    }

    private String showDateTimeInOurLocalTime(String fieldValue) {
        SimpleDateFormat sdf = new SimpleDateFormat(UTC_PATTERN_DATE_TIME, Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date UTCDate;
        try {
            UTCDate = sdf.parse(fieldValue);
            SimpleDateFormat ourSDF = new SimpleDateFormat(dateTimeFormat, Locale.ENGLISH);
            ourSDF.setTimeZone(TimeZone.getDefault());
            return ourSDF.format(UTCDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fieldValue;
    }

    private String showDateInOurLocalTime(String fieldValue) {
        SimpleDateFormat sdf = new SimpleDateFormat(UTC_PATTERN_DATE_TIME, Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date UTCDate;
        try {
            UTCDate = sdf.parse(fieldValue);
            SimpleDateFormat ourSDF = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
            ourSDF.setTimeZone(TimeZone.getDefault());
            return ourSDF.format(UTCDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fieldValue;
    }

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

    private void showDateTimePicker(String fieldValue, String fieldName, final TextView textViewSub, final int picketType, int order) {
        final Calendar calendar = Calendar.getInstance();
        final TimePickerDialog timePickerDialog = new TimePickerDialog(TaskDetailsActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar myCalendar;
                if (picketType == DATE_PICKER_FUTURE) {
                    myCalendar = updateFutureTimeCalender(myDateTimeFutureCalendar, hourOfDay, minute);
                } else if (picketType == DATE_PICKER_PAST) {
                    myCalendar = updateFutureTimeCalender(myDateTimePastCalendar, hourOfDay, minute);
                } else {
                    myCalendar = updateFutureTimeCalender(myDateTimeCalendar, hourOfDay, minute);
                }

                Calendar cal = Calendar.getInstance();
                if (picketType == DATE_TIME_PICKER_FUTURE) {
                    if (myCalendar.getTimeInMillis() >= cal.getTimeInMillis())
                        showSelectedDate(myCalendar, DATE_TIME_PICKER_FUTURE);
                    else {
                        showAlertMsg("Message", "Invalid Time");
                    }
                } else if (picketType == DATE_TIME_PICKER_PAST) {
                    if (myCalendar.getTimeInMillis() <= cal.getTimeInMillis())
                        showSelectedDate(myCalendar, DATE_TIME_PICKER_PAST);
                    else {
                        showAlertMsg("Message", "Invalid Time");
                    }
                } else
                    showSelectedDate(myCalendar, DATE_TIME_PICKER);
            }

            private Calendar updateFutureTimeCalender(Calendar myTimeFutureCalendar, int hourOfDay, int minute) {
                myTimeFutureCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myTimeFutureCalendar.set(Calendar.MINUTE, minute);
                return myTimeFutureCalendar;
            }

            private void showSelectedDate(Calendar myCalendar, int dataType) {
                SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat, Locale.ENGLISH);
                textViewSub.setText(sdf.format(myCalendar.getTime()));

                SimpleDateFormat sdfUTC = new SimpleDateFormat(UTC_PATTERN_DATE_TIME, Locale.ENGLISH);
                sdfUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
                templateFieldName = fieldName;
                templateFieldValue = sdfUTC.format(myCalendar.getTime());
                orderValue = String.valueOf(order);

                if (dataType == DATE_TIME_PICKER_FUTURE)
                    upDateTask(TASK_TEMPLATE_STATUS, DATE_TIME_FUTURE);
                else if (dataType == DATE_TIME_PICKER_PAST)
                    upDateTask(TASK_TEMPLATE_STATUS, DATE_TIME_PAST);
                else
                    upDateTask(TASK_TEMPLATE_STATUS, DATE_TIME);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

        if (picketType == DATE_TIME_PICKER_FUTURE) {
            setDateTimeCalender(myDateTimeFutureCalendar, fieldValue);
        } else if (picketType == DATE_TIME_PICKER_PAST) {
            setDateTimeCalender(myDateTimePastCalendar, fieldValue);
        } else {
            setDateTimeCalender(myDateTimeCalendar, fieldValue);
        }

        final DatePickerDialog datePickerDialogW = new DatePickerDialog(TaskDetailsActivity.this, (view, year, month, dayOfMonth) -> {
            if (picketType == DATE_PICKER_FUTURE) {
                updateFutureDateCalender(myDateTimeFutureCalendar, year, month, dayOfMonth);
            } else if (picketType == DATE_PICKER_PAST) {
                updateFutureDateCalender(myDateTimePastCalendar, year, month, dayOfMonth);
            } else {
                updateFutureDateCalender(myDateTimeCalendar, year, month, dayOfMonth);
            }
            timePickerDialog.show();
        }, year, month, day);

        if (picketType == 1)
            datePickerDialogW.getDatePicker().setMinDate(System.currentTimeMillis());
        else if (picketType == 2)
            datePickerDialogW.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialogW.show();
    }

    private void updateFutureDateCalender(Calendar myDateTimeCalendar, int year, int month, int dayOfMonth) {
        myDateTimeCalendar.set(Calendar.YEAR, year);
        myDateTimeCalendar.set(Calendar.MONTH, month);
        myDateTimeCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    private void showDropDown(String fieldName, List<DropDownOptions> finalOptionsArray,
                              ArrayList<ZohoItemInfo> finalZohoArray, final TextView textViewSub, int order) {
//        String[] fieldValues;
//        if (finalOptionsArray != null) {
//            fieldValues = new String[finalOptionsArray.size()];
//            for (int arrayLength = 0; arrayLength < finalOptionsArray.size(); arrayLength++) {
//                fieldValues[arrayLength] = finalOptionsArray.get(arrayLength).getValue();
//            }
//        } else
//            return;
//        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
//        builder.setCancelable(false);
//        builder.setTitle(fieldName);
//        final String[] finalFieldValues = fieldValues;
//        builder.setItems(fieldValues, (dialog, which) -> {
//            if (finalFieldValues[which].equals("Select")) {
//                return;
//            }
//            textViewSub.setText(finalFieldValues[which]);
//
//            templateFieldName = fieldName;
//            templateFieldValue = finalFieldValues[which];
//            Log.e("orderdropdowwm",""+order);
//            orderValue = ""+order;
//            Log.e("orfinalValue",""+order);
//            upDateTask(TASK_TEMPLATE_STATUS, DROP_DOWN);
//            dialog.dismiss();
//        });
//        builder.setNegativeButton(android.R.string.cancel,
//                (dialog, which) -> dialog.dismiss());
//        androidx.appcompat.app.AlertDialog alert = builder.create();
//        ListView listView=alert.getListView();
//        listView.setDivider(new ColorDrawable(Color.LTGRAY)); // set color
//        listView.setDividerHeight(2); // set height
//        alert.show();


        if (finalOptionsArray != null) {

            DropDownDialog dropDownDialog = new DropDownDialog(TaskDetailsActivity.this, R.style.MyDialogStyle, textViewSub.getText().toString(),
                    finalOptionsArray,
                    new DropDownDialog.DropDialogInterface() {
                        @Override
                        public void dropView(DropDownOptions value) {
//                       if (value.equalsIgnoreCase("Select")){
//                           Toast.makeText(TaskDetailsActivity.this, getString(R.string.valid_one), Toast.LENGTH_SHORT).show();
//                           return;
//                       }
                            textViewSub.setText(value.getValue());

                            templateFieldName = fieldName;
                            templateFieldValue = value.getValue();
                            templateFieldId = value.getId();

                            /**
                             * check crm enabled & update selected quantity into template quantity
                             */
                         /*   if (finalZohoArray != null)
                                for (int i = 0; i < finalZohoArray.size(); i++) {
                                    if (finalZohoArray.get(i).getId().equals(value.getId())) {
                                        templateFieldQty = finalZohoArray.get(i).getQuantity();
                                        break;
                                    }
                                }*/
                            orderValue = "" + order;
                            upDateTask(TASK_TEMPLATE_STATUS, DROP_DOWN);

                        }
                    });
            dropDownDialog.show();
            dropDownDialog.setCancelable(true);
            dropDownDialog.setCanceledOnTouchOutside(true);
        } else {
            return;
        }


    }

    private void showEditText(final TextView textViewSub, String fieldName, String fieldValue,
                              String crmRefId,
                              int typeClassNumber, String dataType, int order) {


        TaskDetailEditTextDialog taskDetailEditTextDialog = new TaskDetailEditTextDialog(
                TaskDetailsActivity.this, R.style.MyDialogStyle, textViewSub.getText().toString(),
                fieldName, fieldValue, typeClassNumber, dataType, new TaskDetailEditTextDialog.editTextInterface() {
            @Override
            public void getEditValue(String value) {
                if (!loginPrefManager.isCrmEnabled() || crmRefId.isEmpty()) {
                    textViewSub.setText(value);
                    templateFieldName = fieldName;
                    templateFieldValue = value;
                    orderValue = String.valueOf(order);
                    upDateTask(TASK_TEMPLATE_STATUS, dataType);
                } else {
                    if (!isFinishing()) show_loader();
                    String fieldId = "";

                    for (int i = 0; i < taskOneDetails.getTemplates().size(); i++) {
                        if (taskOneDetails.getTemplates().get(i).getCrmRefId() != null &&
                                taskOneDetails.getTemplates().get(i).getCrmRefId().equals(crmRefId)) {
                            for (int j = 0; j < taskOneDetails.getTemplates().get(i).getZohoItemInfo().size(); j++) {
                                ZohoItemInfo zohoItemInfo = taskOneDetails.getTemplates().get(i).getZohoItemInfo().get(j);
                                if (zohoItemInfo.getName().equals(taskOneDetails.getTemplates().get(i).getFieldValue())) {
                                    fieldId = zohoItemInfo.getId();
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    if (!fieldId.isEmpty()) {
                        crmFieldId = fieldId;
                        templateFieldQty = "0";
                        apiService.getCrmDetails(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken(), new CrmRequest()).enqueue(new Callback<CrmData>() {
                            @Override
                            public void onResponse(@NotNull Call<CrmData> call, @NotNull Response<CrmData> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    for (int i = 0; i < response.body().getData().size(); i++) {
                                        if (response.body().getData().get(i).getItem_id().equals(crmFieldId)) {
                                            templateFieldQty = response.body().getData().get(i).getAvailable_stock();
                                            break;
                                        }
                                    }
                                }

                                if (Integer.parseInt(value) <= Integer.parseInt(templateFieldQty)) {
                                    textViewSub.setText(value);
                                    templateFieldName = fieldName;
                                    templateFieldValue = value;
                                    orderValue = String.valueOf(order);
                                    upDateTask(TASK_TEMPLATE_STATUS, dataType);
                                } else {
                                    if (!isFinishing()) dismiss_loader();
                                    Toast.makeText(getApplicationContext(), getString(R.string.exceed_qty_dropdown) + " "
                                            + templateFieldQty, Toast.LENGTH_LONG).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<CrmData> call, Throwable t) {
                                if (!isFinishing()) dismiss_loader();
                            }
                        });
                    } else {
                        if (!isFinishing()) dismiss_loader();
                    }
                }
            }
        });
        taskDetailEditTextDialog.show();
        taskDetailEditTextDialog.setCancelable(true);
        taskDetailEditTextDialog.setCanceledOnTouchOutside(true);
    }

    private void showCheckBox(String fieldValue, String fieldName, final boolean[] selectedtruefalse,
                              final String[] items, final TextView textViewSub, int order) {
        alertdialogbuilder = new androidx.appcompat.app.AlertDialog.Builder(TaskDetailsActivity.this);

        ItemsIntoList = Arrays.asList(items);
        View view = getLayoutInflater().inflate(R.layout.titlebar, null);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText(fieldName);

        alertdialogbuilder.setCustomTitle(view);

        alertdialogbuilder.setMultiChoiceItems(items, selectedtruefalse,
                (dialog, selectedItemId, isSelected) -> {

                });
        alertdialogbuilder.setPositiveButton("OK", (dialog, which) -> {
            StringBuilder selectedValues = new StringBuilder();
            int count = 0;
            int countSelected = 0;

            while (count < selectedtruefalse.length) {
                boolean value = selectedtruefalse[count];
                if (value) {
                    selectedValues.append(items[count]);
                    selectedValues.append(",");
                    selectedArray.add(new JsonPrimitive(items[count]));
                    countSelected++;
                }
                count++;
            }
            if (countSelected > 0) {
                String dataCheked;
                if (selectedValues.toString().contains(","))
                    dataCheked = selectedValues.toString().substring(0, selectedValues.length() - 1);
                else dataCheked = selectedValues.toString();

                mSelectedValues = dataCheked;

                templateFieldName = fieldName;
                templateFieldValue = fieldValue;
                orderValue = String.valueOf(order);
                upDateTask(TASK_TEMPLATE_STATUS, CHECK_LIST);

                textViewSub.setText(mSelectedValues);
            } else
                showShortToastMessage("Kindly select the options to update");
        });

//        alertdialogbuilder.setNeutralButton("Cancel", (dialog, which) -> {
//        });

        androidx.appcompat.app.AlertDialog dialog = alertdialogbuilder.create();
        ListView listView = dialog.getListView();
        listView.setDivider(new ColorDrawable(Color.LTGRAY)); // set color
        listView.setDividerHeight(1); // set height
        listView.setFooterDividersEnabled(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
    }

    private void buildBraodCast() {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Log.e("receivercalled", "receiverCalled");
                if (intent != null) {
//                    startTask();
                }
            }
        };

        LocalBroadcastManager.getInstance(TaskDetailsActivity.this).registerReceiver(broadcastReceiver, new IntentFilter("task_start"));
    }

    private void inittoolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar_text = findViewById(R.id.toolbar_title);


        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        toolbar.setNavigationOnClickListener(v -> {
            if (activity_name != null && activity_name.equalsIgnoreCase("navigation")) {
                finish();
            } else if (activity_name != null && activity_name.equalsIgnoreCase("route")) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            } else {
                finish();
            }
        });
    }

    private void initView() {

        toolbar = findViewById(R.id.toolbar);
        taskDetailTitle = findViewById(R.id.toolbar_title);
        tvTaskCompletionTime = findViewById(R.id.tv_task_time);
        custName = findViewById(R.id.tv_customer_name);
        taskName = findViewById(R.id.tv_task_details_name);
        layoutAddOtherDetails = findViewById(R.id.layout_add_other_details);
        task_image_view = findViewById(R.id.task_image_view);
        imgShowCustLocation = findViewById(R.id.img_details_location);
        imgMakeCustCall = findViewById(R.id.img_details_make_call);

        imgMakeCustCall.setVisibility(loginPrefManager.getBooleanValue("driver_call_option_need") ? VISIBLE : INVISIBLE);

        custLocation = findViewById(R.id.tv_customer_location);
        tvTaskDetailsStatus = findViewById(R.id.tv_task_details_task_status);
        taskInformation = findViewById(R.id.layout_task_desc);
        layoutAddNewNote = findViewById(R.id.layout_add_note);
        layoutAddImages = findViewById(R.id.layout_add_images);
        layout_add_barcode = findViewById(R.id.layout_add_barcode);
        layout_actual_customer = findViewById(R.id.layout_actual_customer);
        layoutAddSign = findViewById(R.id.layout_add_sign);
        addSign = findViewById(R.id.tv_add_sign);
        addImages = findViewById(R.id.tv_add_images);
        tv_add_notes = findViewById(R.id.tv_add_notes);
        tvTaskDescription = findViewById(R.id.tv_task_descripion);
        taskInfoRecycler = findViewById(R.id.taskInfoImgsImagesRecycler);
        view = findViewById(R.id.view_task_desc);
        track_layout = findViewById(R.id.track_layout);
        desc_view = findViewById(R.id.desc_view);


        acknowledge_view = findViewById(R.id.acknowledge_view);
        accept_view = findViewById(R.id.accept_view);
        start_view = findViewById(R.id.start_view);
        success_view = findViewById(R.id.success_view);

        customer_unavailable_slide = findViewById(R.id.customer_unavailable_slide);
        customer_unavailable_slide.setText(AppUtils.getButtonStatus(TaskDetailsActivity.this, TASK_STATUS_CUSTOMER_NOT_AVAILABLE));
        lrCustomerUnAvailable = findViewById(R.id.lrCustomerUnAvailable);
        acknowledge_slide = findViewById(R.id.btn_pro_acknowledg_ride);
        acknowledge_slide.setText(AppUtils.getButtonStatus(TaskDetailsActivity.this, TASK_STATUS_ACKNOWLEDGE));
        accept_slide = findViewById(R.id.btn_pro_accept_ride);
        accept_slide.setText(AppUtils.getButtonStatus(TaskDetailsActivity.this, TASK_ACCEPTED));
        decline_slide = findViewById(R.id.btn_pro_decline_ride);
        decline_slide.setText(AppUtils.getButtonStatus(TaskDetailsActivity.this, TASK_DECLINED));
        start_slide = findViewById(R.id.btn_pro_start_ride);
        start_slide.setText(AppUtils.getButtonStatus(TaskDetailsActivity.this, TASK_STARTED));
        arrived_view = findViewById(R.id.ll_arrived_view);
        arrived_slide = findViewById(R.id.sv_arrived_view);
        arrived_slide.setText(AppUtils.getButtonStatus(TaskDetailsActivity.this, TASK_ARRIVED));
        arrived_cancel_slide = findViewById(R.id.sv_arrived_cancel);
        arrived_cancel_slide.setText(AppUtils.getButtonStatus(TaskDetailsActivity.this, DeliforceConstants.TASK_STATUS_CANCELLED));
        cancel_slide = findViewById(R.id.btn_pro_cancel_ride);
        cancel_slide.setText(AppUtils.getButtonStatus(TaskDetailsActivity.this, TASK_STATUS_CANCELLED));
        success_slide = findViewById(R.id.btn_pro_success_ride);
        success_slide.setText(AppUtils.getButtonStatus(TaskDetailsActivity.this, TASK_SUCCESS));
        failed_slide = findViewById(R.id.btn_pro_failure_ride);
        failed_slide.setText(AppUtils.getButtonStatus(TaskDetailsActivity.this, TASK_FAILED));
        image_count = findViewById(R.id.image_count);
        tv_bar_code = findViewById(R.id.tv_bar_code);
        notes_count = findViewById(R.id.note_count);
        signature_enable = findViewById(R.id.signature_added);
        bar_code_added = findViewById(R.id.bar_code_added);
        actual_customer_added = findViewById(R.id.actual_customer_added);

        images_view = findViewById(R.id.selectedImagesRecycler);
        images_view.setVisibility(GONE);

        updateTaskStatus = new UpdateTaskStatus();
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(TaskDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false);
        images_view.setLayoutManager(horizontalLayoutManagaer);

        taskInfoRecycler.setLayoutManager(new LinearLayoutManager(TaskDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));

        onSlidechange();

        onClickevents();

    }

    private void onClickevents() {

        layoutAddImages.setOnClickListener(view -> {
            toAddImages();

        });

        layout_add_barcode.setOnClickListener(view -> barCodeScreenCall("", false, order));

        layout_actual_customer.setOnClickListener(view -> {
            showActualCustomerDialog();
        });

        layoutAddSign.setOnClickListener(view -> goToSignatureScreen());

        imgMakeCustCall.setOnClickListener(view -> AskCallPermission());
        imgShowCustLocation.setOnClickListener(view -> checkOverLayPermissions());


        imvTemplateAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();

            }
        });

        btn_track.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(track_url));
                startActivity(viewIntent);

            }
        });
    }

    private void showActualCustomerDialog() {
        ActualCustomer addActualCustomer = null;
        if (taskOneDetails.getActualcustomers() != null && !taskOneDetails.getActualcustomers().isEmpty()) {
            addActualCustomer = taskOneDetails.getActualcustomers().get(0);
        }
        addActualCustomerDialog = new AddActualCustomerDialog(TaskDetailsActivity.this, R.style.MyDialogStyle,
                addActualCustomer,
                task_id,
                this::updateActualCustomerDetails);
        addActualCustomerDialog.setCanceledOnTouchOutside(true);
        addActualCustomerDialog.show();
    }

    private void updateActualCustomerDetails(AddActualCustomer addActualCustomer, boolean isExisting) {
        addActualCustomerDialog.dismiss();

        if (!isExisting) {
            apiService.insertActualCustomer(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken(), addActualCustomer)
                    .enqueue(new Callback<ActualCustomerOutput>() {
                        @Override
                        public void onResponse(@NonNull Call<ActualCustomerOutput> call, @NonNull Response<ActualCustomerOutput> response) {
                            dismiss_loader();
                            fetchActualCustomerInfo(response);
                        }

                        @Override
                        public void onFailure(@NotNull Call<ActualCustomerOutput> call, @NotNull Throwable t) {
                            dismiss_loader();
                            Timber.tag(TAG).e("onFailure%s", t.getMessage());
                        }
                    });
        } else {
            apiService.updateActualCustomer(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken(), addActualCustomer)
                    .enqueue(new Callback<ActualCustomerOutput>() {
                        @Override
                        public void onResponse(@NonNull Call<ActualCustomerOutput> call, @NonNull Response<ActualCustomerOutput> response) {
                            dismiss_loader();
                            fetchActualCustomerInfo(response);
                        }

                        @Override
                        public void onFailure(@NotNull Call<ActualCustomerOutput> call, @NotNull Throwable t) {
                            dismiss_loader();
                            Timber.tag(TAG).e("onFailure%s", t.getMessage());
                        }
                    });
        }
    }

    private void fetchActualCustomerInfo(Response<ActualCustomerOutput> response) {
        try {
            if (response.raw().code() == 200) {
                actual_customer_added.setVisibility(VISIBLE);
                isActualCustomerAdded = true;
                Toast.makeText(TaskDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                getTaskDetails();
            } else if (response.raw().code() == 401) {
                try {
                    if (AppHelper.getPool().getCurrentUser() != null) {
                        findCurrent();
                    }
                } catch (Exception e) {
                    Timber.e("exxceptioninLocation%s", e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showConfirmationDialog() {
        alertdialogbuilder = new androidx.appcompat.app.AlertDialog.Builder(TaskDetailsActivity.this);
        // Setting Dialog Title
        alertdialogbuilder.setTitle("" + getString(R.string.add_template));
        // Setting Dialog Message
        alertdialogbuilder.setMessage("" + getString(R.string.template_msg));
        // Setting Icon to Dialog
//        alertdialogbuilder.setIcon(R.drawable.save);
        // Setting Positive Yes Button
        alertdialogbuilder.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        // User pressed Cancel button. Write Logic Here
                        upDateTask(DeliforceConstants.TASK_ADD_TEMPLATE_STATUS, "");

                    }
                });
        // Setting Positive Yes Button
        alertdialogbuilder.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        // User pressed No button. Write Logic Here
                        dialog.dismiss();
                    }
                });
        // Showing Alert Message
        alertdialogbuilder.show();
    }

    private void toAddImages() {
        if (layoutAddImages.isClickable()) {
            images_view.setVisibility(VISIBLE);
        }

        IMAGE_COUNT = loginPrefManager.getTaskImageMaxCount("task_image_upload_limit");
        if (file_path.size() < IMAGE_COUNT) {
            IMAGE_COUNT = IMAGE_COUNT - file_path.size();
            imageType = TASK_IMAGE;

            Timber.tag("image_action").e("received");

            AskCameraPermissions();
        } else {
            showShortMessage(getString(R.string.error_max_images));
        }
    }

    /*it will redirect to barcode page*/
    private void barCodeScreenCall(String fieldName, boolean forTemplate, int order) {
        startActivity(new Intent(TaskDetailsActivity.this, BarCodeActivity.class)
                .putExtra("task_id", loginPrefManager.getStringValue("taskId"))
                .putExtra("barcode_task_id", taskOneDetails.getTaskId())
                .putExtra("activity", activity_name)
                .putExtra("field_name", fieldName)
                .putExtra("field_name", fieldName)
                .putExtra("order_value", "" + order)
                .putExtra("fortemplate", forTemplate));
    }

    /*it will redirect to signature page*/
    private void goToSignatureScreen() {
        if (signature_added == null || signature_added.equals("") || signature_added.isEmpty()) {
//            startActivityForResult(new Intent(TaskDetailsActivity.this, SignatureActivity.class)
//                    .putExtra("task_id", loginPrefManager.getStringValue("taskId"))
//                    .putExtra("added_sign", signature_added)
//                    .putExtra("task_success", taskOneDetails.getTaskStatus())
//                    .putExtra("activity", activity_name), SIGNATURE_REQUEST_CODE);
            Log.e("no_signature","received");
        } else {
            startActivityForResult(new Intent(TaskDetailsActivity.this, SignatureActivity.class)
                    .putExtra("task_id", loginPrefManager.getStringValue("taskId"))
                    .putExtra("added_sign", signature_added)
                    .putExtra("task_success", taskOneDetails.getTaskStatus())
                    .putExtra("activity", activity_name), SIGNATURE_REQUEST_CODE);
        }
    }

    private void onSlidechange() {

        customer_unavailable_slide.setOnSlideCompleteListener(slideView -> {
            upDateTask(DeliforceConstants.TASK_STATUS_CUSTOMER_NOT_AVAILABLE, "");
        });


        customer_unavailable_slide.setOnClickListener(v -> {
            upDateTask(DeliforceConstants.TASK_STATUS_CUSTOMER_NOT_AVAILABLE, "");
        });

        acknowledge_slide.setOnSlideCompleteListener(slideView -> {

            if (loginPrefManager.getStringValue("driver_status").equals("3")) {
                showTwoButtonSnackbar();
            } else if (loginPrefManager.getStringValue("driver_status").equalsIgnoreCase("4")) {
                showBlockDialog();
            } else {
                upDateTask(DeliforceConstants.TASK_STATUS_ACKNOWLEDGE, "");

            }
        });


        acknowledge_slide.setOnClickListener(v -> {
            if (loginPrefManager.getStringValue("driver_status").equals("3")) {
                showTwoButtonSnackbar();
            } else if (loginPrefManager.getStringValue("driver_status").equalsIgnoreCase("4")) {
                showBlockDialog();
            } else {

                upDateTask(DeliforceConstants.TASK_STATUS_ACKNOWLEDGE, "");

            }
        });
        accept_slide.setOnSlideCompleteListener(slideView -> {

            if (loginPrefManager.getStringValue("driver_status").equals("3")) {
                showTwoButtonSnackbar();
            } else if (loginPrefManager.getStringValue("driver_status").equalsIgnoreCase("4")) {
                showBlockDialog();
            } else {
                upDateTask(TASK_ACCEPTED, "");
            }
        });


        accept_slide.setOnClickListener(v -> {
            if (loginPrefManager.getStringValue("driver_status").equals("3")) {
                showTwoButtonSnackbar();
            } else if (loginPrefManager.getStringValue("driver_status").equalsIgnoreCase("4")) {
                showBlockDialog();
            } else {
                upDateTask(TASK_ACCEPTED, "");
            }
        });
        start_slide.setOnSlideCompleteListener(slideView -> {
            Boolean isMultiStart = loginPrefManager.getMultiStart();
            if (!isMultiStart) {
                if (loginPrefManager.getStringValue("driver_status").equals("3")) {
                    showTwoButtonSnackbar();
                } else if (loginPrefManager.getStringValue("driver_status").equalsIgnoreCase("4")) {
                    showBlockDialog();
                } else if (loginPrefManager.getStringValue("driver_status").equals("1")) {
                    loginPrefManager.setStringValue("driver_status", "2");
                    if (loginPrefManager.getGlympseEnabled() && glympseTaskID != null) {
                        glympseTask(TASK_STARTED);

                    } else {
                        upDateTask(TASK_STARTED, "");
                    }
                } else {
                    if (loginPrefManager.getGlympseEnabled() && glympseTaskID != null) {
                        glympseTask(TASK_STARTED);
                    } else {
                        upDateTask(TASK_STARTED, "");
                    }
                }
            } else if (isMultiStart){
                String driverStatus = loginPrefManager.getStringValue("driver_status");
                if (!driverStatus.equals("2")){
                    if (loginPrefManager.getStringValue("driver_status").equals("3")) {
                        showTwoButtonSnackbar();
                    } else if (loginPrefManager.getStringValue("driver_status").equalsIgnoreCase("4")) {
                        showBlockDialog();
                    } else if (loginPrefManager.getStringValue("driver_status").equals("1")) {
                        loginPrefManager.setStringValue("driver_status", "2");
                        if (loginPrefManager.getGlympseEnabled() && glympseTaskID != null) {
                            glympseTask(TASK_STARTED);

                        } else {
                            upDateTask(TASK_STARTED, "");
                        }
                    } else {
                        if (loginPrefManager.getGlympseEnabled() && glympseTaskID != null) {
                            glympseTask(TASK_STARTED);
                        } else {
                            upDateTask(TASK_STARTED, "");
                        }
                    }

                }else{
                    show_error_response("Complete your existing task.");
                }
            }

        });

        start_slide.setOnClickListener(v -> {
            // String driverStatus = loginPrefManager.getStringValue("driver_status");
            Boolean isMultiStart = loginPrefManager.getMultiStart();

            if (!isMultiStart) {
                if (loginPrefManager.getStringValue("driver_status").equals("3")) {
                    showTwoButtonSnackbar();
                } else if (loginPrefManager.getStringValue("driver_status").equalsIgnoreCase("4")) {
                    showBlockDialog();
                } else if (loginPrefManager.getStringValue("driver_status").equals("1")) {
                    loginPrefManager.setStringValue("driver_status", "2");
                    if (loginPrefManager.getGlympseEnabled() && glympseTaskID != null) {
                        glympseTask(TASK_STARTED);

                    } else {
                        upDateTask(TASK_STARTED, "");
                    }
                } else {
                    if (loginPrefManager.getGlympseEnabled() && glympseTaskID != null) {
                        glympseTask(TASK_STARTED);
                    } else {
                        upDateTask(TASK_STARTED, "");
                    }
                }
            } else if (isMultiStart){
                String driverStatus = loginPrefManager.getStringValue("driver_status");
                if (!driverStatus.equals("2")){
                    if (loginPrefManager.getStringValue("driver_status").equals("3")) {
                        showTwoButtonSnackbar();
                    } else if (loginPrefManager.getStringValue("driver_status").equalsIgnoreCase("4")) {
                        showBlockDialog();
                    } else if (loginPrefManager.getStringValue("driver_status").equals("1")) {
                        loginPrefManager.setStringValue("driver_status", "2");
                        if (loginPrefManager.getGlympseEnabled() && glympseTaskID != null) {
                            glympseTask(TASK_STARTED);

                        } else {
                            upDateTask(TASK_STARTED, "");
                        }
                    } else {
                        if (loginPrefManager.getGlympseEnabled() && glympseTaskID != null) {
                            glympseTask(TASK_STARTED);
                        } else {
                            upDateTask(TASK_STARTED, "");
                        }
                    }

                }else{
                    show_error_response("Complete your existing task.");
                }
            }
        });


        arrived_slide.setOnSlideCompleteListener(slideView -> {

            if (loginPrefManager.getStringValue("driver_status").equals("3")) {
                showTwoButtonSnackbar();

            } else if (loginPrefManager.getStringValue("driver_status").equalsIgnoreCase("4")) {
                showBlockDialog();
            } else if (loginPrefManager.getStringValue("driver_status").equals("1")) {
//                complete_arrived_task();
                loginPrefManager.setStringValue("driver_status", "2");
                if (!taskOneDetails.getSettings().getEnableArrivedStatus()) {
                    upDateTask(DeliforceConstants.TASK_ARRIVED, "");
                } else {
                    getDistance(DeliforceConstants.TASK_ARRIVED);
                }

            } else {
//                complete_arrived_task();
                if (!taskOneDetails.getSettings().getEnableArrivedStatus())
                    upDateTask(DeliforceConstants.TASK_ARRIVED, "");
                else {
                    getDistance(DeliforceConstants.TASK_ARRIVED);
                }
            }

        });

        arrived_slide.setOnClickListener(v -> {
            if (loginPrefManager.getStringValue("driver_status").equals("3")) {
                showTwoButtonSnackbar();

            } else if (loginPrefManager.getStringValue("driver_status").equalsIgnoreCase("4")) {
                showBlockDialog();
            } else if (loginPrefManager.getStringValue("driver_status").equals("1")) {
                loginPrefManager.setStringValue("driver_status", "2");
//                complete_arrived_task();
//                upDateTask(DeliforceConstants.TASK_ARRIVED, "");
                if (!taskOneDetails.getSettings().getEnableArrivedStatus()) {
                    upDateTask(DeliforceConstants.TASK_ARRIVED, "");
                } else {
                    getDistance(DeliforceConstants.TASK_ARRIVED);
                }
            } else {
//                complete_arrived_task();
//                upDateTask(DeliforceConstants.TASK_ARRIVED, "");
                if (!taskOneDetails.getSettings().getEnableArrivedStatus()) {
                    upDateTask(DeliforceConstants.TASK_ARRIVED, "");
                } else {
                    getDistance(DeliforceConstants.TASK_ARRIVED);
                }
            }
        });

        success_slide.setOnSlideCompleteListener(slideView -> {
            onSuccessSlide();
        });

        success_slide.setOnClickListener(v -> {
            onSuccessSlide();
        });


        cancel_slide.setOnSlideCompleteListener(slideView -> {

            if (loginPrefManager.getStringValue("driver_status").equals("3")) {
                showTwoButtonSnackbar();

            } else {
                showCancleDialog(DeliforceConstants.TASK_STATUS_CANCELLED);
            }

        });

        cancel_slide.setOnClickListener(v -> {
            if (loginPrefManager.getStringValue("driver_status").equals("3")) {

                showTwoButtonSnackbar();

            } else {
                showCancleDialog(DeliforceConstants.TASK_STATUS_CANCELLED);
            }
        });

        arrived_cancel_slide.setOnSlideCompleteListener(slideView -> {

            if (loginPrefManager.getStringValue("driver_status").equals("3")) {
                showTwoButtonSnackbar();

            } else {
                showCancleDialog(DeliforceConstants.TASK_STATUS_CANCELLED);
            }

        });
        arrived_cancel_slide.setOnClickListener(v -> {
            if (loginPrefManager.getStringValue("driver_status").equals("3")) {

                showTwoButtonSnackbar();

            } else {
                showCancleDialog(DeliforceConstants.TASK_STATUS_CANCELLED);
            }
        });

        failed_slide.setOnSlideCompleteListener(slideView -> {


            if (loginPrefManager.getStringValue("driver_status").equals("3")) {
                showTwoButtonSnackbar();
            } else {
                showCancleDialog(DeliforceConstants.TASK_FAILED);
            }

        });

        failed_slide.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginPrefManager.getStringValue("driver_status").equals("3")) {
                    showTwoButtonSnackbar();
                } else {
                    showCancleDialog(DeliforceConstants.TASK_FAILED);
                }
            }
        });

        decline_slide.setOnSlideCompleteListener(slideView -> {

            if (loginPrefManager.getStringValue("driver_status").equals("3")) {
                showTwoButtonSnackbar();

            } else {
                showCancleDialog(TASK_DECLINED);
            }

        });

        decline_slide.setOnClickListener(v -> {
            if (loginPrefManager.getStringValue("driver_status").equals("3")) {
                showTwoButtonSnackbar();

            } else {
                showCancleDialog(TASK_DECLINED);
            }
        });

        layoutAddNewNote.setOnClickListener(v -> goToNotesScreen());

    }

    private boolean isWithinArrivalDistance() {
        return false;
    }

    private void complete_arrived_task() {
        int mandatoryFields = 0;
        StringBuilder mandatoryError = new StringBuilder();
        if (taskOneDetails.getTemplates() != null && taskOneDetails.getTemplates().size() > 0) {
            for (int tempCount = 0; tempCount < taskOneDetails.getTemplates().size(); tempCount++) {
                if (taskOneDetails.getTemplates().get(tempCount).getMandatoryFields().equals(MANDATORY)) {
                    if (taskOneDetails.getTemplates().get(tempCount).getDataType().equals(CHECK_LIST)) {
                        if (taskOneDetails.getTemplates().get(tempCount).getSelectedValues() == null ||
                                taskOneDetails.getTemplates().get(tempCount).getSelectedValues().equals("")) {
                            mandatoryError.append("   - ").append(taskOneDetails.getTemplates().get(tempCount).getFieldName()).append("\n");
                        } else
                            mandatoryFields += 1;
                    } else if (taskOneDetails.getTemplates().get(tempCount).getFieldValue().equals("") ||
                            taskOneDetails.getTemplates().get(tempCount).getFieldValue().equals("Select") ||
                            taskOneDetails.getTemplates().get(tempCount).getFieldValue().equals("false")) {
                        mandatoryError.append("   - ").append(taskOneDetails.getTemplates().get(tempCount).getFieldName()).append("\n");
                    } else
                        mandatoryFields += 1;
                }
            }
        } else
            mandatoryCount = 0;

        if (mandatoryFields == mandatoryCount) {
            if (loginPrefManager.getGlympseEnabled() && glympseTaskID != null) {
                glympseTask(DeliforceConstants.TASK_ARRIVED);
            } else {
                upDateTask(DeliforceConstants.TASK_ARRIVED, "");

            }

        } else
            showAlertMsg("" + getString(R.string.field_mandatory), mandatoryError.toString());
    }

    private void refresh() {
        try {
            EnRouteWrapper.instance().manager().getTaskManager().refresh();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void glympseTask(int taskStatus) {

        if (!glympseTaskID.isEmpty()) {
            int taskId = Integer.parseInt(glympseTaskID);

            if (DeliforceConstants.TASK_STATUS_ACKNOWLEDGE == taskStatus) {
                //Ack Task
                upDateTask(DeliforceConstants.TASK_STATUS_ACKNOWLEDGE, "");


            } else if (TASK_STARTED == taskStatus) {
                //set a tassk in started mode
                show_loader();
                if (EnRouteWrapper.instance().manager().getTaskManager()
                        .findTaskById(taskId) == null) {
                    refresh();
                }


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startGlympseTask();
                    }
                }, 2000);


            } else if (DeliforceConstants.TASK_SUCCESS == taskStatus) {

                completeGlympseTask();
            } else if (DeliforceConstants.TASK_ARRIVED == taskStatus) {
                //set a tassk in started mode
                show_loader();
                if (EnRouteWrapper.instance().manager().getTaskManager()
                        .findTaskById(taskId) == null) {
                    refresh();
                }


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        arrivedStateGlympseTask();
                    }
                }, 2000);


            }

        }
    }

    private void arrivedStateGlympseTask() {
        int taskId = Integer.valueOf(glympseTaskID);
        glympTask = EnRouteWrapper.instance().manager().getTaskManager()
                .findTaskById(taskId);

        EnRouteWrapper.instance().manager().getTaskManager().setTaskPhase(glympTask,
                EnRouteConstants.PHASE_PROPERTY_ARRIVED());

    }

    private void completeGlympseTask() {
        show_loader();
        int taskId = Integer.valueOf(glympseTaskID);
        if (EnRouteWrapper.instance().manager().getTaskManager()
                .findTaskById(taskId) == null) {
            refresh();
        }

        glympTask = EnRouteWrapper.instance().manager().getTaskManager()
                .findTaskById(taskId);
        //if task is already started in glympse
        //directly hit started deliforce api
        if (EnRouteConstants.TASK_STATE_STARTED == glympTask.getState()) {
            if (!glympTask.getPhase().equals(EnRouteConstants.PHASE_PROPERTY_COMPLETED())) {
                EnRouteWrapper.instance().manager().getTaskManager().setTaskPhase(glympTask,
                        EnRouteConstants.PHASE_PROPERTY_COMPLETED());
            } else {
                completeTask();
            }
            /*if (!glympTask.getPhase().equals(EnRouteConstants.PHASE_PROPERTY_ARRIVED())) {
                if (!EnRouteConstants.PHASE_PROPERTY_LIVE().equals(glympTask.getPhase())) {
                    EnRouteWrapper.instance().manager().getTaskManager().setTaskPhase(glympTask,
                            EnRouteConstants.PHASE_PROPERTY_LIVE());
                } else {
                    completeTask();
                }
            } else {
                completeTask();
            }*/
        } else {

            EnRouteWrapper.instance().manager().getTaskManager().startTask(glympTask);
            EnRouteWrapper.instance().manager().getTaskManager().setTaskPhase(glympTask,
                    EnRouteConstants.PHASE_PROPERTY_LIVE());
        }

        //complete the task
        EnRouteWrapper.instance().manager().getTaskManager().completeOperation(glympTask.getOperation());

    }

    private void completeTask() {
        showLoader = false;
//                  upDateTask(TASK_STARTED, "");
//                  upDateTask(DeliforceConstants.TASK_SUCCESS, "");
        updateOTPTask(DeliforceConstants.TASK_SUCCESS, "", 0, false);
    }

    private void startGlympseTask() {
        int taskId = Integer.valueOf(glympseTaskID);
        glympTask = EnRouteWrapper.instance().manager().getTaskManager()
                .findTaskById(taskId);

        //if task is already started in glympse
        //directly hit started deliforce api
        if (EnRouteConstants.TASK_STATE_STARTED == glympTask.getState()) {
            if (!EnRouteConstants.PHASE_PROPERTY_LIVE().equals(glympTask.getPhase())) {
                EnRouteWrapper.instance().manager().getTaskManager().setTaskPhase(glympTask,
                        EnRouteConstants.PHASE_PROPERTY_LIVE());
            } else {
                showLoader = false;
                upDateTask(TASK_STARTED, "");

            }


        } else {

            EnRouteWrapper.instance().manager().getTaskManager().startTask(glympTask);
            EnRouteWrapper.instance().manager().getTaskManager().setTaskPhase(glympTask,
                    EnRouteConstants.PHASE_PROPERTY_LIVE());
        }
    }

    /*redirect to notes screen*/
    private void goToNotesScreen() {
        startActivity(new Intent(TaskDetailsActivity.this, NotesActivity.class)
                .putExtra("taskId", loginPrefManager.getStringValue("taskId"))
                .putExtra("task_status", taskOneDetails.getTaskStatus())
                .putExtra("activity", activity_name));
    }

    @Override
    public void onResume() {
        super.onResume();

        Timber.e("onResume%s", "---");
        getTaskDetails();

    }

    private void getTaskDetails() {
        try {
            if (!isFinishing()) show_loader();

            SuperTask superTask = new SuperTask();

            superTask.setSuperTaskId(loginPrefManager.getStringValue("taskId"));

            notes_list = new ArrayList<>();
            barcode_data = new ArrayList<>();



            apiService.getParticularDetails(loginPrefManager.getCogintoToken(),superTask).enqueue(new Callback<ParticluarTaskDetails>() {
                @Override
                public void onResponse(@NotNull Call<ParticluarTaskDetails> call, @NotNull Response<ParticluarTaskDetails> response) {

                    if (!isFinishing()) dismiss_loader();

                    try {
                        if (response.raw().code() == 200) {
                            taskOneDetails = response.body().getTaskData();


                            if(taskOneDetails.getActualCompletedTime()!=null){
                                tv_end_time.setText(showDateTimeInOurLocalTime(taskOneDetails.getActualCompletedTime()));
                            }

                            if (taskOneDetails.getActualStartedTime()!=null){
                                tv_start_time.setText(showDateTimeInOurLocalTime(taskOneDetails.getActualStartedTime()));
                            }


                            if (taskOneDetails.getTrackingLink()!=null) {
                                track_url = taskOneDetails.getTrackingLink();
                                btn_track.setVisibility(GONE);
                            }else{
                                btn_track.setVisibility(GONE);
                            }


                            if (taskOneDetails.getCustomerNotes()!=null){
                                tv_task_notes.setText(taskOneDetails.getCustomerNotes());
                                layout_notes.setVisibility(VISIBLE);
                            }else{
                                layout_notes.setVisibility(GONE);
                            }

                            signature_added = taskOneDetails.getDriverSignature();


                            if (!signature_added.equals("")) {
                                signature_enable.setVisibility(VISIBLE);
                                layoutAddSign.setVisibility(VISIBLE);
                            }else {
                                signature_enable.setVisibility(GONE);
                                layoutAddSign.setVisibility(GONE);
                            }

                            tvTaskDetailsStatus.setTextColor(Color.parseColor(taskOneDetails.getColor()));


                            Timber.tag("task_details_status").e(AppUtils.getStatus(TaskDetailsActivity.this, taskOneDetails.getTaskStatus()));


                            tvTaskDetailsStatus.setText(AppUtils.getStatus(TaskDetailsActivity.this, taskOneDetails.getTaskStatus()));


                            notes_list = response.body().getTaskData().getNotes();
                            if (notes_list == null || notes_list.isEmpty()){
                                layoutAddNewNote.setVisibility(GONE);
                            }else{
                                notes_list.size();
                                layoutAddNewNote.setVisibility(VISIBLE);
                            }



                            // Log.e("customer_notes",taskOneDetails.getCustomerNotes());

                            if (loginPrefManager.isCustomerNotesEnabled()){
                                if (taskOneDetails.getCustomerNotes()!=null){
                                    view_notes.setVisibility(VISIBLE);
                                   // layout_notes.setVisibility(VISIBLE);



                                    Log.e("received_note",taskOneDetails.getCustomerNotes());


                                }else {
                                    Log.e("customer_notes","disabled");
                                    view_notes.setVisibility(GONE);
                                   // layout_notes.setVisibility(GONE);
                                }
                            }else {
                                Log.e("customer_notes11","disabled");
                                view_notes.setVisibility(GONE);
                               // layout_notes.setVisibility(GONE);
                            }

                            if (loginPrefManager.isCaptureLocationEnabled())
                                isCaptureLocation = taskOneDetails.isCaptureLocation();

                            if (response.body().getTaskData().getSettings().getActionBlock().getDistance() != null) {

                                loginPrefManager.setRadiusValidation("radiusValidation", response.body().getTaskData().getSettings().getActionBlock().getDistance().getExist());

                                //for success distance validation
                                if (response.body().getTaskData().getSettings().getActionBlock().getDistance().getValue() != null &&
                                        !response.body().getTaskData().getSettings().getActionBlock().getDistance().getValue().isEmpty()) {
                                    loginPrefManager.setRadiusDistance("radiusDistance", response.body().getTaskData().getSettings().getActionBlock().getDistance().getValue());
                                } else {
                                    loginPrefManager.setRadiusDistance("radiusDistance", "0.0");

                                }
                            }

                            System.out.println(taskOneDetails.getTemplateName());

                            if (taskOneDetails.getOrderId() != null && taskOneDetails.getOrderId().length() > 0) {
                                ll_order_id.setVisibility(VISIBLE);
                                view_order_id.setVisibility(VISIBLE);
                                txt_order_id_no.setText(taskOneDetails.getOrderId());
                            }
                            if (taskOneDetails.getReferenceId() != null && taskOneDetails.getReferenceId().length() > 0) {
                                llRefId.setVisibility(VISIBLE);
                                txt_ref_id_no.setText(taskOneDetails.getReferenceId());
                            }
                            if (taskOneDetails.getTemplates() != null && taskOneDetails.getTemplates().size() > 0) {
                                ll_main_view.removeAllViews();
                                llTemplateView.setVisibility(VISIBLE);
                                TemplateView.setVisibility(VISIBLE);
                                belowTemplateView.setVisibility(VISIBLE);


                                mandatoryCount = 0;
                                createDynamicView(taskOneDetails.getTemplates(), taskOneDetails.getTaskStatus());
                                /*if (taskOneDetails.getSettings().getEnableArrivedStatus()) {
                                    if (taskOneDetails.getTaskStatus() != DeliforceConstants.TASK_ARRIVED) {
                                        setDynamicViewEnabled(false);
                                    } else {
                                        setDynamicViewEnabled(true);
                                    }
                                } else if (taskOneDetails.getTaskStatus() != DeliforceConstants.TASK_STARTED) {
                                    setDynamicViewEnabled(false);
                                } else {
                                    setDynamicViewEnabled(true);
                                }*/
                                if (taskOneDetails.getTaskStatus() == TASK_STARTED ||
                                        taskOneDetails.getTaskStatus() == DeliforceConstants.TASK_ARRIVED) {
                                    setDynamicViewEnabled(true);
                                    if (taskOneDetails.getRepeat() && taskOneDetails.getDriverTemplateRepeat()) {
                                        imvTemplateAdd.setVisibility(VISIBLE);
                                    } else {
                                        imvTemplateAdd.setVisibility(GONE);
                                    }
                                } else {
                                    setDynamicViewEnabled(false);
                                    imvTemplateAdd.setVisibility(GONE);
                                }
                                ll_main_view.setVisibility(VISIBLE);

                                tvTemplateName.setText(taskOneDetails.getTemplateName());

                            } else
                                ll_main_view.setVisibility(GONE);

                            if (taskOneDetails.getSettings().getEnableArrivedStatus() && taskOneDetails.getTaskStatus() == TASK_ARRIVED) {
                                if (isCaptureLocation) {
                                    menuTaskLocationUpdate.setVisible(true);
                                } else {
                                    menuTaskLocationUpdate.setVisible(false);
                                }
                            } else if (!taskOneDetails.getSettings().getEnableArrivedStatus() && taskOneDetails.getTaskStatus() == TASK_STARTED) {
                                if (isCaptureLocation) {
                                    menuTaskLocationUpdate.setVisible(true);
                                } else {
                                    menuTaskLocationUpdate.setVisible(false);
                                }
                            } else {
                                menuTaskLocationUpdate.setVisible(false);
                            }
                            /*if (taskOneDetails.getTaskStatus() == TASK_STARTED || taskOneDetails.getTaskStatus() == TASK_ARRIVED) {

                                if (isCaptureLocation) {
                                    menuTaskLocationUpdate.setVisible(true);
                                } else {
                                    menuTaskLocationUpdate.setVisible(false);
                                }
                            } else {
                                menuTaskLocationUpdate.setVisible(false);
                            }*/

                            StartLocation startLocation = taskOneDetails.getStartLocation();

                            double start_lat = startLocation.getCoordinates().get(1);
                            double start_lng = startLocation.getCoordinates().get(0);

                            if (taskOneDetails.getImages() != null) {
                                if (taskOneDetails.getImages().size() > 0) {

                                    task_image_view.setVisibility(VISIBLE);

                                    taskInfoRecycler.setAdapter(new AddImagesAdpater(TaskDetailsActivity.this, "6", taskOneDetails.getImages(), position -> {

                                    }));

                                } else {
                                    image_count.setText("");
                                    image_count.setVisibility(GONE);
                                }
                            }

                            if (taskOneDetails.getActualcustomers() != null && !taskOneDetails.getActualcustomers().isEmpty()) {
                                isActualCustomerAdded = true;
                            }
                            //                    for updating ui based on task
                            setUpActionViews();
                            setTaskCategory(taskOneDetails.getBusinessType());
                            setDeliveryTime();
                            setDeliveryName(taskOneDetails.getBusinessType());
                            custName.setText(taskOneDetails.getName());
                            String address = taskOneDetails.getAddress().getFormattedAddress();
                            if(!taskOneDetails.getFlatNo().isEmpty())
                                address = taskOneDetails.getFlatNo()+", "+address;
                            custLocation.setText(address);
                           // setInitialTaskStatus(taskOneDetails.getTaskStatus());

                            tvTaskDetailsStatus.setTextColor(Color.parseColor(taskOneDetails.getColor()));


                            phone_no = taskOneDetails.getPhone();

                            toolbar_text.setText(getString(R.string.Task) + taskOneDetails.getTaskId());

                            String taskDescription = taskOneDetails.getTaskDescription();

                            if (taskDescription.length() > 0) {
                                taskInformation.setVisibility(VISIBLE);
                                tvTaskDescription.setText(taskDescription);
                                tvTaskDescription.setVisibility(VISIBLE);
                                view.setVisibility(INVISIBLE);
                                desc_view.setVisibility(VISIBLE);
                            }





                            barcode_data = response.body().getTaskData().getBarcodes();
                            if (barcode_data == null) {
                                barcode_data = new ArrayList<>();
                            }
                            loginPrefManager.setDropLatitude(String.valueOf(taskOneDetails.getAddress().getGeometry().getLocation().getLat()));
                            loginPrefManager.setDropLongitude(String.valueOf(taskOneDetails.getAddress().getGeometry().getLocation().getLng()));

                            int task_status = taskOneDetails.getTaskStatus();
                            if (task_status == TASK_STARTED || task_status == TASK_ARRIVED || task_status == TASK_SUCCESS) {
                                if (notes_list.size() > 0) {
                                    notes_count.setVisibility(VISIBLE);
                                    notes_count.setText("" + notes_list.size());
                                }
                                if (barcode_data.size() > 0) {
                                    bar_code_added.setVisibility(VISIBLE);
                                    tv_bar_code.setText(getString(R.string.update_bar_code));

                                }
                            }
                            if (notes_list.size() <= 0) {
                                notes_count.setVisibility(GONE);
                                notes_count.setText("" + notes_list.size());
                            }


                        } else {
                            try {
                                if (AppHelper.getPool().getCurrentUser() != null) {
                                    findCurrent();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        //alert dialog for user already logged in

                    } catch (Exception e) {
                        e.printStackTrace();
                        Timber.tag("Exception_msg").e(e.getMessage());
                    }

                }

                @Override
                public void onFailure(Call<ParticluarTaskDetails> call, Throwable t) {
                    Timber.tag("failure_error").e(t.getMessage());
                    if (!isFinishing()) dismiss_loader();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDynamicViewEnabled(boolean isEnabled) {
        for (int disableChilds = 0; disableChilds < taskOneDetails.getTemplates().size(); disableChilds++) {
            View child = ll_main_view.getChildAt(disableChilds);
            if (child != null)
                setDynamicViewAndChildrenEnabled(child, isEnabled);
        }
    }

    private void setDynamicViewAndChildrenEnabled(View view, boolean enable) {
        view.setEnabled(enable);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int viewGroupCount = 0; viewGroupCount < viewGroup.getChildCount(); viewGroupCount++) {
                setDynamicViewAndChildrenEnabled(viewGroup.getChildAt(viewGroupCount), enable);
            }
        }
    }

    private void setDeliveryName(int bussType) {

        boolean isPickUp;
        switch (bussType) {
            case 1:
                isPickUp = taskOneDetails.getIsPickup();
                if (loginPrefManager.isCustomerCategoryEnabled()){
                     if (isPickUp) {
                        taskName.setText("- "+loginPrefManager.getPickupText());
                    } else {
                        taskName.setText("- "+loginPrefManager.getDeliveryText());
                    }
                }else {
                    if (isPickUp) {
                        taskName.setText(getString(R.string.task_type_pick_up));
                    } else {
                        taskName.setText(getString(R.string.task_type_delivery));
                    }
                }
                break;
            case 2:
                if (loginPrefManager.isCustomerCategoryEnabled()){
                    taskName.setText("- "+loginPrefManager.getAppointmentText());
                }else{
                    taskName.setText(getString(R.string.task_type_appointments));
                }
                break;
            case 3:

                if (loginPrefManager.isCustomerCategoryEnabled()){
                    taskName.setText("- "+loginPrefManager.getFieldWorkForceText());
                }else {
                    taskName.setText(getString(R.string.task_type_field_work));
                }
                break;
        }
    }

    private void setDeliveryTime() {
        String actualDate = taskOneDetails.getDate();
        String formattedTime = (actualDate.length() == 22) ? actualDate.substring(11, 16) : "0" + actualDate.substring(11, 16);
        String timeAmPm = (actualDate.length() == 22) ? actualDate.substring(19, 22) : actualDate.substring(19, 21);
        String dateToDisplay = formattedTime + timeAmPm;

        tvTaskCompletionTime.setText(dateToDisplay);
    }

    private void setUpActionViews() {

        SettingsModel taskSettings = taskOneDetails.getSettings();

        int acknowlege_type = taskSettings.getAcknowledgementType();


        if (taskOneDetails.getTaskStatus() == TASK_ASSIGNED) {

            if (acknowlege_type == DeliforceConstants.IS_TASK_ACKNOWLEDGE_NEEDED) {
                //acknowledge_view.setVisibility(VISIBLE);
            } else if (acknowlege_type == DeliforceConstants.IS_TASK_ACCEPT_REJECT_NEEDED) {
                //accept_view.setVisibility(VISIBLE);
                acknowledge_view.setVisibility(GONE);
            } else if (acknowlege_type == DeliforceConstants.IS_TASK_START_NEEDED) {
                accept_view.setVisibility(GONE);
                acknowledge_view.setVisibility(GONE);
               // start_view.setVisibility(VISIBLE);
            }

        } else if (taskOneDetails.getTaskStatus() == TASK_ACCEPTED || taskOneDetails.getTaskStatus() == TASK_STATUS_ACKNOWLEDGE) {
            //start_view.setVisibility(VISIBLE);
            accept_view.setVisibility(GONE);
            acknowledge_view.setVisibility(GONE);
        } else if (taskOneDetails.getTaskStatus() == TASK_STARTED) {

            start_view.setVisibility(GONE);
            accept_view.setVisibility(GONE);
            acknowledge_view.setVisibility(GONE);

            if (!taskOneDetails.getSettings().getEnableArrivedStatus()) {
                success_view.setVisibility(GONE);
                if (loginPrefManager.isAdditionalStatusEnabled()) {
                    lrCustomerUnAvailable.setVisibility(GONE);
                } else {
                    lrCustomerUnAvailable.setVisibility(GONE);
                }
                if (loginPrefManager.isActualCustomer()) {
                    layout_actual_customer.setVisibility(VISIBLE);
                } else {
                    layout_actual_customer.setVisibility(GONE);
                }
                setupSuccessViewFun(taskSettings, true);
            } else {
               // arrived_view.setVisibility(VISIBLE);
                imgMakeCustCall.setVisibility(loginPrefManager.getBooleanValue("driver_call_option_need") ? VISIBLE : INVISIBLE);
                imgShowCustLocation.setVisibility(VISIBLE);
//                setupSuccessViewFun(taskSettings, false);
            }


        } else if (taskOneDetails.getTaskStatus() == TASK_ARRIVED) {
            start_view.setVisibility(GONE);
            accept_view.setVisibility(GONE);
            acknowledge_view.setVisibility(GONE);
            arrived_view.setVisibility(GONE);
            //success_view.setVisibility(VISIBLE);
            if (loginPrefManager.isAdditionalStatusEnabled()) {
                lrCustomerUnAvailable.setVisibility(VISIBLE);
            } else {
                lrCustomerUnAvailable.setVisibility(GONE);
            }
            if (loginPrefManager.isActualCustomer()) {
                layout_actual_customer.setVisibility(VISIBLE);
            } else {
                layout_actual_customer.setVisibility(GONE);
            }
            setupSuccessViewFun(taskSettings, true);
        } else if (taskOneDetails.getTaskStatus() == TASK_SUCCESS) {

            imgMakeCustCall.setVisibility(GONE);
            imgShowCustLocation.setVisibility(GONE);
            success_view.setVisibility(GONE);

            tv_add_notes.setText(getString(R.string.hint_view_note));
            addSign.setText(getString(R.string.hint_view_signature));
            addImages.setText(getString(R.string.hint_images));
            tv_bar_code.setText("");

            if (taskSettings.getActionBlock().getImage().getExist()) {
                layoutAddImages.setVisibility(VISIBLE);
            } else {
                layoutAddImages.setVisibility(GONE);
            }
            if (taskSettings.getActionBlock().getNotes().getExist()) {
               // layoutAddNewNote.setVisibility(VISIBLE);
            } else {
             //   layoutAddNewNote.setVisibility(GONE);
            }

//            if (taskSettings.getActionBlock().getSignature().getExist()) {
//                layoutAddSign.setVisibility(GONE);
//            } else {
//                layoutAddSign.setVisibility(GONE);
//            }



            if (taskSettings.getActionBlock().getBarcode().getExist()) {
                layout_add_barcode.setVisibility(VISIBLE);
            } else {
                layout_add_barcode.setVisibility(GONE);
            }
            if (loginPrefManager.isActualCustomer()) {
                layout_actual_customer.setVisibility(VISIBLE);
            } else {
                layout_actual_customer.setVisibility(GONE);
            }

            layout_add_barcode.setClickable(false);

            file_path = new ArrayList<>();

            file_path = taskOneDetails.getDriverImages();
            addImagesAdpater = new AddImagesAdpater(TaskDetailsActivity.this, "6", file_path, position -> {

            });

            images_view.setAdapter(addImagesAdpater);

            if (file_path.size() > 0)
                images_view.setVisibility(VISIBLE);

            if (file_path.size() > 0) {
                image_count.setVisibility(VISIBLE);
                image_count.setText("" + file_path.size());
            }
            if (file_path.size() <= 0) {
                image_count.setVisibility(GONE);
                image_count.setText("" + file_path.size());
            }
            if (!signature_added.equals("")) {
                signature_enable.setVisibility(VISIBLE);
            }



            if (signature_need) {
                signature_enable.setVisibility(VISIBLE);
            }
            if (isActualCustomerAdded) {
                actual_customer_added.setVisibility(VISIBLE);
            }
        } else if (taskOneDetails.getTaskStatus() == TASK_FAILED) {
            tv_add_notes.setText(getString(R.string.hint_view_note));
            addSign.setText(getString(R.string.hint_view_signature));

            if (!signature_added.equals("")) {
                signature_enable.setVisibility(VISIBLE);
            }
            if (isActualCustomerAdded) {
                actual_customer_added.setVisibility(VISIBLE);
            }



        } else if (taskOneDetails.getTaskStatus() == TASK_DECLINED || taskOneDetails.getTaskStatus() == TASK_STATUS_CANCELLED) {

            start_view.setVisibility(GONE);
            accept_view.setVisibility(GONE);
            acknowledge_view.setVisibility(GONE);
            success_view.setVisibility(GONE);

            if (!signature_added.equals("")) {
                signature_enable.setVisibility(VISIBLE);
            }
            if (isActualCustomerAdded) {
                actual_customer_added.setVisibility(VISIBLE);
            }
        }


    }

    private void setupSuccessViewFun(SettingsModel taskSettings, boolean b) {
        ActionBlock actionBlock = taskSettings.getActionBlock();

        images_need = actionBlock.getImage().getIsMandatory();
        note_need = actionBlock.getNotes().getIsMandatory();
        signature_need = actionBlock.getSignature().getIsMandatory();
        Bar_code_need = actionBlock.getBarcode().getIsMandatory();

        imgMakeCustCall.setVisibility(loginPrefManager.getBooleanValue("driver_call_option_need") ? VISIBLE : INVISIBLE);

        imgShowCustLocation.setVisibility(VISIBLE);
//
//        if (b) {
//            if (!isMyServiceRunning(IdleUpdatedService.class)) {
//                startService(new Intent(TaskDetailsActivity.this, IdleUpdatedService.class));
//            }
//        }

        file_path = new ArrayList<>();

        file_path = taskOneDetails.getDriverImages();

        imageCount = imageCount - file_path.size();
        addImagesAdpater = new AddImagesAdpater(TaskDetailsActivity.this, "1", file_path, position -> deleteImageOnCall(file_path.get(position)));
        images_view.setAdapter(addImagesAdpater);

        if (file_path.size() > 0)
            images_view.setVisibility(VISIBLE);

        if (taskSettings.getActionBlock().getImage().getExist()) {
            layoutAddImages.setVisibility(VISIBLE);
        }
        if (taskSettings.getActionBlock().getNotes().getExist()) {
          //  layoutAddNewNote.setVisibility(VISIBLE);
        }
//        if (taskSettings.getActionBlock().getSignature().getExist()) {
//            layoutAddSign.setVisibility(VISIBLE);
//        }
        if (taskSettings.getActionBlock().getBarcode().getExist()) {
            layout_add_barcode.setVisibility(VISIBLE);
        }
        if (isActualCustomerAdded) {
            actual_customer_added.setVisibility(VISIBLE);
        }
        boolean isSignExist = taskSettings.getActionBlock().getSignature().getExist();
        if (isSignExist) {
            if (signature_added == null || signature_added.equals("") || signature_added.isEmpty()) {
                layoutAddSign.setVisibility(VISIBLE);
            } else {
                layoutAddOtherDetails.setVisibility(VISIBLE);
                addSign.setText(R.string.task_details_edit_sign);
                signature_enable.setVisibility(VISIBLE);
                layoutAddSign.setVisibility(VISIBLE);
            }

            if (file_path.size() > 0) {
                image_count.setVisibility(VISIBLE);
                image_count.setText("" + file_path.size());
            }

            if (file_path.size() <= 0) {
                image_count.setVisibility(GONE);
                image_count.setText("" + file_path.size());
            }
        }
    }

    private void showCancleDialog(int i) {
        Log.e("cancelled_state", "" + i);
        cancelDialog = new CancelDialog(TaskDetailsActivity.this, R.style.MyDialogStyle, (status, msg, reasonId) -> {

            if (status) {
                cancelDialog.dismiss();
                cancelId = reasonId;

                // check glympse is enabled or not
                if (loginPrefManager.getGlympseEnabled() && glympseTaskID != null) {
                    if (i == DeliforceConstants.TASK_FAILED) {
                        failedGlympseTask(i, msg);
                    } else if (i == DeliforceConstants.TASK_STATUS_CANCELLED) {
                        cancelGlympseTask(i, msg);
                    } else {
                        upDateTask(i, msg);
                    }
                } else {
                    upDateTask(i, msg);
                }

            } else {
                cancelDialog.dismiss();
            }
        }, i);

        cancelDialog.setCanceledOnTouchOutside(true);
        cancelDialog.show();
    }

    public void updateOTPTask(int position, String reason, int otpCode, boolean otpRequired) {
        prepareInputParmsForSuccess(position, reason, otpCode, otpRequired);
        if (loginPrefManager.isOTPVerificationEnabled()) {
            apiService.updatetaskWithOTPDetails(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken(), updateTaskStatus).enqueue(new Callback<OTPTaskUpdate>() {
                @Override
                public void onResponse(@NotNull Call<OTPTaskUpdate> call, @NotNull Response<OTPTaskUpdate> response) {
                    fetchTaskSuccessResponse(response);
                }

                @Override
                public void onFailure(Call<OTPTaskUpdate> call, Throwable t) {
                    if (!isFinishing()) dismiss_loader();
                    show_error_response(getString(R.string.onfailure_api));
                }
            });
        } else {
            Log.e("api_received","test");
            String distance1 =  loginPrefManager.getStringValue("manual_distance");
            Log.e("final_distance2233",distance1+"112233");
            updateTaskStatus.setTotalTravelledDistance(distance1);
           // updateTaskStatus.setTotalTravelledDistance("8");
            apiService.updatetaskDetails(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken(), updateTaskStatus).enqueue(new Callback<OTPTaskUpdate>() {
                @Override
                public void onResponse(@NotNull Call<OTPTaskUpdate> call, @NotNull Response<OTPTaskUpdate> response) {
                    try {
                        loginPrefManager.setStringValue("manual_distance", null);
                        fetchTaskSuccessResponse(response);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<OTPTaskUpdate> call, Throwable t) {
                    if (!isFinishing()) dismiss_loader();
                    show_error_response(getString(R.string.onfailure_api));
                }
            });
        }
    }

    private void prepareInputParmsForSuccess(int position, String reason, int otpCode, boolean otpRequired) {
        if (!isFinishing())
            show_loader();
        if (position == DeliforceConstants.TASK_SUCCESS) {
            driver_longitude = loginPrefManager.getDoubleLongitude();
            driver_latitude = loginPrefManager.getDoubleLatitude();
            if (isCaptureLocation && loginPrefManager.isCaptureLocationEnabled()) {
                updateTaskStatus.setCaptureLocation(taskOneDetails.getCaptureLocation());
            }
        } else {
            driver_longitude = user_lng;
            driver_latitude = user_lat;
        }

        double driver_acc = 0;
        if (driver_latitude == 0.0 && current_latitude != 0.0) {
            driver_latitude = current_latitude;
            driver_longitude = current_longitude;
            driver_acc = current_accuraccy;
        } else if (driver_latitude == 0.0 && loginPrefManager.getDoubleLatitude() != 0.0) {
            driver_latitude = loginPrefManager.getDoubleLatitude();
            driver_longitude = loginPrefManager.getDoubleLongitude();
            driver_acc = loginPrefManager.getAccuracy();
        } else {
            if (AppUtils.getDeviceLocation() != null) {
                driver_latitude = AppUtils.getDeviceLocation().getLatitude();
                driver_longitude = AppUtils.getDeviceLocation().getLongitude();
                driver_acc = AppUtils.getDeviceLocation().getAccuracy();
            }
        }

        if (otpRequired)
            updateTaskStatus.setRetryOTPCode(true);
        else
            updateTaskStatus.setRetryOTPCode(false);

        updateTaskStatus.setReferenceId(taskOneDetails.getReferenceId());

        if (taskOneDetails.getBusinessType() == DeliforceConstants.BUSSINESS_TYPE_PICKUP_DROP) {
            updateTaskStatus.setPickup(taskOneDetails.getIsPickup());
        }

        updateTaskStatus.setOTP(otpCode);
        updateTaskStatus.setTaskId(loginPrefManager.getStringValue("taskId"));

        updateTaskStatus.setDeviceType(0);

        updateTaskStatus.setImgUrl(loginPrefManager.getStringValue("image_url"));
        updateTaskStatus.setDriver_name(loginPrefManager.getStringValue("first_name"));
        int batteryPercent = AppUtils.getBatteryPercentage(this);
        updateTaskStatus.setBatteryStatus(batteryPercent);

        String formattedAddress = getCompleteAddressString(TaskDetailsActivity.this, driver_latitude, driver_longitude);

        updateTaskStatus.setAddress(formattedAddress);
        updateTaskStatus.setStatus(position);
        updateTaskStatus.setCancelTaskReason("");
        updateTaskStatus.setDriverStatus(Integer.parseInt(loginPrefManager.getStringValue("driver_status")));

//        Timber.e("deliforce_STARTED%s", "----" + DeliforceConstants.TASK_STARTED);

        updateTaskStatus.setStartLat(driver_latitude);
        updateTaskStatus.setStartLng(driver_longitude);

        if (loginPrefManager.getGlympseEnabled() && glympseTaskID != null) {
            updateTaskStatus.setGlympseTrackingURL(glympTask.getOperation().getInviteUrl());
        }

        if (position == TASK_STARTED) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            updateTaskStatus.setStartedTime(sdf.format(new Date()));
            createALiveLog(driver_latitude, driver_longitude, driver_acc);
        } else if (position == DeliforceConstants.TASK_SUCCESS) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            updateTaskStatus.setTaskSuccessCompletionTime(sdf.format(new Date()));
            LocalBroadcastManager.getInstance(TaskDetailsActivity.this).sendBroadcast(new Intent("updated").putExtra("task_complete", "1"));
        } else if (position == DeliforceConstants.TASK_STATUS_CANCELLED || position == DeliforceConstants.TASK_FAILED
                || position == TASK_DECLINED) {
            updateTaskStatus.setCancelTaskReason(reason);
        } else if (position == TASK_TEMPLATE_STATUS) {
            updateTaskStatus.setFieldName(templateFieldName);
            updateTaskStatus.setFieldValues(templateFieldValue);
            updateTaskStatus.setOrder(Integer.parseInt(orderValue));
            if (selectedArray.size() > 0) {
                selectedObj.add("selectedValues", selectedArray);
                updateTaskStatus.setFieldSelectedValues(selectedArray);
                selectedArray = new JsonArray();
            } else
                updateTaskStatus.setFieldSelectedValues(null);

            updateTaskStatus.setDataType(reason);
        }

        if (notificationManager != null) {
            notificationManager.cancelAll();
        }

        updateTaskStatus.setTopic("task");
        updateTaskStatus.setManager_id(loginPrefManager.getManagerID());

        Set manager = loginPrefManager.getAdminList();
        List<String> mainList = new ArrayList<>(manager);
        updateTaskStatus.setAdminArray(mainList);
    }

    private void fetchTaskSuccessResponse(Response<OTPTaskUpdate> response) {
        try {
            if (response.raw().code() == 200) {
                assert response.body() != null;
                //invalid otp is 101 and resend is 102-- show otp dialog
                if (response.body().getStatuscode() == 102 || response.body().getStatuscode() == 101) {
                    if (!isFinishing()) {
                        dismiss_loader();
                    }
                    showOTPAlert(response.body().getOtp());
                } else if (loginPrefManager.isOTPVerificationEnabled() && response.body().getStatuscode() == 104)// for otp verification for first time
                {
                    if (!isFinishing()) {
                        dismiss_loader();
                    }
                    showOTPAlert(response.body().getOtp());
                } else if (loginPrefManager.getEarningEnabled() && !response.body().getAmount().isEmpty() && response.body().getStatuscode() == 103)// for showing earnings module
                {
                    if (!isFinishing()) dismiss_loader();
                    openEarningsFarePage(new Gson().toJson(response.body()));
                } else {
//                    if (!isMyServiceRunning(IdleUpdatedService.class)) {
//                        Intent start = new Intent(TaskDetailsActivity.this, IdleUpdatedService.class);
//                        start.putExtra("Trip_Completed", true);
//                        startService(start);
//                    }

                    loginPrefManager.setTotalLiveDistance(0.0f);
                    AppUtils.clearLiveLatLngData();
                    if (!isFinishing()) dismiss_loader();
                    removeLocation();
                }
            } else if (response.raw().code() == 401) {
                if (!isFinishing()) dismiss_loader();
                findCurrent();
            }//alert dialog for user already logged in
            else if (response.code() == 494) {
                if (!isFinishing()) dismiss_loader();
                showAlertDialog(TaskDetailsActivity.this);
            } else if (response.code() == 302) {
                Toast.makeText(getApplicationContext(), new JSONObject(response.errorBody().string()).getString("message"), Toast.LENGTH_LONG).show();
            } else if (response.code() == 449) {
                finish();
            } else {
                if (!isFinishing()) dismiss_loader();
                show_error_response(getString(R.string.error_response));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openEarningsFarePage(String jsonData) {
        multiMap.remove(loginPrefManager.getStringValue("taskId"));
        Gson gson = new Gson();
        loginPrefManager.setStringValue("start_locations", "");
        String hashMapString = gson.toJson(multiMap);

        loginPrefManager.setStringValue("start_locations", hashMapString);

//        if (!isMyServiceRunning(IdleUpdatedService.class)) {
//            Intent start = new Intent(TaskDetailsActivity.this, IdleUpdatedService.class);
//            start.putExtra("Trip_Completed", true);
//            startForegroundService(start);
//        }

        Intent fareIntent = new Intent(TaskDetailsActivity.this, TripFareDetailsActivity.class);
        fareIntent.putExtra("jsonData", jsonData);
        startActivity(fareIntent);
        finish();
    }

    public void upDateTask(int position, String reason) {
        prepareInputParmsToUpdateTask(position, reason);
        apiCallForTaskUpdatePost(position);
    }

    private void prepareInputParmsToUpdateTask(int position, String reason) {
        if (showLoader) {
            if (!isFinishing()) show_loader();
        } else {
            showLoader = true;
        }
        if (position == DeliforceConstants.TASK_SUCCESS) {
            driver_longitude = loginPrefManager.getDoubleLongitude();
            driver_latitude = loginPrefManager.getDoubleLatitude();
        } else {
            driver_longitude = user_lng;
            driver_latitude = user_lat;
        }

        double driver_acc = 0;
        if (driver_latitude == 0.0 && current_latitude != 0.0) {
            driver_latitude = current_latitude;
            driver_longitude = current_longitude;
            driver_acc = current_accuraccy;
        } else if (driver_latitude == 0.0 && loginPrefManager.getDoubleLatitude() != 0.0) {
            driver_latitude = loginPrefManager.getDoubleLatitude();
            driver_longitude = loginPrefManager.getDoubleLongitude();
            driver_acc = loginPrefManager.getAccuracy();
        } else {
            if (AppUtils.getDeviceLocation() != null) {
                driver_latitude = AppUtils.getDeviceLocation().getLatitude();
                driver_longitude = AppUtils.getDeviceLocation().getLongitude();
                driver_acc = AppUtils.getDeviceLocation().getAccuracy();
            }
        }

        updateTaskStatus.setTaskId(loginPrefManager.getStringValue("taskId"));
        updateTaskStatus.setDeviceType(0);
        updateTaskStatus.setRetryOTPCode(false);
        updateTaskStatus.setReferenceId(taskOneDetails.getReferenceId());
        if (taskOneDetails.getBusinessType() == DeliforceConstants.BUSSINESS_TYPE_PICKUP_DROP) {
            updateTaskStatus.setPickup(taskOneDetails.getIsPickup());
        }
        updateTaskStatus.setImgUrl(loginPrefManager.getStringValue("image_url"));
        updateTaskStatus.setDriver_name(loginPrefManager.getStringValue("first_name"));
        int batteryPercent = AppUtils.getBatteryPercentage(this);
        updateTaskStatus.setBatteryStatus(batteryPercent);

        String formattedAddress = getCompleteAddressString(TaskDetailsActivity.this, driver_latitude, driver_longitude);

        updateTaskStatus.setAddress(formattedAddress);
        updateTaskStatus.setStatus(position);
        updateTaskStatus.setCancelTaskReason("");
        updateTaskStatus.setDriverStatus(Integer.parseInt(loginPrefManager.getStringValue("driver_status")));


        updateTaskStatus.setStartLat(driver_latitude);
        updateTaskStatus.setStartLng(driver_longitude);

        if (position == TASK_STARTED) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            updateTaskStatus.setStartedTime(sdf.format(new Date()));

            //if glympse is enabled send the glymyse invite url
            if (loginPrefManager.getGlympseEnabled() && glympseTaskID != null) {
                updateTaskStatus.setGlympseTrackingURL(glympTask.getOperation().getInviteUrl());
            }

            createALiveLog(driver_latitude, driver_longitude, driver_acc);

        } else if (position == DeliforceConstants.TASK_SUCCESS) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            updateTaskStatus.setTaskSuccessCompletionTime(sdf.format(new Date()));
            LocalBroadcastManager.getInstance(TaskDetailsActivity.this).sendBroadcast(new Intent("updated").putExtra("task_complete", "1"));
        } else if (position == DeliforceConstants.TASK_STATUS_CANCELLED || position == DeliforceConstants.TASK_FAILED
                || position == TASK_DECLINED || position == DeliforceConstants.TASK_STATUS_CUSTOMER_NOT_AVAILABLE) {
            updateTaskStatus.setCancelTaskReason(reason);
            updateTaskStatus.setReason_id(cancelId);
        } else if (position == TASK_TEMPLATE_STATUS) {
            if (!reason.equals(DROP_DOWN)) {
                templateFieldId = "";
            }
            updateTaskStatus.setId(templateFieldId);
            updateTaskStatus.setFieldName(templateFieldName);
            updateTaskStatus.setFieldValues(templateFieldValue);
            updateTaskStatus.setOrder(Integer.parseInt(orderValue));
            if (selectedArray.size() > 0) {
                selectedObj.add("selectedValues", selectedArray);
                updateTaskStatus.setFieldSelectedValues(selectedArray);
                selectedArray = new JsonArray();
            } else
                updateTaskStatus.setFieldSelectedValues(null);

            updateTaskStatus.setDataType(reason);
        } else if (position == DeliforceConstants.TASK_ADD_TEMPLATE_STATUS) {
            updateTaskStatus.setTemplateName(taskOneDetails.getTemplateName());
        } else if (position == DeliforceConstants.TASK_ARRIVED) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            updateTaskStatus.setArrivedTime(sdf.format(new Date()));
        }

        if (notificationManager != null) {
            notificationManager.cancelAll();
        }

        updateTaskStatus.setTopic("task");
        updateTaskStatus.setManager_id(loginPrefManager.getManagerID());


        Set manager = loginPrefManager.getAdminList();
        List<String> mainList = new ArrayList<>(manager);
        updateTaskStatus.setAdminArray(mainList);
    }

    private void apiCallForTaskUpdatePost(int position) {
        try {
         //   updateTaskStatus.setTotalTravelledDistance("2.5");

            Log.e("taskUpdateRequest",new Gson().toJson(updateTaskStatus));


            Log.e("collected_livelatlong",loginPrefManager.getStringValue("live_latlong"));

            String liveLatlng= loginPrefManager.getStringValue("live_latlong");

            String[] locations = liveLatlng.split(":");


            double finalDistance = 0.0;

           /* for(int i =0 ; i<locations.length; i++){
                String [] locations1 = locations[i].split(",");
                String[] locations2 =null;
                if (i+1>=locations.length-1) {
                    locations2 = locations[i+1].split(",");
                    double distance =  distance(Double.parseDouble(locations1[0]),Double.parseDouble(locations1[1]),Double.parseDouble(locations2[0]),Double.parseDouble(locations2[1]));
                    finalDistance=distance+finalDistance;
                }
            }*/

           // String distance1 =  loginPrefManager.getStringValue("live_lastdistance");

           // Log.e("calculat_finalDistance",String.valueOf(distance1));




            //updateTaskStatus.setTotalTravelledDistance("10");
            apiService.updatetaskDetails(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken(), updateTaskStatus).enqueue(new Callback<OTPTaskUpdate>() {
                @Override
                public void onResponse(@NotNull Call<OTPTaskUpdate> call, @NotNull Response<OTPTaskUpdate> response) {
                    if (!isFinishing()) dismiss_loader();
                    loginPrefManager.setStringValue("live_latlong",null);
                 //   fetchTaskUpdateDetails(position, response);
                }
                @Override
                public void onFailure(Call<OTPTaskUpdate> call, Throwable t) {
                    if (!isFinishing()) dismiss_loader();
                    show_error_response(getString(R.string.onfailure_api));
                }
            });
        } catch (Exception e) {
            if (!isFinishing()) dismiss_loader();
            Timber.e("Exception%s", "---" + e.getMessage());
        }

    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

//    private void fetchTaskUpdateDetails(int position, Response<OTPTaskUpdate> response) {
//        try {
//            if (response.raw().code() == 200) {
//                if (position == TASK_SUCCESS
//                        || position == TASK_FAILED ||
//                        position == TASK_STATUS_CANCELLED
//                        || position == TASK_STATUS_CUSTOMER_NOT_AVAILABLE) {
//
//                    removeLocation();
//                    loginPrefManager.setTotalLiveDistance(0.0f);
//                    AppUtils.clearLiveLatLngData();
//
//                    if (!isMyServiceRunning(IdleUpdatedService.class)) {
//                        Intent start = new Intent(TaskDetailsActivity.this, IdleUpdatedService.class);
//                        start.putExtra("Trip_Completed", true);
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                            startForegroundService(start);
//                        } else {
//                            startService(start);
//                        }
//                    }
//                } else if (position == TASK_DECLINED) {
//                    if (activity_name != null && activity_name.equalsIgnoreCase("navigation")) {
//                        startActivity(new Intent(TaskDetailsActivity.this, NavigationActivity.class));
//                        finish();
//                    } else if (activity_name != null && activity_name.equalsIgnoreCase("route")) {
//                        startActivity(new Intent(TaskDetailsActivity.this, RouteActivity.class));
//                        finish();
//                    } else if (activity_name != null && activity_name.equalsIgnoreCase("map")) {
//                        startActivity(new Intent(TaskDetailsActivity.this, MapTaskActivity.class));
//                        finish();
//                    }
//                } else if (position == TASK_STATUS_ACKNOWLEDGE || position == TASK_STARTED) {
//                    if (isMyServiceRunning(PushNotificationService.class)) {
//                        stopService(new Intent(TaskDetailsActivity.this, PushNotificationService.class));
//                    } else {
//                        getTaskDetails();
//                    }
//                } else if (position == TASK_TEMPLATE_STATUS || position == DeliforceConstants.TASK_ADD_TEMPLATE_STATUS) {
//                    Toast.makeText(TaskDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                    getTaskDetails();
//                } else {
//                    getTaskDetails();
//                }
//            } else if (response.raw().code() == 401) {
//                findCurrent();
//            }//alert dialog for user already logged in
//            else if (response.raw().code() == 494) {
//                showAlertDialog(TaskDetailsActivity.this);
//            } else if (response.raw().code() == 449) {
//                finish();
//            } else if (response.raw().code() == 302) {
//                Toast.makeText(getApplicationContext(), getString(R.string.err_destination), Toast.LENGTH_LONG).show();
//            } else {
//                show_error_response(getString(R.string.error_response));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void createALiveLog(double driver_latitude, double driver_longitude, double driver_acc) {
        try {
            JSONObject jobj = new JSONObject();
            JSONArray liveLocationArray = new JSONArray();
            jobj.put("live_latitude", driver_latitude);
            jobj.put("live_longitude", driver_longitude);
            jobj.put("live_accuracy", driver_acc);
            jobj.put("live_speed", 0);
            jobj.put("live_date_time", simpleDateFormat.format(new Date()));
            jobj.put("live_distance_travelled", 0);

            String live_location_array = locationPrefs.getString("live_location_array", null);

            if (live_location_array != null)
                liveLocationArray = new JSONArray(live_location_array);

            liveLocationArray.put(jobj);

            System.out.println("Job started log: " + liveLocationArray.toString());
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("live_location_array", liveLocationArray.toString());
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

        if (activity_name != null && activity_name.equalsIgnoreCase("navigation")) {
            finish();
        } else if (activity_name != null && activity_name.equalsIgnoreCase("route")) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        } else if (activity_name != null && activity_name.equalsIgnoreCase("map")) {
            finish();
        }
    }

    public void setTaskCategory(int bussType) {
        boolean isPickUp;
        switch (bussType) {
            case 1:
                isPickUp = taskOneDetails.getIsPickup();
                if (isPickUp) {
                    taskName.setText(getString(R.string.task_type_pick_up));
                } else {
                    taskName.setText(getString(R.string.task_type_delivery));
                }
                break;
            case 2:
                taskName.setText(getString(R.string.task_type_appointments));
                break;
            case 3:
                taskName.setText(getString(R.string.task_type_field_work));
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
                String templatePath = "";
                if (imageType.equalsIgnoreCase(TEMPLATE_IMAGE)) {
                    templatePath = returnValue.get(0);
                    compressAndUploadFile(new File(templatePath));
                } else if (imageType.equalsIgnoreCase(TASK_IMAGE)) {
                    selected_images = new ArrayList<>();
                    selected_images.addAll(returnValue);

                    imageCount = returnValue.size();
                    compressAndUploadFile(new File(selected_images.get(0)));
                }
            }
        } else if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            if (resultCode == Activity.RESULT_OK) {
                System.out.println(resultCode);
            }
        } else if (requestCode == SIGNATURE_REQUEST_CODE && resultCode == RESULT_OK) {
            getTaskDetails();
        } else if (requestCode == CancelDialog.CANCEL_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            cancelled_images = new ArrayList<>();
            cancelled_images.addAll(returnValue);
            File file = new File(cancelled_images.get(cancelled_images.size() - 1));
            // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
            long fileSizeInKB = file.length() / 1024;
            if (cancelled_images.size() > 0) {
                if (fileSizeInKB > 1024 && loginPrefManager.getPODImageCompress()) {
                    //show_error_response(getString(R.string.error_img_size));
                    File compressedImageFile;
                    try {
                        compressedImageFile = new Compressor(this).setQuality(75).compressToFile(new File(cancelled_images.get(cancelled_images.size() - 1)));
                        selected_images.set(cancelled_images.size() - 1, compressedImageFile.getPath());
                        updateDriverImages(CANCEL_IMAGE, "");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    updateDriverImages(CANCEL_IMAGE, "");
                }
            }
        }
    }

    private void compressAndUploadFile(File file) {
        // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
        long fileSizeInKB = file.length() / 1024;
        if (imageType.equalsIgnoreCase(TASK_IMAGE)) {
            if (selected_images.size() > 0) {
                if (fileSizeInKB > 1024 && loginPrefManager.getPODImageCompress()) {
                    //show_error_response(getString(R.string.error_img_size));
                    File compressedImageFile;
                    try {
                        compressedImageFile = new Compressor(this).setQuality(75).compressToFile(new File(selected_images.get(0)));
                        selected_images.set(0, compressedImageFile.getPath());
                        updateDriverImages(imageType, "");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    updateDriverImages(imageType, "");
                }
            }
        } else if (imageType.equalsIgnoreCase(TEMPLATE_IMAGE)) {
            if (fileSizeInKB > 1024 && loginPrefManager.getPODImageCompress()) {
                File compressedImageFile;
                try {
                    compressedImageFile = new Compressor(this).setQuality(75).compressToFile(new File(selected_images.get(0)));
                    updateDriverImages(imageType, compressedImageFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                updateDriverImages(imageType, file.getPath());
            }
        }

    }

    private MultipartBody.Part prepareFilePart(String imagePth) {
        File file = new File(imagePth);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        return MultipartBody.Part.createFormData("images", file.getName(), requestFile);
    }

    public void setInitialTaskStatus(int taskStatus) {
        tvTaskDetailsStatus.setText(AppUtils.getStatus(TaskDetailsActivity.this, taskStatus));
        if (taskStatus == TASK_SUCCESS) {
            tvTaskDetailsStatus.setText(getString(R.string.task_status_success));
            layoutAddNewNote.setClickable(true);
            layoutAddImages.setClickable(false);
            layoutAddSign.setClickable(true);
            imgMakeCustCall.setVisibility(INVISIBLE);
            imgShowCustLocation.setVisibility(INVISIBLE);
        }
    }

    private void updateDriverImages(String uploadType, String templatePath) {

        if (!isFinishing())
            taskUpdateLoader();
        RequestBody lang = RequestBody.create(MediaType.parse("multipart/form-data"), task_id);
        RequestBody type = RequestBody.create(MediaType.parse("multipart/form-data"), uploadType);

        List<MultipartBody.Part> parts = new ArrayList<>();

        if (uploadType.equalsIgnoreCase(CANCEL_IMAGE)) {
            for (int i = 0; i < cancelled_images.size(); i++) {
                parts.add(prepareFilePart(cancelled_images.get(i)));
            }
        } else if (uploadType.equalsIgnoreCase(TEMPLATE_IMAGE)) {
            parts.add(prepareFilePart(templatePath));
        } else {
            for (int i = 0; i < selected_images.size(); i++) {
                parts.add(prepareFilePart(selected_images.get(i)));
            }
        }
        try {
            apiService1 = APIServiceFactory.getRetrofit().create(ApiService.class);
            apiService1.uploadtask_images(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken(), lang, type, parts).enqueue(new Callback<DriverImage>() {
                @Override
                public void onResponse(@NotNull Call<DriverImage> call, @NotNull Response<DriverImage> response) {
                    if (!isFinishing())
                        taskUpdateDismissLoader();

                    try {
                        if (response.raw().code() == 200) {
                            if (uploadType.equalsIgnoreCase(CANCEL_IMAGE)) {
                                cancelled_images_path = new ArrayList<>();
                                cancelled_images_path = response.body().getDriverImages();
                                cancelDialog.prepareImages(cancelled_images_path);
                            } else if (uploadType.equalsIgnoreCase(TEMPLATE_IMAGE)) {
                                templateFieldValue = response.body().getTemplateUrl();
                                upDateTask(TASK_TEMPLATE_STATUS, IMAGE);
                            } else {
                                file_path = new ArrayList<>();

                                file_path = response.body().getDriverImages();
                                addImagesAdpater = new AddImagesAdpater(TaskDetailsActivity.this, "1", file_path, position -> deleteImageOnCall(file_path.get(position)));

                                if (file_path.size() > 0) {

                                    image_count.setText("" + file_path.size());
                                    image_count.setVisibility(VISIBLE);
                                } else {
                                    image_count.setVisibility(GONE);
                                }
                                images_view.setAdapter(addImagesAdpater);
                            }
                        } else if (response.raw().code() == 401) {
                            findCurrent();
                        }
                        //alert dialog for user already logged in
                        else if (response.code() == 494) {
                            showAlertDialog(TaskDetailsActivity.this);
                        } else {

                            show_error_response(getString(R.string.error_response));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DriverImage> call, @NotNull Throwable t) {

                    if (!isFinishing()) dismiss_loader();
                    show_error_response(getString(R.string.onfailure_api));


                }
            });


        } catch (Exception e) {
            if (!isFinishing()) dismiss_loader();
            e.printStackTrace();
        }
    }

    private void deleteImageOnCall(String url) {

        try {

            DriverImages driverImages = new DriverImages();

            driverImages.setSuperTaskId(task_id);
            driverImages.setImageUrl(url);


            if (!isFinishing()) show_loader();


            apiService.deleteImage(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken(), driverImages).enqueue(new Callback<DeleteImage>() {
                @Override
                public void onResponse(@NotNull Call<DeleteImage> call, @NotNull Response<DeleteImage> response) {

                    if (!isFinishing()) dismiss_loader();

                    try {
                        if (response.raw().code() == 200 || response.body().getStatus() == 200) {

                            getTaskDetails();
                        } else if (response.raw().code() == 401) {
                            findCurrent();
                        }
                        //alert dialog for user already logged in
                        else if (response.code() == 494) {
                            showAlertDialog(TaskDetailsActivity.this);
                        } else {
                            show_error_response(getString(R.string.error_response));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(@NotNull Call<DeleteImage> call, @NotNull Throwable t) {
                    if (!isFinishing()) dismiss_loader();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void AskCallPermission() {
        permissionHelper.check(Manifest.permission.CALL_PHONE)
                .withDialogBeforeRun(R.string.dialog_before_run_title, R.string.dialog_before_run_message, R.string.dialog_positive_button)
                .setDialogPositiveButtonColor(R.color.colorPrimary)
                .onSuccess(this::onSuccess)
                .onDenied(this::onDenied)
                .onNeverAskAgain(this::onNeverAskAgain)
                .run();
    }

    public void onSuccess() {

        call();

    }

    private void call() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phone_no));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        startActivity(callIntent);
    }

    private void onNeverAskAgain() {
        Snackbar snackbar = Snackbar.make(track_layout, "Please enable permission", Snackbar.LENGTH_LONG).
                setAction("Settings", view -> permissionHelper.startApplicationSettingsActivity());
        snackbar.setActionTextColor(ContextCompat.getColor(TaskDetailsActivity.this, R.color.colorPrimary));
        snackbar.show();

    }

    private void onDenied() {

    }

    private void checkOverLayPermissions() {
        //startNavigation();
        if (Build.VERSION.SDK_INT >= 23) {
            if (!android.provider.Settings.canDrawOverlays(TaskDetailsActivity.this)) {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
            } else {
                startNavigation();
            }
        } else {
            try {
                startNavigation();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void startNavigation() {
        Address details = taskOneDetails.getAddress();

        double latitude = details.getGeometry().getLocation().getLat();
        double longitude = details.getGeometry().getLocation().getLng();

        loginPrefManager.setStringValue("task_id", task_id);
        loginPrefManager.setStringValue("GlympseID", glympseTaskID);
        startService(new Intent(TaskDetailsActivity.this, HUD.class));
        if (loginPrefManager.getNavigation().equalsIgnoreCase(DeliforceConstants.NavigationGoogleMap)) {
            try {
                if (isPackageInstalled(getPackageManager())) {
                    Intent intent1 = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + latitude + "," + longitude));
                    intent1.setPackage(GOOGLE_MAP_PACKAGE);
                    startActivity(intent1);
                    finish();
                } else {
                    Uri.Builder directionsBuilder = new Uri.Builder()
                            .scheme("https")
                            .authority("www.google.com")
                            .appendPath("maps")
                            .appendPath("dir")
                            .appendPath("")
                            .appendQueryParameter("api", "1")
                            .appendQueryParameter("destination", latitude + "," + longitude);

                    startActivity(new Intent(Intent.ACTION_VIEW, directionsBuilder.build()));
                }
            } catch (Exception e) {
                e.printStackTrace();
                String uri = "https://maps.google.com/maps?daddr=" + latitude + "," + longitude;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        } else if (loginPrefManager.getNavigation().equalsIgnoreCase(DeliforceConstants.NavigationWaze)) {
            try {
                String uri = "waze://?ll=" + latitude + "," + longitude + "&navigate=yes";
                startActivity(new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse(uri)));
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    String uri = "https://play.google.com/store/apps/details?id=com.waze";
                    startActivity(new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse(uri)));
                } catch (Exception e1) {
                    e1.printStackTrace();
                    showShortToastMessage(getString(R.string.something_went_wrong));
                }
            }
        }
    }

    private boolean isPackageInstalled(PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(GOOGLE_MAP_PACKAGE, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void showOTPAlert(String otpCODE) {
        alertDialogOTP = new AlertDialogOTP(TaskDetailsActivity.this, R.style.MyDialogStyle, otpCODE, new AlertDialogOTP.BlockedInterface() {
            @Override
            public void onclickOTP(boolean status, int otpCode) {
                if (status) {
                    submitOTP(otpCode);
                } else
                    alertDialogOTP.dismiss();
            }

            @Override
            public void resendOTP() {
                updateOTPTask(DeliforceConstants.TASK_SUCCESS, "", 0, true);
                alertDialogOTP.dismiss();
            }
        });
        alertDialogOTP.setCanceledOnTouchOutside(false);
        alertDialogOTP.show();
    }

    private void submitOTP(int otpCode) {
        try {
            updateOTPTask(DeliforceConstants.TASK_SUCCESS, "", otpCode, false);
            alertDialogOTP.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void show_Alert(int position) {
        alertDialog = new AlertDialog(TaskDetailsActivity.this, R.style.MyDialogStyle, position, new AlertDialog.BlockedInterface() {
            @Override
            public void onclick(boolean status) {

            }

            @Override
            public void reDirectToSpecifPage(String pageDeterminationValue) {
                if (!pageDeterminationValue.isEmpty()) {
                    switch (pageDeterminationValue) {
                        case DeliforceConstants.NotesPage:
                            goToNotesScreen();
                            break;
                        case DeliforceConstants.ImagesPage:
                          //  toAddImages();
                            break;
                        case DeliforceConstants.BarcodePage:
                         //   barCodeScreenCall("", false, order);
                            break;
                        case DeliforceConstants.SignaturePage:
                           // goToSignatureScreen();
                            break;
                        case DeliforceConstants.isActualCustomer + "":
                           // showActualCustomerDialog();
                            break;
                        case DeliforceConstants.isCaptureLocation + "":

                            break;
                        default:
                            break;
                    }

                }

            }
        });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private void complete_task() {
        int mandatoryFields = 0;
        StringBuilder mandatoryError = new StringBuilder();
        if (taskOneDetails.getTemplates() != null && taskOneDetails.getTemplates().size() > 0) {
            for (int tempCount = 0; tempCount < taskOneDetails.getTemplates().size(); tempCount++) {
                if (taskOneDetails.getTemplates().get(tempCount).getMandatoryFields().equals(MANDATORY)) {
                    if (taskOneDetails.getTemplates().get(tempCount).getDataType().equals(CHECK_LIST)) {
                        if (taskOneDetails.getTemplates().get(tempCount).getSelectedValues() == null ||
                                taskOneDetails.getTemplates().get(tempCount).getSelectedValues().equals("")) {
                            mandatoryError.append("   - ").append(taskOneDetails.getTemplates().get(tempCount).getFieldName()).append("\n");
                        } else
                            mandatoryFields += 1;
                    } else if (taskOneDetails.getTemplates().get(tempCount).getFieldValue().equals("") ||
                            taskOneDetails.getTemplates().get(tempCount).getFieldValue().equals("Select") ||
                            taskOneDetails.getTemplates().get(tempCount).getFieldValue().equals("false")) {
                        mandatoryError.append("   - ").append(taskOneDetails.getTemplates().get(tempCount).getFieldName()).append("\n");
                    } else
                        mandatoryFields += 1;
                }
            }
        } else
            mandatoryCount = 0;

        if (mandatoryFields == mandatoryCount) {
            getDistance(DeliforceConstants.TASK_SUCCESS);
        } else
            showAlertMsg("" + getString(R.string.field_mandatory), mandatoryError.toString());
    }

    private void showTwoButtonSnackbar() {

        // Create the Snackbar
        LinearLayout.LayoutParams objLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        snackbar = Snackbar.make(this.findViewById(android.R.id.content), getString(R.string.app_name), Snackbar.LENGTH_INDEFINITE);

        // Get the Snackbar layout view
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();

        // Set snackbar layout params
        int navbarHeight = getNavBarHeight(this);
        FrameLayout.LayoutParams parentParams = (FrameLayout.LayoutParams) layout.getLayoutParams();
        parentParams.setMargins(0, 0, 0, 0 - navbarHeight + 100);
        layout.setLayoutParams(parentParams);
        layout.setPadding(0, 0, 0, 0);
        layout.setLayoutParams(parentParams);

        // Inflate our custom view
        @SuppressLint("InflateParams") View snackView = getLayoutInflater().inflate(R.layout.custom_snack_bar, null);

        // Configure our custom view

        tv_duty_status = snackView.findViewById(R.id.tv_duty_status);
        status_switch = snackView.findViewById(R.id.sw_driver_duty_status);

        change_status();

        // Add our custom view to the Snackbar's layout
        layout.addView(snackView, objLayoutParams);

        // Show the Snackbar
        snackbar.show();
    }

    private void change_status() {
        status_switch.setOnCheckedChangeListener((compoundButton, status) -> {

            if (status) {
                tv_duty_status.setText(getString(R.string.nav_online));
                tv_duty_status.setTextColor(getResources().getColor(R.color.text_color));
               // updateDriverStatus(DeliforceConstants.DRIVER_STATUS_IDLE);


            }
        });
    }

    /*cancel task using glympse */
    private void cancelGlympseTask(int i, String msg) {
        cancelTaskMessage = msg;

        show_loader();
        int taskId = Integer.valueOf(glympseTaskID);
        if (EnRouteWrapper.instance().manager().getTaskManager()
                .findTaskById(taskId) == null) {
            refresh();
        }

        glympTask = EnRouteWrapper.instance().manager().getTaskManager()
                .findTaskById(taskId);

        EnRouteWrapper.instance().manager().getTaskManager().setTaskPhase(glympTask,
                EnRouteConstants.PHASE_PROPERTY_CANCELLED());
    }

    /*failed task using glympse */
    private void failedGlympseTask(int i, String msg) {
        cancelTaskMessage = msg;

        show_loader();
        int taskId = Integer.valueOf(glympseTaskID);
        if (EnRouteWrapper.instance().manager().getTaskManager()
                .findTaskById(taskId) == null) {
            refresh();
        }

        glympTask = EnRouteWrapper.instance().manager().getTaskManager()
                .findTaskById(taskId);

        EnRouteWrapper.instance().manager().getTaskManager().setTaskPhase(glympTask,
                EnRouteConstants.PHASE_PROPERTY_NOT_COMPLETED());


    }

//    private void updateDriverStatus(int position) {
//        try {
//            driverStatus = new DriverStatus();
//            driverStatus.setTimezone(TimeZone.getDefault().getID());
//            driverStatus.setDriverStatus(position);
//
//            updateLat = user_lat;
//            updateLng = user_lng;
//
//            if (updateLat == 0.0 && current_latitude != 0.0) {
//                updateLat = current_latitude;
//                updateLng = current_longitude;
//            }
//
//            driverStatus.setCurrentLat(updateLat);
//            driverStatus.setCurrentLan(updateLng);
//
//            if(position == DeliforceConstants.DRIVER_STATUS_IDLE || position == DeliforceConstants.DRIVER_STATUS_OFFLINE) {
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
//                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
//                driverStatus.setOfflineDate(sdf.format(new Date()));
//            }
//            /*if (gpsTracker.canGetLocation()) {
//                driverStatus.setCurrentLan(gpsTracker.getLatitude());
//                driverStatus.setCurrentLan(gpsTracker.getLongitude());
//            }*/
//
//            if (!isFinishing()) show_loader();
//            apiService.updateDriverStatus(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken(), driverStatus).enqueue(new Callback<DriverStatusUpdate>() {
//                @Override
//                public void onResponse(@NotNull Call<DriverStatusUpdate> call, @NotNull Response<DriverStatusUpdate> response) {
//
//                    if (!isFinishing()) dismiss_loader();
//                    try {
//                        if (response.raw().code() == 200) {
//                            if (position == 4) {
//                                loginPrefManager.setStringValue("driver_status", "2");
//                            } else {
//                                loginPrefManager.setStringValue("driver_status", "1");
//                            }
//                        }//alert dialog for user already logged in
//                        else if (response.code() == 494) {
//                            showAlertDialog(TaskDetailsActivity.this);
//                        }
//
//
//                        snackbar.dismiss();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//
//                @Override
//                public void onFailure(@NotNull Call<DriverStatusUpdate> call, @NotNull Throwable t) {
//                    if (!isFinishing()) dismiss_loader();
//
//                }
//            });
//
//        } catch (Exception e) {
//            if (!isFinishing()) dismiss_loader();
//            e.printStackTrace();
//        }
//    }

    private void getDistance(int type) {
        driver_longitude = user_lng;
        driver_latitude = user_lat;

        if (driver_latitude == 0.0 && current_latitude != 0.0) {
            driver_latitude = current_latitude;
            driver_longitude = current_longitude;
        } else if (driver_latitude == 0.0 && loginPrefManager.getDoubleLatitude() != 0.0) {
            driver_latitude = loginPrefManager.getDoubleLatitude();
            driver_longitude = loginPrefManager.getDoubleLongitude();
        } else {
            if (AppUtils.getDeviceLocation() != null) {
                driver_latitude = AppUtils.getDeviceLocation().getLatitude();
                driver_longitude = AppUtils.getDeviceLocation().getLongitude();
                //driver_acc = AppUtils.getDeviceLocation().getAccuracy();
            }
        }
        System.out.println("getDistance: " + driver_latitude + ", " + driver_longitude);

        // added driver reached customer location validation.
        try {
            if (type == TASK_SUCCESS) {
                if (loginPrefManager.getRadiusValidation()) {
                    calculateDriverDistance(type);
                    /*float[] resultDistance = new float[1];
                    float apiRadiusDistance = Float.parseFloat(loginPrefManager.getRadiusDistance());
                    Location.distanceBetween(driver_latitude, driver_longitude,
                            Double.parseDouble(loginPrefManager.getDropLatitude()), Double.parseDouble(loginPrefManager.getDropLongitude()), resultDistance);
                    if (resultDistance[0] <= apiRadiusDistance) {
                        doGoogleDistanceCalculation();
                    } else
                        Toast.makeText(TaskDetailsActivity.this, "" + getString(R.string.reach_customer_loc), Toast.LENGTH_LONG).show();*/
                } else
                    doGoogleDistanceCalculation();
            } else if (type == TASK_ARRIVED) {
                calculateDriverDistance(type);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (type == TASK_SUCCESS) {
                doGoogleDistanceCalculation();
            } else
                Toast.makeText(getApplicationContext(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
        }
    }

  /*  private void calculateDriverDistance(int type) {
        float[] resultDistance = new float[1];
        if (taskOneDetails.getSettings().getActionBlock()
                .getArrivedDistance() != null && taskOneDetails.getSettings().getActionBlock()
                .getArrivedDistance().getValue() != null &&
                !taskOneDetails.getSettings().getActionBlock()
                        .getArrivedDistance().getValue().isEmpty() &&
                !taskOneDetails.getSettings().getActionBlock().getArrivedDistance().getValue().equals("0") &&
                !taskOneDetails.getSettings().getActionBlock().getArrivedDistance().getValue().equals("0.0")
        ) {
            float apiRadiusDistance = Float.parseFloat(taskOneDetails.getSettings().getActionBlock()
                    .getArrivedDistance().getValue());
            Location.distanceBetween(driver_latitude, driver_longitude,
                    Double.parseDouble(loginPrefManager.getDropLatitude()), Double.parseDouble(loginPrefManager.getDropLongitude()), resultDistance);
            if (resultDistance[0] <= apiRadiusDistance) {
                if (type == TASK_ARRIVED)
                    upDateTask(DeliforceConstants.TASK_ARRIVED, "");
                else if (type == TASK_SUCCESS)
                    doGoogleDistanceCalculation();
            } else {
                if (type == TASK_ARRIVED)
                    Toast.makeText(TaskDetailsActivity.this, "" + getString(R.string.err_arrival_customer_loc), Toast.LENGTH_LONG).show();
                else if (type == TASK_SUCCESS)
                    Toast.makeText(TaskDetailsActivity.this, "" + getString(R.string.reach_customer_loc), Toast.LENGTH_LONG).show();
            }
        } else {
            if (type == TASK_ARRIVED)
                upDateTask(DeliforceConstants.TASK_ARRIVED, "");
            else if (type == TASK_SUCCESS)
                doGoogleDistanceCalculation();
        }
    }*/

    private void calculateDriverDistance(int type) {
        float[] resultDistance = new float[1];
        if (type == TASK_ARRIVED) {
            if (taskOneDetails.getSettings().getActionBlock()
                    .getArrivedDistance() != null && taskOneDetails.getSettings().getActionBlock()
                    .getArrivedDistance().getValue() != null &&
                    !taskOneDetails.getSettings().getActionBlock()
                            .getArrivedDistance().getValue().isEmpty() &&
                    !taskOneDetails.getSettings().getActionBlock().getArrivedDistance().getValue().equals("0") &&
                    !taskOneDetails.getSettings().getActionBlock().getArrivedDistance().getValue().equals("0.0")
            ) {
                float apiRadiusDistance = Float.parseFloat(taskOneDetails.getSettings().getActionBlock()
                        .getArrivedDistance().getValue());
                Location.distanceBetween(driver_latitude, driver_longitude,
                        Double.parseDouble(loginPrefManager.getDropLatitude()), Double.parseDouble(loginPrefManager.getDropLongitude()), resultDistance);
                if (resultDistance[0] <= apiRadiusDistance) {
                    upDateTask(DeliforceConstants.TASK_ARRIVED, "");
                } else {
                    Toast.makeText(TaskDetailsActivity.this, "" + getString(R.string.err_arrival_customer_loc), Toast.LENGTH_LONG).show();
                }
            } else {
                upDateTask(DeliforceConstants.TASK_ARRIVED, "");
            }
        } else if (type == TASK_SUCCESS) {
            if (taskOneDetails.getSettings().getActionBlock()
                    .getDistance() != null && taskOneDetails.getSettings().getActionBlock()
                    .getDistance().getValue() != null &&
                    !taskOneDetails.getSettings().getActionBlock()
                            .getDistance().getValue().isEmpty() &&
                    !taskOneDetails.getSettings().getActionBlock().getDistance().getValue().equals("0") &&
                    !taskOneDetails.getSettings().getActionBlock().getDistance().getValue().equals("0.0")
            ) {
                float apiRadiusDistance = Float.parseFloat(taskOneDetails.getSettings().getActionBlock()
                        .getDistance().getValue());
                Location.distanceBetween(driver_latitude, driver_longitude,
                        Double.parseDouble(loginPrefManager.getDropLatitude()), Double.parseDouble(loginPrefManager.getDropLongitude()), resultDistance);
                if (resultDistance[0] <= apiRadiusDistance) {
                    doGoogleDistanceCalculation();
                } else {
                    Toast.makeText(TaskDetailsActivity.this, "" + getString(R.string.reach_customer_loc), Toast.LENGTH_LONG).show();
                }
            } else {
                doGoogleDistanceCalculation();
            }
        }
    }


    private void doGoogleDistanceCalculation() {

        float totalDriverDis = loginPrefManager.getTotalLiveDistance();
        updateTaskStatus.setTravelledDistance(totalDriverDis / 1000);

        System.out.println("Task update Live data Distance: " + SphericalUtil.computeLength(AppUtils.getLiveDataLatLng()));

        liveLogData = locationPrefs.getString("live_location_array", null);
        System.out.println("Live log data status: " + liveLogData);
        if (liveLogData == null) {
            //upDateTask(DeliforceConstants.TASK_SUCCESS, "");
            if (loginPrefManager.getGlympseEnabled() && glympseTaskID != null) {
                glympseTask(DeliforceConstants.TASK_SUCCESS);
            } else {
                updateOTPTask(DeliforceConstants.TASK_SUCCESS, "", 0, false);
            }
        } else {
            if (loginPrefManager.getGlympseEnabled() && glympseTaskID != null) {
                glympseTask(DeliforceConstants.TASK_SUCCESS);
            } else {
                sendLogAndCompleteTask();
            }
        }
    }

    private void sendLogAndCompleteTask() {
        try {
            show_loader();
            driver_longitude = loginPrefManager.getDoubleLongitude();
            driver_latitude = loginPrefManager.getDoubleLatitude();

            double driver_acc = 0;
            if (driver_latitude == 0.0 && current_latitude != 0.0) {
                driver_latitude = current_latitude;
                driver_longitude = current_longitude;
                driver_acc = current_accuraccy;
            } else if (driver_latitude == 0.0 && loginPrefManager.getDoubleLatitude() != 0.0) {
                driver_latitude = loginPrefManager.getDoubleLatitude();
                driver_longitude = loginPrefManager.getDoubleLongitude();
                driver_acc = loginPrefManager.getAccuracy();
            } else {
                if (AppUtils.getDeviceLocation() != null) {
                    driver_latitude = AppUtils.getDeviceLocation().getLatitude();
                    driver_longitude = AppUtils.getDeviceLocation().getLongitude();
                    driver_acc = AppUtils.getDeviceLocation().getAccuracy();
                }
            }

            driverStatus = new DriverStatus();
            driverStatus.setTimezone(TimeZone.getDefault().getID());
            driverStatus.setDriverStatus(Integer.parseInt(loginPrefManager.getStringValue("driver_status")));
            driverStatus.setCurrentLat(driver_latitude);
            driverStatus.setCurrentLan(driver_longitude);
            driverStatus.setTopic("livetracking");
            driverStatus.setDriver_id(loginPrefManager.getManagerID());
            String liveLocData;
            try {
                Gson gson = new Gson();
                Type type = new TypeToken<List<LiveTrackingModelData>>() {
                }.getType();
                liveLocData = locationPrefs.getString("live_location_array", null);
                List<LiveTrackingModelData> trackRecord = gson.fromJson(liveLocData, type);
                if (trackRecord != null && trackRecord.size() > 0)
                    driverStatus.setDriverLiveLog(trackRecord);
                else
                    driverStatus.setDriverLiveLog(Collections.emptyList());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Set manager = loginPrefManager.getAdminList();
            List<String> mainList = new ArrayList<String>(manager);
            driverStatus.setAdminArray(mainList);

            String formattedAddress = getCompleteAddressString(TaskDetailsActivity.this, driver_latitude, driver_longitude);

            driverStatus.setAddress(formattedAddress);

            driverStatus.setBatteryStatus(AppUtils.getBatteryPercentage(getBaseContext()));
            driverStatus.setTaskIds(loginPrefManager.getTaskIds());

            sendLiveLog(driver_latitude, driver_longitude, driver_acc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendLiveLog(double driver_latitude, double driver_longitude, double driver_acc) {
        createALiveLog(driver_latitude, driver_longitude, driver_acc);
        String liveLocData;
        try {
            Gson gson = new Gson();
            Type type = new TypeToken<List<LiveTrackingModelData>>() {
            }.getType();
            liveLocData = locationPrefs.getString("live_location_array", null);
            List<LiveTrackingModelData> trackRecord = gson.fromJson(liveLocData, type);
            if (trackRecord != null && trackRecord.size() > 0)
                driverStatus.setDriverLiveLog(trackRecord);
            else
                driverStatus.setDriverLiveLog(Collections.emptyList());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.e("taskdetail_livelatlong",driver_latitude+",,"+driver_longitude);

        String location = loginPrefManager.getStringValue("live_latlong");

        if (location==null||location.isEmpty()){
            loginPrefManager.setStringValue("live_latlong",driver_latitude+","+driver_longitude);
        }else{
            //if (!location.contains(driver_latitude+","+driver_longitude)) {
                location = location + ":" + driver_latitude + "," + driver_longitude;
                loginPrefManager.setStringValue("live_latlong", location);
            //}
        }
        apiService.updateDriverLivetrack(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken(), driverStatus).enqueue(new Callback<DriverStatusUpdate>() {
            @Override
            public void onResponse(@NonNull Call<DriverStatusUpdate> call, @NonNull Response<DriverStatusUpdate> response) {
                dismiss_loader();
                try {
                    //System.out.println(response.raw().code());
                    if (response.raw().code() == 200) {
                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("live_location_array", null);
                        editor.apply();
                        updateOTPTask(DeliforceConstants.TASK_SUCCESS, "", 0, false);
                    } else if (response.raw().code() == 401) {
                        try {
                            if (AppHelper.getPool().getCurrentUser() != null) {
                                findCurrent();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<DriverStatusUpdate> call, @NotNull Throwable t) {
                dismiss_loader();
                Timber.tag(TAG).e("onFailure%s", t.getMessage());
            }
        });
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    private void getStartLocations() {

        Gson gson = new Gson();
        String retive_dat = loginPrefManager.getStringValue("start_locations");

        java.lang.reflect.Type type = new TypeToken<HashMap<String, LatLng>>() {
        }.getType();
        HashMap<String, LatLng> testHashMap2 = gson.fromJson(retive_dat, type);


        if (testHashMap2 != null && testHashMap2.size() > 0) {
            multiMap = testHashMap2;

            Set keys = multiMap.keySet();
            for (Object key1 : keys) {
                String key = (String) key1;
                LatLng value = multiMap.get(key);
                if (key.equalsIgnoreCase(task_id)) {
                    start_latLng = new LatLng(value.latitude, value.longitude);
                }
            }
        }


    }

    private void removeLocation() {

        multiMap.remove(loginPrefManager.getStringValue("taskId"));
        Gson gson = new Gson();
        loginPrefManager.setStringValue("start_locations", "");
        String hashMapString = gson.toJson(multiMap);

        loginPrefManager.setStringValue("start_locations", hashMapString);

        if (!isFinishing()) dismiss_loader();
        if (activity_name != null && activity_name.equalsIgnoreCase("navigation")) {
            startActivity(new Intent(TaskDetailsActivity.this, NavigationActivity.class));
            finish();
        } else if (activity_name != null && activity_name.equalsIgnoreCase("route")) {
            startActivity(new Intent(TaskDetailsActivity.this, RouteActivity.class));
            finish();
        } else if (activity_name != null && activity_name.equalsIgnoreCase("map")) {
            startActivity(new Intent(TaskDetailsActivity.this, MapTaskActivity.class));
            finish();
        }
    }

    private void AskCameraPermissions() {
        permissionHelper.check(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .withDialogBeforeRun(R.string.dialog_before_run_title, R.string.dialog_location_before_run_message, R.string.dialog_positive_button)
                .setDialogPositiveButtonColor(R.color.colorPrimary)
                .onSuccess(this::onSuccessProfile)
                .onDenied(this::onDeniedProfile)
                .onNeverAskAgain(this::onNeverAskAgainProfile)
                .run();
    }

    private void onSuccessProfile() {
        Pix.start(TaskDetailsActivity.this, 100);
    }

    private void onNeverAskAgainProfile() {
        showNointerntView(getString(R.string.enable_camera_permission));
    }

    private void onDeniedProfile() {
        showNointerntView(getString(R.string.onDenied));
    }

    private void showBlockDialog() {
        show_error_response(getString(R.string.error_block_user));
    }


    private void onSuccessSlide() {
        int note_size = notes_list.size();
        int image_size = addImagesAdpater.getItemCount();
        int bar_code_size = barcode_data.size();

        if (loginPrefManager.getStringValue("driver_status").equalsIgnoreCase("4")) {
            showBlockDialog();
        }

        if (note_need && images_need && signature_need && Bar_code_need) {
            if (note_size <= 0 && image_size <= 0 && signature_added.equalsIgnoreCase("") && bar_code_size <= 0) {
                show_Alert(0);
            } else if (note_size <= 0 && image_size <= 0 && signature_added.equalsIgnoreCase("")) {
                show_Alert(11);
            } else if (note_size <= 0 && image_size <= 0 && bar_code_size <= 0) {
                show_Alert(12);
            } else if (note_size <= 0 && signature_added.equalsIgnoreCase("") && bar_code_size <= 0) {
                show_Alert(13);
            } else if (image_size <= 0 && signature_added.equalsIgnoreCase("") && bar_code_size <= 0) {
                show_Alert(14);
            } else if (note_size <= 0 && image_size <= 0) {
                show_Alert(1);
            } else if (image_size <= 0 && signature_added.equalsIgnoreCase("")) {
                show_Alert(2);
            } else if ((signature_added.equalsIgnoreCase("") && bar_code_size <= 0)) {
                show_Alert(3);
            } else if (note_size <= 0 && signature_added.equalsIgnoreCase("")) {
                show_Alert(4);
            } else if (note_size <= 0 && bar_code_size <= 0) {
                show_Alert(5);
            } else if (image_size <= 0 && bar_code_size <= 0) {
                show_Alert(6);
            } else if (note_size <= 0) {
                show_Alert(7);
            } else if (image_size <= 0) {
                show_Alert(8);
            } else if (signature_added.equalsIgnoreCase("")) {
                show_Alert(9);
            } else if (bar_code_size <= 0) {
                show_Alert(10);
            } else if (loginPrefManager.isActualCustomer() && !isActualCustomerAdded) {
                show_Alert(DeliforceConstants.isActualCustomer);
            } else {
                complete_task();
            }

        } else if (note_need && images_need && signature_need) {
            if (note_size <= 0 && image_size <= 0 && signature_added.equalsIgnoreCase("")) {
                show_Alert(11);
            } else if (note_size <= 0 && image_size <= 0) {
                show_Alert(1);
            } else if (image_size <= 0 && signature_added.equalsIgnoreCase("")) {
                show_Alert(2);
            } else if (note_size <= 0 && signature_added.equalsIgnoreCase("")) {
                show_Alert(4);
            } else if (note_size <= 0) {
                show_Alert(7);
            } else if (image_size <= 0) {
                show_Alert(8);
            } else if (signature_added.equalsIgnoreCase("")) {
                show_Alert(9);
            } else if (loginPrefManager.isActualCustomer() && !isActualCustomerAdded) {
                show_Alert(DeliforceConstants.isActualCustomer);
            } else {
                complete_task();
            }

        } else if (note_need && images_need && Bar_code_need) {

            if (note_size <= 0 && image_size <= 0 && bar_code_size <= 0) {
                show_Alert(12);
            } else if (note_size <= 0 && image_size <= 0) {
                show_Alert(1);
            } else if (note_size <= 0 && bar_code_size <= 0) {
                show_Alert(5);
            } else if (image_size <= 0 && bar_code_size <= 0) {
                show_Alert(6);
            } else if (note_size <= 0) {
                show_Alert(7);
            } else if (image_size <= 0) {
                show_Alert(8);
            } else if (bar_code_size <= 0) {
                show_Alert(10);
            } else if (loginPrefManager.isActualCustomer() && !isActualCustomerAdded) {
                show_Alert(DeliforceConstants.isActualCustomer);
            } else {
                complete_task();
            }

        } else if (note_need && signature_need && Bar_code_need) {

            if (note_size <= 0 && signature_added.equalsIgnoreCase("") && bar_code_size <= 0) {
                show_Alert(13);
            } else if ((signature_added.equalsIgnoreCase("") && bar_code_size <= 0)) {
                show_Alert(3);
            } else if (note_size <= 0 && signature_added.equalsIgnoreCase("")) {
                show_Alert(4);
            } else if (note_size <= 0 && bar_code_size <= 0) {
                show_Alert(5);
            } else if (note_size <= 0) {
                show_Alert(7);
            } else if (signature_added.equalsIgnoreCase("")) {
                show_Alert(9);
            } else if (bar_code_size <= 0) {
                show_Alert(10);
            } else if (loginPrefManager.isActualCustomer() && !isActualCustomerAdded) {
                show_Alert(DeliforceConstants.isActualCustomer);
            } else {
                complete_task();
            }

        } else if (images_need && signature_need && Bar_code_need) {

            if (image_size <= 0 && signature_added.equalsIgnoreCase("") && bar_code_size <= 0) {
                show_Alert(14);
            } else if (image_size <= 0 && signature_added.equalsIgnoreCase("")) {
                show_Alert(2);
            } else if ((signature_added.equalsIgnoreCase("") && bar_code_size <= 0)) {
                show_Alert(3);
            } else if (image_size <= 0 && bar_code_size <= 0) {
                show_Alert(6);
            } else if (image_size <= 0) {
                show_Alert(8);
            } else if (signature_added.equalsIgnoreCase("")) {
                show_Alert(9);
            } else if (bar_code_size <= 0) {
                show_Alert(10);
            } else if (loginPrefManager.isActualCustomer() && !isActualCustomerAdded) {
                show_Alert(DeliforceConstants.isActualCustomer);
            } else {
                complete_task();
            }

        } else if (note_need && images_need) {

            if (note_size <= 0 && image_size <= 0) {
                show_Alert(1);
            } else if (note_size <= 0) {
                show_Alert(7);
            } else if (image_size <= 0) {
                show_Alert(8);
            } else if (loginPrefManager.isActualCustomer() && !isActualCustomerAdded) {
                show_Alert(DeliforceConstants.isActualCustomer);
            } else {
                complete_task();
            }

        } else if (note_need && signature_need) {
            if (note_size <= 0 && signature_added.equalsIgnoreCase("")) {
                show_Alert(4);
            } else if (note_size <= 0) {
                show_Alert(7);
            } else if (signature_added.equalsIgnoreCase("")) {
                show_Alert(9);
            } else if (loginPrefManager.isActualCustomer() && !isActualCustomerAdded) {
                show_Alert(DeliforceConstants.isActualCustomer);
            } else {
                complete_task();
            }

        } else if (note_need && Bar_code_need) {
            if (note_size <= 0 && bar_code_size <= 0) {
                show_Alert(5);
            } else if (note_size <= 0) {
                show_Alert(7);
            } else if (bar_code_size <= 0) {
                show_Alert(10);
            } else if (loginPrefManager.isActualCustomer() && !isActualCustomerAdded) {
                show_Alert(DeliforceConstants.isActualCustomer);
            } else {
                complete_task();
            }
        } else if (images_need && signature_need) {
            if (image_size <= 0 && signature_added.equalsIgnoreCase("")) {
                show_Alert(2);
            } else if (image_size <= 0) {
                show_Alert(8);
            } else if (signature_added.equalsIgnoreCase("")) {
                show_Alert(9);
            } else if (loginPrefManager.isActualCustomer() && !isActualCustomerAdded) {
                show_Alert(DeliforceConstants.isActualCustomer);
            } else {
                complete_task();
            }
        } else if (images_need && Bar_code_need) {
            if (image_size <= 0 && bar_code_size <= 0) {
                show_Alert(6);
            } else if (image_size <= 0) {
                show_Alert(8);
            } else if (bar_code_size <= 0) {
                show_Alert(10);
            } else if (loginPrefManager.isActualCustomer() && !isActualCustomerAdded) {
                show_Alert(DeliforceConstants.isActualCustomer);
            } else {
                complete_task();
            }
        } else if (signature_need && Bar_code_need) {
            if ((signature_added.equalsIgnoreCase("") && bar_code_size <= 0)) {
                show_Alert(3);
            }
            if (signature_added.equalsIgnoreCase("")) {
                show_Alert(9);
            } else if (bar_code_size <= 0) {
                show_Alert(10);
            } else if (loginPrefManager.isActualCustomer() && !isActualCustomerAdded) {
                show_Alert(DeliforceConstants.isActualCustomer);
            } else {
                complete_task();
            }
        } else if (note_need) {
            if (note_size <= 0) {
                show_Alert(7);
            } else if (loginPrefManager.isActualCustomer() && !isActualCustomerAdded) {
                show_Alert(DeliforceConstants.isActualCustomer);
            } else {
                complete_task();
            }
        } else if (images_need) {
            if (image_size <= 0) {
                show_Alert(8);
            } else if (loginPrefManager.isActualCustomer() && !isActualCustomerAdded) {
                show_Alert(DeliforceConstants.isActualCustomer);
            } else {
                complete_task();
            }
        } else if (signature_need) {
            if (signature_added.equalsIgnoreCase("")) {
                show_Alert(9);
            } else if (loginPrefManager.isActualCustomer() && !isActualCustomerAdded) {
                show_Alert(DeliforceConstants.isActualCustomer);
            } else {
                complete_task();
            }
        } else if (Bar_code_need) {
            if (bar_code_size <= 0) {
                show_Alert(10);
            } else if (loginPrefManager.isActualCustomer() && !isActualCustomerAdded) {
                show_Alert(DeliforceConstants.isActualCustomer);
            } else {
                complete_task();
            }
        } else {
            if (!note_need && !images_need && !signature_need && !Bar_code_need) {
                if (loginPrefManager.isActualCustomer() && !isActualCustomerAdded) {
                    show_Alert(DeliforceConstants.isActualCustomer);
                } else
                    complete_task();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (loginPrefManager.getGlympseEnabled() && glympseTaskID != null) {
            EnRouteWrapper.instance().manager().addListener(this);
            EnRouteWrapper.instance().manager().getTaskManager().addListener(this);

            refresh();
        }


    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    /**
     * GListener section
     */
    public void eventsOccurred(GSource source, int listener, int events, Object param1,
                               Object param2) {


        if (EE.LISTENER_TASKS == listener) {
            if (0 != (EE.TASKS_TASK_LIST_CHANGED & events)) {
            }

            if ((0 != (EE.TASKS_TASK_STARTED & events))) {
                showLoader = false;
                if (APIhitsuccess) {
                    APIhitsuccess = false;
                    upDateTask(TASK_STARTED, "");
                }
            }
            if ((0 != (EE.TASKS_TASK_START_FAILED & events))) {
                dismiss_loader();
                showShortMessage("Glympse task started failed, Please try again");
            }

            if ((0 != (EE.TASKS_OPERATION_COMPLETED & events))) {
                if (APIhitCompletesuccess) {
                    APIhitCompletesuccess = false;
                    if (liveLogData == null) {
                        completeTask();
                    } else {
                        sendLogAndCompleteTask();
                    }
                }
            }

        }
        if ((0 != (EE.TASKS_OPERATION_COMPLETION_FAILED & events))) {
            dismiss_loader();
        }


        if ((0 != (EE.TASKS_TASK_PHASE_CHANGED & events))) {
            if (EnRouteConstants.PHASE_PROPERTY_CANCELLED().equals(glympTask.getPhase())) {
                showLoader = false;
                upDateTask(DeliforceConstants.TASK_STATUS_CANCELLED, cancelTaskMessage);
            } else if (EnRouteConstants.PHASE_PROPERTY_NOT_COMPLETED().equals(glympTask.getPhase())) {
                showLoader = false;
                upDateTask(DeliforceConstants.TASK_FAILED, cancelTaskMessage);
            } else if (EnRouteConstants.PHASE_PROPERTY_ARRIVED().equals(glympTask.getPhase())) {
                showLoader = false;
                upDateTask(DeliforceConstants.TASK_ARRIVED, "");


            }

        }

        if ((0 != (EE.TASKS_TASK_START_FAILED & events))) {
            dismiss_loader();
            if (BuildConfig.DEBUG)
                showShortMessage("Glympse TASKS_TASK_START_FAILED");
        }

        if ((0 != (EE.TASKS_TASK_METADATA_UPDATE_FAILED & events))) {
            if (BuildConfig.DEBUG)
                showShortMessage("Glympse TASKS_TASK_METADATA_UPDATE_FAILED");
        }
        if ((0 != (EE.TASKS_OPERATION_TICKET_CHANGED & events))) {
        }
        if ((0 != (EE.TASKS_TASK_METADATA_UPDATE_SUCCEEDED & events))) {
            if (EnRouteConstants.PHASE_PROPERTY_ARRIVED().equals(glympTask.getPhase())) {
                showLoader = false;
                upDateTask(DeliforceConstants.TASK_ARRIVED, "");


            }
        } else if (EE.LISTENER_SESSIONS == listener) {
            if (0 != (EE.TASKS_OPERATION_TICKET_CHANGED & events)) {
            }

            if (0 != (EE.SESSIONS_SESSION_ACTIVE_TASK_PHASE_CHANGED & events)) {
            }

            if (0 != (EE.SESSIONS_SESSION_ACTIVE_TASK_PHASE_CHANGED & events)) {

            }
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //remove the listener when task is completed
        //otherwise it will add all task
        if (loginPrefManager.getGlympseEnabled() && glympseTaskID != null) {
            EnRouteWrapper.instance().manager().removeListener(this);
            EnRouteWrapper.instance().manager().getTaskManager().removeListener(this);

        }
        dismiss_loader();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task_details, menu);
        menuTaskLocationUpdate = menu.findItem(R.id.menuTaskLocationUpdate);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuTaskLocationUpdate:
                updateCaptureLocation();
                showShortToastMessage(getString(R.string.location_captured));
                menuTaskLocationUpdate.setVisible(false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateCaptureLocation() {
        CaptureLocation captureLocation = new CaptureLocation();
        captureLocation.setAddress(getCompleteAddressString(getApplicationContext(),
                loginPrefManager.getDoubleLatitude(),
                loginPrefManager.getDoubleLongitude()));
        captureLocation.setLatitude(loginPrefManager.getDoubleLatitude() + "");
        captureLocation.setLongitude(loginPrefManager.getDoubleLongitude() + "");
        taskOneDetails.setCaptureLocation(captureLocation);
    }
}