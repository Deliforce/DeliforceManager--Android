package com.app.deliforcemanager.CustomDialogView;

import android.Manifest;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.deliforcemanager.Adapter.AddImagesAdpater;
import com.app.deliforcemanager.ModelClass.GetProfile.FailedReason;
import com.app.deliforcemanager.PermissionChecker.PermissionHelper;
import com.app.deliforcemanager.R;
import com.app.deliforcemanager.Utils.DeliforceConstants;
import com.app.deliforcemanager.Utils.LoginPrefManager;
import com.fxn.pix.Pix;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.app.deliforcemanager.LocalizationActivity.LocalizationActivity.getNavBarHeight;

public class CancelDialog extends Dialog {

    public static final int CANCEL_IMAGE_REQUEST_CODE = 78;
    public LoginPrefManager loginPrefManager;
    public PermissionHelper permissionHelper;
    RecyclerView rvImages;
    AddImagesAdpater addImagesAdpater;
    private FragmentActivity activity;
    private CancelInterface cancelInterface;
    private LinearLayout llAttachImage;
    private LinearLayout layout_add_images;
    private EditText reason;
    private Button cancel, update;
    private TextView image_count;
    private Spinner sprReason;
    private List<String> file_path;
    private List<FailedReason> failedReasons;
    RelativeLayout rlReason;
    private int status;

   /* public CancelDialog(@NonNull Context context, int themeResId, CancelInterface cancelInterface) {
        super(context, themeResId);
        this.context = context;
        this.cancelInterface = cancelInterface;
        loginPrefManager = new LoginPrefManager(context);
    }*/

    public CancelDialog(@NonNull FragmentActivity activity, int themeResId, CancelInterface cancelInterface, int status) {
        super(activity, themeResId);
        this.activity = activity;
        this.cancelInterface = cancelInterface;
        loginPrefManager = new LoginPrefManager(activity);
        permissionHelper = new PermissionHelper(activity);
        this.status = status;
    }

    public void prepareImages(List<String> cancelled_images_path) {
        file_path = cancelled_images_path;
        addImagesAdpater = new AddImagesAdpater(activity, "1", file_path, position -> {

        });
//                deleteImageonCall(file_path.get(position)));
        if (file_path.size() > 0) {
            image_count.setText("" + file_path.size());
            image_count.setVisibility(VISIBLE);
        } else {
            image_count.setVisibility(GONE);
        }
        rvImages.setAdapter(addImagesAdpater);
    }

