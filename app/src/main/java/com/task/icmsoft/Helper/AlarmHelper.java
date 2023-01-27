package com.task.icmsoft.Helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.task.icmsoft.Activities.DashboardActivity;
import com.task.icmsoft.DB.TodoDatabaseHelper;
import com.task.icmsoft.Fragment.AlarmReceiver;
import com.task.icmsoft.ModelClass.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AlarmHelper {

    private Context context;
    private AlarmManager alarmManager;

    public AlarmHelper(Context context) {
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void setAlarmsForIncompleteTasks() {
        TodoDatabaseHelper dbHelper = new TodoDatabaseHelper(context);
        List<Task> tasks = dbHelper.getAllIncompleteTasks();
        for (Task task : tasks) {
            setAlarm(task);
        }
    }

    private void setAlarm(Task task) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("com.example.helloandroid.alarms");
        intent.putExtra("task_id", task.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, task.getId(), intent, PendingIntent.FLAG_IMMUTABLE);

//        if(alarmManager.getNextAlarmClock() == null){
//            Toast.makeText(context, "no alarm set", Toast.LENGTH_SHORT).show();
//        }
//        else{
//            Toast.makeText(context, alarmManager.getNextAlarmClock().toString(), Toast.LENGTH_SHORT).show();
//        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(task.getTime());
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);
            Toast.makeText(context, alarmManager.toString(), Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}


