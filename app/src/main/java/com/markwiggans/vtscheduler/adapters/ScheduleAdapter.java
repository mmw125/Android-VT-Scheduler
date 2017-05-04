package com.markwiggans.vtscheduler.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.markwiggans.vtscheduler.R;
import com.markwiggans.vtscheduler.data.CRN;
import com.markwiggans.vtscheduler.data.Schedule;

import java.util.List;

/**
 * Created by Mark Wiggans on 3/27/2017.
 * Adapter for viewing schedules
 */
public class ScheduleAdapter extends ArrayAdapter<Schedule> {
    private Context context;

    public ScheduleAdapter(@NonNull Context context, int id, List<Schedule> schedules) {
        super(context, id, schedules);
        this.context = context;
    }

    public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_schedule, parent, false);
        }
        Schedule schedule = getItem(position);
        TextView textView = (TextView) convertView.findViewById(R.id.parent);
        textView.setText(context.getString(R.string.score_display, schedule.getScore()));
        textView = (TextView) convertView.findViewById(R.id.text_view);
        String s = "";
        for(CRN c : schedule.getCrns()) {
            if(s.length() > 0) {
                s += "\n";
            }
            String courseStr = c.getCourse() == null ? "" : c.getCourse().getWholeName();
            s += courseStr + " " + c.getCRN() + " " + c.getInstructor();
        }
        textView.setText(s);
        return convertView;
    }
}
