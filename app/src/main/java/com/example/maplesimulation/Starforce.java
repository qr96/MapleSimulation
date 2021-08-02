package com.example.maplesimulation;

import java.util.HashMap;

public class Starforce {
    Equipment equipment;

    public double table_success[]; //성공 확률
    public double table_destroyed[]; //파괴 확률

    public HashMap<String, Integer> starStatTable; //방어구,공격력 증가 수치 테이블,
    // key:렙제+방어구+다음 스타포스 수치

    //0~10 15, 20성 하락X

    public Starforce(Equipment equipment) {
        this.equipment = equipment;
        initTable();
    }

    //테이블 초기화
    public void initTable() {
        this.table_success = new double[]{
                95.0, 90.0, 85.0, 85.0, 80.0, 75.0, 70.0, 65.0, 60.0, 55.0, //0~9
                50.0, 45.0, 40.0, 35.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, //10~19
                30.0, 30.0, 3.0, 2.0, 1.0
        };
        this.table_destroyed = new double[]{
                0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, //0~9
                0.0, 0.0, 0.6, 1.3, 1.4, 2.1, 2.1, 2.1, 2.8, 2.8, //10~19
                7.0, 7.0, 19.4, 29.4, 39.6
        };

        initStarStatTable();
    }

    // 테이블 확률대로 하나 반환
    public int tableRandom(double[] table) {
        // 번호 생성
        double ran = Math.random() * 100;
        double w = 0;

        for (int i = 0; i < table.length; i++) {
            w = w + table[i];

            if (ran < w) {
                return i;
            }
        }
        return -1;
    }

    public int doStarforce() {
        int star = equipment.getStar();
        double success = this.table_success[star];
        double destroy = this.table_destroyed[star];
        double fail = 100.0 - success - destroy;
        int result = -1;

        if(equipment.getStar() >= equipment.getMaxStar()) return -1;

        result = tableRandom(new double[]{success, destroy, fail});

        if(result == 0) { //성공
            upgradeStar();
        }
        else if(result == 1) { //파괴
            equipment.initStarStat();
        }
        else if(result == 2) { //실패

        }

        return result;
    }

    //현재 장비 강화에 필요한 메소  (레벨제한, 강화단계)
    public int howMuch(int reqlv, int step) {
        int money = 0;

        if(step <= 9) {
            money = 1000+(int)(Math.pow(reqlv, 3)*(step)/25)/100*100;
        }
        else if(step <= 14) {
            money = 1000+(int)(Math.pow(reqlv, 3)*Math.pow(step, 2.7)/400)/100*100;
        }
        else {
            money = 1000+(int)(Math.pow(reqlv, 3)*Math.pow(step, 2.7)/200)/100*100;
        }

        return money;
    }

    public void upgradeStar() {
        equipment.upStar(); //스타포스 수치 증가

        //주스텟 상승, 무기 방어구 동일
        if(equipment.getStar() <= 5) {
            for(int i=0; i<4; i++){
                equipment.upgradeStarStat(i, 2);
            }
        }
        else if(equipment.getStar() <= 15) {
            for(int i=0; i<4; i++){
                equipment.upgradeStarStat(i, 3);
            }
        }
        else {
            if(equipment.getLevReq() == 130) {
                for(int i=0; i<4; i++){
                    equipment.upgradeStarStat(i, 7);
                }
            }
            else if(equipment.getLevReq() == 140) {
                for(int i=0; i<4; i++){
                    equipment.upgradeStarStat(i, 9);
                }
            }
            else if(equipment.getLevReq() == 150) {
                for(int i=0; i<4; i++){
                    equipment.upgradeStarStat(i, 11);
                }
            }
            else if(equipment.getLevReq() == 160) {
                for(int i=0; i<4; i++){
                    equipment.upgradeStarStat(i, 13);
                }
            }
            else if(equipment.getLevReq() == 200) {
                for(int i=0; i<4; i++){
                    equipment.upgradeStarStat(i, 15);
                }
            }
        }
        
        //공격력 상승 방어구, 무기 다름
        
    }

    public void initStarStatTable(){
        starStatTable = new HashMap<>();
        starStatTable.put("130방어구16", 7);
        starStatTable.put("130방어구17", 8);
        starStatTable.put("130방어구18", 9);
        starStatTable.put("130방어구19", 10);
        starStatTable.put("130방어구20", 11);

        starStatTable.put("150방어구16", 9);
        starStatTable.put("150방어구17", 10);
        starStatTable.put("150방어구18", 11);
        starStatTable.put("150방어구19", 12);
        starStatTable.put("150방어구20", 13);
        starStatTable.put("150방어구21", 14);
        starStatTable.put("150방어구22", 16);
        starStatTable.put("150방어구23", 18);
        starStatTable.put("150방어구24", 20);
        starStatTable.put("150방어구25", 22);

        starStatTable.put("160방어구16", 10);
        starStatTable.put("160방어구17", 11);
        starStatTable.put("160방어구18", 12);
        starStatTable.put("160방어구19", 13);
        starStatTable.put("160방어구20", 14);
        starStatTable.put("160방어구21", 15);
        starStatTable.put("160방어구22", 17);
        starStatTable.put("160방어구23", 19);
        starStatTable.put("160방어구24", 21);
        starStatTable.put("160방어구25", 23);

        starStatTable.put("200방어구16", 12);
        starStatTable.put("200방어구17", 13);
        starStatTable.put("200방어구18", 14);
        starStatTable.put("200방어구19", 15);
        starStatTable.put("200방어구20", 16);
        starStatTable.put("200방어구21", 17);
        starStatTable.put("200방어구22", 19);
        starStatTable.put("200방어구23", 21);
        starStatTable.put("200방어구24", 23);
        starStatTable.put("200방어구25", 25);

        starStatTable.put("150무기16", 8);
        starStatTable.put("150무기17", 9);
        starStatTable.put("150무기18", 9);
        starStatTable.put("150무기19", 10);
        starStatTable.put("150무기20", 11);
        starStatTable.put("150무기21", 12);
        starStatTable.put("150무기22", 13);
        starStatTable.put("150무기23", 31);
        starStatTable.put("150무기24", 32);
        starStatTable.put("150무기25", 33);

        starStatTable.put("160무기16", 9);
        starStatTable.put("160무기17", 9);
        starStatTable.put("160무기18", 10);
        starStatTable.put("160무기19", 11);
        starStatTable.put("160무기20", 12);
        starStatTable.put("160무기21", 13);
        starStatTable.put("160무기22", 14);
        starStatTable.put("160무기23", 32);
        starStatTable.put("160무기24", 33);
        starStatTable.put("160무기25", 34);

        starStatTable.put("200무기16", 13);
        starStatTable.put("200무기17", 13);
        starStatTable.put("200무기18", 14);
        starStatTable.put("200무기19", 14);
        starStatTable.put("200무기20", 15);
        starStatTable.put("200무기21", 16);
        starStatTable.put("200무기22", 17);
        starStatTable.put("200무기23", 34);
        starStatTable.put("200무기24", 35);
        starStatTable.put("200무기25", 36);
    }
}

