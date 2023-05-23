/*
package com.app.deliforce.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.deliforce.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class DescriptionImagesAdapter extends RecyclerView.Adapter<DescriptionImagesAdapter.MyViewHolder> {

    private Context context;
    private List<String> images_path_list;


    public DescriptionImagesAdapter(Context context, List<String> images_path_list) {
        this.context = context;
        this.images_path_list = images_path_list;
    }

    @NonNull
    @Override
    public DescriptionImagesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item_desc_imgs, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DescriptionImagesAdapter.MyViewHolder myViewHolder, int i) {

        Log.e("url", images_path_list.get(i));

        Glide.with(context).load(images_path_list.get(i)).into(myViewHolder.descImg);
    }

    @Override
    public int getItemCount() {
        return images_path_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView descImg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            descImg = itemView.findViewById(R.id.description_image);
        }
    }
}
*/
