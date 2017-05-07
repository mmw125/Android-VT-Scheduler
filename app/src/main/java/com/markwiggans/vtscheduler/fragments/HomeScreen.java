package com.markwiggans.vtscheduler.fragments;

import android.content.Context;

import com.markwiggans.vtscheduler.R;
import com.markwiggans.vtscheduler.data.DataSource;
import com.markwiggans.vtscheduler.data.Schedule;

import java.util.Collections;
import java.util.List;

/**
 * Home Screen
 *
 * Displays a list of recently saved schedules
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
        setErrorMessage(context.getString(R.string.no_saved_schedules_message));
    }

    @Override
    public void onResume() {
        super.onResume();
        DataSource.getSavedSchedules(getContext(), new DataSource.ScheduleReceiver() {
            @Override
            public void receiveSchedules(List<Schedule> schdules) {
                HomeScreen.this.onSchedulesGenerated(schdules);
            }
        });
    }

    @Override
    protected void sortSchedules(List<Schedule> schedules) {
        Collections.reverse(schedules);
    }

    @Override
    protected void updatePanelUpToolbar(String title, boolean loading) {
        // We don't want to update the panel toolbar
    }
}
