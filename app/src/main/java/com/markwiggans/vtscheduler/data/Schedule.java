package com.markwiggans.vtscheduler.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark Wiggans on 4/10/2017.
 */
public class Schedule {
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
}
