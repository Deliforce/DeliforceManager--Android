package com.app.deliforcemanager.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.deliforcemanager.ModelClass.TaskNotificationModel;
import com.app.deliforcemanager.R;
import com.app.deliforcemanager.Utils.LoginPrefManager;

import java.util.ArrayList;

public class TaskNotificationAdapter extends RecyclerView.Adapter<TaskNotificationAdapter.MyViewHolder> {

    Context context;
    LoginPrefManager loginPrefManager;
    private ArrayList<TaskNotificationModel> taskNotificationModels;

    public TaskNotificationAdapter(Context context, ArrayList<TaskNotificationModel> taskNotificationModels) {
        this.context = context;
        this.taskNotificationModels = taskNotificationModels;
        loginPrefManager = new LoginPrefManager(context);
    }

    @NonNull
    @Override
    public TaskNotificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_task_notification,parent,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TaskNotificationAdapter.MyViewHolder holder, int position) {
        holder.tv_cust_name.setText(taskNotificationModels.get(position).getCustomerName());
        holder.tv_cust_address.setText(taskNotificationModels.get(position).getCustomerAddress());
        holder.from_date.setText(taskNotificationModels.get(position).getStartdate());
        if(taskNotificationModels.get(position).getJobAmount()!=null &&
                !taskNotificationModels.get(position).getJobAmount().isEmpty()) {
            holder.tv_amount.setVisibility(View.VISIBLE);
            holder.tv_amount.setText(taskNotificationModels.get(position).getJobAmount()+ " " + loginPrefManager.getCurrency());
        } else {
            holder.tv_amount.setVisibility(View.GONE);
        }
        String taskdate = taskNotificationModels.get(position).getStartdate();
        String taskEndDate = taskNotificationModels.get(position).getEndDate();
        String taskEndTime = taskNotificationModels.get(position).getEndTime();
        String taskStartTime = taskNotificationModels.get(position).getStartTime();
        try {

            if (taskStartTime != null && taskdate != null) {
                holder.from_date.setText(loginPrefManager.changeNotificationDateForamat(taskdate) + " " +
                        loginPrefManager.changeNotificationTimeForamat(taskStartTime));
            }


            if (taskEndDate == null || taskEndTime == null) {
                holder.tv_end_date_time.setVisibility(View.GONE);
            } else if (taskEndDate.equals("null") || taskEndTime.equals("null")) {
                holder.tv_end_date_time.setVisibility(View.GONE);
            } else if (taskEndDate.equals("") || taskEndTime.equals("")) {
                holder.tv_end_date_time.setVisibility(View.GONE);
            } else if (taskEndDate.length() > 0 && taskEndTime.length() > 0) {
                holder.tv_end_date_time.setText("-" + loginPrefManager.changeNotificationDateForamat(taskEndDate)
                        + " " + loginPrefManager.changeNotificationTimeForamat(taskEndTime));
            }
            int bussType = 0;

            if(taskNotificationModels.get(position).getBusinessType()!=null) {
                bussType = Integer.parseInt(taskNotificationModels.get(position).getBusinessType());
            }

            boolean isPickUp = taskNotificationModels.get(position).getIsPickUp();

            if (bussType == 1 && isPickUp) {
                holder.tv_buss_type.setText(context.getString(R.string.pick_up));
                holder.tv_end_date_time.setVisibility(View.GONE);
            } else if (bussType == 1 && !isPickUp) {
                holder.tv_buss_type.setText(context.getString(R.string.delivery));
                holder.tv_end_date_time.setVisibility(View.GONE);
            } else if (bussType == 2) {
                holder.tv_buss_type.setText(context.getString(R.string.appointments));
                holder.tv_end_date_time.setVisibility(View.VISIBLE);  // original
            } else if (bussType == 3) {
                holder.tv_buss_type.setText(context.getString(R.string.field_work));
                holder.tv_end_date_time.setVisibility(View.VISIBLE);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return taskNotificationModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView from_date,tv_end_date_time,tv_buss_type,tv_cust_name,tv_cust_address,tv_amount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            from_date = itemView.findViewById(R.id.from_date);
            tv_end_date_time = itemView.findViewById(R.id.tv_end_date_time);
            tv_buss_type = itemView.findViewById(R.id.tv_buss_type);
            tv_cust_name = itemView.findViewById(R.id.tv_cust_name);
            tv_cust_address = itemView.findViewById(R.id.tv_cust_address);
            tv_amount = itemView.findViewById(R.id.tv_amount);
        }
    }
}
