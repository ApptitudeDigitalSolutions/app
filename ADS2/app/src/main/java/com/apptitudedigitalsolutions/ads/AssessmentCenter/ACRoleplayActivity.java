package com.apptitudedigitalsolutions.ads.AssessmentCenter;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;

import com.apptitudedigitalsolutions.ads.Application.ADSApplication;
import com.apptitudedigitalsolutions.ads.R;
import com.apptitudedigitalsolutions.ads.DatabaseUtils.DatabaseHelper;

/**
 * Created by Elliot on 19/01/2017.
 */

public class ACRoleplayActivity extends Activity {
    ADSApplication appState = ((ADSApplication)this.getApplication());

    String msg = "Android : ";
    JSONArray roleplayPagesJSONArray = new JSONArray();
    DatabaseHelper db;

//    String appState.AC_CURRENT_CANDIDATE_ID;
//    String appState.ACID;
    String mCURRENT_PAGE_TYPE = "";

    boolean isRecording = false;

//    String appState.AC_TITLE ="";
//    String appState.AC_CONDUCTED_ON="";
//    String appState.AC_DESC="";
//    String appState.AC_ACTIVITY_IDS="";
//    String appState.AC_ACTIVITY_TYPES="";
    String mACTIVITY_TYPE = "";
    String mRANDOM_FILENAME = "";
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    int mCURRENT_TIME_TAKEN = 0;
    int mTIMER_HAS_BEEN_STARTED = 0;
    int mTIME_ON_THIS_QUESTION = 0;
    LinearLayout intro_Layout;
    LinearLayout interview_Layout;
    LinearLayout end_Layout;
    LinearLayout interview_hud;
//    String appState.AC_SET_ACTIVITIE_TAGS;
//    String appState.AC_COMPLETED_ACTIVITIES;

    private MediaRecorder mRecorder = null;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roleplay_activity);
        Log.d(msg, "The onCreate() event");

        db = new DatabaseHelper(ACRoleplayActivity.this);
        db.getReadableDatabase();
        ArrayList<String> userdata = db.getUsernamePasscodeEndpointArray();
//        appState.USERNAME = userdata.get(4);
//        appState.PASSCODE = userdata.get(2);
//        appState.ENDPOINT = userdata.get(0);
//        appState.COMPANY_ID = userdata.get(1);

        // get values from intent and parse into usage objects
