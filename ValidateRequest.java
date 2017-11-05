package com.example.user.dogslight;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

// 가입 시 입력한 id가 있는 기존에 존재하는지 확인한다.
public class ValidateRequest extends StringRequest {
    // USER 테이블의 id 검색 작업을 하는 url을 선언한다.
    final static private String URL = "http://dms7147.cafe24.com/UserValidate.php";
    private Map<String, String> parameters;

    // 생성자
    public ValidateRequest(String userID, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null); //url에 파라미터를 post방식으로 보내준다.
        parameters = new HashMap<>();
        // id를 넘겨준다.
        parameters.put("userID", userID);

    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
