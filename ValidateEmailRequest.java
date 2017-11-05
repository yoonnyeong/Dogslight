package com.example.user.dogslight;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

// 사용자가 입력한 id와 이메일이 있는지 확인한다.
public class ValidateEmailRequest extends StringRequest {
    // USER 테이블의 검색 작업을 하는 url을 선언한다.
    final static private String URL = "http://dms7147.cafe24.com/UserEmailValidate.php";
    private Map<String, String> parameters;

    // 생성자
    public ValidateEmailRequest(String userID, String userEmail, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null); //url에 파라미터를 post방식으로 보내준다.
        parameters = new HashMap<>();
        // id와 이메일을 넘겨준다.
        parameters.put("userID", userID);
        parameters.put("userEmail", userEmail);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
