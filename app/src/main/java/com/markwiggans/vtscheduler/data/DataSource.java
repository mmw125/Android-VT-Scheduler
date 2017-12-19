package com.markwiggans.vtscheduler.data;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.markwiggans.vtscheduler.MainActivity;
import com.markwiggans.vtscheduler.database.CourseReaderContract;
import com.markwiggans.vtscheduler.database.DatabaseWrapper;
import com.markwiggans.vtscheduler.database.Query;
import com.markwiggans.vtscheduler.database.QueryResult;
import com.markwiggans.vtscheduler.interfaces.OnEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark Wiggans on 3/19/2017.
 * Wrapper for the DatabaseReader class
 */
public class DataSource {
    public static Course getCorrespondingCourse(Context context, CRN crn) {
        String whereStr = CourseReaderContract.CourseEntry.COLUMN_NAME_WHOLE_NAME + " = '" + crn.getCourseWholeName()
                + "' and " + CourseReaderContract.CourseEntry.COLUMN_NAME_TYPE + " = '" + crn.getType()
                + "' and " + CourseReaderContract.CourseEntry.COLUMN_NAME_SEMESTER_ID + " = '" + crn.getSemester();
        Query q = new Query(CourseReaderContract.CourseEntry.TABLE_NAME, whereStr);
        Cursor c = DatabaseWrapper.getInstance(context).query(q).getCursor();
        List<Course> courses = Course.createCourses(c);
        c.close();
        return courses.size() > 0 ? courses.get(0) : null;
    }

    public static void getCourse(Context context, final OnEventListener<Course> receiver, final CRN crn) {
        getCourses(context, new OnEventListener<List<Course>>() {
            @Override
            public void onSuccess(List<Course> courses) {
                for (Course c : courses) {
                    if (crn.isCRNOf(c)) {
                        receiver.onSuccess(c);
                        return;
                    }
                }
                receiver.onFailure(null);
            }

            @Override
            public void onFailure(Exception e) {
                receiver.onFailure(e);
            }
        }, new Semester(crn.getSemester()));
    }

    public static void getSavedSchedules(final Context context, final OnEventListener<List<Schedule>> receiver) {
        Query items = new Query(CourseReaderContract.ScheduleItemEntry.TABLE_NAME);
        Query schedules = new Query(CourseReaderContract.ScheduleEntry.TABLE_NAME);
        DatabaseWrapper.getInstance(context).queryAll(new DatabaseWrapper.DatabaseTaskReceiver() {
            @Override
            public void onDatabaseTask(List<QueryResult> results) {
                Cursor itemCursor = results.get(0).getCursor();
                Cursor scheduleCursor = results.get(1).getCursor();
                new AsyncTask<Cursor, Void, ArrayList<Schedule>>() {
                    @Override
                    protected ArrayList<Schedule> doInBackground(Cursor... cursors) {
                        ArrayList<Schedule> schedules = Schedule.createSchedulesFromDatabase(context, cursors[0], cursors[1]);
                        cursors[0].close();
                        cursors[1].close();
                        return schedules;
                    }

                    @Override
                    protected void onPostExecute(ArrayList<Schedule> schedules) {
                        super.onPostExecute(schedules);
                        Log.d(MainActivity.LOG_STRING, schedules.size() + " schedules");
                        receiver.onSuccess(schedules);
                    }
                }.execute(scheduleCursor, itemCursor);
            }
        }, false, items, schedules);
    }

    private static List<Course> courses;
    /**
     * Queries the database for all of the courses. This is a cached operation
     */
    private static void getCourses(Context context, final OnEventListener<List<Course>> receiver) {
        getCourses(context, receiver, null);
    }

