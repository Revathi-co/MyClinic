package com.example.myclinic;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessageService  extends com.google.firebase.messaging.FirebaseMessagingService {

        String TAG ="FirebaseMessageService" ;

@Override
public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        }


}
