package com.task.icmsoft.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.task.icmsoft.Activities.DashboardActivity;
import com.task.icmsoft.Activities.MTodoListActivity;
import com.task.icmsoft.DB.TodoDatabaseHelper;
import com.task.icmsoft.ModelClass.Task;
import com.task.icmsoft.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class newListAdapter extends RecyclerView.Adapter<newListAdapter.NewHolder> {

    Context context;
    List<Task> task;

    public newListAdapter(Context context, List<Task> task) {
        this.context = context;
        this.task = task;
    }


    @NonNull
    @Override
    public NewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_item_new, parent, false);
        return new NewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.task.setText("Task: "+ task.get(position).getTask());
        holder.category.setText("Category: "+ task.get(position).getCategory());
        String str = task.get(position).getTime();
        String[] splitStr = str.split("\\s+");
        String[] items = splitStr[1].split(":");
        int hour = 0, minute = 0;
        String am_pm;
        if (Build.VERSION.SDK_INT >= 23 ){
            hour = Integer.parseInt(items[0]);
            minute = Integer.parseInt(items[1]);
        }
        else{
            holder.time.setText("Time: "+ task.get(position).getTime());
        }
        if(hour > 12) {
            am_pm = "PM";
            hour = hour - 12;
        }
        else
        {
            am_pm="AM";
        }
        holder.time.setText("Time: "+splitStr[0]+" "+hour +":"+ minute+" "+am_pm);
        holder.done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task task1 = new Task(task.get(position).getTask(), true, task.get(position).getTime(), task.get(position).getCategory());
                TodoDatabaseHelper todoDatabaseHelper = new TodoDatabaseHelper(context);
                task1.setId(task.get(position).getId());
                todoDatabaseHelper.updateTask(task1);
                Intent i = new Intent(context, MTodoListActivity.class);
                context.startActivity(i);
                ((Activity) view.getContext()).overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

    }

    @Override
    public int getItemCount() {
        return task == null ? 0 : task.size();
    }

    public class NewHolder extends RecyclerView.ViewHolder {
        TextView task, category, time;
        Button done_btn;
        public NewHolder(@NonNull View itemView) {
            super(itemView);
            task = itemView.findViewById(R.id.task);
            category = itemView.findViewById(R.id.category);
            time = itemView.findViewById(R.id.time);
            done_btn = itemView.findViewById(R.id.done_btn);

        }
    }
}
