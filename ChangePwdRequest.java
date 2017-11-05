package com.example.user.dogslight;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

// 사용자의 비밀번호를 변경한다.
public class ChangePwdRequest extends StringRequest {
    // USER 테이블의 userPassword를 수정하는 url을 선언한다.
    final static private String URL = "http://dms7147.cafe24.com/PwdChange.php";
    private Map<String, String> parameters;

    // 생성자
    public ChangePwdRequest(String userID, String newPassword, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null); //url에 파라미터를 post방식으로 보내준다.
        parameters = new HashMap<>();
        // id와 새로운 비밀번호를 php파일에 넘겨준다.
        parameters.put("userID", userID);
        parameters.put("newPassword", newPassword);

    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
