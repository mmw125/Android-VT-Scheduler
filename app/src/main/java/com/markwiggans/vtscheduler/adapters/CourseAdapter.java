package com.markwiggans.vtscheduler.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.markwiggans.vtscheduler.R;
import com.markwiggans.vtscheduler.data.Course;

import java.util.List;

/**
 * Created by Mark Wiggans on 3/27/2017.
 */
public class CourseAdapter extends ArrayAdapter<Course> {
    public CourseAdapter(@NonNull Context context, int id, List<Course> courses) {
        super(context, id, courses);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_course, parent, false);
        }
        Course course = getItem(position);
        TextView courseName = (TextView) convertView.findViewById(R.id.course_name);
        courseName.setText(course.getName());
        TextView courseType = (TextView) convertView.findViewById(R.id.course_type);
        courseType.setText(course.getWholeName());
        return convertView;
    }
}
