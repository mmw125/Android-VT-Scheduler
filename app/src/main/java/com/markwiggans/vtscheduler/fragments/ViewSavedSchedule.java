package com.markwiggans.vtscheduler.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.markwiggans.vtscheduler.R;
import com.markwiggans.vtscheduler.adapters.CourseAdapter;
import com.markwiggans.vtscheduler.data.Course;
import com.markwiggans.vtscheduler.database.CourseReaderContract;
import com.markwiggans.vtscheduler.database.DataSource;
import com.markwiggans.vtscheduler.database.DatabaseTask;
import com.markwiggans.vtscheduler.database.Query;
import com.markwiggans.vtscheduler.database.QueryResult;
import com.markwiggans.vtscheduler.interfaces.MainActivityInteraction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * Fragment for Quering courses
 */
public class ViewSavedSchedule extends Fragment implements View.OnClickListener {
    public static final String SAVED_SCHEDULES_FRAGMENT = "VIEW_SAVED";
    private MainActivityInteraction mListener;
    private Button submit;
    private EditText crn;
    private View view;

    public ViewSavedSchedule() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ScheduleCreator.
     */
    public static ViewSavedSchedule newInstance() {
        return new ViewSavedSchedule();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_saved_schedule, container, false);
        crn = (EditText) view.findViewById(R.id.id_input);
        submit = (Button) view.findViewById(R.id.submit);
        submit.setOnClickListener(this);
        return view;
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
    public void onResume() {
        super.onResume();
        mListener.setSelected(getString(R.string.search_courses));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if(v.equals(submit)) {
            mListener.loadSchedule(crn.getText().toString());
        }
    }
}