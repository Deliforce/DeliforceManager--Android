package com.app.deliforcemanager.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.deliforcemanager.ModelClass.TaskDetailsApi.NotesList;
import com.app.deliforcemanager.R;
import com.app.deliforcemanager.Utils.DeliforceConstants;
import com.app.deliforcemanager.Utils.LoginPrefManager;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.List;

import timber.log.Timber;

public class DriverListAdapter extends RecyclerView.Adapter<DriverListAdapter.MyViewHolder> {

    private NotesAdapter.NotesManipulationListener notesManipulationListener;
    private List<NotesList> notesList;
    private LoginPrefManager loginPrefManager;

    private int type;

    public DriverListAdapter(Context context,List<NotesList> newNoteList) {
        this.notesList = newNoteList;
        loginPrefManager = new LoginPrefManager(context);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.driver_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {


    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        MyViewHolder(View view) {
            super(view);

        }
    }






}
