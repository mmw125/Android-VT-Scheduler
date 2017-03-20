package com.markwiggans.vtscheduler.database;

import android.content.Context;
import android.database.Cursor;

import com.markwiggans.vtscheduler.data.Course;
import com.markwiggans.vtscheduler.data.MeetingTimeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark Wiggans on 3/19/2017.
 */
public class DataSource {
    private static DataSource instance;

    /**
     * Gets the data source instance
     * @return the data source instance
     */
    public static DataSource getInstance() {
        if(instance != null) {
            instance = new DataSource();
        }
        return instance;
    }

    private DataSource() {
    }

    private List<Course> courses;

    /**
     * Queries the database for all of the courses. This is a cached operation
     * @return all of the courses
     */
    public List<Course> getCourses(Context context) {
        if (courses == null) {
            DatabaseReader myDbHelper = new DatabaseReader(context);
            myDbHelper.openDataBase();
            Cursor c = myDbHelper.query(CourseReaderContract.CourseEntry.TABLE_NAME, null, null, null, null, null, null);
            courses = new ArrayList<>();
            if (c.moveToFirst()) {
                do {
                    courses.add(new Course(c));
                } while (c.moveToNext());
            }
            myDbHelper.close();
        }
        return courses;
    }

    /**
     * Gets the meeting times for many courses using the same database instance
     * @param courses the courses to find the meeting times for
     * @return the meeting times for the given courses
     */
    public List<MeetingTimeList>[] getManyMeetingTimes(Context context, List<Course> courses) {
        DatabaseReader reader = new DatabaseReader(context);
        List<MeetingTimeList>[] arr = (List<MeetingTimeList>[]) new Object[courses.size()];
        reader.openDataBase();
        for(int i = 0; i < courses.size(); i++) {
            arr[i] = this.getMeetingTimes(context, courses.get(i), reader);
        }
        reader.close();
        return arr;
    }

    /**
     * Gets the meeting times for one course
     * @param course the course to get the meeting times for
     * @return the meeting times for the given course
     */
    public List<MeetingTimeList> getMeetingTimes(Context context, Course course) {
        return this.getMeetingTimes(context, course, null);
    }

    private List<MeetingTimeList> getMeetingTimes(Context context, Course course, DatabaseReader reader) {
        if(course.getCachedMeetingTimeLists() != null) {
            return course.getCachedMeetingTimeLists();
        }
        boolean hasReader = reader == null;
        if(!hasReader) {
            reader = new DatabaseReader(context);
        }

        //TODO: Preform query

        if(!hasReader) {
            reader.close();
        }
        return course.getMeetingTimeLists(context);
    }
}
