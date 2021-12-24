package com.amplify.amplify;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserState;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.client.results.SignInResult;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class AuthActivity extends AppCompatActivity {

    private final String TAG = AuthActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Button signin_button = findViewById(R.id.signin_button); // 로그인 버튼
        TextView signup_button = findViewById(R.id.signup_button); // 회원가입 버튼
        TextView forgot_password = findViewById(R.id.forgot_password); // 비밀번호를 잊어버리셨나요?


        // 로그인이 되어있는지 확인
        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {
            @Override
            public void onResult(UserStateDetails userStateDetails) {
                // 로그인이 되어있으면 MainActivity 로 이동
                if (userStateDetails.getUserState() == UserState.SIGNED_IN) {
                    Intent i = new Intent(AuthActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
            @Override
            public void onError(Exception e) {
                Log.e(TAG, e.toString());
            }
        });

        // 로그인 버튼
        signin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignIn();
            }
        });

        // 회원가입 버튼
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AuthActivity.this, SignUpActivity.class);
                startActivity(i);
                finish();
            }

        });

        // 비밀번호를 잊어버리셨나요?
        forgot_password.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(AuthActivity.this, ForgotActivity.class);
                startActivity(i);
                finish();
            }

        });
    }


    // 로그인 함수
    private void showSignIn() {

        // 아이디 비밀번호 순
        EditText login_id = findViewById(R.id.login_id);
        EditText login_pw = findViewById(R.id.login_pw);

        String username = login_id.getText().toString();
        String password = login_pw.getText().toString();


        AWSMobileClient.getInstance().signIn(username, password, null, new Callback<SignInResult>() {
            @Override
            public void onResult(final SignInResult signInResult) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Sign-in" + signInResult.getSignInState());
                        switch (signInResult.getSignInState()) {
                            case DONE:
                                Toast.makeText(getApplicationContext(), "Sign-in Finish.", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(AuthActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                                break;
                        }
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Sign-in error", e);
            }
        });
    }
}