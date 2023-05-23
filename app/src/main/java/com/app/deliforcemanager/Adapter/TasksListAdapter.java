package com.app.deliforcemanager.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.deliforcemanager.ModelClass.TaskDetailsApi.Address;
import com.app.deliforcemanager.ModelClass.TaskDetailsApi.TaskList;
import com.app.deliforcemanager.R;
import com.app.deliforcemanager.Utils.AppUtils;
import com.app.deliforcemanager.Utils.DeliforceConstants;
import com.app.deliforcemanager.Utils.LoginPrefManager;

import java.util.List;

import timber.log.Timber;

public class TasksListAdapter extends RecyclerView.Adapter<TasksListAdapter.MyViewHolder> {

    private Context context;
    private List<TaskList> taskListInfoList;

    private LoginPrefManager loginPrefManager;
    TasksListUpdatedAdapter.TasksListUpdatedListener taskListListener;

    public TasksListAdapter(Context mContext, List<TaskList> tasksList,
                            TasksListUpdatedAdapter.TasksListUpdatedListener taskListListener) {
        this.context = mContext;
        this.taskListInfoList = tasksList;
        this.taskListListener = taskListListener;
        loginPrefManager = new LoginPrefManager(context);
    }

    @NonNull
    @Override
    public TasksListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tasks_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TasksListAdapter.MyViewHolder holder, int position) {

        try {
            for (TaskList taskListInfo : taskListInfoList) {
                taskListInfo = taskListInfoList.get(position);
                holder.tvCustName.setText(taskListInfo.getName());
                boolean isPickUp;

                int bussType = taskListInfo.getBusinessType();


                Log.e("businessType",String.valueOf(bussType));

                switch (bussType) {

                    case 1:
                        isPickUp = taskListInfo.getIsPickup();

                        if (loginPrefManager.isCustomerCategoryEnabled()){
                            if(isPickUp){
                                holder.tvTaskType.setText(loginPrefManager.getPickupText());
                            }else{
                                holder.tvTaskType.setText(loginPrefManager.getDeliveryText());
                            }
                        }else {
                            if (isPickUp) {
                                holder.tvTaskType.setText(context.getString(R.string.task_type_pick_up));
                            } else {
                                holder.tvTaskType.setText(context.getString(R.string.task_type_delivery));
                            }
                        }
                        break;
                    case 2:

                        if (loginPrefManager.isCustomerCategoryEnabled()){
                            holder.tvTaskType.setText(loginPrefManager.getAppointmentText());
                        }else{
                            isPickUp = Boolean.parseBoolean(null);
                            holder.tvTaskType.setText(context.getString(R.string.task_type_appointments));
                        }
                        break;

                    case 3:
                        if (loginPrefManager.isCustomerCategoryEnabled()){
                            holder.tvTaskType.setText(loginPrefManager.getFieldWorkForceText());
                        }else {
                            isPickUp = Boolean.parseBoolean(null);
                            holder.tvTaskType.setText(context.getString(R.string.task_type_field_work));
                        }
                        break;
                }


                String order_id = taskListInfoList.get(position).getOrderId();


                if (!order_id.equals("")) {
                    holder.tvOrderId.setText(order_id);
                    holder.tvOrderId.setVisibility(View.VISIBLE);
                } else {
                    holder.tvOrderId.setText("-----------------");
                    holder.tvOrderId.setVisibility(View.GONE);

                }


                String actualDate = taskListInfo.getDate();
                String formattedTime = (actualDate.length() == 22) ? actualDate.substring(11, 16) : "0" + actualDate.substring(11, 16);
                String timeAmPm = (actualDate.length() == 22) ? actualDate.substring(19, 22) : actualDate.substring(19, 21);
                String dateToDisplay = formattedTime + timeAmPm;
                holder.tvTaskCompletionTime.setText(dateToDisplay);


                holder.tvTaskStatus.setTextColor(Color.parseColor(taskListInfoList.get(position).getColor()));

                holder.start_view.setBackgroundColor(Color.parseColor(taskListInfoList.get(position).getColor()));

                int taskStatus = taskListInfo.getTaskStatus();
                String statusName = AppUtils.getStatus(context, taskStatus);
                Log.e("taskStatus",""+taskStatus);
                holder.tvTaskStatus.setText(statusName);

                if (taskStatus== DeliforceConstants.TASK_ARRIVED){
                    loginPrefManager.setStringValue("arrived_status", "true");
                }else{
                    loginPrefManager.setStringValue("arrived_status", "false");
                }

                switch (taskStatus) {
                    case 2:
                        holder.tvTaskStatus.setText(context.getString(R.string.task_status_assigned));
                        break;
                    case 3:
                        holder.tvTaskStatus.setText(context.getString(R.string.task_status_accepted));
                        break;
                    case 4:
                        holder.tvTaskStatus.setText(context.getString(R.string.task_status_started));
                        break;
                    case 5:
                        holder.tvTaskStatus.setText(context.getString(R.string.task_status_in_progress));
                        break;
                    case 6:
                        holder.tvTaskStatus.setText(context.getString(R.string.task_status_success));
                        break;
                    case 7:
                        holder.tvTaskStatus.setText(context.getString(R.string.task_status_failed));
                        break;
                    case 9:
                        holder.tvTaskStatus.setText(context.getString(R.string.task_status_cancelled));
                        break;
                    case 10:
                        holder.tvTaskStatus.setText(context.getString(R.string.task_status_acknowledge));
                        break;
                }




                Address details = taskListInfo.getAddress();
                String custAddress = details.getFormattedAddress();
                if(!taskListInfo.getFlatNo().isEmpty())
                    custAddress = taskListInfo.getFlatNo() +", "+custAddress;
                holder.tvCustLocation.setText(custAddress);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Timber.tag("Exception_list").e(e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return taskListInfoList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTaskCompletionTime, tvTaskType, tvTaskStatus, tvCustName, tvCustLocation, tvOrderId;
        View start_view;
        RelativeLayout card_view_list_item;


        MyViewHolder(View view) {
            super(view);
            card_view_list_item = view.findViewById(R.id.card_view_list_item);
            tvTaskCompletionTime = view.findViewById(R.id.tv_task_time);
            tvTaskType = view.findViewById(R.id.tv_task_name);
            tvTaskStatus = view.findViewById(R.id.tv_task_status);
            tvCustName = view.findViewById(R.id.tv_cust_name);
            tvCustLocation = view.findViewById(R.id.tv_cust_address);
            start_view = view.findViewById(R.id.start_view);
            tvOrderId = view.findViewById(R.id.order_id);

            card_view_list_item.setOnClickListener(view1 -> {
                final int position = getAdapterPosition();
                 if (taskListListener!=null){
                     taskListListener.taskItemClick(taskListInfoList.get(position));
                 }
            });

            tvCustLocation.setOnClickListener(view13 -> card_view_list_item.performClick());

            tvCustName.setOnClickListener(view12 -> card_view_list_item.performClick());
        }
    }
}
