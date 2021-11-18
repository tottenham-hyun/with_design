package com.amplify.amplify;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.messaging.FirebaseMessaging;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        try{
            Thread.sleep(3000);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
        startActivity(new Intent(this,AuthActivity.class));
        finish();
    }
}