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

import java.util.List;

/**
 * Created by Mark Wiggans on 3/27/2017.
 * Adapter for viewing crns
 */
public class CRNAdapter extends ArrayAdapter<CRN> {
    public CRNAdapter(@NonNull Context context, int id, List<CRN> courses) {
        super(context, id, courses);
    }

    public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_crn, parent, false);
        }
        CRN item = getItem(position);
        TextView crn_number = (TextView) convertView.findViewById(R.id.parent);
        crn_number.setText(item.getCRN() + "");
        TextView instructor = (TextView) convertView.findViewById(R.id.child);
        instructor.setText(item.getInstructor());
        return convertView;
    }
}
