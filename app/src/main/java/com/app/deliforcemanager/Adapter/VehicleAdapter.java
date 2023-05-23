package com.app.deliforcemanager.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.recyclerview.widget.RecyclerView;

import com.app.deliforcemanager.ModelClass.StatusApi.VehicleType;
import com.app.deliforcemanager.R;
import com.bumptech.glide.Glide;

import java.util.List;

import timber.log.Timber;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.MyViewHolder> {

    Context context;
    private List<VehicleType> vehicleTypes;
    private VehicleInterface vehicleInterface;
    private int selected = -1, vehicle_value;

    VehicleAdapter(Context context, List<VehicleType> vehicleTypes, int selected, VehicleInterface vehicleInterface) {
        this.context = context;
        this.vehicleTypes = vehicleTypes;
        this.vehicleInterface = vehicleInterface;
        this.vehicle_value = selected;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vehicle_adpater, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        Timber.e(String.valueOf(vehicle_value));
        myViewHolder.vehicle_name.setText(vehicleTypes.get(position).getName());

        Glide.with(context).load(vehicleTypes.get(position).getUrl()).into(myViewHolder.vehicle_image);




        Timber.e(selected + "/" + position);
        if (selected == position) {
            myViewHolder.vehicle_image.setColorFilter(context.getResources().getColor(R.color.color_black));

            myViewHolder.vehicle_radio.setChecked(true);
        } else {

            myViewHolder.vehicle_image.setColorFilter(context.getResources().getColor(R.color.colorLightGray));

            myViewHolder.vehicle_radio.setChecked(false);
        }

        if (vehicle_value > 0) {
            if (vehicle_value == vehicleTypes.get(position).getValue()) {

                myViewHolder.vehicle_image.setColorFilter(context.getResources().getColor(R.color.color_black));

                myViewHolder.vehicle_radio.setChecked(true);
            } else {

                myViewHolder.vehicle_image.setColorFilter(context.getResources().getColor(R.color.colorLightGray));

                myViewHolder.vehicle_radio.setChecked(false);
            }
        }
        myViewHolder.itemView.setOnClickListener(view -> {
            selected = position;

            if (vehicleInterface != null) {
                vehicleInterface.vehicleselected(true, vehicleTypes.get(position).getValue(), myViewHolder.vehicle_name.getText().toString());
            }

            notifyDataSetChanged();
        });
        myViewHolder.vehicle_radio.setOnCheckedChangeListener((compoundButton, b) -> {
            selected = position;

            if (vehicleInterface != null) {
                vehicleInterface.vehicleselected(true, vehicleTypes.get(position).getValue(), myViewHolder.vehicle_name.getText().toString());
            }

            notifyDataSetChanged();
        });


    }

    @Override
    public int getItemCount() {
        return vehicleTypes.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        AppCompatRadioButton vehicle_radio;
        TextView vehicle_name;
        ImageView vehicle_image;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            vehicle_image = itemView.findViewById(R.id.vehicle_img);
            vehicle_name = itemView.findViewById(R.id.vehicle_name);
            vehicle_radio = itemView.findViewById(R.id.vehicle_radio);
        }
    }

    public interface VehicleInterface {
        void vehicleselected(boolean status, int postion, String name);
    }
}
