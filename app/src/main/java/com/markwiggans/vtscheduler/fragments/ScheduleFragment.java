package com.markwiggans.vtscheduler.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.markwiggans.vtscheduler.R;
import com.markwiggans.vtscheduler.adapters.ScheduleAdapter;
import com.markwiggans.vtscheduler.data.Schedule;
import com.markwiggans.vtscheduler.data.ScheduleGenerationTask;
import com.markwiggans.vtscheduler.interfaces.MainActivityInteraction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A fragment representing a list of Items.
 *
 * Activities containing this fragment MUST implement the {@link MainActivityInteraction}
 * interface.
 */
public class ScheduleFragment extends Fragment implements ScheduleGenerationTask.ScheduleGeneratorTaskReceiver{
    public static final String TAG_SCHEDULE_FRAGMENT = "SCHEDULE_FRAGMENT", SCHEDULES_INDEXES = "schedules_indexes", IS_LOADING = "IS_LOADING";
    public static final int MAX_SCHEDULE_LIMIT = 20;
    protected MainActivityInteraction mListener;
    protected ListView scheduleList;
    protected Context context;
    protected ArrayList<Integer> schedulesIndices;
    protected TextView loadingText;
    protected boolean loading;
    protected String errorMessage;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ScheduleFragment() {    }

    /**
     * Create a new instance of ScheduleFragment where it already has schedules
     * @param scheduleIndexes the indices of schedules to display
     * @return the new ScheduleFragment instance
     */
    public static ScheduleFragment newInstance(ArrayList<Integer> scheduleIndexes) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putIntegerArrayList(SCHEDULES_INDEXES, scheduleIndexes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        errorMessage = getString(R.string.default_error_message);
        if (context instanceof MainActivityInteraction) {
            mListener = (MainActivityInteraction) context;
            this.context = context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if(savedInstanceState == null) {
            schedulesIndices = getArguments() == null ? null : getArguments().getIntegerArrayList(SCHEDULES_INDEXES);
            loading = getArguments() != null && getArguments().getBoolean(IS_LOADING, false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_list, container, false);
        scheduleList = (ListView) view.findViewById(R.id.list);
        loadingText = (TextView) view.findViewById(R.id.loading_screen_text);

        if(loading || schedulesIndices != null) {
            refreshView();
        }
        return view;
    }

    /**
     * Shows/hides/updates portions of the display based on if the values have loaded or not
     */
    protected void refreshView() {
        loadingText.setVisibility(schedulesIndices != null ? View.GONE : View.VISIBLE);
        scheduleList.setVisibility(schedulesIndices != null ? View.VISIBLE : View.GONE);
        if(schedulesIndices != null) {
            List<Schedule> schedules = Schedule.getSchedulesByIndex(schedulesIndices);
            if(schedules.size() > 0) {
                sortSchedules(schedules);
                if(schedules.size() > MAX_SCHEDULE_LIMIT) {
                    schedules = schedules.subList(0, MAX_SCHEDULE_LIMIT);
                }
                ScheduleAdapter adapter = new ScheduleAdapter(context, R.id.list, schedules);
                scheduleList.setAdapter(adapter);
                scheduleList.setOnItemLongClickListener(adapter);
                updatePanelUpToolbar(getString(R.string.generated_schedules_label), false);
            } else {
                loadingText.setVisibility(View.VISIBLE);
                loadingText.setText(errorMessage);
                scheduleList.setVisibility(View.GONE);
                updatePanelUpToolbar(getString(R.string.error_label), false);
            }
        } else {
            mListener.setPanelUpToolbar(getString(R.string.loading), true);
        }
    }

    /**
     * Sorts the schedules in the default way
     * @param schedules a list of the schedules to sort
     */
    protected void sortSchedules(List<Schedule> schedules) {
        Collections.sort(schedules);
    }

    /**
     * Called when the panel up toolbar should be updated
     * @param title the new title for the panel up toolbar
     * @param loading if the loading icon should be displayed
     */
    protected void updatePanelUpToolbar(String title, boolean loading) {
        mListener.setPanelUpToolbar(title, loading);
    }

    /**
     * Tells the fragment to show its loading screen
     */
    public void startLoading() {
        loading = true;
        schedulesIndices = null;
        refreshView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        context = null;
    }

    @Override
    public void onSchedulesGenerated(List<Schedule> results) {
        loading = false;
        schedulesIndices = Schedule.getSchedulesIds(results);
        refreshView();
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        if(!loading && loadingText != null) {
            loadingText.setText(this.errorMessage);
        }
    }
}
