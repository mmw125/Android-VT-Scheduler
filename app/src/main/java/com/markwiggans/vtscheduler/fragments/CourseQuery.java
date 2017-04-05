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
import android.widget.Toast;

import com.markwiggans.vtscheduler.R;
import com.markwiggans.vtscheduler.adapters.CourseAdapter;
import com.markwiggans.vtscheduler.data.Course;
import com.markwiggans.vtscheduler.database.CourseReaderContract;
import com.markwiggans.vtscheduler.database.DataSource;
import com.markwiggans.vtscheduler.database.DatabaseTask;
import com.markwiggans.vtscheduler.database.Query;
import com.markwiggans.vtscheduler.database.QueryResult;
import com.markwiggans.vtscheduler.interfaces.MainActivityInteraction;

import java.util.Comparator;
import java.util.List;


/**
 * Fragment for Quering courses
 */
public class CourseQuery extends Fragment implements View.OnClickListener, DatabaseTask.DatabaseTaskReceiver, DataSource.DepartmentReceiver{
    private MainActivityInteraction mListener;
    private Button submit;
    private EditText crn;
    private LinearLayout layout;
    private AutoCompleteTextView department;
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
        crn = (EditText) view.findViewById(R.id.course_input);
        department = (AutoCompleteTextView) view.findViewById(R.id.department);
        DataSource.getInstance(context).getDepartments(context, this);
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
            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            submit.setEnabled(false);
            linlaHeaderProgress.setVisibility(View.VISIBLE);
            String selectString = CourseReaderContract.CourseEntry.COLUMN_NAME_COURSE_NUMBER + " LIKE '%" + crn.getText().toString() + "%'" +
                    " AND " + CourseReaderContract.CourseEntry.COLUMN_NAME_DEPARTMENT_ID + " LIKE '%" + department.getText().toString() + "%'";
            Query q = new Query(CourseReaderContract.CourseEntry.TABLE_NAME, selectString, null);
            new DatabaseTask(this, context).execute(q);
        }
    }

    @Override
    public void onDatabaseTask(List<QueryResult> cursor) {
        submit.setEnabled(true);
        Toast.makeText(context, "Got task back", Toast.LENGTH_SHORT).show();
        linlaHeaderProgress.setVisibility(View.GONE);
        CourseAdapter adapter = new CourseAdapter(context, R.id.list,
                Course.createCourses(cursor.get(0).getCursor()));
        ListView memberList = (ListView) view.findViewById(R.id.list);
        memberList.setAdapter(adapter);
    }

    @Override
    public void receiveDepartments(List<String> departments) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, departments);
        adapter.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.toLowerCase().compareTo(o2.toLowerCase());
            }
        });
        department.setAdapter(adapter);
    }
}
