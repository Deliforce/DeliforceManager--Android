package com.app.deliforcemanager.Activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.app.deliforcemanager.LocalizationActivity.LocalizationActivity;
import com.app.deliforcemanager.ModelClass.CreateTask.CreateNewTask;
import com.app.deliforcemanager.ModelClass.CreateTask.DriverImageOption;
import com.app.deliforcemanager.ModelClass.TaskBarCode.BarCodeAPi;
import com.app.deliforcemanager.ModelClass.TaskDetailsApi.OTPTaskUpdate;
import com.app.deliforcemanager.ModelClass.TaskRoutesApi.Address;
import com.app.deliforcemanager.ModelClass.TaskRoutesApi.Geometry;
import com.app.deliforcemanager.ModelClass.TaskRoutesApi.Location;
import com.app.deliforcemanager.R;
import com.app.deliforcemanager.RawHeaders.TaskInfo.TaskBarCode;
import com.app.deliforcemanager.RawHeaders.TaskInfo.UpdateTaskStatus;
import com.app.deliforcemanager.Utils.AppUtils;
import com.app.deliforcemanager.Utils.DeliforceConstants;
import com.google.gson.Gson;
import com.google.zxing.Result;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.app.deliforcemanager.Activities.TaskDetailsActivity.BAR_CODE;

