package com.apptitudedigitalsolutions.ads.TestCenter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.apptitudedigitalsolutions.ads.Application.ADSApplication;
import com.apptitudedigitalsolutions.ads.Landing.LandingActivity;
import com.apptitudedigitalsolutions.ads.Main.MainActivity;
import com.apptitudedigitalsolutions.ads.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import android.os.Handler;

/**
 * Created by Elliot on 08/11/2016.
 */

public class TestActivity extends Activity {

    ADSApplication appState = ((ADSApplication)this.getApplication());

    String msg = "Android : ";
    //String mTestID;
    String mLayoutType;
    int mCURRENT_QUESTION = 0;
    int mCURRENT_SECTION = 1;
    int mPAGES_COUNT = 0;
    int mCURRENT_TEST_PAGE_POINTER = 0;
    int mCOUNT_OF_PAGES_IN_INTRO = 0;
    int mCOUNT_OF_PAGES_IN_TEST_MAIN = 0;
    int mCOUNT_OF_PAGES_IN_TEST_FEEDBACK = 0;
    int mCURRENT_MAX_PAGE_POINTER = 0;
    int mCURRENT_TIME_TAKEN = 0;
    int mTIMER_HAS_BEEN_STARTED = 0;

    String mCURRENT_CORRECT_ANSWER = "";
    String mCURRENT_MACO_SECTION_TAG = "";

    int mSECTION_COUNT = 0;
    int mTIME_ALLOWED_IN_SECTION = 0;
    String mCandidateFirst;
    String mCandidateLast;
    String mCandidateEmail;
    String mCandidateRole;
    String mCandidateDoB;
    String mOther;
    String tappedAnswer;
    JSONArray testIntroJSONObject;
    ArrayList<String> SELECTED_ANSWERS = new ArrayList<String>();


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    LinearLayout ip_Layout;
    LinearLayout imc_Layout;
    LinearLayout smc_Layout;
    LinearLayout vt_Layout;

    private RequestQueue requestQueue;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_intro);
        Log.d(msg, "The onCreate() event");

        requestQueue = Volley.newRequestQueue(TestActivity.this);

        ImageView im = (ImageView) findViewById(R.id.client_icon);
        Picasso.with(this).load("https://"+ "adsapp.eu" + "/" +  appState.TEST_ID + "/client_icon.png").into(im);

        ImageView is = (ImageView) findViewById(R.id.company_icon);
        Picasso.with(this).load("https://"+ "adsapp.eu" + "/" +  appState.TEST_ID + "/company_icon.png").into(is);


        // get the test info
        Intent intent = getIntent();
