package com.markwiggans.vtscheduler.data;

import android.content.Context;
import android.database.Cursor;

import com.markwiggans.vtscheduler.database.CourseReaderContract;
import com.markwiggans.vtscheduler.database.DatabaseWrapper;
import com.markwiggans.vtscheduler.database.Query;
import com.markwiggans.vtscheduler.database.QueryResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark Wiggans on 3/19/2017.
 * Wrapper for the DatabaseReader class
 */
public class DataSource {
    public interface CoursesReceiver {
        void receiveCourses(List<Course> courses);
    }

    public static Course getCorrespondingCourse(Context context, CRN crn) {
        String whereStr = CourseReaderContract.CourseEntry.COLUMN_NAME_WHOLE_NAME + " = '" + crn.getCourseWholeName()
                + "' and " + CourseReaderContract.CourseEntry.COLUMN_NAME_TYPE + " = '" + crn.getType()
                + "' and " + CourseReaderContract.CourseEntry.COLUMN_NAME_SEMESTER_ID + " = '" + crn.getSemester();
        Query q = new Query(CourseReaderContract.CourseEntry.TABLE_NAME, whereStr, null);
        Cursor c = DatabaseWrapper.getInstance(context).query(q).getCursor();
        List<Course> courses = Course.createCourses(c);
        c.close();
        return courses.size() > 0 ? courses.get(0) : null;
    }

    public static void getCourse(Context context, final CoursesReceiver receiver, final CRN crn) {
        getCourses(context, new CoursesReceiver() {
            @Override
            public void receiveCourses(List<Course> courses) {
                for (Course c : courses) {
                    if (crn.isCRNOf(c)) {
                        ArrayList<Course> course = new ArrayList<>();
                        course.add(c);
                        receiver.receiveCourses(course);
                    }
                }
                receiver.receiveCourses(null);
            }
        }, new Semester(crn.getSemester()));
    }

    private static List<Course> courses;
    /**
     * Queries the database for all of the courses. This is a cached operation
     */
    private static void getCourses(Context context, final CoursesReceiver receiver) {
        getCourses(context, receiver, null);
    }

    /**
     * Gets the courses
     *
     * @param context  the main screen
     * @param receiver what to call when the task is done
     * @param semester filters
     */
    public static void getCourses(final Context context, final CoursesReceiver receiver, final Semester semester) {
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
        DatabaseWrapper.getInstance(context).query(new DatabaseWrapper.DatabaseTaskReceiver() {
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
        }, new Query(CourseReaderContract.CourseEntry.TABLE_NAME));

    }

    public interface DepartmentReceiver {
        void receiveDepartments(List<String> departments);
    }

    private static List<String> departments;

    /**
     * Gets the names of all of the departments on a separate thread
     * WARNING: receiver can be called before getDepartments returns
     *
     * @param context  the context
     * @param receiver the receiver for the data
     */
    public static void getDepartments(Context context, final DepartmentReceiver receiver) {
        if (departments != null) {
            receiver.receiveDepartments(departments);
            return;
        }
        departments = new ArrayList<>();
        Query query = new Query(CourseReaderContract.CourseEntry.TABLE_NAME, new String[]{"DISTINCT " + CourseReaderContract.CourseEntry.COLUMN_NAME_DEPARTMENT_ID});

        DatabaseWrapper.getInstance(context).query(new DatabaseWrapper.DatabaseTaskReceiver() {
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
        }, query);
    }

    /**
     * Gets the meeting times for one course
     *
     * @param course the course to get the meeting times for
     * @return the meeting times for the given course
     */
    public static List<CRN> getCRNs(Context context, Course course) {
        String whereStatement = CourseReaderContract.CRNEntry.COLUMN_COURSE_SEMESTER + " = '" + course.getSemester() + "' AND " +
                CourseReaderContract.CRNEntry.COLUMN_COURSE_WHOLE_NAME + " = '" + course.getWholeName() + "' AND " +
                CourseReaderContract.CRNEntry.COLUMN_COURSE_TYPE + " = '" + course.getType() + "'";
        Cursor c = DatabaseWrapper.getInstance(context).query(new Query(
                CourseReaderContract.CRNEntry.TABLE_NAME, whereStatement, null)).getCursor();
        List<CRN> crns = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                crns.add(new CRN(course, c));
            } while (c.moveToNext());
        }
        c.close();
        return crns;
    }

    public static List<CRN> getCRNs(Context context, String semester, int[] crns) {
        String whereString = CourseReaderContract.CRNEntry.COLUMN_COURSE_SEMESTER + " = '" + semester + "'";
        for (int i : crns) {
            whereString += crns[0] == i ? " and " : " or ";
            whereString += CourseReaderContract.CRNEntry.COLUMN_NAME_CRN + " = " + i;
        }
        Query q = new Query(CourseReaderContract.CRNEntry.TABLE_NAME, whereString, null);
        Cursor c = DatabaseWrapper.getInstance(context).query(q).getCursor();
        List<CRN> outList = CRN.createCRNs(c);
        c.close();
        return outList;
    }

    public static ArrayList<MeetingTime> getMeetingTimes(Context context, CRN crn) {
        String whereStatement = CourseReaderContract.MeetingTimeEntry.COLUMN_NAME_CRN_SEMESTER + " = '" + crn.getSemester()
                + "' AND " + CourseReaderContract.MeetingTimeEntry.COLUMN_NAME_CRN_CRN + " = " + crn.getCRN();
        Cursor c = DatabaseWrapper.getInstance(context).query(new Query(CourseReaderContract.MeetingTimeEntry.TABLE_NAME, whereStatement)).getCursor();
        ArrayList<MeetingTime> mtl = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                mtl.add(new MeetingTime(c));
            } while (c.moveToNext());
        }
        c.close();
        return mtl;
    }

    interface CourseNameReceiver {
        void receiveCourseNames(List<String> courseNames);
    }

    private static List<String> courseNames;
    public static void getCourseNames(Context context, final CourseNameReceiver receiver) {
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

    public interface SemesterReceiver {
        void receiveSemesters(List<Semester> semesters);
    }

    private static List<Semester> semesters = null;
    public static void getSemesters(Context context, final SemesterReceiver receiver) {
        if (semesters == null) {
            semesters = new ArrayList<>();
            Query query = new Query(CourseReaderContract.CourseEntry.TABLE_NAME, new String[]{"distinct " + CourseReaderContract.CourseEntry.COLUMN_NAME_SEMESTER_ID});
            DatabaseWrapper.getInstance(context).query(new DatabaseWrapper.DatabaseTaskReceiver() {
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
            }, query);
        } else {
            receiver.receiveSemesters(semesters);
        }
    }
}
