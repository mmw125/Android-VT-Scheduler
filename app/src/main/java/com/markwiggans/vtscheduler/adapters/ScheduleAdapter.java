package com.markwiggans.vtscheduler.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.markwiggans.vtscheduler.NetworkTask;
import com.markwiggans.vtscheduler.R;
import com.markwiggans.vtscheduler.data.CRN;
import com.markwiggans.vtscheduler.data.Schedule;

import java.util.List;

/**
 * Created by Mark Wiggans on 3/27/2017.
 * Adapter for viewing schedules
 */
public class ScheduleAdapter extends ArrayAdapter<Schedule> implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {
    private Context context;

    public ScheduleAdapter(@NonNull Context context, int id, List<Schedule> schedules) {
        super(context, id, schedules);
        this.context = context;
    }

    public
    @NonNull
    View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_schedule, parent, false);
        }
        Schedule schedule = getItem(position);
        TextView textView = (TextView) convertView.findViewById(R.id.parent);
        textView.setText(context.getString(R.string.score_display, schedule.getScore()));
        textView = (TextView) convertView.findViewById(R.id.text_view);
        String s = "";
        for (CRN c : schedule.getCrns()) {
            if (s.length() > 0) {
                s += "\n";
            }
            String courseStr = c.getCourse() == null ? "" : c.getCourse().getWholeName();
            s += courseStr + " " + c.getCRN() + " " + c.getInstructor() + "\n" + c.getMeetingTimesString();
        }
        textView.setText(s);
        return convertView;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Schedule schedule = getItem(position);

        String sem = "";
        if(schedule.getCrns().size() >= 1){
            sem = schedule.getCrns().get(0).getSemester();
        }

        new NetworkTask(context, true, sem, schedule.getCrns().toArray(new CRN[schedule.getCrns().size()]) , "").execute();
        //new NetworkTask(context, false, "",null ,"5095e598-3096-11e7-bf9b-0a3764d8e951").execute();

        //Toast.makeText(context, context.getString(R.string.saving), Toast.LENGTH_LONG).show();
        getItem(position).toString();

        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(context, context.getString(R.string.saving_reminder), Toast.LENGTH_SHORT).show();
    }
}
