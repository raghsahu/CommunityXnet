package com.logical.communityapp.Activity.Login_Signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.logical.communityapp.Activity.MainActivity;
import com.logical.communityapp.Preference.SessionManager;
import com.logical.communityapp.R;
import com.logical.communityapp.firebase_service.Constants;

public class SplashActivity extends AppCompatActivity {

    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sessionManager=new SessionManager(SplashActivity.this);




        displayFirebaseRegId();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                try {

                    if (sessionManager.isLoggedIn()) {

                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    finish();

                    } else {
                        Intent  intent = new Intent(SplashActivity.this, Welcome_Activity.class);
                        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                finish();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("$$Exception**=" + e);
                }


            }
        }, 3000);

    }

    private void displayFirebaseRegId() {

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                // send it to server
                sessionManager.saveToken(token);
                Log.e("refresh_tokentoken",token);
            }
        });

        Log.e("save_token",sessionManager.getTokenId());
       // Log.e("fire_token", ""+FirebaseInstanceId.getInstance().getInstanceId());
      // Log.d("FCMToken", "token "+ FirebaseInstanceId.getInstance().getToken());

    }

}
