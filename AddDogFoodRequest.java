package com.example.user.dogslight;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

// FOOD 테이블에 새로운 데이터를 삽입하는 작업을 한다.
public class AddDogFoodRequest extends StringRequest {
    // FOOD 테이블의 삽입 작업을 하는 url을 선언한다.
    final static private String URL = "http://dms7147.cafe24.com/MyDogFoodAdd.php";
    private Map<String, String> parameters;

    // 생성자
    public AddDogFoodRequest(String userID, String foodName, String foodProtein, String foodFat, String foodFiber, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null); //url에 파라미터를 post방식으로 보내준다.
        parameters = new HashMap<>();
        // id와 사료이름, 단백질, 지방, 섬유질 함량을 php파일에 넘겨준다.
        parameters.put("userID", userID);
        parameters.put("foodName", foodName);
        parameters.put("foodProtein", foodProtein);
        parameters.put("foodFat", foodFat);
        parameters.put("foodFiber", foodFiber);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
