package com.markwiggans.vtscheduler.database;

import android.content.Context;
import android.os.AsyncTask;

import com.markwiggans.vtscheduler.data.CRN;
import com.markwiggans.vtscheduler.data.Course;
import com.markwiggans.vtscheduler.data.Schedule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark Wiggans on 3/27/2017.
 * AsyncTask that generates schedules
 */
public class ScheduleGenerationTask extends AsyncTask<Course, Void, List<Schedule>> {
    private Context context;
    private ScheduleGeneratorTaskReceiver receiver;

    public ScheduleGenerationTask(Context context) {
        this(context, (ScheduleGeneratorTaskReceiver) context);
    }

    public ScheduleGenerationTask(Context context, ScheduleGeneratorTaskReceiver receiver) {
        this.context = context;
        this.receiver = receiver;
    }

    @Override
    protected List<Schedule> doInBackground(Course... params) {
        //TODO: This is where the schedules should be generated
        //Put in some code to generate two schedules for testing
//        try {
//            //Sleep to make execution time seem realistic
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        List<Schedule> schedules = new ArrayList<>();
        Query query0 = new Query(CourseReaderContract.CRNEntry.TABLE_NAME, "crn = 12613 or crn = 18154", null);
        Query query1 = new Query(CourseReaderContract.CRNEntry.TABLE_NAME, "crn = 12613 or crn = 18154", null);
        List<QueryResult> results = new DatabaseTask(null, context).doInBackground(query0, query1);
        schedules.add(new Schedule(CRN.createCRNs(results.get(0).getCursor())));
        schedules.add(new Schedule(CRN.createCRNs(results.get(1).getCursor())));
        return schedules;
    }

    @Override
    protected void onPostExecute(List<Schedule> result) {
        receiver.onSchedulesGenerated(result);
    }

    public interface ScheduleGeneratorTaskReceiver {
        void onSchedulesGenerated(List<Schedule> results);
    }
}
