package com.apptitudedigitalsolutions.ads.AssessmentCenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.apptitudedigitalsolutions.ads.Application.ADSApplication;
import com.apptitudedigitalsolutions.ads.R;
import com.apptitudedigitalsolutions.ads.DatabaseUtils.Utilities;
import com.apptitudedigitalsolutions.ads.DatabaseUtils.DatabaseHelper;

import static android.view.View.TEXT_ALIGNMENT_TEXT_START;

/**
 * Created by Elliot on 20/11/2016.
 */

public class ACActivityReviewActivity extends Activity implements MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener{
    String msg = "Android : ";
    ADSApplication appState = ((ADSApplication)this.getApplication());
    SharedPreferences mAppSettings;
    Boolean shouldRenderHistory;

    JSONObject answers = new JSONObject();
    public JSONArray answersForAllReviewQuestions = new JSONArray();
   
    JSONObject CANDIDATE_HISTORY = new JSONObject();
    ArrayList<ArrayList<String>> mANSWERS_TEXT = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> mANSWERS_TYPES = new ArrayList<ArrayList<String>>();
    ArrayList<String> mTEXT_FOR_COMMENTS = new ArrayList<String>();
    ArrayList<String> mTEXT_FOR_SCOURS = new ArrayList<String>();
    DatabaseHelper db;

    private Handler mHandler = new Handler();
    boolean mPLAYING = false;
    private MediaPlayer mPlayer = new MediaPlayer();
    private Button btnPlay;
    private SeekBar songProgressBar;
    private TextView songCurrentDurationLabel;
    private TextView songTotalDurationLabel;
    private Utilities utils;

    private RecyclerView mRecyclerViewReview;
    private RecyclerView.Adapter mAdapterReview;
    private RecyclerView.LayoutManager mLayoutManagerReview;


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    String mTAPPED_AUDIO_FILE_NAME;

