package com.markwiggans.vtscheduler.data;

import android.database.Cursor;
import android.support.annotation.NonNull;

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
        id = c.getInt(0);
        day = Day.stringToDay(c.getString(1));
        startTime = c.getInt(2) + day.toInt();
        startTimeString = c.getString(3);
        endTime = c.getInt(4) + day.toInt();
        endTimeString = c.getString(5);
        crn = c.getInt(6);
        semester = c.getString(7);
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
                case MONDAY: return 10000;
                case TUESDAY: return 20000;
                case WEDNESDAY: return 30000;
                case THURSDAY: return 40000;
                case FRIDAY: return 50000;
                case SATURDAY: return 60000;
                case SUNDAY: return 70000;
                default: return 0;
            }
        }
    }

    @Override
    public int compareTo(@NonNull MeetingTime o) {
        return this.getStartTime() - o.getStartTime();
    }
}
