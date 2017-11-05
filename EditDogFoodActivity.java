package com.example.user.dogslight;

import android.content.Intent;
import android.os.AsyncTask;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

// 사료 데이터를 수정하는 액티비티.
public class EditDogFoodActivity extends AppCompatActivity {

    private String foodName;

    private TextView foodNameText;
    private EditText foodProteinText;
    private EditText foodFatText;
    private EditText foodFiberText;
    private Button editFoodButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dog_food);

        // xml 안의 요소를 변수로 저장한다.
        foodNameText = (TextView) findViewById(R.id.foodNameText);
        foodProteinText = (EditText) findViewById(R.id.foodProteinText);
        foodFatText = (EditText) findViewById(R.id.foodFatText);
        foodFiberText = (EditText) findViewById(R.id.foodFiberText);
        editFoodButton = (Button) findViewById(R.id.editFoodButton);

        // 자신을 부른 Intent를 얻어온다.
        Intent intent = getIntent();
        // intent에 있는 강아지 이름 값을 String 변수에 저장한다.
        foodName = intent.getExtras().getString("foodName");

        // DB에 있는 현재 선택한 강아지의 정보를 가져오는 AsyncTask를 실행한다.
        new BackgroundTask().execute();

        // 수정 버튼을 누르면, DB 수정을 위해 아래와 같은 일을 한다.
        editFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MainActivity에 static으로 선언된 userID를 가져온다.
                String userID = MainActivity.userID;
                // 수정 창에 입력한 값을 얻어온다.
                String foodProtein = foodProteinText.getText().toString();
                String foodFat = foodFatText.getText().toString();
                String foodFiber = foodFiberText.getText().toString();

                // 사용자가 값을 덜 넣으면 아래의 문장을 수행한다.
                if(foodProtein.equals("") || foodFat.equals("") || foodFiber.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditDogFoodActivity.this);
                    AlertDialog dialog = builder.setMessage("빈칸 없이 입력해주십시오.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }

                // 수정 작업을 하는 리스너를 만든다.
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // JSONObject의 response값을 얻어온다.
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            // response의 success값이 true, 즉 삭제가 성공적으로 이루어졌다면 아래와 같은 일을 한다.
                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(EditDogFoodActivity.this);
                                AlertDialog dialog = builder.setMessage("수정되었습니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                                finish();  // MyFragment로 돌아간다.
                            }
                            // 수정을 실패했다면 아래와 같이 알림창이 뜨게 한다.
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(EditDogFoodActivity.this);
                                AlertDialog dialog = builder.setMessage("수정에 실패했습니다.")
                                        .setNegativeButton("다시 시도", null)
                                        .create();
                                dialog.show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                // EditDogFoodRequest에 아이디와 사료 이름, 단백질, 지방, 섬유 함량과, 리스너를 넘긴다.
                EditDogFoodRequest editDogFoodRequest = new EditDogFoodRequest(userID, foodName, foodProtein, foodFat, foodFiber, responseListener);
                // 큐를 만들어 리퀘스트를 실제로 보낼 수 있도록 한다.
                RequestQueue queue = Volley.newRequestQueue(EditDogFoodActivity.this);
                queue.add(editDogFoodRequest);

            }


        });

    }


    // 수정해야 할 사료 데이터를 가져온다.
    class BackgroundTask extends AsyncTask<Void, Void, String> {

        String target;
        // MainActivity에 static으로 선언된 userID를 가져온다.
        String userID = MainActivity.userID;

        @Override
        protected void onPreExecute() {
            try {
                // target에 select 작업을 하는 DogFoodInfoGet 을 지정한다.
                // 사용자 id와 사료 이름을 같이 넘겨준다.
                target = "http://dms7147.cafe24.com/DogFoodInfoGet.php?userID=" + URLEncoder.encode(userID, "UTF-8")
                        + "&foodName=" + URLEncoder.encode(foodName, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        protected String doInBackground(Void... params) {
            try {
                // 위에서 지정한 target을 url로 만든다.
                URL url = new URL(target);
                // url을 연결하고 값을 읽어온다.
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                // 끝이 아닐 때까지 값을 읽어온다.
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");
                }
                // 작업을 종료한 것들은 닫는다.
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(String result) {
            try {
                // JSONObject의 response값을 얻어온다.
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                // array의 길이와 비교할 변수인 count를 선언한다.
                int count = 0;
                // 읽어온 값을 담을 변수를 선언한다.
                String myFoodName;
                Double myFoodProtein;
                Double myFoodFat;
                Double myFoodFiber;

                // 다 읽어올 때까지 while문을 반복한다.
                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    // object에서 전해준 값을, BackgroundTask내의 변수에 저장한다.
                    myFoodName = object.getString("myFoodName");
                    myFoodProtein = object.getDouble("myFoodProtein");
                    myFoodFat = object.getDouble("myFoodFat");
                    myFoodFiber = object.getDouble("myFoodFiber");

                    // textView, editText를 읽어온 값으로 설정한다.
                    foodNameText.setText(myFoodName);
                    foodProteinText.setText(myFoodProtein.toString());
                    foodFatText.setText(myFoodFat.toString());
                    foodFiberText.setText(myFoodFiber.toString());

                    // count값을 1씩 증가한다.
                    count++;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


