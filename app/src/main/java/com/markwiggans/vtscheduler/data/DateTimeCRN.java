package com.markwiggans.vtscheduler.data;

/**
 * Created by Anirudha Simha on 5/2/2017.
 */

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Helper class to simply be used to merge meeting times of crns into easy to manage form
 */
public class DateTimeCRN implements Comparable<DateTimeCRN>{

    private int CRN;
    private int startTime;
    private int endTime;
    private CRN crn;

    public DateTimeCRN(int CRN, int startTime, int endTime, CRN crn ){
        this.CRN = CRN;
        this.startTime = startTime;
        this.endTime = endTime;
        this.crn = crn;
    }
    public CRN getCRNObject(){
        return this.crn;
    }

    public int getCRN(){
        return this.CRN;
    }

    public int getStartTime(){
        return this.startTime;
    }

    public int getEndTime(){
        return this.endTime;
    }

    @Override
    public int compareTo(@NonNull DateTimeCRN o) {
        return 0;
    }
}
