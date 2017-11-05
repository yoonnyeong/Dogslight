package com.example.user.dogslight;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

// DOG 테이블에 새로운 데이터를 삽입하는 작업을 한다.
public class AddDogRequest extends StringRequest {
    // DOG 테이블의 삽입 작업을 하는 url을 선언한다.
    final static private String URL = "http://dms7147.cafe24.com/MyDogAdd.php";
    private Map<String, String> parameters;

    // 생성자
    public AddDogRequest(String userID, String dogName, String dogAges, String dogWeight, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null); //url에 파라미터를 post방식으로 보내준다.
        parameters = new HashMap<>();
        // id와 강아지이름, 나이대, 몸무게를 php파일에 넘겨준다.
        parameters.put("userID", userID);
        parameters.put("dogName", dogName);
        parameters.put("dogAges", dogAges);
        parameters.put("dogWeight", dogWeight);

    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
