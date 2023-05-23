package com.app.deliforcemanager.Activities;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.deliforcemanager.Adapter.TaskNotificationAdapter;
import com.app.deliforcemanager.CustomDialogView.CancelDialog;
import com.app.deliforcemanager.LocalizationActivity.LocalizationActivity;
import com.app.deliforcemanager.ModelClass.AutoAllocationApi.AutoAllocationStatus;
import com.app.deliforcemanager.ModelClass.DriverStatusApi.DriverStatusUpdate;
import com.app.deliforcemanager.ModelClass.TaskDetailsApi.OTPTaskUpdate;
import com.app.deliforcemanager.ModelClass.TaskNotificationModel;
import com.app.deliforcemanager.R;
import com.app.deliforcemanager.RawHeaders.AutoAllocationInfo.AutoAllocationData;
import com.app.deliforcemanager.RawHeaders.DriverStatusInfo.DriverStatus;
import com.app.deliforcemanager.RawHeaders.TaskInfo.UpdateTaskStatus;
import com.app.deliforcemanager.Services.PushNotificationService;
import com.app.deliforcemanager.Utils.AppUtils;
import com.app.deliforcemanager.Utils.DeliforceConstants;
import com.app.deliforcemanager.glypmseWrapper.EnRouteWrapper;
import com.glympse.enroute.android.api.GTask;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static android.media.MediaExtractor.MetricsConstants.FORMAT;
import static com.app.deliforcemanager.Utils.DeliforceConstants.TASK_ACCEPTED;
import static com.app.deliforcemanager.Utils.DeliforceConstants.TASK_DECLINED;
import static com.app.deliforcemanager.Utils.DeliforceConstants.TASK_STARTED;
import static com.app.deliforcemanager.Utils.DeliforceConstants.TASK_STATUS_ACKNOWLEDGE;
import static com.app.deliforcemanager.Utils.DeliforceConstants.TASK_STATUS_CANCELLED;

public class NewTaskActivity extends LocalizationActivity /*implements GListener*/ {

    private static final String TAG = "NewTaskActivity";

