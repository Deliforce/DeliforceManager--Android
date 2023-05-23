/*
package com.app.deliforce.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.deliforce.ModelClass.StatusApi.Status;
import com.app.deliforce.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

*/
/**
 * Created by nextbrain on 18/12/18.
 *//*

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.FilterSubViewVH> {

    private List<Status> mData;
    private Context context;

    ArrayList<String> selectedStrings = new ArrayList<String>();
    private boolean mIsSelectAll = false;
    List<String> stringList;

    //pass one more param what ever you selected it should be a list
    public RecyclerAdapter(Context context, List<Status> commonDatas) {
        this.mData = commonDatas;
        this.context = context;
        //saving selected id's here
//        this.stringList = myList;

    }


    @NonNull
    @Override
    public FilterSubViewVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_status_view, parent, false);

        return new RecyclerAdapter.FilterSubViewVH(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.FilterSubViewVH holder, int position) {
        holder.cbService.setText(mData.get(position).getStatusname());

        //setting checkbox id
        holder.cbService.setId(mData.get(position).getId());

        //for position->0 it should select all value
        if (!mIsSelectAll) holder.cbService.setChecked(false);
        else holder.cbService.setChecked(true);

        //checkbox listener
        holder.cbService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //all select-> position 0
                    if (position == 0) {
                        //dummy purpose am saving 0 value here
                        //replace with all status id
                        selectedStrings.add("0");
                        selectAll();
                        Log.e("firstcheckBox", "setChecked(true)");
                    } else {


                        mData.get(position).setChecked(true);

                        selectedStrings.add("" + holder.cbService.getId());

                        Log.e("checkBox", "setChecked(true)");
                    }


                } else {
                    //all -> deselect position 0

                    if (position == 0) {
                        //dummy purpose am removing same 0 value here
                        //replace with all status id
                        selectedStrings.remove("0");
                        deselectAll();
                        Log.e("firstcheckBox", "setChecked(false)");

                    } else
                        {

                        Log.e("checkBox", "setChecked(false)");

                           mData.get(position).setChecked(false);

                          selectedStrings.remove("" + holder.cbService.getId());

                         if (selectedStrings.contains("0")) {
                             deselectfirstposition();
                         }
                 }

                }

            }
        });
    }

    private void deselectfirstposition() {

        mIsSelectAll=false;
        Status status=new Status();
        status.setChecked(false);
        notifyItemChanged(0);

    }


    private void deselectAll() {
        mIsSelectAll = false;//saving boolean value
        notifyDataSetChanged(); //reload the date
    }

    public void selectAll() {
        mIsSelectAll = true;  //saving boolean value
        notifyDataSetChanged(); //reload the date
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class FilterSubViewVH extends RecyclerView.ViewHolder {
        private TextView tvFilterServiceName;
        private CheckBox cbService;

        public FilterSubViewVH(View itemView) {
            super(itemView);
//            tvFilterServiceName = itemView.findViewById(R.id.tv_service_name);
            cbService = itemView.findViewById(R.id.status_box);

        }
    }

    //get the selected value here call the method
    public String CheckValuesMethod() {


        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < selectedStrings.size(); i++) {

            stringBuilder.append(selectedStrings.get(i));
            if (i != selectedStrings.size() - 1) {
                stringBuilder.append(",");
            }

        }
        Log.e("check_value", "" + stringBuilder.toString());

        return stringBuilder.toString();


    }


}






*/
