package com.example.user.dogslight;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

// 사용자의 비밀번호를 변경한다.
public class ValidatePwdRequest extends StringRequest {
    // USER 테이블의 비밀번호 수정 작업을 하는 url을 선언한다.
    final static private String URL = "http://dms7147.cafe24.com/PwdValidate.php";
    private Map<String, String> parameters;

    // 생성자
    public ValidatePwdRequest(String userID, String currentPassword, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null); //url에 파라미터를 post방식으로 보내준다.
        parameters = new HashMap<>();
        // id와 현재 입력한 비밀번호를 넘겨준다.
        parameters.put("userID", userID);
        parameters.put("currentPassword", currentPassword);

    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
