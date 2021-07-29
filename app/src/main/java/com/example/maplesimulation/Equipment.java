package com.example.maplesimulation;

import java.io.Serializable;
import java.util.ArrayList;

public class Equipment implements Cloneable, Serializable {
    //private String id;
    private String name;
    private String Image;
    private String type;
    private String job;

    private int levReq;

    private int maxUp; //최대 강화수
    private int nowUp; //현재 강화수
    private int failUp; //실패한 강화수

    private int maxStar; //최대 스타포스 수치
    private int star; //스타포스 수치
    private int goldHammer;

    //기본 능력치들
    //STR, DEX, INT, LUK, 최대HP, 최대MP, 방어력, 공격력, 마력, 이동속도, 점프력, 착용레벨감소, 올스텟%, 최대HP%, 방무, 보공, 뎀지
    private ArrayList stats;

    //강화된 수치
    private ArrayList enhance;

    //추가 옵션
    private ArrayList additional;
    
    //최근 혼돈의 주문서 적용된 수치
    private ArrayList<Integer> recentChaos;

    public Equipment() {
        stats = new ArrayList();
        enhance = new ArrayList();
        additional = new ArrayList();
        goldHammer = 0;
    }

    /*------- 강화 실행 함수 ------*/
    //장비 강화 120~200제   [-1:남은 횟수X, 0:실패 1:성공]
    public int doArmorScroll2(int possibility, String justat) {
        if(nowUp+failUp == maxUp) return -1; //업그레이드 가능횟수가 없는 경우
        
        //강화 성공 시
        if(isSuccess(possibility)) {
            nowUp++;
            armorScroll2(possibility, justat);
            return 1;
        }
        failUp++;
        return 0;
    }

    //장신구 강화 75~110제  [-1:남은 횟수X, 0:실패 1:성공]
    public int doAccessaryScroll1(int possibility, String justat) {
        if(nowUp+failUp == maxUp) return -1;
        //강화 성공 시
        if(isSuccess(possibility)) {
            nowUp++;
            accessoryScroll1(possibility, justat);
            return 1;
        }
        failUp++;
        return 0;
    }
    
    //장신구 강화 120~200제  [-1:남은 횟수X, 0:실패 1:성공]
    public int doAccessaryScroll2(int possibility, String justat) {
        if(nowUp+failUp == maxUp) return -1;
        //강화 성공 시
        if(isSuccess(possibility)) {
            nowUp++;
            accessoryScroll2(possibility, justat);
            return 1;
        }
        failUp++;
        return 0;
    }

    //무기 강화 120~200제  [-1:남은 횟수X, 0:실패 1:성공]
    public int doWeaponScroll2(int possibility, String justat) {
        if(nowUp+failUp == maxUp) return -1;
        //강화 성공 시
        if(isSuccess(possibility)) {
            nowUp++;
            weaponScroll2(possibility, justat);
            return 1;
        }
        failUp++;
        return 0;
    }

    //장갑 강화 120~200제  [-1:남은 횟수X, 0:실패 1:성공]
    public int doGloveScroll2(int possibility, String justat) {
        if(nowUp+failUp == maxUp) return -1;
        //강화 성공 시
        if(isSuccess(possibility)) {
            nowUp++;
            gloveScroll(possibility, justat);
            return 1;
        }
        failUp++;
        return 0;
    }


