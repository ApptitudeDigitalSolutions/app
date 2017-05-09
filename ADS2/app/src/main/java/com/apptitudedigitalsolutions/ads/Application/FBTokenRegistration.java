package com.apptitudedigitalsolutions.ads.Application;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Elliot on 21/04/2017.
 */

public class FBTokenRegistration extends FirebaseInstanceIdService {

    ADSApplication appState = ((ADSApplication)this.getApplication());

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("ADS", "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        appState.TOKEN = refreshedToken;
    }

    public void setToken(){
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("ADS", "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        appState.TOKEN = refreshedToken;
    }

}
