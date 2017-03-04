package com.apptitudedigitalsolutions.ads.Landing;

/**
 * Created by Elliot on 19/10/2016.
 */

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import com.apptitudedigitalsolutions.ads.Application.ADSApplication;
import com.apptitudedigitalsolutions.ads.Main.MainActivity;
import com.apptitudedigitalsolutions.ads.R;
import com.apptitudedigitalsolutions.ads.TestCenter.TestActivity;
import com.apptitudedigitalsolutions.ads.DatabaseUtils.DatabaseHelper;

public class LandingActivity extends FragmentActivity {

    ADSApplication appState;

    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 5;
    DatabaseHelper db;
    public final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
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
        appState = ((ADSApplication)this.getApplication());
         db = new DatabaseHelper(LandingActivity.this);
         db.getReadableDatabase();
        ArrayList<String> userdata = db.getUsernamePasscodeEndpointArray();

        if(userdata.size() > 0) {
//            if (userdata.get(userdata.size() - 1) != "") {
                transitionToMain();
//            }
        }

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(LandingActivity.this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(LandingActivity.this,
                    Manifest.permission.RECORD_AUDIO)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(LandingActivity.this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(LandingActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(LandingActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(LandingActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }


        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(LandingActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(LandingActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(LandingActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                // Check if this is the page you want.
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    // Here, thisActivity is the current activity
                    if (ContextCompat.checkSelfPermission(LandingActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(LandingActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                            // Show an explanation to the user *asynchronously* -- don't block
                            // this thread waiting for the user's response! After the user
                            // sees the explanation, try again to request the permission.

                        } else {

                            // No explanation needed, we can request the permission.

                            ActivityCompat.requestPermissions(LandingActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                            // app-defined int constant. The callback method gets the
                            // result of the request.
                        }
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
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

    public void transitionToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void showLogin(View v){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        final Context context = alert.getContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setPadding(10,10,10,10);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText email = new EditText(context);
        email.setHint("email");
        email.setText("elliot_admin");
        layout.addView(email);

        final EditText password = new EditText(context);
        password.setHint("password");
        //password.setText("smashing");
        layout.addView(password);

        alert.setMessage("Please enter your admin email and password");
        alert.setTitle("Login");

        alert.setView(layout);


        alert.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String URL = "http://apptitudedigitalsolutions.com:8080/v1/user/login";

// Post params to be sent to the server
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("username", String.valueOf(email.getText()));
                params.put("password", String.valueOf(password.getText()));

                JsonObjectRequest request_json = new JsonObjectRequest(URL, new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {


                                try {
                                    String  passcode = response.getString("passcode");
                                    String  company_id = response.getString("company_id");
                                    String  name = response.getString("name");
                                    String  endpoint = response.getString("endpoint");
                                    Log.i("returned_values",company_id + " " + passcode + " and name " + name);
                                    // store these values in the DB.
                                    db.getReadableDatabase();
                                    String username = String.valueOf(email.getText());
                                    db.insertuserData(username,name,passcode,company_id,endpoint);
                                    appState.refreshCredentials();

                                    // go into main
                                    Intent intent = new Intent(context, MainActivity.class);
                                    startActivity(intent);

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

                RequestQueue requestQueue = Volley.newRequestQueue(LandingActivity.this);
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






    public void showTestSart(View v){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        final Context context = alert.getContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setPadding(10,10,10,10);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText testIDEntry = new EditText(context);
        testIDEntry.setHint("Please enter the test ID");
        layout.addView(testIDEntry);

        alert.setMessage("Ready to start a test? Enter the test ID below as provided by the test administrator.");
        alert.setTitle("Test Access");

        alert.setView(layout);


        alert.setPositiveButton("Lauch test", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String URL = "http://apptitudedigitalsolutions.com:8080/v1/companies/test/validation/" + testIDEntry.getText();
                Log.e("ADS test info endoirnt ", URL);
// Post params to be sent to the server
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("testID", String.valueOf(testIDEntry.getText()));

                JsonObjectRequest request_json = new JsonObjectRequest(URL, new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                Log.e("ADS test info", String.valueOf(response));
                                Intent intent = new Intent(context, TestActivity.class);
                                try {
                                    intent.putExtra("test_id",response.getString("test_id"));
                                    intent.putExtra("test_title",response.getString("test_title"));
                                    intent.putExtra("description",response.getString("description"));
                                    intent.putExtra("test_ids_in_series",response.getString("test_ids_in_series"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                startActivity(intent);

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(LandingActivity.this);
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





    /**
     * A simple pager adapter that represents 5 LandingActivityPageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new LandingActivityPageFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}