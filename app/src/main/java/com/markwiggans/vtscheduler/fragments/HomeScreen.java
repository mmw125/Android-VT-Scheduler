package com.markwiggans.vtscheduler.fragments;

import android.content.Context;

import com.markwiggans.vtscheduler.data.DataSource;
import com.markwiggans.vtscheduler.data.Schedule;

import java.util.List;

/**
 * Home Screen
 */
public class HomeScreen extends ScheduleFragment {
    public static final String HOME_SCREEN_FRAGMENT = "HOME_SCREEN_FRAGMENT";

    public HomeScreen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ScheduleCreator.
     */
    public static HomeScreen newInstance() {
        return new HomeScreen();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        DataSource.getSavedSchedules(getContext(), new DataSource.ScheduleReceiver() {
            @Override
            public void receiveSchedules(List<Schedule> schdules) {
                HomeScreen.this.onSchedulesGenerated(schdules);
            }
        });
    }
}
