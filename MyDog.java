package com.example.user.dogslight;

// 나의 강아지를 관리하는 리스트에 담기는 클래스.
public class MyDog {
    // 클래스에 들어갈 내용을 String형으로 선언한다.
    String myDogName;   // 강아지 이름
    String myDogAges;   // 강아지 나이대
    Double myDogWeight; // 강아지 몸무게

    public MyDog(String myDogName, String myDogAges, Double myDogWeight) {
        this.myDogName = myDogName;
        this.myDogAges = myDogAges;
        this.myDogWeight = myDogWeight;
    }

    public String getMyDogName() {
        return myDogName;
    }

    public void setMyDogName(String myDogName) {
        this.myDogName = myDogName;
    }

    public String getMyDogAges() {
        return myDogAges;
    }

    public void setMyDogAges(String myDogAges) {
        this.myDogAges = myDogAges;
    }

    public Double getMyDogWeight() {
        return myDogWeight;
    }

    public void setMyDogWeight(Double myDogWeight) {
        this.myDogWeight = myDogWeight;
    }

}
