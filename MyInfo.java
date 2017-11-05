package com.example.user.dogslight;

// 나의 정보를 관리하는 메뉴 클래스
public class MyInfo {
    // 클래스에 들어갈 내용을 String형으로 선언한다.
    String infoText;

    public MyInfo(String infoText) {
        this.infoText = infoText;
    }

    public String getInfoText() {
        return infoText;
    }

    public void setInfoText(String infoText) {
        this.infoText = infoText;
    }
}
