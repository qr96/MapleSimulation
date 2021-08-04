package com.example.maplesimulation;

import java.util.ArrayList;

public class Scroll {
    Equipment equip;

    //최근 혼돈의 주문서 적용된 수치
    private ArrayList<Integer> recentChaos;

    //Constructor
    public Scroll(Equipment equipment){
        this.equip = equipment;
    }

    //정해진 확률에 따른 성공 여부
    public Boolean isSuccess(int possibility) {
        int random = (int)(Math.random()*100);

        if(random < possibility) return true;
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

    public ArrayList<Integer> getRecentChaos(){
        return recentChaos;
    }

    /*---- 각종 주문서 ----*/
    //순백의 주문서
    public int doWhiteScroll(int possibility) {
        if(equip.getFailUp()==0) return -1; //복구할 횟수가 없는 경우

        if(isSuccess(possibility) == true) {
            equip.recoveryFailed();
            return 1;
        }

        return 0;
    }

    //황금망치
    public int useGoldHammer(int possibility) {
        if(equip.getGoldHammer() == 1) return -1; //이미 사용한 경우

        if(isSuccess(possibility) == true) {
            equip.useGoldhammer();
            return 1; //성공
        }
        else{
            equip.useGoldhammer();
            equip.failedScroll();
        }
        return 0; //실패
    }

    //이노센트 주문서
    public int useInnocent(int possibility) {
        if(equip.getNowUp()==0 && equip.getFailUp()==0 && equip.getGoldHammer()==0 && equip.getStar()==0)
            return -1; //사용할 필요가 없는 경우

        if(isSuccess(possibility) == true) {
            equip.useInnocent();
            return 1; //성공
        }
        return 0; //실패
    }

    //놀라운 긍정의 혼돈 주문서
    public int useAwesomeChaos(int possible) {
        if(equip.isFinishEnchant()) return -1; //업그레이드 가능횟수가 없는 경우

        recentChaos = new ArrayList<Integer>();

        //주문서 사용 성공
        if(isSuccess(possible)){
            equip.successScroll();
            double table[] = {18.38, 33.01, 23.87, 13.87, 4.94, 0.0, 5.93}; //{5.93, 4.94, 13.87, 23.87, 33.01, 18.38};
            int prev = 0; //이전 스텟
            int random = 0; //랜덤으로 생성된 숫자

            for(int i=0; i<12; i++){
                if(i==6) {
                    recentChaos.add(0);
                    continue;
                }
                random = tableRandom(table);
                if(i==4 || i==5) random *= 10; //Hp, Mp 는 10단위이므로
                equip.addEnhanceStat(i, random);
                recentChaos.add(random);
            }
            return 1;
        }
        equip.failedScroll();
        return 0;
    }

    //리턴 스크롤 사용
    public void useReturnScroll() {
        System.out.println("리턴 스크롤을 사용합니다.");
        int tmp = 0;
        for(int i=0; i<recentChaos.size(); i++){
            equip.subEnhanceStat(i, recentChaos.get(i));
            recentChaos.set(i, 0);
        }
        equip.recoverySuccess();
    }



    /*------------------ 주흔작 -----------------*/
    /*------- 강화 실행 함수 ------*/
    //방어구 강화 120~200제   [-1:남은 횟수X, 0:실패 1:성공]
    public int doArmorScroll2(int possibility, String justat) {
        if(equip.isFinishEnchant()) return -1; //업그레이드 가능횟수가 없는 경우

        //강화 성공 시
        if(isSuccess(possibility)) {
            equip.successScroll();
            armorScroll2(possibility, justat);
            return 1;
        }
        equip.failedScroll();
        return 0;
    }

    //장신구 강화 75~110제  [-1:남은 횟수X, 0:실패 1:성공]
    public int doAccessaryScroll1(int possibility, String justat) {
        if(equip.isFinishEnchant()) return -1;
        //강화 성공 시
        if(isSuccess(possibility)) {
            equip.successScroll();
            accessoryScroll1(possibility, justat);
            return 1;
        }
        equip.failedScroll();
        return 0;
    }

    //장신구 강화 120~200제  [-1:남은 횟수X, 0:실패 1:성공]
    public int doAccessaryScroll2(int possibility, String justat) {
        if(equip.isFinishEnchant()) return -1;
        //강화 성공 시
        if(isSuccess(possibility)) {
            equip.successScroll();
            accessoryScroll2(possibility, justat);
            return 1;
        }
        equip.failedScroll();
        return 0;
    }

    //무기 강화 120~200제  [-1:남은 횟수X, 0:실패 1:성공]
    public int doWeaponScroll2(int possibility, String justat) {
        if(equip.isFinishEnchant()) return -1;
        //강화 성공 시
        if(isSuccess(possibility)) {
            equip.successScroll();
            weaponScroll2(possibility, justat);
            return 1;
        }
        equip.failedScroll();
        return 0;
    }

    //장갑 강화 120~200제  [-1:남은 횟수X, 0:실패 1:성공]
    public int doGloveScroll2(int possibility, String justat) {
        if(equip.isFinishEnchant()) return -1;
        //강화 성공 시
        if(isSuccess(possibility)) {
            equip.successScroll();
            gloveScroll(possibility, justat);
            return 1;
        }
        equip.failedScroll();
        return 0;
    }

    /*------ 능력치 올려주는 함수 ------*/
    //방어구 강화 120~200제
    private void armorScroll2(int possibility, String justat) {
        int new_justat = 0;
        int new_hp = 0;
        int new_def = 0;

        if(possibility == 100) {
            new_justat += 3;
            new_hp = 30;
            new_def = 3;
        }
        else if(possibility == 70) {
            new_justat = 4;
            new_hp = 70;
            new_def = 5;
        }
        else if(possibility == 30) {
            new_justat = 7;
            new_hp = 120;
            new_def = 10;
        }

        if(justat == "STR") {
            equip.addEnhanceStat(0, new_justat);
            equip.addEnhanceStat(4, new_hp);
            equip.addEnhanceStat(7, new_def);
        }
        else if(justat == "DEX") {
            equip.addEnhanceStat(1, new_justat);
            equip.addEnhanceStat(4, new_hp);
            equip.addEnhanceStat(7, new_def);
        }
        else if(justat == "INT") {
            equip.addEnhanceStat(2, new_justat);
            equip.addEnhanceStat(4, new_hp);
            equip.addEnhanceStat(7, new_def);
        }
        else if(justat == "LUK") {
            equip.addEnhanceStat(3, new_justat);
            equip.addEnhanceStat(4, new_hp);
            equip.addEnhanceStat(7, new_def);
        }
        else if(justat == "HP") {
            if(possibility == 100) new_hp = 180;
            else if(possibility == 70) new_hp = 270;
            else if(possibility == 100) new_hp = 470;
            equip.addEnhanceStat(4, new_hp);
            equip.addEnhanceStat(7, new_def);
        }
    }

    //장갑 강화 120~200제
    private void gloveScroll(int possibility, String type) {
        int atk = 0;

        if(possibility == 100) {
            atk = 1;
        }
        else if(possibility == 70) {
            atk = 2;
        }
        else if(possibility == 30) {
            atk = 3;
        }

        if(type=="physic"){ //공격력
            equip.addEnhanceStat(8, atk);
        }
        else if(type=="magic"){ //마력
            equip.addEnhanceStat(9, atk);
        }
    }

    //장신구 강화 75~110제
    private void accessoryScroll1(int possibility, String justat) {
        int new_justat = 0;

        if(possibility == 100) {
            new_justat = 1;
        }
        else if(possibility == 70) {
            new_justat = 2;
        }
        else if(possibility == 30) {
            new_justat = 4;
        }

        if(justat == "STR") {
            equip.addEnhanceStat(0, new_justat);
        }
        else if(justat == "DEX") {
            equip.addEnhanceStat(1, new_justat);
        }
        else if(justat == "INT") {
            equip.addEnhanceStat(2, new_justat);
        }
        else if(justat == "LUK") {
            equip.addEnhanceStat(3, new_justat);
        }
        else if(justat == "HP") {
            equip.addEnhanceStat(4, new_justat * 50);
        }
    }

    //장신구 강화 120~200제
    private void accessoryScroll2(int possibility, String justat) {
        int new_justat = 0;

        if(possibility == 100) {
            new_justat = 2;
        }
        else if(possibility == 70) {
            new_justat = 3;
        }
        else if(possibility == 30) {
            new_justat = 5;
        }

        if(justat == "STR") {
            equip.addEnhanceStat(0, new_justat);
        }
        else if(justat == "DEX") {
            equip.addEnhanceStat(1, new_justat);
        }
        else if(justat == "INT") {
            equip.addEnhanceStat(2, new_justat);
        }
        else if(justat == "LUK") {
            equip.addEnhanceStat(3, new_justat);
        }
        else if(justat == "HP") {
            equip.addEnhanceStat(0, new_justat * 50);
        }
    }

    //무기 강화 120~200제
    private void weaponScroll2(int possibility, String justat) {
        int new_justat = 0;
        int atk = 0;

        if(possibility == 100) {
            new_justat = 1;
            atk += 3;
        }
        else if(possibility == 70) {
            new_justat = 2;
            atk += 5;
        }
        else if(possibility == 30) {
            new_justat = 3;
            atk += 7;
        }
        else if(possibility == 15) {
            new_justat = 4;
            atk += 9;
        }

        if(justat == "STR") {
            equip.addEnhanceStat(0, new_justat);
            equip.addEnhanceStat(8, atk);
        }
        else if(justat == "DEX") {
            equip.addEnhanceStat(1, new_justat);
            equip.addEnhanceStat(8, atk);
        }
        else if(justat == "INT") {
            equip.addEnhanceStat(2, new_justat);
            equip.addEnhanceStat(9, atk);
        }
        else if(justat == "LUK") {
            equip.addEnhanceStat(3, new_justat);
            equip.addEnhanceStat(8, atk);
        }
    }
}