    @RequiresApi(api = 28)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.cancel_dailog);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;

        rlReason = findViewById(R.id.rlReason);
        sprReason = findViewById(R.id.sprReason);
        llAttachImage = findViewById(R.id.llAttachImage);
        layout_add_images = findViewById(R.id.layout_add_images);
        image_count = findViewById(R.id.image_count);
        cancel = findViewById(R.id.btn_cancel);
        update = findViewById(R.id.btn_update);
        reason = findViewById(R.id.reason);
        rvImages = findViewById(R.id.rvImages);
        rvImages.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        file_path = new ArrayList<>();

        loginPrefManager.setIsFailedOrCancelled(false);
        if (loginPrefManager.isFailedOrCancelled()) {
            llAttachImage.setVisibility(View.VISIBLE);
        } else {
            llAttachImage.setVisibility(View.GONE);
        }
        if (status == DeliforceConstants.TASK_FAILED && loginPrefManager.isCustomFailedReason()) {
            failedReasons = loginPrefManager.getFailedReasons();
            FailedReason failedReason = new FailedReason();
            failedReason.setReason_id("");
            failedReason.setReason(activity.getString(R.string.other));
            failedReasons.add(failedReason);
            rlReason.setVisibility(VISIBLE);
            reason.setVisibility(GONE);
            ArrayAdapter<FailedReason> reasonsAdapter = new ArrayAdapter<FailedReason>(activity, R.layout.cancel_spinner_item, failedReasons);
            reasonsAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
            sprReason.setAdapter(reasonsAdapter);
            sprReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (failedReasons.get(position).getReason_id().isEmpty()) {
                        reason.setVisibility(VISIBLE);
                    } else {
                        reason.setVisibility(GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else {
            rlReason.setVisibility(GONE);
            reason.setVisibility(VISIBLE);
        }
        onClickevetns();


    }


    private void onClickevetns() {

        update.setOnClickListener(view -> {
            if (!validreason()) {
                return;
            }

            if (cancelInterface != null) {
                if (!loginPrefManager.isCustomFailedReason() || status != DeliforceConstants.TASK_FAILED) {
                    cancelInterface.buttonselected(true, reason.getText().toString(),"");
                } else {
                    if (failedReasons.get(sprReason.getSelectedItemPosition()).getReason_id().isEmpty()) {
                        cancelInterface.buttonselected(true, reason.getText().toString(),"");
                    } else {
                        cancelInterface.buttonselected(true, failedReasons.get(sprReason.getSelectedItemPosition()).getReason(),
                                failedReasons.get(sprReason.getSelectedItemPosition()).getReason_id());
                    }
                }
            }
        });

        cancel.setOnClickListener(view -> {
            if (cancelInterface != null) {
                cancelInterface.buttonselected(false, "","");
            }
        });

        layout_add_images.setOnClickListener(
                view -> {
                    if (file_path.size() <= 4) {
                        AskCameraPermissions();
                    } else {
                        Toast.makeText(activity, activity.getString(R.string.error_four_max_images), Toast.LENGTH_SHORT).show();
                    }
                }
        );
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

    private void onNeverAskAgainProfile() {
        showNointerntView(activity.getString(R.string.enable_camera_permission));
    }

    private void onDeniedProfile() {
        showNointerntView(activity.getString(R.string.onDenied));
    }

    private void onSuccessProfile() {
        Pix.start(activity, CANCEL_IMAGE_REQUEST_CODE);
    }

    private boolean validreason() {
        if ((reason.getText().toString().isEmpty()) && ((status != DeliforceConstants.TASK_FAILED) ||
                (status == DeliforceConstants.TASK_FAILED && !loginPrefManager.isCustomFailedReason()) ||
                (status == DeliforceConstants.TASK_FAILED && loginPrefManager.isCustomFailedReason() &&
                        failedReasons.get(sprReason.getSelectedItemPosition()).getReason_id().isEmpty()))) {
            reason.setError(activity.getString(R.string.error_empty_reason));
            return false;
        }
        /*if (!loginPrefManager.isCustomFailedReason() && reason.getText().toString().equals("")) {
            reason.setError(activity.getString(R.string.error_empty_reason));
            return false;
        } else if (loginPrefManager.isCustomFailedReason() &&
                failedReasons.get(sprReason.getSelectedItemPosition()).getReason_id().isEmpty() &&
                reason.getText().toString().isEmpty()) {
            reason.setError(activity.getString(R.string.error_empty_reason));
            return false;
        }*/
       /* else if (loginPrefManager.isFailedOrCancelled() && file_path.isEmpty()) {
            Toast.makeText(activity, activity.getString(R.string.imagepicker_multiselect_not_enough_singular), Toast.LENGTH_SHORT).show();
            return false;
        }*/
        return true;
    }

    private void showNointerntView(String msg) {
        LinearLayout.LayoutParams objLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        Snackbar snackbar = Snackbar.make(this.findViewById(android.R.id.content), activity.getString(R.string.app_name), Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        int navbarHeight = getNavBarHeight(activity);
        FrameLayout.LayoutParams parentParams = (FrameLayout.LayoutParams) layout.getLayoutParams();
        parentParams.setMargins(0, 0, 0, 0 - navbarHeight + 100);
        layout.setLayoutParams(parentParams);
        layout.setPadding(0, 0, 0, 0);
        layout.setLayoutParams(parentParams);
        View snackView = getLayoutInflater().inflate(R.layout.no_internet_view, null);
        TextView status = snackView.findViewById(R.id.message_text_view);
        status.setText(msg);
        layout.addView(snackView, objLayoutParams);
        snackbar.show();
    }

    public interface CancelInterface {
        void buttonselected(boolean status, String msg,String reasonId);
    }
}
