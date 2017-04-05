package com.markwiggans.vtscheduler.fragments;

import android.app.Fragment;
import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.markwiggans.vtscheduler.R;
import com.markwiggans.vtscheduler.data.CourseChip;
import com.markwiggans.vtscheduler.database.DataSource;
import com.markwiggans.vtscheduler.interfaces.MainActivityInteraction;
import com.plumillonforge.android.chipview.Chip;
import com.plumillonforge.android.chipview.ChipView;
import com.plumillonforge.android.chipview.ChipViewAdapter;

import java.util.Comparator;
import java.util.List;

/**
 * Fragment for creating schedules
 * https://github.com/Plumillon/ChipView
 * MIGHT BE BETTER >>> https://github.com/splitwise/TokenAutoComplete <<<
 */
public class ScheduleCreator extends Fragment implements View.OnClickListener{
    private MainActivityInteraction mListener;
    private Context context;
    private Button submit;
    private LinearLayout layout;
    private ChipView courseInput;
    private LinearLayout headerProgress;
    private View view;

    public ScheduleCreator() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ScheduleCreator.
     */
    public static ScheduleCreator newInstance() {
        return new ScheduleCreator();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_creator, container, false);
        submit = (Button) view.findViewById(R.id.submit);
        submit.setOnClickListener(this);
        layout = (LinearLayout) view.findViewById(R.id.linear_layout);
        headerProgress = (LinearLayout) view.findViewById(R.id.linlaHeaderProgress);
        courseInput = (ChipView) view.findViewById(R.id.course_input);
        ChipViewAdapter adapter = new MainChipViewAdapter(context);
        courseInput.setAdapter(adapter);
        DataSource.getInstance(context).getCourseNames(context, new DataSource.CourseNameReceiver() {
            @Override
            public void receiveCourseNames(List<CourseChip> courseNames) {


            }
        });
        return view;
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

    }

    public class Tag implements Chip {
        private String mName;
        private int mType = 0;

        public Tag(String name, int type) {
            this(name);
            mType = type;
        }

        public Tag(String name) {
            mName = name;
        }

        @Override
        public String getText() {
            return mName;
        }

        public int getType() {
            return mType;
        }
    }

    public class MainChipViewAdapter extends ChipViewAdapter {
        public MainChipViewAdapter(Context context) {
            super(context);
        }

        @Override
        public int getLayoutRes(int position) {
            Tag tag = (Tag) getChip(position);

            switch (tag.getType()) {
                default:
                case 2:
                case 4:
                    return 0;

                case 1:
                case 5:
                    return R.layout.chip_double_close;

                case 3:
                    return R.layout.chip_close;
            }
        }

        @Override
        public int getBackgroundColor(int position) {
            Tag tag = (Tag) getChip(position);

            switch (tag.getType()) {
                default:
                    return 0;

                case 1:
                case 4:
                    return getColor(R.color.blue);

                case 2:
                case 5:
                    return getColor(R.color.purple);

                case 3:
                    return getColor(R.color.teal);
            }
        }

        @Override
        public int getBackgroundColorSelected(int position) {
            return 0;
        }

        @Override
        public int getBackgroundRes(int position) {
            return 0;
        }

        @Override
        public void onLayout(View view, int position) {
            Tag tag = (Tag) getChip(position);

            if (tag.getType() == 2)
                ((TextView) view.findViewById(android.R.id.text1)).setTextColor(getColor(R.color.blue));
        }
    }
}
