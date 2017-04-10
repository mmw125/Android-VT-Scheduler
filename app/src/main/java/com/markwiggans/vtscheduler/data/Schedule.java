package com.markwiggans.vtscheduler.data;

import java.util.List;

/**
 * Created by Mark Wiggans on 4/10/2017.
 */
public class Schedule {
    private List<CRN> crns;
    public Schedule(List<CRN> crns) {
        this.crns = crns;
    }

    public List<CRN> getCrns() {
        return crns;
    }
}
