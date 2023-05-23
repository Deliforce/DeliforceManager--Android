package com.app.deliforcemanager.Adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.deliforcemanager.R;

import java.util.List;

import timber.log.Timber;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.MyViewHolder> {
    private SelectedOptionInterface selectedOptionInterface;
    private Context context;
    private int selected_position = 2;
    private List<String> time_array;

    public StatusAdapter(Context context, List<String> time_array, SelectedOptionInterface selectedOptionInterface) {
        this.context = context;
        this.time_array = time_array;
        this.selectedOptionInterface = selectedOptionInterface;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.status_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.menu_item_name.setText(time_array.get(position));
        if (selected_position == position) {

            Timber.e("-%s", selected_position);
            holder.menu_item_name.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.line.setVisibility(View.VISIBLE);

            if (selectedOptionInterface != null) {
                selectedOptionInterface.selectedOption(time_array.get(selected_position));
            }
        } else {
            holder.menu_item_name.setTextColor(ContextCompat.getColor(context, R.color.colorLightGray));
//            holder.menu_item_name.setPaintFlags(0);
            holder.line.setVisibility(View.GONE);
        }


        holder.menu_item_name.setOnClickListener(view -> {
            selected_position = position;
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return time_array.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView menu_item_name, line;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            menu_item_name = itemView.findViewById(R.id.status);
            line = itemView.findViewById(R.id.line);
        }
    }

    public interface SelectedOptionInterface {
        void selectedOption(String timevalue);
    }

    public void SelectedOptionMethod(SelectedOptionInterface selectedOptionInterface) {
        this.selectedOptionInterface = selectedOptionInterface;
    }
}