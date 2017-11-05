package com.example.user.dogslight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

// 로그인 액티비티.
public class LoginActivity extends AppCompatActivity {

    private AlertDialog dialog;
    public SharedPreferences settings;
    public SharedPreferences.Editor editor;
    // id 저장 체크박스의 체크 여부를 저장하는 변수.
    public boolean idSaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // xml의 위젯을 변수에 저장한다.
        final EditText idText = (EditText) findViewById(R.id.idText);
        final EditText passwordText = (EditText) findViewById(R.id.passwordText);
        final Button loginButton = (Button) findViewById(R.id.loginButton);
        final CheckBox saveId = (CheckBox) findViewById(R.id.saveId);

        // 아이디를 저장할 경우를 위해 sharedpreferences를 선언한다.
        settings = getSharedPreferences("settings", Activity.MODE_PRIVATE);
        // settings에서 saveId 값을 얻어온다.
        // 만약 saveId 값이 없다면, false로 가져온다.
        idSaved = settings.getBoolean("saveId", false);

        // 아이디 저장이 체크되어 있었다면 아래 소스가 실행된다.
        if (idSaved) {
            // 아이디 textView를 settings에서 얻어 온 userID로 설정한다.
            idText.setText(settings.getString("userID", ""));
            // 체크박스인 saveId 값을 true로 체크해 놓는다.
            saveId.setChecked(true);
        }
        // 로그인 버튼을 누르면 아래 문장이 실행된다.
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 사용자가 입력한 id와 비밀번호를 String 변수에 저장한다.
                final String userID = idText.getText().toString();
                final String userPassword = passwordText.getText().toString();

                // 아이디 검색 작업을 하는 리스너를 만든다.
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // JSONObject의 response 값을 얻어온다.
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            boolean emailsuccess = jsonResponse.getBoolean("emailsuccess");
                            // 아이디가 있고, 비밀번호가 일치한다면 아래 if문을 실행한다.
                            if (success) {
                                // 이메일 인증이 되었다면, 사용자 테이블의 verify가 Y로 되어 있다.
                                // 사용자의 verify가 Y이면 emailsuccess는 true이고 아래 문장이 실행된다.
                                if (emailsuccess) {
                                    // 이메일 인증된 사용자가, 로그인 시 id 저장 체크박스를 선택했다면 아래 문장을 실행한다.
                                    if (saveId.isChecked()) {
                                        // settings의 userID와 saveId값을 변경한다.
                                        editor = settings.edit();
                                        editor.putString("userID", userID);
                                        editor.putBoolean("saveId", true);
                                        // 변경이 완료되면 commit으로 수정을 종료한다.
                                        editor.commit();
                                    }

                                    // MainActivity로 이동하는 Intent 객체를 만든다.
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    // MainActivity로 이동하는 intent에 userID값을 넣어주어 사용할 수 있도록 한다.
                                    intent.putExtra("userID", userID);
                                    // MainActivity로 이동한다.
                                    LoginActivity.this.startActivity(intent);
                                    finish();
                                }
                                // 사용자 정보는 일치하지만 이메일 인증이 되지 않았다면, 아래 문장을 실행한다.
                                else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    dialog = builder.setMessage("이메일을 인증해주세요")
                                            .setPositiveButton("확인", null)
                                            .create();
                                    dialog.show();

                                }

                            }
                            // 사용자 정보가 일치하지 않는다면, 아래 문장을 실행한다.
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                dialog = builder.setMessage("계정을 다시 확인하세요")
                                        .setNegativeButton("다시 시도", null)
                                        .create();
                                dialog.show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                // LoginRequest에 아이디와 비밀번호, 리스너를 넘긴다.
                LoginRequest loginRequest = new LoginRequest(userID, userPassword, responseListener);
                // 큐를 만들어 리퀘스트를 실제로 보낼 수 있도록 한다.
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });

        // 아이디와 비밀번호 입력창이 있는 레이아웃을 불러온다.
        LinearLayout lLayout = (LinearLayout) findViewById(R.id.lLayout);
        // 레이아웃을 클릭하면 아래와 같은 일을 한다.
        lLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 레이아웃을 클릭하면, 키보드가 아래로 내려간다.
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(passwordText.getWindowToken(), 0);
            }
        });

        // 아이디 저장 버튼을 체크하면 다음과 같은 일을 한다.
        saveId.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // id 저장이 체크되면, idSaved를 true로 한다.
                if (isChecked) {
                    idSaved = true;
                }
                // id 저장을 체크해제하면 다음과 같은 일을 한다.
                else {
                    // idSaved를 false로 한다.
                    idSaved = false;
                    // settings에 저장되어 있던 값을 모두 지우고, 수정을 끝낸다.
                    editor = settings.edit();
                    editor.clear();
                    editor.commit();
                }
            }
        });

        // 임시 비밀번호 발급에 연결된 텍스트뷰를 불러온다.
        TextView tmpPwdButton = (TextView) findViewById(R.id.tmpPwdButton);
        // 텍스트뷰를 클릭하면 아래와 같은 일을 한다.
        tmpPwdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 임시비멀번호를 발급해주는 TempPwdActivity로 이동한다.
                Intent intent = new Intent(LoginActivity.this, TempPwdActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        });

        // 가입에 연결된 텍스트뷰를 불러온다.
        TextView registerButton = (TextView) findViewById(R.id.registerButton);
        // 텍스트뷰를 클릭하면 아래와 같은 일을 한다.
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 가입을 진행하는 RegisterActivity로 이동한다.
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (dialog != null) {
            dialog.dismiss(); // 다이얼로그를 종료한다.
            dialog = null;
        }
    }

}
