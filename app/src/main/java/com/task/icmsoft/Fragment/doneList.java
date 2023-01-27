package com.task.icmsoft.Fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.task.icmsoft.Activities.MTodoListActivity;

import com.task.icmsoft.Adapter.doneListAdapter;
import com.task.icmsoft.Adapter.newListAdapter;
import com.task.icmsoft.DB.TodoDatabaseHelper;
import com.task.icmsoft.ModelClass.Task;
import com.task.icmsoft.R;


import java.util.List;

public class doneList extends Fragment {
    View view;
    private RecyclerView done_recycle;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Task> allTask;
    TodoDatabaseHelper todoDatabaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_done_list, container, false);
        final MTodoListActivity activity = (MTodoListActivity) getActivity();
        done_recycle = view.findViewById(R.id.done_recycle);
        todoDatabaseHelper = new TodoDatabaseHelper(activity);
        layoutManager = new LinearLayoutManager(activity);
        done_recycle.setLayoutManager(layoutManager);
        allTask = todoDatabaseHelper.getAllCompleteTasks();
        //Toast.makeText(getActivity(), "Error inserting task"+allTask.size(), Toast.LENGTH_SHORT).show();
        adapter = new doneListAdapter(getActivity(), allTask);
        done_recycle.setAdapter(adapter);

        return view;
    }

}