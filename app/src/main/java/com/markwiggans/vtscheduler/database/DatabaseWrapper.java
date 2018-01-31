package com.markwiggans.vtscheduler.database;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Debug;
import android.os.Looper;
import android.widget.Toast;

import com.markwiggans.vtscheduler.data.Schedule;
import com.markwiggans.vtscheduler.interfaces.OnEventListener;

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
    public void query(OnEventListener<List<QueryResult>> receiver, Query query) {
        new DatabaseTask(receiver).execute(query);
    }

    /**
     * Do query asynchronously
     *
     * @param receiver interface instance to receive object
     * @param query searches to run
     */
    public void queryAll(OnEventListener<List<QueryResult>> receiver, Query... query) {
        queryAll(receiver, true, query);
    }

    /**
     * Do query asynchronously
     *
     * @param receiver interface instance to receive object
     * @param query searches to run
     */
    public void queryAll(OnEventListener<List<QueryResult>> receiver, boolean closeFPS, Query... query) {
        new DatabaseTask(receiver, closeFPS).execute(query);
    }

    private void verifyNotOnUIThread() {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            throw new IllegalStateException("Cannot do synchronous query on ui thread");
        }
    }

    public void insert(Schedule schedule, String uuid) {
        new DatabaseInsertionTask(schedule, uuid).execute();
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
        verifyNotOnUIThread();
        return new DatabaseTask().doInBackground(query).get(0);
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
        verifyNotOnUIThread();
        return new DatabaseTask().doInBackground(query);
    }

    /**
     * Created by Mark Wiggans on 3/27/2017.
     * AsyncTask that does database queries
     */
    private class DatabaseTask extends AsyncTask<Query, Void, List<QueryResult>> {
        private OnEventListener<List<QueryResult>> receiver;
        private boolean closeFPs = true;

        private DatabaseTask() {
            this(null);
        }

        private DatabaseTask(OnEventListener<List<QueryResult>> receiver) {
            this.receiver = receiver;
        }

        private DatabaseTask(OnEventListener<List<QueryResult>> receiver, boolean closeFPs) {
            this.receiver = receiver;
            this.closeFPs = closeFPs;
        }

        @Override
        protected List<QueryResult> doInBackground(Query... params) {
            openDatabase();
            List<QueryResult> outList = new ArrayList<>();
            for(Query query : params) {
                Cursor c = reader.query(query);
                QueryResult qr = new QueryResult(query, c);
                outList.add(qr);
            }
            return outList;
        }

        @Override
        protected void onPostExecute(List<QueryResult> result) {
            receiver.onSuccess(result);
            if(closeFPs) {
                for(QueryResult qr : result) {
                    qr.getCursor().close();
                }
            }
        }
    }

    /**
     * Created by Mark Wiggans on 3/27/2017.
     * AsyncTask that does database queries
     */
    private class DatabaseInsertionTask extends AsyncTask<Void, Void, Void> {
        private Schedule schedule;
        private String uuid;
        private DatabaseInsertionTask(Schedule schedule, String uuid) {
            this.schedule = schedule;
            this.uuid = uuid;
        }

        @Override
        protected Void doInBackground(Void... params) {
            openDatabase();
            reader.insert(schedule, uuid);
            return null;
        }
    }
}
