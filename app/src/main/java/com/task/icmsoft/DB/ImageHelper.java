package com.task.icmsoft.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ImageHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "photo_db";
    // Table name
    private static final String TABLE_IMAGE = "image";
    // Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_IMAGE = "image";

    public ImageHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_IMAGE_TABLE = "CREATE TABLE " + TABLE_IMAGE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_IMAGE + " BLOB" + ")";
        db.execSQL(CREATE_IMAGE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGE);
        // Create tables again
        onCreate(db);
    }

    public void insertOrReplaceImage(byte[] imageBytes) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, 1);
        values.put(KEY_IMAGE, imageBytes);

        // Insert or Replace
        db.insertWithOnConflict(TABLE_IMAGE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close(); // Closing database connection
    }


    public void updateImage(byte[] imageBytes) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IMAGE, imageBytes);

        // Updating Row
        db.update(TABLE_IMAGE, values, KEY_ID + " = ?",
                new String[]{String.valueOf(1)});
        db.close(); // Closing database connection
    }

    public byte[] getImage() {
        String selectQuery = "SELECT  * FROM " + TABLE_IMAGE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        byte[] imageBytes = cursor.getBlob(1);

        cursor.close();
        db.close();

        return imageBytes;
    }
}

