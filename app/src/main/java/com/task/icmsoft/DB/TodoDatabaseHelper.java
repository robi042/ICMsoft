package com.task.icmsoft.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.task.icmsoft.ModelClass.Task;

import java.util.ArrayList;
import java.util.List;

public class TodoDatabaseHelper extends SQLiteOpenHelper {
    // Database version and name
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "To_do";

    // Table and column names
    private static final String TABLE_NAME = "tasks";
    private static final String COLUMN_TASK_ID = "task_id";
    private static final String COLUMN_TASK = "task";
    private static final String COLUMN_ISDONE = "isDone";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_CATEGORY = "category";

    // Create table SQL query
    private String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TASK + " TEXT,"
            + COLUMN_ISDONE + " INTEGER,"
            + COLUMN_TIME + " TEXT,"
            + COLUMN_CATEGORY + " TEXT" + ")";

    public TodoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK, task.getTask());
        values.put(COLUMN_ISDONE, task.getIsDone() ? 1 : 0);
        values.put(COLUMN_TIME, task.getTime());
        values.put(COLUMN_CATEGORY, task.getCategory());

        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }
    @SuppressLint("Range")
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_TASK_ID + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_TASK_ID)));
                task.setTask(cursor.getString(cursor.getColumnIndex(COLUMN_TASK)));
                task.setIsDone(cursor.getInt(cursor.getColumnIndex(COLUMN_ISDONE)) == 1);
                task.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_TIME)));
                task.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)));
                tasks.add(task);
            } while (cursor.moveToNext());
        }
        db.close();
        return tasks;
    }

    @SuppressLint("Range")
    public List<Task> getAllIncompleteTasks() {
        List<Task> tasks = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ISDONE + " = 0 ORDER BY " + COLUMN_TASK_ID + " DESC ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_TASK_ID)));
                task.setTask(cursor.getString(cursor.getColumnIndex(COLUMN_TASK)));
                task.setIsDone(cursor.getInt(cursor.getColumnIndex(COLUMN_ISDONE)) == 1);
                task.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_TIME)));
                task.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)));
                tasks.add(task);
            } while (cursor.moveToNext());
        }
        db.close();
        return tasks;
    }

    @SuppressLint("Range")
    public List<Task> getAllCompleteTasks() {
        List<Task> tasks = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ISDONE + " = 1 ORDER BY " + COLUMN_TASK_ID + " DESC ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_TASK_ID)));
                task.setTask(cursor.getString(cursor.getColumnIndex(COLUMN_TASK)));
                task.setIsDone(cursor.getInt(cursor.getColumnIndex(COLUMN_ISDONE)) == 1);
                task.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_TIME)));
                task.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)));
                tasks.add(task);
            } while (cursor.moveToNext());
        }
        db.close();
        return tasks;
    }

    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK, task.getTask());
        values.put(COLUMN_ISDONE, task.getIsDone() ? 1 : 0);
        values.put(COLUMN_TIME, task.getTime());
        values.put(COLUMN_CATEGORY, task.getCategory());
        return db.update(TABLE_NAME, values, COLUMN_TASK_ID + " = ?",
                new String[]{String.valueOf(task.getId())});
    }

    public void deleteTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_TASK_ID + " = ?",
                new String[]{String.valueOf(task.getId())});
        db.close();
    }
}
