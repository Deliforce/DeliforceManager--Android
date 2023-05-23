/*
package com.app.deliforce.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.deliforce.R;

public class DashMenuGridAdapter extends BaseAdapter {

    private Context context;
    private final String[] menus;
    private final int[] imgId;

    public DashMenuGridAdapter(Context c, String[] menus, int[] img_id) {
        context = c;
        this.imgId = img_id;
        this.menus = menus;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return menus.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated xstub
        View grid;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(context);
            grid = inflater.inflate(R.layout.menu_adpter, null);
            TextView textView = grid.findViewById(R.id.grid_text);
            ImageView imageView = grid.findViewById(R.id.grid_image);
            textView.setText(menus[position]);
            imageView.setImageResource(imgId[position]);
        } else {
            grid = convertView;
        }

        return grid;
    }

}
*/
