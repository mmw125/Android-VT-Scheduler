package com.markwiggans.vtscheduler.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.markwiggans.vtscheduler.MainActivity;
import com.markwiggans.vtscheduler.R;
import com.markwiggans.vtscheduler.data.Course;
import com.markwiggans.vtscheduler.data.Schedule;

import java.util.List;

/**
 * Created by Mark Wiggans on 3/27/2017.
 * Adapter for viewing courses
 */
public class ScheduleAdapter extends ArrayAdapter<Schedule> {
    private boolean nameFirst;
    private Context context;
    public ScheduleAdapter(@NonNull Context context, int id, List<Schedule> schedules) {
        this(context, id, schedules, true);
    }

    public ScheduleAdapter(@NonNull Context context, int id, List<Schedule> schedules, boolean nameFirst) {
        super(context, id, schedules);
        this.nameFirst = nameFirst;
        this.context = context;
    }

    public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_schedule, parent, false);
        }
        Schedule schedule = getItem(position);
        ListView listView = (ListView) convertView.findViewById(R.id.crn_list);
        listView.setAdapter(new CRNAdapter(context, schedule.getCrns()));
        return convertView;
    }
}
