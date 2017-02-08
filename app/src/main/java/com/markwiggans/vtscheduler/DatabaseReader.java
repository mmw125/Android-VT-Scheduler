package com.markwiggans.vtscheduler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mark Wiggans on 2/8/2017.
 * http://www.vogella.com/tutorials/AndroidSQLite/article.html#sqlite-and-android
 */

public class DatabaseReader extends SQLiteOpenHelper {
    public DatabaseReader(Context context, String name,
                          SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
