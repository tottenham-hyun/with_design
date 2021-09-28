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
import com.amazonaws.mobile.client.results.SignUpResult;
import com.amazonaws.mobile.client.results.UserCodeDeliveryDetails;

public class OkActivity extends AppCompatActivity {
    String TAG = AuthActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok);

        // 인증 확인버튼
        Button Ok_button = findViewById(R.id.Ok_button);

        // SingUpActivity 에서 사용된 username 정보를 가져와 TextView에 넣는다.
        TextView TextView = findViewById(R.id.signUpUsername2);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String username = bundle.getString("email");
        TextView.setText(username);

        // 인증 버튼
        Ok_button.setOnClickListener(new View.OnClickListener() {

            // 인증 코드 확인
            @Override
            public void onClick(View v) {

                EditText code_name = findViewById(R.id.code_name);
                String code = code_name.getText().toString();

                AWSMobileClient.getInstance().confirmSignUp(username, code, new Callback<SignUpResult>() {
                    @Override
                    public void onResult(final SignUpResult signUpResult) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Log.d(TAG, "Sign-up callback state: " + signUpResult.getConfirmationState());
                                if (!signUpResult.getConfirmationState()) {
                                    final UserCodeDeliveryDetails details = signUpResult.getUserCodeDeliveryDetails();

                                    Toast.makeText(getApplicationContext(), "Confirm sign-up with: " + details.getDestination(), Toast.LENGTH_SHORT).show();


                                } else {

                                    // 회원가입이 완료되면 로그인 창으로 이동
                                    Toast.makeText(getApplicationContext(),"성공적으로 회원가입 되셨습니다.", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(OkActivity.this, AuthActivity.class);
                                    startActivity(i);
                                    finish();

                                }
                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "Confirm sign-up error", e);
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

        // 뒤로 가기 할 경우 SignActivity 화면으로 이동
        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)
        {
            Intent i = new Intent(OkActivity.this, SignUpActivity.class);
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