    //GPSTracker gpsTracker;
    //double longitude, latitude;
    ArrayList<TaskNotificationModel> taskNotificationModels = new ArrayList<>();
    RecyclerView recyclerNotifications;
    TaskNotificationAdapter taskNotificationAdapter;
    private Button btnAcceptTask, btnDeclineNotification, acktask, btnStart, btnCancle, btnGlympseTask;
    private ImageButton imgBtnCloseNotification;
    private LinearLayout layoutAcceptTask, layoutDeclineTask, layoutAckTask, layoutStartType, layoutGlympseTask;
    //    private String taskId;
    private CountDownTimer countDownTimer = null;
    private TextView tv_duty_status;
    private Switch status_switch;
    private DriverStatus driverStatus;
    private String auto_status;
    private CancelDialog cancelDialog;
    private Snackbar snackbar;
    private double updateLat, updateLng;
    //    private String glympseTaskID = "";
    private boolean ackBtnPressed = false;
    private GTask glympTask;
    private boolean showLoader = true;

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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_new_task);

        taskNotificationModels=null;

        //glympse listener
       // if (getIntent().getExtras() != null) {

         //   taskNotificationModels = (ArrayList<TaskNotificationModel>) getIntent().getSerializableExtra("task");
            
            //String data =  getIntent().getStringExtra("task");


            String data1 =loginPrefManager.getStringValue("notification_data");

            Log.e("received_data11",data1);

           // TaskNotificationModel   model =(ArrayList<TaskNotificationModel>) new Gson().fromJson(data,TaskNotificationModel.class);


            Type listType = new TypeToken<ArrayList<TaskNotificationModel>>() {}.getType();

            taskNotificationModels = new Gson().fromJson(data1, listType);
            if (taskNotificationModels != null) {
                Log.e("notification_data", taskNotificationModels.get(0).getTitle());

            }

            if (taskNotificationModels != null) {
                if (loginPrefManager.getGlympseEnabled()) {
                    Intent notificationIntent = getIntent();
                    if (notificationIntent != null) {
//                glympseTaskID = notificationIntent.getStringExtra("glympse_task_id");

//                loginPrefManager.setGlympseID("glympseID",""+glympseTaskID);
                    }
                }

                //gpsTracker = new GPSTracker(NewTaskActivity.this);
                try {
                    initView();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                finish();
            }


        /*} else {
            finish();
        }*/
    }

    @Override
    protected void onStart() {
        super.onStart();
/*
         if (loginPrefManager.getGlympseEnabled()&& glympseTaskID!=null) {

                 EnRouteWrapper.instance().manager().addListener(this);
                 EnRouteWrapper.instance().manager().getTaskManager().addListener(this);
                 refresh();
             }
*/

    }

    private void initView() throws ParseException {

        recyclerNotifications = findViewById(R.id.recyclerNotifications);
        btnAcceptTask = findViewById(R.id.btn_accept_task);
        btnAcceptTask.setText(AppUtils.getButtonStatus(NewTaskActivity.this, TASK_ACCEPTED));
        btnDeclineNotification = findViewById(R.id.btn_decline_notification);
        btnDeclineNotification.setText(AppUtils.getButtonStatus(NewTaskActivity.this, TASK_DECLINED));
        imgBtnCloseNotification = findViewById(R.id.btn_close_notification);
        layoutAcceptTask = findViewById(R.id.layout_notify_accept_task);
        layoutDeclineTask = findViewById(R.id.layout_notify_decline_task);
        layoutAckTask = findViewById(R.id.layout_notify_acknowledge);
        acktask = findViewById(R.id.btn_acknowlwdge_task);
        acktask.setText(AppUtils.getButtonStatus(NewTaskActivity.this, TASK_STATUS_ACKNOWLEDGE));
        btnStart = findViewById(R.id.btn_start_task);
        btnStart.setText(AppUtils.getButtonStatus(NewTaskActivity.this, TASK_STARTED));
        btnCancle = findViewById(R.id.btn_cancle_notification);
        btnCancle.setText(AppUtils.getButtonStatus(NewTaskActivity.this, TASK_STATUS_CANCELLED));
        layoutStartType = findViewById(R.id.layoutStartType);
        layoutGlympseTask = findViewById(R.id.layout_glympse_task);
        btnGlympseTask = findViewById(R.id.btn_glympse_task);

        recyclerNotifications.setLayoutManager(new LinearLayoutManager(NewTaskActivity.this));


        init();

        taskNotificationAdapter = new TaskNotificationAdapter(NewTaskActivity.this, taskNotificationModels);
        recyclerNotifications.setAdapter(taskNotificationAdapter);

        onClickEvents();
        BroadcastReceiver new_task_receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    startActivity(new Intent(NewTaskActivity.this, NavigationActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    finish();
                }
            }
        };

        LocalBroadcastManager.getInstance(NewTaskActivity.this).registerReceiver(new_task_receiver, new IntentFilter("receiver"));


    }

    private void onClickEvents() {
        btnStart.setOnClickListener(view -> {

            Timber.e("auto_status" + "" + auto_status);

            if (loginPrefManager.getStringValue("driver_status").equals("3")) {
                showTwoButtonSnackbar();
            } else {
                switch (auto_status) {
                    case "1":
                       /* if (loginPrefManager.getGlympseEnabled()&& glympseTaskID!=null) {

                            glympseTaskAction(DeliforceConstants.TASK_STARTED);


                        } else {*/
                        updateAutoTask(DeliforceConstants.TASK_STARTED, "", 0);

                        /* }*/
                        break;
                    /*This is for direct start*/
                    case "0":
                       /* if (loginPrefManager.getGlympseEnabled()&& glympseTaskID!=null) {

                            glympseTaskAction(DeliforceConstants.TASK_STARTED);

                        } else {*/
                        upDateTask(DeliforceConstants.TASK_STARTED, " ", 0);
                        /* }*/
                        break;
                    default:
//                        changeStatus(DeliforceConstants.TASK_ACCEPTED);
                        /*if (loginPrefManager.getGlympseEnabled()&& glympseTaskID!=null) {
                            glympseTaskAction(DeliforceConstants.TASK_STARTED);

                        } else {*/
                        upDateTask(DeliforceConstants.TASK_STARTED, " ", 0);
                        /*}*/
                        break;
                }
            }

        });
        btnCancle.setOnClickListener(view -> {
            if (loginPrefManager.getStringValue("driver_status").equals("3")) {
                showTwoButtonSnackbar();
            } else {
                if (auto_status.equals("1")) {
                    showCancleDailog(DeliforceConstants.TASK_STATUS_CANCELLED, 1);
                } else {
                    showCancleDailog(DeliforceConstants.TASK_STATUS_CANCELLED, 0);
                }
            }
        });

        btnAcceptTask.setOnClickListener(view -> {
            Log.e("newTaskdriverStatus", "----" + loginPrefManager.getStringValue("driver_status"));

            if (loginPrefManager.getStringValue("driver_status").equals("3")) {
                showTwoButtonSnackbar();
            } else {
                if (auto_status.equals("1")) {
                    updateAutoTask(DeliforceConstants.TASK_ACCEPTED, "", 0);
                } else {
//                        changeStatus(DeliforceConstants.TASK_ACCEPTED);
                    upDateTask(DeliforceConstants.TASK_ACCEPTED, " ", 0);
                }
            }
        });
        acktask.setOnClickListener(view -> {
            ackBtnPressed = true;
            if (loginPrefManager.getStringValue("driver_status").equals("3")) {
                showTwoButtonSnackbar();
            } else {
                if (auto_status.equals("1")) {
                    updateAutoTask(DeliforceConstants.TASK_STATUS_ACKNOWLEDGE, "", 0);
                } else {
                    upDateTask(DeliforceConstants.TASK_STATUS_ACKNOWLEDGE, " ", 0);
                }
            }
        });
        btnDeclineNotification.setOnClickListener(view -> {
            if (loginPrefManager.getStringValue("driver_status").equals("3")) {
                showTwoButtonSnackbar();
            } else {
                if (auto_status.equals("1")) {
                    showCancleDailog(DeliforceConstants.TASK_DECLINED, 1);
                } else {
                    showCancleDailog(DeliforceConstants.TASK_DECLINED, 0);
                }
            }
        });

        imgBtnCloseNotification.setOnClickListener(view -> {
            Intent launchTaskListIntent = new Intent(NewTaskActivity.this, NavigationActivity.class);
            startActivity(launchTaskListIntent);
            finish();
        });

        btnGlympseTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchTaskListIntent = new Intent(NewTaskActivity.this, NavigationActivity.class);
                startActivity(launchTaskListIntent);
                finish();

            }
        });
    }

    /*private void glympseTaskAction(int taskStatus) {
        show_loader();

        if (DeliforceConstants.TASK_STARTED == taskStatus) {
            //set a tassk in started mode
            int taskId = Integer.parseInt(glympseTaskID);
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

        }
    }*/

    /*private void startGlympseTask() {
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
                switch (auto_status) {
                    case "1":
                        updateAutoTask(DeliforceConstants.TASK_STARTED, "");

                        break;
                    *//*This is for direct start*//*
                    case "0":
                        upDateTask(DeliforceConstants.TASK_STARTED, " ");

                        break;
                    default:

                        upDateTask(DeliforceConstants.TASK_STARTED, " ");

                        break;
                }


            }


        } else {

            EnRouteWrapper.instance().manager().getTaskManager().startTask(glympTask);
            EnRouteWrapper.instance().manager().getTaskManager().setTaskPhase(glympTask,
                    EnRouteConstants.PHASE_PROPERTY_LIVE());
        }
    }*/

    private void refresh() {
        try {
            EnRouteWrapper.instance().manager().getTaskManager().refresh();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
    }

    @SuppressLint("SetTextI18n")
    private void init() {

//        Intent notificationIntent = getIntent();
//        if (notificationIntent != null) {
//            String customerName = notificationIntent.getStringExtra("cust_name");
//            String custAddress = notificationIntent.getStringExtra("cust_addres");
//            String taskdate = notificationIntent.getStringExtra("task_date");
//            String taskEndDate = notificationIntent.getStringExtra("taskEndDate");
//            String taskStartTime = notificationIntent.getStringExtra("task_completion_time");
//            taskId = notificationIntent.getStringExtra("super_task_id");
//            String ackType = notificationIntent.getStringExtra("acknowledgementType");
//            String taskEndTime = notificationIntent.getStringExtra("taskEndTime");
//            int bussType = notificationIntent.getIntExtra("bussId", 0);

        if (!taskNotificationModels.isEmpty()) {
            int acknowledgeType = taskNotificationModels.get(0).getAcknowledgementType();
            auto_status = taskNotificationModels.get(0).getAuto();


            Log.e("taskData",new Gson().toJson(taskNotificationModels));

            Log.e("acknowledge_type",String.valueOf(acknowledgeType));


            //checking acknowledgement type and show visibility of views
            if (acknowledgeType == DeliforceConstants.ACK_TYPE_ACKNOWLEDGEMENT) {
//                taskEndDateTime.setVisibility(View.GONE);
                layoutAckTask.setVisibility(View.VISIBLE);
                layoutAcceptTask.setVisibility(View.GONE);
                layoutStartType.setVisibility(View.GONE);
                layoutDeclineTask.setVisibility(View.GONE);
                layoutGlympseTask.setVisibility(View.GONE);
            } else if (acknowledgeType == DeliforceConstants.ACK_TYPE_ACCEPT_DECLINE) {
                layoutAcceptTask.setVisibility(View.VISIBLE);
                layoutDeclineTask.setVisibility(View.VISIBLE);
                layoutAckTask.setVisibility(View.GONE);
                layoutStartType.setVisibility(View.GONE);
                layoutGlympseTask.setVisibility(View.GONE);
            } else if (acknowledgeType == DeliforceConstants.ACK_TYPE_START) {
                layoutAckTask.setVisibility(View.GONE);
                layoutAcceptTask.setVisibility(View.GONE);
                layoutDeclineTask.setVisibility(View.GONE);
                if (loginPrefManager.getGlympseEnabled()) {
                    layoutGlympseTask.setVisibility(View.VISIBLE);
                    layoutStartType.setVisibility(View.GONE);
                    Log.e("new_task_enable","true");
                } else {
                    Log.e("new_task_enable","false");
                    layoutStartType.setVisibility(View.GONE);
                    layoutGlympseTask.setVisibility(View.GONE);
                }
            } else if (acknowledgeType == DeliforceConstants.AUTO_ALLOCATION_ONE_BY_ONE) {
                layoutAcceptTask.setVisibility(View.VISIBLE);
                layoutDeclineTask.setVisibility(View.VISIBLE);
                layoutAckTask.setVisibility(View.GONE);
                layoutStartType.setVisibility(View.GONE);
                layoutGlympseTask.setVisibility(View.GONE);
                startTimer();
            } else if (acknowledgeType == DeliforceConstants.AUTO_ALLOCATION_NEAREST_AVAILABLE) {
                layoutAcceptTask.setVisibility(View.VISIBLE);
                layoutDeclineTask.setVisibility(View.GONE);
                layoutAckTask.setVisibility(View.GONE);
                layoutStartType.setVisibility(View.GONE);
                layoutGlympseTask.setVisibility(View.GONE);
                startTimer();
            } else {
//                taskEndDateTime.setVisibility(View.GONE);
                layoutAckTask.setVisibility(View.GONE);
                layoutAcceptTask.setVisibility(View.GONE);
                layoutDeclineTask.setVisibility(View.GONE);
                layoutGlympseTask.setVisibility(View.GONE);

//                new Handler().postDelayed(() -> changeStatus(DeliforceConstants.TASK_STATUS_ACKNOWLEDGE), 2000);
            }
        } else {
            finish();
        }

//        }

//        AWSIoTConfig.getIntaskNotificationModelsstance().initContext(this);
//        AWSIoTConfig.getInstance().initAWSIot();
//        mqttManager = AWSIoTConfig.getInstance().connectWithMQTT();


//        String cognitoIdToken = loginPrefManager.getCogintoToken();
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(DeliforceConstants.ACCEPT_NOTIFICATION_TIMER, DeliforceConstants.NOTIFICATION_COUNTDOWN_INTERVAL) {
            public void onTick(long millisUntilFinished) {
                btnAcceptTask.setText(String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished)));
            }

            public void onFinish() {
            }
        };
        countDownTimer.start();
    }

    //cancel timer
    void cancelTimer() {
        if (countDownTimer != null)
            countDownTimer.cancel();
    }

    private void upDateTask(int position, String reason, int currentPosition) {
        /* if (showLoader) {*/
        show_loader();
      /*  }else{
            showLoader =false;
        }*/
        UpdateTaskStatus updateTaskStatus = new UpdateTaskStatus();
        updateTaskStatus.setTaskId(taskNotificationModels.get(currentPosition).get_id());
        updateTaskStatus.setPickup(taskNotificationModels.get(currentPosition).getIsPickUp());
        updateTaskStatus.setStatus(position);
        updateTaskStatus.setCancelTaskReason(reason);
        updateTaskStatus.setTopic("task");
        updateTaskStatus.setImgUrl(loginPrefManager.getStringValue("image_url"));
        updateTaskStatus.setDriver_name(loginPrefManager.getStringValue("first_name"));
        if (!loginPrefManager.getStringValue("driver_status").isEmpty())
            updateTaskStatus.setDriverStatus(Integer.parseInt(loginPrefManager.getStringValue("driver_status")));
        updateTaskStatus.setManager_id(loginPrefManager.getManagerID());

        updateLat = user_lat;
        updateLng = user_lng;

        if (updateLat == 0.0 && current_latitude != 0.0) {
            updateLat = current_latitude;
            updateLng = current_longitude;
        } /*else if (updateLat == 0 && gpsTracker.canGetLocation()) {
            updateLat = gpsTracker.getLatitude();
            updateLng = gpsTracker.getLongitude();
        }*/

        Timber.e("driver_latitude" + "------" + updateLat);
        Timber.e("driver_longitude" + "------" + updateLng);

        updateTaskStatus.setStartLat(updateLat);
        updateTaskStatus.setStartLng(updateLng);

        String formattedAddress;
        if (updateLat != 0 && updateLat != 0.0)
            formattedAddress = AppUtils.getCompleteAddressString(this, updateLat, updateLng);
        else
            formattedAddress = AppUtils.getCompleteAddressString(this, loginPrefManager.getDoubleLatitude(), loginPrefManager.getDoubleLongitude());

        updateTaskStatus.setAddress(formattedAddress);

        Set manager = loginPrefManager.getAdminList();
        List<String> mainList = new ArrayList<>(manager);
        updateTaskStatus.setAdminArray(mainList);
        Timber.e("task_id%s", updateTaskStatus.getTaskId());
        Timber.e("status" + "-" + updateTaskStatus.getStatus());
        Timber.e("topic%s", updateTaskStatus.getTopic());
        if (position == 4) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            updateTaskStatus.setStartedTime(sdf.format(new Date()));
        } else if (position == 6) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            updateTaskStatus.setTaskSuccessCompletionTime(sdf.format(new Date()));
        }
        if (position == DeliforceConstants.TASK_STATUS_CANCELLED || position == DeliforceConstants.TASK_FAILED || position == DeliforceConstants.TASK_DECLINED) {
            updateTaskStatus.setCancelTaskReason(reason);
            if(taskNotificationModels.get(0).getReferenceId()!=null)
                updateTaskStatus.setReferenceId(taskNotificationModels.get(0).getReferenceId());
        }

       /* if (loginPrefManager.getGlympseEnabled()&& glympseTaskID!=null) {
            updateTaskStatus.setGlympseTrackingURL(glympTask.getOperation().getInviteUrl());
        }*/

        int batteryPercent = AppUtils.getBatteryPercentage(this);
        updateTaskStatus.setBatteryStatus(batteryPercent);

        try {

            apiService.updatetaskDetails(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken(), updateTaskStatus).enqueue(new Callback<OTPTaskUpdate>() {
                @Override
                public void onResponse(@NotNull Call<OTPTaskUpdate> call, @NotNull Response<OTPTaskUpdate> response) {

                    try {
                        if (response.raw().code() == 200) {
                            int currentTaskPosition = currentPosition;
                            if (currentTaskPosition == taskNotificationModels.size() - 1) {
                                dismiss_loader();
                                startActivity(new Intent(NewTaskActivity.this, NavigationActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                finish();

                                if (isMyServiceRunning(PushNotificationService.class)) {
                                    stopService(new Intent(NewTaskActivity.this, PushNotificationService.class));
                                }
                            } else {
                                currentTaskPosition++;
                                upDateTask(position, reason, currentTaskPosition);
                            }
                        }
                        //alert dialog for user already logged in
                        else if (response.code() == 494) {
                            dismiss_loader();
                            showAlertDialog(NewTaskActivity.this);
                        } else if (response.code() == 449) {
                            dismiss_loader();
                            finish();
                        } else {
                            dismiss_loader();
                            show_error_response(getString(R.string.error_response));
                        }
                    } catch (Exception e) {
                        dismiss_loader();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<OTPTaskUpdate> call, Throwable t) {
                    dismiss_loader();
                    Timber.e("onFailure" + t.getMessage());
                }
            });

        } catch (Exception e) {
            dismiss_loader();
            Timber.e("Exception" + e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
        //remove the listener when task is completed
        //otherwise it will add all task
       /* if (loginPrefManager.getGlympseEnabled()&& glympseTaskID!=null) {
            EnRouteWrapper.instance().manager().removeListener(this);
            EnRouteWrapper.instance().manager().getTaskManager().removeListener(this);


        }*/
    }

    private void showTwoButtonSnackbar() {
        LinearLayout.LayoutParams objLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        snackbar = Snackbar.make(this.findViewById(android.R.id.content), getString(R.string.app_name), Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        int navbarHeight = getNavBarHeight(this);
        FrameLayout.LayoutParams parentParams = (FrameLayout.LayoutParams) layout.getLayoutParams();
        parentParams.setMargins(0, 0, 0, 0 - navbarHeight + 100);
        layout.setLayoutParams(parentParams);
        layout.setPadding(0, 0, 0, 0);
        layout.setLayoutParams(parentParams);
        View snackView = getLayoutInflater().inflate(R.layout.custom_snack_bar, null);
        tv_duty_status = snackView.findViewById(R.id.tv_duty_status);
        status_switch = snackView.findViewById(R.id.sw_driver_duty_status);
        change_status();
        layout.addView(snackView, objLayoutParams);
        snackbar.show();
    }

    private void change_status() {
        status_switch.setOnCheckedChangeListener((compoundButton, status) -> {

            if (status) {
                tv_duty_status.setText(getString(R.string.nav_online));
                tv_duty_status.setTextColor(getResources().getColor(R.color.text_color));
                //updateDriverStatus(DeliforceConstants.DRIVER_STATUS_IDLE);
            }
        });
    }

//    private void updateDriverStatus(int position) {
//        try {
//            driverStatus = new DriverStatus();
//            driverStatus.setTimezone(TimeZone.getDefault().getID());
//            driverStatus.setDriverStatus(position);
//            driverStatus.setTopic("driver");
//            driverStatus.setDriver_id(loginPrefManager.getDriverID());
//            if(position == DeliforceConstants.DRIVER_STATUS_IDLE || position == DeliforceConstants.DRIVER_STATUS_OFFLINE) {
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
//                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
//                driverStatus.setOfflineDate(sdf.format(new Date()));
//            }
//            Set manager = loginPrefManager.getAdminList();
//
//            List<String> mainList = new ArrayList<>(manager);
//            driverStatus.setAdminArray(mainList);
//
//            updateLat = user_lat;
//            updateLng = user_lng;
//
//            if (updateLat == 0.0 && current_latitude != 0.0) {
//                updateLat = current_latitude;
//                updateLng = current_longitude;
//            }
//
//            Timber.e("driver_latitude" + "------" + updateLat);
//            Timber.e("driver_longitude" + "------" + updateLng);
//
//            driverStatus.setCurrentLat(updateLat);
//            driverStatus.setCurrentLan(updateLng);
//
//            /*if (gpsTracker.canGetLocation()) {
//                driverStatus.setCurrentLan(gpsTracker.getLatitude());
//                driverStatus.setCurrentLan(gpsTracker.getLongitude());
//            }*/
//
//            show_loader();
//            apiService.updateDriverStatus(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken(), driverStatus).enqueue(new Callback<DriverStatusUpdate>() {
//                @Override
//                public void onResponse(@NotNull Call<DriverStatusUpdate> call, @NotNull Response<DriverStatusUpdate> response) {
//
//                    dismiss_loader();
//
//                    try {
//                        if (response.raw().code() == 200) {
//                            loginPrefManager.setStringValue("driver_status", String.valueOf(position));
//                            //                        publishData(position);
//                            snackbar.dismiss();
//                        } else if (response.raw().code() == 401) {
//                            findCurrent();
//                        }//alert dialog for user already logged in
//                        else if (response.code() == 494) {
//                            showAlertDialog(NewTaskActivity.this);
//                        } else {
//                            show_error_response(getString(R.string.error_response));
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onFailure(@NotNull Call<DriverStatusUpdate> call, @NotNull Throwable t) {
//                    dismiss_loader();
//                }
//            });
//
//        } catch (Exception e) {
//            dismiss_loader();
//            Timber.e("Exception%s", e.getMessage());
//        }
//    }


    private void updateAutoTask(int position, String msg, final int currentPosition) {
        try {
            /*  if (showLoader) {*/
            show_loader();
            /*}else{
                showLoader=true;
            }*/
            AutoAllocationData autoAllocationData = new AutoAllocationData();
            autoAllocationData.setId(taskNotificationModels.get(currentPosition).get_id());
            autoAllocationData.setTaskStatus(position);
            autoAllocationData.setImgUrl(loginPrefManager.getStringValue("image_url"));
            autoAllocationData.setDriver_name(loginPrefManager.getStringValue("first_name"));
            autoAllocationData.setReason(msg);
            Set manager = loginPrefManager.getAdminList();
            List<String> mainList = new ArrayList<>(manager);
            autoAllocationData.setAdminArray(mainList);

           /* if (position==DeliforceConstants.TASK_STARTED){
                if (loginPrefManager.getGlympseEnabled()&& glympseTaskID!=null){
                    autoAllocationData.setGlympseTrackingURL(glympTask.getOperation().getInviteUrl());
                }

            }*/


            apiService.updateAutoAllocationtask(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken(), autoAllocationData).enqueue(new Callback<AutoAllocationStatus>() {
                @Override
                public void onResponse(@NotNull Call<AutoAllocationStatus> call, @NotNull Response<AutoAllocationStatus> response) {

                    try {
                        if (response.raw().code() == 200) {
                            int currentTaskPosition = currentPosition;
                            if (currentTaskPosition == taskNotificationModels.size() - 1) {
                                dismiss_loader();
                                if (position == DeliforceConstants.ACK_TYPE_ACKNOWLEDGEMENT || position == DeliforceConstants.TASK_ACCEPTED) {
                                    LocalBroadcastManager.getInstance(NewTaskActivity.this).sendBroadcast(new Intent("service_status").putExtra("server_profile", "100"));
                                }
                                startActivity(new Intent(NewTaskActivity.this, NavigationActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                finish();

                                if (isMyServiceRunning(PushNotificationService.class)) {
                                    stopService(new Intent(NewTaskActivity.this, PushNotificationService.class));
                                }
                            } else {
                                currentTaskPosition++;
                                updateAutoTask(position, msg, currentTaskPosition);
                            }

                        } else if (response.raw().code() == 302) {
                            dismiss_loader();
                            showShortMessage(getString(R.string.task_accept_error));
                            stopService(new Intent(NewTaskActivity.this, PushNotificationService.class));
                            LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(new Intent("receiver").putExtra("send_data", "1"));
                        } else if (response.raw().code() == 401) {
                            dismiss_loader();
                            findCurrent();


                        } else {
                            dismiss_loader();
                            showShortMessage(getString(R.string.error_response));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        dismiss_loader();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<AutoAllocationStatus> call, @NotNull Throwable t) {

                    dismiss_loader();
                    showShortMessage(t.getMessage());
                }
            });


        } catch (Exception e) {
            dismiss_loader();
            Timber.e("Exception%s", e.getMessage());
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Timber.e("isMyServiceRunning?" + true + "");
                return true;
            }
        }
        Timber.i("isMyServiceRunning?" + false + "");
        return false;
    }

    private void showCancleDailog(int newStatus, int type) {

        cancelDialog = new CancelDialog(NewTaskActivity.this, R.style.MyDialogStyle,(status, msg,reasonId) -> {

            if (status) {
                cancelDialog.dismiss();

                if (type == 1) {
                    updateAutoTask(newStatus, msg, 0);
                } else {
                    upDateTask(newStatus, msg, 0);
                }

            } else {
                cancelDialog.dismiss();
            }

        },newStatus);

        cancelDialog.setCanceledOnTouchOutside(true);
        cancelDialog.show();

    }


    /**
     * GListener section
     */
    /*public void eventsOccurred(GSource source, int listener, int events, Object param1,
                               Object param2) {
        Log.e("newTaskevent", "newTaskEven");
        if (EE.LISTENER_TASKS == listener) {
            if (0 != (EE.TASKS_TASK_LIST_CHANGED & events)) {
                System.out.print("update Task");


            }

            if ((0 != (EE.TASKS_TASK_STARTED & events))) {
                System.out.print("update live status");
                Log.e("Newuupdate_started", "TASKS_TASK_STARTED");
                Log.e("auto_status", "auto_status");
                Log.e("Taskstartedauto_status", auto_status);

                showLoader = false;

                switch (0) {
                    case "1":
                        updateAutoTask(DeliforceConstants.TASK_STARTED, "");

                        break;
                    *//*This is for direct start*//*
                    case "0":
                        upDateTask(DeliforceConstants.TASK_STARTED, " ");

                        break;
                    default:

                        upDateTask(DeliforceConstants.TASK_STARTED, " ");

                        break;
                }
            }

            if ((0 != (EE.TASKS_TASK_START_FAILED & events))) {
                System.out.print("task failed");
            }

            if ((0 != (EE.TASKS_OPERATION_COMPLETED & events))) {
                System.out.print("complete Task");

            }
            if ((0 != (EE.TASKS_OPERATION_COMPLETION_FAILED & events))) {
                Log.e("Newuupdate_failed", "TASKS_OPERATION_COMPLETION_FAILED");
                showShortMessage("Glympse task completion failed, Please try again");
            }


            if ((0 != (EE.TASKS_TASK_PHASE_CHANGED & events))) {

                Log.e("Newupdate_PHASE_CHANGED", "TASKS_TASK_PHASE_CHANGED");

                if (BuildConfig.DEBUG)
                    showShortMessage("Glympse TASKS_TASK_PHASE_CHANGED");
            }


        }

    }*/
}



