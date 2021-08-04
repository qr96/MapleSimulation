package com.example.maplesimulation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Equipment implements Cloneable, Serializable {
    public ArrayList<String> armors //방어구 종류
            = new ArrayList<>(Arrays.asList("상의","하의","모자","망토","신발","한벌옷","장갑","파워소스"));

    public ArrayList<String> accessories //장신구 종류
            = new ArrayList<>(Arrays.asList("얼굴장식","눈장식","벨트","귀고리","어깨장식","반지","펜던트"));

    public ArrayList<String> weapons
            = new ArrayList<>(Arrays.asList("한손검", "한손도끼", "한손둔기",  "두손검", "두손도끼",
                "두손둔기", "창", "폴암", "데스브링어", "건틀렛리볼버", "튜너", "완드", "스태프", "샤이닝로드",
                "ESP리미터", "매직건틀렛", "활", "석궁", "듀얼보우건", "에인션트보우", "브레스슈터", "단검",
                "블레이드", "아대", "케인", "에너지소드", "체인", "부채", "건", "너클", "핸드캐논", "소울슈터"));

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

    private String potential1[]; //윗잠재
    private String potential2[]; //아랫잠재

    int potentialGrade1; // 잠재 등급
    int potentialGrade2; // 아랫잠재 등급

    // 능력치 순서 {"STR", "DEX", "INT", "LUK", "최대HP", "최대MP", "착용레벨감소",
    //                "방어력", "공격력", "마력", "이동속도", "점프력", "올스텟%",
    //                "보스데미지%", "데미지%", "최대HP%", "방어율무시%"};

    //기본 능력치들
    private ArrayList stats;

    //강화된 수치
    private ArrayList<Integer> enhance;

    //추가 옵션
    private ArrayList<Integer> additional;

    //스타포스로 강화된 능력치
    private ArrayList<Integer> starStat;


    public Equipment() {
        Image = "";
        stats = new ArrayList();
        enhance = new ArrayList<>();
        additional = new ArrayList<>();
        starStat = new ArrayList<>();
        initEnhance();
        initAdditional();
        initStarStat();
        goldHammer = 0;
        potential1 = new String[]{"잠재능력을 재설정 해주세요.", "", ""};
        potential2 = new String[]{"에디셔널 잠재능력을 재설정 해주세요.", "", ""};
        potentialGrade1 = 0;
        potentialGrade2 = 0;
    }

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

    //방어구인지 여부
    public boolean isArmor() {
        if(armors.contains(this.type)) return true;
        else return false;
    }

    //장신구인지 여부
    public boolean isAccessary() {
        if(accessories.contains(this.type)) return true;
        else return false;
    }

    //무기인지 여부
    public boolean isWeapon() {
        if(weapons.contains(this.type)) return true;
        return false;
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
        String table[] = {"레어", "에픽", "유니크", "레전더리"};
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
