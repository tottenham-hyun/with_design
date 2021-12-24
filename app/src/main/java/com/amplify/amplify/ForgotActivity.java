package com.amplify.amplify;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.results.ForgotPasswordResult;
import com.amazonaws.mobile.client.results.ForgotPasswordState;

public class ForgotActivity extends AppCompatActivity {

    private final String TAG = AuthActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        Button code_button = findViewById(R.id.code_button); // 인증 코드 버튼
        Button new_pw_button = findViewById(R.id.new_pw_button); // 비밀번호 재설정 버튼


        // 인증 버튼
        code_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 인증코드 확인
                EditText pw_signUpUsername = (EditText) findViewById(R.id.pw_signUpUsername);
                String username = pw_signUpUsername.getText().toString();

                AWSMobileClient.getInstance().forgotPassword(username, new Callback<ForgotPasswordResult>() {
                    @Override
                    public void onResult(final ForgotPasswordResult result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "forgot password state: " + result.getState());
                                if (result.getState() == ForgotPasswordState.CONFIRMATION_CODE) {
                                    Toast.makeText(getApplicationContext(), "이메일 주소로 인증 코드가 전송되었습니다.", Toast.LENGTH_SHORT).show();


                                } else {
                                    Log.e(TAG, "un-supported forgot password state");
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "forgot password error", e);
                    }

                });
            }
        });



        // 비밀번호 재설정 버튼
        new_pw_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // 인증코드, 비밀번호 재설정
                EditText pw_code_name = (EditText) findViewById(R.id.pw_code_name);
                EditText new_pw_name = (EditText) findViewById(R.id.new_pw_name);


                String confirm_code = pw_code_name.getText().toString();
                String new_password = new_pw_name.getText().toString();


                AWSMobileClient.getInstance().confirmForgotPassword(new_password, confirm_code, new Callback<ForgotPasswordResult>() {
                    @Override
                    public void onResult(final ForgotPasswordResult result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "forgot password state: " + result.getState());
                                if (result.getState() == ForgotPasswordState.DONE) {

                                    // 비밀번호가 재설정 되었으면 로그인 창으로 이동
                                    Toast.makeText(getApplicationContext(), "비밀번호가 재설정 되었습니다.", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(ForgotActivity.this, AuthActivity.class);
                                    startActivity(i);
                                    finish();

                                } else {
                                    Log.e(TAG, "un-supported forgot password state");
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "forgot password error", e);
                    }
                });
            }
        });
    }

    private final long FINISH_INTERVAL_TIME = 1000;
    private long backPressedTime = 0;

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)
        {
            Intent i = new Intent(ForgotActivity.this, AuthActivity.class);
            startActivity(i);
            finish();
        }
        else
        {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }
}