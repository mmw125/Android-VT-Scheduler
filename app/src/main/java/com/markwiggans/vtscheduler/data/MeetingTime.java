package com.markwiggans.vtscheduler.data;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.markwiggans.vtscheduler.database.CourseReaderContract;

/**
 * Created by Mark Wiggans on 3/19/2017.
 * Used to hold a time that a crn meets
 */
public class MeetingTime implements Comparable<MeetingTime>{
//    private int id;
    private Day day;
    private int startTime, endTime /*, crn */;
    private String startTimeString, endTimeString /*, semester */;

    private static final int DAY_LENGTH = 1440;

    MeetingTime(Cursor c) {
//        id = c.getInt(c.getColumnIndex(CourseReaderContract.MeetingTimeEntry.COLUMN_NAME_ID));
        day = Day.stringToDay(c.getString(c.getColumnIndex(CourseReaderContract.MeetingTimeEntry.COLUMN_NAME_DAY)));
        startTimeString = c.getString(c.getColumnIndex(CourseReaderContract.MeetingTimeEntry.COLUMN_NAME_START_TIME_STR));
        startTime = c.getInt(c.getColumnIndex(CourseReaderContract.MeetingTimeEntry.COLUMN_NAME_START_TIME)) + day.toInt();
        endTimeString = c.getString(c.getColumnIndex(CourseReaderContract.MeetingTimeEntry.COLUMN_NAME_END_TIME_STR));
        endTime = c.getInt(c.getColumnIndex(CourseReaderContract.MeetingTimeEntry.COLUMN_NAME_END_TIME)) + day.toInt();
//        crn = c.getInt(c.getColumnIndex(CourseReaderContract.MeetingTimeEntry.COLUMN_NAME_CRN_CRN));
//        semester = c.getString(c.getColumnIndex(CourseReaderContract.MeetingTimeEntry.COLUMN_NAME_CRN_SEMESTER));
    }

    /**
     * Gets the day that the meeting time is on.
     * @return the day.
     */
    Day getDay() {
        return this.day;
    }

    /**
     * Get the start time.
     * @return start time.
     */
    int getStartTime() {
        return this.startTime;
    }

    /**
     * Get the start time without day.
     * @return start time.
     */
    int getStartTimeWithoutDay() {
        return this.startTime - day.toInt();
    }

    /**
     * Get the end time.
     * @return the end time.
     */
    int getEndTime() {
        return this.endTime;
    }

    /**
     * Gets the time without the day.
     * @return the end time.
     */
    int getEndTimeWithoutDay() {
        return this.endTime - day.toInt();
    }

    public enum Day {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY, DEFAULT;

        /**
         * Creates a day from a string object;
         * @param s the string to create a day from.
         * @return a day from the string.
         */
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
                case TUESDAY: return DAY_LENGTH;
                case WEDNESDAY: return DAY_LENGTH * 2;
                case THURSDAY: return DAY_LENGTH * 3;
                case FRIDAY: return DAY_LENGTH * 4;
                case SATURDAY: return DAY_LENGTH * 5;
                case SUNDAY: return DAY_LENGTH * 6;
                default: return 0;
            }
        }

        public String toShortString() {
            switch (this) {
                case MONDAY: return "M";
                case TUESDAY: return "T";
                case WEDNESDAY: return "W";
                case THURSDAY: return "H";
                case FRIDAY: return "F";
                case SATURDAY: return "S";
                case SUNDAY: return "U";
            }
            return "ARR";
        }
    }

    @Override
    public int compareTo(@NonNull MeetingTime o) {
        return this.getStartTime() - o.getStartTime();
    }

    /**
     * Get the string used to create the object.
     * @return start time string.
     */
    String getStartTimeString() {
        return this.startTimeString;
    }

    /**
     * Get the string representing the end time.
     * @return end time string.
     */
    String getEndTimeString() {
        return this.endTimeString;
    }
}
