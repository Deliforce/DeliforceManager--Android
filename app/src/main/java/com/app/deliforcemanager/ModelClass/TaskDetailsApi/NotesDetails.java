package com.app.deliforcemanager.ModelClass.TaskDetailsApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotesDetails {

    @SerializedName("notesList")
    @Expose
    private List<NotesList> notesList = null;

    public List<NotesList> getNotesList() {
        return notesList;
    }

    public void setNotesList(List<NotesList> notesList) {
        this.notesList = notesList;
    }
}
