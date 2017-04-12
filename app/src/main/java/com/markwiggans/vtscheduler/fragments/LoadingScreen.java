package com.markwiggans.vtscheduler.fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.markwiggans.vtscheduler.R;
import com.markwiggans.vtscheduler.data.Course;
import com.markwiggans.vtscheduler.interfaces.MainActivityInteraction;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoadingScreen.MainActivityInteraction} interface
 * to handle interaction events.
 * Use the {@link LoadingScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoadingScreen extends Fragment {
    public static final String LOADING_SCREEN_FRAGMENT = "LOADING_SCREEN_FRAGMENT";
    private MainActivityInteraction mListener;

    public LoadingScreen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoadingScreen.
     */
    public static LoadingScreen newInstance() {
        return new LoadingScreen();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_loading_screen, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivityInteraction) {
            mListener = (MainActivityInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
