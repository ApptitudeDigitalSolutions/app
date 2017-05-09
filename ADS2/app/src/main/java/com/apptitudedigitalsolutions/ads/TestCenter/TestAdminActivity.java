package com.apptitudedigitalsolutions.ads.TestCenter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.HashMap;

import com.apptitudedigitalsolutions.ads.Application.ADSApplication;
import com.apptitudedigitalsolutions.ads.R;
import com.apptitudedigitalsolutions.ads.DatabaseUtils.DatabaseHelper;

/**
 * Created by Elliot on 19/10/2016.
 */

public class TestAdminActivity extends Activity {

    ADSApplication appState = ((ADSApplication)this.getApplication());
    RequestQueue requestQueue;


    final Handler h = new Handler();
    final int delay = 2000; //milliseconds

    String msg = "Android : ";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
//    private String appState.USERNAME = "";
//    private String appState.PASSCODE = "";
//    private String appState.ENDPOINT = "";
//    private String appState.COMPANY_ID = "";
//    private String  appState.TEST_ID = "";
//    private String testTitle = "";
//    private String appState.TEST_DATE = "";
//    private String appState.TEST_DESC = "";
    int SERVERTIME = 0;

    String []mTEST_SECTION_DELIMITERS;
    String []mTEST_SECTION_DELIMITER_TAGS;
    int mCURRECT_SECTION_INDEX_IN_DELIMIERTS_ARRAY;

    String NEXT_SECTION_TITLE;

    JSONArray participantsJSONArray = new JSONArray();
    DatabaseHelper db;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        db = new DatabaseHelper(TestAdminActivity.this);
        db.getReadableDatabase();

        requestQueue = Volley.newRequestQueue(TestAdminActivity.this);
        getServerTime();

        getCurrentTestInfo();

        //mTEST_SECTION_DELIMITERS
//        mTEST_SECTION_DELIMITERS=appState.TEST_SECTIONS.split(",");
//        mTEST_SECTION_DELIMITER_TAGS=appState.TEST_SECTIONS_TAGS.split(",");

        Log.e("ADS", "USER : " + appState.USERNAME);
        Log.e("ADS", "PASSCODE : " + appState.PASSCODE );
        Log.e("ADS", "EndPoint " + appState.ENDPOINT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_admin);
        Log.d(msg, "The onCreate() event");


        TextView testTitleTextView = (TextView) findViewById(R.id.test_title);
        testTitleTextView.setText("Test Title : " +  appState.TEST_TITLE);

        TextView testIDTextView = (TextView) findViewById(R.id.test_ID);
        testIDTextView.setText("Test ID : " + appState.TEST_ID);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new TestAdminActivity.MyAdapter(participantsJSONArray);
        mRecyclerView.setAdapter(mAdapter);
        getParticipants();

