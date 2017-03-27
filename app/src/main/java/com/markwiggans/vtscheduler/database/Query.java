package com.markwiggans.vtscheduler.database;

/**
 * Created by Mark Wiggans on 3/27/2017.
 */

public class Query {
    private String table, selection, groupBy, having, orderBy, limit;
    private String[] columns, selectionArgs;

    public Query(String table) {
        this(table, null);
    }

    public Query(String table, String selection, String[] selectionArgs) {
        this(table, null, selection, selectionArgs, null, null, null, null);
    }

    public Query(String table, String[] columns) {
        this(table, columns, null, null, null, null, null, null);
    }

    public Query(String table, String[] columns, String selection, String[] selectionArgs,
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

    public String getTable() {
        return table;
    }

    public String getSelection() {
        return selection;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public String getHaving() {
        return having;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public String getLimit() {
        return limit;
    }

    public String[] getColumns() {
        return columns;
    }

    public String[] getSelectionArgs() {
        return selectionArgs;
    }
}
