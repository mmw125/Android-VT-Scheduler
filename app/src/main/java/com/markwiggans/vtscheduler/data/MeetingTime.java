package com.markwiggans.vtscheduler.data;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.markwiggans.vtscheduler.database.CourseReaderContract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mark Wiggans on 3/19/2017.
 * Used to hold a time that a crn meets
 */
public class MeetingTime implements Comparable<MeetingTime>{
    private int id;
    private Day day;
    private int startTime, endTime, crn;
    private String startTimeString, endTimeString, semester;

    public MeetingTime(Cursor c) {
//        id = c.getInt(c.getColumnIndex(CourseReaderContract.MeetingTimeEntry.COLUMN_NAME_ID));
        day = Day.stringToDay(c.getString(c.getColumnIndex(CourseReaderContract.MeetingTimeEntry.COLUMN_NAME_DAY)));
        startTimeString = c.getString(c.getColumnIndex(CourseReaderContract.MeetingTimeEntry.COLUMN_NAME_START_TIME_STR));
        startTime = c.getInt(c.getColumnIndex(CourseReaderContract.MeetingTimeEntry.COLUMN_NAME_START_TIME));
        endTimeString = c.getString(c.getColumnIndex(CourseReaderContract.MeetingTimeEntry.COLUMN_NAME_END_TIME_STR));
        endTime = c.getInt(c.getColumnIndex(CourseReaderContract.MeetingTimeEntry.COLUMN_NAME_END_TIME));
//        crn = c.getInt(c.getColumnIndex(CourseReaderContract.MeetingTimeEntry.COLUMN_NAME_CRN_CRN));
//        semester = c.getString(c.getColumnIndex(CourseReaderContract.MeetingTimeEntry.COLUMN_NAME_CRN_SEMESTER));
    }

    public Day getDay() {
        return this.day;
    }

    public int getStartTime() {
        return this.startTime;
    }

    public int getEndTime() {
        return this.endTime;
    }

    public enum Day {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY, DEFAULT;

        private static Day stringToDay(String s) {
            switch (s) {
                case "MONDAY": return MONDAY;
                case "TUESDAY": return TUESDAY;
                case "WEDNESDAY": return WEDNESDAY;
                case "THURSDAY": return THURSDAY;
                case "FRIDAY": return FRIDAY;
                case "SATURDAY": return SATURDAY;
                case "SUNDAY": return SUNDAY;
            }
            return DEFAULT;
        }

        private int toInt() {
            switch(this) {
                case MONDAY: return 0;
                case TUESDAY: return 1440;
                case WEDNESDAY: return 2880;
                case THURSDAY: return 4320;
                case FRIDAY: return 5760;
                case SATURDAY: return 7200;
                case SUNDAY: return 8640;
                default: return 0;
            }
        }
    }

    @Override
    public int compareTo(@NonNull MeetingTime o) {
        return this.getStartTime() - o.getStartTime();
    }

    public String getStartTimeString() {
        return this.startTimeString;
    }

    public String getEndTimeString() {
        return this.endTimeString;
    }
}
