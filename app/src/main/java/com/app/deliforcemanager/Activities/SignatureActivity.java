package com.app.deliforcemanager.Activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.app.deliforcemanager.CustomViews.SignatureView.views.SignaturePad;
import com.app.deliforcemanager.LocalizationActivity.LocalizationActivity;
import com.app.deliforcemanager.ModelClass.UpdateImagesApi.DriverImage;
import com.app.deliforcemanager.R;
import com.app.deliforcemanager.Utils.DeliforceConstants;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignatureActivity extends LocalizationActivity {

    String task_id, signature_added;
    int task_success;

    SignaturePad signaturePad;
    Button btn_update, clear, btn_add;
    File signature_file, updatedSignature;
    ImageView uploadedSignatureImg;
    private TextView toolbar_text;
    private int signature = 0;

    LinearLayout btn_view;
    boolean image_added = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_signature);

        if (getIntent().getExtras() != null) {
            task_id = getIntent().getExtras().getString("task_id");
            signature_added = getIntent().getExtras().getString("added_sign");
            task_success = getIntent().getExtras().getInt("task_success", 0);
        }

        inittoolbar();
        initView();

        if (signature_added != null) {
            image_added = true;
            loaduserimage(signature_added);
        }

    }

    private void inittoolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar_text = findViewById(R.id.toolbar_title);

        if (signature_added != null) {
            toolbar_text.setText(getString(R.string.update_sign));
        } else {
            toolbar_text.setText(getString(R.string.update_signature_header));
        }

        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        toolbar.setNavigationOnClickListener(view -> finish());
    }

    private void onClickevents() {

        clear.setOnClickListener(view -> {
            uploadedSignatureImg.setVisibility(View.INVISIBLE);
            signaturePad.setVisibility(View.VISIBLE);
            signaturePad.clear();
            signature = 0;
            signature_added = "";
            image_added = false;

            signaturePad.setBackgroundResource(R.drawable.signature);
        });

        btn_update.setOnClickListener(view -> {
            if (signature == 1) {
                Bitmap signatureUpdatedBitmap = signaturePad.getSignatureBitmap();
                if (updatedJpgSignatureToGallery(signatureUpdatedBitmap)) {
                    upDateSignature();
                } else {
                    Toast.makeText(SignatureActivity.this, getString(R.string.unable_signture), Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(SignatureActivity.this, getString(R.string.no_signature), Toast.LENGTH_SHORT).show();
            }

        });

        btn_add.setOnClickListener(view -> {
            if (signature == 1) {
                Bitmap signatureBitmap = signaturePad.getSignatureBitmap();
                if (addJpgSignatureToGallery(signatureBitmap)) {
                    uploadSignature();
                } else {
                    Toast.makeText(SignatureActivity.this, getString(R.string.signaute_load), Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(SignatureActivity.this, getString(R.string.no_signature), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initView() {
        signaturePad = findViewById(R.id.inkV_sign_here);
        btn_update = findViewById(R.id.btn_sign_update);
        clear = findViewById(R.id.btn_sign_reset);
        btn_add = findViewById(R.id.btn_add_update);
        uploadedSignatureImg = findViewById(R.id.img_edit_sign);

        btn_view = findViewById(R.id.btn_view);

        if (task_success != DeliforceConstants.TASK_STARTED) {
            btn_view.setVisibility(View.GONE);
            uploadedSignatureImg.setClickable(false);
            toolbar_text.setText(getString(R.string.signature));
        }
        if (task_success == DeliforceConstants.TASK_STARTED || task_success == DeliforceConstants.TASK_ARRIVED) {
            btn_view.setVisibility(View.VISIBLE);
            uploadedSignatureImg.setClickable(true);
        }

        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                signaturePad.setBackgroundResource(android.R.color.transparent);
            }

            @Override
            public void onSigned() {
                btn_update.setEnabled(true);
                if (signature == 0 && !signature_added.equals("")) {
                    signaturePad.clear();
                }

                signature = 1;
            }

            @Override
            public void onClear() {
                signature = 0;
                btn_update.setEnabled(false);
            }
        });

        uploadedSignatureImg.setOnClickListener(view -> {
            if (task_success == DeliforceConstants.TASK_STARTED|| task_success == DeliforceConstants.TASK_ARRIVED) {
                uploadedSignatureImg.setVisibility(View.INVISIBLE);
                signaturePad.setVisibility(View.VISIBLE);
                signaturePad.clear();
                signature = 0;
                signature_added = "";
                image_added = false;

                signaturePad.setBackgroundResource(R.drawable.signature);
            }
        });
        onClickevents();
    }

    private void uploadSignature() {
        try {
            RequestBody task = RequestBody.create(MediaType.parse("multipart/form-data"), task_id);

            RequestBody imgfile = RequestBody.create(MediaType.parse("multipart/form-data"), signature_file);
            final MultipartBody.Part MEDIA = MultipartBody.Part.createFormData("image", signature_file.getName(), imgfile);

            show_loader();

            apiService.uploadtask_signature_images(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken(), task, MEDIA).enqueue(new Callback<DriverImage>() {
                @Override
                public void onResponse(@NonNull Call<DriverImage> call, @NotNull Response<DriverImage> response) {
                    dismiss_loader();

                    try {
                        if (response.raw().code() == 200) {
//                            startActivity(new Intent(SignatureActivity.this,
//                                    TaskDetailsActivity.class).putExtra("task_id", task_id));
//                            finish();

                            Intent intent= new Intent();
                            setResult(RESULT_OK, intent);
                            finish();


                            showShortMessage(getString(R.string.signture_added));
                        } else if (response.raw().code() == 401) {
                            findCurrent();
                        } else if (response.code() == 494) {
                            showAlertDialog(SignatureActivity.this);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<DriverImage> call, @NotNull Throwable t) {
                    dismiss_loader();
                    showShortMessage(t.getMessage());
                }
            });
        } catch (Exception e) {
            dismiss_loader();
        }
    }

    private void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

    private boolean addJpgSignatureToGallery(Bitmap signature) {
        boolean result = false;
        try {
            signature_file = new File(getAlbumStorageDir(), String.format("Signature_%d.jpg", System.currentTimeMillis()));
            saveBitmapToJPG(signature, signature_file);
            scanMediaFile(signature_file);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private boolean updatedJpgSignatureToGallery(Bitmap signature) {
        boolean result = false;
        try {
            updatedSignature = new File(getAlbumStorageDir(), String.format("Signature_%d.jpg", System.currentTimeMillis()));
            saveBitmapToJPG(signature, updatedSignature);
            scanMediaFile(updatedSignature);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }


    private File getAlbumStorageDir() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Deliforce");
        file.mkdirs();
        return file;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void upDateSignature() {
        try {
            RequestBody task = RequestBody.create(MediaType.parse("multipart/form-data"), task_id);

            RequestBody imgfile = RequestBody.create(MediaType.parse("multipart/form-data"), updatedSignature);
            final MultipartBody.Part MEDIA = MultipartBody.Part.createFormData("image", updatedSignature.getName(), imgfile);


            show_loader();

            apiService.updateExistingSignatute(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken(), task, MEDIA).enqueue(new Callback<DriverImage>() {
                @Override
                public void onResponse(@NonNull Call<DriverImage> call, @NotNull Response<DriverImage> response) {

                    dismiss_loader();

                    try {
                        if (response.raw().code() == 200) {

                            finish();
                            showShortMessage(getString(R.string.signature_update));

                        } else if (response.raw().code() == 401) {
                            findCurrent();
                        }
                        //alert dialog for user already logged in
                        else if (response.code() == 494) {
                            showAlertDialog(SignatureActivity.this);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<DriverImage> call, @NotNull Throwable t) {

                    dismiss_loader();
                    showShortMessage(t.getMessage());
                }
            });

        } catch (Exception e) {
            dismiss_loader();
        }
    }


    private void loaduserimage(String url) {


        if (!url.equals("")) {
            btn_update.setVisibility(View.VISIBLE);
            btn_add.setVisibility(View.GONE);
            signaturePad.setVisibility(View.GONE);
            uploadedSignatureImg.setVisibility(View.VISIBLE);
            Glide.with(this).load(url).into(uploadedSignatureImg);
        } else {
            btn_update.setVisibility(View.GONE);
            btn_add.setVisibility(View.VISIBLE);
        }

    }

    private void AskCameraPermissions() {
        permissionHelper.check(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withDialogBeforeRun(R.string.dialog_before_run_title, R.string.dialog_location_before_run_message, R.string.dialog_positive_button)
                .setDialogPositiveButtonColor(R.color.colorPrimary)
                .onSuccess(this::onSuccessProfile)
                .onDenied(this::onDeniedProfile)
                .onNeverAskAgain(this::onNeverAskAgainProfile)
                .run();
    }

    @Override
    public void onResume() {
        super.onResume();
        AskCameraPermissions();
    }

    private void onSuccessProfile() {

        btn_add.setEnabled(true);
        btn_update.setEnabled(true);

    }

    private void onNeverAskAgainProfile() {
        showNointerntView(getString(R.string.enable_permission));
    }

    private void onDeniedProfile() {
        showNointerntView(getString(R.string.onDenied));
    }


}
