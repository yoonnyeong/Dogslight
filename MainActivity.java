package com.example.user.dogslight;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    // BackButton을 두 번 누를 때 종료 여부 핸들러를 선언한다.
    private BackPressCloseHandler backPressCloseHandler;

    // 전역변수로 로그인한 userID를 선언한다.
    public static String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 세로로 화면을 고정한다.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 핸들러를 생성한다.
        backPressCloseHandler = new BackPressCloseHandler(this);

        // userID는 LoginActivity에서 intent값에 넘겨준 값으로 지정한다.
        userID = getIntent().getStringExtra("userID");

        // 탭 역할을 하는 버튼을 가져온다.
        final Button feedButton = (Button) findViewById(R.id.feedButton);
        final Button fatButton = (Button) findViewById(R.id.fatButton);
        final Button walkNoticeButton = (Button) findViewById(R.id.walkNoticeButton);
        final Button walkButton = (Button) findViewById(R.id.walkButton);
        final Button myButton = (Button) findViewById(R.id.myButton);;

        // feedButton을 클릭하면 아래와 같은 일이 나타난다.
        feedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 선택된 버튼의 색만 다른 색으로 해서 현재 클릭한 버튼을 알 수 있게 한다.
                feedButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                fatButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                walkNoticeButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                walkButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                myButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
               // 프래그먼트 매니저를 통해 FeedFragment를 불러온다.
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new FeedFragment());
                fragmentTransaction.commit();

            }
        });
        // fatButton을 클릭하면 아래와 같은 일이 나타난다.
        fatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 선택된 버튼의 색만 다른 색으로 해서 현재 클릭한 버튼을 알 수 있게 한다.
                feedButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                fatButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                walkNoticeButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                walkButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                myButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                // 프래그먼트 매니저를 통해 FatFragment를 불러온다.
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new FatFragment());
                fragmentTransaction.commit();

            }
        });
        // walkNoticeButton을 클릭하면 아래와 같은 일이 나타난다.
        walkNoticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 선택된 버튼의 색만 다른 색으로 해서 현재 클릭한 버튼을 알 수 있게 한다.
                feedButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                fatButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                walkNoticeButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                walkButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                myButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                // 프래그먼트 매니저를 통해 WalkNoticeFragment를 불러온다.
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new WalkNoticeFragment());
                fragmentTransaction.commit();

            }
        });
        // walkButton을 클릭하면 아래와 같은 일이 나타난다.
        walkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 선택된 버튼의 색만 다른 색으로 해서 현재 클릭한 버튼을 알 수 있게 한다.
                feedButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                fatButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                walkNoticeButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                walkButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                myButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                // 프래그먼트 매니저를 통해 WalkFragment를 불러온다.
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new WalkFragment());
                fragmentTransaction.commit();

            }
        });
        // myButton을 클릭하면 아래와 같은 일이 나타난다.
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 선택된 버튼의 색만 다른 색으로 해서 현재 클릭한 버튼을 알 수 있게 한다.
                feedButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                fatButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                walkNoticeButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                walkButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                myButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                // 프래그먼트 매니저를 통해 MyFragment를 불러온다.
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new MyFragment());
                fragmentTransaction.commit();

            }
        });

        // walkNoticeButton이 클릭되어 있도록 한다.
        // WalkNoticeFragment가 메인에 띄어 있도록 한다.
        walkNoticeButton.performClick();
    }


    // back버튼을 누르면, BackPressCloseHandler가 작동하게 한다.
    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

}
