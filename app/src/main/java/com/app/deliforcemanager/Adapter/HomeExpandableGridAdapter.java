/*
package com.app.deliforce.Adapter;

import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.deliforce.R;
import java.util.List;


public class HomeExpandableGridAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private List<String> item_name;
    private int[] imageId;
    private Context context;
    int selecte_position = -1;


    public HomeExpandableGridAdapter(Context context, List<String> item_name, int[] osImages) {
        // TODO Auto-generated constructor stub
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.item_name = item_name;
        imageId = osImages;
        this.context = context;


    }

    @Override
    public int getCount() {
        return this.item_name.size();
    }


    @Override
    public Object getItem(int position) {

        return this.item_name.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        protected ImageView ho_sto_row_img_view;
        protected TextView ho_sto_row_name_txt_view;
        private LinearLayout linearLayout;

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {


        final Holder holder = new Holder();
        View rowView = inflater.inflate(R.layout.grid_view_item, parent, false);

        holder.ho_sto_row_img_view = rowView.findViewById(R.id.ho_sto_row_img_view);


        holder.linearLayout = rowView.findViewById(R.id.item_view);
        holder.ho_sto_row_img_view.setImageResource(imageId[position]);
        holder.ho_sto_row_name_txt_view = rowView.findViewById(R.id.ho_sto_row_name_txt_view);
        holder.ho_sto_row_name_txt_view.setText(item_name.get(position));

        if (selecte_position == position) {
            holder.linearLayout.setBackgroundColor(context.getColor(R.color.btn_color));
            holder.ho_sto_row_name_txt_view.setTextColor(context.getColor(R.color.color_white));
        } else {
            holder.linearLayout.setBackgroundColor(context.getColor(R.color.color_white));
            holder.ho_sto_row_name_txt_view.setTextColor(context.getColor(R.color.btn_color));
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selecte_position = position;

                notifyDataSetChanged();
                Log.e("slected position", String.valueOf(position));
            }
        });


        rowView.setPadding(2, 2, 2, 2);
        return rowView;
    }

}*/
