package com.markwiggans.vtscheduler.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.markwiggans.vtscheduler.R;
import com.markwiggans.vtscheduler.data.CRN;
import com.markwiggans.vtscheduler.data.Course;

import java.util.List;

/**
 * Created by Mark Wiggans on 3/27/2017.
 */
public class CRNAdapter extends ArrayAdapter<CRN> {
    public CRNAdapter(@NonNull Context context, List<CRN> courses) {
        super(context, 0, courses);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_crn, parent, false);
        }
        CRN item = getItem(position);
        TextView crn_number = (TextView) convertView.findViewById(R.id.crn_number);
        crn_number.setText(item.getCrn() + "");
        TextView instructor = (TextView) convertView.findViewById(R.id.instructor);
        instructor.setText(item.getInstructor());
        return convertView;
    }
}
