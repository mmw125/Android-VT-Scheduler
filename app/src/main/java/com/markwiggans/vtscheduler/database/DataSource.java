package com.markwiggans.vtscheduler.database;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.markwiggans.vtscheduler.data.Course;
import com.markwiggans.vtscheduler.data.CourseChip;
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
     *
     * @return the data source instance
     */
    public static DataSource getInstance(Context context) {
        if (instance == null) {
            instance = new DataSource(context);
        }
        return instance;
    }

    private DatabaseReader reader;

    private DataSource(Context context) {
        reader = new DatabaseReader(context);
    }

    public interface CoursesReceiver {
        public void receiveCourses(List<Course> courses);
    }

    private List<Course> courses;

    /**
     * Queries the database for all of the courses. This is a cached operation
     */
    public void getCourses(Context context, final CoursesReceiver receiver) {
        if (courses != null) {
            receiver.receiveCourses(courses);
            return;
        }
        Query query = new Query(CourseReaderContract.CourseEntry.TABLE_NAME);
        new DatabaseTask(new DatabaseTask.DatabaseTaskReceiver() {
            @Override
            public void onDatabaseTask(List<QueryResult> results) {
                courses = new ArrayList<>();
                Cursor c = results.get(0).getCursor();
                if (c.moveToFirst()) {
                    do {
                        courses.add(new Course(c));
                    } while (c.moveToNext());
                }
                receiver.receiveCourses(courses);
            }
        }, context).execute(query);

    }

    public interface DepartmentReceiver {
        public void receiveDepartments(List<String> departments);
    }

    private List<String> departments;

    /**
     * Gets the names of all of the departments on a separate thread
     * WARNING: receiver can be called before getDepartments returns
     *
     * @param context  the context
     * @param receiver the receiver for the data
     */
    public void getDepartments(Context context, final DepartmentReceiver receiver) {
        if (departments != null) {
            receiver.receiveDepartments(departments);
            return;
        }
        departments = new ArrayList<>();
        Query query = new Query(CourseReaderContract.CourseEntry.TABLE_NAME, new String[]{"DISTINCT " + CourseReaderContract.CourseEntry.COLUMN_NAME_DEPARTMENT_ID});

        new DatabaseTask(new DatabaseTask.DatabaseTaskReceiver() {
            @Override
            public void onDatabaseTask(List<QueryResult> results) {
                Cursor c = results.get(0).getCursor();
                if (c.moveToFirst()) {
                    do {
                        departments.add(c.getString(0));
                    } while (c.moveToNext());
                }
                receiver.receiveDepartments(departments);
            }
        }, context).execute(query);
    }

    /**
     * Gets the meeting times for many courses using the same database instance
     *
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
        for (int i = 0; i < courses.size(); i++) {
            arr[i] = this.getMeetingTimes(courses.get(i));
        }
        return arr;
    }

    /**
     * Gets the meeting times for one course
     *
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

    private List<CourseChip> courseNames;

    public interface CourseNameReceiver{
        public void receiveCourseNames(List<CourseChip> courseNames);
    }

    public void getCourseNames(Context context, final CourseNameReceiver receiver) {
        if (courseNames == null) {
            courseNames = new ArrayList<>();
            getCourses(context, new CoursesReceiver() {
                @Override
                public void receiveCourses(List<Course> courses) {
                    for (Course c : courses) {
                        courseNames.add(new CourseChip(c.getCourseName()));
                    }
                    receiver.receiveCourseNames(courseNames);
                }
            });

        } else {
            receiver.receiveCourseNames(courseNames);
        }
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
