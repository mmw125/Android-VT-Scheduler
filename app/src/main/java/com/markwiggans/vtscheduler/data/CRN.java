package com.markwiggans.vtscheduler.data;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.markwiggans.vtscheduler.MainActivity;
import com.markwiggans.vtscheduler.database.CourseReaderContract;
import com.markwiggans.vtscheduler.interfaces.OnEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark Wiggans on 3/19/2017.
 * Represents a CRN of a class
 */
public class CRN {
    static List<CRN> createCRNs(Cursor c) {
        ArrayList<CRN> courses = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                CRN crn = new CRN(c);
                courses.add(crn);
            } while (c.moveToNext());
        }
        return courses;
    }

    private int crn;
    // private String crnText;
    private String instructor, location, semester, type, courseWholeName;
    private List<MeetingTime> meetingTimes;
    private Course course;

    /**
     * Creates the crn without a course.
     * @param c the database cursor to the crn.
     */
    public CRN(Cursor c) {
        this(null, c);
    }

    /**
     * Create a new crn object.
     * @param course the course that the crn is for.
     * @param c the database cursor for the crn in the database.
     */
    CRN(Course course, Cursor c) {
        crn = c.getInt(c.getColumnIndex(CourseReaderContract.CRNEntry.COLUMN_NAME_CRN));
        instructor = c.getString(c.getColumnIndex(CourseReaderContract.CRNEntry.COLUMN_NAME_INSTRUCTOR));
        location = c.getString(c.getColumnIndex(CourseReaderContract.CRNEntry.COLUMN_NAME_LOCATION));
        semester = c.getString(c.getColumnIndex(CourseReaderContract.CRNEntry.COLUMN_COURSE_SEMESTER));
        type = c.getString(c.getColumnIndex(CourseReaderContract.CRNEntry.COLUMN_COURSE_TYPE));
        courseWholeName = c.getString(c.getColumnIndex(CourseReaderContract.CRNEntry.COLUMN_COURSE_WHOLE_NAME));
        this.course = course;
        meetingTimes = new ArrayList<>();
    }

    /**
     * Gets the numerical crn for this crn
     * @return the crn number
     */
    public int getCRN() {
        return crn;
    }

    String getType() {
        return type;
    }

    String getCourseWholeName() {
        return courseWholeName;
    }

    public String getInstructor() {
        return instructor;
    }

    @Override
    public String toString() {
        return "" + crn;
    }

    public String getLocation() {
        return this.location;
    }

    public String getSemester() {
        return this.semester;
    }

    public String getMeetingTimesString() {
        StringBuilder builder = new StringBuilder();
        for(MeetingTime mt : getMeetingTimes()) {
            builder.append("\t");
            builder.append(mt.getDay());
            builder.append(" ");
            builder.append(mt.getStartTimeString());
            builder.append(" ");
            builder.append(mt.getEndTimeString());
            builder.append("\n");
        }
        return builder.toString();
    }

    public String getShortMeetingTimesString() {
        StringBuilder builder = new StringBuilder();
        List<MeetingTime> temp = new ArrayList<>(getMeetingTimes());
        for (int i = 0; i < temp.size(); i++) {
            MeetingTime mt = temp.get(i);
            builder.append(mt.getDay().toShortString());
            builder.append(" ");
            for (int j = i + 1; j < temp.size(); j++) {
                if (mt.getStartTimeWithoutDay() == temp.get(j).getStartTimeWithoutDay() &&
                        mt.getEndTimeWithoutDay() == temp.get(j).getEndTimeWithoutDay()) {
                    builder.append(temp.get(j).getDay().toShortString());
                    builder.append(" ");
                    temp.remove(j);
                    j--;
                }
            }
            builder.append(mt.getStartTimeString());
            builder.append(" ");
            builder.append(mt.getEndTimeString());
            builder.append("\n");
        }
        return builder.toString();
    }

    public List<MeetingTime> getMeetingTimes(Context context) {
        if(context != null && getMeetingTimes() == null) {
            updateMeetingTimes(context);
        }
        return getMeetingTimes();
    }

    List<MeetingTime> getMeetingTimes(){
        return meetingTimes;
    }

    public void updateMeetingTimes(Context c){
        DataSource.getMeetingTimes(c, this, new OnEventListener<List<MeetingTime>>() {
            @Override
            public void onSuccess(List<MeetingTime> object) {
                meetingTimes = object;
            }
        });
    }

    public Course getCourse(Context context) {
        if(getCourse() == null && context != null) {
            course = DataSource.getCorrespondingCourse(context, this);
        }
        return getCourse();
    }

    public Course getCourse() {
        return course;
    }

    boolean isCRNOf(Course c) {
        return c != null && c.getSemester() != null && c.getSemester().equals(semester) &&
                c.getWholeName() != null && c.getWholeName().equals(courseWholeName) &&
                c.getType() != null && c.getType().equals(type);
    }
}
