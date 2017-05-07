package com.markwiggans.vtscheduler.fragments;

import android.content.Context;
import android.view.View;

import com.markwiggans.vtscheduler.R;
import com.markwiggans.vtscheduler.adapters.ScheduleAdapter;
import com.markwiggans.vtscheduler.data.DataSource;
import com.markwiggans.vtscheduler.data.Schedule;

import java.util.Collections;
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

    /**
     * Shows/hides/updates portions of the display based on if the values have loaded or not
     */
    @Override
    protected void refreshView() {
        loadingText.setVisibility(schedulesIndices != null ? View.GONE : View.VISIBLE);
        scheduleList.setVisibility(schedulesIndices != null ? View.VISIBLE : View.GONE);
        if(schedulesIndices != null) {
            List<Schedule> schedules = Schedule.getSchedulesByIndex(schedulesIndices);
            Collections.sort(schedules);
            if(schedules.size() > 0) {
                ScheduleAdapter adapter = new ScheduleAdapter(context, R.id.list, schedules);
                scheduleList.setAdapter(adapter);
                scheduleList.setOnItemLongClickListener(adapter);
            } else {
                loadingText.setVisibility(View.VISIBLE);
                loadingText.setText(errorMessage);
                scheduleList.setVisibility(View.GONE);
            }
        } else {
            mListener.setPanelUpToolbar(getString(R.string.loading), true);
        }
    }
}
