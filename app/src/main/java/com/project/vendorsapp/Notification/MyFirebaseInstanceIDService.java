package com.project.vendorsapp.Notification;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    public void onTokenRefresh() {
        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        sp = getSharedPreferences("config_info", 0);
        editor = sp.edit();
        editor.putString("TOKEN", refreshedToken);
        editor.commit();
        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);
       // sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        sp =  getSharedPreferences("config", 0);
        editor = sp.edit();
        editor.putString("TOKEN", token);
        editor.commit();
    }
}
