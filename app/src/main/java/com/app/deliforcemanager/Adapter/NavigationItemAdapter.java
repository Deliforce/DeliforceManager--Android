package com.app.deliforcemanager.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.deliforcemanager.R;
import com.app.deliforcemanager.Utils.DrawerItem;
import com.app.deliforcemanager.Utils.LoginPrefManager;

import java.util.ArrayList;


public class NavigationItemAdapter extends RecyclerView.Adapter<NavigationItemAdapter.HomeViewHolder> {

    private final Context context;
    private ArrayList<DrawerItem> drawerItems;
    private ClickEventInterface clickEventInterface;
    private static int selectedPosition = 0;
    private LoginPrefManager loginPrefManager;

    public NavigationItemAdapter(Context ctx, ArrayList<DrawerItem> drawerItems, ClickEventInterface clickEventInterface) {
        this.context = ctx;
        this.drawerItems = drawerItems;
        this.clickEventInterface = clickEventInterface;
        loginPrefManager=new LoginPrefManager(context);
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_adpter, viewGroup, false);
        return new HomeViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder homeViewHolder, int i) {

        homeViewHolder.txtMenuTitle.setText(drawerItems.get(i).getTitle());
        homeViewHolder.imgMenuIcon.setImageResource(drawerItems.get(i).getIcon());

        if (drawerItems.get(i).getTitle().equalsIgnoreCase("Car")) {
            homeViewHolder.txtMenuTitle.setVisibility(View.GONE);
            homeViewHolder.imgMenuIcon.setVisibility(View.GONE);
        } else {
            homeViewHolder.txtMenuTitle.setVisibility(View.VISIBLE);
            homeViewHolder.imgMenuIcon.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return drawerItems.size();
    }

    /*creating viewHolder for Normal view*/
    class HomeViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llView;
        TextView txtMenuTitle;
        ImageView imgMenuIcon;


        HomeViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            txtMenuTitle = itemView.findViewById(R.id.grid_text);
            imgMenuIcon = itemView.findViewById(R.id.grid_image);
            llView = itemView.findViewById(R.id.adapter_item);


            itemView.setOnClickListener(view -> {
                clickOnItemView(getLayoutPosition(), itemView, getAdapterPosition(),txtMenuTitle.getText().toString());

                if (drawerItems.get(getAdapterPosition()).getTitle().equalsIgnoreCase("Help")
                        || drawerItems.get(getAdapterPosition()).getTitle().equalsIgnoreCase("Logout")
                        || drawerItems.get(getAdapterPosition()).getTitle().equalsIgnoreCase("Ayuda")
                        || drawerItems.get(getAdapterPosition()).getTitle().equalsIgnoreCase("Salir")
                        || drawerItems.get(getAdapterPosition()).getTitle().equalsIgnoreCase("Car")) {

                } else {
                    llView.setBackgroundResource(R.color.colorLightGray);
                    txtMenuTitle.setTextColor(context.getResources().getColor(R.color.color_white));
                }
            });
        }
    }

    /* common item click view */
    private void clickOnItemView(int layoutPosition, View itemView, int adapterPosition,String title) {
           /* Below line is just like a safety check, because sometimes holder could be null,
                 in that case, getAdapterPosition() will return RecyclerView.NO_POSITION*/
        if (adapterPosition == RecyclerView.NO_POSITION) return;
        if (selectedPosition != layoutPosition) {
            int lastSelectedPosition = selectedPosition;
            selectedPosition = layoutPosition;
            notifyItemChanged(lastSelectedPosition);
            itemView.setSelected(true);
        }


        if (clickEventInterface != null) {
            clickEventInterface.selectedItem(title);
        }

    }

    /* Interface to get clicked position which is implemented in activity*/
    public interface ClickEventInterface {
        void selectedItem(String title);
    }


}