//        mTestID = intent.getStringExtra("test_id");
        ip_Layout = (LinearLayout) findViewById(R.id.ip);
        imc_Layout = (LinearLayout) findViewById(R.id.imc);
        smc_Layout = (LinearLayout) findViewById(R.id.smc);
        vt_Layout = (LinearLayout) findViewById(R.id.vt);

        Button as1 = (Button) findViewById(R.id.answer_1);
        as1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Button b = (Button)v;
                tappedAnswer = b.getText().toString();
                v.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                Button as2 = (Button) findViewById(R.id.answer_2);
                as2.getBackground().clearColorFilter();
                Button as3 = (Button) findViewById(R.id.answer_3);
                as3.getBackground().clearColorFilter();
                Button as4 = (Button) findViewById(R.id.answer_4);
                as4.getBackground().clearColorFilter();
            }
        });
        Button as2 = (Button) findViewById(R.id.answer_2);
        as2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Button b = (Button)v;
                tappedAnswer = b.getText().toString();
                v.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                Button as1 = (Button) findViewById(R.id.answer_1);
                as1.getBackground().clearColorFilter();
                Button as3 = (Button) findViewById(R.id.answer_3);
                as3.getBackground().clearColorFilter();
                Button as4 = (Button) findViewById(R.id.answer_4);
                as4.getBackground().clearColorFilter();
            }
        });
        Button as3 = (Button) findViewById(R.id.answer_3);
        as3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Button b = (Button)v;
                tappedAnswer = b.getText().toString();
                v.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                Button as1 = (Button) findViewById(R.id.answer_1);
                as1.getBackground().clearColorFilter();
                Button as2 = (Button) findViewById(R.id.answer_2);
                as2.getBackground().clearColorFilter();
                Button as4 = (Button) findViewById(R.id.answer_4);
                as4.getBackground().clearColorFilter();
            }
        });
        Button as4 = (Button) findViewById(R.id.answer_4);
        as4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Button b = (Button)v;
                tappedAnswer = b.getText().toString();
                v.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                Button as1 = (Button) findViewById(R.id.answer_1);
                as1.getBackground().clearColorFilter();
                Button as2 = (Button) findViewById(R.id.answer_2);
                as2.getBackground().clearColorFilter();
                Button as3 = (Button) findViewById(R.id.answer_3);
                as3.getBackground().clearColorFilter();
            }
        });

        Button i_as1 = (Button) findViewById(R.id.i_answer_1);
        i_as1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Button b = (Button)v;
                tappedAnswer = b.getText().toString();
                v.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                Button i_as2 = (Button) findViewById(R.id.i_answer_2);
                i_as2.getBackground().clearColorFilter();
                Button i_as3 = (Button) findViewById(R.id.i_answer_3);
                i_as3.getBackground().clearColorFilter();
                Button i_as4 = (Button) findViewById(R.id.i_answer_4);
                i_as4.getBackground().clearColorFilter();
            }
        });
        Button i_as2 = (Button) findViewById(R.id.i_answer_2);
        i_as2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Button b = (Button)v;
                tappedAnswer = b.getText().toString();
                v.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                Button i_as1 = (Button) findViewById(R.id.i_answer_1);
                i_as1.getBackground().clearColorFilter();
                Button i_as3 = (Button) findViewById(R.id.i_answer_3);
                i_as3.getBackground().clearColorFilter();
                Button i_as4 = (Button) findViewById(R.id.i_answer_4);
                i_as4.getBackground().clearColorFilter();
            }
        });
        Button i_as3 = (Button) findViewById(R.id.i_answer_3);
        i_as3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Button b = (Button)v;
                tappedAnswer = b.getText().toString();
                v.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                Button i_as1 = (Button) findViewById(R.id.i_answer_1);
                i_as1.getBackground().clearColorFilter();
                Button i_as2 = (Button) findViewById(R.id.i_answer_2);
                i_as2.getBackground().clearColorFilter();
                Button i_as4 = (Button) findViewById(R.id.i_answer_4);
                i_as4.getBackground().clearColorFilter();
            }
        });
        Button i_as4 = (Button) findViewById(R.id.i_answer_4);
        i_as4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Button b = (Button) v;
                tappedAnswer = b.getText().toString();
                v.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                Button i_as1 = (Button) findViewById(R.id.i_answer_1);
                i_as1.getBackground().clearColorFilter();
                Button i_as2 = (Button) findViewById(R.id.i_answer_2);
                i_as2.getBackground().clearColorFilter();
                Button i_as3 = (Button) findViewById(R.id.i_answer_3);
                i_as3.getBackground().clearColorFilter();
            }
        });

        Button next = (Button) findViewById(R.id.next_button);
        next.setVisibility(View.VISIBLE);

        Button previous = (Button) findViewById(R.id.previous_button);
        previous.setVisibility(View.INVISIBLE);

        LinearLayout testInfoBar = (LinearLayout) findViewById(R.id.info_bar);
        testInfoBar.setVisibility(View.GONE);

        Boolean noCandidateInfo = candidatePresneceCheck();
        if(noCandidateInfo == true){
            // set all viws to gone and show add candedate dialog
            View v1 = getWindow().getDecorView().getRootView();
            showAddParticipant(v1);
        }



    }

    public void previous(View v){
        if(mCURRENT_TEST_PAGE_POINTER >= 0) {

            if(mCURRENT_MACO_SECTION_TAG.equals("test")) {
                int lastQuestionNumber = mCURRENT_QUESTION;
                if(tappedAnswer.equals("")){

                }else {
                    SELECTED_ANSWERS.set(mCURRENT_TEST_PAGE_POINTER, tappedAnswer);
                    sendAnswer(tappedAnswer, lastQuestionNumber, mCURRENT_SECTION, mCURRENT_CORRECT_ANSWER);
                }
                tappedAnswer = "";
            }

            mCURRENT_QUESTION--;
            mCURRENT_TEST_PAGE_POINTER--;

            Button as1 = (Button) findViewById(R.id.answer_1);
            as1.getBackground().clearColorFilter();

            Button as2 = (Button) findViewById(R.id.answer_2);
            as2.getBackground().clearColorFilter();

            Button as3 = (Button) findViewById(R.id.answer_3);
            as3.getBackground().clearColorFilter();

            Button as4 = (Button) findViewById(R.id.answer_4);
            as4.getBackground().clearColorFilter();

            // setting the views bg colours
            Button i_as1 = (Button) findViewById(R.id.i_answer_1);
            i_as1.getBackground().clearColorFilter();

            Button i_as2 = (Button) findViewById(R.id.i_answer_2);
            i_as2.getBackground().clearColorFilter();

            Button i_as3 = (Button) findViewById(R.id.i_answer_3);
            i_as3.getBackground().clearColorFilter();

            Button i_as4 = (Button) findViewById(R.id.i_answer_4);
            i_as4.getBackground().clearColorFilter();

            if(mCURRENT_TEST_PAGE_POINTER == 0){
                v.setVisibility(View.INVISIBLE);
            }

            if(mCURRENT_TEST_PAGE_POINTER+1 < mCURRENT_MAX_PAGE_POINTER){
                // show next button
                Button next = (Button) findViewById(R.id.next_button);
                next.setVisibility(View.VISIBLE);
            }
            setViews();
        }
    }

    public void next(View v){
        if(mCURRENT_TEST_PAGE_POINTER < mPAGES_COUNT){
            mCURRENT_QUESTION++;

            if(mCURRENT_MACO_SECTION_TAG.equals("test")) {
                int lastQuestionNumber = mCURRENT_QUESTION - 1;
                if(tappedAnswer.equals("")){

                }else {
                    SELECTED_ANSWERS.set(mCURRENT_TEST_PAGE_POINTER, tappedAnswer);
                    sendAnswer(tappedAnswer, lastQuestionNumber, mCURRENT_SECTION, mCURRENT_CORRECT_ANSWER);
                }
                tappedAnswer = "";
            }

            mCURRENT_TEST_PAGE_POINTER++;
            JSONObject newJsonObject = new JSONObject();
            try {
                 newJsonObject = testIntroJSONObject.getJSONObject(mCURRENT_TEST_PAGE_POINTER);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            int shouldShowNextButton = 0;
            try {
                shouldShowNextButton = newJsonObject.getInt("should_show_next");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.v("ADS","The page number is : " + mCURRENT_TEST_PAGE_POINTER + " And question = " + mCURRENT_QUESTION);
            Button previous = (Button) findViewById(R.id.previous_button);
            previous.setVisibility(View.VISIBLE);

            Button as1 = (Button) findViewById(R.id.answer_1);
            as1.getBackground().clearColorFilter();

            Button as2 = (Button) findViewById(R.id.answer_2);
            as2.getBackground().clearColorFilter();

            Button as3 = (Button) findViewById(R.id.answer_3);
            as3.getBackground().clearColorFilter();

            Button as4 = (Button) findViewById(R.id.answer_4);
            as4.getBackground().clearColorFilter();

            // setting the views bg colours
            Button i_as1 = (Button) findViewById(R.id.i_answer_1);
            i_as1.getBackground().clearColorFilter();

            Button i_as2 = (Button) findViewById(R.id.i_answer_2);
            i_as2.getBackground().clearColorFilter();

            Button i_as3 = (Button) findViewById(R.id.i_answer_3);
            i_as3.getBackground().clearColorFilter();

            Button i_as4 = (Button) findViewById(R.id.i_answer_4);
            i_as4.getBackground().clearColorFilter();



//            if(mCURRENT_TEST_PAGE_POINTER == mPAGES_COUNT || mCURRENT_TEST_PAGE_POINTER == mCURRENT_MAX_PAGE_POINTER ){
//                v.setVisibility(View.INVISIBLE);
//                Log.v("ADS","NEXT BUTTON CLCIKEC : The page number is : " + mCURRENT_TEST_PAGE_POINTER + " And MAX = " + mCURRENT_MAX_PAGE_POINTER);
//
//            }

            if(shouldShowNextButton == 0){
                v.setVisibility(View.INVISIBLE);
            }
            setViews();
        }

    }

    public void sendAnswer(String answer , int question_number , int section_number,String correct_answer){
        // make resquest
        String URL = "https://"+ "adsapp.eu" + "/v1/companies/test/answer/" +  appState.TEST_ID;

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("answer", answer);
        params.put("candidate_email", mCandidateEmail);
        params.put("correct_answer", correct_answer);
        params.put("question_id", String.valueOf(question_number));
        params.put("section_id", String.valueOf(section_number));

        JsonObjectRequest request_json = new JsonObjectRequest(URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                            Log.v("ADS", "Answer updated");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

//        RequestQueue requestQueue = Volley.newRequestQueue(TestActivity.this);
        request_json.setShouldCache(false);
        requestQueue.add(request_json);

    }

    public void getGetTestInto(){

        // make resquest
        String URL = "https://"+ "adsapp.eu" + "/v1/companies/tests/" +  appState.TEST_ID + "/intro";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("candidate_email", mCandidateEmail);
        params.put("is_admin", "0");

        JsonObjectRequest request_json = new JsonObjectRequest(URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            testIntroJSONObject = response.getJSONArray("pages");
                            Log.v("ADS", String.valueOf(testIntroJSONObject));
                            mPAGES_COUNT = testIntroJSONObject.length();

                            for(int i=0; i<mPAGES_COUNT; i++) {
                                JSONObject item = testIntroJSONObject.getJSONObject(i);
                                String pageType = item.getString("macro_section_type");
                                String layoutType = item.getString("answer_type");
                                int section_under_inspection = item.getInt("section_id");
                                SELECTED_ANSWERS.add("");

                                if(mCURRENT_SECTION == section_under_inspection){

                                    if(pageType.equals("intro")){
                                        mCOUNT_OF_PAGES_IN_INTRO++;
                                    }

                                    if(pageType.equals("test") && (layoutType.equals("smc") || layoutType.equals("imc"))){
                                        mCOUNT_OF_PAGES_IN_TEST_MAIN++;
                                    }

                                    if(pageType.equals("feedback")){
                                        mCOUNT_OF_PAGES_IN_TEST_FEEDBACK++;
                                    }

                                }
                            }
                            mCURRENT_MAX_PAGE_POINTER = mCOUNT_OF_PAGES_IN_INTRO;
                            Log.v("ADS", "INTO length = "+mCOUNT_OF_PAGES_IN_INTRO+"pages");
                            Log.v("ADS", "TEST MAIN length = "+mCOUNT_OF_PAGES_IN_TEST_MAIN+"pages");
                            Log.v("ADS", "FEEDBACK length = "+mCOUNT_OF_PAGES_IN_TEST_FEEDBACK+"pages");

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

//        RequestQueue requestQueue = Volley.newRequestQueue(TestActivity.this);
        request_json.setShouldCache(false);
        requestQueue.add(request_json);
        // Call for layout refresh.
    }


    public void statusCheck(){
        Log.v("ADS","Status Check");
        final Handler handler = new Handler();

        if(appState.BACK_TO_LOGIN == true){

            appState.TEST_ID = ""; // This is set in the landing activity and then loaded locally from there.
            appState.TEST_TITLE= "";
            appState.TEST_DATE= "";
            appState.TEST_DESC= "";

            appState.TEST_SECTIONS= "";
            appState.TEST_SECTIONS_TAGS= "";

            appState.CURRENT_TEST_SECTION_SLIDE_INDEX_NUMBER = 0;

            Intent intent = new Intent(this.getApplicationContext(), LandingActivity.class);
            startActivity(intent);
        }

        String URL = "https://"+ "adsapp.eu" + "/v1/companies/test/"+ appState.TEST_ID+"/status";
        Log.v("ADS", "URL is  .. " + URL);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("candidate_email", mCandidateEmail);

        JsonObjectRequest request_json = new JsonObjectRequest(URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            int testPageID = response.getInt("min_page_of_test");
                            if(testPageID > mCURRENT_TEST_PAGE_POINTER+1){
                                Log.v("ADS","We need to go to another page in the test");
                                // now we set the pointer to what is needed
                                mCURRENT_TEST_PAGE_POINTER = testPageID - 1;

                                // now we get the desired section (given the page id) then from this we deduce the max pointer value for that sction within section
                                int desired_section = 0;
                                int end_of_section_index = 0;
                                int end_of_section_raw = 0;
                                String section_tag = "";
                                mCOUNT_OF_PAGES_IN_TEST_MAIN = 0;

                                mCURRENT_QUESTION = 0;
                                mCOUNT_OF_PAGES_IN_INTRO = 0;
                                mCOUNT_OF_PAGES_IN_TEST_MAIN = 0;
                                mCOUNT_OF_PAGES_IN_TEST_FEEDBACK = 0;
                                mCURRENT_TIME_TAKEN = 0;

                                int oldSection = 0;
                                JSONObject item = new JSONObject();

                                for(int i=0; i<mPAGES_COUNT; i++) {
                                    item = testIntroJSONObject.getJSONObject(i);

                                    // get the id of first page in section
                                    if(item.getInt("id") == testPageID){
                                        desired_section = item.getInt("section_id");
                                        section_tag = item.getString("macro_section_type");
                                    }

                                    // get id of last page in section
                                    if(item.getInt("section_id") == desired_section && item.getString("macro_section_type").equals(section_tag)){
                                        if(end_of_section_index == 0){
                                            end_of_section_index = i;
                                        }else{
                                            end_of_section_index++;
                                            mCOUNT_OF_PAGES_IN_TEST_MAIN++;
                                        }
                                    }

                                }
                                 // set the max page number
                                mCURRENT_MAX_PAGE_POINTER = end_of_section_index;
                                mCURRENT_MACO_SECTION_TAG = section_tag;


                                    // then we need to adjust the test pointer to set it to start in the desired section.
                                    int newTestBaseline = mCOUNT_OF_PAGES_IN_INTRO + mCOUNT_OF_PAGES_IN_TEST_MAIN + mCOUNT_OF_PAGES_IN_TEST_FEEDBACK; // this is where all pointers should now be based off ( as opposed ot the start of the test
                                    // reset all sections to 0
                                    mCOUNT_OF_PAGES_IN_INTRO = 0;
                                    mCOUNT_OF_PAGES_IN_TEST_MAIN = 0; // might need to add 1 to this
                                    mCOUNT_OF_PAGES_IN_TEST_FEEDBACK = 0;

                                    // itterate over the pages in the section under inspection and set up page number counts for each section
                                    for(int i=mCURRENT_TEST_PAGE_POINTER; i<mPAGES_COUNT; i++) {

                                        JSONObject item1 = testIntroJSONObject.getJSONObject(i);
                                        if(item1.getInt("section_id") == desired_section) {

                                            String pageType = item1.getString("macro_section_type");
                                            String layoutType = item1.getString("answer_type");
                                            if (pageType.equals("intro")) {
                                                mCOUNT_OF_PAGES_IN_INTRO++;

                                            }

                                            if (pageType.equals("test") && (layoutType.equals("smc") || layoutType.equals("imc"))) {
                                                mCOUNT_OF_PAGES_IN_TEST_MAIN++;
                                            }

                                            if (pageType.equals("feedback")) {
                                                mCOUNT_OF_PAGES_IN_TEST_FEEDBACK++;
                                            }
                                        }

                                    }


                                // Now we show the corect buttons and hide the remainder
                                Button next = (Button) findViewById(R.id.next_button);
                                next.setVisibility(View.VISIBLE);

                                Button previous = (Button) findViewById(R.id.previous_button);
                                previous.setVisibility(View.INVISIBLE);

                                // setting the views bg colours
                                Button as1 = (Button) findViewById(R.id.answer_1);
                                as1.getBackground().clearColorFilter();

                                Button as2 = (Button) findViewById(R.id.answer_2);
                                as2.getBackground().clearColorFilter();

                                Button as3 = (Button) findViewById(R.id.answer_3);
                                as3.getBackground().clearColorFilter();

                                Button as4 = (Button) findViewById(R.id.answer_4);
                                as4.getBackground().clearColorFilter();

                                // setting the views bg colours
                                Button i_as1 = (Button) findViewById(R.id.i_answer_1);
                                i_as1.getBackground().clearColorFilter();

                                Button i_as2 = (Button) findViewById(R.id.i_answer_2);
                                i_as2.getBackground().clearColorFilter();

                                Button i_as3 = (Button) findViewById(R.id.i_answer_3);
                                i_as3.getBackground().clearColorFilter();

                                Button i_as4 = (Button) findViewById(R.id.i_answer_4);
                                i_as4.getBackground().clearColorFilter();

                                // ask the views to be reset
                                setViews();

                            }

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

//        RequestQueue requestQueue = Volley.newRequestQueue(TestActivity.this);
        request_json.setShouldCache(false);
        requestQueue.add(request_json);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                statusCheck();
            }
        }, 5000);

    }

    public boolean candidatePresneceCheck(){
        if(mCandidateFirst == ""){
            return false;
        }else {
            return true;
        }
    }


    public void showAddParticipant(View v){

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);

        final Context context = alert.getContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setPadding(10,10,10,10);
        layout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout layoutForFirstAndLast = new LinearLayout(context);
        layout.setPadding(10,10,10,10);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        final EditText first = new EditText(context);
        first.setHint("first name");
       // first.setText("Elliot");
        layoutForFirstAndLast.addView(first);

        final EditText last = new EditText(context);
        last.setHint("Last name");
        //last.setText("Campbelton");
        layoutForFirstAndLast.addView(last);

        layout.addView(layoutForFirstAndLast);

        final EditText email = new EditText(context);
        email.setHint("email address");
        //email.setText("e.b.campbelton@gmail.com");
        layout.addView(email);

        final EditText dob = new EditText(context);
        dob.setHint("DoB");
        //dob.setText("2/3/1993");
        layout.addView(dob);

//        final EditText other = new EditText(context);
//        other.setHint("other");
//        other.setText("smashing");
//        layout.addView(other);


        alert.setMessage("Please enter your details below.  ");
        alert.setTitle("New test participant");

        alert.setView(layout);


        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int whichButton) {

                String URL = "https://"+ "adsapp.eu" + "/v1/companies/test/"+ appState.TEST_ID+"/participants/add";

                HashMap<String, String> params = new HashMap<String, String>();

                mCandidateFirst = String.valueOf(first.getText());
                mCandidateLast = String.valueOf(last.getText());
                mCandidateEmail = String.valueOf(email.getText());
                mCandidateDoB = String.valueOf(dob.getText());



                params.put("first", String.valueOf(first.getText()));
                params.put("last", String.valueOf(last.getText()));
                params.put("email", String.valueOf(email.getText()));
                params.put("dob", String.valueOf(dob.getText()));
                if(appState.TOKEN != null) {
                    params.put("token", appState.TOKEN);
                }

                JsonObjectRequest request_json = new JsonObjectRequest(URL, new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.v("ADS","Added uer");
                        getGetTestInto();
                        statusCheck();
                        dialog.dismiss();


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                    }
                });