       // getActionBar().setTitle("Hello world App");
       // getSupportActionBar().setTitle("Hello world App");
    }

    public void getCurrentTestInfo(){

        String URL = "https://"+ appState.ENDPOINT + "/v1/companies/test/info/" +  appState.TEST_ID;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", appState.USERNAME);
        params.put("passcode", appState.PASSCODE);

        JsonObjectRequest request_json = new JsonObjectRequest(URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject Info = new JSONObject();
                        try {
                            Info = response.getJSONObject("info");
                            NEXT_SECTION_TITLE = Info.getString("next_section_title");

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

        //RequestQueue requestQueue = Volley.newRequestQueue(TestAdminActivity.this);
        request_json.setShouldCache(false);
        requestQueue.add(request_json);

    }
    public void getParticipants(){
        String URL = "https://"+ appState.ENDPOINT + "/v1/companies/test/participants/" +  appState.TEST_ID;

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", appState.USERNAME);
        params.put("passcode", appState.PASSCODE);

        JsonObjectRequest request_json = new JsonObjectRequest(URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            participantsJSONArray = response.getJSONArray("candidates");
                            TextView lame= (TextView) findViewById(R.id.participants_count);
                            lame.setText("Total participants : " + participantsJSONArray.length());
                            mAdapter.notifyDataSetChanged();
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

        //RequestQueue requestQueue = Volley.newRequestQueue(TestAdminActivity.this);
        request_json.setShouldCache(false);
        requestQueue.add(request_json);
    }


    public void startTest(View v){
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);

        alert.setMessage("Are you sure you want to START the test from section : " + NEXT_SECTION_TITLE);
        alert.setTitle("START test");

        alert.setPositiveButton("Start", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int whichButton) {

//                String URL = "https://"+ appState.ENDPOINT + "/v1/companies/test/set/" +  appState.TEST_ID;
                String URL = "https://"+ appState.ENDPOINT + "/v2/companies/test/set/" +  appState.TEST_ID;

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("username", appState.USERNAME);
                params.put("passcode", appState.PASSCODE);
                params.put("action","start");
//                params.put("page_number",mTEST_SECTION_DELIMITERS[0]);
//                mCURRECT_SECTION_INDEX_IN_DELIMIERTS_ARRAY = 0;
                JsonObjectRequest request_json = new JsonObjectRequest(URL, new JSONObject(params), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                getCurrentTestInfo();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(TestAdminActivity.this);
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



    public void completeTest(View v){
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);

        alert.setMessage("Are you sure you want to complete the test. This stops the test and emails the results to you");
        alert.setTitle("STOP test");

        alert.setPositiveButton("Start", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int whichButton) {

//                String URL = "https://"+ appState.ENDPOINT + "/v1/companies/test/set/" +  appState.TEST_ID;
                String URL = "https://"+ appState.ENDPOINT + "/v2/companies/test/set/" +  appState.TEST_ID;

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("username", appState.USERNAME);
                params.put("passcode", appState.PASSCODE);
                params.put("action","complete");
//                params.put("page_number",mTEST_SECTION_DELIMITERS[mTEST_SECTION_DELIMITERS.length-1]);
//                params.put("testEndFlag","true");
//                mCURRECT_SECTION_INDEX_IN_DELIMIERTS_ARRAY = mTEST_SECTION_DELIMITERS.length-1;
                JsonObjectRequest request_json = new JsonObjectRequest(URL, new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        getCurrentTestInfo();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(TestAdminActivity.this);
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





    public void resetTest(View v){
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);

        alert.setMessage("Are you sure you want to Reset the test. This will take all participants tablets back to the launch screen");
        alert.setTitle("Reset Test");

        alert.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int whichButton) {


//                String URL = "https://"+ appState.ENDPOINT + "/v1/companies/test/reset/" +  appState.TEST_ID;
                String URL = "https://"+ appState.ENDPOINT + "/v2/companies/test/set/" +  appState.TEST_ID;

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("username", appState.USERNAME);
                params.put("passcode", appState.PASSCODE);
                params.put("action","reset");
//                params.put("page_number",mTEST_SECTION_DELIMITERS[mTEST_SECTION_DELIMITERS.length-1]);
//                mCURRECT_SECTION_INDEX_IN_DELIMIERTS_ARRAY = mTEST_SECTION_DELIMITERS.length-1;
//                appState.CURRENT_TEST_SECTION_SLIDE_INDEX_NUMBER = 0;

                JsonObjectRequest request_json = new JsonObjectRequest(URL, new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        getCurrentTestInfo();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(TestAdminActivity.this);
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





    public void nextSection(View v){
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);


        alert.setMessage("Are you sure you want to Start the next section : " + NEXT_SECTION_TITLE);
        alert.setTitle("Next Section");


        alert.setPositiveButton("Start", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int whichButton) {

//                String URL = "https://"+ appState.ENDPOINT + "/v1/companies/test/set/" +  appState.TEST_ID;
                String URL = "https://"+ appState.ENDPOINT + "/v2/companies/test/set/" +  appState.TEST_ID;

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("username", appState.USERNAME);
                params.put("passcode", appState.PASSCODE);
                params.put("action","next");
//                params.put("action",mTEST_SECTION_DELIMITERS[appState.CURRENT_TEST_SECTION_SLIDE_INDEX_NUMBER+1]);
//                appState.CURRENT_TEST_SECTION_SLIDE_INDEX_NUMBER++;

//                TextView lame= (TextView) findViewById(R.id.participants_count);
//                lame.setText("Total participants : " + mTEST_SECTION_DELIMITERS[appState.CURRENT_TEST_SECTION_SLIDE_INDEX_NUMBER]);

                JsonObjectRequest request_json = new JsonObjectRequest(URL, new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        getCurrentTestInfo();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(TestAdminActivity.this);
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








    public void getServerTime(){
        String URL = "https://"+ appState.ENDPOINT + "/v1/time";

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", appState.USERNAME);
        params.put("passcode", appState.PASSCODE);

        JsonObjectRequest request_json = new JsonObjectRequest(URL, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    SERVERTIME = response.getInt("time");
                    startTimer();
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

        RequestQueue requestQueue = Volley.newRequestQueue(TestAdminActivity.this);
        request_json.setShouldCache(false);
        requestQueue.add(request_json);
    }

    public void startTimer(){

        h.postDelayed(new Runnable(){
            public void run(){
                //do something
                SERVERTIME++;
                Log.e("ADS", "SERVER TIME IS : " + SERVERTIME);
                getParticipants();
                h.postDelayed(this, delay);
            }
        }, delay);
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


//    @Override
//    public void onBackPressed() {
//    }

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

    public class MyAdapter extends RecyclerView.Adapter<TestAdminActivity.MyAdapter.ViewHolder> {

        public MyAdapter(JSONArray testsJSONArray) {
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            CardView cv;
            public TextView info1;
            public TextView info2;
            public TextView info3;

            ViewHolder(View itemView) {
                super(itemView);
                cv = (CardView) itemView.findViewById(R.id.card_view);
                info1 = (TextView) itemView.findViewById(R.id.participants_name);
                info2 = (TextView) itemView.findViewById(R.id.participants_email);
                info3 = (TextView) itemView.findViewById(R.id.question_and_section);

            }

            public ViewHolder(TextView v) {
                super(v);
                info1 = v;
                info2 = v;
                info3 = v;
            }
        }

        // Create new views (invoked by the layout manager)
        @Override
        public TestAdminActivity.MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.participant_cardview, parent, false);

            // set the view's size, margins, paddings and layout parameters
            TestAdminActivity.MyAdapter.ViewHolder vh = new TestAdminActivity.MyAdapter.ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(TestAdminActivity.MyAdapter.ViewHolder holder, int position) {
            // - get element from your dataset at this position

                // render cells off the back of tests
                JSONObject n = new JSONObject();
                try {
                    n = (JSONObject) participantsJSONArray.get(position);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String id = null;
                String first = null;
                String last= null;
                String email= null;
                String current_question= null;
                String current_section = null;

                try {

                 id = n.getString("candidate_id");
                 first = n.getString("candidate_first");
                    last = n.getString("candidate_last");
                    email = n.getString("candidate_email");
                    current_question =  n.getString("current_question");
                    current_section = n.getString("current_section");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                holder.info1.setText(first + " " + last);
                holder.info2.setText(email);
                holder.info3.setText("Current Section : " + current_section + " | Current Question : " + current_question);


        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return participantsJSONArray.length();
        }
    }



    public void showAddParticipant(View v){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        final Context context = alert.getContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setPadding(10,10,10,10);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText first = new EditText(context);
        first.setHint("first name");
        first.setText("Elliot");
        layout.addView(first);

        final EditText last = new EditText(context);
        last.setHint("Last name");
        last.setText("Campbelton");
        layout.addView(last);

        final EditText email = new EditText(context);
        email.setHint("email address");
        email.setText("e.b.campbelton@gmail.com");
        layout.addView(email);

        final EditText dob = new EditText(context);
        dob.setHint("DoB");
        dob.setText("2/3/1993");
        layout.addView(dob);

        final EditText other = new EditText(context);
        other.setHint("other");
        other.setText("smashing");
        layout.addView(other);


        alert.setMessage("Add detials for new test participant");
        alert.setTitle("New Test Participant");

        alert.setView(layout);


        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String URL = "https://"+ appState.ENDPOINT + "/v1/companies/test/"+ appState.TEST_ID+"/participants/add";

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("username", appState.USERNAME);
                params.put("passcode", appState.PASSCODE);

                params.put("first", String.valueOf(first.getText()));
                params.put("last", String.valueOf(last.getText()));
                params.put("email", String.valueOf(email.getText()));
                params.put("dob", String.valueOf(dob.getText()));
                params.put("other", String.valueOf(other.getText()));


                JsonObjectRequest request_json = new JsonObjectRequest(URL, new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                       Log.e("ADS","Added uer");

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(TestAdminActivity.this);
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


}