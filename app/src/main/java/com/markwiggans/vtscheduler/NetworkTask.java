package com.markwiggans.vtscheduler;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.AsyncTask;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.markwiggans.vtscheduler.data.CRN;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrey on 3/1/2017.
 */

public class NetworkTask extends AsyncTask<String, Void, JSONObject> {
    public static final int READ_TIMEOUT_MS = 20000;
    public static final int CONNECT_TIMEOUT_MS = 20000;
    private String uuid;
    private String semester;
    private CRN[] crns;
    private boolean post;
    private Context context;
    private String result = "";
    private JSONObject getResults;

    public NetworkTask(Context context, boolean post, String semester, CRN[] crns, String uuid) {
        this.uuid = uuid;
        this.semester = semester;
        this.crns = crns;
        this.post = post;
        this.context = context;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        //String result = Constants.STATUS_OFFLINE;
        try {
            // Send the entered username and password to the server and check for success
            return attemptRequestToServer();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new JSONObject();
    }

    @Override
    protected void onProgressUpdate(Void... params) {

        //activity.NetworkingFlagUpdate(true);
        return;
    }
    @Override
    protected void onPostExecute(JSONObject result) {
        if(post){
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Schedule UUID", this.uuid);
            clipboard.setPrimaryClip(clip);

            Toast.makeText(context, "Copied UUID to clipboard", Toast.LENGTH_SHORT).show();
        }
        else{
            // returning data back to caller
            //((GetCompleted)this.context).onGetComplete(this.getResults);
        }

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

    private JSONObject attemptRequestToServer() throws IOException {
        InputStream is = null;
        String cookie = "empty cookie";
        String result="relogin";
        HttpURLConnection conn;

        try {
            System.setProperty("http.keepAlive", "false");
            Log.d("Scheduler", "Creating connection to server for logging in.. url "+ context.getString(R.string.login_url));
            if(post){

                JSONObject credentials = new JSONObject();
                credentials.put("semester", this.semester);
                int[] crnsList = new int[this.crns.length];
                for(int i = 0; i < this.crns.length; i++){
                    crnsList[i] = this.crns[i].getCRN();
                }
                credentials.put("crns", crnsList);

                URL url = new URL("http://ec2-54-191-60-136.us-west-2.compute.amazonaws.com:8080/share");

                conn = (HttpURLConnection) (url).openConnection();
                conn.setDoOutput(true);
                conn.setReadTimeout(READ_TIMEOUT_MS /* milliseconds */);
                conn.setConnectTimeout(CONNECT_TIMEOUT_MS /* milliseconds */);

                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestMethod("POST");
                conn.connect();
                Log.d("Scheduler", "Sending schedule information to server... url "+ context.getString(R.string.login_url));
                Writer osw = new OutputStreamWriter(conn.getOutputStream());
                osw.write(credentials.toString());
                osw.flush();
                osw.close();
                final int HttpResultCode = conn.getResponseCode();
                is = HttpResultCode >= 400 ? conn.getErrorStream() : conn.getInputStream();

                Log.d("Scheduler", "Reposne is: " + HttpResultCode);
                if (HttpResultCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    StringBuilder responseStrBuilder = new StringBuilder();

                    String inputStr;
                    while ((inputStr = streamReader.readLine()) != null)
                        responseStrBuilder.append(inputStr);
                    JSONObject j = new JSONObject(responseStrBuilder.toString());

                    Log.d("Scheduler",j.toString(4));

                    // storing it in this.uuid
                    this.uuid = j.getString("unique_id");
                    this.getResults = j;

                    //Map<String, List<String>> headerFields = conn.getHeaderFields();
                    //List<String> cookiesHeader = headerFields.get(context.getString(R.string.cookies_header));
                    //cookie = cookiesHeader.get(0).substring(0, cookiesHeader.get(0).indexOf(";"));
                    //saveInSharedPreferences(cookie);
                    //result =Constants.STATUS_LOGGED_IN;
                } else if(HttpResultCode==401){
                    Log.d("Scheduler", "Did not receive HTTP_OK from server!:"+401);
                    //result=Constants.STATUS_RELOGIN;
                }else{
                    //result=Constants.STATUS_OFFLINE;
                    Log.d("Scheduler", "Did not receive HTTP_OK from server!--other");

                }


            }
            else{

                URL url = new URL("http://ec2-54-191-60-136.us-west-2.compute.amazonaws.com:8080/share/"+this.uuid);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT_MS /* milliseconds */);
                conn.setConnectTimeout(CONNECT_TIMEOUT_MS /* milliseconds */);
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestMethod("GET");

                //InputStream in = new BufferedInputStream(conn.getInputStream());

                conn.connect();

                Log.d("Scheduler", "Sending schedule information to server... url "+ url.toString());

                final int HttpResultCode = conn.getResponseCode();
                is = HttpResultCode >= 400 ? conn.getErrorStream() : conn.getInputStream();

                Log.d("Scheduler", "Response is: " + HttpResultCode);
                if (HttpResultCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    StringBuilder responseStrBuilder = new StringBuilder();

                    String inputStr;
                    while ((inputStr = streamReader.readLine()) != null)
                        responseStrBuilder.append(inputStr);
                    JSONObject j = new JSONObject(responseStrBuilder.toString());

                    // storing get request results in object to pass back
                    this.getResults = j;

                    Log.d("Scheduler",j.toString(4));

                    Map<String, List<String>> headerFields = conn.getHeaderFields();
                    //List<String> cookiesHeader = headerFields.get(context.getString(R.string.cookies_header));
                    //cookie = cookiesHeader.get(0).substring(0, cookiesHeader.get(0).indexOf(";"));
                    //saveInSharedPreferences(cookie);
                    //result =Constants.STATUS_LOGGED_IN;
                } else if(HttpResultCode==401){
                    Log.d("Scheduler", "Did not receive HTTP_OK from server!:"+401);
                    //result=Constants.STATUS_RELOGIN;
                }else{
                    //result=Constants.STATUS_OFFLINE;
                    Log.d("Scheduler", "Did not receive HTTP_OK from server!--other");

                }

            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {

            if (is != null) {
                is.close();
            }


        }

        return this.getResults;
    }
}