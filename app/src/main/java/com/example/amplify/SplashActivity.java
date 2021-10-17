package com.example.amplify;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

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