package com.app.deliforcemanager.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.app.deliforcemanager.Adapter.VehicleDialog;
import com.app.deliforcemanager.BaseActivity.BaseDrawerActivity;
import com.app.deliforcemanager.BuildConfig;
import com.app.deliforcemanager.CustomDialogView.ChooseLanguageDialog;
import com.app.deliforcemanager.CustomDialogView.ChooseMapDialog;
import com.app.deliforcemanager.CustomDialogView.ChooseMapStyle;
import com.app.deliforcemanager.CustomDialogView.ChooseVibrationStyleDialog;
import com.app.deliforcemanager.ModelClass.SettingsApi.NavigationTypes;
import com.app.deliforcemanager.ModelClass.SettingsApi.SettingsDetails;
import com.app.deliforcemanager.ModelClass.SettingsApi.SettingsInfo;
import com.app.deliforcemanager.ModelClass.StatusApi.DefalutStatus;
import com.app.deliforcemanager.ModelClass.StatusApi.VehicleType;
import com.app.deliforcemanager.R;
import com.app.deliforcemanager.Utils.DeliforceConstants;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.app.deliforcemanager.Activities.TaskDetailsActivity.CODE_DRAW_OVER_OTHER_APP_PERMISSION;


public class SettingsActivity extends BaseDrawerActivity {

    private static final String TAG = "SettingsActivity";

    TextView titleChooseVehicle, tvVehicleType, tvRingtoneName, vibrationAlert, appLanguage, mapStyle, tvAppVersion, tvNavigationApp;
    RelativeLayout layoutVehicleType, layoutRingtone, layoutVibrate, layoutChooseLang, lay_select_navigation,
            layoutMapStyle, layoutNavigationHelp;
    ImageView imageVehicleType, imageSelectVehicle;
    Switch switchRingRepeat, switchPowerSaving, switchNavigationHelper;
    ChooseVibrationStyleDialog chooseVibrationStyleDialog;
    ChooseLanguageDialog languageDialog;
    ChooseMapDialog chooseMapDialog;
    ChooseMapStyle mapStyleDialog;
    Button btnUpdateSettings, btnClearFields;
    String selectedMap = DeliforceConstants.NavigationGoogleMap;
    SettingsDetails settingsDetails;
    SettingsInfo settingsInfo, oldSettingInfo;
    VehicleDialog vehicleTypeDialog;
    List<VehicleType> vehicle_list;
    public ArrayList<NavigationTypes> tempNavigationTypes;
    public ArrayList<NavigationTypes> navigationTypes;
    int map_style;
    int vehicle_type;
    int rigntone_type;
    int vibration_type;
    int langauge_type;
    int navigation_type;
    boolean isLanguageChanged;
    String currentLanguage = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        vehicle_list = new ArrayList<>();

        initToolbar();
        initViews();

        getSettingsDetails();

