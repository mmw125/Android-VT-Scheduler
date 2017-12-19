package com.markwiggans.vtscheduler.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.markwiggans.vtscheduler.R;
import com.markwiggans.vtscheduler.interfaces.MainActivityInteraction;

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
        crn = (EditText) view.findViewById(R.id.courseNumber);

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
        mListener.setSelected(getString(R.string.load_schedule));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if(v.equals(submit)) {
            InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            mListener.loadSchedule(crn.getText().toString());
        }
    }
}
