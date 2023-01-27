package com.task.icmsoft.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.task.icmsoft.DB.CatagoryHelper;
import com.task.icmsoft.DB.ImageHelper;
import com.task.icmsoft.DB.TodoDatabaseHelper;
import com.task.icmsoft.Helper.AlarmHelper;
import com.task.icmsoft.Helper.auth;
import com.task.icmsoft.ModelClass.Task;
import com.task.icmsoft.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    private auth auth = new auth(this);
    private TextView email, phone,go_todo;
    private ImageView profile_image;
    private static final int GALLERY_REQUEST_CODE = 1, PICK_IMAGE_REQUEST = 1;
    final int IMAGE_REQUEST_CODE = 999;
    SQLiteDatabase sqLiteDatabase;
    private Uri mImageUri;
    byte[] pro_img;
    private EditText catagory, task_name;
    private Button catagory_btn,time_picker,date_picker, task_btn;
    private List<String> categories;
    private Spinner catagoryList;
    CatagoryHelper catagoryHelper;
    private String final_time, final_date;
    TodoDatabaseHelper todoDatabaseHelper;
    private AlarmHelper alarmHelper;
    private ImageHelper imageHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        init_view();
        alarmHelper = new AlarmHelper(this);
        alarmHelper.setAlarmsForIncompleteTasks();
        catagory_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCatagory();
            }
        });
        loadCatagory();
        time_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateTime();
            }
        });
        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateDate();
            }
        });
        task_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewTask();
            }
        });
        go_todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MTodoListActivity.class);
                startActivity(i);
            }
        });

        if(imageHelper.getImage() != null){
            byte[] imageAs = imageHelper.getImage(); // get image from the database
            Bitmap image = BitmapFactory.decodeByteArray(imageAs, 0, imageAs.length);

            profile_image.setImageBitmap(image);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Set alarms for all incomplete tasks
        alarmHelper.setAlarmsForIncompleteTasks();
    }

    private void addNewTask() {
        String task = task_name.getText().toString();
        String time =final_date+" "+final_time;
        String catagory = catagoryList.getSelectedItem().toString();
        boolean isDone = false;
        Task taskObject = new Task(task, isDone, time, catagory);
        long id = todoDatabaseHelper.insertTask(taskObject);

        if (id == -1) {
            Toast.makeText(DashboardActivity.this, "Error inserting task", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(DashboardActivity.this, "Task inserted successfully with id: " + id, Toast.LENGTH_SHORT).show();
        }
    }

    private void calculateTime() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.Theme_ICMsoft));
        builder.setCancelable(false);
        final View customLayout = getLayoutInflater().inflate(R.layout.time_builder, null);
        builder.setView(customLayout);
        final TimePicker time_clock = customLayout.findViewById(R.id.time_clock);
        Button take_time_btn = customLayout.findViewById(R.id.take_time_btn);
        final AlertDialog dialog = builder.create();
        dialog.show();

        take_time_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour, minute;
                String am_pm;
                if (Build.VERSION.SDK_INT >= 23 ){
                    hour = time_clock.getHour();
                    minute = time_clock.getMinute();
                }
                else{
                    hour = time_clock.getCurrentHour();
                    minute = time_clock.getCurrentMinute();
                }
                if(hour > 12) {
                    am_pm = "PM";
                    hour = hour - 12;
                }
                else
                {
                    am_pm="AM";
                }
                Date dt = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                String time1 = sdf.format(dt);

                //course_time.setText();
                final_time = time_clock.getHour() +":"+ time_clock.getMinute()+":00";
                time_picker.setText(hour +":"+ minute+" "+am_pm);
                dialog.dismiss();
            }
        });
    }

    private void calculateDate() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.Theme_ICMsoft));
        builder.setCancelable(false);
        final View customLayout = getLayoutInflater().inflate(R.layout.date_builder, null);
        builder.setView(customLayout);
        final DatePicker date_picker1 = customLayout.findViewById(R.id.date_picker1);
        Button take_time_btn = customLayout.findViewById(R.id.take_time_btn);
        final AlertDialog dialog = builder.create();
        dialog.show();

        take_time_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final_date = date_picker1.getYear()+"-"+date_picker1.getMonth()+1+"-"+date_picker1.getDayOfMonth();
                date_picker.setText(final_date);
                dialog.dismiss();
            }
        });
    }


    private void loadCatagory() {
        categories = new ArrayList<String>();
        categories.add("Please Select catagory");
        List<String> newcategories = catagoryHelper.getCategories();
        for (String category : newcategories) {
            categories.add(category);

        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catagoryList.setAdapter(dataAdapter);
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
    }

    private void addCatagory() {
        if (TextUtils.isEmpty(catagory.getText().toString()))
        {
            Toast.makeText(DashboardActivity.this,
                    "Empty field not allowed!",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            catagoryHelper.addCategory(catagory.getText().toString());
            catagory.setText("");
            Toast.makeText(DashboardActivity.this,
                    "Added Succesfully",
                    Toast.LENGTH_SHORT).show();
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
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                byte[] imageAsByteArray = stream.toByteArray();
                imageHelper.insertOrReplaceImage(imageAsByteArray);

                byte[] imageAs = imageHelper.getImage(); // get image from the database
                Bitmap image = BitmapFactory.decodeByteArray(imageAs, 0, imageAs.length);

                profile_image.setImageBitmap(image);



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
        catagory = findViewById(R.id.catagory);
        catagory_btn = findViewById(R.id.catagory_btn);
        catagoryList = findViewById(R.id.catagoryList);
        time_picker = findViewById(R.id.time_picker);
        date_picker = findViewById(R.id.date_picker);
        task_name = findViewById(R.id.task_name);
        task_btn = findViewById(R.id.task_btn);
        go_todo = findViewById(R.id.go_todo);
        email.setText("Email: "+auth.getEmail());
        phone.setText("Phone: "+auth.getPhone());
        catagoryHelper = new CatagoryHelper(this);
        todoDatabaseHelper = new TodoDatabaseHelper(this);
        imageHelper = new ImageHelper(this);
        //Toast.makeText(getApplicationContext(), ""+auth.getImage(), Toast.LENGTH_LONG).show();

    }
}