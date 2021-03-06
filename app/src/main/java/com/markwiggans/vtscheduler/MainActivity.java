package com.markwiggans.vtscheduler;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.markwiggans.vtscheduler.data.Course;
import com.markwiggans.vtscheduler.data.Schedule;
import com.markwiggans.vtscheduler.data.ScheduleGenerationTask;
import com.markwiggans.vtscheduler.fragments.CourseQuery;
import com.markwiggans.vtscheduler.fragments.HomeScreen;
import com.markwiggans.vtscheduler.fragments.ScheduleCreator;
import com.markwiggans.vtscheduler.fragments.ScheduleFragment;
import com.markwiggans.vtscheduler.fragments.ViewSavedSchedule;
import com.markwiggans.vtscheduler.interfaces.MainActivityInteraction;
import com.markwiggans.vtscheduler.interfaces.OnEventListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import java.util.ArrayList;
import java.util.List;

/**
 * The Main Activity for the application
 */
public class MainActivity extends Activity implements MainActivityInteraction {
    public static final String LOG_STRING = "VT_Scheduler";
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private SlidingUpPanelLayout slidingPanelLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private ScheduleFragment panelUpFragment;

    private TextView panelUpLabel;
    private ProgressBar panelUpProgressBar;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] menuOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();
        menuOptions = getResources().getStringArray(R.array.menu_options);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        slidingPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        panelUpLabel = (TextView) findViewById(R.id.panel_up_label);
        panelUpProgressBar = (ProgressBar) findViewById(R.id.panel_up_progress_bar);

        slidingPanelLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        panelUpFragment = (ScheduleFragment) getFragmentManager().findFragmentByTag(ScheduleFragment.TAG_SCHEDULE_FRAGMENT);
        if(panelUpFragment == null) {
            panelUpFragment = new ScheduleFragment();
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, panelUpFragment, ScheduleFragment.TAG_SCHEDULE_FRAGMENT).commit();
        }
        createDrawer();

        slidingPanelLayout.setPanelState(PanelState.HIDDEN);

        if (savedInstanceState == null) {
            homeScreen = new HomeScreen();
            getFragmentManager().beginTransaction().replace(R.id.content_frame, homeScreen).commit();
        }
    }

    public void createDrawer() {
        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, menuOptions));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitleTextColor(getColor(R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_drawer);
        setActionBar(toolbar);


        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        if(getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Called whenever we call invalidateOptionsMenu()
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    private Fragment courseQuery, scheduleCreator, homeScreen, scheduleViewer;
    private String currentFragment;

    @Override
    public void changeFragment(String fragmentName, Object... params) {
        if(fragmentName.equals(currentFragment)) {
            return;
        }
        currentFragment = fragmentName;
        Fragment fragment = null;
        if (HomeScreen.HOME_SCREEN_FRAGMENT.equals(fragmentName)) {
            if (homeScreen == null) {
                homeScreen = new HomeScreen();
            }
            fragment = homeScreen;
        } else if(CourseQuery.COURSE_QUERY_FRAGMENT.equals(fragmentName)) {
            if (courseQuery == null) {
                courseQuery = new CourseQuery();
            }
            fragment = courseQuery;
        } else if (ScheduleCreator.SCHEDULE_CREATOR_FRAGMENT.equals(fragmentName)) {
            if (scheduleCreator == null) {
                scheduleCreator = new ScheduleCreator();
            }
            fragment = scheduleCreator;
        } else if(ViewSavedSchedule.SAVED_SCHEDULES_FRAGMENT.equals(fragmentName)){
            if(scheduleViewer == null){
                scheduleViewer = new ViewSavedSchedule();
            }
            fragment = scheduleViewer;
        }
        changeFragment(fragment);
    }

    @Override
    public void changeFragment(Fragment fragment) {
        if (fragment != null) {
            // update the main content by replacing fragments
            getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
        }
    }

    @Override
    public void loadSchedule(String id) {
        panelUpFragment.startLoading();
        Schedule.createFromUUID(getApplicationContext(), new OnEventListener<Schedule>() {
            @Override
            public void onSuccess(Schedule schedule) {
                new AsyncTask<Schedule, Void, Schedule>() {
                    @Override
                    protected Schedule doInBackground(Schedule... schedules) {
                        schedules[0].getScore(getApplicationContext());
                        return schedules[0];
                    }

                    @Override
                    protected void onPostExecute(Schedule schedule) {
                        List<Schedule> schedules = new ArrayList<>();
                        schedules.add(schedule);
                        panelUpFragment.onSchedulesGenerated(schedules);
                    }
                }.execute(schedule);
            }
        }, id);
    }

    @Override
    public void generateSchedules(ArrayList<Course> courseList) {
        panelUpFragment.startLoading();
        Toast.makeText(getApplicationContext(), "Starting generation of schedules!", Toast.LENGTH_SHORT).show();
        new ScheduleGenerationTask(this, panelUpFragment).execute(courseList.toArray(new Course[courseList.size()]));
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

    private String titleToFragmentName(String title) {
        if(title.equals(getString(R.string.home))) {
            return HomeScreen.HOME_SCREEN_FRAGMENT;
        } else if(title.equals(getString(R.string.search_courses))) {
            return CourseQuery.COURSE_QUERY_FRAGMENT;
        } else if(title.equals(getString(R.string.create_schedule))) {
            return ScheduleCreator.SCHEDULE_CREATOR_FRAGMENT;
        }else if(title.equals(getString(R.string.load_schedule))){
            return ViewSavedSchedule.SAVED_SCHEDULES_FRAGMENT;
        }
        return null;
    }

    /**
     * Called when an item in the drawer is selected with its index
     *
     * @param position the index in the drawer that was selected
     */
    private void selectItem(int position) {
        String[] items = getResources().getStringArray(R.array.menu_options);
        changeFragment(titleToFragmentName(items[position]));

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
//        mDrawerToggle.syncState();
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
    public void setPanelUpToolbar(String title, boolean loading) {
        panelUpLabel.setText(title);
        panelUpProgressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        slidingPanelLayout.setAnchorPoint(loading ? 0.3f : 1.0f);
        slidingPanelLayout.setPanelState(loading ? PanelState.COLLAPSED : PanelState.EXPANDED);
    }

    @Override
    public void setSelected(String title) {
        currentFragment = titleToFragmentName(title);
        String[] arr = getResources().getStringArray(R.array.menu_options);
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(title)) {
                selectItem(i);
                return;
            }
        }
    }

    @Override
    public PanelState getSlidingUpPanelStatus() {
        return slidingPanelLayout.getVisibility() == View.GONE ? PanelState.HIDDEN : slidingPanelLayout.getPanelState();
    }
}