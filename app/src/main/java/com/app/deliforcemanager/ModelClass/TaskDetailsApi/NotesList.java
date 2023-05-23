package com.app.deliforcemanager.ModelClass.TaskDetailsApi;

import com.google.gson.annotations.SerializedName;

public class NotesList {

    @SerializedName("_id")
    private String noteId;
    @SerializedName("isDeleted")
    private Integer isDeleted;
    @SerializedName("notes")
    private String content;
    @SerializedName("taskId")
    private String taskId;
    @SerializedName("created_at")
    private String createdAt;

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
