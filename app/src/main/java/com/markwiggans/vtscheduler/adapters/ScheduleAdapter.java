package com.markwiggans.vtscheduler.adapters;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alamkanak.weekview.WeekView;
import com.markwiggans.vtscheduler.NetworkTask;
import com.markwiggans.vtscheduler.R;
import com.markwiggans.vtscheduler.data.CRN;
import com.markwiggans.vtscheduler.data.Schedule;
import com.markwiggans.vtscheduler.fragments.ScheduleWeekViewFragment;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Mark Wiggans on 3/27/2017.
 * Adapter for viewing schedules
 */
public class ScheduleAdapter extends ArrayAdapter<Schedule> implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {
    private Context context;
    private FragmentManager fragmentManager;

    public ScheduleAdapter(@NonNull Context context, int id, List<Schedule> schedules, FragmentManager fragmentManager) {
        super(context, id, schedules);
        this.context = context;
        this.fragmentManager = fragmentManager;
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
        Schedule schedule = getItem(position);

        String sem = "";
        if(schedule.getCrns().size() >= 1){
            sem = schedule.getCrns().get(0).getSemester();
        }

        new NetworkTask(context, true, sem, schedule.getCrns().toArray(new CRN[schedule.getCrns().size()]) , ""){
            // Doing this so that I can access the data from onPostExecute
            @Override
            protected void onPostExecute( JSONObject result ) {
                String str = "";
                try{
                    str = result.getString("unique_id:");
                }catch (Exception e){
                    Log.d("Scheduler", e.toString());
                }
                super.onPostExecute(result);
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Schedule_UUID", str);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "Copied UUID to clipboard", Toast.LENGTH_SHORT).show();
            }
        }.execute();

        FragmentTransaction ft = this.fragmentManager.beginTransaction();
        //FragmentTransaction ft = parent.getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new ScheduleWeekViewFragment()).addToBackStack(null).commit();

        return true;
    }

    public void showPopup(View anchorView) {
        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_schedule_week_view, null);

        PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // Example: If you have a TextView inside `popup_layout.xml`
        WeekView weekView = (WeekView) popupView.findViewById(R.id.weekView);


        // If the PopupWindow should be focusable
        popupWindow.setFocusable(true);

        // If you need the PopupWindow to dismiss when when touched outside
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        int location[] = new int[2];

        // Get the View's(the one that was clicked in the Fragment) location
        anchorView.getLocationOnScreen(location);

        // Using location, the PopupWindow will be displayed right under anchorView
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY,
                location[0], location[1] + anchorView.getHeight());

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(context, context.getString(R.string.saving_reminder), Toast.LENGTH_SHORT).show();
        FragmentTransaction ft = this.fragmentManager.beginTransaction();
        //FragmentTransaction ft = parent.getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new ScheduleWeekViewFragment()).addToBackStack(null).commit();
    }

    @Override
    public boolean isEnabled(int position)
    {
        return true;
    }
}
