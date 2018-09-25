package com.craptordevelopers.eric.personalcoursemanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.craptordevelopers.eric.personalcoursemanager.Database.Models.Module;
import com.craptordevelopers.eric.personalcoursemanager.Database.ModuleDB;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private List<Module> moduleArrayList ;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ModuleDB db;
    private String[] semisters = {"1st Semester","2nd Semester","3rd Semester"
            ,"4th Semester","5th Semester","6th Semester"};
    private int current_semester = 1;
    private Toolbar toolbar;
    private String TAG = "MAIN ACTIVITY TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();

    }

    private void initialize (){

        setUpActionBar();
        moduleArrayList = new ArrayList<>();
        db = new ModuleDB(this);
        moduleArrayList.addAll(db.getAllModules(current_semester));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showModuleDialog(false, null, -1);
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
        mAdapter = new ModuleAdapter(this,moduleArrayList);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                Intent intent = new Intent(MainActivity.this, ModuleActivity.class);
                intent.putExtra("ID", String.valueOf((moduleArrayList.get(position).getId())));
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));



    }

    private void setUpActionBar(){

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(semisters[current_semester-1]);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_sem1) {
            current_semester =1;
            initialize();
        } else if (id == R.id.nav_sem2) {
            current_semester =2;
            initialize();
        } else if (id == R.id.nav_sem3) {
            current_semester =3;
            initialize();
        } else if (id == R.id.nav_sem4) {
            current_semester =4;
            initialize();
        } else if (id == R.id.nav_sem5) {
            current_semester =5;
            initialize();
        } else if (id == R.id.nav_sem6) {
            current_semester =6;
            initialize();
        } else if (id == R.id.nav_reminders) {
            startActivity(new Intent(MainActivity.this, ReminderActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Inserting new module in db
     * and refreshing the list
     */
    private void createModule(String module,String duration,String lecturer) {
        // inserting module in db and getting
        // newly inserted module id
        long id = db.insertModule(module, current_semester +"" , "No Marks" ,duration,lecturer);

        // get the newly inserted module from db
        Module n = db.getModule(id);

        if (n != null) {
            // adding new module to array list at 0 position
            moduleArrayList.add(0, n);

            // refreshing the list
            mAdapter.notifyDataSetChanged();

//            toggleEmptyNotes();
        }
    }

    /**
     * Updating module in db and updating
     * item in the list by its position
     */
    private void updateModule(String module,String duration,String lecturer, int position) {
        Module n = moduleArrayList.get(position);
        // updating module text
        n.setModule(module);
        n.setDuration(duration);
        n.setLecturer(lecturer);

        // updating module in db
        db.updateModule(n);

        // refreshing the list
        moduleArrayList.set(position, n);
        mAdapter.notifyItemChanged(position);

//        toggleEmptyNotes();
    }


    private void updateMarks (String marks, int position){
        Module n = moduleArrayList.get(position);
        // updating marks text
        n.setMarks(marks);// updating module in db
        db.updateModule(n);

        // refreshing the list
        moduleArrayList.set(position, n);
        mAdapter.notifyItemChanged(position);
    }

    /**
     * Deleting module from SQLite and removing the
     * item from the list by its position
     */
    private void deleteModule(int position) {
        // deleting the module from db
        db.deleteModule(moduleArrayList.get(position));

        // removing the module from the list
        moduleArrayList.remove(position);
        mAdapter.notifyItemRemoved(position);

//        toggleEmptyNotes();
    }

    /**
     * Opens dialog with Edit - Delete options
     * Edit - 0
     * Delete - 0
     */
    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete" , "Marks"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showModuleDialog(true, moduleArrayList.get(position), position);
                } else if (which == 1) {
                    deleteModule(position);
                }else if (which == 2) {
                    showMarksDialog(true, moduleArrayList.get(position), position);
                }
            }
        });
        builder.show();
    }


    /**
     * Shows alert dialog with EditText options to enter / edit
     * a module.
     * when shouldUpdate=true, it automatically displays old module and changes the
     * button text to UPDATE
     */
    private void showModuleDialog(final boolean shouldUpdate, final Module module, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.new_module_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText ed_module = view.findViewById(R.id.ed_module);
        final EditText ed_duration = view.findViewById(R.id.ed_duration);
        final EditText ed_lecturer = view.findViewById(R.id.ed_lecturer);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_module_title) : getString(R.string.lbl_edit_module_title));

        if (shouldUpdate && module != null) {
            ed_module.setText(module.getModule());
            ed_duration.setText(module.getDuration());
            ed_lecturer.setText(module.getLecturer());
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
                if (TextUtils.isEmpty(ed_module.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter module!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating module
                if (shouldUpdate && module != null) {
                    // update module by it's id
                    updateModule(ed_module.getText().toString(),
                            ed_duration.getText().toString(),
                            ed_lecturer.getText().toString(), position);
                } else {
                    // create new module
                    createModule(ed_module.getText().toString(),
                                ed_duration.getText().toString(),
                                ed_lecturer.getText().toString());
                    }
            }
        });
    }

    private void showMarksDialog(final boolean shouldUpdate, final Module module, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.marks_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputMarks = view.findViewById(R.id.ed_marks);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText("Update Marks");

        if (shouldUpdate && module != null) {
            inputMarks.setText(module.getMarks());
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
                if (TextUtils.isEmpty(inputMarks.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter Marks!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating module
                if (shouldUpdate && module != null) {
                    // update module by it's id
                    updateMarks(inputMarks.getText().toString(), position);
                } else {
                }
            }
        });
    }
}
