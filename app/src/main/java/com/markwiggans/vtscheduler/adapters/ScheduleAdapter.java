package com.markwiggans.vtscheduler.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.markwiggans.vtscheduler.R;
import com.markwiggans.vtscheduler.data.CRN;
import com.markwiggans.vtscheduler.data.DataSource;
import com.markwiggans.vtscheduler.data.Schedule;

import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

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
        textView.setText(context.getString(R.string.score_display, schedule.getScore(context)));
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
        final Schedule schedule = getItem(position);
        final Context context = getContext();

        String sem = "";
        if (schedule.getCrns().size() >= 1) {
            sem = schedule.getCrns().get(0).getSemester();
        }

        String str = UUID.randomUUID().toString();
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Schedule_UUID", str);
        try {
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Copied UUID to clipboard", Toast.LENGTH_SHORT).show();
        } catch (java.lang.NullPointerException e) {
            Toast.makeText(context, "Could not copy UUID to clipboard", Toast.LENGTH_SHORT).show();
        }
        DataSource.saveSchedule(context, schedule, str);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(context, context.getString(R.string.saving_reminder), Toast.LENGTH_SHORT).show();
    }
}
