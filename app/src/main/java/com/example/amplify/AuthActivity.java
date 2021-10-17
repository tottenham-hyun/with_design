package com.example.amplify;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserState;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.client.results.SignInResult;

public class AuthActivity extends AppCompatActivity {

    private final String TAG = AuthActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Button signIn_button = findViewById(R.id.signIn_button); // 로그인 버튼
        TextView signUp_button = findViewById(R.id.signUp_button); // 회원가입 버튼
        TextView forgot_Password_button = findViewById(R.id.forgot_Password_button); // 비밀번호를 잊어버리셨나요?

        // 로그인이 되어있는지 확인
        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {

            @Override
            public void onResult(UserStateDetails userStateDetails) {
                Log.i(TAG, userStateDetails.getUserState().toString());

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
        signIn_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showSignIn();
            }
        });

        // 회원가입 버튼
        signUp_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(AuthActivity.this, SignUpActivity.class);
                startActivity(i);
                finish();
            }

        });

        // 비밀번호를 잊어버리셨나요?
        forgot_Password_button.setOnClickListener(new View.OnClickListener() {

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
        EditText login_paw = findViewById(R.id.login_paw);

        String username = login_id.getText().toString();
        String password = login_paw.getText().toString();


        AWSMobileClient.getInstance().signIn(username, password, null, new Callback<SignInResult>() {
            @Override
            public void onResult(final SignInResult signInResult) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Sign-in callback state: " + signInResult.getSignInState());
                        switch (signInResult.getSignInState()) {
                            case DONE:
                                Toast.makeText(getApplicationContext(), "Sign-in done.", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(AuthActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                                break;
                            case SMS_MFA:
                                Toast.makeText(getApplicationContext(), "Please confirm sign-in with SMS.", Toast.LENGTH_SHORT).show();
                                break;
                            case NEW_PASSWORD_REQUIRED:
                                Toast.makeText(getApplicationContext(), "Please confirm sign-in with new password.", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), "Unsupported sign-in confirmation: " + signInResult.getSignInState(), Toast.LENGTH_SHORT).show();
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