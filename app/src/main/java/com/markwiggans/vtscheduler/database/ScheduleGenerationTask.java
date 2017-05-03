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
        List<Schedule> schedules = new ArrayList<>();
        schedules.addAll(ScheduleGenerator.generateSchedules(context, new ArrayList<>(Arrays.asList(params))));
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
