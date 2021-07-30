package com.example.maplesimulation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Equipment implements Cloneable, Serializable {
    //private String id;
    private String name;
    private String Image;
    private String type; //장비 부위
    private String job;

    private int levReq;

    private int maxUp; //최대 강화수
    private int nowUp; //현재 강화수
    private int failUp; //실패한 강화수

    private int maxStar; //최대 스타포스 수치
    private int star; //스타포스 수치
    private int goldHammer;

    // 능력치 순서 {"STR", "DEX", "INT", "LUK", "최대HP", "최대MP", "착용레벨감소",
    //                "방어력", "공격력", "마력", "이동속도", "점프력", "올스텟%",
    //                "보스데미지%", "데미지%", "최대HP%", "방어율무시%"};

    //기본 능력치들
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
            double table[] = {18.38, 33.01, 23.87, 13.87, 4.94, 0.0, 5.93}; //{5.93, 4.94, 13.87, 23.87, 33.01, 18.38};
            int prev = 0; //이전 스텟
            int random = 0; //랜덤으로 생성된 숫자
            
            for(int i=0; i<12; i++){
                if(i==6) {
                    recentChaos.add(0);
                    continue;
                }
                prev = (int) enhance.get(i);
                random = tableRandom(table);
                if(i==4 || i==5) random *= 10; //Hp, Mp 는 10단위이므로
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
            tmp = (int)enhance.get(i) - recentChaos.get(i);
            enhance.set(i, tmp);
            recentChaos.set(i, 0);
        }
        nowUp--;
    }


    /*------ 환생의 불꽃 -----*/
    //강력한 환생의 불꽃
    public int usePowerfulFlame() {

        int grade = -1; //추가 옵션의 등급
        int selected[]; // 선택된 추가옵션들
        double table[] = {20, 30, 36, 14, 0}; //강환불 추옵 등급 확률 테이블

        selected = selectN(19, 4); //강환불은 4개의 추옵 상승

        for(int i=0; i<selected.length; i++){
            grade = tableRandom(table);
            flameUpArmor(selected[i], grade);
        }

        return 0;
    }

    //영원한 환생의 불꽃
    public int useEternalFlame() {

        int grade = -1; //추가 옵션의 등급
        int selected[]; // 선택된 추가옵션들
        double table[] = {20, 30, 36, 14, 0}; //강환불 추옵 등급 확률 테이블

        selected = selectN(19, 4); //강환불은 4개의 추옵 상승

        for(int i=0; i<selected.length; i++){
            grade = tableRandom(table);
            flameUpArmor(selected[i], grade);
        }

        return 0;
    }

    //방어구, 장신구 추옵 설정
    public void flameUpArmor(int selected, int grade) {
        Map<Integer, int[]> justat_table = new HashMap<>();
        Map<Integer, int[]> mixstat_table = new HashMap<>();
        Map<Integer, int[]> hpmp_table = new HashMap<>();
        Map<Integer, int[]> allstat_table = new HashMap<>(); //공,마,이,점, 올
        Map<Integer, int[]> def_table = new HashMap<>(); //방어력
        Map<Integer, int[]> levdown_table = new HashMap<>();

        //150제 추옵 테이블
        justat_table.put(150, new int[] {24, 32, 40, 48, 56});
        mixstat_table.put(150, new int[] {12, 16, 20, 24, 28});
        hpmp_table.put(150, new int[] {1350, 1800, 2250, 2700, 3150});
        allstat_table.put(150, new int[] {3, 4, 5, 6, 7});
        def_table.put(150, new int[] {24, 32, 40, 48, 56});
        levdown_table.put(150, new int[] {-15, -20, -25, -30, -35});

        int tmp = 0;

        if(selected < 4) { //주스텟
            tmp = Objects.requireNonNull(justat_table.get(levReq))[grade];
            additional.set(selected, tmp);
        }
        else if(selected < 10) { //복합스텟
            int mixstats[][] = {{0, 1}, {0, 2}, {0, 3}, {1, 2}, {1, 3}, {2, 3}}; //복합스텟 조합들
            int stat1 = mixstats[selected-4][0];
            int stat2 = mixstats[selected-4][1];
            tmp = Objects.requireNonNull(mixstat_table.get(levReq))[grade];
            additional.set(stat1, tmp);
            tmp = Objects.requireNonNull(mixstat_table.get(levReq))[grade];
            additional.set(stat2, tmp);
        }
        else if(selected < 12) { //HP, MP
            tmp = Objects.requireNonNull(hpmp_table.get(levReq))[grade];
            additional.set(selected-6, tmp);
        }
        else if(selected == 12) { //착용 레벨 감소
            tmp = Objects.requireNonNull(levdown_table.get(levReq))[grade];
            additional.set(6, tmp);
        }
        else if(selected == 13) { //방어력
            tmp = Objects.requireNonNull(def_table.get(levReq))[grade];
            additional.set(7, tmp);
        }
        else if(selected < 18) { //공격력, 마력, 이동속도, 점프력
            tmp = Objects.requireNonNull(allstat_table.get(levReq))[grade];
            additional.set(selected-6, tmp);
        }
        else if(selected == 18) { //올스텟
            tmp = Objects.requireNonNull(allstat_table.get(levReq))[grade];
            additional.set(12, tmp);
        }

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

    // n개중에 r개 중복 없이 랜덤으로 선택
    public static int[] selectN(int n, int r) {
        int result[] = new int[r];

        // 번호 생성
        for (int i = 0; i < r; i++) {
            result[i] = (int) (Math.random() * n);

            // 중복 존재하면 다시 생성
            for (int j = 0; j < i; j++) {
                if (result[i] == result[j]) {
                    i--;
                    break;
                }
            }
        }

        return result;
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

    public ArrayList getAdditional() { return additional; }

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
