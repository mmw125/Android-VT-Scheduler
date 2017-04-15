package com.markwiggans.vtscheduler.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.markwiggans.vtscheduler.R;
import com.markwiggans.vtscheduler.adapters.ScheduleAdapter;
import com.markwiggans.vtscheduler.data.Schedule;
import com.markwiggans.vtscheduler.interfaces.MainActivityInteraction;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link MainActivityInteraction}
 * interface.
 */
public class ScheduleFragment extends Fragment {
    private static final String SCHEDULES_INDEXES = "schedules_indexes";
    private MainActivityInteraction mListener;
    private ListView scheduleList;
    private Context context;
    private ArrayList<Integer> courseIndices;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ScheduleFragment() {
    }

    public static ScheduleFragment newInstance(ArrayList<Integer> scheduleIndexes) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putIntegerArrayList(SCHEDULES_INDEXES, scheduleIndexes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null) {
            courseIndices = getArguments().getIntegerArrayList(SCHEDULES_INDEXES);
        } else {
            courseIndices = savedInstanceState.getIntegerArrayList(SCHEDULES_INDEXES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_list, container, false);
        scheduleList = (ListView) view.findViewById(R.id.list);
        scheduleList.setAdapter(new ScheduleAdapter(context, R.id.list, Schedule.getSchedulesByIndex(courseIndices)));
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivityInteraction) {
            mListener = (MainActivityInteraction) context;
            this.context = context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        savedState.putIntegerArrayList(SCHEDULES_INDEXES, courseIndices);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
