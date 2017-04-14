package com.markwiggans.vtscheduler.interfaces;

import com.markwiggans.vtscheduler.data.Course;
import com.markwiggans.vtscheduler.data.Schedule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark Wiggans on 3/25/2017.
 */
public interface MainActivityInteraction {
    public void changeFragment(String fragmentName, Object... params);
    public void generateSchedules(ArrayList<Course> courseList);
    public void showSlidingUpPanel(boolean show);
    public List<Schedule> getSchedules();
}
