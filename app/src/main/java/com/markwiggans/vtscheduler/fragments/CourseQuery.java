package com.markwiggans.vtscheduler.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.markwiggans.vtscheduler.R;
import com.markwiggans.vtscheduler.adapters.CourseAdapter;
import com.markwiggans.vtscheduler.data.Course;
import com.markwiggans.vtscheduler.database.CourseReaderContract;
import com.markwiggans.vtscheduler.database.DatabaseWrapper;
import com.markwiggans.vtscheduler.database.Query;
import com.markwiggans.vtscheduler.database.QueryResult;
import com.markwiggans.vtscheduler.interfaces.MainActivityInteraction;

import java.util.List;


/**
 * Fragment for Quering courses
 */
public class CourseQuery extends Fragment implements DatabaseWrapper.DatabaseTaskReceiver, TextWatcher {
    public static final String COURSE_QUERY_FRAGMENT = "QUERY_FRAGMENT";
    private MainActivityInteraction mListener;
    private EditText crn, department;
    private LinearLayout layout;
    private Context context;
    private LinearLayout linlaHeaderProgress;
    private View view;

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
        view = inflater.inflate(R.layout.fragment_course_query, container, false);
        crn = (EditText) view.findViewById(R.id.id_input);
        crn.addTextChangedListener(this);
        department = (EditText) view.findViewById(R.id.department);
        department.addTextChangedListener(this);
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
    public void onDatabaseTask(List<QueryResult> cursor) {
        linlaHeaderProgress.setVisibility(View.GONE);
        CourseAdapter adapter = new CourseAdapter(context, R.id.panel_up_list,
                Course.createCourses(cursor.get(0).getCursor()));
        ListView memberList = (ListView) view.findViewById(R.id.panel_up_list);
        memberList.setAdapter(adapter);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        updateResults();
    }

    protected void updateResults() {
        linlaHeaderProgress.setVisibility(View.VISIBLE);
        String selectString = CourseReaderContract.CourseEntry.COLUMN_NAME_COURSE_NUMBER + " LIKE '%" + crn.getText().toString() + "%'" +
                " AND " + CourseReaderContract.CourseEntry.COLUMN_NAME_DEPARTMENT_ID + " LIKE '%" + department.getText().toString() + "%'";
        String groupBy = CourseReaderContract.CourseEntry.COLUMN_NAME_WHOLE_NAME + ", " + CourseReaderContract.CourseEntry.COLUMN_NAME_TYPE;
        Query q = new Query(CourseReaderContract.CourseEntry.TABLE_NAME, selectString, groupBy);
        DatabaseWrapper.getInstance(getContext()).query(this, q);
    }
}
