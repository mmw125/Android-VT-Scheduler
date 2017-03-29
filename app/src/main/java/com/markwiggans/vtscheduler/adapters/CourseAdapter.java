package com.markwiggans.vtscheduler.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.markwiggans.vtscheduler.R;
import com.markwiggans.vtscheduler.data.Course;

import java.util.List;

/**
 * Created by Mark Wiggans on 3/27/2017.
 */
public class CourseAdapter extends ArrayAdapter<Course> {
    public CourseAdapter(@NonNull Context context, List<Course> courses) {
        super(context, 0, courses);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_course, parent, false);
        }
        getItem(position);
        return convertView;
    }
}