    /*------ 능력치만 올려주는 함수 ------*/
    //방어구 강화 120~200제
    public void armorScroll2(int possibility, String justat) {
        int new_justat = 0;
        int new_hp = (int)enhance.get(4);
        int new_def = (int)enhance.get(7);

        if(possibility == 100) {
            new_justat += 3;
            new_hp += 30;
            new_def += 3;
        }
        else if(possibility == 70) {
            new_justat += 4;
            new_hp += 70;
            new_def += 5;
        }
        else if(possibility == 30) {
            new_justat += 7;
            new_hp += 120;
            new_def += 10;
        }

        if(justat == "STR") {
            new_justat += (int)enhance.get(0);
            enhance.set(0, new_justat);
            enhance.set(4, new_hp);
            enhance.set(7, new_def);
        }
        else if(justat == "DEX") {
            new_justat += (int)enhance.get(1);
            enhance.set(1, new_justat);
            enhance.set(4, new_hp);
            enhance.set(7, new_def);
        }
        else if(justat == "INT") {
            new_justat += (int)enhance.get(2);
            enhance.set(2, new_justat);
            enhance.set(4, new_hp);
            enhance.set(7, new_def);
        }
        else if(justat == "LUK") {
            new_justat += (int)enhance.get(3);
            enhance.set(3, new_justat);
            enhance.set(4, new_hp);
            enhance.set(7, new_def);
        }
    }

    //장갑 강화 120~200제
    public void gloveScroll(int possibility, String type) {
        int atk = 0;

        if(possibility == 100) {
            atk += 1;
        }
        else if(possibility == 70) {
            atk += 2;
        }
        else if(possibility == 30) {
            atk += 3;
        }

        if(type=="physic"){ //공격력
            atk += (int)enhance.get(8);
            enhance.set(8, atk);
        }
        else if(type=="magic"){ //마력
            atk += (int)enhance.get(9);
            enhance.set(9, atk);
        }
    }

    //장신구 강화 75~110제
    public void accessoryScroll1(int possibility, String justat) {
        int new_justat = 0;

        if(possibility == 100) {
            new_justat += 1;
        }
        else if(possibility == 70) {
            new_justat += 2;
        }
        else if(possibility == 30) {
            new_justat += 4;
        }

        if(justat == "STR") {
            new_justat += (int)enhance.get(0);
            enhance.set(0, new_justat);
        }
        else if(justat == "DEX") {
            new_justat += (int)enhance.get(1);
            enhance.set(1, new_justat);
        }
        else if(justat == "INT") {
            new_justat += (int)enhance.get(2);
            enhance.set(2, new_justat);
        }
        else if(justat == "LUK") {
            new_justat += (int)enhance.get(3);
            enhance.set(3, new_justat);
        }
    }

    //장신구 강화 120~200제
    public void accessoryScroll2(int possibility, String justat) {
        int new_justat = 0;

        if(possibility == 100) {
            new_justat += 2;
        }
        else if(possibility == 70) {
            new_justat += 3;
        }
        else if(possibility == 30) {
            new_justat += 5;
        }

        if(justat == "STR") {
            new_justat += (int)enhance.get(0);
            enhance.set(0, new_justat);
        }
        else if(justat == "DEX") {
            new_justat += (int)enhance.get(1);
            enhance.set(1, new_justat);
        }
        else if(justat == "INT") {
            new_justat += (int)enhance.get(2);
            enhance.set(2, new_justat);
        }
        else if(justat == "LUK") {
            new_justat += (int)enhance.get(3);
            enhance.set(3, new_justat);
        }
    }
    
    //무기 강화 120~200제
    public void weaponScroll2(int possibility, String justat) {
        int new_justat = 0;
        int atk = 0;

        if(possibility == 100) {
            new_justat += 1;
            atk += 3;
        }
        else if(possibility == 70) {
            new_justat += 2;
            atk += 5;
        }
        else if(possibility == 30) {
            new_justat += 3;
            atk += 7;
        }
        else if(possibility == 15) {
            new_justat += 4;
            atk += 9;
        }

        if(justat == "STR") {
            new_justat += (int)enhance.get(0);
            enhance.set(0, new_justat);

            atk += (int)enhance.get(8);
            enhance.set(8, atk);
        }
        else if(justat == "DEX") {
            new_justat += (int)enhance.get(1);
            enhance.set(1, new_justat);

            atk += (int)enhance.get(8);
            enhance.set(8, atk);
        }
        else if(justat == "INT") {
            new_justat += (int)enhance.get(2);
            enhance.set(2, new_justat);

            atk += (int)enhance.get(9);
            enhance.set(9, atk);
        }
        else if(justat == "LUK") {
            new_justat += (int)enhance.get(3);
            enhance.set(3, new_justat);

            atk += (int)enhance.get(8);
            enhance.set(8, atk);
        }
    }


