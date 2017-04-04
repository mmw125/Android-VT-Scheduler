package com.markwiggans.vtscheduler.data;

import android.database.Cursor;
import android.util.SparseArray;

import java.util.List;

/**
 * Created by Mark Wiggans on 3/19/2017.
 * Represents the data of a MeetingTimeList in the database
 */
public class MeetingTimeList {
    private int id;
    private int courseId;
    public MeetingTimeList(Cursor c) {
        this.id = c.getInt(0);
        this.courseId = c.getInt(1);
    }

    public List<CRN> getCRNs() {
        return null;
    }
}
