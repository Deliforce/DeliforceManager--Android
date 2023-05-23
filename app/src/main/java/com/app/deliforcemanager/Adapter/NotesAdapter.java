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

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {

    private NotesManipulationListener notesManipulationListener;
    private List<NotesList> notesList;
    private LoginPrefManager loginPrefManager;

    private int type;

    public NotesAdapter(Context context, int type, List<NotesList> newNoteList, NotesManipulationListener notesManipulationListener) {
        this.notesList = newNoteList;
        this.notesManipulationListener = notesManipulationListener;
        loginPrefManager = new LoginPrefManager(context);
        // This object helps you save/restore the open/close state of each view
        ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
        viewBinderHelper.setOpenOnlyOne(true);
        this.type = type;
    }

    @NonNull
    @Override
    public NotesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_item, parent, false);
        return new NotesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.MyViewHolder holder, final int position) {
        if (type == DeliforceConstants.TASK_STARTED) {
            holder.swipeLayout.setLockDrag(false);
        } else {
            holder.swipeLayout.setLockDrag(true);
        }

        // setting notes data
        for (NotesList note : notesList) {
            note = notesList.get(position);
            String convertDate = note.getCreatedAt();
            if (convertDate != null) {
                holder.noteCreationTime.setText(loginPrefManager.changeDateForamat(convertDate));
                Timber.e("Note Creation Time --> %s", convertDate);

            }
            holder.content.setText(note.getContent());
        }

        //edit and delete note click events
        holder.imgBtnEditNote.setOnClickListener(v -> {
            NotesList note = notesList.get(position);
            String noteContent = note.getContent();
            String noteId = note.getNoteId();
            Timber.e("Notes Content -->%s", noteContent);

            if (notesManipulationListener != null) {
                notesManipulationListener.onEditNoteClick(position, noteId, noteContent);
                closeSwipe(holder.swipeLayout);
            }
        });

        holder.imgBtnDeleteNote.setOnClickListener(v -> {
            NotesList note = notesList.get(position);
            String noteId = note.getNoteId();
            notesManipulationListener.onDeleteNoteClick(position, noteId);
            //delete(position);
            closeSwipe(holder.swipeLayout);
        });


    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView noteCreationTime, content;
        private LinearLayout imgBtnEditNote, imgBtnDeleteNote;
        private SwipeRevealLayout swipeLayout;

        MyViewHolder(View view) {
            super(view);
            noteCreationTime = view.findViewById(R.id.timeStamp);
            content = view.findViewById(R.id.note_content);
            imgBtnEditNote = view.findViewById(R.id.img_btn_edit_note);
            imgBtnDeleteNote = view.findViewById(R.id.img_btn_delete_note);
            swipeLayout = view.findViewById(R.id.swipe_reveal_layout);
        }
    }

    public interface NotesManipulationListener {
        void onEditNoteClick(int position, String noteId, String noteContent);

        void onDeleteNoteClick(int position, String noteId);
    }

    public void delete(int position) { //removes the row
        notesList.remove(position);
        notifyItemRemoved(position);
    }

    private void closeSwipe(SwipeRevealLayout swipeRevealLayout) {
        swipeRevealLayout.close(true);

    }

}
