package com.markwiggans.vtscheduler;

import android.database.Cursor;

/**
 * Created by Mark Wiggans on 3/19/2017.
 */

public class Course {
    private String data;
    public Course(Cursor c) {
        data = "";
        for(int i = 0; i < c.getColumnCount(); i++)
            data += c.getString(i);
    }

    @Override
    public String toString() {
        return data;
    }
}
