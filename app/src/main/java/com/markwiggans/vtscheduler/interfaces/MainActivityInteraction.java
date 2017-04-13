package com.markwiggans.vtscheduler.interfaces;

import com.markwiggans.vtscheduler.data.Course;

import java.util.List;

/**
 * Created by Mark Wiggans on 3/25/2017.
 */
public interface MainActivityInteraction {
    public void changeFragment(String fragmentName, Object... params);
    public void generateSchedules(List<Course> courseList);
    public void showSlidingUpPanel(boolean show);
}
