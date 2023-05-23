package com.app.deliforcemanager.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.app.deliforcemanager.Activities.TaskDetailsActivity;
import com.app.deliforcemanager.ModelClass.ManagerTaskList.Doc;
import com.app.deliforcemanager.ModelClass.TaskDetailsApi.TaskList;
import com.app.deliforcemanager.R;
import com.app.deliforcemanager.Utils.AppUtils;
import com.app.deliforcemanager.Utils.DeliforceConstants;
import com.app.deliforcemanager.Utils.LoginPrefManager;

import java.util.List;

import timber.log.Timber;

public class TasksListUpdatedAdapter extends RecyclerView.Adapter<TasksListUpdatedAdapter.MyViewHolder> {

    private Context context;
    private List<Doc> tasksList;

    private LoginPrefManager loginPrefManager;

    public TasksListUpdatedAdapter(Context mContext, List<Doc> tasksList) {
        this.context = mContext;
        this.tasksList = tasksList;
        loginPrefManager = new LoginPrefManager(context);
    }

    @NonNull
    @Override
    public TasksListUpdatedAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tasks_list_updated_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       // holder.rvTasks.setLayoutManager(new LinearLayoutManager(context));


        try {


            holder.tv_task_name.setText(tasksList.get(position).getName());

            holder.tv_cust_address.setText(tasksList.get(position).getAddress().getFormattedAddress());

            holder.order_id.setText(tasksList.get(position).getTaskId());

            holder.tv_cust_name.setText(tasksList.get(position).getDriver());


            int taskStatus = tasksList.get(position).getTaskStatus();
            //String statusName = AppUtils.getStatus(context, taskStatus);
            Log.e("taskStatus",""+taskStatus);


            String actualDate = tasksList.get(position).getDate();
            String formattedTime = (actualDate.length() == 22) ? actualDate.substring(11, 16) : "0" + actualDate.substring(11, 16);
            String timeAmPm = (actualDate.length() == 22) ? actualDate.substring(19, 22) : actualDate.substring(19, 21);
            String dateToDisplay = formattedTime + timeAmPm;
            holder.tv_task_time.setText(dateToDisplay);
          //  holder.tv_task_status.setText(statusName);



            if (taskStatus== DeliforceConstants.TASK_ARRIVED){
                loginPrefManager.setStringValue("arrived_status", "true");
            }else{
                loginPrefManager.setStringValue("arrived_status", "false");
            }


            holder.tv_task_status.setTextColor(Color.parseColor(tasksList.get(position).getTaskColor()));

            holder.start_view.setBackgroundColor(Color.parseColor(tasksList.get(position).getTaskColor()));

            switch (taskStatus) {
                case 1:
                    holder.tv_task_status.setText("UnAssigned");
                    break;
                case 2:
                    holder.tv_task_status.setText(context.getString(R.string.task_status_assigned));
                    break;
                case 3:
                    holder.tv_task_status.setText(context.getString(R.string.task_status_accepted));
                    break;
                case 4:
                    holder.tv_task_status.setText(context.getString(R.string.task_status_started));
                    break;
                case 5:
                    holder.tv_task_status.setText(context.getString(R.string.task_status_in_progress));
                    break;
                case 6:
                    holder.tv_task_status.setText(context.getString(R.string.task_status_success));
                    break;
                case 7:
                    holder.tv_task_status.setText(context.getString(R.string.task_status_failed));
                    break;
                case 9:
                    holder.tv_task_status.setText(context.getString(R.string.task_status_cancelled));
                    break;
                case 10:
                    holder.tv_task_status.setText(context.getString(R.string.task_status_acknowledge));
                    break;
            }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent =  new Intent(context,TaskDetailsActivity.class);
                    intent.putExtra("task_id",tasksList.get(position).getId());
                    intent.putExtra("activity","navigation");
                    intent.putExtra("glympse_id","");
                    context.startActivity(intent);

                }
            });

        }catch (Exception e){
            e.printStackTrace();
            Timber.tag("Task_List_Exception").e(e.getMessage());
        }


//        holder.tasksListAdapter = new TasksListAdapter(context,tasksList.get(position).getTaskDetails(),taskListListener);
//        holder.rvTasks.setAdapter(holder.tasksListAdapter);
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }

    public void addData(List<Doc> taskDetails) {

        tasksList.addAll(taskDetails);
        notifyItemInserted(tasksList.size() - 1);

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
      TextView tv_task_name,tv_cust_address,order_id,tv_cust_name,tv_task_status,tv_task_time;
      View start_view;



        MyViewHolder(View view) {
            super(view);
            tv_task_name= view.findViewById(R.id.tv_task_name);
            tv_cust_address=view.findViewById(R.id.tv_cust_address);
            order_id= view.findViewById(R.id.order_id);
            tv_cust_name = view.findViewById(R.id.tv_cust_name);
            tv_task_status = view.findViewById(R.id.tv_task_status);
            start_view = view.findViewById(R.id.start_view);
            tv_task_time = view.findViewById(R.id.tv_task_time);

            /*card_view_list_item.setOnClickListener(view1 -> {
                final int position = getAdapterPosition();
                 if (taskListListener!=null){
                     taskListListener.taskItemClick(taskListInfoList.get(position));
                 }
            });*/
        }
    }
    private TasksListUpdatedListener taskListListener;

    public interface TasksListUpdatedListener {
        void taskItemClick(TaskList taskList);
    }


    public  void setAdapterClickInterface(TasksListUpdatedListener clickInterface){
        this.taskListListener =clickInterface;
    }
}
