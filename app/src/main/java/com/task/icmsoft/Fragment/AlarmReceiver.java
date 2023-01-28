package com.task.icmsoft.Fragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.task.icmsoft.Activities.MTodoListActivity;
import com.task.icmsoft.Helper.AlarmHelper;
import com.task.icmsoft.R;

public class AlarmReceiver extends BroadcastReceiver {
    MediaPlayer mp;

    @Override
    public void onReceive(Context context, Intent intent) {
        mp = MediaPlayer.create(context, Settings.System.DEFAULT_ALARM_ALERT_URI);
        mp.setLooping(true);
        mp.start();
        int taskId = intent.getIntExtra("task_id", -1);
        String taskName = intent.getStringExtra("task_name");
        AlarmHelper alarmHelper = new AlarmHelper(context);
        alarmHelper.cancelAlarm(taskId);
        // handle the alarm here, for example, show a notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel_name";
            String description = "channel_description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("channel_id", name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }
        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id")
                .setSmallIcon(R.drawable.ic_baseline_arrow_back_24)
                .setContentTitle("Task Reminder")
                .setContentText(taskName)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);


        // Set the Intent to open when the notification is selected
        Intent notificationIntent = new Intent(context, MTodoListActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(contentIntent);

        // Send the notification
        notificationManager.notify(0, builder.build());

    }
}

