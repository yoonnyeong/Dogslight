package com.example.user.dogslight;

import android.app.Activity;
import android.widget.Toast;

// 뒤로 가기 버튼을 두 번 누를 때의 작업을 다루는 클래스이다.
public class BackPressCloseHandler {
    // 시간을 저장하는 변수
    private long backKeyPressedTime = 0;
    private Toast toast;

    private Activity activity;

    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            // 현재 시간이 뒤로가기 버튼을 전에 누를 떄보다 2초가 넘었으면 현재시간을 저장한다
            backKeyPressedTime = System.currentTimeMillis();
            // toast창을 띄어준다
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            // 현재 시간이 뒤로가기 버튼을 전에 누를 때보다 2초보다 적으면 액티비티를 종료한다.
            activity.finish();
            toast.cancel();
        }
    }

    public void showGuide() {
        toast = Toast.makeText(activity,
                "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }
}
