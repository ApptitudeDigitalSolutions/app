package com.apptitudedigitalsolutions.ads.AssessmentCenter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.apptitudedigitalsolutions.ads.Application.ADSApplication;
import com.apptitudedigitalsolutions.ads.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Elliot on 19/11/2016.
 */

public class ACSelectActivityActivity extends Activity {
    ADSApplication appState;

    String msg = "Android : ";
//    String appState.ACID;
//    String appState.AC_CURRENT_CANDIDATE_ID;
//    String appState.AC_CANDIDATE_FIRST;
//    String appState.AC_CANDIDATE_LAST;
//    String appState.AC_CANDIDATE_EMAIL;
    String mSET_ACTIVITIES;
//    String[] appState.AC_SET_ACTIVITIES_IDS_ARRAY;
//    String appState.AC_COMPLETED_ACTIVITIES;
//    String[] appState.AC_COMPLETED_ACTIVITIES_ARRAY;
//    String appState.AC_SET_ACTIVITIE_TAGS;
//    String[] appState.AC_SET_ACTIVITIE_TAGS_ARRAY;


//    String appState.AC_TITLE ="";
//    String appState.AC_CONDUCTED_ON="";
//    String appState.AC_DESC="";
//    String appState.AC_ACTIVITY_IDS="";
//    String appState.AC_ACTIVITY_TYPES="";


//    ArrayList<String> appState.AC_COMPLETED_ACTIVITIES_TAGS_ARRAY = new ArrayList<String>();
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_ac_activity);
        Log.d(msg, "The onCreate() event");
        appState = ((ADSApplication)this.getApplication());

//        Intent intent = getIntent();
//        appState.ACID = intent.getStringExtra("ac_id");
//        appState.AC_CURRENT_CANDIDATE_ID = intent.getStringExtra("candidate_id");
//        appState.AC_CANDIDATE_FIRST= intent.getStringExtra("candidate_first");
//        appState.AC_CANDIDATE_LAST= intent.getStringExtra("candidate_last");
//        appState.AC_CANDIDATE_EMAIL= intent.getStringExtra("candidate_email");
//        mSET_ACTIVITIES= intent.getStringExtra("set_activities");
//        appState.AC_SET_ACTIVITIE_TAGS= intent.getStringExtra("set_activitie_tags");
//        appState.AC_COMPLETED_ACTIVITIES= intent.getStringExtra("completed_activities");
//
//
//        appState.AC_TITLE = intent.getStringExtra("title");
//        appState.AC_CONDUCTED_ON = intent.getStringExtra("to_be_conducted_on");
//        appState.AC_DESC = intent.getStringExtra("description");
//        appState.AC_ACTIVITY_IDS = intent.getStringExtra("activity_ids");
//        appState.AC_ACTIVITY_TYPES = intent.getStringExtra("activity_types");


        // set up on click listeners for each button
        appState.AC_COMPLETED_ACTIVITIES_ARRAY = appState.AC_COMPLETED_ACTIVITIES.split(",");
        appState.AC_SET_ACTIVITIES_IDS_ARRAY = appState.AC_ACTIVITY_IDS.split(",");
//        appState.AC_SET_ACTIVITIE_TAGS_ARRAY = appState.AC_SET_ACTIVITIE_TAGS.split(",");
        appState.AC_SET_ACTIVITIE_TYPES_ARRAY = appState.AC_ACTIVITY_TYPES.split(",");

        for(int i = 0; i <appState.AC_SET_ACTIVITIE_TYPES_ARRAY.length; i++){
            String id = appState.AC_SET_ACTIVITIE_TYPES_ARRAY[i];
            for(int j = 0;j < appState.AC_COMPLETED_ACTIVITIES_ARRAY.length; j++){
                if(id.equals(appState.AC_COMPLETED_ACTIVITIES_ARRAY[j])){
                    // get the i'th value form the tags array and add it
                    appState.AC_COMPLETED_ACTIVITIES_TAGS_ARRAY.add(appState.AC_SET_ACTIVITIE_TYPES_ARRAY[i]);
                }
            }
        }


        Button interviewBtn = (Button) findViewById(R.id.interview);
        interviewBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Goo to the interview activity
                Intent intent = new Intent(ACSelectActivityActivity.this, ACInterviewActivity.class);
                // get the id of the activity you are looking for in this case
