package com.markwiggans.vtscheduler.data;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.markwiggans.vtscheduler.database.DataSource;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Mark Wiggans on 4/10/2017.
 */
public class Schedule implements Comparable<Schedule>{
    @Override
    public int compareTo(@NonNull Schedule o) {
        return this.getScore() - o.getScore();
    }

    interface ScheduleReceiver {
        void receiveSchedule(Schedule schedule);
    }

    public static void createScheduleFromServerResponse(final Context context, final ScheduleReceiver receiver, final String semester, final int[] crns) {
        new AsyncTask<Object, Object, List<CRN>>(){
            @Override
            protected List<CRN> doInBackground(Object... params) {
                return DataSource.getInstance(context).getCRNs(semester, crns);
            }

            @Override
            protected void onPostExecute(List<CRN> crns) {
                super.onPostExecute(crns);
                receiver.receiveSchedule(new Schedule(crns));
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
        for(Schedule schedule : schedules) {
            integers.add(schedule.index);
        }
        return integers;
    }

    private int index;
    private List<CRN> crns;
    public Schedule(List<CRN> crns) {
        this.crns = crns;
        this.index = schedules.size();
        schedules.add(this);
    }

    public List<CRN> getCrns() {
        return crns;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(CRN crn : crns) {
            builder.append(crn.toString() + ", ");
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
        if(score != -1) {
            return score;
        }
        score = 0;
        ArrayList<MeetingTime> meetingTimes = new ArrayList<>();
        for(CRN crn : getCrns()) {
            meetingTimes.addAll(crn.getMeetingTimes());
        }
        Collections.sort(meetingTimes);
        MeetingTime start = meetingTimes.get(0);
        MeetingTime end = meetingTimes.get(0);
        for(int i = 1; i < meetingTimes.size(); i++) {
            if(meetingTimes.get(i).getDay().equals(start.getDay())) {
                end = meetingTimes.get(i);
            } else {
                score += end.getEndTime() - start.getStartTime();
                start = meetingTimes.get(i);
                end = meetingTimes.get(i);
            }
        }
        score += end.getEndTime() - start.getStartTime();
        return score;
    }
}
