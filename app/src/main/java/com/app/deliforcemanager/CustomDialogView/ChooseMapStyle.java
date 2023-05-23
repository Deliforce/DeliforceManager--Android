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

public class ChooseMapStyle extends Dialog {

    private RadioGroup selectMapStyle;
    private ChooseMapStyleListener mapStyleListener;
    private int map_style;

    public ChooseMapStyle(@NonNull Context con, int themeResId, int map_style, ChooseMapStyleListener styleListener) {
        super(con);
        this.map_style = map_style;
        this.mapStyleListener = styleListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.choose_map_style_dialog);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;

        selectMapStyle = findViewById(R.id.radioMapStyleSelectionGroup);

        RadioButton normal_btn = findViewById(R.id.radio_map_style_normal);
        RadioButton night_btn = findViewById(R.id.radio_map_style_night);

        if (map_style == 1) {
            normal_btn.setChecked(true);
        } else {
            night_btn.setChecked(true);
        }
        onClickEvent();
    }

    private void onClickEvent() {

        selectMapStyle.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radio_map_style_normal:
                    mapStyleListener.onNormalMapStyleClick();
                    break;
                case R.id.radio_map_style_night:
                    mapStyleListener.onNightModeStyleClick();
                    break;
            }

        });

    }

    public interface ChooseMapStyleListener {
        void onNormalMapStyleClick();

        void onNightModeStyleClick();
    }
}
