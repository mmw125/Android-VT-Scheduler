package com.markwiggans.vtscheduler.database;

import android.database.Cursor;

/**
 * Created by Mark Wiggans on 3/27/2017.
 */
public class QueryResult {
    private Query query;
    private Cursor cursor;
    public QueryResult(Query query, Cursor cursor) {
        this.query = query;
        this.cursor = cursor;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public Query getQuery() {
        return query;
    }
}
