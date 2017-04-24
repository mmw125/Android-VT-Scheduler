package com.markwiggans.vtscheduler.data;

import android.database.Cursor;

/**
 * Created by Mark Wiggans on 4/5/2017.
 */

public class Semester {
    private String name;

    public Semester(Cursor cursor) {
        name = cursor.getString(0);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
