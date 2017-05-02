package com.markwiggans.vtscheduler.data;

/**
 * Created by Anirudha Simha on 5/2/2017.
 */

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Helper class to simply be used to merge meeting times fof crnsinto easy to manage form
 */
public class DateTimeCRN /*implements Comparable<DateTimeCRN>*/{

    private int CRN;
    private int startTime;
    private int endTime;
    public DateTimeCRN(int CRN, int startTime, int endTime ){
        this.CRN = CRN;
        this.startTime = startTime;
        this.endTime = endTime;
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


}
