package com.markwiggans.vtscheduler.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.markwiggans.vtscheduler.R;
import com.markwiggans.vtscheduler.data.Schedule;
import com.markwiggans.vtscheduler.database.DataSource;
import com.markwiggans.vtscheduler.interfaces.MainActivityInteraction;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

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
        DataSource.getInstance(context).getSavedSchedules(getContext(), new DataSource.ScheduleReceiver() {
            @Override
            public void receiveSchedules(List<Schedule> schdules) {
                HomeScreen.this.onSchedulesGenerated(schdules);
            }
        });
    }
}
