package com.markwiggans.vtscheduler.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.markwiggans.vtscheduler.R;
import com.markwiggans.vtscheduler.data.Course;
import com.tokenautocomplete.TokenCompleteTextView;

/**
 * Created by Mark Wiggans on 4/5/2017.
 * View for Autocomplete on Course objects
 */
public class CourseCompletionView extends TokenCompleteTextView<Course> {
    public CourseCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View getViewForObject(Course course) {
        LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        TextView view = (TextView) l.inflate(R.layout.string_token, (ViewGroup) getParent(), false);
        view.setText(course.toString());
        return view;
    }

    @Override
    protected Course defaultObject(String completionText) {
        return null;
    }
}