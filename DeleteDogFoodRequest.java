package com.example.user.dogslight;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

// FOOD 테이블에 있는 데이터를 삭제한다.
public class DeleteDogFoodRequest extends StringRequest {
    // FOOD 테이블의 삭제 작업을 하는 url을 선언한다.
    final static private String URL = "http://dms7147.cafe24.com/MyDogFoodDel.php";
    private Map<String, String> parameters;

    // 생성자
    public DeleteDogFoodRequest(String userID, String foodName, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null); //url에 파라미터를 post방식으로 보내준다.
        parameters = new HashMap<>();
        // id와 사료이름을 php파일에 넘겨준다.
        parameters.put("userID", userID);
        parameters.put("foodName", foodName);

    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
