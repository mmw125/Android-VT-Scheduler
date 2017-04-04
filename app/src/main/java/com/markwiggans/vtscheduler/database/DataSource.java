package com.markwiggans.vtscheduler.database;

import android.content.Context;
import android.database.Cursor;

import com.markwiggans.vtscheduler.data.CRN;
import com.markwiggans.vtscheduler.data.Course;
import com.markwiggans.vtscheduler.data.MeetingTimeList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark Wiggans on 3/19/2017.
 * Wrapper for the DatabaseReader class
 */
public class DataSource {
    private static DataSource instance;

    /**
     * Gets the data source instance
     * @return the data source instance
     */
    public static DataSource getInstance(Context context) {
        if(instance == null) {
            instance = new DataSource(context);
        }
        return instance;
    }

    private DatabaseReader reader;
    private DataSource(Context context) {
        reader = new DatabaseReader(context);
    }

    private List<Course> courses;

    /**
     * Queries the database for all of the courses. This is a cached operation
     * @return all of the courses
     */
    public List<Course> getCourses() {
        if (courses == null) {
            try {
                reader.createDataBase();
            } catch (IOException e) { }
            reader.openDataBase();
            Cursor c = reader.query(CourseReaderContract.CourseEntry.TABLE_NAME);
            courses = new ArrayList<>();
            if (c.moveToFirst()) {
                do {
                    courses.add(new Course(c));
                } while (c.moveToNext());
            }
        }
        return courses;
    }

    private List<String> departments;
    public List<String> getDepartments() {
        if(departments == null) {
            departments = new ArrayList<>();
            Query query = new Query(CourseReaderContract.CourseEntry.TABLE_NAME, new String[]{"DISTINCT " + CourseReaderContract.CourseEntry.COLUMN_NAME_DEPARTMENT_ID});
            QueryResult result = query(query);
            Cursor c = result.getCursor();
            if (c.moveToFirst()) {
                do {
                    departments.add(c.getString(0));
                } while (c.moveToNext());
            }
        }
        return departments;
    }

    /**
     * Gets the meeting times for many courses using the same database instance
     * @param courses the courses to find the meeting times for
     * @return the meeting times for the given courses
     */
    public List<MeetingTimeList>[] getManyMeetingTimes(List<Course> courses) {
        List<MeetingTimeList>[] arr = (List<MeetingTimeList>[]) new Object[courses.size()];
        try {
            reader.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        reader.openDataBase();
        for(int i = 0; i < courses.size(); i++) {
            arr[i] = this.getMeetingTimes(courses.get(i));
        }
        return arr;
    }

    /**
     * Gets the meeting times for one course
     * @param course the course to get the meeting times for
     * @return the meeting times for the given course
     */
    public List<MeetingTimeList> getMeetingTimes(Course course) {
        reader.openDataBase();
        String whereStatement = CourseReaderContract.MeetingTimeListEntry.COLUMN_NAME_COURSE_ID + " = " + course.getId();
        Cursor c = reader.query(CourseReaderContract.MeetingTimeListEntry.TABLE_NAME, whereStatement);
        List<MeetingTimeList> meetingTimeLists = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                meetingTimeLists.add(new MeetingTimeList(c));
            } while (c.moveToNext());
        }
        return meetingTimeLists;
    }

    protected QueryResult query(Query query) {
        try {
            reader.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        reader.openDataBase();
        return new QueryResult(query, reader.query(query));
    }
}
