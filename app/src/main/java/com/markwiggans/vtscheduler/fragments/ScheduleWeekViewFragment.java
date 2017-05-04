package com.markwiggans.vtscheduler.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.markwiggans.vtscheduler.R;
import com.markwiggans.vtscheduler.data.CRN;
import com.markwiggans.vtscheduler.data.MeetingTime;
import com.markwiggans.vtscheduler.data.Schedule;
import com.markwiggans.vtscheduler.interfaces.MainActivityInteraction;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import static com.markwiggans.vtscheduler.data.MeetingTime.Day.FRIDAY;
import static com.markwiggans.vtscheduler.data.MeetingTime.Day.SATURDAY;
import static com.markwiggans.vtscheduler.data.MeetingTime.Day.SUNDAY;
import static com.markwiggans.vtscheduler.data.MeetingTime.Day.THURSDAY;
import static com.markwiggans.vtscheduler.data.MeetingTime.Day.WEDNESDAY;
import static com.markwiggans.vtscheduler.data.MeetingTime.SHORT_HOUR_TIME;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ScheduleWeekViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleWeekViewFragment extends Fragment implements
        WeekView.EventClickListener,
        MonthLoader.MonthChangeListener,
        WeekView.EventLongPressListener,
        WeekView.EmptyViewLongPressListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM = "schedule_param";

    private Context context;

    private MainActivityInteraction mListener;

    private WeekView mWeekView;

    private Schedule mSchedule;

    public ScheduleWeekViewFragment() {
        // Required empty public constructor
    }

    public static ScheduleWeekViewFragment newInstance(Schedule schedule) {
        ScheduleWeekViewFragment fragment = new ScheduleWeekViewFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM, (Serializable)schedule);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mSchedule = (Schedule)getArguments().getSerializable(ARG_PARAM);


        View view = inflater.inflate(R.layout.fragment_schedule_week_view, container, false);
        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) view.findViewById(R.id.weekView);

        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, Calendar.MAY);
        c.set(Calendar.YEAR, 2017);
        c.set(Calendar.DAY_OF_MONTH, 1);
        mWeekView.goToDate(c);


        // Prevents scrolling horizontally
        mWeekView.setXScrollingSpeed(0);
        // sets first day as Monday
        mWeekView.setFirstDayOfWeek(Calendar.MONDAY);

        // sets visible days to 5
        mWeekView.setNumberOfVisibleDays(5);




        mWeekView.setShowNowLine(false);

        //mWeekView.setDateTimeInterpreter();

        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(this);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);

        // Set long press listener for empty view
        mWeekView.setEmptyViewLongPressListener(this);

        // Inflate the layout for this fragment
        return view;
    }


    /*MonthLoader.MonthChangeListener mMonthChangeListener = new MonthLoader.MonthChangeListener() {
        @Override
        public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
            // Populate the week view with some events.
            List<WeekViewEvent> events = getEvents(newYear, newMonth);
            return events;
        }
    };*/

    public void setSchedule(Schedule schedule) {
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        // Populate the week view with some events.
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, Calendar.MAY);
        c.set(Calendar.YEAR, 2017);
        c.set(Calendar.DAY_OF_MONTH, 1);
        mWeekView.goToDate(c);


        // sets first day as Monday
        mWeekView.setFirstDayOfWeek(Calendar.MONDAY);

        // sets visible days to 5
        mWeekView.setNumberOfVisibleDays(5);

        for(CRN crn : mSchedule.getCrns()){
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            for(MeetingTime t: crn.getMeetingTimes()){
                Calendar startTime = Calendar.getInstance();
                //startTime.set(Calendar.DAY_OF_WEEK, );
                try{
                    Date d = SHORT_HOUR_TIME.parse(t.getStartTimeString());
                    startTime.set(Calendar.HOUR, d.getHours());
                    startTime.set(Calendar.MINUTE, d.getMinutes());

                    switch(t.getDay()) {

                        case MONDAY: startTime.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                        case TUESDAY: startTime.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                        case WEDNESDAY:startTime.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                        case THURSDAY: startTime.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                        case FRIDAY: startTime.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                        case SATURDAY: startTime.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                        case SUNDAY: startTime.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                        default: startTime.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                    }

                } catch (ParseException e) {

                }


                startTime.set(Calendar.MINUTE, 0);
                startTime.set(Calendar.MONTH, newMonth-1);
                startTime.set(Calendar.YEAR, newYear);
                Calendar endTime = (Calendar) startTime.clone();
                try{
                    Date d = SHORT_HOUR_TIME.parse(t.getStartTimeString());
                    endTime.set(Calendar.HOUR, d.getHours());
                    endTime.set(Calendar.MINUTE, d.getMinutes());

                    switch(t.getDay()) {

                        case MONDAY: endTime.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                        case TUESDAY: endTime.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                        case WEDNESDAY:endTime.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                        case THURSDAY: endTime.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                        case FRIDAY: endTime.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                        case SATURDAY: endTime.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                        case SUNDAY: endTime.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                        default: endTime.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                    }
                } catch (ParseException e) {

                }
                WeekViewEvent event = new WeekViewEvent(1, crn.getCourseWholeName(), startTime, endTime);
                event.setColor(color);
                events.add(event);
            }
        }

        /*
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth-1);
        startTime.set(Calendar.YEAR, newYear);
        Calendar endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR, 1);
        endTime.set(Calendar.MONTH, newMonth-1);
        WeekViewEvent event = new WeekViewEvent(1, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_01));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 30);
        startTime.set(Calendar.MONTH, newMonth-1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.HOUR_OF_DAY, 4);
        endTime.set(Calendar.MINUTE, 30);
        endTime.set(Calendar.MONTH, newMonth-1);
        event = new WeekViewEvent(10, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_02));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 4);
        startTime.set(Calendar.MINUTE, 20);
        startTime.set(Calendar.MONTH, newMonth-1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.HOUR_OF_DAY, 5);
        endTime.set(Calendar.MINUTE, 0);
        event = new WeekViewEvent(10, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_03));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 5);
        startTime.set(Calendar.MINUTE, 30);
        startTime.set(Calendar.MONTH, newMonth-1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 2);
        endTime.set(Calendar.MONTH, newMonth-1);
        event = new WeekViewEvent(2, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_02));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 5);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth-1);
        startTime.set(Calendar.YEAR, newYear);
        startTime.add(Calendar.DATE, 1);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 3);
        endTime.set(Calendar.MONTH, newMonth - 1);
        event = new WeekViewEvent(3, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_03));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.DAY_OF_MONTH, 15);
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth-1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 3);
        event = new WeekViewEvent(4, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_04));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.DAY_OF_MONTH, 1);
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth-1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 3);
        event = new WeekViewEvent(5, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_01));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.DAY_OF_MONTH, startTime.getActualMaximum(Calendar.DAY_OF_MONTH));
        startTime.set(Calendar.HOUR_OF_DAY, 15);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth-1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 3);
        event = new WeekViewEvent(5, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_02));
        events.add(event);
        */

        return events;
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(context, "Clicked " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(context, "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {
        Toast.makeText(context, "Empty view long pressed: " + getEventTitle(time), Toast.LENGTH_SHORT).show();

    }

    public WeekView getWeekView() {
        return mWeekView;
    }

    protected String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
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
    }
}
