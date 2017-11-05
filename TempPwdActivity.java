package com.example.user.dogslight;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Random;

// 임시 비밀번호를 발급하는 액티비티.
public class TempPwdActivity extends AppCompatActivity {

    private AlertDialog dialog;
    // 입력한 id와 이메일이 존재하는지 확인하는 변수.
    private boolean validateEmail = false;

    private TextView passwordText;

    //editText에 입력한 값을 저장하는 변수.
    private String userID = "";
    // 인증번호/ 임시발급 번호를 만들어 저장하는 변수.
    private String randomNum = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_pwd);

        // xml에서 위젯을 가져온다.
        final EditText idText = (EditText) findViewById(R.id.idText);
        final EditText emailText = (EditText) findViewById(R.id.emailText);
        final Button sendEmailButton = (Button) findViewById(R.id.sendEmailButton);
        final EditText verifyNumText = (EditText) findViewById(R.id.verifyNumText);
        final Button verifyNumButton = (Button) findViewById(R.id.verifyNumButton);
        passwordText = (TextView) findViewById(R.id.passwordText);
        final TextView loginButton = (TextView) findViewById(R.id.loginButton);

        // 인증번호를 보낸다.
        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userID = idText.getText().toString();
                final String userEmail = emailText.getText().toString();

                // 4자리 인증번호를 생성한다.
                int randomInt = (new Random()).nextInt(10000) + 1000;
                if (randomInt > 10000) {
                    randomInt -= 1000;
                }
                // request에 String으로 전하기 위해 String 변수에 담는다.
                randomNum = Integer.toString(randomInt);

                // 입력 안 된 칸이 있으면, 알림창을 띄운다.
                if (userID.equals("") || userEmail.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(TempPwdActivity.this);
                    dialog = builder.setMessage("빈 칸 없이 입력해 주십시오.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }
                // 입력이 되어 있으면 아래의 일을 한다.
                else {
                    // id와 이메일이 있는지 검색한다.
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                // JSONObject의 response값을 얻어온다.
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                // 해당 id와 이메일을 가진 사용자가 존재한다면 아래와 같은 일을 한다.
                                if (success) {
                                    // 해당 이메일로 인증번호를 포함하여 메일을 보낸다.
                                    ValidateNumRequest validateNumRequest = new ValidateNumRequest(userEmail, randomNum, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {  }
                                    });
                                    RequestQueue queue = Volley.newRequestQueue(TempPwdActivity.this);
                                    // 큐를 만들어 리퀘스트를 실제로 보낼 수 있도록 한다.
                                    queue.add(validateNumRequest);

                                    // 메일을 보낸 후, 알림창을 띄운다.
                                    AlertDialog.Builder builder = new AlertDialog.Builder(TempPwdActivity.this);
                                    dialog = builder.setMessage("이메일을 확인해주세요.")
                                            .setPositiveButton("확인", null)
                                            .create();
                                    dialog.show();

                                    idText.setEnabled(false);
                                    emailText.setEnabled(false);
                                    // id와 이메일을 확인했으므로, validateEmail을 true로 변경한다.
                                    validateEmail = true;
                                    // id, 이메일텍스트의 배경을 바꾼다.
                                    idText.setBackgroundColor(getResources().getColor(R.color.colorGray));
                                    emailText.setBackgroundColor(getResources().getColor(R.color.colorGray));
                                }
                                // 사용자가 입력한 정보가 없다면 알림창을 띄운다.
                                else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(TempPwdActivity.this);
                                    dialog = builder.setMessage("입력한 정보를 다시 확인해주세요.")
                                            .setNegativeButton("확인", null)
                                            .create();
                                    dialog.show();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    // ValidateEmailRequest에 아이디와 이메일, 리스너를 넘긴다.
                    ValidateEmailRequest validateEmailRequest = new ValidateEmailRequest(userID, userEmail, responseListener);
                    // 큐를 만들어 리퀘스트를 실제로 보낼 수 있도록 한다.
                    RequestQueue queue = Volley.newRequestQueue(TempPwdActivity.this);
                    queue.add(validateEmailRequest);
                }

            }
        });


        // 비밀번호 확인 버튼을 누르면, 인증번호가 맞는지 확인한 후
        // 텍스트뷰에 임시비밀번호 6자리를 알려준다.
        verifyNumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼을 누르면 자판이 내려간다.
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(verifyNumText.getWindowToken(), 0);

                // 사용자가 입력한 인증번호를 string 변수에 저장한다.
                String userRandomNum = verifyNumText.getText().toString();

                // 이메일이 인증되었으면 다음과 같이 한다.
                if(validateEmail) {
                    // 발급한 4자리의 랜덤숫자와, 사용자가 입력한 숫자가 같은지 확인한다.
                    if (randomNum.equals(userRandomNum)) {
                        // 같다면, 임시비밀번호인 6자리 숫자를 발급한다.
                        int randomInt = (new Random()).nextInt(1000000) + 100000;
                        if (randomInt > 1000000) {
                            randomInt -= 100000;
                        }
                        // 임시비밀번호를 String변수에 저장한다.
                        String tempPassword = Integer.toString(randomInt);
                        passwordText.setText("임시비밀번호는 " + tempPassword + "입니다.");

                        // DB의 비밀번호를 임시비밀번호로 수정한다.
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    // JSONObject의 response값을 얻어온다.
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    // response의 success값이 true, 즉 삭제가 성공적으로 이루어졌다면 아래와 같은 일을 한다.
                                    if (success) {
                                    }
                                    // 수정에 실패했다면 에러 알림창을 띄운다.
                                    else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(TempPwdActivity.this);
                                        dialog = builder.setMessage("error.")
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
                        // ChangePwdRequest에 아이디와 임시비밀번호, 리스너를 보낸다.
                        ChangePwdRequest changePwdRequest = new ChangePwdRequest(userID, tempPassword, responseListener);
                        // 큐를 만들어 리퀘스트를 실제로 보낼 수 있도록 한다.
                        RequestQueue queue = Volley.newRequestQueue(TempPwdActivity.this);
                        queue.add(changePwdRequest);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(TempPwdActivity.this);
                        dialog = builder.setMessage("인증번호가 다릅니다.")
                                .setNegativeButton("확인", null)
                                .create();
                        dialog.show();
                    }
                }
                // 유저의 정보가 확인이 되지 않았는데, 비밀번호 확인 버튼을 누른 경우 알림창을 띄운다.
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(TempPwdActivity.this);
                    dialog = builder.setMessage("이메일 전송 요청부터 해주세요")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                }
            }
        });

        // 로그인 버튼을 누르면, 로그인 화면으로 간다.
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TempPwdActivity.this, LoginActivity.class);
                TempPwdActivity.this.startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dialog != null) {
            dialog.dismiss();  // 알림창을 죽인다.
            dialog = null;
        }
    }

}