package com.apptitudedigitalsolutions.ads.Main;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.HashMap;

import com.apptitudedigitalsolutions.ads.Application.ADSApplication;
import com.apptitudedigitalsolutions.ads.AssessmentCenter.ACAdminActivity;
import com.apptitudedigitalsolutions.ads.R;
import com.apptitudedigitalsolutions.ads.Landing.LandingActivity;
import com.apptitudedigitalsolutions.ads.TestCenter.TestAdminActivity;
import com.apptitudedigitalsolutions.ads.DatabaseUtils.DatabaseHelper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ADSApplication appState = ((ADSApplication)this.getApplication());

    DatabaseHelper db;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String mToggleState = "TESTS";
    JSONArray assessmentcentresJSONArray = new JSONArray();
    JSONArray testsJSONArray = new JSONArray();
    Button acs;
    Button tests;
    LinearLayout empty_list_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHelper(MainActivity.this);
        db.getReadableDatabase();

        Log.e("ADS", "USER : " + appState.USERNAME);
        Log.e("ADS", "PASSCODE : " +  appState.PASSCODE );
        Log.e("ADS", "EndPoint " +  appState.ENDPOINT);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        empty_list_layout = (LinearLayout) findViewById(R.id.empty_list_layout);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#6666ff")));

        acs = (Button) findViewById(R.id.acs_button);
        acs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               acsButtonTap(view);
            }
        });


        tests = (Button) findViewById(R.id.tests_button);
        tests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testsButtonTap(view);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(testsJSONArray);
        mRecyclerView.setAdapter(mAdapter);
        getAllTests();
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_phone_us) {

        } else if (id == R.id.nav_tests) {
            getSupportActionBar().setTitle("ADS - Test Centers");
            tests.performClick();
        } else if (id == R.id.nav_acs) {
            getSupportActionBar().setTitle("ADS - Assessment Centers");
            acs.performClick();
        } else if (id == R.id.nav_email_us) {

        } else if (id == R.id.nav_logout) {
            Log.e("ADS","LOGOUT TAPPED");
            db.nukeUserData();
            Intent intent = new Intent(this, LandingActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void testsButtonTap(View v) {
        mToggleState = "TESTS";
        getAllTests();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#6666ff")));
    }


    public void acsButtonTap(View v) {
        mToggleState = "ACS";
        getAllACs();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#6a9afb")));
    }

    public void getAllACs(){
        String URL = "http://"+  appState.ENDPOINT + "/v1/companies/assessmentcentres/" +  appState.COMPANY_ID;
        Log.v("ADS", "URL is  .. " + URL);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", appState.USERNAME);
        params.put("passcode",  appState.PASSCODE);

        JsonObjectRequest request_json = new JsonObjectRequest(URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            assessmentcentresJSONArray = response.getJSONArray("acs");
                            mAdapter.notifyDataSetChanged();
                            if(assessmentcentresJSONArray.length() == 0){
                                getSupportActionBar().setTitle("ADS");
                                // show no tests background
                                mRecyclerView.setVisibility(View.GONE);
                                empty_list_layout.setVisibility(View.VISIBLE);
                            }else{
                                mRecyclerView.setVisibility(View.VISIBLE);
                                empty_list_layout.setVisibility(View.GONE);
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

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        request_json.setShouldCache(false);
        requestQueue.add(request_json);
    }

    public void getAllTests(){
        getSupportActionBar().setTitle("ADS - Test Centers");
        String URL = "http://"+  appState.ENDPOINT + "/v1/companies/tests/" +  appState.COMPANY_ID;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", appState.USERNAME);
        params.put("passcode",  appState.PASSCODE);

        JsonObjectRequest request_json = new JsonObjectRequest(URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            testsJSONArray = response.getJSONArray("tests");
                            mAdapter.notifyDataSetChanged();
                            if(testsJSONArray.length() == 0){
                                getSupportActionBar().setTitle("ADS");
                                // show no tests background
                                mRecyclerView.setVisibility(View.GONE);
                                empty_list_layout.setVisibility(View.VISIBLE);
                            }else{
                                mRecyclerView.setVisibility(View.VISIBLE);
                                empty_list_layout.setVisibility(View.GONE);
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

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        request_json.setShouldCache(false);
        requestQueue.add(request_json);
    }

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
                        if(mToggleState == "TESTS"){
                            JSONObject n = new JSONObject();
                            try {
                                n = (JSONObject) testsJSONArray.get(pos);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            String testTitle = null;
                            String conducted_on = null;
                            String description = null;
                            String id = null;
                            String sections = null;
                            String sections_tags = null;
                          try {
                                testTitle = n.getString("test_title");
                                conducted_on = n.getString("to_be_conducted_on");
                                description = n.getString("description");
                                id = n.getString("test_id");
                                sections = n.getString("section_delimiters");
                                sections_tags= n.getString("section_tags");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            // launch admin view for test
                            Intent intent = new Intent(MainActivity.this, TestAdminActivity.class);
                            appState.TEST_ID = id;
                            appState.TEST_TITLE = testTitle;
                            appState.TEST_DATE = conducted_on;
                            appState.TEST_DESC = description;
                            appState.TEST_SECTIONS = sections;
                            appState.TEST_SECTIONS_TAGS = sections_tags;
//                            intent.putExtra("id", id);
//                            intent.putExtra("test_title", testTitle);
//                            intent.putExtra("to_be_conducted_on", conducted_on);
//                            intent.putExtra("description", description);
//                            intent.putExtra("sections",sections);
//                            intent.putExtra("sections_tags",sections_tags);
                            startActivity(intent);


                        }else{

                            JSONObject n = new JSONObject();

                            try {
                                n = (JSONObject) assessmentcentresJSONArray.get(pos);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            String id = null;
                            String title = null;
                            String conducted_on = null;
                            String description = null;
                            String activity_ids = null;
                            String activity_types = null;
                            try {
                                title = n.getString("title");
                                conducted_on = n.getString("conducted_on");
                                description = n.getString("description");
                                id = n.getString("ac_id");
                                activity_ids = n.getString("activity_ids");
                                activity_types = n.getString("activity_types");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            appState.ACID = id;
                            appState.AC_TITLE = title;
                            appState.AC_CONDUCTED_ON = conducted_on;
                            appState.AC_DESC = description;
                            appState.AC_ACTIVITY_IDS = activity_ids;
                            appState.AC_ACTIVITY_TYPES = activity_types;

                            Intent intent = new Intent(MainActivity.this, ACAdminActivity.class);
//                            intent.putExtra("id", id);
//                            intent.putExtra("title", title);
//                            intent.putExtra("to_be_conducted_on", conducted_on);
//                            intent.putExtra("description", description);
//                            intent.putExtra("activity_ids", activity_ids);
//                            intent.putExtra("activity_types", activity_types);
                            startActivity(intent);
                        }

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

//        // Provide a suitable constructor (depends on the kind of dataset)
//        public MyAdapter(JSONArray[] myDataset) {
//            mDataset = myDataset;
//        }

        // Create new views (invoked by the layout manager)
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cardview, parent, false);

            // set the view's size, margins, paddings and layout parameters
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            if(mToggleState == "TESTS"){
                // render cells off the back of tests
                JSONObject n = new JSONObject();
                try {
                     n = (JSONObject) testsJSONArray.get(position);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String interview_title = null;
                String conducted_on = null;
                String description = null;

                try {
                    interview_title = n.getString("test_title");
                    conducted_on = n.getString("to_be_conducted_on");
                    description = n.getString("description");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                holder.info1.setText(interview_title);
                holder.info2.setText("To be conducted on : " + conducted_on);
                holder.info3.setText(description);
            }

            if(mToggleState == "ACS"){
                // render cells off the back of interview

                JSONObject n = new JSONObject();
                try {
                    n = (JSONObject) assessmentcentresJSONArray.get(position);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String interview_title = null;
                String conducted_on = null;
                String description = null;

                try {
                    interview_title = n.getString("title");
                    conducted_on = n.getString("conducted_on");
                    description = n.getString("description");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                holder.info1.setText(interview_title);
                holder.info2.setText("To be conducted on : " + conducted_on);
                holder.info3.setText(description);

            }



        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            if(mToggleState == "TESTS"){
                return testsJSONArray.length();
            }else {
                return assessmentcentresJSONArray.length();
            }
        }
    }

}