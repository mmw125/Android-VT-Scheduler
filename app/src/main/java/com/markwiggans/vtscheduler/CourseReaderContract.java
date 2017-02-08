package com.markwiggans.vtscheduler;

import android.provider.BaseColumns;

/**
 * Created by Mark Wiggans on 2/8/2017.
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
}
