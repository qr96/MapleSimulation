package com.example.maplesimulation;

import java.util.HashMap;

public class Noljang {
    public HashMap<String, Integer[]> table;
    public double[] percent = {60.0, 55.0, 50.0, 40.0, 30.0, 20.0, 19.0, 18.0, 17.0, 16.0, 14.0, 12.0, 10.0};
    Equipment equipment;

    //놀장, 슈페리얼 테이블
    public Noljang(Equipment equipment) {
        this.equipment = equipment;
        initTable();
    }

    public int useNoljang() {
        if(equipment.isStarforce || equipment.getStar()==equipment.getMaxStar()) return -1; //이미 스타포스 작이 되어있는 경우
        int star = equipment.getStar();

        if(equipment.getMaxStar()>15){
            equipment.tmpMaxStar = equipment.getMaxStar();
            equipment.setMaxStar(15);
        }

        equipment.isNoljang = true;
        equipment.used_noljang++;

        if(star < 12) {
            if(isSuccess(percent[star])){ //성공
                equipment.upStar();
                upStat();
                return 1;
            }
            else{
                equipment.used_protect_shield++;
                return 0;
            }
        }
        else {
            if(isSuccess(10.0)){ //성공
                equipment.upStar();
                upStat();
                return 1;
            }
            else{
                equipment.used_protect_shield++;
                return 0;
            }
        }
    }

    public void upStat(){
        String key = "";
        if(equipment.isArmor() || equipment.isAccessary() || equipment.getType().equals("기계심장")) {
            key = equipment.getLevReq()+"방어구"+equipment.getStar();
            if(equipment.getStar()>12) key = equipment.getLevReq()+"방어구12"; //12성 이상부터 확률 동일

        }
        else return;

        int bonus = 0; //증가치

        //120제 이상 장신구 보너스 스텟
        if(equipment.isAccessary()){
            if(equipment.getLevReq()>=120) bonus = 2;
            else bonus=1;
        }
        for(int i=0; i<4; i++){
            equipment.addEnhanceStat(i, table.get(key)[0]+bonus); //스텟 증가
        }
        equipment.addEnhanceStat(8, table.get(key)[1]); //공격력
        equipment.addEnhanceStat(9, table.get(key)[1]); //마력
    }

    //정해진 확률에 따른 성공 여부
    public Boolean isSuccess(double possibility) {
        double random = (double)(Math.random()*100);

        if(random < possibility) return true;
        return false;
    }