        getStatus();
    }

    private void initToolbar() {


        tvTitle.setText(getString(R.string.nav_settings));

    }

    private void initViews() {

        tvNavigationApp = findViewById(R.id.tv_navigation_app);
        tvAppVersion = findViewById(R.id.tv_app_version);
        titleChooseVehicle = findViewById(R.id.settings_vehicle_title);
        imageVehicleType = findViewById(R.id.img_vehicle_type);
        imageSelectVehicle = findViewById(R.id.select_vehicle_type);
        layoutVehicleType = findViewById(R.id.layout_vehicleType);
        layoutRingtone = findViewById(R.id.layoutSetRingTone);
        tvVehicleType = findViewById(R.id.tv_driver_vehicle_type);
        tvRingtoneName = findViewById(R.id.tv_setting_ringtone);
        vibrationAlert = findViewById(R.id.settings_vibrate);
        layoutVibrate = findViewById(R.id.layoutVibration);
        layoutNavigationHelp = findViewById(R.id.layout_nav_helper);
        btnUpdateSettings = findViewById(R.id.btn_settings_apply);
        btnClearFields = findViewById(R.id.btn_settings_clear);
        settingsDetails = new SettingsDetails();
        settingsInfo = new SettingsInfo();
        switchRingRepeat = findViewById(R.id.sw_repeat_ring);
        switchPowerSaving = findViewById(R.id.sw_power_saving);
        switchNavigationHelper = findViewById(R.id.sw_nav_helper);
        layoutChooseLang = findViewById(R.id.lay_select_lang);
        lay_select_navigation = findViewById(R.id.lay_select_navigation);
        layoutMapStyle = findViewById(R.id.lay_map_style);
        appLanguage = findViewById(R.id.tv_selected_lang);
        mapStyle = findViewById(R.id.tv_style_of_map);

        onClickEvents();
    }

    private void onClickEvents() {

        layoutVehicleType.setOnClickListener(view -> {

            vehicleTypeDialog = new VehicleDialog(SettingsActivity.this, R.style.MyDialogStyle, vehicle_type, vehicle_list, new VehicleDialog.DialogInterface() {
                @Override
                public void vehicle_dialog(boolean status, int postion, String value) {

                    Timber.e(postion + "/" + value);
                    tvVehicleType.setText(value);
                    imageVehicleType.setVisibility(View.VISIBLE);
                    setImageView(postion);
                    vehicle_type = postion;

                    settingsDetails.setSettingsInfo(settingsInfo);
                    vehicleTypeDialog.dismiss();
                }
            });
            vehicleTypeDialog.show();

        });


        layoutRingtone.setOnClickListener(v -> {
            Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
            if (loginPrefManager.getStringValue("ringtoneUri").isEmpty())
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
            else
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Uri.parse(loginPrefManager.getStringValue("ringtoneUri")));
            startActivityForResult(intent, 1);
        });

        layoutVibrate.setOnClickListener(v -> {
            chooseVibrationStyleDialog = new ChooseVibrationStyleDialog(SettingsActivity.this, R.style.MyDialogStyle, new ChooseVibrationStyleDialog.SelectVibrationStyleListener() {
                @Override
                public void onLongClick() {
                    vibrationAlert.setText(getString(R.string.vibration_alert_long));
                    settingsInfo.setVibration(1);
                    settingsDetails.setSettingsInfo(settingsInfo);
                    loginPrefManager.setIntValue(DeliforceConstants.PREF_SETTINGS_VIBRATE_LONG, 1);

                    Timber.e("Pref Vibrate Value--> %s", loginPrefManager.getIntValue(DeliforceConstants.PREF_SETTINGS_VIBRATE_LONG));
                    chooseVibrationStyleDialog.dismiss();
                    vibration_type = 1;
                }

                @Override
                public void onSystemClick() {
                    vibrationAlert.setText(getString(R.string.vibration_alert_system));
                    settingsInfo.setVibration(2);
                    settingsDetails.setSettingsInfo(settingsInfo);
                    loginPrefManager.setIntValue(DeliforceConstants.PREF_SETTINGS_VIBRATE_LONG, 2);
                    Timber.e("Pref Vibrate Value--> %s", loginPrefManager.getIntValue(DeliforceConstants.PREF_SETTINGS_VIBRATE_SYSTEM));
                    chooseVibrationStyleDialog.dismiss();
                    vibration_type = 2;
                }
            });
            chooseVibrationStyleDialog.setCanceledOnTouchOutside(false);
            chooseVibrationStyleDialog.show();
        });

        layoutChooseLang.setOnClickListener(v -> {
            languageDialog = new ChooseLanguageDialog(SettingsActivity.this, R.style.MyDialogStyle, langauge_type,
                    new ChooseLanguageDialog.SelectAppLangChangeListener() {
                        @Override
                        public void onEnglishSelectClick() {
                            if(!currentLanguage.equalsIgnoreCase(DeliforceConstants.LANGUAGE_ENGLISH)) {
                                isLanguageChanged = true;
                            }
                            appLanguage.setText(getString(R.string.settings_language_eng));
                            langauge_type = Integer.parseInt(DeliforceConstants.LANGUAGE_ENGLISH);
                            settingsInfo.setLanguage(langauge_type);
                            settingsDetails.setSettingsInfo(settingsInfo);
                            languageDialog.dismiss();
                        }

                        @Override
                        public void onSpanishSelectClick() {
                            if(!currentLanguage.equalsIgnoreCase(DeliforceConstants.LANGUAGE_SPANISH)) {
                                isLanguageChanged = true;
                            }
                            appLanguage.setText(getString(R.string.settings_language_spanish));
                            langauge_type = Integer.parseInt(DeliforceConstants.LANGUAGE_SPANISH);
                            settingsInfo.setLanguage(langauge_type);
                            settingsDetails.setSettingsInfo(settingsInfo);
                            languageDialog.dismiss();
                        }

                        @Override
                        public void onFrenchSelectClick() {
                            if(!currentLanguage.equalsIgnoreCase(DeliforceConstants.LANGUAGE_FRENCH)) {
                                isLanguageChanged = true;
                            }
                            appLanguage.setText(getString(R.string.settings_language_french));
                            langauge_type = Integer.parseInt(DeliforceConstants.LANGUAGE_FRENCH);
                            settingsInfo.setLanguage(langauge_type);
                            settingsDetails.setSettingsInfo(settingsInfo);
                            languageDialog.dismiss();
                        }

                        @Override
                        public void onPortugueseSelectClick() {
                            if(!currentLanguage.equalsIgnoreCase(DeliforceConstants.LANGUAGE_PORTUGUESE)) {
                                isLanguageChanged = true;
                            }
                            appLanguage.setText(getString(R.string.settings_language_portuguese));
                            langauge_type = Integer.parseInt(DeliforceConstants.LANGUAGE_PORTUGUESE);
                            settingsInfo.setLanguage(langauge_type);
                            settingsDetails.setSettingsInfo(settingsInfo);
                            languageDialog.dismiss();
                        }

                        @Override
                        public void onMalaySelectClick() {
                            if(!currentLanguage.equalsIgnoreCase(DeliforceConstants.LANGUAGE_MALAY)) {
                                isLanguageChanged = true;
                            }
                            appLanguage.setText(getString(R.string.settings_language_malay));
                            langauge_type = Integer.parseInt(DeliforceConstants.LANGUAGE_MALAY);
                            settingsInfo.setLanguage(langauge_type);
                            settingsDetails.setSettingsInfo(settingsInfo);
                            languageDialog.dismiss();
                        }

                        @Override
                        public void onArabicSelectClick() {
                            if(!currentLanguage.equalsIgnoreCase(DeliforceConstants.LANGUAGE_ARABIC)) {
                                isLanguageChanged = true;
                            }
                            appLanguage.setText(getString(R.string.settings_language_arabic));
                            langauge_type = Integer.parseInt(DeliforceConstants.LANGUAGE_ARABIC);
                            settingsInfo.setLanguage(langauge_type);
                            settingsDetails.setSettingsInfo(settingsInfo);
                            languageDialog.dismiss();
                        }
                    });

            languageDialog.setCanceledOnTouchOutside(false);
            languageDialog.show();
        });

        lay_select_navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tempNavigationTypes.size() == 2) {
                    chooseMapDialog = new ChooseMapDialog(SettingsActivity.this, R.style.MyDialogStyle, selectedMap,
                            new ChooseMapDialog.OnClickMapListener() {
                                @Override
                                public void onGoogleMapSelected() {
                                    for (int i = 0; i < tempNavigationTypes.size(); i++) {
                                        if (tempNavigationTypes.get(i).getMap().equalsIgnoreCase(DeliforceConstants.NavigationGoogleMap)) {
                                            tempNavigationTypes.get(i).setIsDefault(1);
                                            tvNavigationApp.setText(tempNavigationTypes.get(i).getMap());
                                            selectedMap = tempNavigationTypes.get(i).getMap();
                                        } else {
                                            tempNavigationTypes.get(i).setIsDefault(0);
                                        }
                                    }
                                }

                                @Override
                                public void onWazeSelected() {
                                    for (int i = 0; i < tempNavigationTypes.size(); i++) {
                                        if (tempNavigationTypes.get(i).getMap().equalsIgnoreCase(DeliforceConstants.NavigationWaze)) {
                                            tempNavigationTypes.get(i).setIsDefault(1);
                                            tvNavigationApp.setText(tempNavigationTypes.get(i).getMap());
                                            selectedMap = tempNavigationTypes.get(i).getMap();
                                        } else {
                                            tempNavigationTypes.get(i).setIsDefault(0);
                                        }
                                    }
                                }
                            });
                    chooseMapDialog.show();
                }
            }
        });

        layoutNavigationHelp.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 23) {
//
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                //startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
                if (!android.provider.Settings.canDrawOverlays(SettingsActivity.this)) {
                    startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
                    settingsInfo.setNavigationHelper(false);
                    settingsDetails.setSettingsInfo(settingsInfo);
                } else {
                    startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
                    settingsInfo.setNavigationHelper(true);
                    settingsDetails.setSettingsInfo(settingsInfo);
                }
            }

        });

        btnUpdateSettings.setOnClickListener(v -> updateSettings());

        btnClearFields.setOnClickListener(v -> clearSettingsData());


        layoutMapStyle.setOnClickListener(v -> {
            mapStyleDialog = new ChooseMapStyle(SettingsActivity.this, R.style.MyDialogStyle, map_style, new ChooseMapStyle.ChooseMapStyleListener() {
                @Override
                public void onNormalMapStyleClick() {
                    mapStyle.setText(getString(R.string.settings_map_style_normal));
                    settingsInfo.setMapStyle(1);
                    settingsDetails.setSettingsInfo(settingsInfo);
                    mapStyleDialog.dismiss();
                    map_style = 1;
                }

                @Override
                public void onNightModeStyleClick() {
                    mapStyle.setText(getString(R.string.settings_map_style_night));
                    settingsInfo.setMapStyle(2);
                    map_style = 2;
                    settingsDetails.setSettingsInfo(settingsInfo);
                    mapStyleDialog.dismiss();
                }
            });

            mapStyleDialog.setCanceledOnTouchOutside(false);
            mapStyleDialog.show();
        });
    }

    @SuppressLint("NewApi")
    private void setImageView(int postion) {
        switch (postion) {
            case 1:
                imageVehicleType.setImageDrawable(getDrawable(R.drawable.bike_gray));
                break;
            case 2:
                imageVehicleType.setImageDrawable(getDrawable(R.drawable.car_gray));
                break;
            case 3:
                imageVehicleType.setImageDrawable(getDrawable(R.drawable.truckicon));
                break;
            case 4:
                imageVehicleType.setImageDrawable(getDrawable(R.drawable.scooter_gray));
                break;
            case 5:
                imageVehicleType.setImageDrawable(getDrawable(R.drawable.cycle_gray));
                break;
            case 6:
                imageVehicleType.setImageDrawable(getDrawable(R.drawable.walk_gray));
                break;

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            if (requestCode == 1 && data != null) {
                try {
                    Uri ringUrl = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                    if (ringUrl != null) {
                        loginPrefManager.setStringValue("ringtoneUri", ringUrl.toString());
                        Timber.e("Ringtone Path%s", ringUrl.toString());
                        Ringtone ringtone = RingtoneManager.getRingtone(this, ringUrl);
                        String title = ringtone.getTitle(this);
                        Timber.e("Ringtone Name%s", title);
                        if (title.isEmpty())
                            title = getString(R.string.settings_ring_default);
                        tvRingtoneName.setText(title);
                        loginPrefManager.setStringValue("ringtoneSelected", title);
                    } else {
                        tvRingtoneName.setText(getString(R.string.settings_ring_default));
                        loginPrefManager.setStringValue("ringtoneUri", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION) + "");
                        loginPrefManager.setStringValue("ringtoneSelected", "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    }


    private void getSettingsDetails() {
        try {
            tvAppVersion.setText(BuildConfig.VERSION_NAME);
            show_loader();
            apiService.getSettingsDetails(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken()).enqueue(new Callback<SettingsDetails>() {
                @Override
                public void onResponse(@NotNull Call<SettingsDetails> call, @NotNull Response<SettingsDetails> response) {
                    dismiss_loader();

                    try {
                        if (response.code() == 200) {

                            int languageId = 0, vehicleType = 0, vibration = 0;
                            SettingsInfo settingsInfo = response.body().getSettingsInfo();
                            if (settingsInfo != null) {
                                if (settingsInfo.getVehicleType() != null) {
                                    vehicleType = settingsInfo.getVehicleType();
                                }
                                if (settingsInfo.getVibration() != null) {
                                    vibration = settingsInfo.getVibration();
                                }
                                if (settingsInfo.getLanguage() != null) {
                                    languageId = settingsInfo.getLanguage();
                                }
                                int mapStyleId = settingsInfo.getMapStyle();
                                boolean isRingRepeat = settingsInfo.getRepeat();
                                boolean isNavigationHelper = settingsInfo.getNavigationHelper();
                                boolean isPowerSavingMode = settingsInfo.getPowerSavingModel();
                                Timber.e("Got SettingsModel--> " + isRingRepeat + " " + " " + isPowerSavingMode + " " + isNavigationHelper);
                                oldSettingInfo = response.body().getSettingsInfo();

                                String ringtone = loginPrefManager.getStringValue("ringtoneSelected");

                                Timber.e(ringtone);
                                Timber.e(loginPrefManager.getStringValue("ringtoneUri"));
                                if (!ringtone.equals("")) {
                                    tvRingtoneName.setText(ringtone);
                                } else {
                                    tvRingtoneName.setText(getString(R.string.settings_ring_default));
                                }
                                map_style = response.body().getSettingsInfo().getMapStyle();
                                vehicle_type = response.body().getSettingsInfo().getVehicleType();
                                rigntone_type = response.body().getSettingsInfo().getRingtone();
                                vibration_type = response.body().getSettingsInfo().getVibration();
                                langauge_type = response.body().getSettingsInfo().getLanguage();
                                navigation_type = response.body().getSettingsInfo().getNavigation();
                                if (loginPrefManager.isNavigationTypeEnabled())
                                    navigationTypes = response.body().getSettingsInfo().getNavigationTypes();
                                if (navigationTypes == null)
                                    navigationTypes = new ArrayList<>();
                                tempNavigationTypes = new ArrayList<>();
                                tempNavigationTypes.addAll(navigationTypes);
                                selectedMap = loginPrefManager.getNavigation();
                                tvNavigationApp.setText(loginPrefManager.getNavigation());

                                File file = new File("/external_files/Android/media/com.google.android.talk/Ringtones/hangouts_incoming_call.ogg");
                                Timber.i("temp exists : %s", file.exists());
                                if (file.exists()) {
                                    Timber.e(file.getName());
                                }

                                loginPrefManager.setIntValue(DeliforceConstants.PREF_SETTINGS_VIBRATE_LONG, vibration_type);
                                Timber.e(String.valueOf(langauge_type));

                                if (map_style == 1) {
                                    mapStyle.setText(getString(R.string.settings_map_style_normal));
                                } else {
                                    mapStyle.setText(getString(R.string.settings_map_style_night));
                                }

                                if (vibration == 1) {
                                    vibrationAlert.setText(getString(R.string.vibration_alert_long));
                                } else {
                                    vibrationAlert.setText(getString(R.string.vibration_alert_system));
                                }

                                Timber.e("--%s", vehicleType);
                                if (vehicleType != 0) {
                                    //set vehicle type
                                    switch (vehicleType) {
                                        case 1:
                                            tvVehicleType.setText(getString(R.string.vehicle_type_bike));
                                            imageVehicleType.setImageDrawable(ContextCompat.getDrawable(SettingsActivity.this, R.drawable.bike_gray));
                                            break;
                                        case 2:
                                            tvVehicleType.setText(getString(R.string.vehicle_type_car));
                                            imageVehicleType.setImageDrawable(ContextCompat.getDrawable(SettingsActivity.this, R.drawable.car_gray));
                                            break;
                                        case 3:
                                            tvVehicleType.setText(getString(R.string.vehicle_type_truck));
                                            imageVehicleType.setImageDrawable(ContextCompat.getDrawable(SettingsActivity.this, R.drawable.truckicon));
                                            break;
                                        case 4:
                                            tvVehicleType.setText(getString(R.string.vehicle_type_scooter));
                                            imageVehicleType.setImageDrawable(ContextCompat.getDrawable(SettingsActivity.this, R.drawable.scooter_gray));
                                            break;
                                        case 5:
                                            tvVehicleType.setText(getString(R.string.vehicle_type_cycle));
                                            imageVehicleType.setImageDrawable(ContextCompat.getDrawable(SettingsActivity.this, R.drawable.cycle_gray));
                                            break;
                                        case 6:
                                            tvVehicleType.setText(getString(R.string.vehicle_type_walking));
                                            imageVehicleType.setImageDrawable(ContextCompat.getDrawable(SettingsActivity.this, R.drawable.walk_gray));
                                            break;

                                    }
                                }

                                if (isRingRepeat) {
                                    switchRingRepeat.setChecked(true);
                                } else {
                                    switchRingRepeat.setChecked(false);
                                }


                                if (isPowerSavingMode) {
                                    switchPowerSaving.setChecked(true);
                                } else {
                                    switchPowerSaving.setChecked(false);
                                }
                                if (isNavigationHelper) {
                                    switchNavigationHelper.setChecked(true);
                                } else {
                                    switchNavigationHelper.setChecked(false);
                                }

                                if (languageId == Integer.parseInt(DeliforceConstants.LANGUAGE_ENGLISH)) {
                                    appLanguage.setText(getString(R.string.settings_language_eng));
                                } else if (languageId == Integer.parseInt(DeliforceConstants.LANGUAGE_SPANISH)) {
                                    appLanguage.setText(getString(R.string.settings_language_spanish));
                                } else if (languageId == Integer.parseInt(DeliforceConstants.LANGUAGE_FRENCH)) {
                                    appLanguage.setText(getString(R.string.settings_language_french));
                                } else if (languageId == Integer.parseInt(DeliforceConstants.LANGUAGE_PORTUGUESE)) {
                                    appLanguage.setText(getString(R.string.settings_language_portuguese));
                                } else if (languageId == Integer.parseInt(DeliforceConstants.LANGUAGE_MALAY)) {
                                    appLanguage.setText(getString(R.string.settings_language_malay));
                                } else if (languageId == Integer.parseInt(DeliforceConstants.LANGUAGE_ARABIC)) {
                                    appLanguage.setText(getString(R.string.settings_language_arabic));
                                }
                                currentLanguage = languageId+"";
                            }
                        } else if (response.raw().code() == 401) {
                            findCurrent();
                        }    //alert dialog for user already logged in
                        else if (response.code() == 494) {
                            showAlertDialog(SettingsActivity.this);
                        } else {
                            show_error_response(getString(R.string.error_response));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<SettingsDetails> call, @NotNull Throwable t) {
                    dismiss_loader();
                    Timber.e("Get settings Error%s", t.toString());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void updateSettings() {

        try {

            settingsInfo.setRingtone(1);
            settingsInfo.setVibration(vibration_type);
            settingsInfo.setLanguage(langauge_type);
            settingsInfo.setNavigation(1);
            settingsInfo.setMapStyle(map_style);
            settingsInfo.setVehicleType(vehicle_type);

            if (switchRingRepeat.isChecked()) {
                settingsInfo.setRepeat(true);
                loginPrefManager.setBooleanValue(DeliforceConstants.PREF_SETTINGS_REPEAT_RING, true);
            } else {
                settingsInfo.setRepeat(false);
                loginPrefManager.setBooleanValue(DeliforceConstants.PREF_SETTINGS_REPEAT_RING, false);
            }
            if (switchPowerSaving.isChecked()) {
                settingsInfo.setPowerSavingModel(true);
            } else {
                settingsInfo.setPowerSavingModel(false);
            }

            if (switchNavigationHelper.isChecked()) {
                settingsInfo.setNavigationHelper(true);
            } else {
                settingsInfo.setNavigationHelper(false);
            }
            settingsInfo.setNavigationTypes(tempNavigationTypes);
            settingsDetails.setSettingsInfo(settingsInfo);

            show_loader();
            apiService.updateSettings(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken(), settingsDetails).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    dismiss_loader();

                    try {
                        if(isLanguageChanged) {
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                        loginPrefManager.setStringValue("lang_postion", langauge_type + "");
                        if (langauge_type == Integer.parseInt(DeliforceConstants.LANGUAGE_ENGLISH)) {
                            setLanguage(DeliforceConstants.LANGUAGE_CODE_ENGLISH);
                            loginPrefManager.setStringValue("Lang", DeliforceConstants.LANGUAGE_CODE_ENGLISH);
                        } else if (langauge_type == Integer.parseInt(DeliforceConstants.LANGUAGE_SPANISH)) {
                            setLanguage(DeliforceConstants.LANGUAGE_CODE_SPANISH);
                            loginPrefManager.setStringValue("Lang", DeliforceConstants.LANGUAGE_CODE_SPANISH);
                        } else if (langauge_type == Integer.parseInt(DeliforceConstants.LANGUAGE_FRENCH)) {
                            setLanguage(DeliforceConstants.LANGUAGE_CODE_FRENCH);
                            loginPrefManager.setStringValue("Lang", DeliforceConstants.LANGUAGE_CODE_FRENCH);
                        } else if (langauge_type == Integer.parseInt(DeliforceConstants.LANGUAGE_PORTUGUESE)) {
                            setLanguage(DeliforceConstants.LANGUAGE_CODE_PORTUGUESE);
                            loginPrefManager.setStringValue("Lang", DeliforceConstants.LANGUAGE_CODE_PORTUGUESE);
                        } else if (langauge_type == Integer.parseInt(DeliforceConstants.LANGUAGE_MALAY)) {
                            setLanguage(DeliforceConstants.LANGUAGE_CODE_MALAY);
                            loginPrefManager.setStringValue("Lang", DeliforceConstants.LANGUAGE_CODE_MALAY);
                        } else if (langauge_type == Integer.parseInt(DeliforceConstants.LANGUAGE_ARABIC)) {
                            setLanguage(DeliforceConstants.LANGUAGE_CODE_ARABIC);
                            loginPrefManager.setStringValue("Lang", DeliforceConstants.LANGUAGE_CODE_ARABIC);
                        }

                        loginPrefManager.setLangauge(String.valueOf(langauge_type));

                        navigationTypes.clear();
                        navigationTypes.addAll(tempNavigationTypes);
                        updateNavigation(navigationTypes);
                        if (response.code() == 200) {
                            Timber.e("%s", response.toString());
                            Timber.e("%s", response.toString());
                            showShortMessage(getString(R.string.settings_update));
                        }    //alert dialog for user already logged in
                        else if (response.code() == 494) {
                            showAlertDialog(SettingsActivity.this);
                        } else {
                            show_error_response(getString(R.string.error_response));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                    dismiss_loader();
                }
            });
        } catch (Exception e) {
            dismiss_loader();
        }
    }

    private void clearSettingsData() {

        int languageId = 0, vehicleType = 0, vibration = 0;
        if (oldSettingInfo != null) {
            if (oldSettingInfo.getVehicleType() != null) {

                vehicleType = oldSettingInfo.getVehicleType();
            }
            if (oldSettingInfo.getVibration() != null) {
                vibration = oldSettingInfo.getVibration();
            }
            if (oldSettingInfo.getLanguage() != null) {
                languageId = oldSettingInfo.getLanguage();
            }
            boolean isRingRepeat = oldSettingInfo.getRepeat();
            boolean isNavigationHelper = oldSettingInfo.getNavigationHelper();
            boolean isPowerSavingMode = oldSettingInfo.getPowerSavingModel();

            String ringtone = loginPrefManager.getStringValue("ringtoneSelected");
            if (!ringtone.equals("")) {
                tvRingtoneName.setText(ringtone);
            } else {
                tvRingtoneName.setText(getString(R.string.settings_ring_default));
            }
            map_style = oldSettingInfo.getMapStyle();
            vehicle_type = oldSettingInfo.getVehicleType();
            rigntone_type = oldSettingInfo.getRingtone();
            vibration_type = oldSettingInfo.getVibration();
            langauge_type = oldSettingInfo.getLanguage();
            navigation_type = oldSettingInfo.getNavigation();

            Timber.e(String.valueOf(langauge_type));

            if (map_style == 1) {
                mapStyle.setText(getString(R.string.settings_map_style_normal));
            } else {
                mapStyle.setText(getString(R.string.settings_map_style_night));
            }

            if (vibration == 1) {
                vibrationAlert.setText(getString(R.string.vibration_alert_long));
            } else {
                vibrationAlert.setText(getString(R.string.vibration_alert_system));
            }

            if (vehicleType != 0) {
                //set vehicle type
                switch (vehicleType) {
                    case 1:
                        tvVehicleType.setText(getString(R.string.vehicle_type_bike));
                        imageVehicleType.setImageDrawable(ContextCompat.getDrawable(SettingsActivity.this, R.drawable.bike_gray));
                        break;
                    case 2:
                        tvVehicleType.setText(getString(R.string.vehicle_type_car));
                        imageVehicleType.setImageDrawable(ContextCompat.getDrawable(SettingsActivity.this, R.drawable.car_gray));
                        break;
                    case 3:
                        tvVehicleType.setText(getString(R.string.vehicle_type_truck));
                        imageVehicleType.setImageDrawable(ContextCompat.getDrawable(SettingsActivity.this, R.drawable.truckicon));
                        break;
                    case 4:
                        tvVehicleType.setText(getString(R.string.vehicle_type_scooter));
                        imageVehicleType.setImageDrawable(ContextCompat.getDrawable(SettingsActivity.this, R.drawable.scooter_gray));
                        break;
                    case 5:
                        tvVehicleType.setText(getString(R.string.vehicle_type_cycle));
                        imageVehicleType.setImageDrawable(ContextCompat.getDrawable(SettingsActivity.this, R.drawable.cycle_gray));
                        break;
                    case 6:
                        tvVehicleType.setText(getString(R.string.vehicle_type_walking));
                        imageVehicleType.setImageDrawable(ContextCompat.getDrawable(SettingsActivity.this, R.drawable.walk_gray));
                        break;

                }
            }

            if (isRingRepeat) {
                switchRingRepeat.setChecked(true);
            } else {
                switchRingRepeat.setChecked(false);
            }


            if (isPowerSavingMode) {
                switchPowerSaving.setChecked(true);
            } else {
                switchPowerSaving.setChecked(false);
            }
            if (isNavigationHelper) {
                switchNavigationHelper.setChecked(true);
            } else {
                switchNavigationHelper.setChecked(false);
            }

            if ((languageId + "").equals(DeliforceConstants.LANGUAGE_ENGLISH)) {
                appLanguage.setText(getString(R.string.settings_language_eng));
            } else if ((languageId + "").equals(DeliforceConstants.LANGUAGE_SPANISH)) {
                appLanguage.setText(getString(R.string.settings_language_spanish));
            } else if ((languageId + "").equals(DeliforceConstants.LANGUAGE_FRENCH)) {
                appLanguage.setText(getString(R.string.settings_language_french));
            } else if ((languageId + "").equals(DeliforceConstants.LANGUAGE_PORTUGUESE)) {
                appLanguage.setText(getString(R.string.settings_language_portuguese));
            } else if ((languageId + "").equals(DeliforceConstants.LANGUAGE_MALAY)) {
                appLanguage.setText(getString(R.string.settings_language_malay));
            } else if ((languageId + "").equals(DeliforceConstants.LANGUAGE_ARABIC)) {
                appLanguage.setText(getString(R.string.settings_language_arabic));
            }
            tempNavigationTypes.clear();
            updateNavigation(navigationTypes);
            tempNavigationTypes.addAll(navigationTypes);
            tvNavigationApp.setText(loginPrefManager.getNavigation());
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startHomeScreen();
    }

    private void getStatus() {
        try {
            String lang = "";
            if (loginPrefManager.getLangauge().equalsIgnoreCase(DeliforceConstants.LANGUAGE_ENGLISH)) {
                lang = DeliforceConstants.LANGUAGE_CODE_ENGLISH;
            }
            if (loginPrefManager.getLangauge().equalsIgnoreCase(DeliforceConstants.LANGUAGE_SPANISH)) {
                lang = DeliforceConstants.LANGUAGE_CODE_SPANISH;
            }
            if (loginPrefManager.getLangauge().equalsIgnoreCase(DeliforceConstants.LANGUAGE_FRENCH)) {
                lang = DeliforceConstants.LANGUAGE_CODE_FRENCH;
            }
            if (loginPrefManager.getLangauge().equalsIgnoreCase(DeliforceConstants.LANGUAGE_PORTUGUESE)) {
                lang = DeliforceConstants.LANGUAGE_CODE_PORTUGUESE;
            }
            if (loginPrefManager.getLangauge().equalsIgnoreCase(DeliforceConstants.LANGUAGE_MALAY)) {
                lang = DeliforceConstants.LANGUAGE_CODE_MALAY;
            }
            if (loginPrefManager.getLangauge().equalsIgnoreCase(DeliforceConstants.LANGUAGE_ARABIC)) {
                lang = DeliforceConstants.LANGUAGE_CODE_ARABIC;
            }
            show_loader();
            apiService.getStatusList(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken(), lang).enqueue(new Callback<DefalutStatus>() {
                @Override
                public void onResponse(@NotNull Call<DefalutStatus> call, @NotNull Response<DefalutStatus> response) {
                    dismiss_loader();
                    try {

                        if (response.raw().code() == 200) {
                            if (response.body().getVehicleType() != null)
                                if (response.body().getVehicleType().size() != 0) {
                                    vehicle_list = response.body().getVehicleType();

                                    Log.e("vehicleList", new Gson().toJson(vehicle_list));
                                }
                        }
                    } catch (Exception e) {
                        Timber.e("%s", e.getMessage());
                    }

                }

                @Override
                public void onFailure(@NotNull Call<DefalutStatus> call, @NotNull Throwable t) {
                    dismiss_loader();

                }
            });

        } catch (Exception e) {
            dismiss_loader();
            Timber.e(e);
        }

    }


}
