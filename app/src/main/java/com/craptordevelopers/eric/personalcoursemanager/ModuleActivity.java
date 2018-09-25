package com.craptordevelopers.eric.personalcoursemanager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.craptordevelopers.eric.personalcoursemanager.Database.Models.Module;
import com.craptordevelopers.eric.personalcoursemanager.Database.ModuleDB;

public class ModuleActivity extends AppCompatActivity {

    private String MODULE_ID ;
    private Module module;
    private ModuleDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MODULE_ID = getIntent().getExtras().getString("ID");
        db = new ModuleDB(this);
        module = db.getModule(Long.parseLong(MODULE_ID));


        getSupportActionBar().setTitle(module.getModule());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initialize();
    }

    private void initialize (){

        TextView tv_semester , tv_lecturer, tv_duration , tv_marks;
        tv_semester = findViewById(R.id.tv_semester);
        tv_lecturer = findViewById(R.id.tv_lecturer);
        tv_duration = findViewById(R.id.tv_duration);
        tv_marks = findViewById(R.id.tv_marks);

        getSupportActionBar().setTitle(module.getModule());
        tv_semester.setText(module.getSemester());
        tv_lecturer.setText(module.getLecturer());
        tv_duration.setText(module.getDuration());
        tv_marks.setText(module.getMarks());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showModuleDialog();
            }
        });
    }
    private void updateModule(String module_name,String duration,String lecturer,String marks) {

        // updating module text
        module.setModule(module_name);
        module.setDuration(duration);
        module.setLecturer(lecturer);
        module.setMarks(marks);

        // updating module in db
        db.updateModule(module);

    }

    private void showModuleDialog() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.new_module_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(ModuleActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText ed_module = view.findViewById(R.id.ed_module);
        final EditText ed_duration = view.findViewById(R.id.ed_duration);
        final EditText ed_lecturer = view.findViewById(R.id.ed_lecturer);
        final EditText ed_marks = view.findViewById(R.id.ed_marks);
        ed_marks.setVisibility(View.VISIBLE);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText( getString(R.string.lbl_edit_module_title));


        ed_module.setText(module.getModule());
        ed_duration.setText(module.getDuration());
        ed_lecturer.setText(module.getLecturer());
        ed_marks.setText(module.getMarks());

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton( "update" , new DialogInterface.OnClickListener() {
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
                if (TextUtils.isEmpty(ed_module.getText().toString())) {
                    Toast.makeText(ModuleActivity.this, "Enter module!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                updateModule(ed_module.getText().toString(),
                        ed_duration.getText().toString(),
                        ed_lecturer.getText().toString(),
                        ed_marks.getText().toString());
                initialize();

            }
        });
    }


}
