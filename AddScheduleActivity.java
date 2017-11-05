package com.example.user.dogslight;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

// 산책 기록을 추가하는 액티비티.
public class AddScheduleActivity extends AppCompatActivity {

    private String walk;
    private  String formatDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        // xml의 위젯을 변수로 선언한다.
        final TextView walkContentText = (TextView) findViewById(R.id.walkContentText);
        final TextView walkDateText = (TextView) findViewById(R.id.walkDateText);
        final EditText walkNameText = (EditText) findViewById(R.id.walkNameText);
        final Button addButton = (Button) findViewById(R.id.addButton);

        //현재시간을 now에 저장한다.
        long now= System.currentTimeMillis();
        // now로 새로운 Date객체를 만든다.
        Date date= new Date(now);
        // date를 테이블에 삽입하는 타입으로 만들기 위해 SimpleDateFormat 변수를 선언한다.
        SimpleDateFormat df;
        df = new SimpleDateFormat("yyyy-MM-dd");
        // date, 즉 현재 날짜를 formatDate에 2017-08-30과 같은 값으로 저장한다.
        formatDate= df.format(date);

        // 자신을 부른 Intent를 얻어온다.
        Intent intent = getIntent();
        // Intent에 넣어준 산책 시간인 walk를 String변수에 저장한다.
        walk = intent.getExtras().getString("walk");

        // textView에 현재시간과 산책시간을 보여준다.
        walkDateText.setText(formatDate);
        walkContentText.setText(walk);

        // 추가버튼을 누르면 아래와 같은 작업을 한다.
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // textView에 적힌 데이터를 읽어 String 변수에 저장한다.
                String content = walkContentText.getText().toString();
                String date = walkDateText.getText().toString();
                String name = walkNameText.getText().toString();
                // MainActivity에 static으로 선언된 userID를 가져온다.
                String userID = MainActivity.userID;

                // 사용자가 값을 덜 넣으면 아래의 문장을 수행한다.
                if(content.equals("") ||date.equals("") || name.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddScheduleActivity.this);
                    AlertDialog dialog = builder.setMessage("빈칸 없이 입력해주십시오.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }

                // 추가 작업을 하는 리스너를 만든다.
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // JSONObject의 response값을 얻어온다.
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            // response의 success값이 true, 즉 삽입이 성공적으로 이루어졌다면 현재 액티비티를 종료한다.
                            if(success) {
                                finish(); // WalkFragment로 돌아간다.
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                // AddScheduleRequest에 입력된 값을 모두 넘긴다.
                AddScheduleRequest addScheduleRequest = new AddScheduleRequest(userID, content, name, date, responseListener);
                // 큐를 만들어 리퀘스트를 실제로 보낼 수 있도록 한다.
                RequestQueue queue = Volley.newRequestQueue(AddScheduleActivity.this);
                queue.add(addScheduleRequest);
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();


    }
}
