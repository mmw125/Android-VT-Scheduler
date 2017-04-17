package com.markwiggans.vtscheduler.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

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
 * Home Screen
 */
public class HomeScreen extends Fragment {
    public static final String HOME_SCREEN_FRAGMENT = "HOME_SCREEN_FRAGMENT";
    private MainActivityInteraction mListener;
    private Context context;
    private FloatingActionButton fab;
    private CardView recentQueries;
    private TextView noRecentQueriesText;

    public HomeScreen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ScheduleCreator.
     */
    public static HomeScreen newInstance() {
        return new HomeScreen();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_screen, container, false);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        recentQueries = (CardView) view.findViewById(R.id.recent_queries_list);
        noRecentQueriesText = (TextView) view.findViewById(R.id.no_recent_queries_text);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.changeFragment(ScheduleCreator.SCHEDULE_CREATOR_FRAGMENT);
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
}
