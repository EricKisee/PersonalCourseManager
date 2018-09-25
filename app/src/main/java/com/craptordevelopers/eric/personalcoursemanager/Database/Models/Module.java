package com.craptordevelopers.eric.personalcoursemanager.Database.Models;

public class Module {

    public static final String TABLE_NAME = "module";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_MODULE = "module";
    public static final String COLUMN_SEMESTER = "semester";
    public static final String COLUMN_MARKS= "marks";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_LECTURER= "lecturer";

    private int id;
    private String module;
    private String semester;
    private String marks;
    private String duration;
    private String lecturer;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_MODULE + " TEXT,"
                    + COLUMN_SEMESTER + " TEXT,"
                    + COLUMN_MARKS + " TEXT,"
                    + COLUMN_DURATION + " TEXT,"
                    + COLUMN_LECTURER + " TEXT"
                    + ")";

    public Module() {
    }

    public Module(int id, String module, String semester, String marks, String duration, String lecturer) {
        this.id = id;
        this.module = module;
        this.semester = semester;
        this.marks = marks;
        this.duration = duration;
        this.lecturer = lecturer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