//        Intent intent = getIntent();
//        appState.AC_CURRENT_CANDIDATE_ID= intent.getStringExtra("candidate_id");
//        appState.ACID = intent.getStringExtra("ac_id");
//        mACTIVITY_TYPE = "roleplay";
//        appState.AC_TITLE = intent.getStringExtra("title");
//        appState.AC_CONDUCTED_ON = intent.getStringExtra("to_be_conducted_on");
//        appState.AC_DESC = intent.getStringExtra("description");
//        appState.AC_ACTIVITY_IDS = intent.getStringExtra("activity_ids");
//        appState.AC_ACTIVITY_TYPES = intent.getStringExtra("activity_types");
//        appState.AC_SET_ACTIVITIE_TAGS= intent.getStringExtra("set_activitie_tags");
//        appState.AC_COMPLETED_ACTIVITIES= intent.getStringExtra("completed_activities");

        intro_Layout = (LinearLayout) findViewById(R.id.interview_intro_layout);
        interview_Layout = (LinearLayout) findViewById(R.id.interview_layout);
        end_Layout = (LinearLayout) findViewById(R.id.interview_end_layout);
        interview_hud = (LinearLayout) findViewById(R.id.interview_hud);


        // get all the interview pages
        getRoleplayPages();
    }

    public void getRoleplayPages(){

        // retrieve the list of participats
        String URL = "https://"+ appState.ENDPOINT + "/v1/companies/roleplay/"+appState.ACID;
        Log.v("ADS", "URL is  .. " + URL);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", appState.USERNAME);
        params.put("passcode", appState.PASSCODE);
        params.put("ac_id", appState.ACID);

        JsonObjectRequest request_json = new JsonObjectRequest(URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            roleplayPagesJSONArray = response.getJSONArray("pages");
                            for(int i = 0; i < roleplayPagesJSONArray.length();){
                                JSONObject page = roleplayPagesJSONArray.getJSONObject(i);
                                i++;
                            }
                            setViews();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(ACRoleplayActivity.this);
        request_json.setShouldCache(false);
        requestQueue.add(request_json);// reload the list view
    }

    /**
     * Called when the activity is about to become visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(msg, "The onStart() event");
    }

    /**
     * Called when the activity has become visible.
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(msg, "The onResume() event");
    }

    /**
     * Called when another activity is taking focus.
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(msg, "The onPause() event");
        super.onPause();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }
    }

    /**
     * Called when the activity is no longer visible.
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(msg, "The onStop() event");
    }

    /**
     * Called just before the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(msg, "The onDestroy() event");
    }

    public void nextInterviewQuestion(View v){
        // get type of current page
        mTIME_ON_THIS_QUESTION = 0;
        // stop the rec
        if(isRecording) {
            stopRecording();
        }
        if(mCURRENT_PAGE_TYPE == "role"){
            mCURRENT_PAGE_TYPE = "end";
        }
        // save all info to the db (id,file_ref,length,candiddate_id,question_id)
        db.insertNewAudio(Integer.valueOf(appState.AC_CURRENT_CANDIDATE_ID),1,Integer.valueOf(appState.ACID),mFileName,mACTIVITY_TYPE,0,0,0);
        setViews();
    }

    public void promptTapped(){
        // record the time stamp on this question
    }

    public void goToReviewPage(View v){
        // pass the candidate id etc on to the other activity
        Intent intent = new Intent(ACRoleplayActivity.this, ACActivityReviewActivity.class);
//        intent.putExtra("candidate_id", appState.AC_CURRENT_CANDIDATE_ID);
//        intent.putExtra("ac_id", appState.ACID);
//        intent.putExtra("id", appState.ACID);
//        intent.putExtra("title", appState.AC_TITLE);
//        intent.putExtra("activity_type","roleplay");
//        intent.putExtra("to_be_conducted_on", appState.AC_CONDUCTED_ON);
//        intent.putExtra("description", appState.AC_DESC);
//        intent.putExtra("activity_ids", appState.AC_ACTIVITY_IDS);
//        intent.putExtra("activity_types", appState.AC_ACTIVITY_TYPES);
//        intent.putExtra("set_activitie_tags",appState.AC_SET_ACTIVITIE_TAGS);
//        intent.putExtra("completed_activities",appState.AC_COMPLETED_ACTIVITIES);

        startActivity(intent);

    }

    public void startInterview(View v){
        if(mCURRENT_PAGE_TYPE == "role"){
           mCURRENT_PAGE_TYPE = "end";
        }else {
            mCURRENT_PAGE_TYPE = "role";
        }
        setViews();
    }

    public void setViews(){
        // ok so we first need to get the json object that corresponds to the section we are in

        try {
            JSONObject currentPage = roleplayPagesJSONArray.getJSONObject(0);

            // now we need to pick out all of the values
            int id = currentPage.getInt("id");
            String presentation_info_text = currentPage.getString("roleplay_info_text");
            String presentation_info_ref = currentPage.getString("roleplay_info_ref");
            if(mCURRENT_PAGE_TYPE == "") {
                mCURRENT_PAGE_TYPE = "intro";
            }

            // now we need to decide which layouts to render and which should be set to GONE
            if(mCURRENT_PAGE_TYPE.equals("intro")){
                intro_Layout.setVisibility(View.VISIBLE);
                interview_Layout.setVisibility(View.GONE);
                end_Layout.setVisibility(View.GONE);
                interview_hud.setVisibility(View.GONE);

                // set the varables for this view
                TextView introTitleText = (TextView) findViewById(R.id.interview_intro_title_text);
                introTitleText.setText("Roleplay Introduction");

                Button introText = (Button) findViewById(R.id.interview_intro_text);
                    introText.setText(presentation_info_text);

                Button startRoleplay = (Button) findViewById(R.id.start_interview_button);
                startRoleplay.setText("Start Roleplay");

                TextView recrodingLable = (TextView) findViewById(R.id.recroding_lable);
                recrodingLable.setVisibility(View.INVISIBLE);
            }

            if(mCURRENT_PAGE_TYPE.equals("role")){
                intro_Layout.setVisibility(View.VISIBLE);
                interview_Layout.setVisibility(View.GONE);
                end_Layout.setVisibility(View.GONE);
                interview_hud.setVisibility(View.GONE);

                if(mTIMER_HAS_BEEN_STARTED==0){
                    updateTimeTaken();
                }

                Button next_question = (Button) findViewById(R.id.start_interview_button);
                next_question.setText("End Rolplay Audio Capture");

                // add that recording lable to the screen
                // AUDIO_START we will also need to start the audio recroding here
                mRANDOM_FILENAME = randomString(10) + appState.AC_CURRENT_CANDIDATE_ID;
                mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
                mFileName += "/"+mRANDOM_FILENAME+".3gp";
                startRecording();

            }

            if(mCURRENT_PAGE_TYPE.equals("end")){
                intro_Layout.setVisibility(View.GONE);
                interview_Layout.setVisibility(View.GONE);
                end_Layout.setVisibility(View.VISIBLE);
                interview_hud.setVisibility(View.GONE);
                TextView recrodingLable = (TextView) findViewById(R.id.recroding_lable);
                recrodingLable.setVisibility(View.INVISIBLE);
                stopRecording();
                db.insertNewAudio(Integer.valueOf(appState.AC_CURRENT_CANDIDATE_ID),1,Integer.valueOf(appState.ACID),mFileName,mACTIVITY_TYPE,0,0,0);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    String randomString( int len ){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

    public void updateTimeTaken(){
        mCURRENT_TIME_TAKEN++;
        mTIME_ON_THIS_QUESTION++;
        mTIMER_HAS_BEEN_STARTED =1;

//        if(mCURRENT_TIME_TAKEN => )
        // convert to big int and then convert to a MM and SS object string and display
        Integer intObj = new Integer(mCURRENT_TIME_TAKEN);
        Number numObj = (Number)intObj;
        BigDecimal big = new BigDecimal(numObj.toString());

        int[] compoents = splitToComponentTimes(big);
        Log.v("ADS", "The Time taken so far is = " + compoents);

        TextView timeTaken = (TextView) findViewById(R.id.current_interviewTime);
        timeTaken.setText("Time Taken : " + compoents[1] + "mins " + compoents[2] + "sec");

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateTimeTaken();
            }
        }, 1000);

    }

    public static int[] splitToComponentTimes(BigDecimal biggy)
    {
        long longVal = biggy.longValue();
        int hours = (int) longVal / 3600;
        int remainder = (int) longVal - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        int[] ints = {hours , mins , secs};
        return ints;
    }



    private static final String LOG_TAG = "AudioRecordTest";
    private static String mFileName = null;

    private void startRecording() {
        isRecording = true;
        TextView recrodingLable = (TextView) findViewById(R.id.recroding_lable);
        recrodingLable.setVisibility(View.VISIBLE);
        Log.e("ADS","STARTED REC with file ref :" + mFileName);
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
        mRecorder.start();
    }

    private void stopRecording() {
        TextView recrodingLable = (TextView) findViewById(R.id.recroding_lable);
        recrodingLable.setVisibility(View.INVISIBLE);
        Log.e("ADS","STOPPED REC for file :" + mFileName);
        if(mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

}