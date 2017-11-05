package com.example.user.dogslight;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

// WALK 테이블에 새로운 데이터를 삽입하는 작업을 한다.
public class AddScheduleRequest extends StringRequest {
    // WALK 테이블의 삽입 작업을 하는 url을 선언한다.
    final static private String URL = "http://dms7147.cafe24.com/MyScheduleAdd.php";
    private Map<String, String> parameters;

    // 생성자
    public AddScheduleRequest(String userID, String content, String name, String date, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null); //url에 파라미터를 post방식으로 보내준다.
        parameters = new HashMap<>();
        // id와 산책시간, 강아지이름, 날짜를 넘겨준다.
        parameters.put("userID", userID);
        parameters.put("walkContent", content);
        parameters.put("walkName", name);
        parameters.put("walkDate", date);

    }

    //@Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
