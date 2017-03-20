package com.markwiggans.vtscheduler.data;

import android.content.Context;
import android.database.Cursor;

import com.markwiggans.vtscheduler.database.DataSource;

import java.util.List;

/**
 * Created by Mark Wiggans on 3/19/2017.
 */
public class Course {
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

    private List<MeetingTimeList> meetingTimeLists;
    public List<MeetingTimeList> getMeetingTimeLists(Context context) {
        if(meetingTimeLists == null) {
            meetingTimeLists = DataSource.getInstance().getMeetingTimes(context, this);
        }
        return meetingTimeLists;
    }

    public void setMeetingTimeLists(List<MeetingTimeList> meetingTimeLists){
        this.meetingTimeLists = meetingTimeLists;
    }

    public List<MeetingTimeList> getCachedMeetingTimeLists() {
        return meetingTimeLists;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return wholeName;
    }
}
