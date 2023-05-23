package com.app.deliforcemanager.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.app.deliforcemanager.BaseActivity.BaseDrawerActivity;
import com.app.deliforcemanager.ModelClass.GetProfile.GetProfileApi;
import com.app.deliforcemanager.ModelClass.GetProfile.GetProfileImage;
import com.app.deliforcemanager.ModelClass.ManagerProfile.ProfileResponse;
import com.app.deliforcemanager.ModelClass.ProfileUpdateRequest;
import com.app.deliforcemanager.R;
import com.bumptech.glide.Glide;
import com.fxn.pix.Pix;
import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class ProfileActivity extends BaseDrawerActivity {

    private static final String TAG = "ProfileActivity";

    ImageView editProfile;
    CircularImageView driver_image;
    Button btnUpdateProfile, btnCancelProfile;
    CountryCodePicker countryCodePicker;
    EditText edtDriverName, edtDriverPhone, edtDriverEmailId, edt_last_name;
    TextView txt_profile_cc;
    TextInputLayout last_hint;
    private String contactNumber = "";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        initViews();
    }

    private void initViews() {
        tvTitle.setText(getString(R.string.my_profile));
        edtDriverName = findViewById(R.id.edt_user_name);
        txt_profile_cc = findViewById(R.id.txt_profile_cc);
        edtDriverPhone = findViewById(R.id.edt_mob_num);
        editProfile = findViewById(R.id.edit_profile);
        driver_image = findViewById(R.id.driver_image);
        countryCodePicker = findViewById(R.id.country_code_picker);
        edtDriverEmailId = findViewById(R.id.edt_email_id);
        edt_last_name = findViewById(R.id.edt_last_name);
        btnUpdateProfile = findViewById(R.id.btn_update_profile);
        btnCancelProfile = findViewById(R.id.btn_profile_cancel);
        last_hint = findViewById(R.id.last_hint);

        edtDriverPhone.setFocusable(false); //to disable it

        onClickEvents();

        final Handler handler = new Handler();
        handler.postDelayed(this::getDriverProfileDetails, 800);

        if (loginPrefManager.getStringValue("lang_postion").equalsIgnoreCase("2")) {
            //Locale spanishLocale = new Locale("es", "ES");
            countryCodePicker.changeDefaultLanguage(CountryCodePicker.Language.SPANISH);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void onClickEvents() {
        edtDriverName.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (edtDriverName.getRight() - edtDriverName.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                    edtDriverName.setFocusableInTouchMode(true);
                    edtDriverName.setEnabled(true);
                    edtDriverName.setCursorVisible(true);
                    edtDriverName.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(edtDriverName, InputMethodManager.SHOW_IMPLICIT);
                    return true;
                }
            }
            return false;
        });
        edt_last_name.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (edt_last_name.getRight() - edt_last_name.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                    edt_last_name.setFocusableInTouchMode(true);
                    edt_last_name.setEnabled(true);
                    edt_last_name.setCursorVisible(true);

                    edt_last_name.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(edt_last_name, InputMethodManager.SHOW_IMPLICIT);
                    return true;
                }
            }
            return false;
        });

        //no need
        edtDriverEmailId.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (edtDriverPhone.getRight() - edtDriverPhone.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    //to open the keypad

                    edtDriverEmailId.setEnabled(true);
                    edtDriverEmailId.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    edtDriverEmailId.setFocusable(true);
                    edtDriverEmailId.setFocusableInTouchMode(true);
                    edtDriverEmailId.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(edtDriverEmailId, InputMethodManager.SHOW_IMPLICIT);

                    return true;

                }
            }
            return false;
        });


        btnUpdateProfile.setOnClickListener(v -> {

            if (!validEmail(edtDriverEmailId)) {
                return;
            } else if (!emailpattern(edtDriverEmailId)) {
                return;
            }

            if (!validFirstName(edtDriverName)) {
                return;
            }

            requestToUpdateProfile();
        });

        btnCancelProfile.setOnClickListener(v -> navigateToTaskList());

        editProfile.setOnClickListener(view -> AskCameraPermissions());

    }

    private void getDriverProfileDetails() {
        try {
            show_loader();
            apiService.getManagerProfileDetails(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken()).enqueue(new Callback<ProfileResponse>() {
                @Override
                public void onResponse(@NonNull Call<ProfileResponse> call, @NonNull Response<ProfileResponse> response) {

                    dismiss_loader();

                    try {
                        if (response.raw().code() == 200) {
                          //  fetchProfileDetails(loginPrefManager, response.body());
                            edtDriverName.setText(response.body().getName());
                            edt_last_name.setText(response.body().getName().replaceAll("null", ""));

                            //Log.e("map_key",response.body().getGoogleMapKey());

                            edtDriverEmailId.setText(response.body().getEmail());
//
//                            String driverImg = response.body().getImage();
//                            if (driverImg != null) {
//                                Glide.with(ProfileActivity.this).load(driverImg).into(driver_image);
//                            }
                            contactNumber = response.body().getPhone();

                            if (!contactNumber.equals(""))
                                countryCodePicker.setFullNumber(contactNumber);

                            //fullnumber with plus consist of country code
                            //based on that removing the country code in phone number
                            String formattedContact = contactNumber.split(" ")[1];
                            txt_profile_cc.setText(contactNumber.split(" ")[0]);
                            //String formattedContact = contactNumber.replace(countryCodePicker.getFullNumberWithPlus(), "");
                            edtDriverPhone.setText(formattedContact);
                        } else {
                            findCurrent();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                @Override
                public void onFailure(@NonNull Call<ProfileResponse> call, @NotNull Throwable t) {
                    dismiss_loader();
                    Timber.e("Get Profile Err--> %s", t.toString());
                }
            });

        } catch (Exception e) {
            Timber.e(e);
        }
    }


    private void requestToUpdateProfile() {
        try {
            //here I changed
//            GetProfileApi profileDetails = new GetProfileApi();


//            String countryCode = countryCodePicker.getSelectedCountryCodeWithPlus();
//            Timber.e("CountryCode-->%s", countryCode);
//            String updatedDriverName = edtDriverName.getText().toString();
//            String update_lastname = edt_last_name.getText().toString();
//            String updatedContactNumber = edtDriverPhone.getText().toString().trim();
//            String formattedMobileNumber = countryCode + " " + updatedContactNumber;



//            profileDetails.setName(updatedDriverName);
//            profileDetails.setLastname(update_lastname);
//            Timber.e("formated--> %s", formattedMobileNumber);
//
//            profileDetails.setPhone(formattedMobileNumber);
//            profileDetails.setEmail(edtDriverEmailId.getText().toString());


            String countryCode = countryCodePicker.getSelectedCountryCodeWithPlus();
            String updatedDriverName = edtDriverName.getText().toString();
            String update_lastname = edt_last_name.getText().toString();
            String updatedContactNumber = edtDriverPhone.getText().toString().trim();
            String formattedMobileNumber = countryCode + " " + updatedContactNumber;


            ProfileUpdateRequest profileRequest = new ProfileUpdateRequest();

            profileRequest.setEmail(edtDriverEmailId.getText().toString());

            profileRequest.setId(loginPrefManager.getManagerID());

            profileRequest.setName(updatedDriverName);

            profileRequest.setPhone(formattedMobileNumber);

            profileRequest.setPassword(loginPrefManager.getGlympseUserPswd());






            show_loader();
            apiService.updateDriverProfileDetails(loginPrefManager.getCogintoToken(), profileRequest).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    dismiss_loader();
                    try {
                        if (response.code() == 200) {
                            show_error_response("Profile  Updated Successfully.");
                           // edtDriverEmailId.setText(response.body().getEmail());
                            edtDriverPhone.setFocusable(false);
                            edtDriverName.setFocusable(false);
                            getDriverProfileDetails();
                        } else if (response.raw().code() == 401) {
                            findCurrent();
                        } else if (response.raw().code() == 429) {
                            show_error_response(getString(R.string.error_exist_mobile));
                        } else if (response.code() == 494) {
                            showAlertDialog(ProfileActivity.this);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dismiss_loader();
                    showShortMessage(t.getMessage());
                }
            });
        } catch (Exception e) {

            dismiss_loader();
            Timber.e(e);
        }
    }

    private void navigateToTaskList() {
        startActivity(new Intent(ProfileActivity.this, NavigationActivity.class));
        finish();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startHomeScreen();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                /*List<Image> returnValue = ImagePicker.getImages(data);

                File file = new File(returnValue.get(0).getPath());
                long length = file.length() / 1024;

                if (returnValue.size() > 0) {
                    if (length > 5120) {
                        show_error_response(getString(R.string.error_img_size));
                    } else {
                        uploadProfilePic(returnValue.get(0).getPath());
                    }
                }*/
                ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);

                File file = new File(returnValue.get(0));
                long length = file.length() / 1024;

                if (returnValue.size() > 0) {
                    if (length > 5120) {
                        show_error_response(getString(R.string.error_img_size));
                    } else {
                        uploadProfilePic(returnValue.get(0));
                    }
                }
            } else {
                Toast.makeText(this, "Profile image upload failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadProfilePic(String driverImgPath) {
        try {
            File file = new File(driverImgPath);

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part driverProfilePic = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
            show_loader();

            apiService.uploadDriverProfilePic(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken(), driverProfilePic).enqueue(new Callback<GetProfileImage>() {
                @Override
                public void onResponse(@NotNull Call<GetProfileImage> call, @NotNull Response<GetProfileImage> response) {

                    dismiss_loader();
                    try {
                        if (response.raw().code() == 200) {
                            String imgUrl = response.body().getDriverProfileImg();

                            loginPrefManager.setStringValue("image_url", imgUrl);

                            Glide.with(ProfileActivity.this).load(imgUrl).into(driver_image);
                            Glide.with(ProfileActivity.this).load(imgUrl).into(user_profile);

                            showShortMessage(getString(R.string.profile_img_success));
                        } else if (response.raw().code() == 401) {
                            findCurrent();
                        } else {
                            show_error_response(getString(R.string.error_response));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<GetProfileImage> call, @NotNull Throwable t) {
                    dismiss_loader();
                    Timber.e("Err-->%s", t.toString());
                }
            });

        } catch (Exception e) {
            dismiss_loader();
            e.printStackTrace();
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
        Pix.start(ProfileActivity.this, 100);
        //galleryMode();
    }

    /*private void galleryMode() {
        ImagePicker.create(ProfileActivity.this)
                .returnMode(ReturnMode.ALL) // set whether pick and / or camera action should return immediate result or not.
                .folderMode(true) // folder mode (false by default)
                .toolbarFolderTitle("Folder") // folder selection title
                .toolbarImageTitle("Profile picture") // image selection title
                .toolbarArrowColor(Color.WHITE) // Toolbar 'up' arrow color
                .includeVideo(false) // Show video on image picker
                .showCamera(true)
                .limit(1)
                .single()
                .theme(R.style.ef_AppTheme) // must inherit ef_BaseTheme. please refer to sample
                .enableLog(false) // disabling log
                .start(100); // start image picker activity with request code
    }*/

    private void onNeverAskAgainProfile() {
        showNointerntView(getString(R.string.enable_camera_permission));
    }

    private void onDeniedProfile() {
        showNointerntView(getString(R.string.onDenied));
    }

}
