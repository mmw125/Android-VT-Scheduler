package com.markwiggans.vtscheduler.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.markwiggans.vtscheduler.R;
import com.markwiggans.vtscheduler.database.DataSource;
import com.markwiggans.vtscheduler.interfaces.MainActivityInteraction;

import java.util.Comparator;
import java.util.List;

/**
 * Fragment for creating schedules
 */
public class ScheduleCreator extends Fragment implements View.OnClickListener{
    private MainActivityInteraction mListener;
    private Context context;
    private Button submit;
    private LinearLayout layout;
    private MultiAutoCompleteTextView courseInput;
    private LinearLayout headerProgress;
    private View view;

    public ScheduleCreator() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ScheduleCreator.
     */
    public static ScheduleCreator newInstance() {
        return new ScheduleCreator();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_creator, container, false);
        submit = (Button) view.findViewById(R.id.submit);
        submit.setOnClickListener(this);
        layout = (LinearLayout) view.findViewById(R.id.linear_layout);
        headerProgress = (LinearLayout) view.findViewById(R.id.linlaHeaderProgress);
        courseInput = (MultiAutoCompleteTextView) view.findViewById(R.id.course_input);
        DataSource.getInstance(context).getCourseNames(context, new DataSource.CourseNameReceiver() {
            @Override
            public void receiveCourseNames(List<String> courseNames) {
                Log.d("ScheduleCreator", "got course names");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, courseNames);
                adapter.sort(new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o1.toLowerCase().compareTo(o2.toLowerCase());
                    }
                });
                courseInput.setAdapter(adapter);
                courseInput.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
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
        context = null;
    }

    @Override
    public void onClick(View v) {

    }
}
