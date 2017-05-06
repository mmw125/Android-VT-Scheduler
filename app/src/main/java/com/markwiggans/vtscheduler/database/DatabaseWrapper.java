package com.markwiggans.vtscheduler.database;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark Wiggans on 3/19/2017.
 * Wrapper for the DatabaseReader class
 */
public class DatabaseWrapper {
    private static DatabaseWrapper instance;

    /**
     * Gets the data source instance
     *
     * @return the data source instance
     */
    public static DatabaseWrapper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseWrapper(context);
        }
        return instance;
    }

    private DatabaseReader reader;
    private boolean checked = false;

    private DatabaseWrapper(Context context) {
        reader = new DatabaseReader(context);
        if(Looper.getMainLooper().getThread() != Thread.currentThread()) {
            reader.createDataBase();
            checked = true;
        }
    }

    private synchronized void checkDatabase() {
        if(checked)
            return;
        reader.createDataBase();
        checked = true;
    }

    private void openDatabase() {
        checkDatabase();
        reader.openDataBase();
    }

    /**
     * Do query asynchronously
     *
     * @param receiver interface instance to receive object
     * @param query searches to run
     */
    public void query(DatabaseTaskReceiver receiver, Query query) {
        new DatabaseTask(receiver).execute(query);
    }

    /**
     * Do query asynchronously
     *
     * @param receiver interface instance to receive object
     * @param query searches to run
     */
    public void queryAll(DatabaseTaskReceiver receiver, Query... query) {
        new DatabaseTask(receiver).execute(query);
    }

    /**
     * Do query synchronously
     *
     * YOU ARE RESPONSIBLE FOR CLOSING DATABASE AFTER THIS QUERY
     *
     * @param query searches to run
     * @return list of results of query
     */
    public QueryResult query(Query query) {
        if(Looper.getMainLooper().getThread() == Thread.currentThread())
            throw new IllegalStateException("Cannot do synchronous query on ui thread");
        return new DatabaseTask(null).doInBackground(query).get(0);
    }

    /**
     * Do query synchronously
     *
     * YOU ARE RESPONSIBLE FOR CLOSING DATABASE AFTER THIS QUERY
     *
     * @param query searches to run
     * @return list of results of query
     */
    public List<QueryResult> queryAll(Query... query) {
        if(Looper.getMainLooper().getThread() == Thread.currentThread())
            throw new IllegalStateException("Cannot do synchronous query on ui thread");
        return new DatabaseTask(null).doInBackground(query);
    }

    /**
     * Created by Mark Wiggans on 3/27/2017.
     * AsyncTask that does database queries
     */
    private class DatabaseTask extends AsyncTask<Query, Void, List<QueryResult>> {
        private DatabaseTaskReceiver receiver;

        private DatabaseTask(DatabaseTaskReceiver receiver) {
            this.receiver = receiver;
        }

        @Override
        protected List<QueryResult> doInBackground(Query... params) {
            openDatabase();
            List<QueryResult> outList = new ArrayList<>();
            for(Query query : params) {
                outList.add(new QueryResult(query, reader.query(query)));
            }
            return outList;
        }

        @Override
        protected void onPostExecute(List<QueryResult> result) {
            receiver.onDatabaseTask(result);
            for(QueryResult qr : result) {
                qr.getCursor().close();
            }
        }
    }

    public interface DatabaseTaskReceiver {
        void onDatabaseTask(List<QueryResult> results);
    }
}
