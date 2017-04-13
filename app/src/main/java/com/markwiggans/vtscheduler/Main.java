package com.markwiggans.vtscheduler;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.markwiggans.vtscheduler.fragments.LoadingScreen;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.Arrays;
import java.util.List;

public class Main extends Activity {
    private SlidingUpPanelLayout mLayout;
    private static final String TAG = "MainAcitvity";
    List<String> array_list;
    TextView textView ;
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        init();            // call init method
        setListview();    // call setListview method
        panelListener(); // Call paneListener method

        android.app.FragmentManager manager = getFragmentManager();
        manager.beginTransaction().replace(R.id.frame_layout, new LoadingScreen()).commit();

    }

    /**
     * Initialization of the textview and SlidingUpPanelLayout
     */
    public void init(){

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        textView = (TextView) findViewById(R.id.list_main);
        listview = (ListView) findViewById(R.id.list);
    }

    /**
     *  in this method, we set array adapter to display list of item
     *  within this method, call callback setOnItemClickListener method
     *  It call when use click on the list of item
     *  When user click on the list of item, slide up layout and display item of the list
     */
    public void setListview(){

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                textView.setText(array_list.get(position));
                Toast.makeText(Main.this, "onItemClick" , Toast.LENGTH_SHORT).show();
            }
        });


        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                array_list());

        listview.setAdapter(arrayAdapter);
    }

    /**
     * this method call setPanelSlidelistener method to listen open and close of slide panel
     */
    public void panelListener(){

    }

    /**
     * This is return type method.
     * With in this method, we create array list
     * @return array list
     */
    public List<String> array_list(){
        array_list = Arrays.asList(
                "This",
                "Is",
                "An",
                "Example",
                "ListView",
                "That",
                "You",
                "Can",
                "Scroll",
                ".",
                "It",
                "Shows",
                "How",
                "Any",
                "Scrollable",
                "View",
                "Can",
                "Be",
                "Included",
                "As",
                "A",
                "Child",
                "Of",
                "SlidingUpPanelLayout"
        );
        return array_list;
    }


    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }
}
