package com.example.amplify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button signOut_button = findViewById(R.id.signOut_button); // 로그아웃 버튼

        // 로그아웃 버튼
        signOut_button.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {
                    @Override
                    public void onResult(UserStateDetails userStateDetails) {
                        // 로그아웃 후 로그인 창으로 이동
                        AWSMobileClient.getInstance().signOut();
                        Intent i = new Intent(MainActivity.this, AuthActivity.class);
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
            }
        });
    }
}