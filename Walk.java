package com.example.user.dogslight;

// 산책 기록을 보여주는 리스트에 담기는 클래스.
public class Walk {
    // 클래스에 들어갈 내용을 String형으로 선언한다.
    String walk; // 산책 기록 시간
    String name; // 이름
    String date; // 날짜

    public Walk(String walk, String name, String date) {
        this.walk = walk;
        this.name = name;
        this.date = date;
    }

    public String getWalk() {
        return walk;
    }

    public void setWalk(String walk) {
        this.walk = walk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
