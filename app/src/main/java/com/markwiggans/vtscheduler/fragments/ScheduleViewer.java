package com.markwiggans.vtscheduler.fragments;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.markwiggans.vtscheduler.R;
import com.markwiggans.vtscheduler.adapters.ScheduleViewerAdapter;
import com.markwiggans.vtscheduler.data.Schedule;
import com.markwiggans.vtscheduler.interfaces.MainActivityInteraction;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainActivityInteraction} interface
 * to handle interaction events.
 * Use the {@link ScheduleViewer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleViewer extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    public static final String SAVED_SCHEDULES_FRAGMENT = "SAVED_SCHEDULES_FRAGMENT";

    // View Objects
    private RecyclerView mRecyclerView;
    private ScheduleViewerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Schedule> schedulesList;

    private MainActivityInteraction mListener;

    public ScheduleViewer() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ScheduleViewer.
     */
    // TODO: Rename and change types and number of parameters
    public static ScheduleViewer newInstance(/*String param1, String param2*/) {
        /*
        ScheduleViewer fragment = new ScheduleViewer();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
        */
        return new ScheduleViewer();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize ArrayList wth saved schedules
        setRetainInstance(true);
        if (savedInstanceState == null) {
            schedulesList = new ArrayList<>();
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule_viewer, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivityInteraction) {
            mListener = (MainActivityInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MainActivityInteraction");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {

    }
}
