package com.example.maplesimulation;

import android.widget.Switch;

import java.util.ArrayList;
import java.util.Arrays;
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

    public void useRedCube() {
        String[] option;
        String type = equipment.getType();
        ArrayList<String> subWeapon = new ArrayList<>(Arrays.asList("포스실드", "소울링", "방패"));
        ArrayList<String> armor2 = new ArrayList<>(Arrays.asList("망토", "벨트", "어깨장식"));
        ArrayList<String> accessary = new ArrayList<>(Arrays.asList("얼굴장식", "눈장식", "귀고리", "반지", "펜던트"));

        //같은 테이블인 경우들 처리
        if(equipment.isWeapon()) type = "무기";
        else if(subWeapon.contains(type)) type="보조무기";
        else if(type == "한벌옷") type="상의";
        else if(armor2.contains(type)) type="망토";
        else if(accessary.contains(type)) type="얼굴장식";


        switch (equipment.getPotentialGrade1()) {
            case "레어":
                if(isSuccess(6.0)){ //등급업
                    equipment.potentialGradeUp1();
                    option = epic(type, 10.0, 1.0);
                }
                else {
                    option = rare(type);
                }
                break;
            case "에픽":
                if(isSuccess(1.8)){ //등급업
                    equipment.potentialGradeUp1();
                    option = unique(type, 10.0, 1.0);
                }
                else {
                    option = epic(type, 10.0, 1.0);
                }
                break;
            case "유니크":
                if(isSuccess(0.3)){ //등급업
                    equipment.potentialGradeUp1();
                    option = legendary(type, 10.0, 1.0);
                }
                else {
                    option = unique(type, 10.0, 1.0);
                }
                break;
            case "레전드리":
                option = legendary(type, 10.0, 1.0);
                break;
            default:
                option = new String[3];
        }
        this.equipment.setPotential1(option);
    }

    public void useAddiCube() {
        String[] option;
        String type = equipment.getType();
        ArrayList<String> armors = new ArrayList<>(Arrays.asList("한벌옷", "하의", "신발", "망토", "벨트", "어깨장식", "얼굴장식", "눈장식", "귀고리", "반지", "펜던트"));
        ArrayList<String> subWeapon = new ArrayList<>(Arrays.asList("포스실드", "소울링", "방패"));
        if(equipment.isWeapon()) type = "무기";
        else if(subWeapon.contains(type)) type="보조무기";
        else if(armors.contains(type)) type="상의"; //armors 들은 상의와 에디 옵션 테이블이 같기 때문

        switch (equipment.getPotentialGrade2()) {
            case "레어":
                if(isSuccess(4.7619)){ //등급업
                    equipment.potentialGradeUp2();
                    option = epic(type, 4.7619, 4.7619);
                }
                else {
                    option = rare(type);
                }
                break;
            case "에픽":
                if(isSuccess(1.9608)){ //등급업
                    equipment.potentialGradeUp2();
                    option = unique(type, 1.9608, 1.9608);
                }
                else {
                    option = epic(type, 4.7619, 4.7619);
                }
                break;
            case "유니크":
                if(isSuccess(0.4975)){ //등급업
                    equipment.potentialGradeUp2();
                    option = legendary(type, 0.4975, 0.4975);
                }
                else {
                    option = unique(type, 1.9608, 1.9608);
                }
                break;
            case "레전드리":
                option = legendary(type, 0.4975, 0.4975);
                break;
            default:
                option = new String[3];
        }
        this.equipment.setPotential2(option);
    }

    private String[] rare(String type){
        String option[] = new String[3];
        option[0] = select("레어"+type+"1");
        option[1] = select("레어"+type+"2");
        option[2] = select("레어"+type+"3");
        return option;
    }

    //에픽, [타입, 이탈확률12]
    private String[] epic(String type, Double lucky1, Double lucky2){
        String option[] = new String[3];
        option[0] = select("에픽"+type+"1");
        if(isSuccess(lucky1)) option[1] = select("에픽"+type+"2");
        else option[1] = select("레어"+type+"2");
        if(isSuccess(lucky2)) option[2] = select("에픽"+type+"3");
        else option[2] = select("레어"+type+"3");
        return option;
    }

    //유니크 [타입, 이탈확률12]
    private String[] unique(String type, Double lucky1, Double lucky2){
        String option[] = new String[3];
        option[0] = select("유니크"+type+"1");
        if(isSuccess(lucky1)) option[1] = select("유니크"+type+"2");
        else option[1] = select("에픽"+type+"2");
        if(isSuccess(lucky2)) option[2] = select("유니크"+type+"3");
        else option[2] = select("에픽"+type+"3");
        return option;
    }

    //레전드리 [타입, 이탈확률12]
    private String[] legendary(String type, Double lucky1, Double lucky2){
        String option[] = new String[3];
        option[0] = select("레전드리"+type+"1");
        if(isSuccess(lucky1)) option[1] = select("레전드리"+type+"2");
        else option[1] = select("유니크"+type+"2");
        if(isSuccess(lucky2)) option[2] = select("레전드리"+type+"3");
        else option[2] = select("유니크"+type+"3");
        return option;
    }

    public void useBlackCube() {
        String[] option;
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

    //테이블에서 key값에 맞는 옵션 랜덤하게 선택
    private String select(String key){
        System.out.println("Cube key : "+key);
        List table = cubeTable.percentTable.get(key);
        System.out.println("percent table : "+cubeTable.percentTable.get(key));
        if(table == null) return "Error : " + key;
        int random = tableRandom(table);
        return (String) cubeTable.optionTable.get(key).get(random);
    }
}
