package com.apptitudedigitalsolutions.ads.AssessmentCenter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

public class ACAdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ADSApplication appState = ((ADSApplication)this.getApplication());

    DatabaseHelper db;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    JSONArray assessmentcentresParticipantsJSONArray = new JSONArray();

    String msg = "Android : ";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ac_admin);
        Log.d(msg, "The onCreate() event");
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#6666ff")));

        db = new DatabaseHelper(ACAdminActivity.this);
        db.getReadableDatabase();
//        ArrayList<String> userdata = db.getUsernamePasscodeEndpointArray();
//        appState.USERNAME = userdata.get(4);
//        appState.PASSCODE = userdata.get(2);
//        appState.ENDPOINT = userdata.get(0);
//        appState.COMPANY_ID = userdata.get(1);

        // get values from intent and parse into usage objects
//        Intent intent = getIntent();
//        appState.ACID = intent.getStringExtra("id");
//        appState.AC_TITLE = intent.getStringExtra("title");
//        appState.AC_CONDUCTED_DATE= intent.getStringExtra("to_be_conducted_on");
//        appState.AC_DESC= intent.getStringExtra("description");
//        appState.AC_ACTIVITY_IDS= intent.getStringExtra("activity_ids");
//        appState.AC_ACTIVITY_TYPES= intent.getStringExtra("activity_types");

        //set other on screen lables and shit
        TextView title = (TextView) findViewById(R.id.ac_title);
        title.setText(appState.AC_TITLE);

        TextView activites = (TextView) findViewById(R.id.ac_activities);
        activites.setText(appState.AC_ACTIVITY_TYPES);

        // create all of that adapter shit
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ACAdminActivity.MyAdapter(assessmentcentresParticipantsJSONArray);
        mRecyclerView.setAdapter(mAdapter);


        // start slow poll for get all participants
        getACparticipants();
    }

    // add method for adding a participant and hook the old gurl up to that filthy button fam.
    public void AddACParticipant(View v){

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

        alert.setMessage("Add detials for new test participant");
        alert.setTitle("New Test Participant");

        alert.setView(layout);


        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String URL = "https://"+ appState.ENDPOINT + "/v1/companies/assessmentcentre/addcandidate/"+appState.ACID;
                Log.e("ADS",URL);
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("username", appState.USERNAME);
                params.put("passcode", appState.PASSCODE);

                params.put("first", String.valueOf(first.getText()));
                params.put("last", String.valueOf(last.getText()));
                params.put("email", String.valueOf(email.getText()));
                params.put("dob", String.valueOf(dob.getText()));
                params.put("other","");
                params.put("set_activities", appState.AC_ACTIVITY_IDS);

                JsonObjectRequest request_json = new JsonObjectRequest(URL, new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("ADS","Added uer");
                        getACparticipants();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(ACAdminActivity.this);
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

    // add method for getting all partcicpatns
    public void getACparticipants(){
        // retrieve the list of participats
        String URL = "https://"+ appState.ENDPOINT + "/v2/companies/assessmentcentres/candidates";
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
                            assessmentcentresParticipantsJSONArray = response.getJSONArray("candidates");
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

        RequestQueue requestQueue = Volley.newRequestQueue(ACAdminActivity.this);
        request_json.setShouldCache(false);
        requestQueue.add(request_json);// reload the list view
    }


    /** Called when the activity is about to become visible. */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(msg, "The onStart() event");
    }

    /** Called when the activity has become visible. */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(msg, "The onResume() event");
    }

    /** Called when another activity is taking focus. */
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(msg, "The onPause() event");
    }

    /** Called when the activity is no longer visible. */
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(msg, "The onStop() event");
    }

    /** Called just before the activity is destroyed. */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(msg, "The onDestroy() event");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }


    // create list adapter showing each with name , email and sections complete


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

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
                                n = (JSONObject) assessmentcentresParticipantsJSONArray.get(pos);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            // revisited when returned from get particiapnts is defined
                            String candidate_id = null;
                            String candidate_first = null;
                            String candidate_last = null;
                            String candidate_email = null;
                            String candidate_role = null;
                            String other = null;
                            String set_activities = null;
                            String completed_activities = null;

                            try {
                                candidate_id = n.getString("candidate_id");
                                candidate_first = n.getString("candidate_first");
                                candidate_last = n.getString("candidate_last");
                                candidate_email = n.getString("candidate_email");
                                candidate_role = n.getString("candidate_role");
                                other= n.getString("other");
                                set_activities= n.getString("set_activities");
                                completed_activities= n.getString("completed_activities");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            appState.AC_CURRENT_CANDIDATE_ID = candidate_id;
                            appState.AC_CANDIDATE_FIRST = candidate_first;
                            appState.AC_CANDIDATE_LAST = candidate_last;
                            appState.AC_CANDIDATE_EMAIL = candidate_email;
                            appState.AC_CANDIDATE_ROLE = candidate_role;

                            appState.AC_ACTIVITY_IDS= set_activities;
                            appState.AC_COMPLETED_ACTIVITIES = completed_activities;

                            appState.AC_COMPLETED_ACTIVITIES_ARRAY = new String[0];
                            appState.AC_SET_ACTIVITIES_IDS_ARRAY = new String[0];
                            appState.AC_SET_ACTIVITIE_TYPES_ARRAY = new String[0];
                            appState.AC_COMPLETED_ACTIVITIES_TAGS_ARRAY= new ArrayList<String>();

                        // launch admin view for test
                            Intent intent = new Intent(ACAdminActivity.this, ACSelectActivityActivity.class);
                            startActivity(intent);

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
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ac_cardview, parent, false);

            // set the view's size, margins, paddings and layout parameters
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

                JSONObject n = new JSONObject();
                try {
                     n = (JSONObject) assessmentcentresParticipantsJSONArray.get(position);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String name = null;
                String email = null;
                String completed_activities = null;

                try {
                    name = n.getString("candidate_first") + " " + n.getString("candidate_last");
                    email = n.getString("candidate_email");
                    completed_activities = n.getString("completed_activities");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                holder.info1.setText(name);
                holder.info2.setText(email);
                holder.info3.setText(completed_activities);
            }

        @Override
        public int getItemCount() {
            return  assessmentcentresParticipantsJSONArray.length();
        }

    }

}
