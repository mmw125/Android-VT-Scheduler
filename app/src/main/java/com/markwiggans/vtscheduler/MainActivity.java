package com.markwiggans.vtscheduler;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.markwiggans.vtscheduler.data.Course;
import com.markwiggans.vtscheduler.database.DataSource;

import java.util.List;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<Course> courses = DataSource.getInstance(this).getCourses();
        setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, courses));
    }
}
