package com.example.user.dogslight;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

// 사용자가 입력한 정보를 삽입한다.
public class RegisterRequest extends StringRequest {
    final static private String URL = "http://dms7147.cafe24.com/UserRegister.php";
    private Map<String, String> parameters;

    // 생성자
    public RegisterRequest(String userID, String userPassword, String userEmail, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null); //url에 파라미터를 post방식으로 보내준다.
        parameters = new HashMap<>();
        // id와 이메일, 비밀번호를 넘겨준다.
        parameters.put("userID", userID);
        parameters.put("userPassword", userPassword);
        parameters.put("userEmail", userEmail);

    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
