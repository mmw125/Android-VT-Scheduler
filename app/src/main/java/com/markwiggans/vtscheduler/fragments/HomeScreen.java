package com.markwiggans.vtscheduler.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.markwiggans.vtscheduler.R;
import com.markwiggans.vtscheduler.interfaces.MainActivityInteraction;

/**
 * Home Screen
 */
public class HomeScreen extends Fragment implements View.OnClickListener {
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

        fab.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.setSelected(getString(R.string.home));
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
        mListener.changeFragment(ScheduleCreator.SCHEDULE_CREATOR_FRAGMENT);
    }
}
