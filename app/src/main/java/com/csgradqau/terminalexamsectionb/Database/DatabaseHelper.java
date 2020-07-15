package com.csgradqau.terminalexamsectionb.Database;

import  com.csgradqau.terminalexamsectionb.Database.model.task;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "db_task";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(task.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + task.TABLE_NAME);

        onCreate(db);
    }

    public long addTask(task t) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(task.COLUMN_TITLE, t.getTitle());
        values.put(task.COLUMN_TASKDETAILS, t.getTaskDetails());
        values.put(task.COLUMN_DEADLINE, t.getDeadline());

        long id = db.insert(task.TABLE_NAME, null, values);

        db.close();
        return id;
    }

    public task getTask(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(task.TABLE_NAME,
                new String[]{task.COLUMN_ID, task.COLUMN_TITLE, task.COLUMN_TASKDETAILS, task.COLUMN_DEADLINE},
                task.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        task t = new task(
                cursor.getInt(cursor.getColumnIndex(task.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(task.COLUMN_TITLE)),
                cursor.getString(cursor.getColumnIndex(task.COLUMN_TASKDETAILS)),
                cursor.getString(cursor.getColumnIndex(task.COLUMN_DEADLINE)));

        cursor.close();

        return t;
    }

    public List<task> getAllTasks() {
        List<task> tasks = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + task.TABLE_NAME ;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                task t = new task(
                        cursor.getInt(cursor.getColumnIndex(task.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(task.COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndex(task.COLUMN_TASKDETAILS)),
                        cursor.getString(cursor.getColumnIndex(task.COLUMN_DEADLINE)));

                tasks.add(t);
            } while (cursor.moveToNext());
        }
        db.close();

        return tasks;
    }

    public int getTaskCount() {
        String countQuery = "SELECT  * FROM " + task.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}
