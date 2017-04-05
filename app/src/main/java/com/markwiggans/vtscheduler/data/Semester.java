package com.markwiggans.vtscheduler.data;

import android.database.Cursor;

/**
 * Created by Mark Wiggans on 4/5/2017.
 */

public class Semester {
    private String name;
    private int id;
    public Semester(Cursor cursor) {
        id = cursor.getInt(0);
        name = cursor.getString(1);
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
