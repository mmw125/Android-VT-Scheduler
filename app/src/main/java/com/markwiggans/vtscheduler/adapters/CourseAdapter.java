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
 * Adapter for viewing courses
 */
public class CourseAdapter extends ArrayAdapter<Course> {
    private boolean nameFirst;
    public CourseAdapter(@NonNull Context context, int id, List<Course> courses) {
        this(context, id, courses, true);
    }

    public CourseAdapter(@NonNull Context context, int id, List<Course> courses, boolean nameFirst) {
        super(context, id, courses);
        this.nameFirst = nameFirst;
    }

    public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_course, parent, false);
        }
        Course course = getItem(position);
        String first, second;
        if(nameFirst) {
            first = course.getName();
            second = course.getWholeName();
        } else {
            first = course.getCourseNameWithDash();
            second = course.getName();
        }

        TextView courseName = (TextView) convertView.findViewById(R.id.parent);
        courseName.setText(first);
        TextView courseType = (TextView) convertView.findViewById(R.id.child);
        courseType.setText(second);
        return convertView;
    }
}
