package com.app.deliforcemanager.CustomDialogView;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;

import com.app.deliforcemanager.R;
import com.app.deliforcemanager.Utils.DeliforceConstants;

public class ChooseMapDialog extends Dialog {

    private RadioGroup radioNavigationSelectionGroup;
    private OnClickMapListener onClickMapListener;
    private String mapType;

    public ChooseMapDialog(@NonNull Context context, int themeResId, String mapType,
                           OnClickMapListener appLangChangeListener) {
        super(context);
        this.onClickMapListener = appLangChangeListener;
        this.mapType = mapType;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.alert_choose_map);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        radioNavigationSelectionGroup = findViewById(R.id.radioNavigationSelectionGroup);
        RadioButton radio_waze = findViewById(R.id.radio_waze);
        RadioButton radio_google_map = findViewById(R.id.radio_google_map);

        if (mapType.equalsIgnoreCase(DeliforceConstants.NavigationGoogleMap)) {
            radio_google_map.setChecked(true);
        } else if (mapType.equalsIgnoreCase(DeliforceConstants.NavigationWaze)) {
            radio_waze.setChecked(true);
        }
        onClickEvent();
    }

    private void onClickEvent() {

        radioNavigationSelectionGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radio_google_map:
                    onClickMapListener.onGoogleMapSelected();
                    dismiss();
                    break;
                case R.id.radio_waze:
                    onClickMapListener.onWazeSelected();
                    dismiss();
                    break;
            }
        });

    }

    public interface OnClickMapListener {

        void onGoogleMapSelected();

        void onWazeSelected();
    }
}