//                RequestQueue requestQueue = Volley.newRequestQueue(TestActivity.this);
                request_json.setShouldCache(false);
                requestQueue.add(request_json);

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();
    }


    @Override
    public void onBackPressed()
    {
        // Your Code Here. Leave empty if you want nothing to happen on back press.
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

    public void sendAnswerForQuestion(){

    }
    public void setViews(){

        JSONObject page = new JSONObject();
        try {

            if(testIntroJSONObject != null) {


                page = testIntroJSONObject.getJSONObject(mCURRENT_TEST_PAGE_POINTER);
                Log.e("ADS", String.valueOf(page));

                mCURRENT_TEST_PAGE_POINTER = page.getInt("id") - 1;
                mCURRENT_QUESTION = page.getInt("question_id");
                mCURRENT_SECTION = page.getInt("section_id");
                String section_title = page.getString("section_title");
                String section_text = page.getString("section_text");
                String test_info_ref = page.getString("test_info_ref");
                String section_media_url = page.getString("section_media_url");
                String section_media_type = page.getString("section_media_type");
                int section_question_count = 0;
                if (page.isNull("section_question_count")) {
                    int j = 0;
                    section_question_count = j;
                } else {
                    int j = page.getInt("section_question_count");
                    section_question_count = j;
                }
                String question = page.getString("question");
                String question_media_url = page.getString("question_media_url");
                String question_media_type = page.getString("question_media_type");
                String answer_type = page.getString("answer_type");
                String answer_options = page.getString("answer_options");
                String answer_catergories = page.getString("answer_catergories");
                String correct_answer = page.getString("correct_answer");
                String macro_section_type = page.getString("macro_section_type");
                int time_allowed_in_section;
                if (page.isNull("time_allowed_in_section")) {
                    time_allowed_in_section = 0;
                } else {
                    time_allowed_in_section = page.getInt("time_allowed_in_section");
                }
                String test_results_file_ref = page.getString("test_results_file_ref");
                mCURRENT_CORRECT_ANSWER = correct_answer;

                LinearLayout testHUD = (LinearLayout) findViewById(R.id.info_bar);
                int visibility = testHUD.getVisibility();

                if (macro_section_type.equals("test") && (answer_type.equals("smc") || answer_type.equals("imc"))) {
                    // we need to set up and show the test HUD
                    testHUD.setVisibility(View.VISIBLE);

                    // set title
                    TextView title = (TextView) findViewById(R.id.section_title);
                    title.setText(section_title);

                    // set question number
                    TextView questionNumber = (TextView) findViewById(R.id.question_number);
                    int testPagesMinusLast = mCOUNT_OF_PAGES_IN_TEST_MAIN;
                    questionNumber.setText("Question " + mCURRENT_QUESTION + "/" + testPagesMinusLast);

                    // hide section number

                    // set time limit
                    Integer intObj = new Integer(time_allowed_in_section);
                    Number numObj = (Number) intObj;
                    BigDecimal big = new BigDecimal(numObj.toString());

                    int[] compoents = splitToComponentTimes(big);
                    Log.v("ADS", "The Time In this section is = " + compoents);

                    TextView timeAllowed = (TextView) findViewById(R.id.time_allowed);
                    timeAllowed.setText("Time Allowed : " + compoents[1] + "mins");

                    if ((visibility == View.GONE || visibility == View.INVISIBLE) && mTIMER_HAS_BEEN_STARTED == 0) {
                        // this means the counter needs to start firing. othervise there is no need to start the method (danger of haveing multiple concurrent methods running otherwise
                        updateTimeTaken();
                    }

                    // set up counter with method
                } else {
                    testHUD.setVisibility(View.GONE);
                }

                mCURRENT_MACO_SECTION_TAG = macro_section_type;

                //What kind of layout is it that we are looking at for this question
                if (answer_type.equals("ip")) {
                    // set all other layouts to gone and ip == visible
                    ip_Layout.setVisibility(View.VISIBLE);
                    imc_Layout.setVisibility(View.GONE);
                    smc_Layout.setVisibility(View.GONE);
                    vt_Layout.setVisibility(View.GONE);

                    TextView title = (TextView) findViewById(R.id.info_panel_title);
                    title.setText(section_title);

                    TextView message = (TextView) findViewById(R.id.info_panel_message);
                    message.setText(section_text);
                    Linkify.addLinks(message, Linkify.ALL);

                }

                if (answer_type.equals("vt")) {
                    ip_Layout.setVisibility(View.GONE);
                    imc_Layout.setVisibility(View.GONE);
                    smc_Layout.setVisibility(View.GONE);
                    vt_Layout.setVisibility(View.VISIBLE);

                    TextView videoTitle = (TextView) findViewById(R.id.video_title);
                    videoTitle.setText(section_title);


                    TextView videoText = (TextView) findViewById(R.id.video_descriptor);
                    videoText.setText(section_text);

                    final VideoView vid = (VideoView) findViewById(R.id.videoView);
                    Uri vidUri = Uri.parse(section_media_url);
                    vid.setMediaController(new MediaController(this));
                    vid.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            vid.start();
                        }
                    });
                    vid.setVideoURI(vidUri);
                }

                if (answer_type.equals("smc")) {
                    ip_Layout.setVisibility(View.GONE);
                    imc_Layout.setVisibility(View.GONE);
                    smc_Layout.setVisibility(View.VISIBLE);
                    vt_Layout.setVisibility(View.GONE);
                    // split answers string into array
                    String[] items = answer_options.split("@&@");

                    Button questionText = (Button) findViewById(R.id.question_text);
                    questionText.setText(question);

                    int indexOfCorrectAnswer = getArrayIndex(items, correct_answer);
                    if (items.length == 4) {
                        Button as1 = (Button) findViewById(R.id.answer_1);
                        as1.setText(items[0]);
                        if (indexOfCorrectAnswer == 0 && macro_section_type.equals("intro")) {
                            as1.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        }
                        // if the anser to go in this cell == the answer at the questions index

                        if(items[0].equals(SELECTED_ANSWERS.get(mCURRENT_TEST_PAGE_POINTER))){
                            as1.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        }

                        Button as2 = (Button) findViewById(R.id.answer_2);
                        as2.setText(items[1]);
                        if (indexOfCorrectAnswer == 1 && macro_section_type.equals("intro")) {
                            as2.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        }
                        // if the anser to go in this cell == the answer at the questions index
                        if(items[1].equals(SELECTED_ANSWERS.get(mCURRENT_TEST_PAGE_POINTER))){
                            as2.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        }

                        Button as3 = (Button) findViewById(R.id.answer_3);
                        as3.setText(items[2]);
                        if (indexOfCorrectAnswer == 2 && macro_section_type.equals("intro")) {
                            as3.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        }
                        // if the anser to go in this cell == the answer at the questions index
                        if(items[2].equals(SELECTED_ANSWERS.get(mCURRENT_TEST_PAGE_POINTER))){
                            as3.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        }


                        Button as4 = (Button) findViewById(R.id.answer_4);
                        as4.setText(items[3]);
                        if (indexOfCorrectAnswer == 3 && macro_section_type.equals("intro")) {
                            as4.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        }
                        // if the anser to go in this cell == the answer at the questions index
                        if(items[3].equals(SELECTED_ANSWERS.get(mCURRENT_TEST_PAGE_POINTER))){
                            as4.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        }

                    }

                    if (items.length == 3) {
                        Button as1 = (Button) findViewById(R.id.answer_1);
                        as1.setText(items[0]);
                        if (indexOfCorrectAnswer == 0 && macro_section_type.equals("intro")) {
                            as1.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        }
                        // if the anser to go in this cell == the answer at the questions index
                        if(items[0].equals(SELECTED_ANSWERS.get(mCURRENT_TEST_PAGE_POINTER))){
                            as1.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        }

                        Button as2 = (Button) findViewById(R.id.answer_2);
                        as2.setText(items[1]);
                        if (indexOfCorrectAnswer == 1 && macro_section_type.equals("intro")) {
                            as2.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        }
                        // if the anser to go in this cell == the answer at the questions index
                        if(items[1].equals(SELECTED_ANSWERS.get(mCURRENT_TEST_PAGE_POINTER))){
                            as2.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        }

                        Button as3 = (Button) findViewById(R.id.answer_3);
                        as3.setText(items[2]);
                        if (indexOfCorrectAnswer == 2 && macro_section_type.equals("intro")) {
                            as3.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        }
                        // if the anser to go in this cell == the answer at the questions index
                        if(items[2].equals(SELECTED_ANSWERS.get(mCURRENT_TEST_PAGE_POINTER))){
                            as3.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        }

                        Button as4 = (Button) findViewById(R.id.answer_4);
                        as4.setVisibility(View.GONE);
                    }


                    if (items.length == 2) {
                        Button as1 = (Button) findViewById(R.id.answer_1);
                        as1.setText(items[0]);
                        if (indexOfCorrectAnswer == 0 && macro_section_type.equals("intro")) {
                            as1.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        }
                        // if the anser to go in this cell == the answer at the questions index
                        if(items[0].equals(SELECTED_ANSWERS.get(mCURRENT_TEST_PAGE_POINTER))){
                            as1.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        }

                        Button as2 = (Button) findViewById(R.id.answer_2);
                        as2.setText(items[1]);
                        if (indexOfCorrectAnswer == 1 && macro_section_type.equals("intro")) {
                            as2.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        }
                        // if the anser to go in this cell == the answer at the questions index
                        if(items[2].equals(SELECTED_ANSWERS.get(mCURRENT_TEST_PAGE_POINTER))){
                            as2.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        }

                        Button as3 = (Button) findViewById(R.id.answer_3);
                        as3.setVisibility(View.GONE);

                        Button as4 = (Button) findViewById(R.id.answer_4);
                        as4.setVisibility(View.GONE);
                    }


                }
                if (answer_type.equals("imc")) {
                    ip_Layout.setVisibility(View.GONE);
                    imc_Layout.setVisibility(View.VISIBLE);
                    smc_Layout.setVisibility(View.GONE);
                    vt_Layout.setVisibility(View.GONE);


                    String[] items = answer_options.split("@&@");

                    TextView questionText = (TextView) findViewById(R.id.i_question_text);
                    questionText.setText(question);


                    ImageView im = (ImageView) findViewById(R.id.question_image);
                    Picasso.with(this).load("https://" + "adsapp.eu" + "/" + appState.TEST_ID + "/" + question_media_url).into(im);

                    int indexOfCorrectAnswer = getArrayIndex(items, correct_answer);
                    if (items.length == 4) {
                        Button as1 = (Button) findViewById(R.id.i_answer_1);
                        as1.setText(items[0]);
                        if (indexOfCorrectAnswer == 0 && macro_section_type.equals("intro")) {
                            as1.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        }
                        if(items[0].equals(SELECTED_ANSWERS.get(mCURRENT_TEST_PAGE_POINTER))){
                            as1.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        }

                        Button as2 = (Button) findViewById(R.id.i_answer_2);
                        as2.setText(items[1]);
                        if (indexOfCorrectAnswer == 1 && macro_section_type.equals("intro")) {
                            as2.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        }
                        if(items[1].equals(SELECTED_ANSWERS.get(mCURRENT_TEST_PAGE_POINTER))){
                            as2.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        }

                        Button as3 = (Button) findViewById(R.id.i_answer_3);
                        as3.setText(items[2]);
                        if (indexOfCorrectAnswer == 2 && macro_section_type.equals("intro")) {
                            as3.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        }
                        if(items[2].equals(SELECTED_ANSWERS.get(mCURRENT_TEST_PAGE_POINTER))){
                            as3.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        }

                        Button as4 = (Button) findViewById(R.id.i_answer_4);
                        as4.setText(items[3]);
                        if (indexOfCorrectAnswer == 3 && macro_section_type.equals("intro")) {
                            as4.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        }
                        if(items[3].equals(SELECTED_ANSWERS.get(mCURRENT_TEST_PAGE_POINTER))){
                            as3.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        }
                    }

                    if (items.length == 3) {
                        Button as1 = (Button) findViewById(R.id.i_answer_1);
                        as1.setText(items[0]);
                        if (indexOfCorrectAnswer == 0 && macro_section_type.equals("intro")) {
                            as1.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        }
                        if(items[0].equals(SELECTED_ANSWERS.get(mCURRENT_TEST_PAGE_POINTER))){
                            as1.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        }

                        Button as2 = (Button) findViewById(R.id.i_answer_2);
                        as2.setText(items[1]);
                        if (indexOfCorrectAnswer == 1 && macro_section_type.equals("intro")) {
                            as2.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        }
                        if(items[1].equals(SELECTED_ANSWERS.get(mCURRENT_TEST_PAGE_POINTER))){
                            as2.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        }

                        Button as3 = (Button) findViewById(R.id.i_answer_3);
                        as3.setText(items[2]);
                        if (indexOfCorrectAnswer == 2 && macro_section_type.equals("intro")) {
                            as3.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        }
                        if(items[2].equals(SELECTED_ANSWERS.get(mCURRENT_TEST_PAGE_POINTER))){
                            as3.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        }

                        Button as4 = (Button) findViewById(R.id.i_answer_4);
                        as4.setVisibility(View.GONE);
                    }


                    if (items.length == 2) {
                        Button as1 = (Button) findViewById(R.id.i_answer_1);
                        as1.setText(items[0]);
                        if (indexOfCorrectAnswer == 0 && macro_section_type.equals("intro")) {
                            as1.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        }
                        if(items[0].equals(SELECTED_ANSWERS.get(mCURRENT_TEST_PAGE_POINTER))){
                            as1.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        }

                        Button as2 = (Button) findViewById(R.id.i_answer_2);
                        as2.setText(items[1]);
                        if (indexOfCorrectAnswer == 1 && macro_section_type.equals("intro")) {
                            as2.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        }

                        if (items[1].equals(SELECTED_ANSWERS.get(mCURRENT_TEST_PAGE_POINTER))) {
                           as2.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        }

                        Button as3 = (Button) findViewById(R.id.i_answer_3);
                        as3.setVisibility(View.GONE);

                        Button as4 = (Button) findViewById(R.id.i_answer_4);
                        as4.setVisibility(View.GONE);
                    }

                }

            }
            // more types of layout here ....


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void updateTimeTaken(){
        mCURRENT_TIME_TAKEN++;
        mTIMER_HAS_BEEN_STARTED =1;

//      if(mCURRENT_TIME_TAKEN => )
        // convert to big int and then convert to a MM and SS object string and display
        Integer intObj = new Integer(mCURRENT_TIME_TAKEN);
        Number numObj = (Number)intObj;
        BigDecimal big = new BigDecimal(numObj.toString());

        int[] compoents = splitToComponentTimes(big);
        Log.v("ADS", "The Time taken so far is = " + compoents);

        TextView timeTaken = (TextView) findViewById(R.id.time_taken);
        timeTaken.setText("Time Taken : " + compoents[1] + "mins " + compoents[2] + "sec");

        // if mCURRENT_TIME_TAKEN > mTIME_ALLOWED_IN_SECTION then send the user back to main
        if (mCURRENT_TIME_TAKEN > mTIME_ALLOWED_IN_SECTION){
            // out of time . new time out layout needs to be defined more accuratly showing this state (we dont want to leave the acvitity unless we are looking at the last section alreat fam.
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateTimeTaken();
            }
        }, 1000);

    }

    public int getArrayIndex(String[] arr,String value) {

        int k=0;
        for(int i=0;i<arr.length;i++){

            if(arr[i].equals(value)){
                k=i;
                break;
            }
        }
        return k;
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

}