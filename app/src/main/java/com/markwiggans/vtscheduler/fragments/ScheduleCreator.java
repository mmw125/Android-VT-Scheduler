package com.markwiggans.vtscheduler.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.markwiggans.vtscheduler.R;
import com.markwiggans.vtscheduler.adapters.CourseAdapter;
import com.markwiggans.vtscheduler.data.Course;
import com.markwiggans.vtscheduler.data.Semester;
import com.markwiggans.vtscheduler.database.DataSource;
import com.markwiggans.vtscheduler.interfaces.MainActivityInteraction;
import com.markwiggans.vtscheduler.views.CourseCompletionView;

import java.util.List;

/**
 * Fragment for creating schedules
 * https://github.com/Plumillon/ChipView
 * MIGHT BE BETTER >>> https://github.com/splitwise/TokenAutoComplete <<<
 */
public class ScheduleCreator extends Fragment implements View.OnClickListener{
    private MainActivityInteraction mListener;
    private Context context;
    private Button submit;
    private CourseCompletionView courseInput;
    private Spinner semesterSelector;

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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_creator, container, false);
        submit = (Button) view.findViewById(R.id.submit);
        semesterSelector = (Spinner) view.findViewById(R.id.semester_selector);
        semesterSelector.setOnClickListener(this);
        DataSource.getInstance(context).getSemesters(context, new DataSource.SemesterReceiver() {
            @Override
            public void receiveSemesters(List<Semester> courseNames) {

            }
        });
        submit.setOnClickListener(this);
        courseInput = (CourseCompletionView) view.findViewById(R.id.course_input);
        DataSource.getInstance(context).getCourses(context, new DataSource.CoursesReceiver() {
            @Override
            public void receiveCourses(List<Course> courses) {
                CourseAdapter adapter = new CourseAdapter(context, 0, courses, false);
                courseInput.setAdapter(adapter);
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
        if(v.equals(semesterSelector)) {
            semesterSelector.getSelectedItem();
        }
    }
}
