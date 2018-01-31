package com.markwiggans.vtscheduler.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.markwiggans.vtscheduler.MainActivity;
import com.markwiggans.vtscheduler.R;
import com.markwiggans.vtscheduler.adapters.CRNListAdapter;
import com.markwiggans.vtscheduler.data.CRN;
import com.markwiggans.vtscheduler.data.Course;
import com.markwiggans.vtscheduler.data.DataSource;
import com.markwiggans.vtscheduler.interfaces.OnEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mark Wiggans on 3/27/2017.
 * Adapter for viewing crns
 */
public class CRNDisplay extends Fragment {
    private static final String ARG_PARAM1 = "param1";

    private Course course = null;
    private ExpandableListView crnView;

    public CRNDisplay() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ScheduleCreator.
     */
    public static CRNDisplay newInstance(Course course) {
        CRNDisplay fragment = new CRNDisplay();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, course);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.course = (Course) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.crn_display, container, false);
        crnView = (ExpandableListView) view.findViewById(R.id.crn_expandable_list);
        DataSource.getCRNs(getContext(), course, false, new OnEventListener<List<CRN>>() {
            @Override
            public void onSuccess(List<CRN> crns) {
                List<String> semesters = new ArrayList<>();
                HashMap<String, List<CRN>> map = new HashMap<>();
                for (CRN crn : crns) {
                    if (map.get(crn.getSemester()) == null) {
                        semesters.add(crn.getSemester());
                        map.put(crn.getSemester(), new ArrayList<CRN>());
                    }
                    crn.updateMeetingTimes(getContext());
                    map.get(crn.getSemester()).add(crn);
                }

                CRNListAdapter adapter = new CRNListAdapter(getContext(), semesters, map);
                crnView.setAdapter(adapter);
            }
        });
        return view;
    }
}
