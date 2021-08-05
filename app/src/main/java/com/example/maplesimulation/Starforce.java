package com.example.maplesimulation;

import java.util.HashMap;

public class Starforce {
    Equipment equipment;
    int chanceStack; //찬스타임 스택
    public String event;
    public boolean prevent;

    public double table_success[]; //성공 확률
    public double table_destroyed[]; //파괴 확률

    public HashMap<String, Integer> starStatTable; //방어구,공격력 증가 수치 테이블,
    // key:렙제+방어구+다음 스타포스 수치


    public Starforce(Equipment equipment) {
        this.equipment = equipment;
        this.event="이벤트 없음";
        initTable();
        chanceStack = 0;
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

    //찬스타임인지 반환
    public boolean isChanceTime(){
        if(chanceStack == 2) return true;
        return false;
    }

    //파괴방지 사용가능한지
    public boolean canPrevent() {
        if(equipment.getStar()<=17 && equipment.getStar()>=12) return true;
        return false;
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
    
    //1516이벤트 적용 가능한지
    public boolean can1516event() {
        int star = equipment.getStar();
        if(event.equals("5,10,15성100%") && (star==5 || star==10 || star==15))
            return true;
        return false;
    }

    //10성 이하 2배 이벤트 가능한지
    public boolean canDouble() {
        int star = equipment.getStar();
        if(event.equals("10성1+1") && star<=10) return true;
        return false;
    }

    //스타포스 실행 (스타캐치, 파괴방지)
    public int doStarforce(boolean starCatch) {
        int star = equipment.getStar();
        equipment.used_meso+=howMuch(equipment.getLevReq(), star);
        double success = this.table_success[star];
        if(starCatch) success *= 1.05;
        double destroy = this.table_destroyed[star];
        if(this.prevent) destroy = 0;
        double fail = 100.0 - success - destroy;
        int result = -1;

        if(equipment.getStar() >= equipment.getMaxStar()) return -1;

        //찬스타임인경우
        if(chanceStack==2) {
            success = 100.0;
            chanceStack = 0;
        }

        if(can1516event()) success=100;

        result = tableRandom(new double[]{success, destroy, fail});


        if(result == 0) { //성공
            if(canDouble()) success();
            success();
        }
        else if(result == 1) { //파괴
            destroyed();
        }
        else if(result == 2) { //실패
            failed();
        }

        return result;
    }

    //현재 장비 강화에 필요한 메소  (레벨제한, 강화단계)
    public int howMuch(int reqlv, int step) {
        int money = 0;

        if(step <= 9) {
            money = 1000+(int)Math.round((Math.pow(reqlv, 3)*(step)/25)/100.0)*100;
        }
        else if(step <= 14) {
            money = 1000+(int)Math.round((Math.pow(reqlv, 3)*Math.pow(step, 2.7)/400)/100.0)*100;
        }
        else {
            money = 1000+(int)Math.round((Math.pow(reqlv, 3)*Math.pow(step, 2.7)/200)/100.0)*100;
        }

        if(event.equals("30%할인")) money = (int)(money*0.7);
        if(prevent) money*=2;


        return money;
    }

    public void success() {
        int list[] = increment(equipment.getStar()); //주스텟, 공격력, 마력
        for(int i=0; i<4; i++){
            if((int)equipment.getStats().get(i) > 0) equipment.upgradeStarStat(i, list[0]);
        }
        if((int)equipment.getStats().get(8) > 0) equipment.upgradeStarStat(8, list[1]);
        if((int)equipment.getStats().get(9) > 0) equipment.upgradeStarStat(9, list[2]);

        equipment.upStar();
        chanceStack=0;
    }

    public void destroyed() {
        chanceStack=0;
        equipment.initStarStat();
        for(int i=0; i<12; i++) success();
    }

    //하락 가능성 있는지
    public boolean canDown() {
        if(equipment.getStar() <= 10 || equipment.getStar()==15 || equipment.getStar()==20){
                return false;
        }
        else {
            return true;
        }
    }

    public void failed() {
        if(canDown()){
            equipment.downStar();
            int list[] = increment(equipment.getStar()); //주스텟, 공격력, 마력
            for(int i=0; i<4; i++){
                if((int)equipment.getStats().get(i) > 0) equipment.downStarStat(i, list[0]);
            }
            if((int)equipment.getStats().get(8) > 0) equipment.downStarStat(8, list[1]);
            if((int)equipment.getStats().get(9) > 0) equipment.downStarStat(9, list[2]);
            chanceStack++;
        }
        else chanceStack=0;
    }

    //스타포스로 인해 증가할 수치 계산 [주스텟, 공격력, 마력] 반환
    public int[] increment(int star) {
        int stats[] = {0, 0, 0}; //주스텟, 공격력, 마력

        //주스텟 상승, 무기 방어구 동일
        if(star < 5) {
            stats[0] = 2;
        }
        else if(star < 15) {
            stats[0] = 3;
        }
        else {
            if(equipment.getLevReq() == 130) {
                stats[0] = 7;
            }
            else if(equipment.getLevReq() == 140) {
                stats[0] = 9;
            }
            else if(equipment.getLevReq() == 150) {
                stats[0] = 11;
            }
            else if(equipment.getLevReq() == 160) {
                stats[0] = 13;
            }
            else if(equipment.getLevReq() == 200) {
                stats[0] = 15;
            }
        }

        //15성 이하 공격력 상승 방어구, 무기 다름
        if(star < 15) { //0->1 ~ 14->15
            if(equipment.isWeapon()) {
                if((int)equipment.getStats().get(9) > 0) { //법사 직업
                    stats[2] = (int)equipment.getStats().get(9)/50+1;
                }
                else{ //공격력 직업
                    stats[1] = (int)equipment.getStats().get(8)/50+1;
                }

            }
            else if(equipment.getType().equals("장갑")){
                if(star==4||star==6||star==8||star==10
                        ||star==12||star==13||star==14){
                    stats[1]++;
                    stats[2]++;
                }
            }
        }
        else { //15성 이상
            int levReq = equipment.getLevReq();
            int step = star + 1;

            if(equipment.isWeapon()) {
                if((int)equipment.getStats().get(9) > 0) { //법사 직업
                    stats[2] = starStatTable.get(levReq+"무기"+step);
                }
                else{ //공격력 직업
                    stats[1] = starStatTable.get(levReq+"무기"+step);
                }
            }
            else {//방어구인 경우
                stats[2] = starStatTable.get(levReq+"방어구"+step);
                stats[1] = starStatTable.get(levReq+"방어구"+step);
            }
        }

        return stats;
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

