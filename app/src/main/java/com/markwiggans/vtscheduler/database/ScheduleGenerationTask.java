package com.markwiggans.vtscheduler.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.markwiggans.vtscheduler.data.CRN;
import com.markwiggans.vtscheduler.data.Course;
import com.markwiggans.vtscheduler.data.Schedule;
import com.markwiggans.vtscheduler.data.ScheduleGenerator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.ContentValues.TAG;

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


    //TODO: Fix issue where params is empty and not actually containing courses entered by user
    @Override
    protected List<Schedule> doInBackground(Course... params) {
        //TODO: This is where the schedules should be generated
        Log.d("schedule_gen", "Arrays.asList(params) size = "+Arrays.asList(params).size());
        List<Schedule> schedules = new ArrayList<>();
        schedules.addAll(ScheduleGenerator.generateSchedules(context, new ArrayList<Course>(Arrays.asList(params))));

        //Put in some code to generate two schedules for testing
        try {
            //Sleep to make execution time seem realistic
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Query query0 = new Query(CourseReaderContract.CRNEntry.TABLE_NAME, "crn = 18154", null);
        Query query1 = new Query(CourseReaderContract.CRNEntry.TABLE_NAME, "crn = 18154", null);
        List<QueryResult> results = new DatabaseTask(null, context).doInBackground(query0, query1);
        schedules.add(new Schedule(CRN.createCRNs(results.get(0).getCursor())));
        schedules.add(new Schedule(CRN.createCRNs(results.get(1).getCursor())));
        return schedules;

    }

    @Override
    protected void onPostExecute(List<Schedule> result) {
        receiver.onSchedulesGenerated(result);
        Toast.makeText(context, "Finished generation of schedules!", Toast.LENGTH_SHORT).show();
    }

    public interface ScheduleGeneratorTaskReceiver {
        void onSchedulesGenerated(List<Schedule> results);
    }
}
