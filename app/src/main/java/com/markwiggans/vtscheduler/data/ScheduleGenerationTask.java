package com.markwiggans.vtscheduler.data;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.markwiggans.vtscheduler.R;

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

    @Override
    protected List<Schedule> doInBackground(Course... params) {
        List<Schedule> schedules = new ArrayList<>();
        schedules.addAll(ScheduleGenerator.generateSchedules(context, new ArrayList<>(Arrays.asList(params))));
        return schedules;

    }

    @Override
    protected void onPostExecute(List<Schedule> result) {
        receiver.onSchedulesGenerated(result);
        receiver.setErrorMessage(context.getString(R.string.unable_to_generate_schedules));
        Toast.makeText(context, "Finished generation of schedules!", Toast.LENGTH_SHORT).show();
    }

    public interface ScheduleGeneratorTaskReceiver {
        void onSchedulesGenerated(List<Schedule> results);
        void setErrorMessage(String message);
    }
}
