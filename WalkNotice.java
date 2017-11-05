package com.example.user.dogslight;

// 산책 시 유의사항을 보여주는 리스트에 담기는 클래스.
public class WalkNotice {
    // 클래스에 들어갈 내용을 String형으로 선언한다.
    String notice;  // 유의사항 내용
    String name;    // 이름
    String date;    // 날짜

    public WalkNotice(String notice, String name, String date) {
        this.notice = notice;
        this.name = name;
        this.date = date;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {  this.name = name; }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
