package com.craptordevelopers.eric.personalcoursemanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.craptordevelopers.eric.personalcoursemanager.Database.Models.Module;
import com.craptordevelopers.eric.personalcoursemanager.Database.Models.Reminder;
import com.craptordevelopers.eric.personalcoursemanager.Database.ReminderDB;

import java.util.ArrayList;
import java.util.List;

public class ReminderActivity extends AppCompatActivity {


    private List<Reminder> reminderArrayList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ReminderDB db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Reminders");
        db = new ReminderDB(this);

        reminderArrayList.addAll(db.getAllReminders());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                showReminderDialog(false, null, -1);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new ReminderAdapter(this, reminderArrayList);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));



    }

    /**
     * Inserting new reminder in db
     * and refreshing the list
     */
    private void createNote(String reminder ,String date, String time) {
        // inserting reminder in db and getting
        // newly inserted reminder id
        long id = db.insertReminder(reminder , date+" "+time);

        // get the newly inserted reminder from db
        Reminder n = db.getReminder(id);

        if (n != null) {
            // adding new reminder to array list at 0 position
            reminderArrayList.add(0, n);

            // refreshing the list
            mAdapter.notifyDataSetChanged();

//            toggleEmptyNotes();
        }
    }

    /**
     * Updating note in db and updating
     * item in the list by its position
     */
    private void updateReminder(String note,String date, String time, int position) {
        Reminder n = reminderArrayList.get(position);
        // updating note text
        n.setReminder(note);
        n.setTimestamp(date+" "+time);

        // updating note in db
        db.updateReminder(n);

        // refreshing the list
        reminderArrayList.set(position, n);
        mAdapter.notifyItemChanged(position);

//        toggleEmptyNotes();
    }

    /**
     * Deleting note from SQLite and removing the
     * item from the list by its position
     */
    private void deleteReminder(int position) {
        // deleting the note from db
        db.deleteReminder(reminderArrayList.get(position));

        // removing the note from the list
        reminderArrayList.remove(position);

        mAdapter.notifyItemRemoved(position);

//        toggleEmptyNotes();
    }

    /**
     * Opens dialog with Edit - Delete options
     * Edit - 0
     * Delete - 0
     */
    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showReminderDialog(true, reminderArrayList.get(position), position);
                } else {
                    deleteReminder(position);
                }
            }
        });
        builder.show();
    }


    /**
     * Shows alert dialog with EditText options to enter / edit
     * a note.
     * when shouldUpdate=true, it automatically displays old note and changes the
     * button text to UPDATE
     */
    private void showReminderDialog(final boolean shouldUpdate, final Reminder reminder, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.new_reminder_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(ReminderActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText ed_reminder = view.findViewById(R.id.ed_reminder);
        final EditText ed_date = view.findViewById(R.id.ed_date);
        final EditText ed_time = view.findViewById(R.id.ed_time);

        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_module_title) : getString(R.string.lbl_edit_module_title));

        if (shouldUpdate && reminder != null) {
            ed_reminder.setText(reminder.getReminder());
            ed_date.setText(reminder.getTimestamp().split(" ")[0]);
            ed_time.setText(reminder.getTimestamp().split(" ")[1]);
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(ed_reminder.getText().toString())) {
                    Toast.makeText(ReminderActivity.this, "Enter note!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating note
                if (shouldUpdate && reminder != null) {
                    // update note by it's id
                    updateReminder(ed_reminder.getText().toString(), ed_date.getText().toString() , ed_time.getText().toString(), position);
                } else {
                    // create new note
                    createNote(ed_reminder.getText().toString() , ed_date.getText().toString() , ed_time.getText().toString());
                }
            }
        });
    }


}