package com.markwiggans.vtscheduler.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alamkanak.weekview.WeekView;
import com.markwiggans.vtscheduler.R;
import com.markwiggans.vtscheduler.data.Schedule;

import java.util.ArrayList;

/**
 * Created by anirudha on 4/30/2017.
 */

public class ScheduleViewerAdapter extends RecyclerView.Adapter<ScheduleViewerAdapter.CustomViewHolder>{

    private ArrayList<Schedule> schedulesList;
    private Context context;

    public ScheduleViewerAdapter(Context context,ArrayList<Schedule> schedules){
        this.schedulesList = schedules;
        this.context = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_card_view, null);
        CustomViewHolder viewHolder = new CustomViewHolder(v);

        return null;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder{

        protected WeekView weekView;
        public CustomViewHolder(View view){
            super(view);
            this.weekView = (WeekView)view.findViewById(R.id.weekView);
        }
    }


}
