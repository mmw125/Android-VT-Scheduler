package com.markwiggans.vtscheduler.interfaces;

import com.markwiggans.vtscheduler.data.Course;
import com.markwiggans.vtscheduler.database.DataSource;
import com.markwiggans.vtscheduler.database.Query;

import java.util.List;

/**
 * Created by Mark Wiggans on 3/25/2017.
 */
public interface MainActivityInteraction {
    public void changeFragment(String fragmentName, String... params);
    public void generateSchedules(List<Course> courseList);
}
