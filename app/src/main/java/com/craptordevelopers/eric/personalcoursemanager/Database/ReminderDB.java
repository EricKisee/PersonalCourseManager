package com.craptordevelopers.eric.personalcoursemanager.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import com.craptordevelopers.eric.personalcoursemanager.Database.Models.Reminder;

    public class ReminderDB extends SQLiteOpenHelper {

        // Database Version
        private static final int DATABASE_VERSION = 1;

        // Database Name
        private static final String DATABASE_NAME = "reminder_db";


        public ReminderDB(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        // Creating Tables
        @Override
        public void onCreate(SQLiteDatabase db) {

            // create reminders table
            db.execSQL(Reminder.CREATE_TABLE);
        }

        // Upgrading database
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Drop older table if existed
            db.execSQL("DROP TABLE IF EXISTS " + Reminder.TABLE_NAME);

            // Create tables again
            onCreate(db);
        }

        public long insertReminder(String reminder, String timestamp) {
            // get writable database as we want to write data
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            // `id` and `timestamp` will be inserted automatically.
            // no need to add them
            values.put(Reminder.COLUMN_REMINDER, reminder);
            values.put(Reminder.COLUMN_TIMESTAMP, timestamp);

            // insert row
            long id = db.insert(Reminder.TABLE_NAME, null, values);

            // close db connection
            db.close();

            // return newly inserted row id
            return id;
        }

        public Reminder getReminder(long id) {
            // get readable database as we are not inserting anything
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.query(Reminder.TABLE_NAME,
                    new String[]{Reminder.COLUMN_ID, Reminder.COLUMN_REMINDER, Reminder.COLUMN_TIMESTAMP},
                    Reminder.COLUMN_ID + "=?",
                    new String[]{String.valueOf(id)}, null, null, null, null);

            if (cursor != null)
                cursor.moveToFirst();

            // prepare reminder object
            Reminder reminder = new Reminder(
                    cursor.getInt(cursor.getColumnIndex(Reminder.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(Reminder.COLUMN_REMINDER)),
                    cursor.getString(cursor.getColumnIndex(Reminder.COLUMN_TIMESTAMP)));

            // close the db connection
            cursor.close();

            return reminder;
        }

        public List<Reminder> getAllReminders() {
            List<Reminder> reminders = new ArrayList<>();

            // Select All Query
            String selectQuery = "SELECT  * FROM " + Reminder.TABLE_NAME + " ORDER BY " +
                    Reminder.COLUMN_TIMESTAMP + " DESC";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Reminder reminder = new Reminder();
                    reminder.setId(cursor.getInt(cursor.getColumnIndex(Reminder.COLUMN_ID)));
                    reminder.setReminder(cursor.getString(cursor.getColumnIndex(Reminder.COLUMN_REMINDER)));
                    reminder.setTimestamp(cursor.getString(cursor.getColumnIndex(Reminder.COLUMN_TIMESTAMP)));

                    reminders.add(reminder);
                } while (cursor.moveToNext());
            }

            // close db connection
            db.close();

            // return reminders list
            return reminders;
        }

        public int getRemindersCount() {
            String countQuery = "SELECT  * FROM " + Reminder.TABLE_NAME;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);

            int count = cursor.getCount();
            cursor.close();


            // return count
            return count;
        }

        public int updateReminder(Reminder reminder) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(Reminder.COLUMN_REMINDER, reminder.getReminder());

            // updating row
            return db.update(Reminder.TABLE_NAME, values, Reminder.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(reminder.getId())});
        }

        public void deleteReminder(Reminder reminder) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(Reminder.TABLE_NAME, Reminder.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(reminder.getId())});
            db.close();
        }
    }