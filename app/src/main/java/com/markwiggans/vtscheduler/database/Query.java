package com.markwiggans.vtscheduler.database;

import android.database.Cursor;

/**
 * Created by Mark Wiggans on 3/27/2017
 * Represents a query
 */
public class Query {
    private String table, selection, groupBy, having, orderBy, limit;
    private String[] columns, selectionArgs;

    public Query(String table) {
        this(table, null, null);
    }

    public Query(String table, String selection, String[] selectionArgs) {
        this(table, null, selection, selectionArgs, null, null, null, null);
    }

    public Query(String table, String selection) {
        this(table, null, selection, null, null, null, null, null);
    }

    public Query(String table, String[] columns) {
        this(table, columns, null, null, null, null, null, null);
    }

    private Query(String table, String[] columns, String selection, String[] selectionArgs,
                 String groupBy, String having, String orderBy, String limit) {
        this.table = table;
        this.columns = columns;
        this.selection = selection;
        this.selectionArgs = selectionArgs;
        this.groupBy = groupBy;
        this.having = having;
        this.orderBy = orderBy;
        this.limit = limit;
    }

    String getTable() {
        return table;
    }

    String getSelection() {
        return selection;
    }

    String getGroupBy() {
        return groupBy;
    }

    String getHaving() {
        return having;
    }

    String getOrderBy() {
        return orderBy;
    }

    String getLimit() {
        return limit;
    }

    String[] getColumns() {
        return columns;
    }

    String[] getSelectionArgs() {
        return selectionArgs;
    }
}
