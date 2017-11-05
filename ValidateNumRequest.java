package com.example.user.dogslight;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

// 메일에 인증번호를 보낸다.
public class ValidateNumRequest extends StringRequest {
    // 메일을 보내는 url을 선언한다.
    final static private String URL = "http://dms7147.cafe24.com/NumValidate.php";
    private Map<String, String> parameters;

    // 생성자
    public ValidateNumRequest(String userEmail, String randomNum, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null); //url에 파라미터를 post방식으로 보내준다.
        parameters = new HashMap<>();
        // 이메일과 인증번호를 넘겨준다.
        parameters.put("userEmail", userEmail);
        parameters.put("randomNum", randomNum);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
