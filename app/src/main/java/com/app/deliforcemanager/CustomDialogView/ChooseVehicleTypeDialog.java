/*
package com.app.deliforce.CustomDialogView;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;

import com.app.deliforce.R;

public class ChooseVehicleTypeDialog extends Dialog {

    private static final String TAG="ChooseVehicleTypeDialog";

    private Context context;
    private RadioGroup vehicleTypeRadioGrp;
    SelectVehicleListener vehicleTypeListener;

    public ChooseVehicleTypeDialog(@NonNull Context context, int themeResId, SelectVehicleListener selectVehicleListener) {
        super(context);
        this.context = context;
        this.vehicleTypeListener = selectVehicleListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.alert_settings_choose_vehicle_dialog);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;

        vehicleTypeRadioGrp = (RadioGroup) findViewById(R.id.radioVahicleTypeGroup);
        onClickEvents();

    }

    private void onClickEvents(){

        vehicleTypeRadioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId){
                    case R.id.radio_walking:
                        Log.e(TAG, "Walking");
                        vehicleTypeListener.onWalkingClick();
                        break;
                    case R.id.radio_cycle:
                        Log.e(TAG, "Cycle");
                        vehicleTypeListener.onCycleClick();
                        break;
                    case R.id.radio_scooter:
                        Log.e(TAG, "Scooter");
                        vehicleTypeListener.onScooterClick();
                        break;
                    case R.id.radio_bike:
                        Log.e(TAG, "Bike");
                        vehicleTypeListener.onBikeClick();
                        break;
                    case R.id.radio_car:
                        Log.e(TAG, "Car");
                        vehicleTypeListener.onCarClick();
                        break;
                }
            }
        });
    }


    public interface SelectVehicleListener {
        void onWalkingClick();
        void onCycleClick();
        void onScooterClick();
        void onBikeClick();
        void onCarClick();
    }
}
*/