    /*---- 각종 주문서 ----*/
    //순백의 주문서
    public int doWhiteScroll(int possibility) {
        if(failUp==0) return -1; //복구할 횟수가 없는 경우

        if(isSuccess(possibility) == true) {
            failUp--;
            return 1;
        }

        return 0;
    }

    //황금망치
    public int useGoldHammer(int possibility) {
        if(goldHammer==1) return -1; //이미 사용한 경우

        if(isSuccess(possibility) == true) {
            goldHammer++;
            maxUp++;
            return 1; //성공
        }
        else{
            goldHammer++;
            failUp++;
            maxUp++;
        }
        return 0; //실패
    }

    //이노센트 주문서
    public int useInnocent(int possibility) {
        if(nowUp==0 && failUp==0 && goldHammer==0) return -1; //사용할 필요가 없는 경우

        if(isSuccess(possibility) == true) {
            if(goldHammer>0) maxUp--;
            nowUp=0;
            goldHammer=0;
            failUp=0;
            for(int i=0; i<enhance.size(); i++){
                enhance.set(i, 0);
            }
            return 1; //성공
        }
        return 0; //실패
    }
    
    //놀라운 긍정의 혼돈 주문서
    public int useAwesomeChaos(int possible) {
        if(nowUp+failUp==maxUp) return -1; //업그레이드 가능횟수가 없는 경우

        recentChaos = new ArrayList<Integer>();

        //주문서 사용 성공
        if(isSuccess(possible)){
            nowUp++;
            double table[] = {5.93, 4.94, 13.87, 23.87, 33.01, 18.38};
            int prev = 0; //이전 스텟
            int random = 0; //랜덤으로 생성된 숫자
            
            for(int i=0; i<11; i++){
                prev = (int) enhance.get(i);
                if(i==4 || i==5) random = (tableRandom(table)*10); //Hp, Mp 는 10단위
                else random = tableRandom(table);
                enhance.set(i, prev+random);
                recentChaos.add(random);
            }
            return 1;
        }
        failUp++;
        return 0;
    }

    //리턴 스크롤 사용
    public void useReturnScroll() {
        System.out.println("리턴 스크롤을 사용합니다.");
        int tmp = 0;
        for(int i=0; i<recentChaos.size(); i++){
            tmp = (int)enhance.get(i);
            tmp -= recentChaos.get(i);
            enhance.set(i, tmp);
            recentChaos.set(i, 0);
        }
        nowUp--;
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
    
    
    //정해진 확률에 따른 성공 여부
    public Boolean isSuccess(int possibility) {
        int random = (int)(Math.random()*100);

        if(random < possibility) return true;
        return false;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getName() {
        return name;
    }

    public String getImage() { return Image; }

    public String getType() { return type; }

    public String getJob() { return  job; }

    public int getLevReq() { return levReq; }

    public int getMaxUp() { return maxUp; }

    public int getNowUp() { return nowUp; }

    public int getFailUp() { return failUp; }

    public ArrayList getStats() {
        return stats;
    }

    public ArrayList getEnhance() {
        return enhance;
    }

    public int getGoldHammer() { return goldHammer; }

    public ArrayList getRecentChaos() { return recentChaos; }



    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) { this.Image = image; }

    public void setType(String type) {
        this.type = type;
    }

    public void setJob(String job) { this.job = job; }

    public void setLevReq(int levReq) {
        this.levReq = levReq;
    }

    public void setMaxUp(int maxUp) { this.maxUp = maxUp; }

    public void setNowUp(int nowUp) { this.nowUp = nowUp; }

    public void setFailUp(int failUp) { this.failUp = failUp; }

    public void setMaxStar(int maxStar) { this.maxStar = maxStar; }

    public void setStar(int star) { this.star = star; }

    public void addStats(int stat) {
        this.stats.add(stat);
        this.enhance.add(0);
        this.additional.add(0);
    }

}
