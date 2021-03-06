package com.markwiggans.vtscheduler.data;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.alamkanak.weekview.WeekViewEvent;
import com.markwiggans.vtscheduler.MainActivity;
import com.markwiggans.vtscheduler.R;
import com.markwiggans.vtscheduler.database.CourseReaderContract;
import com.markwiggans.vtscheduler.interfaces.OnEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Mark Wiggans on 4/10/2017.
 * Represents a schedule in memory
 */
public class Schedule implements Comparable<Schedule>{
    @Override
    public int compareTo(@NonNull Schedule o) {
        return this.getScore() - o.getScore();
    }

    public static ArrayList<Schedule> createSchedulesFromDatabase(Context context, Cursor schedules, Cursor scheduleItems) {
        HashMap<String, ArrayList<ScheduleItem>> map = new HashMap<>();
        ArrayList<Schedule> outList = new ArrayList<>();

        if (scheduleItems.moveToFirst()) {
            do {
                ScheduleItem crn = new ScheduleItem(scheduleItems);
                if(!map.containsKey(crn.getScheduleID())) {
                    map.put(crn.getScheduleID(), new ArrayList<ScheduleItem>());
                }
                map.get(crn.getScheduleID()).add(crn);
            } while (scheduleItems.moveToNext());
        }

        if (schedules.moveToFirst()) {
            do {
                Schedule schedule = new Schedule(schedules);
                outList.add(schedule);
                for(ScheduleItem item : map.get(schedule.uuid)) {
                    schedule.crns.add(DataSource.getCRN(context, item.crn, item.crnSemester));
                }
                schedule.getScore(context);
            } while (schedules.moveToNext());
        }
        return outList;
    }

    public static void createScheduleFromServerResponse(final Context context, final OnEventListener<Schedule> receiver, final String semester, final int[] crns) {
        new AsyncTask<Object, Object, List<CRN>>(){
            @Override
            protected List<CRN> doInBackground(Object... params) {
                return DataSource.getCRNs(context, semester, crns);
            }

            @Override
            protected void onPostExecute(List<CRN> crns) {
                super.onPostExecute(crns);
                receiver.onSuccess(new Schedule(crns));
            }
        }.execute();
    }

    private static ArrayList<Schedule> schedules = new ArrayList<>();
    public static List<Schedule> getSchedulesByIndex(List<Integer> index) {
        List<Schedule> outList = new ArrayList<>();
        for(int i : index) {
            outList.add(getScheduleByIndex(i));
        }
        return outList;
    }

    public static Schedule getScheduleByIndex(int index) {
        if(index >= 0 && index < schedules.size()) {
            return schedules.get(index);
        }
        return null;
    }

    public static ArrayList<Integer> getSchedulesIds(List<Schedule> schedules) {
        ArrayList<Integer> integers = new ArrayList<>();
        if(schedules != null) {
            for(Schedule schedule : schedules) {
                integers.add(schedule.index);
            }
        }
        return integers;
    }

    private int index;
    private List<CRN> crns;
    private boolean inDatabase = false;

    public Schedule(List<CRN> crns) {
        this.crns = crns;
        this.index = schedules.size();
        schedules.add(this);
    }

    private int id;
    private String uuid;
    public Schedule(Cursor c) {
        this.id = c.getInt(0);
        this.uuid = c.getString(1);
        crns = new ArrayList<>();
        this.index = schedules.size();
        schedules.add(this);
        inDatabase = true;
    }

    public List<CRN> getCrns() {
        return crns;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(CRN crn : crns) {
            builder.append(crn.toString());
            builder.append(", ");
        }
        builder.delete(builder.length() - 2, builder.length());
        return builder.toString();
    }

    public int getIndex() {
        return index;
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("semester", crns.size() > 0 ? crns.get(0).getSemester() : "none");
            obj.put("crns", crns);
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    private int score = -1;

    public int getScore() {
        return getScore(null);
    }

    public int getScore(Context context) {
        if(score != -1) {
            return score;
        }
        score = 0;
        ArrayList<MeetingTime> meetingTimes = new ArrayList<>();
        for(CRN crn : getCrns()) {
            meetingTimes.addAll(crn.getMeetingTimes(context));
        }
        if(meetingTimes.size() == 0) {
            return 0;
        }
        Collections.sort(meetingTimes);
        MeetingTime start = meetingTimes.get(0);
        MeetingTime end = meetingTimes.get(0);
        for(int i = 1; i < meetingTimes.size(); i++) {
            if(meetingTimes.get(i).getDay().equals(start.getDay())) {
                end = meetingTimes.get(i);
            } else {
                score += end.getEndTime() - start.getStartTime() + 100;
                start = meetingTimes.get(i);
                end = meetingTimes.get(i);
            }
        }
        score += end.getEndTime() - start.getStartTime();
        score /= 10;
        return score;
    }

    public ArrayList<WeekViewEvent> toCalendars(Context context, int month, int year) {
        ArrayList<WeekViewEvent> events = new ArrayList<>();
        for(CRN crn : getCrns()) {
            for(MeetingTime m : crn.getMeetingTimes(context)) {
                Calendar startTime = Calendar.getInstance();
                startTime.set(Calendar.HOUR_OF_DAY, m.getStartTime() / 60);
                startTime.set(Calendar.MINUTE, m.getStartTime() % 60);
                startTime.set(Calendar.MONTH, month-1);
                startTime.set(Calendar.YEAR, year);
                Calendar endTime = (Calendar) startTime.clone();
                endTime.add(Calendar.HOUR, m.getEndTime() / 60);
                endTime.set(Calendar.MINUTE, m.getEndTime() % 60);
                WeekViewEvent event = new WeekViewEvent(1, crn.getCourse().getWholeName(), startTime, endTime);
                event.setColor(context.getColor(R.color.event_color_01));
                events.add(event);
            }
        }

        return events;
    }

    public boolean isInDatabase() {
        return inDatabase;
    }

    public String getUUID() {
        StringBuilder builder = new StringBuilder();
        for (CRN crn : getCrns()) {
            if (builder.length() == 0) {
                builder.append(crn.getSemester());
            }
            builder.append("|");
            builder.append(crn.getCRN());
        }
        return builder.toString();
    }

    public static void createFromUUID(Context context, final OnEventListener<Schedule> listener, String uuid) {
        String[] split = uuid.split("\\|");
        int[] crns = new int[split.length - 1];
        for (int i = 1; i < split.length; i++) {
            Log.d(MainActivity.LOG_STRING, split[i]);
            crns[i - 1] = Integer.parseInt(split[i]);
        }
        DataSource.getCRNs(context, split[0], crns, new OnEventListener<List<CRN>>() {
            @Override
            public void onSuccess(List<CRN> crnList) {
                listener.onSuccess(new Schedule(crnList));
            }
        });
    }

    public static class ScheduleItem {
        private int crn;
        private String crnSemester, scheduleID;
        public ScheduleItem(Cursor c) {
            crn = c.getInt(c.getColumnIndex(CourseReaderContract.ScheduleItemEntry.COLUMN_NAME_CRN_CRN));
            crnSemester = c.getString(c.getColumnIndex(CourseReaderContract.ScheduleItemEntry.COLUMN_NAME_CRN_SEMESTER));
            scheduleID = c.getString(c.getColumnIndex(CourseReaderContract.ScheduleItemEntry.COLUMN_NAME_SCHEDULE_ID));
        }

        public String getScheduleID() {
            return scheduleID;
        }
    }
}
