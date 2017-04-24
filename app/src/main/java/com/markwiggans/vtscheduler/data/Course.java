package com.markwiggans.vtscheduler.data;

import android.content.Context;
import android.database.Cursor;

import com.markwiggans.vtscheduler.database.DataSource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark Wiggans on 3/19/2017.
 */
public class Course implements Serializable{
    public static List<Course> createCourses(Cursor c) {
        ArrayList<Course> courses = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                courses.add(new Course(c));
            } while (c.moveToNext());
        }
        return courses;
    }

    private String wholeName, name, courseNumber, type, departmentId, semester;

    public Course(Cursor c) {
        wholeName = c.getString(0);     // For example CS-2114
        name = c.getString(1);          // Softw Des & Data Structures
        courseNumber = c.getString(2);  // 2114
        type = c.getString(3);          // Lecture
        departmentId = c.getString(4);  // CS
        semester = c.getString(5);      // Sprint 2017
    }

    public List<CRN> getCRNs(Context context) {
        return DataSource.getInstance(context).getCRNs(this);
    }

    public String getName() {
        return name;
    }

    public String getWholeName() {
        return wholeName;
    }

    public String getType() {
        return type;
    }

    public String getCourseName() {
        return departmentId + courseNumber;
    }

    public String getCourseNameWithDash() {
        return departmentId + "-" + courseNumber;
    }

    public String getSemester() {
        return semester;
    }

    @Override
    public String toString() {
        return wholeName;
    }
}
