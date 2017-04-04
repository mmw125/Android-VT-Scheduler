package com.markwiggans.vtscheduler.data;

import android.content.Context;
import android.database.Cursor;

import com.markwiggans.vtscheduler.database.DataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark Wiggans on 3/19/2017.
 */
public class Course {
    public static List<Course> createCourses(Cursor c) {
        ArrayList<Course> courses = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                courses.add(new Course(c));
            } while (c.moveToNext());
        }
        return courses;
    }

    private int id;
    private String wholeName;
    private String name;
    private String courseNumber;
    private String type;
    private String departmentId;
    private int semesterId;

    public Course(Cursor c) {
        id = c.getInt(0);
        wholeName = c.getString(1);
        name = c.getString(2);
        courseNumber = c.getString(3);
        type = c.getString(4);
        departmentId = c.getString(5);
        semesterId = c.getInt(6);
    }

    public List<MeetingTimeList> getMeetingTimeLists(Context context) {
        return DataSource.getInstance(context).getMeetingTimes(this);
    }

    public int getId() {
        return id;
    }

    public List<CRN> getCRNs(Context context) {
        ArrayList<CRN> crns = new ArrayList<>();
        for (MeetingTimeList list : this.getMeetingTimeLists(context)) {
            crns.addAll(list.getCRNs());
        }
        return crns;
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

    @Override
    public String toString() {
        return wholeName;
    }
}
