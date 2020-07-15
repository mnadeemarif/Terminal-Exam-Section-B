package com.csgradqau.terminalexamsectionb.Database.model;

public class task {
    public static final String TABLE_NAME = "tasks";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DEADLINE = "deadline";
    public static final String COLUMN_TASKDETAILS = "taskDetails";

    private int id;
    private String title;
    private String deadline;
    private String taskDetails;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TITLE + " TEXT,"
                    + COLUMN_TASKDETAILS + " TEXT,"
                    + COLUMN_DEADLINE + " TEXT"
                    + ")";

    public task() {
    }

    public task(int id, String title, String taskDetails,String deadline) {
        this.id = id;
        this.title = title;
        this.deadline = deadline;
        this.taskDetails = taskDetails;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getTaskDetails() {
        return taskDetails;
    }

    public void setTaskDetails(String taskDetails) {
        this.taskDetails = taskDetails;
    }

}
