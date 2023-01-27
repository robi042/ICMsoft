package com.task.icmsoft.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.task.icmsoft.Adapter.TodoAdapter;
import com.task.icmsoft.Fragment.doneList;
import com.task.icmsoft.Fragment.newList;
import com.task.icmsoft.R;

public class MTodoListActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mtodo_list);
        getSupportActionBar().setElevation(0);//remove actionbar shadow
        setTitle("ToDo List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager=findViewById(R.id.viewPager_id);

        TodoAdapter todoAdapter=new TodoAdapter(getSupportFragmentManager());
        todoAdapter.addFragment(new newList(),"New List");
        todoAdapter.addFragment(new doneList(),"Done List");


        viewPager.setAdapter(todoAdapter);
        tabLayout=findViewById(R.id.tabLayout);

        tabLayout.setupWithViewPager(viewPager);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}