package com.example.maplesimulation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Equipment implements Cloneable, Serializable {
    private int id; //id가 -1이면 더미 객체
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

    private String potential1[]; //윗잠재
    private String potential2[]; //아랫잠재

    int potentialGrade1; // 잠재 등급
    int potentialGrade2; // 아랫잠재 등급

    // ["STR", "DEX", "INT", "LUK", "최대HP", "최대MP", "착용레벨감소",
    //  "방어력", "공격력", "마력", "이동속도", "점프력", "올스텟%",
    //  "보스데미지%", "데미지%", "최대HP%", "방어율무시%"]

    private ArrayList<Integer> stats;//기본 능력치들
    private ArrayList<Integer> enhance;//강화된 수치
    private ArrayList<Integer> additional;//추가 옵션
    private ArrayList<Integer> starStat;//스타포스로 강화된 능력치

    public int used_juheun;
    public int used_sunback;
    public int used_goldhammer;
    public int used_returnscroll;
    public int used_innocent;
    public int used_chaos;
    public int used_eternal;
    public int used_powerful;
    public int used_noljang;
    public int used_protect_shield;
    public int used_blackcube;
    public int used_redcube;
    public int used_addicube;
    public int used_meso;
    public int used_destroy;

    public boolean isNoljang; //놀장 인지 여부
    public boolean isStarforce; //스타포스 작인지 여부

    public Equipment() {
        stats = new ArrayList();
        enhance = new ArrayList<Integer>();
        additional = new ArrayList<Integer>();
        starStat = new ArrayList<Integer>();
        initEnhance();
        initAdditional();
        initStarStat();
        initUsed();
        goldHammer = 0;
        potential1 = new String[]{"잠재능력을 재설정 해주세요.", "", ""};
        potential2 = new String[]{"에디셔널 잠재능력을 재설정 해주세요.", "", ""};
        potentialGrade1 = 0;
        potentialGrade2 = 0;
        id = -1; //더미 객체
        name = "잘못된 이름";
        Image = "";
        type = "잘못된 장비";
        levReq = 0;
        maxUp = 0;
        nowUp = 0;
        failUp = 0;
        maxStar = 0;
        star = 0;
        goldHammer = 0;
        isNoljang = false;
        isStarforce = false;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    //강화 수치 증가
    public void addEnhanceStat(int index, int num) {
        int tmp = enhance.get(index);
        tmp = tmp + num;
        enhance.set(index, tmp);
    }

    //강화 수치 감소
    public void subEnhanceStat(int index, int num) {
        int tmp = enhance.get(index);
        tmp = tmp - num;
        enhance.set(index, tmp);
    }

    //추옵 값 설정
    public void setAdditionStat(int index, int num) {
        additional.set(index, num);
    }

    //강화 한번 성공
    public void successScroll() {
        this.nowUp++;
    }

    //주문서 사용 횟수 복구
    public void recoverySuccess() {
        this.nowUp--;
    }

    //강화 실패
    public void failedScroll() {
        this.failUp++;
    }

    //강화 실패 복구
    public void recoveryFailed() {
        this.failUp--;
    }

    //강화가 끝났으면 true 반환
    public boolean isFinishEnchant() {
        if(nowUp+failUp == maxUp) return true;
        else return false;
    }

    //추가 옵션 초기화
    public void initAdditional() {
        additional.clear();
        for(int i=0; i<21; i++) additional.add(0);
    }
    
    //강화 초기화
    public void initEnhance() {
        enhance.clear();
        for(int i=0; i<21; i++) enhance.add(0);
    }

    //스타포스 초기화
    public void initStarStat() {
        star = 0;
        starStat.clear();
        for(int i=0; i<21; i++) starStat.add(0);
    }

    public void initUsed() {
        used_juheun = 0;
        used_sunback = 0;
        used_goldhammer = 0;
        used_returnscroll = 0;
        used_innocent = 0;
        used_chaos = 0;
        used_eternal =0 ;
        used_powerful =0 ;
        used_blackcube= 0;
        used_redcube = 0;
        used_addicube= 0;
        used_meso = 0;
        used_noljang = 0;
        used_destroy = 0;
    }

    //방어구인지 여부
    public boolean isArmor() {
        return EquipmentManager.isArmor(this.type);
    }

    //장신구인지 여부
    public boolean isAccessary() {
        return EquipmentManager.isAccessary(this.type);
    }

    //무기인지 여부
    public boolean isWeapon() {
        return EquipmentManager.isWeapon(this.type);
    }
    
    //황금망치 사용
    public void useGoldhammer(){
        this.goldHammer = 1;
        this.maxUp++;
    }
    
    //이노센트 주문서 사용
    public void useInnocent() {
        if(goldHammer>0) maxUp--;
        nowUp=0;
        goldHammer=0;
        failUp=0;
        initEnhance();
        initStarStat();
    }

    //추가옵션이 몇 급인지 반환
    public int calculateAdditional(String justat) {
        int sum = 0;

        if(justat == "INT") {
            sum = additional.get(2);
            sum = sum+additional.get(9)*4;
            sum = sum+additional.get(12)*10;

            return sum;
        }

        if(justat == "STR") sum = additional.get(0);
        else if(justat == "DEX") sum = additional.get(1);
        else if(justat == "LUK") sum = additional.get(3);

        sum = sum+additional.get(8)*4;
        sum = sum+additional.get(12)*10;

        return sum;
    }

    public void potentialGradeUp1() {
        potentialGrade1++;
    }

    public void potentialGradeUp2() {
        potentialGrade2++;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        if(Image == null) this.Image = "";
        return Image;
    }

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

    public String getPotentialGrade1() {
        String table[] = {"레어", "에픽", "유니크", "레전드리"};
        return table[potentialGrade1];
    }

    public String getPotentialGrade2() {
        String table[] = {"레어", "에픽", "유니크", "레전드리"};
        return table[potentialGrade2];
    }

    public String[] getPotential1() { return this.potential1; }

    public String[] getPotential2() { return potential2; }

    public int getMaxStar() { return maxStar; }

    public int getStar() { return star; }

    public void upStar() {this.star++; } //스타포스 수치 업

    public ArrayList getStarStat() { return this.starStat; }

    public void downStar() {this.star--; }

    public void upgradeStarStat(int index, int num) { //스타포스 능력치 강화
        int tmp = this.starStat.get(index);
        tmp += num;
        this.starStat.set(index, tmp);
    }

    public void downStarStat(int index, int num) {
        int tmp = this.starStat.get(index);
        tmp -= num;
        this.starStat.set(index, tmp);
    }

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
    }

    public void setPotential1(String[] potential) {
        this.potential1[0] = potential[0];
        this.potential1[1] = potential[1];
        this.potential1[2] = potential[2];
    }

    public void setPotential2(String[] potential) {
        this.potential2[0] = potential[0];
        this.potential2[1] = potential[1];
        this.potential2[2] = potential[2];
    }
}
