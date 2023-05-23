package com.app.deliforcemanager.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.app.deliforcemanager.ModelClass.StatusApi.VehicleType;
import com.app.deliforcemanager.R;

import java.util.List;

public class VehicleDialog extends Dialog {

    private List<VehicleType> vehicleTypes;
    Context context;
    private int type;
    private DialogInterface dialogInterface;

    public VehicleDialog(@NonNull Context context, int themeResId, int type, List<VehicleType> vehicleTypes, DialogInterface dialogInterface) {
        super(context, themeResId);
        this.context = context;
        this.vehicleTypes = vehicleTypes;
        this.type = type;
        this.dialogInterface = dialogInterface;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.custom_vehicle_dialog);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;

        RecyclerView vehicle_view = findViewById(R.id.vehicle_view);
        TextView tvNoVehicle = findViewById(R.id.no_vechile);

        vehicle_view.setLayoutManager(new LinearLayoutManager(context));
        if (vehicleTypes.size() > 0) {
            vehicle_view.setVisibility(View.VISIBLE);
            vehicle_view.setAdapter(new VehicleAdapter(context, vehicleTypes, type, new VehicleAdapter.VehicleInterface() {
                @Override
                public void vehicleselected(boolean status, int postion, String name) {
                    if (dialogInterface != null) {
                        dialogInterface.vehicle_dialog(true, postion, name);
                    }
                }
            }));
        } else {
            tvNoVehicle.setVisibility(View.VISIBLE);
            vehicle_view.setVisibility(View.GONE);
        }
    }

    public interface DialogInterface {
        void vehicle_dialog(boolean status, int postion, String value);
    }
}
