package com.example.maplesimulation;

public class Cube {
    Equipment equipment;

    public Cube(Equipment equipment){
        this.equipment = equipment;
    }

    //정해진 확률에 따른 성공 여부
    public Boolean isSuccess(int possibility) {
        int random = (int)(Math.random()*100);

        if(random < possibility) return true;
        return false;
    }

    public void RedCube() {

    }
    
}
