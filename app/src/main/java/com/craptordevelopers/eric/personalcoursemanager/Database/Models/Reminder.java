package com.craptordevelopers.eric.personalcoursemanager.Database.Models;

public class Reminder {
    public static final String TABLE_NAME = "reminder";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_REMINDER = "reminder";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private String reminder;
    private String timestamp;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_REMINDER + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public Reminder() {
    }

    public Reminder(int id, String reminder, String timestamp) {
        this.id = id;
        this.reminder = reminder;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}