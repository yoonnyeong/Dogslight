package com.example.user.dogslight;

import android.content.DialogInterface;
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

public class RegisterActivity extends AppCompatActivity {

    private AlertDialog dialog;
    private boolean validate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // xml에서 위젯을 가져온다.
        final EditText idText = (EditText) findViewById(R.id.idText);
        final EditText passwordText = (EditText) findViewById(R.id.passwordText);
        final EditText emailText = (EditText) findViewById(R.id.emailText);

        // 중복체크 버튼.
        final Button validateButton = (Button) findViewById(R.id.validateButton);
        // 중복체크 버튼을 누르면 다음과 같은 일을 한다.
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 사용자가 입력한 값을 id로 받아온다.
                String userID = idText.getText().toString();
                // 중복확인되었다면, 끝낸다.
                if(validate) {
                    return;
                }
                // userID에 입력하지 않았다면, 다음과 같이 알림창을 띄운다.
                if(userID.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("아이디를 입력해 주십시오.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }

                // 중복 여부를 검색하는 리스너를 만든다.
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    //응답받도록
                    public void onResponse(String response) {
                        try {
                            // JSONObject의 response값을 얻어온다.
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            // response의 success값이 true, 즉 해당 id가 없다면 아래와 같은 일을 한다.
                            if(success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("사용할 수 있는 아이디입니다.\n 사용하시겠습니까?")
                                        .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // idText를 못고치게 한다.
                                                idText.setEnabled(false);
                                                // 중복여부 확인했으므로 validate 값을 true로 바꾼다.
                                                validate = true;
                                                // idText와 validateButton 배경 색을 회색으로 한다.
                                                idText.setBackgroundColor(getResources().getColor(R.color.colorGray));
                                                validateButton.setBackgroundColor(getResources().getColor(R.color.colorGray));
                                            }
                                        })
                                        .setPositiveButton("취소", null)
                                .create();
                                dialog.show();
                            }
                            // 이미 해당 id가 존재하면 아래와 같이 알림창을 띄운운.
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("사용할 수 없는 아이디입니다.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                // ValidateRequest에 아이디와 리스너를 넘긴다.
                ValidateRequest validateRequest = new ValidateRequest(userID, responseListener);
                // 큐를 만들어 리퀘스트를 실제로 보낼 수 있도록 한다.
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(validateRequest);
            }
        });

        // 가입 버튼.
        final Button registerButton = (Button) findViewById(R.id.registerButton);
        // 가입 버튼을 누르면 다음과 같은 일을 한다.
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 사용자가 입력한 값들을 받아온다.
                String userID = idText.getText().toString();
                String userPassword = passwordText.getText().toString();
                String userEmail = emailText.getText().toString();

                // 중복 체크가 되지 않았다면 알림창을 띄운다.
                if(!validate) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("중복체크 해주세요.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }

                // 입력하지 않은 것이 있다면, 다음과 같이 알림창을 띄운다.
                if(userID.equals("") || userPassword.equals("") || userEmail.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("빈 칸 없이 입력해주세요")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }

                // 삽입 작업을 하는 리스너를 만든다.
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // JSONObject의 response값을 얻어온다.
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            // response의 success값이 true, 즉 삽입이 성공적으로 이루어졌다면 아래와 같은 일을 한다.
                            if(success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("이메일의 링크를 클릭하시면 가입이 완료됩니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                                finish(); // LoginActivity로 간다.
                            }
                            // 삽입에 실패했다면 아래와 같이 알림창을 띄운다.
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("회원등록에 실패했습니다")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                RegisterRequest registerRequest = new RegisterRequest(userID, userPassword, userEmail, responseListener);
                // 큐를 만들어 리퀘스트를 실제로 보낼 수 있도록 한다.
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(dialog != null) {
            dialog.dismiss(); // 알림창을 죽인다.
            dialog = null;
        }
    }
}
