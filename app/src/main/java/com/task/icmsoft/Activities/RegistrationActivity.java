package com.task.icmsoft.Activities;

import static java.sql.Types.NULL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.task.icmsoft.R;

public class RegistrationActivity extends AppCompatActivity {
    private EditText email, phone, password;
    private Button submit;
    private SQLiteDatabase sqLiteDatabase;
    private String e, p, pa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        inti();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(email.getText().toString()) || TextUtils.isEmpty(phone.getText().toString()) || TextUtils.isEmpty(password.toString()))
                {
                    Toast.makeText(RegistrationActivity.this,
                            "Empty field not allowed!",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    SQLiteDatabase sqLiteDatabase = getBaseContext().openOrCreateDatabase("SQLite", MODE_PRIVATE, null);

                    String sql = "CREATE TABLE IF NOT EXISTS UserInfo (_id INTEGER PRIMARY KEY,email TEXT,phone TEXT,password TEXT);";
                    sqLiteDatabase.execSQL(sql);
                    String sql_new = "INSERT or REPLACE INTO UserInfo VALUES ( 1,'" + email.getText().toString() + "','" + phone.getText().toString() + "','" + password.getText().toString() + "');";
                    sqLiteDatabase.execSQL(sql_new);
                    Cursor query = sqLiteDatabase.rawQuery("SELECT * FROM UserInfo", null);
                    if (query.moveToFirst()) {
                        try {
                            e = query.getString(1);
                            p = query.getString(2);
                            pa = query.getString(3);

                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Intent i = new Intent(getApplicationContext(), RegistrationActivity.class);
                        startActivity(i);
                    }
                    query.close();
                    //Toast.makeText(getApplicationContext(), e, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }

            }
        });
    }

    private void inti() {
        getSupportActionBar().setElevation(0);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#6A5BE2"));
        }
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#6A5BE2")));
        email = findViewById(R.id.email);
        phone = findViewById(R.id. phone);
        password = findViewById(R.id.password);
        submit = findViewById(R.id.submit_btn);
    }
}