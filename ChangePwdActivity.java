package com.example.user.dogslight;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

// 비밀번호를 변경하는 액티비티
public class ChangePwdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);

        // xml의 요소들을 가져온다.
        final EditText newPwdText = (EditText) findViewById(R.id.newPwdText);
        final EditText validateNewPwdText = (EditText) findViewById(R.id.validateNewPwdText);
        final Button validateButton = (Button) findViewById(R.id.validateButton);

        // 비밀번호 변경 버튼을 누르면 아래와 같은 일을 한다.
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MainActivity에 static으로 선언된 userID를 가져온다.
                String userID = MainActivity.userID;
                // 새로운 비밀번호와, 비밀번호 확인 창에 넣은 값을 String 변수에 저장한다.
                String newPassword = newPwdText.getText().toString();
                String validateNewPwd = validateNewPwdText.getText().toString();

                // 사용자가 값을 덜 넣으면 아래의 문장을 수행한다.
                if(newPassword.equals("") || validateNewPwd.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChangePwdActivity.this);
                    AlertDialog dialog = builder.setMessage("변경할 비밀번호를 입력해주십시오")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }
                // 새로운 비밀번호와 비밀번호 확인 창에 넣은 값이 다르마녀 아래의 문장을 수행한다.
                else if(!newPassword.equals(validateNewPwd)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChangePwdActivity.this);
                    AlertDialog dialog = builder.setMessage("비밀번호가 같지 않습니다.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }

                else {
                    // 변경 작업을 하는 리스너를 만든다.
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                // JSONObject의 response값을 얻어온다.
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                // response의 success값이 true, 즉 수정이 성공적으로 이루어졌다면 아래와 같은 일을 한다.
                                if (success) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ChangePwdActivity.this);
                                    AlertDialog dialog = builder.setMessage("비밀번호가 변경되었습니다.")
                                            .setPositiveButton("확인", null)
                                            .create();
                                    dialog.show();
                                    finish();
                                }
                                // 수정에 실패했다면, 아래 메시지를 띄운다.
                                else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ChangePwdActivity.this);
                                    AlertDialog dialog = builder.setMessage("수정에 실패했습니다.")
                                            .setPositiveButton("확인", null)
                                            .create();
                                    dialog.show();
                                    finish();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    // ChangePwdRequest 아이디와 새로운 비밀번호를 넘긴다.
                    ChangePwdRequest changePwdRequest = new ChangePwdRequest(userID, newPassword, responseListener);
                    // 큐를 만들어 리퀘스트를 실제로 보낼 수 있도록 한다.
                    RequestQueue queue = Volley.newRequestQueue(ChangePwdActivity.this);
                    queue.add(changePwdRequest);
                }

            }
        });
    }
}
