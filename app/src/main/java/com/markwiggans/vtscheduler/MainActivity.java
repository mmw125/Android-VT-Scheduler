package com.markwiggans.vtscheduler;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.markwiggans.vtscheduler.adapters.CourseAdapter;
import com.markwiggans.vtscheduler.adapters.ScheduleAdapter;
import com.markwiggans.vtscheduler.data.Course;
import com.markwiggans.vtscheduler.data.Schedule;
import com.markwiggans.vtscheduler.database.ScheduleGenerationTask;
import com.markwiggans.vtscheduler.fragments.CourseQuery;
import com.markwiggans.vtscheduler.fragments.LoadingScreen;
import com.markwiggans.vtscheduler.fragments.ScheduleCreator;
import com.markwiggans.vtscheduler.fragments.ScheduleFragment;
import com.markwiggans.vtscheduler.interfaces.MainActivityInteraction;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import java.util.ArrayList;
import java.util.List;

/**
 * The Main Activity for the application
 */
public class MainActivity extends AppCompatActivity implements MainActivityInteraction {
    public static final String LOG_STRING = "VT_Scheduler";
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private SlidingUpPanelLayout slidingPanelLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private LinearLayout dragView;

    private TextView panelUpLabel;
    private ProgressBar panelUpProgressBar;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] menuOptions;
    private List<Schedule> schedules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();
        menuOptions = getResources().getStringArray(R.array.menu_options);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        dragView = (LinearLayout) findViewById(R.id.dragView);

        slidingPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        panelUpLabel = (TextView) findViewById(R.id.panel_up_label);
        panelUpProgressBar = (ProgressBar) findViewById(R.id.panel_up_progress_bar);

        slidingPanelLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

//        panelUpList.setAdapter(new ScheduleAdapter(this, R.id.panel_up_list, new ArrayList<Schedule>()));
        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, menuOptions));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        setActionBar((Toolbar) findViewById(R.id.main_toolbar));
        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*
     * Called whenever we call invalidateOptionsMenu()
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_websearch:
                // create intent to perform web search for this planet
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
                // catch event that there's no activity to handle intent
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Fragment courseQuery, scheduleCreator;

    @Override
    public void changeFragment(String fragmentName, Object... params) {
        Fragment fragment = null;
        if (CourseQuery.COURSE_QUERY_FRAGMENT.equals(fragmentName)) {
            if (courseQuery == null) {
                courseQuery = new CourseQuery();
            }
            fragment = courseQuery;
        } else if (ScheduleCreator.SCHEDULE_CREATOR_FRAGMENT.equals(fragmentName)) {
            if (scheduleCreator == null) {
                scheduleCreator = new ScheduleCreator();
            }
            fragment = scheduleCreator;
        } else if (LoadingScreen.LOADING_SCREEN_FRAGMENT.equals(fragmentName)) {
            fragment = LoadingScreen.newInstance();
        }
        if (fragment != null) {
            // update the main content by replacing fragments
            getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        }
    }

    @Override
    public void generateSchedules(ArrayList<Course> courseList) {
        showSlidingUpPanel(true);
        slidingPanelLayout.setAnchorPoint(0.3f);
        slidingPanelLayout.setPanelState(PanelState.COLLAPSED);
        panelUpLabel.setText("Loading");
        panelUpProgressBar.setVisibility(View.VISIBLE);
        getFragmentManager().beginTransaction().replace(R.id.panel_up_content, new LoadingScreen()).commit();
        new ScheduleGenerationTask(this, new ScheduleGenerationTask.ScheduleGeneratorTaskReceiver() {
            @Override
            public void onSchedulesGenerated(List<Schedule> results) {
                slidingPanelLayout.setAnchorPoint(1.0f);
                panelUpLabel.setText("Generated Schedules");
                panelUpProgressBar.setVisibility(View.INVISIBLE);
                schedules = results;
                getFragmentManager().beginTransaction().replace(R.id.panel_up_content, ScheduleFragment.newInstance(Schedule.getSchedulesIds(schedules))).commit();
            }
        }).execute();
    }

    @Override
    public void showSlidingUpPanel(final boolean show) {
        Log.d(MainActivity.LOG_STRING, show ? "showing" : "hiding");
        slidingPanelLayout.setPanelState(show ? SlidingUpPanelLayout.PanelState.COLLAPSED : SlidingUpPanelLayout.PanelState.HIDDEN);
    }

    /*
     * The click listener for ListView in the navigation drawer
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /**
     * Called when an item in the drawer is selected with its index
     *
     * @param position the index in the drawer that was selected
     */
    private void selectItem(int position) {
        if (position == 0) {
            changeFragment(CourseQuery.COURSE_QUERY_FRAGMENT);
        } else {
            changeFragment(ScheduleCreator.SCHEDULE_CREATOR_FRAGMENT);
        }
        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(menuOptions[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    /**
     * Sets the title of the activity
     *
     * @param title the string to set the title to
     */
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        if (getActionBar() != null) {
            getActionBar().setTitle(mTitle);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (slidingPanelLayout != null &&
                (slidingPanelLayout.getPanelState() == PanelState.EXPANDED || slidingPanelLayout.getPanelState() == PanelState.ANCHORED)) {
            slidingPanelLayout.setPanelState(PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public List<Schedule> getSchedules() {
        return schedules;
    }
}