package com.apptitudedigitalsolutions.ads;

/**
 * Created by Elliot on 19/10/2016.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import database.DatabaseHelper;

public class ScreenSlidePagerActivity extends FragmentActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 5;


    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        DatabaseHelper db = new DatabaseHelper(ScreenSlidePagerActivity.this);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    public void testTransition(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void showLogin(View v){

//            LinearLayout rootView = (LinearLayout)findViewById(R.id.rootLayout);
//            PopoverView popoverView = new PopoverView(this, R.layout.pop_over_view);
//            popoverView.setContentSizeForViewInPopover(new Point(320, 340));
//            //popoverView.setDelegate(this);
//            popoverView.showPopoverFromRectInViewGroup(rootView, PopoverView.getFrameForView(v), PopoverView.PopoverArrowDirectionAny, true);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        Context context = alert.getContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setPadding(10,10,10,10);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText email = new EditText(context);
        email.setHint("email");
        layout.addView(email);

        final EditText password = new EditText(context);
        password.setHint("password");
        layout.addView(password);

//        final EditText username = new EditText(this);
//        final EditText password = new EditText(this);
        alert.setMessage("Please enter your admin email and password");
        alert.setTitle("Login");

        alert.setView(layout);
        //alert.setView(password);

        alert.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                final Editable emailText = email.getText();
                final Editable passwordText = password.getText();

                String REGISTER_URL = "";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
//                                Toast.makeText(ScreenSlidePagerActivity.this,response,Toast.LENGTH_LONG).show();
                                JSONObject mainObject = null;
                                try {
                                    mainObject = new JSONObject(response);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                String  passcode = mainObject.getJsonString("passcode");
                                String  company_id = mainObject.getJsonString("company_id");


                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(ScreenSlidePagerActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                            }
                        }){
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("email", String.valueOf(emailText));
                        params.put("password", String.valueOf(passwordText));
                        return params;
                    }

                };

                RequestQueue requestQueue = Volley.newRequestQueue(ScreenSlidePagerActivity.this);
                requestQueue.add(stringRequest);

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();
    }

    public void loginuser (View view){

    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new ScreenSlidePageFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}