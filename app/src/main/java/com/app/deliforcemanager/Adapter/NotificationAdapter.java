/*
package com.app.deliforce.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.deliforce.ModelClass.TaskDetailsApi.Address;
import com.app.deliforce.ModelClass.TaskDetailsApi.TaskList;
import com.app.deliforce.R;
import com.app.deliforce.Utils.LoginPrefManager;

import java.text.ParseException;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private Context context;
    private List<TaskList> notificationList;
    private LoginPrefManager loginPrefManager;

    public NotificationAdapter(Context context, List<TaskList> deletedNotificationList) {
        this.context = context;
        this.notificationList = deletedNotificationList;
        loginPrefManager = new LoginPrefManager(context);
    }

    @NonNull
    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notifications_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.MyViewHolder holder, int position) {
        for (TaskList taskListInfo : notificationList) {
            taskListInfo = notificationList.get(position);

            int bussType = taskListInfo.getBusinessType();
            boolean isPickUp = true;
            if (bussType == 1 && isPickUp) {
                holder.tvTaskType.setText(context.getString(R.string.task_type_pick_up));
            } else if (bussType == 2) {
                holder.tvTaskType.setText(context.getString(R.string.task_type_appointments));
            } else if (bussType == 3) {
                holder.tvTaskType.setText(context.getString(R.string.task_type_field_work));
            } else {
                holder.tvTaskType.setText(context.getString(R.string.task_type_delivery));
            }

            String actualDate = taskListInfo.getDate();
            Log.e("Task date n Time", "--> " + actualDate);
            Log.e("TAG", "-->" + actualDate);

            try {
                holder.tvTaskCompletionTime.setText(loginPrefManager.changeNotificationActivityDateForamat(actualDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            holder.tvTaskStatus.setTextColor(ContextCompat.getColor(context, R.color.colorTaskCancelled));
            holder.tvTaskStatus.setText(context.getString(R.string.task_status_deleted));
            Address details = taskListInfo.getAddress();
            String custAddress = details.getFormattedAddress();
            holder.tvCustLocation.setText(custAddress);
        }

    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTaskCompletionTime, tvTaskType, tvTaskStatus, tvCustLocation;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTaskCompletionTime = (TextView) itemView.findViewById(R.id.tv_task_time);
            tvTaskType = (TextView) itemView.findViewById(R.id.tv_task_name);
            tvTaskStatus = (TextView) itemView.findViewById(R.id.tv_task_status);
            tvCustLocation = (TextView) itemView.findViewById(R.id.tv_cust_address);
        }
    }
}
*/
