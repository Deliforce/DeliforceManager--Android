package com.app.deliforcemanager.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.deliforcemanager.ModelClass.TaskDetailsApi.DropDownOptions;
import com.app.deliforcemanager.R;

import java.util.List;

import timber.log.Timber;

public class DropDownAdapter extends RecyclerView.Adapter<DropDownAdapter.MyViewHolder> {

    Context context;
    private List<DropDownOptions> vehicleTypes;
    private AdapterInterface adapterInterface;
    private int selected = -1;
    private String previousItem;

    public DropDownAdapter(Context context, List<DropDownOptions> vehicleTypes,String selected, AdapterInterface vehicleInterface) {
        this.context = context;
        this.vehicleTypes = vehicleTypes;
        this.adapterInterface = vehicleInterface;
        this.previousItem = selected;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vehicle_adpater, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.vehicle_name.setText(vehicleTypes.get(position).getValue());
        myViewHolder.vehicle_name.setTextColor(ContextCompat.getColor(context,R.color.colorLightGray));



        Timber.e(selected + "/" + position);
        if (selected == position) {
            myViewHolder.vehicle_radio.setChecked(true);
            myViewHolder.vehicle_name.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));

        } else {

            myViewHolder.vehicle_radio.setChecked(false);
            myViewHolder.vehicle_name.setTextColor(ContextCompat.getColor(context,R.color.colorLightGray));

        }

        if (!previousItem.isEmpty()) {
            if (previousItem.equals(vehicleTypes.get(position).getValue())) {
                myViewHolder.vehicle_radio.setChecked(true);
                myViewHolder.vehicle_name.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));

            } else {
                myViewHolder.vehicle_radio.setChecked(false);
                myViewHolder.vehicle_name.setTextColor(ContextCompat.getColor(context,R.color.colorLightGray));

            }
        }
        myViewHolder.itemView.setOnClickListener(view -> {
            selected = position;

            if (adapterInterface != null) {
                adapterInterface.selectedItem(vehicleTypes.get(position));
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
            vehicle_radio.setClickable(false);
            vehicle_image.setVisibility(View.GONE);
        }
    }

    public interface AdapterInterface {
        void selectedItem(DropDownOptions selectedItem);
    }
}