package com.apptitudedigitalsolutions.ads.Application;

/**
 * Created by Elliot on 16/01/2017.
 */


import android.app.Application;
import android.content.res.Configuration;

import java.util.ArrayList;

import com.apptitudedigitalsolutions.ads.DatabaseUtils.DatabaseHelper;

import org.json.JSONArray;


public class ADSApplication extends Application {

    // DatabaseHelper db;
    // general globals
    /*
    * These properties are set at the start of the applications lide cycle and are unlikly to change throughout
    * */
    public static String USERNAME = "";
    public static String PASSCODE = "";
    public static String ENDPOINT = "";
    public static String COMPANY_ID = "";

    // AC spercific locals
    /*
    * These properties are set when an asessemtn center is picked in the first case, then the remainder
    * are set when a particular activity within the accessment center is selected.
    * */
    public static String ACID= "";
    public static String AC_TITLE= "";
    public static String AC_CONDUCTED_ON= "";
    public static String AC_DESC= "";

    public static String AC_ACTIVITY_IDS= "";
    public static String AC_ACTIVITY_TYPES= "";

    public static String AC_CURRENT_ACTIVITY_TYPE= "";
    public static String AC_CURRENT_CANDIDATE_ID= "";

    public static String AC_SET_ACTIVITIE_TAGS= "";
    public static String AC_COMPLETED_ACTIVITIES= "";

    public static String [] AC_SET_ACTIVITIE_TYPES_ARRAY;
    public static String [] AC_COMPLETED_ACTIVITIES_ARRAY;
    public static ArrayList<String> AC_COMPLETED_ACTIVITIES_TAGS_ARRAY = new ArrayList<String>();

    public static String [] AC_SET_ACTIVITIES_IDS_ARRAY;

    public static String AC_CANDIDATE_FIRST = "";
    public static String AC_CANDIDATE_LAST= "";
    public static String AC_CANDIDATE_EMAIL= "";
    public static String AC_CANDIDATE_ROLE = "";



    // Test spercific globals
    /*
    * These properties are sert when a test is started by a participant
    * and or when an admin enters the admin screen.
    * */
    public static String TEST_ID = ""; // This is set in the landing activity and then loaded locally from there.
    public static String TEST_TITLE= "";
    public static String TEST_DATE= "";
    public static String TEST_DESC= "";

    public static String TEST_SECTIONS= "";
    public static String TEST_SECTIONS_TAGS= "";

    public static int CURRENT_TEST_SECTION_SLIDE_INDEX_NUMBER;



    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    @Override
    public void onCreate() {
        super.onCreate();
        // Required initialization logic here!
        // Set Globals

        refreshCredentials();
    }

    public void refreshCredentials(){
        DatabaseHelper db;
        db = new DatabaseHelper(ADSApplication.this);
        db.getReadableDatabase();
        ArrayList<String> userdata = db.getUsernamePasscodeEndpointArray();

        if(userdata.size() > 0) {
            USERNAME = userdata.get(4);
            PASSCODE = userdata.get(2);
            ENDPOINT = userdata.get(0);
            COMPANY_ID = userdata.get(1);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
