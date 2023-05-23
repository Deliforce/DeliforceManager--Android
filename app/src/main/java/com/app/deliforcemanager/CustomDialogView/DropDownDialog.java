package com.app.deliforcemanager.CustomDialogView;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.deliforcemanager.Adapter.DropDownAdapter;
import com.app.deliforcemanager.ModelClass.TaskDetailsApi.DropDownOptions;
import com.app.deliforcemanager.R;

import java.util.List;

public class DropDownDialog extends Dialog {

    private List<DropDownOptions> dropDownOptions;
    Context context;
    private String type;
    private DropDialogInterface dialogInterface;

    public DropDownDialog(@NonNull Context context, int themeResId, String type, List<DropDownOptions> vehicleTypes, DropDialogInterface dialogInterface) {
        super(context, themeResId);
        this.context = context;
        this.dropDownOptions = vehicleTypes;
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
        TextView tvTitle = findViewById(R.id.tvTitle);


        tvTitle.setText(""+context.getString(R.string.please_select));


        vehicle_view.setLayoutManager(new LinearLayoutManager(context));
        if (dropDownOptions.size() > 0) {
            vehicle_view.setVisibility(View.VISIBLE);
            vehicle_view.setAdapter(new DropDownAdapter(context, dropDownOptions, type, new DropDownAdapter.AdapterInterface() {
                @Override
                public void selectedItem(DropDownOptions selectedItem) {
                    dismiss();
                    if (dialogInterface!=null){
                        dialogInterface.dropView(selectedItem);
                    }

                }
            }));
        } else {
            tvNoVehicle.setVisibility(View.VISIBLE);
            tvNoVehicle.setText(context.getString(R.string.no_data_found));
            vehicle_view.setVisibility(View.GONE);
        }
    }

    public interface DropDialogInterface {
        void dropView(DropDownOptions value);
    }
}
