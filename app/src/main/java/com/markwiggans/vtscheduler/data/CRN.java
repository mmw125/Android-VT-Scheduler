package com.markwiggans.vtscheduler.data;

import android.database.Cursor;

import com.markwiggans.vtscheduler.database.CourseReaderContract;

/**
 * Created by Mark Wiggans on 3/19/2017.
 */
public class CRN {
    private int id;
    private int crn;
    // private String crnText;
    private String instructor;
    private String location;
    private int meetingTimesId;

    public CRN(Cursor c) {
        id = c.getInt(c.getColumnIndex(CourseReaderContract.CRNEntry.COLUMN_NAME_ID));
        crn = c.getInt(c.getColumnIndex(CourseReaderContract.CRNEntry.COLUMN_NAME_CRN));
        instructor = c.getString(c.getColumnIndex(CourseReaderContract.CRNEntry.COLUMN_NAME_INSTRUCTOR));
        location = c.getString(c.getColumnIndex(CourseReaderContract.CRNEntry.COLUMN_NAME_LOCATION));
        meetingTimesId = c.getInt(c.getColumnIndex(CourseReaderContract.CRNEntry.COLUMN_NAME_MEETING_TIMES_ID));
    }
}