public class BarCodeActivity extends LocalizationActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;

    String task_id,barcode_task_id ="";
    String field_name;
    String orderValue;
    boolean fortemplate;
    String from = "task";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(BarCodeActivity.this);
        setContentView(mScannerView);

        from = Objects.requireNonNull(getIntent().getExtras()).getString("from", "task");
        if(from.equals("task")) {
            barcode_task_id = Objects.requireNonNull(getIntent().getExtras()).getString("barcode_task_id", "");
            field_name = Objects.requireNonNull(getIntent().getExtras()).getString("field_name", "");
            orderValue = Objects.requireNonNull(getIntent().getExtras()).getString("order_value", "");
            task_id = Objects.requireNonNull(getIntent().getExtras()).getString("task_id", "");
            fortemplate = getIntent().getExtras().getBoolean("fortemplate", false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        AskCameraPermissions();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        mScannerView.stopCamera();
        if(from.equals("task")) {
            if (!fortemplate) {
                if(!loginPrefManager.isWayBillEnabled())
                    send_bar_code(rawResult.getText());
                else {
                    if(barcode_task_id.equals(rawResult.getText())) {
                        send_bar_code(rawResult.getText());
                    } else {
                        showShortToastMessage(getString(R.string.invalid_barcode));
                        mScannerView.startCamera();
                        mScannerView.resumeCameraPreview(BarCodeActivity.this::handleResult);
                    }
                }
            }
            else
                sendBarCodeTemplate(rawResult.getText());
        } else {
            createNewDeliveryTask(rawResult.getText());
        }
    }

    private void createNewDeliveryTask(String barcodeValue) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        final String dateTimeFormat = "dd-MMM-yyyy hh:mm a";
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY,3);

        CreateNewTask createNewTask = new CreateNewTask();
        createNewTask.setName("Guest");
        createNewTask.setPhone("+91 9876543210");
        createNewTask.setEmail("guest@mailinator.com");
        createNewTask.setOrderId(barcodeValue);

        Address address = new Address();
        String driverAddress = AppUtils.getCompleteAddressString(BarCodeActivity.this, loginPrefManager.getDoubleLatitude(), loginPrefManager.getDoubleLongitude());
        if(driverAddress.isEmpty())
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

        apiService.createNewTask(loginPrefManager.getCogintoToken(), createNewTask).enqueue(new Callback<CreateNewTask>() {
            @Override
            public void onResponse(@NotNull Call<CreateNewTask> call, @NotNull Response<CreateNewTask> response) {
                dismiss_loader();
                try {
                    if (response.code() == 200) {
                        show_error_response(response.body().getMessage());
                        setResult(RESULT_OK,new Intent());
                        Toast.makeText(BarCodeActivity.this, getString(R.string.task_created), Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (response.raw().code() == 401) {
                        findCurrent();
                    } else if (response.raw().code() == 429) {
                        show_error_response(getString(R.string.error_exist_mobile));
                    } else if (response.code() == 494) {
                        showAlertDialog(BarCodeActivity.this);
                    } else if(response.code() == 421) {
                        show_error_response(response.errorBody().string());
                        mScannerView.startCamera();
                        mScannerView.resumeCameraPreview(BarCodeActivity.this::handleResult);

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

    private void sendBarCodeTemplate(String value) {
        show_loader();

        try {
            UpdateTaskStatus updateTaskStatus = new UpdateTaskStatus();
            updateTaskStatus.setTaskId(task_id);
            updateTaskStatus.setStatus(DeliforceConstants.TASK_TEMPLATE_STATUS);
            updateTaskStatus.setDriverStatus(Integer.parseInt(loginPrefManager.getStringValue("driver_status")));
            updateTaskStatus.setFieldName(field_name);
            updateTaskStatus.setFieldValues(value);
            updateTaskStatus.setOrder(Integer.parseInt(orderValue));
            updateTaskStatus.setManager_id(loginPrefManager.getManagerID());
            updateTaskStatus.setFieldSelectedValues(null);
            updateTaskStatus.setDataType(BAR_CODE);

            Set manager = loginPrefManager.getAdminList();
            List<String> mainList = new ArrayList<>(manager);
            updateTaskStatus.setAdminArray(mainList);
            Log.e("BarCodeTaskStatus", "---------"+new Gson().toJson(updateTaskStatus));

            //apiService.updatetaskDetails(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken(), updateTaskStatus).enqueue(new Callback<TaskUpdate>() {
            apiService.updatetaskDetails(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken(), updateTaskStatus).enqueue(new Callback<OTPTaskUpdate>() {
                @Override
                public void onResponse(@NotNull Call<OTPTaskUpdate> call, @NotNull Response<OTPTaskUpdate> response) {
                    dismiss_loader();
                    try {
                        if (response.raw().code() == 200) {
                            showShortMessage(getString(R.string.bar_code_success));
                            finish();
                        } else if (response.code() == 494) {
                            showAlertDialog(BarCodeActivity.this);
                        } else {
                            mScannerView.startCamera();
                            mScannerView.resumeCameraPreview(BarCodeActivity.this::handleResult);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<OTPTaskUpdate> call, @NotNull Throwable t) {
                    dismiss_loader();
                    Timber.e(t);
                }
            });

        } catch (Exception e) {
            Timber.e(e);
            dismiss_loader();
        }
    }

    private void send_bar_code(String value) {
        show_loader();

        try {
            TaskBarCode taskBarCode = new TaskBarCode();
            taskBarCode.setBarcode(value);
            taskBarCode.setTaskId(task_id);

            Log.e("taskBarCode", "---------"+new Gson().toJson(taskBarCode));

            apiService.sendBarCodeValue(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken(), taskBarCode).enqueue(new Callback<BarCodeAPi>() {
                @Override
                public void onResponse(@NotNull Call<BarCodeAPi> call, @NotNull Response<BarCodeAPi> response) {
                    dismiss_loader();
                    try {
                        if (response.raw().code() == 200) {
                            showShortMessage(getString(R.string.bar_code_success));
                            finish();
                        }
                        //alert dialog for user already logged in
                        else if (response.code() == 494) {
                            showAlertDialog(BarCodeActivity.this);
                        } else {
                            mScannerView.startCamera();
                            mScannerView.resumeCameraPreview(BarCodeActivity.this::handleResult);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<BarCodeAPi> call, @NotNull Throwable t) {
                    dismiss_loader();
                    Timber.e(t);
                }
            });

        } catch (Exception e) {
            Timber.e(e);
            dismiss_loader();
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
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
        mScannerView.resumeCameraPreview(BarCodeActivity.this::handleResult);
    }

    private void onNeverAskAgainProfile() {
        showNointerntView(getString(R.string.enable_camera_permission));
    }

    private void onDeniedProfile() {
        showNointerntView(getString(R.string.onDenied));
    }

}
