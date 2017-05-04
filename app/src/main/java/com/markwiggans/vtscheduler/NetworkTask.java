package com.markwiggans.vtscheduler;

import android.os.AsyncTask;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.markwiggans.vtscheduler.data.CRN;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;


import static android.R.attr.password;

/**
 * Created by Andrey on 3/1/2017.
 */

public class NetworkTask extends AsyncTask<String, Void, String> {
    public static final int READ_TIMEOUT_MS = 20000;
    public static final int CONNECT_TIMEOUT_MS = 20000;
    private String uuid;
    private String semester;
    private CRN[] crns;
    private boolean post;
    private Context context;
    private String result = "";

    public NetworkTask(Context context, boolean post, String semester, CRN[] crns, String uuid) {
        this.uuid = uuid;
        this.semester = semester;
        this.crns = crns;
        this.post = post;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        //String result = Constants.STATUS_OFFLINE;
        try {
            // Send the entered username and password to the server and check for success
            result = attemptLogin();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onProgressUpdate(Void... params) {

        //activity.NetworkingFlagUpdate(true);
        return;
    }
    @Override
    protected void onPostExecute(String result) {

        //activity.NetworkingFlagUpdate(false);
        //activity.LoginStatus(result);


    }

    @Override
    protected void onCancelled() {

    }

    private void saveInSharedPreferences(String result) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        if(post){
            editor.putString("uuid", result);
        }
        else{
            //editor.putString("semester")
        }

        //editor.putString("username", username);
        //editor.putString("password", password);

        editor.apply();
    }

    private String attemptLogin() throws IOException {
        InputStream is = null;
        String cookie = "empty cookie";
        String result="relogin";

        try {
            System.setProperty("http.keepAlive", "false");
            Log.d("FitEx", "Creating connection to server for logging in.. url"+ context.getString(R.string.login_url));
            HttpURLConnection conn;

            JSONObject credentials = new JSONObject();



            if(post){
                conn = (HttpURLConnection) ((new URL(
                        context.getString(R.string.login_url)).openConnection()));
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                credentials.put("semester", this.semester);
                int[] crnsList = new int[this.crns.length];
                for(int i = 0; i < this.crns.length; i++){
                    crnsList[i] = this.crns[i].getCRN();
                }

                credentials.put("unique_id", crnsList);
            }
            else{
                conn = (HttpURLConnection) ((new URL(
                        context.getString(R.string.login_url)+"/"+this.uuid).openConnection()));
                conn.setRequestMethod("GET");
            }


            conn.setReadTimeout(READ_TIMEOUT_MS /* milliseconds */);
            conn.setConnectTimeout(CONNECT_TIMEOUT_MS /* milliseconds */);

            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            conn.connect();



            Log.d("FitEx", "Sending login credentials to server... url"+ context.getString(R.string.login_url));
            Writer osw = new OutputStreamWriter(conn.getOutputStream());
            osw.write(credentials.toString());
            osw.flush();
            osw.close();

            final int HttpResultCode = conn.getResponseCode();
            is = HttpResultCode >= 400 ? conn.getErrorStream() : conn.getInputStream();

            Log.d("FitEx", "Reposne is: " + HttpResultCode);
            if (HttpResultCode == HttpURLConnection.HTTP_OK) {
                Map<String, List<String>> headerFields = conn.getHeaderFields();
                //List<String> cookiesHeader = headerFields.get(context.getString(R.string.cookies_header));
                //cookie = cookiesHeader.get(0).substring(0, cookiesHeader.get(0).indexOf(";"));
                //saveInSharedPreferences(cookie);
                //result =Constants.STATUS_LOGGED_IN;
            } else if(HttpResultCode==401){
                Log.d("FitEx", "Did not receive HTTP_OK from server!:"+401);
                //result=Constants.STATUS_RELOGIN;
            }else{
                //result=Constants.STATUS_OFFLINE;
                Log.d("FitEx", "Did not receive HTTP_OK from server!--other");

            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (is != null) {
                is.close();
            }

        }

        return result;
    }
}