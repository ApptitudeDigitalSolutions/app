package com.apptitudedigitalsolutions.ads.Application;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class Notifications extends FirebaseMessagingService {
    public Notifications() {
    }

    ADSApplication appState = ((ADSApplication)this.getApplication());


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        Log.d("ADS", "From: " + remoteMessage.getFrom());
        Log.d("ADS", "Notification Message Body: " + remoteMessage.getNotification().getBody());

        appState.BACK_TO_LOGIN = true;

    }
}
