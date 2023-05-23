package com.app.deliforcemanager.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.deliforcemanager.Activities.EarningsDetailsActivity;
import com.app.deliforcemanager.ModelClass.EarningsChart.EarningsList;
import com.app.deliforcemanager.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class EarningsListAdapter extends RecyclerView.Adapter<EarningsListAdapter.MyViewHolder> {

    private Context context;
    private List<EarningsList> taskEarningsInfoList;
    SimpleDateFormat ApiDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);

    SimpleDateFormat dateFormat =new SimpleDateFormat("dd MMMM",Locale.ENGLISH);
    SimpleDateFormat timeFormat =new SimpleDateFormat("hh:mm a",Locale.ENGLISH);

//    2019-06-08T05:10:29.462ZF

    public EarningsListAdapter(Context mContext, List<EarningsList> tasksList) {
        this.context = mContext;
        this.taskEarningsInfoList = tasksList;
    }

    @NonNull
    @Override
    public EarningsListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_earning_data, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EarningsListAdapter.MyViewHolder holder, int position) {
        try {
            EarningsList earningsList = taskEarningsInfoList.get(position);
            holder.txt_earnings_id.setText("ID: " + earningsList.getTaskId());

            holder.txt_earning_date.setText(dateFormat.format(ApiDateFormat.parse(earningsList.getDate())));
            holder.txt_earning_status.setText(context.getString(R.string.success));
            holder.txt_earnings_amount.setText("" + earningsList.getIndividualAmount());
            holder.txt_earnings_minutes.setText(earningsList.getCompletedTimeString());
            holder.txt_earnings_time.setText(timeFormat.format(ApiDateFormat.parse(earningsList.getDate()))+ " - " + earningsList.getBussinessTypeName());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return taskEarningsInfoList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_earnings_id, txt_earning_date, txt_earning_status, txt_earnings_amount, txt_earnings_minutes, txt_earnings_time;
        LinearLayout ll_earnings;

        MyViewHolder(View view) {
            super(view);
            ll_earnings = view.findViewById(R.id.ll_earnings);
            txt_earnings_id = view.findViewById(R.id.txt_earnings_id);
            txt_earning_date = view.findViewById(R.id.txt_earning_date);
            txt_earning_status = view.findViewById(R.id.txt_earning_status);
            txt_earnings_amount = view.findViewById(R.id.txt_earnings_amount);
            txt_earnings_minutes = view.findViewById(R.id.txt_earnings_minutes);
            txt_earnings_time = view.findViewById(R.id.txt_earnings_time);

            ll_earnings.setOnClickListener(view1 -> {
                final int position = getAdapterPosition();

//                ((Activity) context).startActivityForResult(new Intent(context, EarningsDetailsActivity.class).putExtra("task_id",
//                        taskEarningsInfoList.get(position).getId()), 536);

                context.startActivity(new Intent(context, EarningsDetailsActivity.class).putExtra("task_id",
                        taskEarningsInfoList.get(position).getId()));
            });
        }
    }

    public void notifyDataSetChanged(List<EarningsList> taskEarningsInfoList) {
        this.taskEarningsInfoList = taskEarningsInfoList;
        notifyDataSetChanged();
    }

}
