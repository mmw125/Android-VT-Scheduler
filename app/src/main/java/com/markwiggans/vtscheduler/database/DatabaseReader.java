package com.markwiggans.vtscheduler.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.markwiggans.vtscheduler.MainActivity;
import com.markwiggans.vtscheduler.data.CRN;
import com.markwiggans.vtscheduler.data.Schedule;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Mark Wiggans on 2/8/2017.
 * Wrapper for the SQLiteDatabase object
 * http://www.vogella.com/tutorials/AndroidSQLite/article.html#sqlite-and-android
 */
class DatabaseReader extends SQLiteOpenHelper {
    private String DB_PATH = null;
    private static String DB_NAME = "externalDB.sqlite3";
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    DatabaseReader(Context context){
        super(context, DB_NAME, null, 10);
        this.myContext = context;
        this.DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
    }

    void createDataBase() {
        if (!checkDataBase()) {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        }catch (SQLiteException e) {
            // Don't need to do anything here. We are expecting checkDB to fail the first time
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null;
    }

    private void copyDataBase() throws IOException {
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[10];
        int length;
        while((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        if(myDataBase == null || !myDataBase.isOpen()) {
            myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        }
    }

    public synchronized void close() {
        if(myDataBase != null) {
            myDataBase.close();
        }
        super.close();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    Cursor query(Query query) {
        return myDataBase.query(query.getTable(), query.getColumns(), query.getSelection(),
                query.getSelectionArgs(), query.getGroupBy(), query.getHaving(),
                query.getOrderBy(), query.getLimit());
    }

    /**
     * Gets all of the elements in the table with the given table
     * @param table the table to query
     * @return cursor on the given table
     */
    public Cursor query(String table) {
        return myDataBase.query(table, null, null, null, null, null, null, null);
    }

    /**
     * Gets all of the elements in the table with the given attribute
     * @param table name of the table to query
     * @param selection selection statement to match items on
     * @return the cursor on the elements
     */
    public Cursor query(String table, String selection) {
        return myDataBase.query(table, null, selection, null, null, null, null, null);
    }

    public void insert(Schedule schedule, String uuid) {
        Cursor c = query(CourseReaderContract.ScheduleEntry.TABLE_NAME);
        c.moveToFirst();
        Log.d(MainActivity.LOG_STRING, "Before add, " + c.getCount() + " schedules");
        c.close();
        Log.d(MainActivity.LOG_STRING, "Starting Database insert");
        ContentValues values = new ContentValues();
        values.put(CourseReaderContract.ScheduleEntry.COLUMN_NAME_SCHEDULE_UUID, uuid);
        values.put(CourseReaderContract.ScheduleEntry.COLUMN_NAME_NAME, "");
        myDataBase.beginTransaction();
        long returnVal = myDataBase.insert(CourseReaderContract.ScheduleEntry.TABLE_NAME,
                CourseReaderContract.ScheduleEntry.COLUMN_NAME_NAME + ", " + CourseReaderContract.ScheduleEntry.COLUMN_NAME_SCHEDULE_UUID, values);
        if(returnVal == -1) {
            Log.d(MainActivity.LOG_STRING, "An error occurred");
        }
        for(CRN crn : schedule.getCrns()) {
            values = new ContentValues();
            values.put(CourseReaderContract.ScheduleItemEntry.COLUMN_NAME_CRN_CRN, crn.getCRN());
            values.put(CourseReaderContract.ScheduleItemEntry.COLUMN_NAME_CRN_SEMESTER, crn.getSemester());
            values.put(CourseReaderContract.ScheduleItemEntry.COLUMN_NAME_SCHEDULE_ID, uuid);
            returnVal = myDataBase.insert(CourseReaderContract.ScheduleItemEntry.TABLE_NAME,
                    CourseReaderContract.ScheduleItemEntry.COLUMN_NAME_CRN_CRN + ", " + CourseReaderContract.ScheduleItemEntry.COLUMN_NAME_CRN_SEMESTER + ", " + CourseReaderContract.ScheduleItemEntry.COLUMN_NAME_SCHEDULE_ID, values);
            if(returnVal == -1) {
                Log.d(MainActivity.LOG_STRING, "An error occurred");
            }
        }
        myDataBase.setTransactionSuccessful();
        myDataBase.endTransaction();
        c = query(CourseReaderContract.ScheduleEntry.TABLE_NAME);
        c.moveToFirst();
        Log.d(MainActivity.LOG_STRING, "After add, " + c.getCount() + " schedules");
        c.close();
    }
}
