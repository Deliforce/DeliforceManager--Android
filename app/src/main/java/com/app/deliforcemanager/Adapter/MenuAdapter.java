/*
package com.app.deliforce.Adapter;

import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.deliforce.R;
*/
/**
 * Created by nextbrain on 27/5/18.
 *//*


public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {

    private Context context;
    private String[] name_list;
    private int[] image_list;
    int selecte_position = -1;


    public MenuAdapter(Context context, String[] name_list, int[] image_list) {
        this.context = context;
        this.image_list = image_list;
        this.name_list = name_list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_dashboard_menus, parent, false);


        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {


        holder.itemView.setPadding(2, 2, 2, 2);
        holder.os_text.setText(name_list[position]);
        holder.os_img.setImageResource(image_list[position]);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecte_position = position;

            }
        });

    }

    @Override
    public int getItemCount() {
        return name_list.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView os_text;
        ImageView os_img;
        View view;


        public MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }
    }
}
*/