    public void initTable() {
        table = new HashMap<>();

        table.put("150방어구1", new Integer[]{19, 0});
        table.put("150방어구2", new Integer[]{20, 0});
        table.put("150방어구3", new Integer[]{22, 0});
        table.put("150방어구4", new Integer[]{25, 0});
        table.put("150방어구5", new Integer[]{29, 0});
        table.put("150방어구6", new Integer[]{0, 9});
        table.put("150방어구7", new Integer[]{0, 10});
        table.put("150방어구8", new Integer[]{0, 11});
        table.put("150방어구9", new Integer[]{0, 12});
        table.put("150방어구10", new Integer[]{0, 13});
        table.put("150방어구11", new Integer[]{0, 14});
        table.put("150방어구12", new Integer[]{0, 16});

        table.put("145방어구1", new Integer[]{18, 0});
        table.put("145방어구2", new Integer[]{19, 0});
        table.put("145방어구3", new Integer[]{21, 0});
        table.put("145방어구4", new Integer[]{24, 0});
        table.put("145방어구5", new Integer[]{28, 0});
        table.put("145방어구6", new Integer[]{0, 8});
        table.put("145방어구7", new Integer[]{0, 9});
        table.put("145방어구8", new Integer[]{0, 10});
        table.put("145방어구9", new Integer[]{0, 11});
        table.put("145방어구10", new Integer[]{0, 12});
        table.put("145방어구11", new Integer[]{0, 13});
        table.put("145방어구12", new Integer[]{0, 15});

        table.put("140방어구1", new Integer[]{17, 0});
        table.put("140방어구2", new Integer[]{18, 0});
        table.put("140방어구3", new Integer[]{20, 0});
        table.put("140방어구4", new Integer[]{23, 0});
        table.put("140방어구5", new Integer[]{27, 0});
        table.put("140방어구6", new Integer[]{0, 8});
        table.put("140방어구7", new Integer[]{0, 9});
        table.put("140방어구8", new Integer[]{0, 10});
        table.put("140방어구9", new Integer[]{0, 11});
        table.put("140방어구10", new Integer[]{0, 12});
        table.put("140방어구11", new Integer[]{0, 13});
        table.put("140방어구12", new Integer[]{0, 15});

        table.put("135방어구1", new Integer[]{15, 0});
        table.put("135방어구2", new Integer[]{16, 0});
        table.put("135방어구3", new Integer[]{18, 0});
        table.put("135방어구4", new Integer[]{21, 0});
        table.put("135방어구5", new Integer[]{25, 0});
        table.put("135방어구6", new Integer[]{0, 7});
        table.put("135방어구7", new Integer[]{0, 8});
        table.put("135방어구8", new Integer[]{0, 9});
        table.put("135방어구9", new Integer[]{0, 10});
        table.put("135방어구10", new Integer[]{0, 11});
        table.put("135방어구11", new Integer[]{0, 12});
        table.put("135방어구12", new Integer[]{0, 14});

        table.put("130방어구1", new Integer[]{14, 0});
        table.put("130방어구2", new Integer[]{15, 0});
        table.put("130방어구3", new Integer[]{17, 0});
        table.put("130방어구4", new Integer[]{20, 0});
        table.put("130방어구5", new Integer[]{24, 0});
        table.put("130방어구6", new Integer[]{0, 7});
        table.put("130방어구7", new Integer[]{0, 8});
        table.put("130방어구8", new Integer[]{0, 9});
        table.put("130방어구9", new Integer[]{0, 10});
        table.put("130방어구10", new Integer[]{0, 11});
        table.put("130방어구11", new Integer[]{0, 12});
        table.put("130방어구12", new Integer[]{0, 14});

        table.put("120방어구1", new Integer[]{12, 0});
        table.put("120방어구2", new Integer[]{13, 0});
        table.put("120방어구3", new Integer[]{15, 0});
        table.put("120방어구4", new Integer[]{18, 0});
        table.put("120방어구5", new Integer[]{22, 0});
        table.put("120방어구6", new Integer[]{0, 6});
        table.put("120방어구7", new Integer[]{0, 7});
        table.put("120방어구8", new Integer[]{0, 8});
        table.put("120방어구9", new Integer[]{0, 9});
        table.put("120방어구10", new Integer[]{0, 10});
        table.put("120방어구11", new Integer[]{0, 11});
        table.put("120방어구12", new Integer[]{0, 13});

        table.put("110방어구1", new Integer[]{9, 0});
        table.put("110방어구2", new Integer[]{10, 0});
        table.put("110방어구3", new Integer[]{12, 0});
        table.put("110방어구4", new Integer[]{15, 0});
        table.put("110방어구5", new Integer[]{19, 0});
        table.put("110방어구6", new Integer[]{0, 5});
        table.put("110방어구7", new Integer[]{0, 6});
        table.put("110방어구8", new Integer[]{0, 7});
        table.put("110방어구9", new Integer[]{0, 8});
        table.put("110방어구10", new Integer[]{0, 9});
        table.put("110방어구11", new Integer[]{0, 10});
        table.put("110방어구12", new Integer[]{0, 12});

        table.put("100방어구1", new Integer[]{7, 0});
        table.put("100방어구2", new Integer[]{8, 0});
        table.put("100방어구3", new Integer[]{10, 0});
        table.put("100방어구4", new Integer[]{13, 0});
        table.put("100방어구5", new Integer[]{17, 0});
        table.put("100방어구6", new Integer[]{0, 4});
        table.put("100방어구7", new Integer[]{0, 5});
        table.put("100방어구8", new Integer[]{0, 6});
        table.put("100방어구9", new Integer[]{0, 7});
        table.put("100방어구10", new Integer[]{0, 8});
        table.put("100방어구11", new Integer[]{0, 9});
        table.put("100방어구12", new Integer[]{0, 11});
    }
}
