package com.markwiggans.vtscheduler.fragments;

import android.app.Fragment;
import android.content.ClipboardManager;
import android.content.Context;
import android.database.CursorJoiner;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.markwiggans.vtscheduler.NetworkTask;
import com.markwiggans.vtscheduler.R;
import com.markwiggans.vtscheduler.adapters.CourseAdapter;
import com.markwiggans.vtscheduler.data.CRN;
import com.markwiggans.vtscheduler.data.Course;
import com.markwiggans.vtscheduler.data.Schedule;
import com.markwiggans.vtscheduler.database.CourseReaderContract;
import com.markwiggans.vtscheduler.database.DataSource;
import com.markwiggans.vtscheduler.database.DatabaseTask;
import com.markwiggans.vtscheduler.database.Query;
import com.markwiggans.vtscheduler.database.QueryResult;
import com.markwiggans.vtscheduler.interfaces.GetCompleted;
import com.markwiggans.vtscheduler.interfaces.MainActivityInteraction;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static android.content.Context.CLIPBOARD_SERVICE;


/**
 * Fragment for Quering courses
 */
public class ViewSavedSchedule extends Fragment implements View.OnClickListener, GetCompleted, Schedule.ScheduleReceiver {
    public static final String SAVED_SCHEDULES_FRAGMENT = "VIEW_SAVED";
    private MainActivityInteraction mListener;
    private Button submit;
    private EditText crn;
    private TextView getResults;
    private View view;

    public ViewSavedSchedule() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ScheduleCreator.
     */
    public static ViewSavedSchedule newInstance() {
        return new ViewSavedSchedule();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_saved_schedule, container, false);
        crn = (EditText) view.findViewById(R.id.id_input);

        crn.isLongClickable();
        // automatically add uuid to editext
        /*ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);
        String label = clipboard.getPrimaryClip().getDescription().getLabel().toString();
        if(label.equals("Schedule_UUID")){
            crn.setText(clipboard.getPrimaryClip().toString());
        }*/


        getResults = (TextView)view.findViewById(R.id.getResults);
        submit = (Button) view.findViewById(R.id.submit);
        submit.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivityInteraction) {
            mListener = (MainActivityInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MainActivityInteraction");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.setSelected(getString(R.string.search_courses));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if(v.equals(submit)) {
            //mListener.loadSchedule(crn.getText().toString());
            new NetworkTask(getContext(), false, "",null ,crn.getText().toString()){

                // Doing this so that I can access the data from onPostExecute
                @Override
                protected void onPostExecute( JSONObject result ) {

                    super.onPostExecute(result);
                    // Do something with result here
                    Toast.makeText(getActivity(), "Schedule Data retrieved", Toast.LENGTH_SHORT).show();
                    try{
                        //getResults.setText(result.toString(4));

                        JSONArray JSONCrnsArray = result.getJSONArray("crns");
                        int[] crnsArray = new int[JSONCrnsArray.length()];
                        for(int i = 0; i < JSONCrnsArray.length(); i++){
                            crnsArray[i] = JSONCrnsArray.getInt(i);
                        }
                        displaySchedule(result.getString("semester"), crnsArray);
                    }catch(Exception e){
                        getResults.setText(result.toString());
                        Log.d("Scheduler", e.toString());
                    }


                }
            }.execute();
        }
    }

    @Override
    public void onGetComplete(JSONObject result){
        Toast.makeText(getActivity(), "Schedule Data retrieved", Toast.LENGTH_SHORT).show();
        try{
            getResults.setText(result.toString(4));
        }catch(Exception e){
            getResults.setText(result.toString());
            Log.d("Scheduler", e.toString());
        }


    }

    public void displaySchedule(String semester, int[] crnsArray){
        Schedule.createScheduleFromServerResponse(getContext(), this, semester, crnsArray);
    }



    @Override
    public void receiveSchedule(Schedule schedule) {
        //ToDo: Display schedule in the getResults TextView
    }


}
