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
        public static final String TABLE_NAME = "scheduler_server_course";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_WHOLE_NAME= "whole_name";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_COURSE_NUMBER = "course_number";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_DEPARTMENT_ID = "department_id";
        public static final String COLUMN_NAME_SEMESTER_ID = "semester_id";
    }

    public static class MeetingTimeListEntry implements BaseColumns {
        public static final String TABLE_NAME = "scheduler_server_meetingtimelist";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_COURSE_ID = "course_id";
    }

    public static class CRNEntry implements BaseColumns {
        public static final String TABLE_NAME = "scheduler_server_crn";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_CRN = "crn";
        public static final String COLUMN_NAME_CRN_TEXT = "crn_txt";
        public static final String COLUMN_NAME_INSTRUCTOR = "instructor";
        public static final String COLUMN_NAME_LOCATION = "location";
        public static final String COLUMN_NAME_MEETING_TIMES_ID = "meeting_times_id";
    }

    public static class SemesterEntry implements BaseColumns {
        public static final String TABLE_NAME = "scheduler_server_semester";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_SEMESTER = "semester_name";
    }
}
