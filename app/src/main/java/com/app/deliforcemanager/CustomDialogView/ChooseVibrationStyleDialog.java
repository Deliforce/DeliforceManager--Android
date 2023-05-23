package com.app.deliforcemanager.CustomDialogView;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;

import com.app.deliforcemanager.R;

import timber.log.Timber;

public class ChooseVibrationStyleDialog extends Dialog {

    private RadioGroup vibrationRadioGroup;
    private SelectVibrationStyleListener selectVibrationStyleListener;

    public ChooseVibrationStyleDialog(@NonNull Context context, int themeResId, SelectVibrationStyleListener vibrationStyleListener) {
        super(context);
        this.selectVibrationStyleListener = vibrationStyleListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.alert_vibration_settings);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;

        vibrationRadioGroup = findViewById(R.id.radioVibrationStyleGroup);
        onClickEvents();
    }

    private void onClickEvents() {
        vibrationRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedItemId) {
                switch (checkedItemId) {
                    case R.id.radio_vibrate_long:
                        Timber.e("Vibrate Long");
                        selectVibrationStyleListener.onLongClick();
                        break;
                    case R.id.radio_vibrate_system:
                        Timber.e("Vibrate System");
                        selectVibrationStyleListener.onSystemClick();
                        break;
                }
            }
        });
    }

    public interface SelectVibrationStyleListener {

        void onLongClick();

        void onSystemClick();
    }
}
