package com.example.amplify;

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
import com.amazonaws.mobile.client.results.SignUpResult;
import com.amazonaws.mobile.client.results.UserCodeDeliveryDetails;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    String TAG = AuthActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        Button signUp_button2 = findViewById(R.id.signUp_button2); // 회원 가입 버튼

        // 회원 가입 버튼
        signUp_button2.setOnClickListener(new View.OnClickListener() {


            // 회원가입
            @Override
            public void onClick(View v) {

                // 이름, 아이디(이메일), 비밀번호 순
                EditText signUpName =  (EditText)findViewById(R.id.signUpName);
                EditText signUpUsername =  (EditText)findViewById(R.id.signUpUsername);
                EditText signUpPassword =  (EditText)findViewById(R.id.signUpPassword);

                String name = signUpName.getText().toString();
                String username = signUpUsername.getText().toString();
                String password = signUpPassword.getText().toString();


                final Map<String, String> attributes = new HashMap<>();
                attributes.put("name", name);
                attributes.put("email", username);
                Log.d("TTT",attributes.toString());


                AWSMobileClient.getInstance().signUp(username, password, attributes, null, new Callback<SignUpResult>() {
                    @Override
                    public void onResult(final SignUpResult signUpResult) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Log.d(TAG, "Sign-up callback state: " + signUpResult.getConfirmationState());
                                if (!signUpResult.getConfirmationState()) {


                                    final UserCodeDeliveryDetails details = signUpResult.getUserCodeDeliveryDetails();
                                    Toast.makeText(getApplicationContext(), "인증 메일을 보냈습니다.: " + details.getDestination(), Toast.LENGTH_SHORT).show();

                                    // 이메일에 문제가 없으면 인증 코드 창으로 이동
                                    Intent i = new Intent(SignUpActivity.this, OkActivity.class);
                                    i.putExtra("email",username); // username을 인증 코드 창에서 사용하기 위해
                                    startActivity(i);
                                    finish();

                                } else {
                                    // 인증 코드 창으로 이동
                                    Toast.makeText(getApplicationContext(), "Sign-up done.", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "Sddd");
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "Sign-up error", e);
                    }
                });
            }

        });

    }

    // 뒤로가기 2번 눌러야 종료
    private final long FINISH_INTERVAL_TIME = 1000;
    private long backPressedTime = 0;

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        // 뒤로 가기 할 경우 AuthActivity 화면으로 이동
        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)
        {
            Intent i = new Intent(SignUpActivity.this, AuthActivity.class);
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