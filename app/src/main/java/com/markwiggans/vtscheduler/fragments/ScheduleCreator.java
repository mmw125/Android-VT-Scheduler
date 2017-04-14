package com.markwiggans.vtscheduler.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.markwiggans.vtscheduler.MainActivity;
import com.markwiggans.vtscheduler.R;
import com.markwiggans.vtscheduler.adapters.CourseAdapter;
import com.markwiggans.vtscheduler.data.Course;
import com.markwiggans.vtscheduler.data.Semester;
import com.markwiggans.vtscheduler.database.DataSource;
import com.markwiggans.vtscheduler.interfaces.MainActivityInteraction;
import com.markwiggans.vtscheduler.views.CourseCompletionView;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment for creating schedules
 * https://github.com/Plumillon/ChipView
 * MIGHT BE BETTER >>> https://github.com/splitwise/TokenAutoComplete <<<
 */
public class ScheduleCreator extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    public static final String SCHEDULE_CREATOR_FRAGMENT = "SCHEDULE_CREATOR_FRAGMENT";
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
        Log.d(MainActivity.LOG_STRING, "Schedule Creator OnCreateView");
        View view = inflater.inflate(R.layout.fragment_schedule_creator, container, false);
        submit = (Button) view.findViewById(R.id.submit);
        semesterSelector = (Spinner) view.findViewById(R.id.semester_selector);
        semesterSelector.setOnItemSelectedListener(this);
        semesterSelector.setAdapter(new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_dropdown_item, new ArrayList<Semester>()));
        courseInput = (CourseCompletionView) view.findViewById(R.id.course_input);
        DataSource.getInstance(context).getSemesters(context, new DataSource.SemesterReceiver() {
            @Override
            public void receiveSemesters(List<Semester> semesters) {
                ArrayAdapter<Semester> semesterAdapter = (ArrayAdapter) semesterSelector.getAdapter();
                semesterAdapter.addAll(semesters);
                semesterAdapter.notifyDataSetChanged();
                updateCourseAutocomplete((Semester) semesterSelector.getSelectedItem());
            }
        });
        submit.setOnClickListener(this);
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
        if(v.equals(submit)) {
            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            mListener.showSlidingUpPanel(true);
            mListener.generateSchedules(new ArrayList<>(courseInput.getObjects()));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent != null && parent.equals(semesterSelector)) {
            if(courseInput.isEnabled()) {
                updateCourseAutocomplete((Semester) semesterSelector.getSelectedItem());
            }
        }
    }

    public void removeAllCourses() {
        courseInput.setText("");
        for(Course c : courseInput.getObjects()) {
            Log.d(MainActivity.LOG_STRING, "Removing " + c.toString());
            courseInput.removeObject(c);
        }
    }

    public void updateCourseAutocomplete(Semester selectedSemester) {
        removeAllCourses();
        courseInput.setEnabled(false);
        DataSource.getInstance(context).getCourses(context, new DataSource.CoursesReceiver() {
            @Override
            public void receiveCourses(List<Course> courses) {
                CourseAdapter adapter = (CourseAdapter) courseInput.getAdapter();
                if(adapter == null) {
                    adapter = new CourseAdapter(context, 0, courses, false);
                    courseInput.setAdapter(adapter);
                } else {
                    adapter.clear();
                    adapter.addAll(courses);
                }
                courseInput.setEnabled(true);
            }
        }, selectedSemester);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        removeAllCourses();
        courseInput.setEnabled(false);
    }
}