    int mCURRENT_QUESTION_NUMBER;
    JSONArray reviewPagesJSONArray = new JSONArray();
    int mREVIEW_LENGTH;
    int mCURRENT_REVIEW_PAGE_POINTER_INDEX = 0;
    int mSHOULD_RETRUN_TO_ACMAIN = 0;
//    String appState.AC_SET_ACTIVITIE_TAGS;
//    String appState.AC_COMPLETED_ACTIVITIES;
    JSONArray audioBlobsJSONARRAY =new JSONArray();
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_interview_review);
        Log.d(msg, "The onCreate() event");


        Context context = getApplicationContext();
        mAppSettings = context.getSharedPreferences("SharedPref", Context.MODE_PRIVATE);


        btnPlay = (Button) findViewById(R.id.btnPlay);
        songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
        songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
        songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);
        utils = new Utilities();

        // Listeners
        songProgressBar.setOnSeekBarChangeListener(this); // Important
        mPlayer.setOnCompletionListener(this);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        db = new DatabaseHelper(ACActivityReviewActivity.this);
        db.getReadableDatabase();

        String CANDIDATE_HISTORY_string = mAppSettings.getString(appState.AC_CURRENT_CANDIDATE_ID,"");
        try {
            CANDIDATE_HISTORY = new JSONObject(CANDIDATE_HISTORY_string);

        Log.i("ADS",CANDIDATE_HISTORY_string);

       // Now we need to unload the answers into the correct arrays, lets print the array and then work out how to handle it

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                JSONArray x = new JSONArray(CANDIDATE_HISTORY.getJSONArray("answers"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


//        ArrayList<String> userdata = db.getUsernamePasscodeEndpointArray();
//        appState.USERNAME = userdata.get(4);
//        appState.PASSCODE = userdata.get(2);
//        appState.ENDPOINT = userdata.get(0);
//        appState.COMPANY_ID = userdata.get(1);

//        Intent intent = getIntent();
//        appState.AC_CURRENT_CANDIDATE_ID= intent.getStringExtra("candidate_id");
//        appState.ACID = intent.getStringExtra("ac_id");
//        appState.AC_CURRENT_ACTIVITY_TYPE = intent.getStringExtra("activity_type");
//
//        appState.AC_TITLE = intent.getStringExtra("title");
//        appState.AC_CONDUCTED_ON = intent.getStringExtra("to_be_conducted_on");
//        appState.AC_DESC = intent.getStringExtra("description");
//        appState.AC_ACTIVITY_IDS = intent.getStringExtra("activity_ids");
//        appState.AC_ACTIVITY_TYPES = intent.getStringExtra("activity_types");
//        appState.AC_SET_ACTIVITIE_TAGS= intent.getStringExtra("set_activitie_tags");
//        appState.AC_COMPLETED_ACTIVITIES= intent.getStringExtra("completed_activities");

        audioBlobsJSONARRAY = db.getAllAudioFileReferences(appState.AC_CURRENT_CANDIDATE_ID,appState.AC_CURRENT_ACTIVITY_TYPE);
        // create all of that adapter shit
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_review);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ACActivityReviewActivity.MyReviewAdapter(audioBlobsJSONARRAY);
        mRecyclerView.setAdapter(mAdapter);


        Log.e("ADS", "AUDO BLOBS >>>>>>> " + audioBlobsJSONARRAY.toString());



        getInterviewReviewPages();

        mRecyclerViewReview = (RecyclerView) findViewById(R.id.my_recycler_view_review_questions);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerViewReview.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManagerReview = new LinearLayoutManager(this);
        mRecyclerViewReview.setLayoutManager(mLayoutManagerReview);

        mAdapterReview = new ACActivityReviewActivity.MyAdapter(reviewPagesJSONArray);
        mRecyclerViewReview.setAdapter(mAdapterReview);




    }

    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable thread
     * */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mPlayer.getDuration();
            long currentDuration = mPlayer.getCurrentPosition();

            // Displaying Total Duration time
            songTotalDurationLabel.setText(""+utils.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            songCurrentDurationLabel.setText(""+utils.milliSecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            songProgressBar.setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };

    /**
     *
     * */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

    }

    /**
     * When user starts moving the progress handler
     * */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // remove message Handler from updating progress bar
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mPlayer.getDuration();
        int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        mPlayer.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();
    }


    public  void pausePlaySelection(View v){
        if(mPLAYING == true){
            stopPlaying();
        }else {
            startPlaying();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
            mPLAYING = true;
        } else {

            stopPlaying();
            mPLAYING = false;
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {

                mPlayer.setDataSource(mTAPPED_AUDIO_FILE_NAME);
                mPlayer.prepare();
                mPlayer.start();
                btnPlay.setText("Tap to pause");
                songProgressBar.setProgress(0);
                songProgressBar.setMax(100);

                // Updating progress bar
                updateProgressBar();

        } catch (IOException e) {
            Log.e("ADS", "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }






























    public void submitReview(View v){
        // itterate over the array of objects mANSWERS_TEXT and then pass credetials to submit method
        JSONObject userObject = new JSONObject();
        try {
            userObject.put("id", appState.AC_CURRENT_CANDIDATE_ID);


        for(int i = 0; i < mANSWERS_TEXT.size();){
            for(int j = 0; j < mANSWERS_TEXT.get(i).size();){
               JSONObject answerObject = new JSONObject();
                answerObject.put("answer_text",mANSWERS_TEXT.get(i).get(j));
                answerObject.put("answer_type",mANSWERS_TYPES.get(i).get(j));

                answersForAllReviewQuestions.put(i,answerObject);

               submit(mANSWERS_TEXT.get(i).get(j),mANSWERS_TYPES.get(i).get(j),i);

                //int id = getResources().getIdentifier("c"+i, "id", getPackageName());
                j++;
            }
            i++;
        }

            userObject.put("answers", answersForAllReviewQuestions);
            Log.e("ADS" , "User Object " + userObject);

            //save the object to shared preferences
            SharedPreferences.Editor editor = mAppSettings.edit();
            editor.putString(appState.AC_CURRENT_CANDIDATE_ID, String.valueOf(userObject));
            editor.commit();

    } catch (JSONException e) {
            e.printStackTrace();
        }
        mANSWERS_TEXT.clear();
        mANSWERS_TYPES.clear();

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        mSHOULD_RETRUN_TO_ACMAIN = 1;
        if(mSHOULD_RETRUN_TO_ACMAIN ==1){
            // send acitvity completed request
            candidateCompletedActivity();

            // return to main
            Intent intent = new Intent(ACActivityReviewActivity.this, ACAdminActivity.class);
            //
//            intent.putExtra("id", appState.ACID);
//            intent.putExtra("title", appState.AC_TITLE);
//            intent.putExtra("to_be_conducted_on", appState.AC_CONDUCTED_ON);
//            intent.putExtra("description", appState.AC_DESC);
//            intent.putExtra("activity_ids", appState.AC_ACTIVITY_IDS);
//            intent.putExtra("activity_types", appState.AC_ACTIVITY_TYPES);
//            intent.putExtra("set_activitie_tags",appState.AC_SET_ACTIVITIE_TAGS);
//            intent.putExtra("completed_activities",appState.AC_COMPLETED_ACTIVITIES);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }

//        setViews();
    }


    public void getInterviewReviewPages(){

        // retrieve the list of participats
        String URL ="";
        if(appState.AC_CURRENT_ACTIVITY_TYPE.equals("interview")) {
             URL = "http://" + appState.ENDPOINT + "/v1/companies/assessmentcentres/interview/review/" + appState.ACID;
        }else if(appState.AC_CURRENT_ACTIVITY_TYPE.equals("roleplay")){
            URL = "http://" + appState.ENDPOINT + "/v1/companies/assessmentcentres/roleplay/review/" + appState.ACID;
        }else if(appState.AC_CURRENT_ACTIVITY_TYPE.equals("presentation")){
            URL = "http://" + appState.ENDPOINT + "/v1/companies/assessmentcentres/presentation/review/" + appState.ACID;
        }

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
                            reviewPagesJSONArray = response.getJSONArray("pages");
                            mREVIEW_LENGTH = reviewPagesJSONArray.length()-1;

                            for(int i = 0; i < reviewPagesJSONArray.length();){
                                ArrayList<String> x = new ArrayList<String>();
                                ArrayList<String> y = new ArrayList<String>();
                                if(CANDIDATE_HISTORY.getJSONArray("answers").length() != 0) {
                                    for (int j = 0; j < CANDIDATE_HISTORY.getJSONArray("answers").length(); ) {
                                        String answerText = CANDIDATE_HISTORY.getJSONArray("answers").getJSONObject(j).get("answer_text").toString();
                                        if (answerText.equals(reviewPagesJSONArray.getJSONObject(i).getString("question"))) {
                                            // this measn the text of the answer is the same as that which has been entered before so we need
                                            // to add the anser and the answer type

                                            String answerType = CANDIDATE_HISTORY.getJSONArray("answers").getJSONObject(j).get("answer_type").toString();
                                            if(answerType.equals("ac")){
                                                mTEXT_FOR_COMMENTS.add(CANDIDATE_HISTORY.getJSONArray("answers").getJSONObject(j).get("answer_text").toString());
                                            }

                                            if(answerType.equals("s")){
                                                mTEXT_FOR_SCOURS.add(CANDIDATE_HISTORY.getJSONArray("answers").getJSONObject(j).get("answer_text").toString());
                                            }

                                            if(answerType.equals("pi") || answerType.equals("ni")){
                                                x.add(answerType);
                                                y.add(answerText);
                                            }
                                        }
                                        j++;
                                    }
                                }

                                mANSWERS_TYPES.add(x);
                                mANSWERS_TEXT.add(y);
//                                mTEXT_FOR_SCOURS.add("");
//                                mTEXT_FOR_COMMENTS.add("");
                                i++;
                            }
//                            setViews();
                            mAdapterReview.notifyDataSetChanged();

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

        RequestQueue requestQueue = Volley.newRequestQueue(ACActivityReviewActivity.this);
        request_json.setShouldCache(false);
        requestQueue.add(request_json);// reload the list view
    }



    public  void submit(String answer_text, String answer_type,int question_number){
        // perfrom request
        String URL = "";
        if(appState.AC_CURRENT_ACTIVITY_TYPE.equals("interview")){
            URL = "http://"+ appState.ENDPOINT + "/v1/companies/assessmentcentres/interview/submitreview/"+appState.ACID;
        }else if (appState.AC_CURRENT_ACTIVITY_TYPE.equals("presentation")){
            URL = "http://"+ appState.ENDPOINT + "/v1/companies/assessmentcentres/presentation/submitreview/"+appState.ACID;
        }else if (appState.AC_CURRENT_ACTIVITY_TYPE.equals("roleplay")){
            URL = "http://"+ appState.ENDPOINT + "/v1/companies/assessmentcentres/roleplay/submitreview/"+appState.ACID;
        }

        Log.v("ADS", "URL is  .. " + URL);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("answer_text", answer_text);
        params.put("answer_type", answer_type);
        params.put("username", appState.USERNAME);
        params.put("passcode", appState.PASSCODE);
        params.put("candidate_id",appState.AC_CURRENT_CANDIDATE_ID);
        params.put("question_id", String.valueOf(question_number));
        params.put("ac_id", appState.ACID);

        JsonObjectRequest request_json = new JsonObjectRequest(URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(ACActivityReviewActivity.this);
        request_json.setShouldCache(false);
        requestQueue.add(request_json);// reload the list view
    }

    public void candidateCompletedActivity(){
        // perfrom request
        String  URL = "";
        if(appState.AC_CURRENT_ACTIVITY_TYPE.equals("interview")){
            URL = "http://"+ appState.ENDPOINT + "/v1/companies/assessmentcentres/interview/complete/"+appState.ACID;
        }else if (appState.AC_CURRENT_ACTIVITY_TYPE.equals("presentation")){
            URL = "http://"+ appState.ENDPOINT + "/v1/companies/assessmentcentres/presentation/complete/"+appState.ACID;
        }else if (appState.AC_CURRENT_ACTIVITY_TYPE.equals("roleplay")){
            URL = "http://"+ appState.ENDPOINT + "/v1/companies/assessmentcentres/roleplay/complete/"+appState.ACID;
        }

        Log.v("ADS", "URL is  .. " + URL);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", appState.USERNAME);
        params.put("passcode", appState.PASSCODE);
        params.put("candidate_id", appState.AC_CURRENT_CANDIDATE_ID);
        params.put("section_type", "i");
        params.put("ac_id", appState.ACID);

        JsonObjectRequest request_json = new JsonObjectRequest(URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(ACActivityReviewActivity.this);
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

    // SEEKBAR METHODS
    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    // SEEKBAR METHODS END

    public class MyAdapter extends RecyclerView.Adapter<ACActivityReviewActivity.MyAdapter.ViewHolder> {

        public MyAdapter(JSONArray testsJSONArray) {
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case

            // create all the needed view refernces for each button
            List<String> mCURRENT_NEGATIVE_INDICATORS = new ArrayList<String>();
            List<String> mCURRENT_POSITIVE_INDICATORS = new ArrayList<String>();

            public Button pq1;
            public Button pq2;
            public Button pq3;
            public Button pq4;
            public Button pq5;
            public Button pq6;

            public Button nq1;
            public Button nq2;
            public Button nq3;
            public Button nq4;
            public Button nq5;
            public Button nq6;


            public RadioButton p1;
            public RadioButton p2;
            public RadioButton p3;
            public RadioButton p4;
            public RadioButton p5;
            public RadioButton p6;

            public RadioButton n1;
            public RadioButton n2;
            public RadioButton n3;
            public RadioButton n4;
            public RadioButton n5;
            public RadioButton n6;


            //set review question
            public Button sd;
            public Button p0;
            public Button n0;

            public EditText comment;
            public  EditText scoure;

//            LinearLayout hud;
            public LinearLayout mediahud;
            public LinearLayout huds;

            CardView cv;
            ViewHolder(View itemView) {
                super(itemView);
                cv = (CardView)itemView.findViewById(R.id.review_card_view);

                p1 = (RadioButton) itemView.findViewById(R.id.pi_c1_r1);
                p2 = (RadioButton) itemView.findViewById(R.id.pi_c1_r2);
                p3 = (RadioButton) itemView.findViewById(R.id.pi_c1_r3);
                p4 = (RadioButton) itemView.findViewById(R.id.pi_c1_r4);
                p5 = (RadioButton) itemView.findViewById(R.id.pi_c1_r5);
                p6 = (RadioButton) itemView.findViewById(R.id.pi_c1_r6);

                pq1 = (Button) itemView.findViewById(R.id.pi_c0_r1);
                pq2 = (Button) itemView.findViewById(R.id.pi_c0_r2);
                pq3 = (Button) itemView.findViewById(R.id.pi_c0_r3);
                pq4 = (Button) itemView.findViewById(R.id.pi_c0_r4);
                pq5 = (Button) itemView.findViewById(R.id.pi_c0_r5);
                pq6 = (Button) itemView.findViewById(R.id.pi_c0_r6);

                n1 = (RadioButton) itemView.findViewById(R.id.pi_c3_r1);
                n2 = (RadioButton) itemView.findViewById(R.id.pi_c3_r2);
                n3 = (RadioButton) itemView.findViewById(R.id.pi_c3_r3);
                n4 = (RadioButton) itemView.findViewById(R.id.pi_c3_r4);
                n5 = (RadioButton) itemView.findViewById(R.id.pi_c3_r5);
                n6 = (RadioButton) itemView.findViewById(R.id.pi_c3_r6);

                nq1 = (Button) itemView.findViewById(R.id.pi_c2_r1);
                nq2 = (Button) itemView.findViewById(R.id.pi_c2_r2);
                nq3 = (Button) itemView.findViewById(R.id.pi_c2_r3);
                nq4 = (Button) itemView.findViewById(R.id.pi_c2_r4);
                nq5 = (Button) itemView.findViewById(R.id.pi_c2_r5);
                nq6 = (Button) itemView.findViewById(R.id.pi_c2_r6);

                sd = (Button) itemView.findViewById(R.id.review_question);
                n0 = (Button) itemView.findViewById(R.id.pi_c2_r0);
                p0 = (Button) itemView.findViewById(R.id.pi_c0_r0);

                comment= (EditText) itemView.findViewById(R.id.additional_comments);
                scoure = (EditText) itemView.findViewById(R.id.overall_score);

//                hud = (LinearLayout) findViewById(R.id.review_hud);
                mediahud = (LinearLayout) itemView.findViewById(R.id.questions_review_hud);
                huds = (LinearLayout) itemView.findViewById(R.id.review_hud);
//                itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override public void onClick(View v) {
//
//                    }
//                });

            }

            public ViewHolder(Button v , RadioButton m, EditText x) {
                super(v);

                pq1 = v;
                pq2 = v;
                pq3 = v;
                pq4 = v;
                pq5 = v;
                pq6 = v;

                nq1 = v;
                nq2 = v;
                nq3 = v;
                nq4 = v;
                nq5 = v;
                nq6 = v;

                sd = v;
                n0 = v;
                p0 = v;

                p1 = m;
                p2 = m;
                p3 = m;
                p4 = m;
                p5 = m;
                p6 = m;

                n1 = m;
                n2 = m;
                n3 = m;
                n4 = m;
                n5 = m;
                n6 = m;

                comment = x;
                scoure = x;

            }
        }

        @Override
        public ACActivityReviewActivity.MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_cardview, parent, false);

            // set the view's size, margins, paddings and layout parameters
            ACActivityReviewActivity.MyAdapter.ViewHolder vh = new ACActivityReviewActivity.MyAdapter.ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final ACActivityReviewActivity.MyAdapter.ViewHolder holder, final int position) {

            JSONObject n = new JSONObject();
            try {
                n = (JSONObject) reviewPagesJSONArray.get(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String review_question = "";
            String positive_indicators;
            String negative_indicators;

            try {

                 review_question = n.getString("review_question");
                 positive_indicators = n.getString("positive_indicators");
                 negative_indicators = n.getString("negative_indicators");
                 holder.mCURRENT_POSITIVE_INDICATORS = Arrays.asList(positive_indicators.split("@&@")); //positive_indicators.split("@&@");
                 holder.mCURRENT_NEGATIVE_INDICATORS = Arrays.asList(negative_indicators.split("@&@"));


            } catch (JSONException e) {
                e.printStackTrace();
            }

            holder.p0.setWidth(500);
            holder.n0.setWidth(500);

            //set review question
            holder.sd.setText(review_question);
            // set all pos indicators
            holder.pq1.setText(holder.mCURRENT_POSITIVE_INDICATORS.get(0));
            holder.pq1.setWidth(500);

            holder.pq2.setText(holder.mCURRENT_POSITIVE_INDICATORS.get(1));
            holder.pq2.setWidth(500);
            holder.pq3.setText(holder.mCURRENT_POSITIVE_INDICATORS.get(2));
            holder.pq3.setWidth(500);
            holder.pq4.setText(holder.mCURRENT_POSITIVE_INDICATORS.get(3));
            holder.pq4.setWidth(500);
            holder.pq5.setText(holder.mCURRENT_POSITIVE_INDICATORS.get(4));
            holder.pq5.setWidth(500);
            holder.pq6.setText(holder.mCURRENT_POSITIVE_INDICATORS.get(5));
            holder.pq6.setWidth(500);

            // set all negaive indicators
            holder.nq1.setText(holder.mCURRENT_NEGATIVE_INDICATORS.get(0));
            holder.nq1.setWidth(500);
            holder.nq2.setText(holder.mCURRENT_NEGATIVE_INDICATORS.get(1));
            holder.nq2.setWidth(500);
            holder.nq3.setText(holder.mCURRENT_NEGATIVE_INDICATORS.get(2));
            holder.nq3.setWidth(500);
            holder.nq4.setText(holder.mCURRENT_NEGATIVE_INDICATORS.get(3));
            holder.nq4.setWidth(500);
            holder.nq5.setText(holder.mCURRENT_NEGATIVE_INDICATORS.get(4));
            holder.nq5.setWidth(500);
            holder.nq6.setText(holder.mCURRENT_NEGATIVE_INDICATORS.get(5));
            holder.nq6.setWidth(500);

            if(mANSWERS_TEXT.get(position).contains(holder.mCURRENT_POSITIVE_INDICATORS.get(0))){ holder.p1.setChecked(true);}else{holder.p1.setChecked(false); }
            if(mANSWERS_TEXT.get(position).contains(holder.mCURRENT_POSITIVE_INDICATORS.get(1))){ holder.p2.setChecked(true);}else{holder.p2.setChecked(false); }
            if(mANSWERS_TEXT.get(position).contains(holder.mCURRENT_POSITIVE_INDICATORS.get(2))){ holder.p3.setChecked(true);}else{holder.p3.setChecked(false); }
            if(mANSWERS_TEXT.get(position).contains(holder.mCURRENT_POSITIVE_INDICATORS.get(3))){ holder.p4.setChecked(true);}else{holder.p4.setChecked(false); }
            if(mANSWERS_TEXT.get(position).contains(holder.mCURRENT_POSITIVE_INDICATORS.get(4))){ holder.p5.setChecked(true);}else{holder.p5.setChecked(false); }
            if(mANSWERS_TEXT.get(position).contains(holder.mCURRENT_POSITIVE_INDICATORS.get(5))){ holder.p6.setChecked(true);}else{holder.p6.setChecked(false); }

            if(mANSWERS_TEXT.get(position).contains(holder.mCURRENT_NEGATIVE_INDICATORS.get(0))){ holder.n1.setChecked(true);}else{holder.n1.setChecked(false); }
            if(mANSWERS_TEXT.get(position).contains(holder.mCURRENT_NEGATIVE_INDICATORS.get(1))){ holder.n2.setChecked(true);}else{holder.n2.setChecked(false); }
            if(mANSWERS_TEXT.get(position).contains(holder.mCURRENT_NEGATIVE_INDICATORS.get(2))){ holder.n3.setChecked(true);}else{holder.n3.setChecked(false); }
            if(mANSWERS_TEXT.get(position).contains(holder.mCURRENT_NEGATIVE_INDICATORS.get(3))){ holder.n4.setChecked(true);}else{holder.n4.setChecked(false); }
            if(mANSWERS_TEXT.get(position).contains(holder.mCURRENT_NEGATIVE_INDICATORS.get(4))){ holder.n5.setChecked(true);}else{holder.n5.setChecked(false); }
            if(mANSWERS_TEXT.get(position).contains(holder.mCURRENT_NEGATIVE_INDICATORS.get(5))){ holder.n6.setChecked(true);}else{holder.n6.setChecked(false); }
//
//            if(mANSWERS_TEXT.get(position).contains(holder.comment.getText().toString())){ holder.comment}
            holder.comment.setText(mTEXT_FOR_COMMENTS.get(position));
            holder.scoure.setText(mTEXT_FOR_SCOURS.get(position));
            holder.comment.setTag("c" + String.valueOf(position));
            holder.scoure.setTag("s" + String.valueOf(position));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                holder.sd.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);

                holder.pq1.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                holder.pq2.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                holder.pq3.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                holder.pq4.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                holder.pq5.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                holder.pq6.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);

                holder.nq1.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                holder.nq2.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                holder.nq3.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                holder.nq4.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                holder.nq5.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                holder.nq6.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
            }


            // add all anserrs to this object answersForAllReviewQuestions and remove them if the uer deselects

            holder.p1.setOnClickListener(new View.OnClickListener() { public void onClick(View v) { String mselectetion = holder.pq1.getText().toString();
                    if(mANSWERS_TEXT.get(position).contains(mselectetion)){RadioButton x = (RadioButton)v; x.setChecked(false); int indexOfObjectToRemove = mANSWERS_TEXT.get(position).indexOf(mselectetion); mANSWERS_TEXT.get(position).remove(indexOfObjectToRemove); mANSWERS_TYPES.get(position).remove(indexOfObjectToRemove);}else{mANSWERS_TEXT.get(position).add(mselectetion); mANSWERS_TYPES.get(position).add("pi");}
                }});

            holder.p2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) { String mselectetion = holder.pq2.getText().toString();
                    if(mANSWERS_TEXT.get(position).contains(mselectetion)){RadioButton x = (RadioButton)v; x.setChecked(false); int indexOfObjectToRemove = mANSWERS_TEXT.get(position).indexOf(mselectetion); mANSWERS_TEXT.get(position).remove(indexOfObjectToRemove); mANSWERS_TYPES.get(position).remove(indexOfObjectToRemove);}else{mANSWERS_TEXT.get(position).add(mselectetion); mANSWERS_TYPES.get(position).add("pi");} }
            });

            holder.p3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) { String mselectetion = holder.pq3.getText().toString();
                    if(mANSWERS_TEXT.get(position).contains(mselectetion)){RadioButton x = (RadioButton)v; x.setChecked(false); int indexOfObjectToRemove = mANSWERS_TEXT.get(position).indexOf(mselectetion); mANSWERS_TEXT.get(position).remove(indexOfObjectToRemove); mANSWERS_TYPES.get(position).remove(indexOfObjectToRemove);}else{mANSWERS_TEXT.get(position).add(mselectetion); mANSWERS_TYPES.get(position).add("pi");} }
            });

            holder.p4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) { String mselectetion = holder.pq4.getText().toString();
                    if(mANSWERS_TEXT.get(position).contains(mselectetion)){RadioButton x = (RadioButton)v; x.setChecked(false); int indexOfObjectToRemove = mANSWERS_TEXT.get(position).indexOf(mselectetion); mANSWERS_TEXT.get(position).remove(indexOfObjectToRemove); mANSWERS_TYPES.get(position).remove(indexOfObjectToRemove);}else{mANSWERS_TEXT.get(position).add(mselectetion); mANSWERS_TYPES.get(position).add("pi");} }
            });

            holder.p5.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) { String mselectetion = holder.pq5.getText().toString();
                    if(mANSWERS_TEXT.get(position).contains(mselectetion)){RadioButton x = (RadioButton)v; x.setChecked(false); int indexOfObjectToRemove = mANSWERS_TEXT.get(position).indexOf(mselectetion); mANSWERS_TEXT.get(position).remove(indexOfObjectToRemove); mANSWERS_TYPES.get(position).remove(indexOfObjectToRemove);}else{mANSWERS_TEXT.get(position).add(mselectetion); mANSWERS_TYPES.get(position).add("pi");} }
            });

            holder.p6.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) { String mselectetion = holder.pq6.getText().toString();
                    if(mANSWERS_TEXT.get(position).contains(mselectetion)){RadioButton x = (RadioButton)v; x.setChecked(false); int indexOfObjectToRemove = mANSWERS_TEXT.get(position).indexOf(mselectetion); mANSWERS_TEXT.get(position).remove(indexOfObjectToRemove); mANSWERS_TYPES.get(position).remove(indexOfObjectToRemove);}else{mANSWERS_TEXT.get(position).add(mselectetion); mANSWERS_TYPES.get(position).add("pi");} }
            });


            holder.n1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) { String mselectetion = holder.nq1.getText().toString();
                    if(mANSWERS_TEXT.get(position).contains(mselectetion)){RadioButton x = (RadioButton)v; x.setChecked(false); int indexOfObjectToRemove = mANSWERS_TEXT.get(position).indexOf(mselectetion); mANSWERS_TEXT.get(position).remove(indexOfObjectToRemove); mANSWERS_TYPES.get(position).remove(indexOfObjectToRemove);}else{mANSWERS_TEXT.get(position).add(mselectetion); mANSWERS_TYPES.get(position).add("ni");} }
            });

            holder.n2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) { String mselectetion = holder.nq2.getText().toString();
                    if(mANSWERS_TEXT.get(position).contains(mselectetion)){RadioButton x = (RadioButton)v; x.setChecked(false); int indexOfObjectToRemove = mANSWERS_TEXT.get(position).indexOf(mselectetion); mANSWERS_TEXT.get(position).remove(indexOfObjectToRemove); mANSWERS_TYPES.get(position).remove(indexOfObjectToRemove);}else{mANSWERS_TEXT.get(position).add(mselectetion); mANSWERS_TYPES.get(position).add("ni");} }
            });

            holder.n3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) { String mselectetion = holder.nq3.getText().toString();
                    if(mANSWERS_TEXT.get(position).contains(mselectetion)){RadioButton x = (RadioButton)v; x.setChecked(false); int indexOfObjectToRemove = mANSWERS_TEXT.get(position).indexOf(mselectetion); mANSWERS_TEXT.get(position).remove(indexOfObjectToRemove); mANSWERS_TYPES.get(position).remove(indexOfObjectToRemove);}else{mANSWERS_TEXT.get(position).add(mselectetion); mANSWERS_TYPES.get(position).add("ni");} }
            });
            holder.n4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) { String mselectetion = holder.nq4.getText().toString();
                    if(mANSWERS_TEXT.get(position).contains(mselectetion)){RadioButton x = (RadioButton)v; x.setChecked(false); int indexOfObjectToRemove = mANSWERS_TEXT.get(position).indexOf(mselectetion); mANSWERS_TEXT.get(position).remove(indexOfObjectToRemove); mANSWERS_TYPES.get(position).remove(indexOfObjectToRemove);}else{mANSWERS_TEXT.get(position).add(mselectetion); mANSWERS_TYPES.get(position).add("ni");} }
            });

            holder.n5.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) { String mselectetion = holder.nq5.getText().toString();
                    if(mANSWERS_TEXT.get(position).contains(mselectetion)){RadioButton x = (RadioButton)v; x.setChecked(false); int indexOfObjectToRemove = mANSWERS_TEXT.get(position).indexOf(mselectetion); mANSWERS_TEXT.get(position).remove(indexOfObjectToRemove); mANSWERS_TYPES.get(position).remove(indexOfObjectToRemove);}else{mANSWERS_TEXT.get(position).add(mselectetion); mANSWERS_TYPES.get(position).add("ni");} }
            });

            holder.n6.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) { String mselectetion = holder.nq6.getText().toString();
                    if(mANSWERS_TEXT.get(position).contains(mselectetion)){RadioButton x = (RadioButton)v; x.setChecked(false); int indexOfObjectToRemove = mANSWERS_TEXT.get(position).indexOf(mselectetion); mANSWERS_TEXT.get(position).remove(indexOfObjectToRemove); mANSWERS_TYPES.get(position).remove(indexOfObjectToRemove);}else{mANSWERS_TEXT.get(position).add(mselectetion); mANSWERS_TYPES.get(position).add("ni");} }
            });

            holder.comment.addTextChangedListener(new TextWatcher() {
                String curentText = holder.comment.getText().toString();
                @Override
                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(mANSWERS_TEXT.get(position).contains(curentText)) {
                        int indexOfObjectToRemove = mANSWERS_TEXT.get(position).indexOf(curentText);
                        mANSWERS_TEXT.get(position).remove(indexOfObjectToRemove);
                        mANSWERS_TYPES.get(position).remove(indexOfObjectToRemove);
                        Log.e("ADS", "STRING OF comment IS +>> " + s.toString());
                        mANSWERS_TEXT.get(position).add(s.toString());
                        mANSWERS_TYPES.get(position).add("ac");
                        // add comment string
                    }else{
                        mANSWERS_TEXT.get(position).add(s.toString());
                        mANSWERS_TYPES.get(position).add("ac");
                    }
                }
            });

            holder.scoure.addTextChangedListener(new TextWatcher() {
                String curentText = holder.scoure.getText().toString();
                @Override
                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(mANSWERS_TEXT.get(position).contains(curentText)){
                    int indexOfObjectToRemove = mANSWERS_TEXT.get(position).indexOf(curentText);
                    mANSWERS_TEXT.get(position).remove(indexOfObjectToRemove);
                    mANSWERS_TYPES.get(position).remove(indexOfObjectToRemove);
                    Log.e("ADS","STRING OF comment IS +>> " + s.toString());
                    mANSWERS_TEXT.get(position).add(s.toString());
                    mANSWERS_TYPES.get(position).add("s");
                    // add comment strin

                    }else{
                        mANSWERS_TEXT.get(position).add(s.toString());
                        mANSWERS_TYPES.get(position).add("s");
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return  reviewPagesJSONArray.length();
        }

    }



    public class MyReviewAdapter extends RecyclerView.Adapter<ACActivityReviewActivity.MyReviewAdapter.ViewHolder> {

        public MyReviewAdapter(JSONArray j) {
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            CardView cv;
            public TextView info1;
            public TextView info2;
            public TextView info3;

            ViewHolder(View itemView) {
                super(itemView);
                cv = (CardView)itemView.findViewById(R.id.card_view);
                info1 = (TextView)itemView.findViewById(R.id.info_text_1);
                info2 = (TextView)itemView.findViewById(R.id.info_text_2);
                info3 = (TextView)itemView.findViewById(R.id.info_text_3);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        // item clicked
                        int pos = getAdapterPosition();
                        Log.e("ADS","Tapped index is : " + pos);

                        JSONObject n = new JSONObject();
                        try {
                            n = (JSONObject) audioBlobsJSONARRAY.get(pos);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // revisited when returned from get particiapnts is defined
                        String question_number = null;
                        String length = null;
                        String filename = null;

                        try {
                            question_number = n.getString("question_id");
                            length = n.getString("length");
                            filename = n.getString("filename");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // do whats needed inthe player department
                        mTAPPED_AUDIO_FILE_NAME = filename;
                        Log.e("ADS","FILEPATH ============ " + mTAPPED_AUDIO_FILE_NAME);
                        onPlay(true);
                    }
                });

            }

            public ViewHolder(TextView v) {
                super(v);
                info1 = v;
                info2 = v;
                info3 = v;
            }
        }

        @Override
        public ACActivityReviewActivity.MyReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ac_cardview, parent, false);

            // set the view's size, margins, paddings and layout parameters
            ACActivityReviewActivity.MyReviewAdapter.ViewHolder vh = new ACActivityReviewActivity.MyReviewAdapter.ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ACActivityReviewActivity.MyReviewAdapter.ViewHolder holder, int position) {

            JSONObject n = new JSONObject();
            try {
                n = (JSONObject) audioBlobsJSONARRAY.get(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String question_number = null;
            String length = null;
            String filename = null;

            try {
                question_number = n.getString("question_id");
                length = n.getString("length");
                filename = n.getString("filename");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            holder.info1.setText("Audio from Question "+ question_number);
            holder.info2.setText(filename);
            holder.info3.setText(length);
        }

        @Override
        public int getItemCount() {
            return  audioBlobsJSONARRAY.length();
        }

    }

}



//
// package com.apptitudedigitalsolutions.ads;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.PorterDuff;
//import android.media.MediaPlayer;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.support.v7.widget.CardView;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.RadioButton;
//import android.widget.TextView;
//
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.VolleyLog;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//
//import com.apptitudedigitalsolutions.ads.database.DatabaseHelper;
//
//import static android.view.View.TEXT_ALIGNMENT_TEXT_START;
//
///**
// * Created by Elliot on 20/11/2016.
// */
//
//public class ACActivityReviewActivity extends Activity {
//    String msg = "Android : ";
//    ArrayList<String> mANSWERS_TEXT = new ArrayList<String>();
//    ArrayList<String> mANSWERS_TYPES = new ArrayList<String>();
//    DatabaseHelper db;
//    private String appState.USERNAME = "";
//    private String appState.PASSCODE = "";
//    private String appState.ENDPOINT = "";
//    private String appState.COMPANY_ID = "";
//
//    private RecyclerView mRecyclerView;
//    private RecyclerView.Adapter mAdapter;
//    private RecyclerView.LayoutManager mLayoutManager;
//
//    boolean mPLAYING = false;
//
//    private MediaPlayer mPlayer = null;
//
//    String appState.AC_TITLE ="";
//    String appState.AC_CONDUCTED_ON="";
//    String appState.AC_DESC="";
//    String appState.AC_ACTIVITY_IDS="";
//    String appState.AC_ACTIVITY_TYPES="";
//
//    String mTAPPED_AUDIO_FILE_NAME;
//
//    public List<String> mCURRENT_POSITIVE_INDICATORS = new ArrayList<String>();
//    public List<String> mCURRENT_NEGATIVE_INDICATORS = new ArrayList<String>();
//
//    String appState.ACID;
//    String appState.AC_CURRENT_CANDIDATE_ID;
//    int mCURRENT_QUESTION_NUMBER;
//    JSONArray reviewPagesJSONArray = new JSONArray();
//    int mREVIEW_LENGTH;
//    int mCURRENT_REVIEW_PAGE_POINTER_INDEX = 0;
//    int mSHOULD_RETRUN_TO_ACMAIN = 0;
//    String appState.AC_SET_ACTIVITIE_TAGS;
//    String appState.AC_COMPLETED_ACTIVITIES;
//    JSONArray audioBlobsJSONARRAY =new JSONArray();
//    /**
//     * Called when the activity is first created.
//     */
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_interview_review);
//        Log.d(msg, "The onCreate() event");
//        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//
//        db = new DatabaseHelper(ACActivityReviewActivity.this);
//        db.getReadableDatabase();
//        ArrayList<String> userdata = db.getUsernamePasscodeEndpointArray();
//        appState.USERNAME = userdata.get(4);
//        appState.PASSCODE = userdata.get(2);
//        appState.ENDPOINT = userdata.get(0);
//        appState.COMPANY_ID = userdata.get(1);
//
//        // get values from intent and parse into usage objects
//        Intent intent = getIntent();
//        appState.AC_CURRENT_CANDIDATE_ID= intent.getStringExtra("candidate_id");
//        appState.ACID = intent.getStringExtra("ac_id");
//
//        appState.AC_TITLE = intent.getStringExtra("title");
//        appState.AC_CONDUCTED_ON = intent.getStringExtra("to_be_conducted_on");
//        appState.AC_DESC = intent.getStringExtra("description");
//        appState.AC_ACTIVITY_IDS = intent.getStringExtra("activity_ids");
//        appState.AC_ACTIVITY_TYPES = intent.getStringExtra("activity_types");
//        appState.AC_SET_ACTIVITIE_TAGS= intent.getStringExtra("set_activitie_tags");
//        appState.AC_COMPLETED_ACTIVITIES= intent.getStringExtra("completed_activities");
//
//
//        // create all the needed view refernces for each button
//
//        RadioButton p1 = (RadioButton) findViewById(R.id.pi_c1_r1);
//        p1.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) { String mselectetion = mCURRENT_POSITIVE_INDICATORS.get(0);
//        if(mANSWERS_TEXT.contains(mselectetion)){RadioButton x = (RadioButton)v; x.setChecked(false); int indexOfObjectToRemove = mANSWERS_TEXT.indexOf(mselectetion); mANSWERS_TEXT.remove(indexOfObjectToRemove); mANSWERS_TYPES.remove(indexOfObjectToRemove);}else{mANSWERS_TEXT.add(mselectetion); mANSWERS_TYPES.add("pi");} }
//        });
//
//        RadioButton p2 = (RadioButton) findViewById(R.id.pi_c1_r2);
//        p2.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) { String mselectetion = mCURRENT_POSITIVE_INDICATORS.get(1);
//                if(mANSWERS_TEXT.contains(mselectetion)){RadioButton x = (RadioButton)v; x.setChecked(false); int indexOfObjectToRemove = mANSWERS_TEXT.indexOf(mselectetion); mANSWERS_TEXT.remove(indexOfObjectToRemove); mANSWERS_TYPES.remove(indexOfObjectToRemove);}else{mANSWERS_TEXT.add(mselectetion); mANSWERS_TYPES.add("pi");} }
//        });
//
//        RadioButton p3 = (RadioButton) findViewById(R.id.pi_c1_r3);
//        p3.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) { String mselectetion = mCURRENT_POSITIVE_INDICATORS.get(2);
//                if(mANSWERS_TEXT.contains(mselectetion)){RadioButton x = (RadioButton)v; x.setChecked(false); int indexOfObjectToRemove = mANSWERS_TEXT.indexOf(mselectetion); mANSWERS_TEXT.remove(indexOfObjectToRemove); mANSWERS_TYPES.remove(indexOfObjectToRemove);}else{mANSWERS_TEXT.add(mselectetion); mANSWERS_TYPES.add("pi");} }
//        });
//
//        RadioButton p4 = (RadioButton) findViewById(R.id.pi_c1_r4);
//        p4.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) { String mselectetion = mCURRENT_POSITIVE_INDICATORS.get(3);
//                if(mANSWERS_TEXT.contains(mselectetion)){RadioButton x = (RadioButton)v; x.setChecked(false); int indexOfObjectToRemove = mANSWERS_TEXT.indexOf(mselectetion); mANSWERS_TEXT.remove(indexOfObjectToRemove); mANSWERS_TYPES.remove(indexOfObjectToRemove);}else{mANSWERS_TEXT.add(mselectetion); mANSWERS_TYPES.add("pi");} }
//        });
//
//        RadioButton p5 = (RadioButton) findViewById(R.id.pi_c1_r5);
//        p5.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) { String mselectetion = mCURRENT_POSITIVE_INDICATORS.get(4);
//                if(mANSWERS_TEXT.contains(mselectetion)){RadioButton x = (RadioButton)v; x.setChecked(false); int indexOfObjectToRemove = mANSWERS_TEXT.indexOf(mselectetion); mANSWERS_TEXT.remove(indexOfObjectToRemove); mANSWERS_TYPES.remove(indexOfObjectToRemove);}else{mANSWERS_TEXT.add(mselectetion); mANSWERS_TYPES.add("pi");} }
//        });
//
//        RadioButton p6 = (RadioButton) findViewById(R.id.pi_c1_r6);
//        p6.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) { String mselectetion = mCURRENT_POSITIVE_INDICATORS.get(5);
//                if(mANSWERS_TEXT.contains(mselectetion)){RadioButton x = (RadioButton)v; x.setChecked(false); int indexOfObjectToRemove = mANSWERS_TEXT.indexOf(mselectetion); mANSWERS_TEXT.remove(indexOfObjectToRemove); mANSWERS_TYPES.remove(indexOfObjectToRemove);}else{mANSWERS_TEXT.add(mselectetion); mANSWERS_TYPES.add("pi");} }
//        });
//
//
//
//
//
//
//
//
//        RadioButton n1 = (RadioButton) findViewById(R.id.pi_c3_r1);
//        n1.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) { String mselectetion = mCURRENT_NEGATIVE_INDICATORS.get(0);
//                if(mANSWERS_TEXT.contains(mselectetion)){RadioButton x = (RadioButton)v; x.setChecked(false); int indexOfObjectToRemove = mANSWERS_TEXT.indexOf(mselectetion); mANSWERS_TEXT.remove(indexOfObjectToRemove); mANSWERS_TYPES.remove(indexOfObjectToRemove);}else{mANSWERS_TEXT.add(mselectetion); mANSWERS_TYPES.add("pi");} }
//        });
//
//        RadioButton n2 = (RadioButton) findViewById(R.id.pi_c3_r2);
//        n2.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) { String mselectetion = mCURRENT_NEGATIVE_INDICATORS.get(1);
//                if(mANSWERS_TEXT.contains(mselectetion)){RadioButton x = (RadioButton)v; x.setChecked(false); int indexOfObjectToRemove = mANSWERS_TEXT.indexOf(mselectetion); mANSWERS_TEXT.remove(indexOfObjectToRemove); mANSWERS_TYPES.remove(indexOfObjectToRemove);}else{mANSWERS_TEXT.add(mselectetion); mANSWERS_TYPES.add("pi");} }
//        });
//
//        RadioButton n3 = (RadioButton) findViewById(R.id.pi_c3_r3);
//        n3.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) { String mselectetion = mCURRENT_NEGATIVE_INDICATORS.get(2);
//                if(mANSWERS_TEXT.contains(mselectetion)){RadioButton x = (RadioButton)v; x.setChecked(false); int indexOfObjectToRemove = mANSWERS_TEXT.indexOf(mselectetion); mANSWERS_TEXT.remove(indexOfObjectToRemove); mANSWERS_TYPES.remove(indexOfObjectToRemove);}else{mANSWERS_TEXT.add(mselectetion); mANSWERS_TYPES.add("pi");} }
//        });
//
//        RadioButton n4 = (RadioButton) findViewById(R.id.pi_c3_r4);
//        n4.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) { String mselectetion = mCURRENT_NEGATIVE_INDICATORS.get(3);
//                if(mANSWERS_TEXT.contains(mselectetion)){RadioButton x = (RadioButton)v; x.setChecked(false); int indexOfObjectToRemove = mANSWERS_TEXT.indexOf(mselectetion); mANSWERS_TEXT.remove(indexOfObjectToRemove); mANSWERS_TYPES.remove(indexOfObjectToRemove);}else{mANSWERS_TEXT.add(mselectetion); mANSWERS_TYPES.add("pi");} }
//        });
//
//        RadioButton n5 = (RadioButton) findViewById(R.id.pi_c3_r5);
//        n5.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) { String mselectetion = mCURRENT_NEGATIVE_INDICATORS.get(4);
//                if(mANSWERS_TEXT.contains(mselectetion)){RadioButton x = (RadioButton)v; x.setChecked(false); int indexOfObjectToRemove = mANSWERS_TEXT.indexOf(mselectetion); mANSWERS_TEXT.remove(indexOfObjectToRemove); mANSWERS_TYPES.remove(indexOfObjectToRemove);}else{mANSWERS_TEXT.add(mselectetion); mANSWERS_TYPES.add("pi");} }
//        });
//
//        RadioButton n6 = (RadioButton) findViewById(R.id.pi_c3_r6);
//        n6.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) { String mselectetion = mCURRENT_NEGATIVE_INDICATORS.get(5);
//                if(mANSWERS_TEXT.contains(mselectetion)){RadioButton x = (RadioButton)v; x.setChecked(false); int indexOfObjectToRemove = mANSWERS_TEXT.indexOf(mselectetion); mANSWERS_TEXT.remove(indexOfObjectToRemove); mANSWERS_TYPES.remove(indexOfObjectToRemove);}else{mANSWERS_TEXT.add(mselectetion); mANSWERS_TYPES.add("pi");} }
//        });
//
//
//
//
//
//        // set up on click behaviour for each button that adds the associated text in to the array of answers_text
//
//        // get a list of review questions
//        getInterviewReviewPages();
//        audioBlobsJSONARRAY = db.getAllAudioFileReferences(appState.AC_CURRENT_CANDIDATE_ID);
//
//        // create all of that adapter shit
//        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_review);
//
//        // use this setting to improve performance if you know that changes
//        // in content do not change the layout size of the RecyclerView
//        mRecyclerView.setHasFixedSize(true);
//        // use a linear layout manager
//        mLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//
//        mAdapter = new ACActivityReviewActivity.MyAdapter(audioBlobsJSONARRAY);
//        mRecyclerView.setAdapter(mAdapter);
//
//        Log.e("ADS", "AUDO BLOBS >>>>>>> " + audioBlobsJSONARRAY.toString());
//
//    }
//
//    public  void pausePlaySelection(View v){
//        if(mPLAYING == true){
//            stopPlaying();
//        }else {
//            startPlaying();
//        }
//    }
//
//    private void onPlay(boolean start) {
//        if (start) {
//            startPlaying();
//            mPLAYING = true;
//        } else {
//
//            stopPlaying();
//            mPLAYING = false;
//        }
//    }
//
//    private void startPlaying() {
//        mPlayer = new MediaPlayer();
//        try {
//            mPlayer.setDataSource(mTAPPED_AUDIO_FILE_NAME);
//            mPlayer.prepare();
//            mPlayer.start();
//        } catch (IOException e) {
//            Log.e("ADS", "prepare() failed");
//        }
//    }
//
//    private void stopPlaying() {
//        mPlayer.release();
//        mPlayer = null;
//    }
//
//    public void submitReview(View v){
//        // itterate over the array of objects mANSWERS_TEXT and then pass credetials to submit method
//        mCURRENT_REVIEW_PAGE_POINTER_INDEX++;
//        if(mCURRENT_REVIEW_PAGE_POINTER_INDEX == mREVIEW_LENGTH){
//            // chnage the button text to send and end
//            mSHOULD_RETRUN_TO_ACMAIN =1;
//            Button a = (Button) findViewById(R.id.submit);
//            a.setText("Submit and return to AC");
//        }
//        RadioButton p1 = (RadioButton) findViewById(R.id.pi_c1_r1); p1.setChecked(false);
//        RadioButton p2 = (RadioButton) findViewById(R.id.pi_c1_r2); p2.setChecked(false);
//        RadioButton p3 = (RadioButton) findViewById(R.id.pi_c1_r3); p3.setChecked(false);
//        RadioButton p4 = (RadioButton) findViewById(R.id.pi_c1_r4); p4.setChecked(false);
//        RadioButton p5 = (RadioButton) findViewById(R.id.pi_c1_r5); p5.setChecked(false);
//        RadioButton p6 = (RadioButton) findViewById(R.id.pi_c1_r6); p6.setChecked(false);
//
//        RadioButton n1 = (RadioButton) findViewById(R.id.pi_c3_r1); n1.setChecked(false);
//        RadioButton n2 = (RadioButton) findViewById(R.id.pi_c3_r2); n2.setChecked(false);
//        RadioButton n3 = (RadioButton) findViewById(R.id.pi_c3_r3); n3.setChecked(false);
//        RadioButton n4 = (RadioButton) findViewById(R.id.pi_c3_r4); n4.setChecked(false);
//        RadioButton n5 = (RadioButton) findViewById(R.id.pi_c3_r5); n5.setChecked(false);
//        RadioButton n6 = (RadioButton) findViewById(R.id.pi_c3_r6); n6.setChecked(false);
//
//        for(int i = 0; i < mANSWERS_TEXT.size();){
//            submit(mANSWERS_TEXT.get(i),mANSWERS_TYPES.get(i),mCURRENT_QUESTION_NUMBER);
//            i++;
//        }
//
//        mANSWERS_TEXT.clear();
//        mANSWERS_TYPES.clear();
//
//        EditText et = (EditText) findViewById(R.id.additional_comments);
//        String text = et.getText().toString();
//
//        if(text.length() > 0){
//            submit(text,"ac",mCURRENT_QUESTION_NUMBER);
//        }
//        et.setText("");
//
//        EditText scoure = (EditText) findViewById(R.id.overall_score);
//        String scouretext = scoure.getText().toString();
//
//
//        if(scouretext.length() > 0){
//            submit(scouretext,"s",mCURRENT_QUESTION_NUMBER);
//        }
//        scoure.setText("");
//
//        View view = this.getCurrentFocus();
//        if (view != null) {
//            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }
//        setViews();
//    }
//
//
//    public void getInterviewReviewPages(){
//
//        // retrieve the list of participats
//        String URL = "http://"+ appState.ENDPOINT + "/v1/companies/assessmentcentres/interview/review/"+appState.ACID;
//        Log.v("ADS", "URL is  .. " + URL);
//        HashMap<String, String> params = new HashMap<String, String>();
//        params.put("username", appState.USERNAME);
//        params.put("passcode", appState.PASSCODE);
//        params.put("ac_id", appState.ACID);
//
//        JsonObjectRequest request_json = new JsonObjectRequest(URL, new JSONObject(params),
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        try {
//                            reviewPagesJSONArray = response.getJSONArray("pages");
//                            mREVIEW_LENGTH = reviewPagesJSONArray.length()-1;
//
//                            setViews();
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.e("Error: ", error.getMessage());
//            }
//        });
//
//        RequestQueue requestQueue = Volley.newRequestQueue(ACActivityReviewActivity.this);
//        request_json.setShouldCache(false);
//        requestQueue.add(request_json);// reload the list view
//    }
//
//
//
//    public  void submit(String answer_text, String answer_type,int question_number){
//        // perfrom request
//        String URL = "http://"+ appState.ENDPOINT + "/v1/companies/assessmentcentres/interview/submitreview/"+appState.ACID;
//        Log.v("ADS", "URL is  .. " + URL);
//        HashMap<String, String> params = new HashMap<String, String>();
//        params.put("answer_text", answer_text);
//        params.put("answer_type", answer_type);
//        params.put("username", appState.USERNAME);
//        params.put("passcode", appState.PASSCODE);
//        params.put("candidate_id",appState.AC_CURRENT_CANDIDATE_ID);
//        params.put("question_id", String.valueOf(question_number));
//        params.put("ac_id", appState.ACID);
//
//        JsonObjectRequest request_json = new JsonObjectRequest(URL, new JSONObject(params),
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.e("Error: ", error.getMessage());
//            }
//        });
//
//        RequestQueue requestQueue = Volley.newRequestQueue(ACActivityReviewActivity.this);
//        request_json.setShouldCache(false);
//        requestQueue.add(request_json);// reload the list view
//
//        if(mSHOULD_RETRUN_TO_ACMAIN ==1){
//            // send acitvity completed request
//            candidateCompletedInterview(appState.AC_CURRENT_CANDIDATE_ID);
//            // return to main
//            Intent intent = new Intent(ACActivityReviewActivity.this, ACAdminActivity.class);
//            //
//            intent.putExtra("id", appState.ACID);
//            intent.putExtra("title", appState.AC_TITLE);
//            intent.putExtra("to_be_conducted_on", appState.AC_CONDUCTED_ON);
//            intent.putExtra("description", appState.AC_DESC);
//            intent.putExtra("activity_ids", appState.AC_ACTIVITY_IDS);
//            intent.putExtra("activity_types", appState.AC_ACTIVITY_TYPES);
//            intent.putExtra("set_activitie_tags",appState.AC_SET_ACTIVITIE_TAGS);
//            intent.putExtra("completed_activities",appState.AC_COMPLETED_ACTIVITIES);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//
//        }
//    }
//
//    public void candidateCompletedInterview(String appState.AC_CURRENT_CANDIDATE_ID){
//        // perfrom request
//        String URL = "http://"+ appState.ENDPOINT + "/v1/companies/assessmentcentres/interview/complete/"+appState.ACID;
//        Log.v("ADS", "URL is  .. " + URL);
//        HashMap<String, String> params = new HashMap<String, String>();
//        params.put("username", appState.USERNAME);
//        params.put("passcode", appState.PASSCODE);
//        params.put("candidate_id", appState.AC_CURRENT_CANDIDATE_ID);
//        params.put("section_type", "i");
//        params.put("ac_id", appState.ACID);
//
//        JsonObjectRequest request_json = new JsonObjectRequest(URL, new JSONObject(params),
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.e("Error: ", error.getMessage());
//            }
//        });
//
//        RequestQueue requestQueue = Volley.newRequestQueue(ACActivityReviewActivity.this);
//        request_json.setShouldCache(false);
//        requestQueue.add(request_json);// reload the list view
//    }
//
//    /**
//     * Called when the activity is about to become visible.
//     */
//    @Override
//    protected void onStart() {
//        super.onStart();
//        Log.d(msg, "The onStart() event");
//    }
//
//    /**
//     * Called when the activity has become visible.
//     */
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.d(msg, "The onResume() event");
//    }
//
//    /**
//     * Called when another activity is taking focus.
//     */
//    @Override
//    protected void onPause() {
//        super.onPause();
//        Log.d(msg, "The onPause() event");
//    }
//
//    /**
//     * Called when the activity is no longer visible.
//     */
//    @Override
//    protected void onStop() {
//        super.onStop();
//        Log.d(msg, "The onStop() event");
//    }
//
//    /**
//     * Called just before the activity is destroyed.
//     */
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Log.d(msg, "The onDestroy() event");
//    }
//
//
//    public void setViews(){
//        // get the next json object
//
//        // ok so we need to itterate over the set of objects  in this array and render as many as needed.
//
//        // in terms of holdin the answers we will need
//        JSONObject currentPage = null;
//        try {
//            currentPage = reviewPagesJSONArray.getJSONObject(mCURRENT_REVIEW_PAGE_POINTER_INDEX);
//            // now we need to pick out all of the values
//
//            mCURRENT_QUESTION_NUMBER = currentPage.getInt("review_question_id");
//            String review_question = currentPage.getString("review_question");
//            String positive_indicators = currentPage.getString("positive_indicators");
//            String negative_indicators = currentPage.getString("negative_indicators");
//
//
//            mCURRENT_POSITIVE_INDICATORS = Arrays.asList(positive_indicators.split("@&@")); //positive_indicators.split("@&@");
//            mCURRENT_NEGATIVE_INDICATORS = Arrays.asList(negative_indicators.split("@&@"));
//
//            //set review question
//            Button sd = (Button) findViewById(R.id.review_question);
//            sd.setText(review_question);
//
//            Button p0 = (Button) findViewById(R.id.pi_c0_r0);
//            p0.setWidth(500);
//
//            Button n0 = (Button) findViewById(R.id.pi_c2_r0);
//            n0.setWidth(500);
//
//
//            // set all pos indicators
//            Button p1 = (Button) findViewById(R.id.pi_c0_r1);
//            p1.setText(mCURRENT_POSITIVE_INDICATORS.get(0));
//            p1.setWidth(500);
//
//            Button p2 = (Button) findViewById(R.id.pi_c0_r2);
//            p2.setText(mCURRENT_POSITIVE_INDICATORS.get(1));
//            p2.setWidth(500);
//            Button p3 = (Button) findViewById(R.id.pi_c0_r3);
//            p3.setText(mCURRENT_POSITIVE_INDICATORS.get(2));
//            p3.setWidth(500);
//            Button p4 = (Button) findViewById(R.id.pi_c0_r4);
//            p4.setText(mCURRENT_POSITIVE_INDICATORS.get(3));
//            p4.setWidth(500);
//            Button p5 = (Button) findViewById(R.id.pi_c0_r5);
//            p5.setText(mCURRENT_POSITIVE_INDICATORS.get(4));
//            p5.setWidth(500);
//            Button p6 = (Button) findViewById(R.id.pi_c0_r6);
//            p6.setText(mCURRENT_POSITIVE_INDICATORS.get(5));
//            p6.setWidth(500);
//
//            // set all negaive indicators
//            Button n1 = (Button) findViewById(R.id.pi_c2_r1);
//            n1.setText(mCURRENT_NEGATIVE_INDICATORS.get(0));
//            n1.setWidth(500);
//            Button n2 = (Button) findViewById(R.id.pi_c2_r2);
//            n2.setText(mCURRENT_NEGATIVE_INDICATORS.get(1));
//            n2.setWidth(500);
//            Button n3 = (Button) findViewById(R.id.pi_c2_r3);
//            n3.setText(mCURRENT_NEGATIVE_INDICATORS.get(2));
//            n3.setWidth(500);
//            Button n4 = (Button) findViewById(R.id.pi_c2_r4);
//            n4.setText(mCURRENT_NEGATIVE_INDICATORS.get(3));
//            n4.setWidth(500);
//            Button n5 = (Button) findViewById(R.id.pi_c2_r5);
//            n5.setText(mCURRENT_NEGATIVE_INDICATORS.get(4));
//            n5.setWidth(500);
//            Button n6 = (Button) findViewById(R.id.pi_c2_r6);
//            n6.setText(mCURRENT_NEGATIVE_INDICATORS.get(5));
//            n6.setWidth(500);
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                sd.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
//
//                p1.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
//                p2.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
//                p3.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
//                p4.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
//                p5.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
//                p6.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
//
//                n1.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
//                n2.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
//                n3.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
//                n4.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
//                n5.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
//                n6.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
//            }
//
//            LinearLayout hud = (LinearLayout) findViewById(R.id.review_hud);
//
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.MATCH_PARENT
//            );
//
//            params.setMargins(30, 30, 30, 50);
//            params.weight=1.0f;
//            hud.setLayoutParams(params);
//
//
//
//            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.MATCH_PARENT
//            );
//
//            params2.setMargins(20, 20, 20, 20);
//            params2.weight=2.5f;
//            LinearLayout mediahud = (LinearLayout) findViewById(R.id.questions_review_hud);
//            mediahud.setLayoutParams(params2);
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//
//
//    // table view shit
//
//
//    public class MyAdapter extends RecyclerView.Adapter<ACActivityReviewActivity.MyAdapter.ViewHolder> {
//
//        public MyAdapter(JSONArray testsJSONArray) {
//        }
//
//        public class ViewHolder extends RecyclerView.ViewHolder {
//            // each data item is just a string in this case
//            CardView cv;
//            public TextView info1;
//            public TextView info2;
//            public TextView info3;
//
//            ViewHolder(View itemView) {
//                super(itemView);
//                cv = (CardView)itemView.findViewById(R.id.card_view);
//                info1 = (TextView)itemView.findViewById(R.id.info_text_1);
//                info2 = (TextView)itemView.findViewById(R.id.info_text_2);
//                info3 = (TextView)itemView.findViewById(R.id.info_text_3);
//
//                itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override public void onClick(View v) {
//                        // item clicked
//                        int pos = getAdapterPosition();
//                        Log.e("ADS","Tapped index is : " + pos);
//
//                        JSONObject n = new JSONObject();
//                        try {
//                            n = (JSONObject) audioBlobsJSONARRAY.get(pos);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        // revisited when returned from get particiapnts is defined
//                        String question_number = null;
//                        String length = null;
//                        String filename = null;
//
//                        try {
//                            question_number = n.getString("question_id");
//                            length = n.getString("length");
//                            filename = n.getString("filename");
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        // do whats needed inthe player department
//                        mTAPPED_AUDIO_FILE_NAME = filename;
//                        Log.e("ADS","FILEPATH ============ " + mTAPPED_AUDIO_FILE_NAME);
//                        onPlay(true);
//                    }
//                });
//
//            }
//
//            public ViewHolder(TextView v) {
//                super(v);
//                info1 = v;
//                info2 = v;
//                info3 = v;
//            }
//        }
//
//        @Override
//        public ACActivityReviewActivity.MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            // create a new view
//            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ac_cardview, parent, false);
//
//            // set the view's size, margins, paddings and layout parameters
//            ACActivityReviewActivity.MyAdapter.ViewHolder vh = new ACActivityReviewActivity.MyAdapter.ViewHolder(v);
//            return vh;
//        }
//
//        // Replace the contents of a view (invoked by the layout manager)
//        @Override
//        public void onBindViewHolder(ACActivityReviewActivity.MyAdapter.ViewHolder holder, int position) {
//
//            JSONObject n = new JSONObject();
//            try {
//                n = (JSONObject) audioBlobsJSONARRAY.get(position);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            String question_number = null;
//            String length = null;
//            String filename = null;
//
//            try {
//                question_number = n.getString("question_id");
//                length = n.getString("length");
//                filename = n.getString("filename");
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            holder.info1.setText("Audio from Question "+ question_number);
//            holder.info2.setText(filename);
//            holder.info3.setText(length);
//        }
//
//        @Override
//        public int getItemCount() {
//            return  audioBlobsJSONARRAY.length();
//        }
//
//    }
//
//}