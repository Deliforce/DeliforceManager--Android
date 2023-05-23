package com.app.deliforcemanager.Activities;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.deliforcemanager.Adapter.NotesAdapter;
import com.app.deliforcemanager.LocalizationActivity.LocalizationActivity;
import com.app.deliforcemanager.ModelClass.TaskDetailsApi.NotesDetails;
import com.app.deliforcemanager.ModelClass.TaskDetailsApi.NotesList;
import com.app.deliforcemanager.R;
import com.app.deliforcemanager.RawHeaders.TaskInfo.DeleteNotHeader;
import com.app.deliforcemanager.RawHeaders.TaskInfo.DeleteNote;
import com.app.deliforcemanager.Utils.DeliforceConstants;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class NotesActivity extends LocalizationActivity {

    private static final String TAG = "AddNotesActivity";

    private RecyclerView notesRecyclerView;
    private EditText edtNoteContent;
    private ImageButton imgBtnAddNote;
    private String taskID, noteId;
    List<NotesList> notesList;
    ImageView empty_note;
    LinearLayout lay_input_note;
    int task_status;

    String activity_name;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        initViews();
        initToolbar();
        getAllNotesList();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initToolbar() {

        Toolbar toolbar = findViewById(R.id.notes_toolbar);
        TextView toolbar_txt = findViewById(R.id.toolbar_title);
        toolbar_txt.setText(getString(R.string.title_add_notes));
        setSupportActionBar(toolbar);
        toolbar.setTitle("");

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        toolbar.setNavigationOnClickListener(view -> finish());
    }

    private void initViews() {
        taskID = getIntent().getExtras().getString("taskId");
        activity_name = getIntent().getExtras().getString("activity");
        task_status = getIntent().getExtras().getInt("task_status", 0);
        notesRecyclerView = findViewById(R.id.notes_recyclerview);
        edtNoteContent = findViewById(R.id.edt_note_content);
        imgBtnAddNote = findViewById(R.id.img_btn_add_note);
        empty_note = findViewById(R.id.empty_note);
        lay_input_note = findViewById(R.id.lay_input_note);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(NotesActivity.this));

        Timber.e("taskstatus --%s", task_status);

        if (task_status != DeliforceConstants.TASK_STARTED) {
            lay_input_note.setVisibility(View.GONE);
        }
        if (task_status == DeliforceConstants.TASK_STARTED||task_status == DeliforceConstants.TASK_ARRIVED) {
            lay_input_note.setVisibility(View.VISIBLE);
        }
        onClickEvents();
    }

    private void onClickEvents() {
        imgBtnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ApplicationUtils.hideKeyboard(this);
                String noteContent = edtNoteContent.getText().toString();
                if (noteContent.matches("")) {
                    showShortMessage(getString(R.string.notes_empty));
                } else {
                    if (noteId == null) {
                        sendAddNoteRequest();
                    } else {
                        requestToUpdateExistingNote();
                    }

                }
            }
        });
    }

    private void sendAddNoteRequest() {
        try {

            //ApplicationUtils.hideKeyboard(this);
            hideKeyboard(NotesActivity.this);
            String noteContent = edtNoteContent.getText().toString();
            NotesList newNoteData = new NotesList();
            newNoteData.setTaskId(taskID);
            newNoteData.setContent(noteContent);
            show_loader();

            apiService.postNewNote(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken(), newNoteData).enqueue(new Callback<NotesList>() {
                @Override
                public void onResponse(@NotNull Call<NotesList> call, @NotNull Response<NotesList> response) {
                    dismiss_loader();
                    try {
                        if (response.raw().code() == 200) {

                            edtNoteContent.setText("");
                            getAllNotesList();
                        } else if (response.raw().code() == 401) {
                            findCurrent();
                        }    //alert dialog for user already logged in
                        else if (response.code() == 494) {
                            showAlertDialog(NotesActivity.this);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<NotesList> call, Throwable t) {
                    dismiss_loader();
                    showShortMessage(t.toString());
                }
            });

        } catch (Exception e) {
            dismiss_loader();
            e.printStackTrace();
        }
    }


    private void getAllNotesList() {
        try {

            NotesList notesBuilder = new NotesList();
            notesBuilder.setTaskId(taskID);
            show_loader();

            apiService.getAllNotesList(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken(), notesBuilder).enqueue(new Callback<NotesDetails>() {
                @Override
                public void onResponse(@NotNull Call<NotesDetails> call, @NotNull Response<NotesDetails> response) {

                    dismiss_loader();
                    try {
                        if (response.raw().code() == 200) {
                            notesList = response.body().getNotesList();
                            if (notesList != null && notesList.size() > 0) {

                                empty_note.setVisibility(View.GONE);
                                notesRecyclerView.setVisibility(View.VISIBLE);
                                notesRecyclerView.setAdapter(new NotesAdapter(NotesActivity.this, task_status, notesList, new NotesAdapter.NotesManipulationListener() {
                                    @Override
                                    public void onEditNoteClick(int position, String existingNoteId, String noteContent) {
                                        edtNoteContent.requestFocus();
                                        edtNoteContent.setText(noteContent);
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                                        noteId = existingNoteId;
                                    }

                                    @Override
                                    public void onDeleteNoteClick(int position, String noteId) {

                                        Timber.e("-----");
                                        deleteNote(position, noteId);
                                    }
                                }));
                            } else {

                                empty_note.setVisibility(View.VISIBLE);
                                notesRecyclerView.setVisibility(View.GONE);
                                //                            showShortMessage("No Notes added. Please add them.");
                            }

                        } else if (response.raw().code() == 401) {
                            findCurrent();
                        }    //alert dialog for user already logged in
                        else if (response.code() == 494) {
                            showAlertDialog(NotesActivity.this);
                        } else {
                            show_error_response(getString(R.string.error_response));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<NotesDetails> call, Throwable t) {
                    dismiss_loader();
                    Timber.e("Get Notes List err--> %s", t.toString());
                }
            });
        } catch (Exception e) {
            dismiss_loader();
            e.printStackTrace();
        }

    }

    private void requestToUpdateExistingNote() {

        try {
            hideKeyboard(NotesActivity.this);
            String updatedNoteContent = edtNoteContent.getText().toString();
            NotesList notesBuilder = new NotesList();
            notesBuilder.setNoteId(noteId);
            notesBuilder.setTaskId(taskID);
            notesBuilder.setContent(updatedNoteContent);
            show_loader();

            apiService.editNote(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken(), notesBuilder).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    dismiss_loader();
                    try {
                        if (response.raw().code() == 200) {

                            showShortMessage(getString(R.string.update_note));
                            edtNoteContent.setText("");
                            getAllNotesList();
                        } else if (response.raw().code() == 401) {
                            findCurrent();
                        }
                        //alert dialog for user already logged in
                        else if (response.code() == 494) {
                            showAlertDialog(NotesActivity.this);
                        } else {
                            show_error_response(getString(R.string.error_response));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        } catch (Exception e) {
            dismiss_loader();
            e.printStackTrace();
        }
    }

    private void deleteNote(int position, String noteId) {
        try {


            DeleteNotHeader deleteNotHeader = new DeleteNotHeader();

            DeleteNote deleteNote = new DeleteNote();

            deleteNote.setTaskId(taskID);
            deleteNote.setId(noteId);

            deleteNotHeader.setFilter(deleteNote);

            show_loader();


            apiService.deletenote(loginPrefManager.getCogintoToken(), loginPrefManager.getDeviceToken(), deleteNote).enqueue(new Callback<DeleteNote>() {
                @Override
                public void onResponse(@NotNull Call<DeleteNote> call, @NotNull Response<DeleteNote> response) {
                    dismiss_loader();

                    try {
                        if (response.raw().code() == 200) {
                            Timber.e("Item to Delete Position--> %s", position);
                            getAllNotesList();

                            showShortMessage(getString(R.string.delete_note));

                        } else if (response.raw().code() == 401) {
                            findCurrent();
                        }
                        //alert dialog for user already logged in
                        else if (response.code() == 494) {
                            showAlertDialog(NotesActivity.this);
                        } else {
                            show_error_response(getString(R.string.error_response));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<DeleteNote> call, Throwable t) {
                    dismiss_loader();
                    Timber.e(t);
                }
            });

        } catch (Exception e) {
            dismiss_loader();
            Timber.e(e);
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
