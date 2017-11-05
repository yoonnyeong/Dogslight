package com.example.user.dogslight;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // 핸들러 객체 선언
        Handler handler = new Handler();
        // 2초가 지나면 로그인 액티비티로 이동한다.
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                startActivity(intent);
                // 뒤로가기 했을경우 안나오도록 한다.
                finish();
            }
        }, 2000);
    }
}
