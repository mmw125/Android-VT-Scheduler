package com.markwiggans.vtscheduler;

import android.app.ListActivity;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseReader myDbHelper = new DatabaseReader(this);
//        try {
//            myDbHelper.createDataBase();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        myDbHelper.openDataBase();
        Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
        Cursor c = myDbHelper.query("scheduler_server_course", null, null, null, null, null, null);
        List<Course> courses = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                courses.add(new Course(c));
            } while (c.moveToNext());
        }
        setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, courses));
    }
}
