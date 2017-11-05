package com.example.user.dogslight;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

// DOG 테이블의 값을 변경한다.
public class EditDogRequest extends StringRequest {
    // DOG 테이블의 수정 작업을 하는 url을 선언한다.
    final static private String URL = "http://dms7147.cafe24.com/MyDogEdit.php";
    private Map<String, String> parameters;

    // 생성자
    public EditDogRequest(String userID, String dogName, String dogAges, String dogWeight, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null); //url에 파라미터를 post방식으로 보내준다.
        parameters = new HashMap<>();
        // id와 강아지 이름, 나이대, 몸무게를 넘겨준다.
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
