package com.craptordevelopers.eric.personalcoursemanager.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import com.craptordevelopers.eric.personalcoursemanager.Database.Models.Module;

public class ModuleDB extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "module_db";


    public ModuleDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(Module.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Module.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertModule(String module, String semester, String marks, String duration, String lecturer) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` will be inserted automatically.
        // no need to add them
        values.put(Module.COLUMN_MODULE, module);
        values.put(Module.COLUMN_SEMESTER, semester);
        values.put(Module.COLUMN_MARKS, marks);
        values.put(Module.COLUMN_DURATION, duration);
        values.put(Module.COLUMN_LECTURER, lecturer);

        // insert row
        long id = db.insert(Module.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Module getModule(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Module.TABLE_NAME,
                new String[]{Module.COLUMN_ID, Module.COLUMN_MODULE, Module.COLUMN_SEMESTER,
                        Module.COLUMN_MARKS, Module.COLUMN_DURATION, Module.COLUMN_LECTURER},
                Module.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        Module note = new Module(
                cursor.getInt(cursor.getColumnIndex(Module.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Module.COLUMN_MODULE)),
                cursor.getString(cursor.getColumnIndex(Module.COLUMN_SEMESTER)),
                cursor.getString(cursor.getColumnIndex(Module.COLUMN_MARKS)),
                cursor.getString(cursor.getColumnIndex(Module.COLUMN_DURATION)),
                cursor.getString(cursor.getColumnIndex(Module.COLUMN_LECTURER)));

        cursor.getString(cursor.getColumnIndex(Module.COLUMN_MODULE));

        // close the db connection
        cursor.close();

        return note;
    }

    public List<Module> getAllModules() {
        List<Module> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Module.TABLE_NAME + " ORDER BY " +
                Module.COLUMN_MODULE + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Module note = new Module();
                note.setId(cursor.getInt(cursor.getColumnIndex(Module.COLUMN_ID)));
                note.setModule(cursor.getString(cursor.getColumnIndex(Module.COLUMN_MODULE)));
                note.setSemester(cursor.getString(cursor.getColumnIndex(Module.COLUMN_SEMESTER)));
                note.setMarks(cursor.getString(cursor.getColumnIndex(Module.COLUMN_MARKS)));
                note.setDuration(cursor.getString(cursor.getColumnIndex(Module.COLUMN_DURATION)));
                note.setLecturer(cursor.getString(cursor.getColumnIndex(Module.COLUMN_LECTURER)));

                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }

    public List<Module> getAllModules(int semester) {
        List<Module> notes = new ArrayList<>();

        // Select All Query

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(Module.TABLE_NAME,
                new String[]{Module.COLUMN_ID, Module.COLUMN_MODULE, Module.COLUMN_SEMESTER,
                        Module.COLUMN_MARKS, Module.COLUMN_DURATION, Module.COLUMN_LECTURER},
                Module.COLUMN_SEMESTER + "=?",
                new String[]{String.valueOf(semester)}, null, null, Module.COLUMN_MODULE+" ASC", null);


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Module module = new Module();
                module.setId(cursor.getInt(cursor.getColumnIndex(Module.COLUMN_ID)));
                module.setModule(cursor.getString(cursor.getColumnIndex(Module.COLUMN_MODULE)));
                module.setSemester(cursor.getString(cursor.getColumnIndex(Module.COLUMN_SEMESTER)));
                module.setMarks(cursor.getString(cursor.getColumnIndex(Module.COLUMN_MARKS)));
                module.setDuration(cursor.getString(cursor.getColumnIndex(Module.COLUMN_DURATION)));
                module.setLecturer(cursor.getString(cursor.getColumnIndex(Module.COLUMN_LECTURER)));

                notes.add(module);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }

    public int getModuleCount() {
        String countQuery = "SELECT  * FROM " + Module.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateModule(Module module) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Module.COLUMN_MODULE, module.getModule());
        values.put(Module.COLUMN_SEMESTER, module.getSemester());
        values.put(Module.COLUMN_MARKS, module.getMarks());
        values.put(Module.COLUMN_DURATION, module.getDuration());
        values.put(Module.COLUMN_LECTURER, module.getLecturer());

        // updating row
        return db.update(Module.TABLE_NAME, values, Module.COLUMN_ID + " = ?",
                new String[]{String.valueOf(module.getId())});
    }

    public void deleteModule(Module module) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Module.TABLE_NAME, Module.COLUMN_ID + " = ?",
                new String[]{String.valueOf(module.getId())});
        db.close();
    }
}