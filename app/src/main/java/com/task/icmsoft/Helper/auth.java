package com.task.icmsoft.Helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class auth {
    Context context;
    private String email, phone, pass;
    private byte[] imageData;
    SQLiteDatabase sqLiteDatabase;

    public auth(Context context){this.context = context;};

    public String getEmail() {
        //pin fetch from database
        sqLiteDatabase = context.openOrCreateDatabase("SQLite", Context.MODE_PRIVATE, null);
        Cursor query = sqLiteDatabase.rawQuery("SELECT * FROM UserInfo", null);
        if (query.moveToFirst()) {
            email = query.getString(1);
        }
        sqLiteDatabase.close();
        return email;
    }

    public String getPhone() {
        //pin fetch from database
        sqLiteDatabase = context.openOrCreateDatabase("SQLite", Context.MODE_PRIVATE, null);
        Cursor query = sqLiteDatabase.rawQuery("SELECT * FROM UserInfo", null);
        if (query.moveToFirst()) {
            phone = query.getString(2);
        }
        sqLiteDatabase.close();
        return phone;
    }
    public String getPass() {
        //pin fetch from database
        sqLiteDatabase = context.openOrCreateDatabase("SQLite", Context.MODE_PRIVATE, null);
        Cursor query = sqLiteDatabase.rawQuery("SELECT * FROM UserInfo", null);
        if (query.moveToFirst()) {
            pass = query.getString(3);
        }
        sqLiteDatabase.close();
        return pass;
    }
//    public byte[] getImage() {
//        //pin fetch from database
//        sqLiteDatabase = context.openOrCreateDatabase("SQLite", Context.MODE_PRIVATE, null);
//        Cursor query = sqLiteDatabase.rawQuery("SELECT * FROM UserInfo", null);
//        if (query.moveToFirst()) {
//            imageData = query.getBlob(4);
//        }
//        sqLiteDatabase.close();
//        return imageData;
//    }
}
