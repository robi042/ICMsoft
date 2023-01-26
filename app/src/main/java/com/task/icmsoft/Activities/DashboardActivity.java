package com.task.icmsoft.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.task.icmsoft.Helper.auth;
import com.task.icmsoft.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DashboardActivity extends AppCompatActivity {
    private auth auth = new auth(this);
    private TextView email, phone;
    private ImageView profile_image;
    private static final int GALLERY_REQUEST_CODE = 1, PICK_IMAGE_REQUEST = 1;
    final int IMAGE_REQUEST_CODE = 999;
    SQLiteDatabase sqLiteDatabase;
    private Uri mImageUri;
    byte[] pro_img;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        init_view();
        //Toast.makeText(getApplicationContext(), auth.getEmail(), Toast.LENGTH_LONG).show();
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
        sqLiteDatabase = getBaseContext().openOrCreateDatabase("SQLite", MODE_PRIVATE, null);
        try {
            Cursor query = sqLiteDatabase.rawQuery("SELECT * FROM ProfilePhoto", null);
            if (query.moveToFirst()) {
                try {
                    pro_img = query.getBlob(1);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            } else {
                Intent i = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(i);
            }
            query.close();
            profile_image.setImageBitmap(BitmapFactory.decodeByteArray(pro_img, 0, pro_img.length));
            Toast.makeText(getApplicationContext(), pro_img.toString(), Toast.LENGTH_LONG).show();

        }catch (NullPointerException  e){
            Toast.makeText(getApplicationContext(), "Null value", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);
                //profile_image.setImageBitmap(bitmap);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] imageAsByteArray = stream.toByteArray();
                //profile_image.setImageBitmap(BitmapFactory.decodeByteArray(imageAsByteArray, 0, imageAsByteArray.length));

                String sql = "CREATE TABLE IF NOT EXISTS ProfilePhoto (_id INTEGER PRIMARY KEY,image BLOB);";
                sqLiteDatabase.execSQL(sql);
                String sql_new = "INSERT or REPLACE INTO ProfilePhoto VALUES ( 1,'" + imageAsByteArray + "');";
                sqLiteDatabase.execSQL(sql_new);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

private void init_view() {
        getSupportActionBar().setElevation(0);//remove actionbar shadow
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.WHITE);
        }
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        profile_image = findViewById(R.id.profile_image);
        email.setText("Email: "+auth.getEmail());
        phone.setText("Phone: "+auth.getPhone());
        //Toast.makeText(getApplicationContext(), ""+auth.getImage(), Toast.LENGTH_LONG).show();

    }
}