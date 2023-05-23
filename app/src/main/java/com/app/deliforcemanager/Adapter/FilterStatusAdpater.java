package com.app.deliforcemanager.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.deliforcemanager.ModelClass.StatusApi.Status;
import com.app.deliforcemanager.R;
import com.app.deliforcemanager.Utils.LoginPrefManager;

import java.util.List;

public class FilterStatusAdpater extends RecyclerView.Adapter<FilterStatusAdpater.MyViewHolder> {

    private LoginPrefManager loginPrefManager;
    private List<Status> statusList;
    private OnClickListener onClickListener;

    public FilterStatusAdpater(Context context, List<Status> statusList,
                               OnClickListener onClickListener) {
        this.statusList = statusList;
        this.onClickListener = onClickListener;
        loginPrefManager = new LoginPrefManager(context);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.filter_status_view, viewGroup, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.status_box.setText(statusList.get(position).getStatusname());
        myViewHolder.status_box.setChecked(statusList.get(position).isChecked());
        myViewHolder.itemView.setOnClickListener(v -> onClickListener.onClick(position));
    }

    @Override
    public int getItemCount() {
        return statusList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private CheckBox status_box;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            status_box = itemView.findViewById(R.id.status_box);
        }
    }

    public interface OnClickListener {
        void onClick(int position);
    }

    public void notifyDataSetChanged(List<Status> statusList) {
        this.statusList = statusList;
        notifyDataSetChanged();
    }
}