    /**
     * Gets the courses
     *
     * @param context  the main screen
     * @param receiver what to call when the task is done
     * @param semester filters
     */
    public static void getCourses(final Context context, final OnEventListener<List<Course>> listener, final Semester semester) {
        if (courses != null) {
            if (semester == null) {
                listener.onSuccess(courses);
                return;
            } else {
                List<Course> outCourses = new ArrayList<>();
                for (Course c : courses) {
                    if (c.getSemester().equals(semester.getName())) {
                        outCourses.add(c);
                    }
                }
                listener.onSuccess(outCourses);
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
                    getCourses(context, listener, semester);
                } else {
                    listener.onSuccess(courses);
                }
            }
        }, new Query(CourseReaderContract.CourseEntry.TABLE_NAME));

    }

    private static List<String> departments;

    /**
     * Gets the names of all of the departments on a separate thread
     * WARNING: receiver can be called before getDepartments returns
     *
     * @param context  the context
     * @param receiver the receiver for the data
     */
    public static void getDepartments(Context context, final OnEventListener<List<String>> receiver) {
        if (departments != null) {
            receiver.onSuccess(departments);
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
                receiver.onSuccess(departments);
            }
        }, query);
    }

    /**
     * Gets the meeting times for one course
     *
     * @param course the course to get the meeting times for
     * @return the meeting times for the given course
     */
    public static CRN getCRN(Context context, int crn, String semester) {
        String whereStatement = CourseReaderContract.CRNEntry.COLUMN_COURSE_SEMESTER + " = '" + semester + "' AND " +
                CourseReaderContract.CRNEntry.COLUMN_NAME_CRN + " = " + crn;
        Cursor c = DatabaseWrapper.getInstance(context).query(new Query(CourseReaderContract.CRNEntry.TABLE_NAME, whereStatement)).getCursor();
        if (c.moveToFirst()) {
            do {
                CRN crnOut = new CRN(c);
                c.close();
                return crnOut;
            } while (c.moveToNext());
        }
        c.close();
        return null;
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
                CourseReaderContract.CRNEntry.TABLE_NAME, whereStatement)).getCursor();
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
        Query q = new Query(CourseReaderContract.CRNEntry.TABLE_NAME, whereString);
        Cursor c = DatabaseWrapper.getInstance(context).query(q).getCursor();
        List<CRN> outList = CRN.createCRNs(c);
        c.close();
        return outList;
    }

    public static void getCRNs(Context context, String semester, int[] crns, final OnEventListener<List<CRN>> reciever) {
        StringBuilder whereString = new StringBuilder(CourseReaderContract.CRNEntry.COLUMN_COURSE_SEMESTER + " = '" + semester + "'");
        for (int i : crns) {
            whereString.append(crns[0] == i ? " and " : " or ");
            whereString.append(CourseReaderContract.CRNEntry.COLUMN_NAME_CRN);
            whereString.append(" = ");
            whereString.append(i);
        }
        DatabaseWrapper.getInstance(context).query(new DatabaseWrapper.DatabaseTaskReceiver() {
            @Override
            public void onDatabaseTask(List<QueryResult> results) {
                Cursor c = results.get(0).getCursor();
                List<CRN> outList = CRN.createCRNs(c);
                c.close();
                reciever.onSuccess(outList);
            }
        }, new Query(CourseReaderContract.CRNEntry.TABLE_NAME, whereString.toString()));
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

    private static List<String> courseNames;
    public static void getCourseNames(Context context, final OnEventListener<List<String>> receiver) {
        if (courseNames == null) {
            courseNames = new ArrayList<>();
            getCourses(context, new OnEventListener<List<Course>>() {
                @Override
                public void onSuccess(List<Course> courses) {
                    for (Course c : courses) {
                        courseNames.add(c.getCourseName());
                    }
                    receiver.onSuccess(courseNames);
                }
            });
        } else {
            receiver.onSuccess(courseNames);
        }
    }

    private static List<Semester> semesters = null;
    public static void getSemesters(Context context, final OnEventListener<List<Semester>> receiver) {
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
                    receiver.onSuccess(semesters);
                }
            }, query);
        } else {
            receiver.onSuccess(semesters);
        }
    }

    public static void saveSchedule(Context context, Schedule schedule, String uuid) {
        DatabaseWrapper.getInstance(context).insert(schedule, uuid);
    }
}
