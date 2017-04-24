package com.markwiggans.vtscheduler.database;

import android.content.Context;
import android.database.Cursor;

import com.markwiggans.vtscheduler.data.CRN;
import com.markwiggans.vtscheduler.data.Course;
import com.markwiggans.vtscheduler.data.Semester;

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
        void receiveCourses(List<Course> courses);
    }

    private List<Course> courses;

    /**
     * Queries the database for all of the courses. This is a cached operation
     */
    public void getCourses(Context context, final CoursesReceiver receiver) {
        getCourses(context, receiver, null);
    }

    public void getCourses(final Context context, final CoursesReceiver receiver, final Semester semester) {
        if (courses != null) {
            if (semester == null) {
                receiver.receiveCourses(courses);
                return;
            } else {
                List<Course> outCourses = new ArrayList<>();
                for (Course c : courses) {
                    if (c.getSemester().equals(semester.getName())) {
                        outCourses.add(c);
                    }
                }
                receiver.receiveCourses(outCourses);
                return;
            }
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
                if (semester != null) {
                    getCourses(context, receiver, semester);
                } else {
                    receiver.receiveCourses(courses);
                }
            }
        }, context).execute(query);

    }

    public interface DepartmentReceiver {
        void receiveDepartments(List<String> departments);
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

//    /**
//     * Gets the meeting times for many courses using the same database instance
//     *
//     * @param courses the courses to find the meeting times for
//     * @return the meeting times for the given courses
//     */
//    public List<MeetingTimeList>[] getManyMeetingTimes(List<Course> courses) {
//        List<MeetingTimeList>[] arr = (List<MeetingTimeList>[]) new Object[courses.size()];
//        try {
//            reader.createDataBase();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        reader.openDataBase();
//        for (int i = 0; i < courses.size(); i++) {
//            arr[i] = this.getMeetingTimes(courses.get(i));
//        }
//        return arr;
//    }

    /**
     * Gets the meeting times for one course
     *
     * @param course the course to get the meeting times for
     * @return the meeting times for the given course
     */
    public List<CRN> getCRNs(Course course) {
        reader.openDataBase();
        String whereStatement = CourseReaderContract.CRNEntry.COLUMN_COURSE_SEMESTER + " = " + course.getSemester() + " AND " +
                CourseReaderContract.CRNEntry.COLUMN_COURSE_WHOLE_NAME + " = " + course.getWholeName() + " AND " +
                CourseReaderContract.CRNEntry.COLUMN_COURSE_TYPE + " = " + course.getType();
        Cursor c = reader.query(CourseReaderContract.CRNEntry.TABLE_NAME, whereStatement);
        List<CRN> crns = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                crns.add(new CRN(c));
            } while (c.moveToNext());
        }
        return crns;
    }

    private List<String> courseNames;

    public interface CourseNameReceiver {
        void receiveCourseNames(List<String> courseNames);
    }

    public void getCourseNames(Context context, final CourseNameReceiver receiver) {
        if (courseNames == null) {
            courseNames = new ArrayList<>();
            getCourses(context, new CoursesReceiver() {
                @Override
                public void receiveCourses(List<Course> courses) {
                    for (Course c : courses) {
                        courseNames.add(c.getCourseName());
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

    public interface SemesterReceiver {
        void receiveSemesters(List<Semester> semesters);
    }

    private List<Semester> semesters = null;
    public void getSemesters(Context context, final SemesterReceiver receiver) {
        if (semesters == null) {
            semesters = new ArrayList<>();
            Query query = new Query(CourseReaderContract.CourseEntry.TABLE_NAME, new String[]{"distinct " + CourseReaderContract.CourseEntry.COLUMN_NAME_SEMESTER_ID});
            new DatabaseTask(new DatabaseTask.DatabaseTaskReceiver() {
                @Override
                public void onDatabaseTask(List<QueryResult> results) {
                    Cursor c = results.get(0).getCursor();
                    if (c.moveToFirst()) {
                        do {
                            semesters.add(new Semester(c));
                        } while (c.moveToNext());
                    }
                    receiver.receiveSemesters(semesters);
                }
            }, context).execute(query);
        } else {
            receiver.receiveSemesters(semesters);
        }
    }
}
