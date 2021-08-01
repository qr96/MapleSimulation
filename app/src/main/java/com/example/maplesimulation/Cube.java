package com.example.maplesimulation;

import android.widget.Switch;

import java.util.HashMap;
import java.util.List;

public class Cube {
    Equipment equipment;
    CubeTable cubeTable;

    public Cube(Equipment equipment, CubeTable cubeTable){
        this.equipment = equipment;
        this.cubeTable = cubeTable;
    }

    //정해진 확률에 따른 성공 여부
    public Boolean isSuccess(double possibility) {
        double random = Math.random()*100;

        if(random < possibility) return true;
        return false;
    }

    //테이블 확률대로 반환
    public int tableRandom(List table) {
        // 번호 생성
        double ran = Math.random() * 100;
        double w = 0;

        for (int i = 0; i < table.size(); i++) {
            w = w + (double) table.get(i);

            if (ran < w) {
                return i;
            }
        }
        return -1;
    }

    public void useBlackCube() {
        String option[];
        String type = equipment.getType();
        if(equipment.isWeapon()) type = "무기";

        switch(equipment.getPotentialGrade1()) {
            case "레어":
                if(isSuccess(15.0)){ //등급업
                    equipment.potentialGradeUp1();
                    option = blackEpic(type);
                }
                else{
                    option = blackRare(type);
                }
                break;
            case "에픽":
                if(isSuccess(3.5)){ //등급업
                    equipment.potentialGradeUp1();
                    option = blackUnique(type);
                }
                else{
                    option = blackEpic(type);
                }
                break;
            case "유니크":
                if(isSuccess(1.0)){ //등급업
                    equipment.potentialGradeUp1();
                    option = blackLegendary(type);
                }
                else{
                    option = blackUnique(type);
                }
                break;
            case "레전드리":
                option = blackLegendary(type);
                break;
            default:
                option = new String[3];
        }
        this.equipment.setPotential1(option);
    }

    private String[] blackRare(String type){
        String option[] = new String[3];
        option[0] = select("레어"+type+"1");
        option[1] = select("레어"+type+"2");
        option[2] = select("레어"+type+"3");
        return option;
    }

    private String[] blackEpic(String type){
        String option[] = new String[3];
        option[0] = select("에픽"+type+"1");
        if(isSuccess(20)) option[1] = select("에픽"+type+"2");
        else option[1] = select("레어"+type+"2");
        if(isSuccess(5)) option[2] = select("에픽"+type+"3");
        else option[2] = select("레어"+type+"3");
        return option;
    }

    private String[] blackUnique(String type){
        String option[] = new String[3];
        option[0] = select("유니크"+type+"1");
        if(isSuccess(20)) option[1] = select("유니크"+type+"2");
        else option[1] = select("에픽"+type+"2");
        if(isSuccess(5)) option[2] = select("유니크"+type+"3");
        else option[2] = select("에픽"+type+"3");
        return option;
    }

    private String[] blackLegendary(String type){
        String option[] = new String[3];
        option[0] = select("레전드리"+type+"1");
        if(isSuccess(20)) option[1] = select("레전드리"+type+"2");
        else option[1] = select("유니크"+type+"2");
        if(isSuccess(5)) option[2] = select("레전드리"+type+"3");
        else option[2] = select("유니크"+type+"3");
        return option;
    }

    //테이블에서 key값에 맞는 옵션 선택
    private String select(String key){
        List table = cubeTable.percentTable.get(key);
        if(table == null) return "Error : " + key;
        int random = tableRandom(table);
        return (String) cubeTable.optaionTable.get(key).get(random);
    }
}
