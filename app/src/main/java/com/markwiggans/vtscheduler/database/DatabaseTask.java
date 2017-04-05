package com.markwiggans.vtscheduler.database;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark Wiggans on 3/27/2017.
 */
public class DatabaseTask extends AsyncTask<Query, Void, List<QueryResult>> {
    private Context context;
    private DatabaseTaskReceiver receiver;

    public DatabaseTask(Context context) {
        this((DatabaseTaskReceiver) context, context);
    }

    public DatabaseTask(DatabaseTaskReceiver receiver, Context context) {
        this.context = context;
        this.receiver = receiver;
    }

    @Override
    protected List<QueryResult> doInBackground(Query... params) {
        DataSource source = DataSource.getInstance(context);
        List<QueryResult> outList = new ArrayList<>();
        for(Query query : params) {
            outList.add(source.query(query));
        }
        return outList;
    }

    @Override
    protected void onPostExecute(List<QueryResult> result) {
        receiver.onDatabaseTask(result);
    }

    public interface DatabaseTaskReceiver {
        void onDatabaseTask(List<QueryResult> results);
    }
}
