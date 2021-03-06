package com.markwiggans.vtscheduler.interfaces;

import android.app.Fragment;

import com.markwiggans.vtscheduler.data.Course;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import java.util.ArrayList;

/**
 * Created by Mark Wiggans on 3/25/2017.
 * This is the interface that the fragments talk to the MainActivity through
 */
public interface MainActivityInteraction {

    /**
     * Changes the fragment
     * @param fragmentName the name of the fragment to create
     * @param params parameters to pass to the fragment instance
     */
    void changeFragment(String fragmentName, Object... params);
    void changeFragment(Fragment fragment);

    /**
     * Starts a generate schedules query
     * @param courseList the list of courses to generate the schedules with
     */
    void generateSchedules(ArrayList<Course> courseList);

    /**
     * Sets the properties of the toolbar on the SlidingUpPanelLayout
     * @param title the name to put of the bar
     * @param loading if a loading icon should be displayed
     */
    void setPanelUpToolbar(String title, boolean loading);

    /**
     * Lets the activity know when the fragment has been changed
     * @param title the name of the fragment
     */
    void setSelected(String title);

    void loadSchedule(String id);

    PanelState getSlidingUpPanelStatus();
}
