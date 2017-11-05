package com.example.user.dogslight;

// 나의 강아지 사료를 관리하는 리스트에 담기는 클래스.
public class MyDogFood {
    // 클래스에 들어갈 내용을 String형으로 선언한다.
    String myFoodName;      // 사료 이름
    Double myFoodProtein;   // 사료의 단백질 함량
    Double myFoodFat;       // 사료의 지방 함량
    Double myFoodFiber;     // 사료의 섬유질 함량

    public MyDogFood(String myFoodName, Double myFoodProtein, Double myFoodFat, Double myFoodFiber) {
        this.myFoodName = myFoodName;
        this.myFoodProtein = myFoodProtein;
        this.myFoodFat = myFoodFat;
        this.myFoodFiber = myFoodFiber;
    }

    public String getMyFoodName() {
        return myFoodName;
    }

    public void setMyFoodName(String myFoodName) {
        this.myFoodName = myFoodName;
    }

    public Double getMyFoodProtein() {
        return myFoodProtein;
    }

    public void setMyFoodProtein(Double myFoodProtein) {
        this.myFoodProtein = myFoodProtein;
    }

    public Double getMyFoodFat() {
        return myFoodFat;
    }

    public void setMyFoodFat(Double myFoodFat) {
        this.myFoodFat = myFoodFat;
    }

    public Double getMyFoodFiber() {
        return myFoodFiber;
    }

    public void setMyFoodFiber(Double myFoodFiber) {
        this.myFoodFiber = myFoodFiber;
    }


}
