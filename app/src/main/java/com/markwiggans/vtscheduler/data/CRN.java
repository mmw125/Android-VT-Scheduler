package com.markwiggans.vtscheduler.data;

import android.database.Cursor;
import android.util.Log;

import com.markwiggans.vtscheduler.MainActivity;
import com.markwiggans.vtscheduler.database.CourseReaderContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark Wiggans on 3/19/2017.
 * Represents a CRN of a class
 */
public class CRN {
    public static List<CRN> createCRNs(Cursor c) {
        ArrayList<CRN> courses = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                CRN crn = new CRN(c);
                courses.add(crn);
                Log.d(MainActivity.LOG_STRING, crn.toString());
            } while (c.moveToNext());
        }
        return courses;
    }

    private int crn;
    // private String crnText;
    private String instructor, location, semester;

    public CRN(Cursor c) {
        crn = c.getInt(c.getColumnIndex(CourseReaderContract.CRNEntry.COLUMN_NAME_CRN));
        instructor = c.getString(c.getColumnIndex(CourseReaderContract.CRNEntry.COLUMN_NAME_INSTRUCTOR));
        location = c.getString(c.getColumnIndex(CourseReaderContract.CRNEntry.COLUMN_NAME_LOCATION));
        semester = c.getString(c.getColumnIndex(CourseReaderContract.CRNEntry.COLUMN_COURSE_SEMESTER));
    }

    /**
     * Gets the numerical crn for this crn
     * @return the crn number
     */
    public int getCRN() {
        return crn;
    }

    public String getInstructor() {
        return instructor;
    }

    public String toString() {
        return "" + crn;
    }

    public String getLocation() {
        return this.location;
    }

    public String getSemester() {
        return this.semester;
    }
}
