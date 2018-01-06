package com.markwiggans.vtscheduler.database;

import android.provider.BaseColumns;

/**
 * Created by Mark Wiggans on 2/8/2017.
 * This stores the table names and column names in the database
 * https://developer.android.com/training/basics/data-storage/databases.html
 */
public class CourseReaderContract {
    private CourseReaderContract() {}

    public static class CourseEntry implements BaseColumns {
        public static final String TABLE_NAME = "course";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_WHOLE_NAME= "whole_name";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_COURSE_NUMBER = "course_number";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_DEPARTMENT_ID = "department_id";
        public static final String COLUMN_NAME_SEMESTER_ID = "semester";
    }

    public static class CRNEntry implements BaseColumns {
        public static final String TABLE_NAME = "crn";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_CRN = "crn";
        public static final String COLUMN_NAME_CRN_TEXT = "crn_txt";
        public static final String COLUMN_NAME_INSTRUCTOR = "instructor";
        public static final String COLUMN_NAME_LOCATION = "location";
        public static final String COLUMN_COURSE_WHOLE_NAME = "course_whole_name";
        public static final String COLUMN_COURSE_SEMESTER = "course_semester";
        public static final String COLUMN_COURSE_TYPE = "course_type";
    }

    public static class MeetingTimeEntry implements BaseColumns {
        public static final String TABLE_NAME = "meetingtime";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_DAY = "day";
        public static final String COLUMN_NAME_START_TIME = "start_time";
        public static final String COLUMN_NAME_START_TIME_STR = "start_time_str";
        public static final String COLUMN_NAME_END_TIME = "end_time";
        public static final String COLUMN_NAME_END_TIME_STR = "end_time_str";
        public static final String COLUMN_NAME_CRN_CRN = "crn_crn";
        public static final String COLUMN_NAME_CRN_SEMESTER = "crn_semester";
    }

    public static class DepartmentEntry implements BaseColumns {
        public static final String TABLE_NAME = "department";
        public static final String COLUMN_NAME_DEPARTMENT_NAME = "department_name";
        public static final String COLUMN_NAME_DEPARTMENT_ABB = "department_abbreviation";
    }

    public static class ScheduleEntry implements BaseColumns {
        public static final String TABLE_NAME = "schedule";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_SCHEDULE_UUID = "schedule_uuid";
    }

    public static class ScheduleItemEntry implements BaseColumns {
        public static final String TABLE_NAME = "scheduleitem";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_CRN_CRN = "crn_crn";
        public static final String COLUMN_NAME_CRN_SEMESTER = "crn_semester";
        public static final String COLUMN_NAME_SCHEDULE_ID = "schedule_uuid";
    }
}