//                intent.putExtra("candidate_id", appState.AC_CURRENT_CANDIDATE_ID);
//                intent.putExtra("ac_id",appState.ACID);
//                intent.putExtra("id", appState.ACID);
//                intent.putExtra("title", appState.AC_TITLE);
//                intent.putExtra("to_be_conducted_on", appState.AC_CONDUCTED_ON);
//                intent.putExtra("description", appState.AC_DESC);
//                intent.putExtra("activity_ids", appState.AC_ACTIVITY_IDS);
//                intent.putExtra("activity_types", appState.AC_ACTIVITY_TYPES);
//                intent.putExtra("set_activitie_tags",appState.AC_SET_ACTIVITIE_TAGS);
//                intent.putExtra("completed_activities",appState.AC_COMPLETED_ACTIVITIES);

                startActivity(intent);

            }
        });

        if(Arrays.asList(appState.AC_SET_ACTIVITIE_TYPES_ARRAY).contains("i")){
            // button should be shown
            interviewBtn.setVisibility(View.VISIBLE);
        }else {
            interviewBtn.setVisibility(View.GONE);
        }



        Button presentationBtn = (Button) findViewById(R.id.presentation);
        presentationBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // go to the presentation activity
                Intent intent = new Intent(ACSelectActivityActivity.this, ACPresentationActivity.class);
                // get the id of the activity you are looking for in this case
//                intent.putExtra("candidate_id", appState.AC_CURRENT_CANDIDATE_ID);
//                intent.putExtra("ac_id",appState.ACID);
//                intent.putExtra("id", appState.ACID);
//                intent.putExtra("title", appState.AC_TITLE);
//                intent.putExtra("to_be_conducted_on", appState.AC_CONDUCTED_ON);
//                intent.putExtra("description", appState.AC_DESC);
//                intent.putExtra("activity_ids", appState.AC_ACTIVITY_IDS);
//                intent.putExtra("activity_types", appState.AC_ACTIVITY_TYPES);
//                intent.putExtra("set_activitie_tags",appState.AC_SET_ACTIVITIE_TAGS);
//                intent.putExtra("completed_activities",appState.AC_COMPLETED_ACTIVITIES);

                startActivity(intent);

            }
        });
        if(Arrays.asList(appState.AC_SET_ACTIVITIE_TYPES_ARRAY).contains("p")){
            // button should be shown
        }else {
            interviewBtn.setVisibility(View.GONE);
        }


        Button roleplayBtn = (Button) findViewById(R.id.roleplay);
        roleplayBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // go to the roleplay activity
                Intent intent = new Intent(ACSelectActivityActivity.this, ACRoleplayActivity.class);
                // get the id of the activity you are looking for in this case
//                intent.putExtra("candidate_id", appState.AC_CURRENT_CANDIDATE_ID);
//                intent.putExtra("ac_id",appState.ACID);
//                intent.putExtra("id", appState.ACID);
//                intent.putExtra("title", appState.AC_TITLE);
//                intent.putExtra("to_be_conducted_on", appState.AC_CONDUCTED_ON);
//                intent.putExtra("description", appState.AC_DESC);
//                intent.putExtra("activity_ids", appState.AC_ACTIVITY_IDS);
//                intent.putExtra("activity_types", appState.AC_ACTIVITY_TYPES);
//                intent.putExtra("set_activitie_tags",appState.AC_SET_ACTIVITIE_TAGS);
//                intent.putExtra("completed_activities",appState.AC_COMPLETED_ACTIVITIES);

                startActivity(intent);
            }
        });
        if(Arrays.asList(appState.AC_SET_ACTIVITIE_TYPES_ARRAY).contains("rp")){
            // button should be shown

        }else {
            interviewBtn.setVisibility(View.GONE);
        }



        if(appState.AC_COMPLETED_ACTIVITIES_TAGS_ARRAY.contains("i")){
            // make its bg green as it has been completed, sick fam
            interviewBtn.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
        }else{
            // clear its bg
            interviewBtn.getBackground().clearColorFilter();
        }

        if(appState.AC_COMPLETED_ACTIVITIES_TAGS_ARRAY.contains("p")) {
            // make its bg green as it has been completed, sick fam
            presentationBtn.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
        }else{
            // clear its bg
            presentationBtn.getBackground().clearColorFilter();
        }

        if(appState.AC_COMPLETED_ACTIVITIES_TAGS_ARRAY.contains("rp")) {
            // make its bg green as it has been completed, sick fam
            roleplayBtn.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
        }else{
            // clear its bg
            roleplayBtn.getBackground().clearColorFilter();
        }

        // do a get on the participant to see what activities they have completed

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

}