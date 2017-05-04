package com.markwiggans.vtscheduler.interfaces;

import org.json.JSONObject;

/**
 * Created by anirudha on 5/4/2017.
 */

public interface GetCompleted {
        // Define data you like to return from AysncTask
        void onGetComplete(JSONObject result);
}
