package com.amplify.amplify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.UserStateDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.amazonaws.mobile.client.Callback;

public class MainActivity extends AppCompatActivity {

    private String address = "https://sj7jw37am2.execute-api.ap-northeast-2.amazonaws.com/default/lambda_api_test";

    Button signOut_button;
    TextView textView;
    EditText serial_number;
    private Button btnData;
    private RecyclerView recyclerview;
    private RecyclerAdapter adapter = new RecyclerAdapter();    // adapter 생성

    private ArrayList<Data> list = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serial_number = findViewById(R.id.serial_number);
        signOut_button = findViewById(R.id.signOut_button);
        btnData = findViewById(R.id.btnData);
        recyclerview = findViewById(R.id.recyclerview);

        // RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(linearLayoutManager);
        // recyclerview에 adapter 적용
        recyclerview.setAdapter(adapter);


        // 데이터 불러오기 버튼 클릭
        btnData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(){
                    @Override
                    public void run() {
                        try {
                            String num = serial_number.getText().toString();
                            Log.e("Num::",num);
                            URL url = new URL(address);

                            InputStream is = url.openStream();
                            InputStreamReader isr = new InputStreamReader(is);
                            BufferedReader reader = new BufferedReader(isr);

                            StringBuffer buffer = new StringBuffer();
                            String line = reader.readLine();
                            while (line != null) {
                                buffer.append(line + "\n");
                                line = reader.readLine();
                            }
                            String jsonData = buffer.toString();

                            // jsonData를 먼저 JSONObject 형태로 바꾼다.
                            JSONObject obj = new JSONObject(jsonData);


                            // boxOfficeResult의 JSONObject에서 "our_list"의 JSONArray 추출
                            JSONArray our_list = (JSONArray)obj.get("Items");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        // out_list의 length만큼 for문 반복
                                        list.clear();
                                        for (int i = 0; i < our_list.length(); i++) {
                                            // our_list를 각 JSONObject 형태로 객체를 생성한다.
                                            JSONObject temp = our_list.getJSONObject(i);
                                            Log.e("Serial:",temp.getString("serial"));

                                            if(num.equals(temp.getString("serial"))) {
                                                Log.e("Success","!");
                                                // list의 json 값들을 넣는다.
                                                list.add(new Data(temp.getString("image"), temp.getString("time"), temp.getString("serial")));
                                            }

                                        }
                                        // adapter에 적용
                                        adapter.setmovieList(list);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

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