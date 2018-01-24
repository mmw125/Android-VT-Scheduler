package com.markwiggans.vtscheduler.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.markwiggans.vtscheduler.R;
import com.markwiggans.vtscheduler.data.CRN;

/**
 * Created by Mark Wiggans on 3/27/2017.
 * Adapter for viewing crns
 */
public class CRNDisplay extends Fragment {
    private static final String ARG_PARAM1 = "param1";

    private int crnNumber;
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
    public static CRNDisplay newInstance(int crnNumber) {
        CRNDisplay fragment = new CRNDisplay();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, crnNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.crnNumber = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.crn_display, container, false);
        crnView = (ExpandableListView) view.findViewById(R.id.crn_expandable_list);
        crnView.setAdapter();
        return view;
    }

    public @NonNull
    View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.crn_display, parent, false);
        }
        CRN item = getItem(position);
        TextView crn_number = (TextView) convertView.findViewById(R.id.parent);
        crn_number.setText(item.getCRN() + "");
        TextView instructor = (TextView) convertView.findViewById(R.id.child);
        instructor.setText(item.getInstructor());
        return convertView;
    }
}
