package com.markwiggans.vtscheduler.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import com.markwiggans.vtscheduler.R;
import com.markwiggans.vtscheduler.adapters.CourseAdapter;
import com.markwiggans.vtscheduler.data.Course;
import com.markwiggans.vtscheduler.database.CourseReaderContract;
import com.markwiggans.vtscheduler.database.DatabaseTask;
import com.markwiggans.vtscheduler.database.Query;
import com.markwiggans.vtscheduler.database.QueryResult;
import com.markwiggans.vtscheduler.interfaces.MainActivityInteraction;

import java.util.List;


/**
 * Fragment for Quering courses
 */
public class CourseQuery extends Fragment implements View.OnClickListener, DatabaseTask.DatabaseTaskReceiver{
    private MainActivityInteraction mListener;
    private Button submit;
    private EditText crn;
    private LinearLayout layout;
    private Context context;
    private LinearLayout linlaHeaderProgress;

    public CourseQuery() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ScheduleCreator.
     */
    public static CourseQuery newInstance() {
        return new CourseQuery();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_query, container, false);
        crn = (EditText) view.findViewById(R.id.editText);
        submit = (Button) view.findViewById(R.id.submit);
        submit.setOnClickListener(this);
        layout = (LinearLayout) view.findViewById(R.id.linear_layout);
        linlaHeaderProgress = (LinearLayout) view.findViewById(R.id.linlaHeaderProgress);
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
    }

    @Override
    public void onClick(View v) {
        if(v.equals(submit)) {
            linlaHeaderProgress.setVisibility(View.VISIBLE);
            Query q = new Query(CourseReaderContract.CourseEntry.TABLE_NAME,
                    CourseReaderContract.CourseEntry.COLUMN_NAME_COURSE_NUMBER + " = " + crn.getText().toString(), null);
            new DatabaseTask(context).execute(q);
        }
    }

    @Override
    public void onDatabaseTask(List<QueryResult> cursor) {
        List<Course> courses = Course.createCourses(cursor.get(0).getCursor());
        ListAdapter adapter = new CourseAdapter(context, courses);
    }
}
