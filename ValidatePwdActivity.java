package com.example.user.dogslight;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

// 비밀번호를 확인하는 액티비티
public class ValidatePwdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_pwd);

        // xml의 위젯을 변수에 저장한다.
        final EditText currentPwdText = (EditText) findViewById(R.id.currentPwdText);
        final Button pwdValidateButton = (Button) findViewById(R.id.pwdValidateButton);

        // 비밀번호 확인 버튼.
        pwdValidateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MainActivity에 static으로 선언된 userID를 가져온다.
                final String userID = MainActivity.userID;
                // 사용자가 입력한 비밀번호를 String변수에 저장한다.
                String currentPassword = currentPwdText.getText().toString();

                // 비밀번호가 맞는지 검색한다.
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // JSONObject의 response값을 얻어온다.
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            // 비밀번호가 확인되었다면, 다음과 같은 일을 한다.
                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ValidatePwdActivity.this);
                                AlertDialog dialog = builder.setMessage("비밀번호 변경이 가능합니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();

                                // 비밀번호를 변경하는 ChangePwdActivity로 이동한다.
                                Intent intent = new Intent(ValidatePwdActivity.this, ChangePwdActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            // 비밀번호가 확인되지 않았다면 알림창을 띄운다.
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ValidatePwdActivity.this);
                                AlertDialog dialog = builder.setMessage("비밀번호를 다시 확인해주십시오")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                // ValidatePwdRequest에 아이디와 현재 비밀번호, 리스너를 보내 확인한다.
                ValidatePwdRequest validatePwdRequest = new ValidatePwdRequest(userID, currentPassword, responseListener);
                // 큐를 만들어 리퀘스트를 실제로 보낼 수 있도록 한다.
                RequestQueue queue = Volley.newRequestQueue(ValidatePwdActivity.this);
                queue.add(validatePwdRequest);

            }
        });
    }
}
