package com.example.user.dogslight;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

// 강아지를 추가하는 액티비티.
public class AddDogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dog);

        // xml 안의 요소들을 가져온다.
        final EditText dogNameText = (EditText) findViewById(R.id.dogNameText); // 강아지 이름을 입력하는 editText
        final EditText dogWeightText = (EditText) findViewById(R.id.dogWeightText); // 강아지 몸무게를 입력하는 editText
        final RadioGroup dogAgesRadio = (RadioGroup) findViewById(R.id.dogAgesRadio); // 강아지 나잇대를 선택하는 radioGroup
        final Button addDogButton = (Button) findViewById(R.id.addDogButton); // 입력한 내용을 추가하는 button

        // 추가 버튼을 클릭하면 아래와 같은 일을 한다.
        addDogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 사용자가 입력한 editText 값을 가져온다.
                String dogName = dogNameText.getText().toString();
                String dogWeight = dogWeightText.getText().toString();
                String dogAges;
                // MainActivity에 static으로 선언된 userID를 가져온다.
                String userID = MainActivity.userID;

                // 사용자가 체크한 라디오 버튼에 따라, dogAges값을 바꾼다.
                if (dogAgesRadio.getCheckedRadioButtonId() == R.id.puppy) {
                    dogAges = "puppy";
                } else {
                    dogAges = "adult";
                }

                // 사용자가 값을 덜 넣으면 아래의 문장을 수행한다.
                if (dogName.equals("") || dogWeight.equals("") || dogAges.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddDogActivity.this);
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
                            if (success) {
                                finish(); // MyFragment로 돌아간다.
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                // AddDogRequest에 입력된 값을 모두 넘긴다.
                AddDogRequest addDogRequest = new AddDogRequest(userID, dogName, dogAges, dogWeight, responseListener);
                // 큐를 만들어 리퀘스트를 실제로 보낼 수 있도록 한다.
                RequestQueue queue = Volley.newRequestQueue(AddDogActivity.this);
                queue.add(addDogRequest);

            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();

    }
}
