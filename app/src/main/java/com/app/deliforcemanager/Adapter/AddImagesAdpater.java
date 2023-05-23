package com.app.deliforcemanager.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.app.deliforcemanager.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import timber.log.Timber;

public class AddImagesAdpater extends RecyclerView.Adapter<AddImagesAdpater.MyViewHolder> {

    private final Context context;
    private final List<String> images_path_list;
    private final AddImageInterface addImageInterface;
    private final String type;

    public AddImagesAdpater(Context context, String type, List<String> images_path_list, AddImageInterface addImageInterface) {
        this.context = context;
        this.images_path_list = images_path_list;
        this.addImageInterface = addImageInterface;
        this.type = type;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_dashboard_menus, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Timber.e(images_path_list.get(i));

        Glide.with(context).load(images_path_list.get(i)).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(myViewHolder.task_images);

        Glide.with(context).load(images_path_list.get(i)).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                myViewHolder.loading_view.setVisibility(View.GONE);
                Toast.makeText(context, context.getString(R.string.unable_load_imgs), Toast.LENGTH_SHORT).show();

                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                myViewHolder.task_images.setVisibility(View.VISIBLE);
                myViewHolder.loading_view.setVisibility(View.GONE);
                return false;
            }
        }).into(myViewHolder.task_images);

        if (type.equals("6")) {
            myViewHolder.delete_img.setVisibility(View.GONE);
        }

        myViewHolder.delete_img.setOnClickListener(view -> {
            if (addImageInterface != null) {
                addImageInterface.imageDeleted(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return images_path_list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView task_images;
        private final ImageView delete_img;
        private final ProgressBar loading_view;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            task_images = itemView.findViewById(R.id.task_image);
            delete_img = itemView.findViewById(R.id.delete);
            loading_view = itemView.findViewById(R.id.loading_view);
        }
    }

    public interface AddImageInterface {
        void imageDeleted(int position);
    }

}
