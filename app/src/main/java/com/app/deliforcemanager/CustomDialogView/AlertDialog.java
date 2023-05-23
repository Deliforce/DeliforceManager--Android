package com.app.deliforcemanager.CustomDialogView;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.app.deliforcemanager.ModelClass.TaskDetailsApi.Note;
import com.app.deliforcemanager.R;
import com.app.deliforcemanager.Utils.DeliforceConstants;

import java.util.List;

import timber.log.Timber;

public class AlertDialog extends Dialog {

    List<Note> notes_list;
    String signature_txt;
    BlockedInterface blockedInterface;
    private Context context;
    private TextView images, notes, signature, bar_code_txt, blocked_txt, actual_customer_txt, is_capture_location_txt;
    private Button update_btn;
    private int type, photo_size;


    public AlertDialog(@NonNull Context context, int themeResId, int type) {
        super(context, themeResId);
        this.context = context;
        this.type = type;
    }

    public AlertDialog(Context context, int themeResId, int type, BlockedInterface blockedInterface) {
        super(context, themeResId);
        this.context = context;
        this.type = type;
        this.blockedInterface = blockedInterface;
    }

    @RequiresApi(api = 28)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.alert_dailog);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;


        images = findViewById(R.id.image_text);
        notes = findViewById(R.id.notes_txt);
        signature = findViewById(R.id.signature_txt);
        update_btn = findViewById(R.id.btn_update);
        bar_code_txt = findViewById(R.id.bar_code_txt);
        blocked_txt = findViewById(R.id.blocked_text);
        actual_customer_txt = findViewById(R.id.actual_customer_txt);
        is_capture_location_txt = findViewById(R.id.is_capture_location_txt);

        if (type == 100) {
            update_btn.setText(context.getString(R.string.ok_btn));
            blocked_txt.setVisibility(View.VISIBLE);
        }

        if (type == 0) {
            images.setVisibility(View.VISIBLE);
            notes.setVisibility(View.VISIBLE);
            signature.setVisibility(View.VISIBLE);
            bar_code_txt.setVisibility(View.VISIBLE);
        }
        if (type == 1) {
            images.setVisibility(View.VISIBLE);
            notes.setVisibility(View.VISIBLE);
            signature.setVisibility(View.GONE);
            bar_code_txt.setVisibility(View.GONE);
        }
        if (type == 2) {
            images.setVisibility(View.VISIBLE);
            notes.setVisibility(View.GONE);
            signature.setVisibility(View.VISIBLE);
            bar_code_txt.setVisibility(View.GONE);
        }
        if (type == 3) {
            notes.setVisibility(View.GONE);
            images.setVisibility(View.GONE);
            signature.setVisibility(View.VISIBLE);
            bar_code_txt.setVisibility(View.VISIBLE);
        }
        if (type == 4) {
            notes.setVisibility(View.VISIBLE);
            images.setVisibility(View.GONE);
            signature.setVisibility(View.VISIBLE);
            bar_code_txt.setVisibility(View.GONE);
        }
        if (type == 5) {
            notes.setVisibility(View.VISIBLE);
            images.setVisibility(View.GONE);
            signature.setVisibility(View.GONE);
            bar_code_txt.setVisibility(View.VISIBLE);
        }
        if (type == 6) {
            notes.setVisibility(View.GONE);
            images.setVisibility(View.VISIBLE);
            signature.setVisibility(View.GONE);
            bar_code_txt.setVisibility(View.VISIBLE);
        }
        if (type == 7) {
            notes.setVisibility(View.VISIBLE);
            images.setVisibility(View.GONE);
            signature.setVisibility(View.GONE);
            bar_code_txt.setVisibility(View.GONE);
        }
        if (type == 8) {
            notes.setVisibility(View.GONE);
            images.setVisibility(View.VISIBLE);
            signature.setVisibility(View.GONE);
            bar_code_txt.setVisibility(View.GONE);
        }
        if (type == 9) {
            notes.setVisibility(View.GONE);
            images.setVisibility(View.GONE);
            signature.setVisibility(View.VISIBLE);
            bar_code_txt.setVisibility(View.GONE);
        }
        if (type == 10) {
            notes.setVisibility(View.GONE);
            images.setVisibility(View.GONE);
            signature.setVisibility(View.GONE);
            bar_code_txt.setVisibility(View.VISIBLE);
        }
        if (type == 11) {
            images.setVisibility(View.VISIBLE);
            notes.setVisibility(View.VISIBLE);
            signature.setVisibility(View.VISIBLE);
            bar_code_txt.setVisibility(View.GONE);
        }
        if (type == 12) {
            images.setVisibility(View.VISIBLE);
            notes.setVisibility(View.VISIBLE);
            signature.setVisibility(View.GONE);
            bar_code_txt.setVisibility(View.VISIBLE);
        }
        if (type == 13) {
            images.setVisibility(View.GONE);
            notes.setVisibility(View.VISIBLE);
            signature.setVisibility(View.VISIBLE);
            bar_code_txt.setVisibility(View.VISIBLE);
        }
        if (type == 14) {
            images.setVisibility(View.VISIBLE);
            notes.setVisibility(View.GONE);
            signature.setVisibility(View.VISIBLE);
            bar_code_txt.setVisibility(View.VISIBLE);
        }
        if (type == DeliforceConstants.isActualCustomer) {
            actual_customer_txt.setVisibility(View.VISIBLE);
        }
        if (type == DeliforceConstants.isCaptureLocation) {
            is_capture_location_txt.setVisibility(View.VISIBLE);
        }

        onClickevents();

    }

    private void onClickevents() {

        update_btn.setOnClickListener(view -> {
            dismiss();
            Timber.e("---%s", validateVisibility());

            if (type == 100) {
                if (blockedInterface != null) {
                    blockedInterface.onclick(true);
                }
            } else {
                if (blockedInterface != null) {
                    blockedInterface.reDirectToSpecifPage(validateVisibility());
                }
            }
        });
    }

    /*To check which text is visible based on that navigate to another screen*/
    private String validateVisibility() {
        if (notes.getVisibility() == View.VISIBLE) {
            return DeliforceConstants.NotesPage;
        } else if (images.getVisibility() == View.VISIBLE) {
            return DeliforceConstants.ImagesPage;
        } else if (signature.getVisibility() == View.VISIBLE) {
            return DeliforceConstants.SignaturePage;
        } else if (bar_code_txt.getVisibility() == View.VISIBLE) {
            return DeliforceConstants.BarcodePage;
        } else if (type == DeliforceConstants.isActualCustomer) {
            return type + "";
        } else if (type == DeliforceConstants.isCaptureLocation) {
            return type + "";
        } else {
            return "";
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (type == 100) {
            if (blockedInterface != null) {
                blockedInterface.onclick(true);
            }
        }
    }

    public interface BlockedInterface {
        void onclick(boolean status);

        void reDirectToSpecifPage(String pageDeterminationValue);
    }
}
