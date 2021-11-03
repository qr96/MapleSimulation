package com.example.maplesimulation;

import java.util.ArrayList;
import java.util.Arrays;

public class StatManager {
    private Character character;
    private ArrayList<Equipment> equipments;

    private int[] stats;//장비 종합 능력치들
    // ["STR", "DEX", "INT", "LUK", "최대HP", "최대MP", "착용레벨감소",
    //  "방어력", "공격력", "마력", "이동속도", "점프력", "올스텟%",
    //  "보스데미지%", "데미지%", "최대HP%", "방어율무시%"]

    public StatManager(Character character) {
        this.character = character;
        this.equipments = character.getEquipped();
    }

    public String getResult() {
        String result = "";
        result = makeString();

        return result;
    }

    //계산 결과를 문자열로 반환
    public String makeString() {
        String result = "";

        result = result + "1000~" + getStatAtk() + "\n";
        result = result + "1000\n";
        result = result + getStat("str") + " (" + character.getStats(character.STR)+"+"+getOnlyAddStat("str")+")\n";
        result = result + getStat("dex") + " (" + character.getStats(character.DEX)+"+"+getOnlyAddStat("dex")+")\n";
        result = result + getStat("int") + " (" + character.getStats(character.INT)+"+"+getOnlyAddStat("int")+")\n";
        result = result + getStat("luk") + " (" + character.getStats(character.LUK)+"+"+getOnlyAddStat("luk")+")\n";
        result = result + getStat("attk") + "\n";

        return result;
    }

    //스공
    public int getStatAtk() {
        double result = 0;
        String job = character.getJob();
        String weapon = equipments.get(7).getType();

        result = (getStat(getJustat(job))*4 + (double) getStat(getSubStat(job)) / 100) *
                getStat(getAttkType(job)) *
                ((double) (100 + getStat("dmg")) / 100) *
                ((100+(double) getStat("fdmg"))/100) *
                ((100+(double) getStat("attkper"))/100) *
                getJobConstant(job) * getWeaponConstant(weapon);

        return (int) result;
    }

    //주스탯
    public String getJustat(String job) {
        String[] type1 = {"히어로", "팔라딘", "다크나이트", "소울마스터", "미하일", "아란", "블래스터",
                "데몬슬레이어", "카이저", "제로", "아델", "하야토", "핑크빈"};
        String[] type2 = {"보우마스터", "신궁", "패스파인더", "윈드브레이커", "메르세데스", "와일드헌터", "카인"};
        String[] type3 = {"아크메이지(불,독)", "아크메이지(썬,콜)", "비숍", "플레임위자드", "에반",
                "루미너스", "배틀메이지", "키네시스", "일리움", "라라", "칸나", "비스트테이머"};
        String[] type4 = {"나이트로드", "섀도어", "듀얼블레이드", "나이트워커", "팬텀", "카데나", "호영"};

        if(Arrays.asList(type1).contains(job)) return "str";
        else if(Arrays.asList(type2).contains(job)) return "dex";
        else if(Arrays.asList(type3).contains(job)) return "int";
        else if(Arrays.asList(type4).contains(job)) return "luk";
        else return "str";
    }
    
    //부스탯
    public String getSubStat(String job) {
        if(getJustat(job).equals("str")) return "dex";
        else if(getJustat(job).equals("dex")) return "str";
        else if(getJustat(job).equals("int")) return "luk";
        else if(getJustat(job).equals("luk")) return "dex";
        else return "dex";
    }

    public String getAttkType(String job) {
        if(getJustat(job).equals("int")) return "magic";
        else return "attk";
    }

    //직업상수
    public double getJobConstant(String job) {
        if(job.equals("제논")) return 0.875;
        else if(job.equals("아크메이지(불,독)")) return 1.2;
        else if(job.equals("아크메이지(썬,콜)")) return 1.2;
        else if(job.equals("플레임위자드")) return 1.2;
        else return 1.0;
    }

    //무기상수
    public double getWeaponConstant(String weapon) {
        String[] type1 = {"한손검", "한손도끼", "한손둔기", "스태프", "완드", "샤이닝로드", "ESP리미터", "매직건틀렛"}; //1.2
        String[] type2 = {"단검", "블레이드", "케인", "데스페라도", "체인", "부채", "튜너", "브레스슈터", "활", "듀얼보우건", "에인션트보우"}; //1.3
        String[] type3 = {"에너지소드", "건", "핸드캐논"}; //1.5
        String[] type4 = {"소울슈터", "건틀렛리볼버", "너클"};//1.7
        String[] type5 = {"두손검", "두손도끼", "두손둔기", "태도"}; //1.34
        String[] type6 = {"창", "폴암 ", "대검"}; //1.49
        String[] type7 = {"석궁"}; //1.35
        String[] type8 = {"아대"}; //1.75

        if(Arrays.asList(type1).contains(weapon)) return 1.2;
        else if(Arrays.asList(type2).contains(weapon)) return 1.3;
        else if(Arrays.asList(type3).contains(weapon)) return 1.5;
        else if(Arrays.asList(type4).contains(weapon)) return 1.7;
        else if(Arrays.asList(type5).contains(weapon)) return 1.34;
        else if(Arrays.asList(type6).contains(weapon)) return 1.49;
        else if(Arrays.asList(type7).contains(weapon)) return 1.35;
        else if(Arrays.asList(type8).contains(weapon)) return 1.75;

        return 1.0;
    }

    //최종 스탯
    public int getStat(String option) {
        int sum = 0;

        sum = getAddStat(option) + getAddStat2(option);

        return sum;
    }

    //직업 올린 능력치 제외
    public int getOnlyAddStat(String option) {
        int result = 0;

        result = getStat(option);
        if(option.equals("str")) result -= character.getStats(character.STR);
        else if(option.equals("dex")) result -= character.getStats(character.DEX);
        else if(option.equals("int")) result -= character.getStats(character.INT);
        else if(option.equals("luk")) result -= character.getStats(character.LUK);
        else if(option.equals("hp")) result -= character.getStats(character.HP);

        return result;
    }

    //스탯
    public int getAddStat(String option) {
        int add = 0;
        int equipStat = 0;
        int charStat = 0;

        if(option.equals("str")) {
            equipStat = 0;
            charStat = 0;
        }
        else if(option.equals("dex")) {
            equipStat = 1;
            charStat = 1;
        }
        else if(option.equals("int")) {
            equipStat = 2;
            charStat = 2;
        }
        else if(option.equals("luk")) {
            equipStat = 3;
            charStat = 3;
        }
        else if(option.equals("hp")) {
            equipStat = 4;
            charStat = 4;
        }
        else if(option.equals("dmg")) {
            equipStat = 14;
            charStat = 8;
        }
        else if(option.equals("attk")) {
            equipStat = 8;
            charStat = 10;
        }
        else if(option.equals("magic")) {
            equipStat = 9;
            charStat = 11;
        }
        else if(option.equals("fdmg")) { //최종뎀
            equipStat = - 1;
            charStat = 12;
        }
        else if(option.equals("attkper")) { //공격력퍼
            equipStat = -1;
            charStat = 13;
        }
        else if(option.equals("magicper")) { //마력퍼
            equipStat = -1;
            charStat = 14;
        }

        //장비 스탯
        if(equipStat != -1) {
            for(int i=0; i<equipments.size(); i++) {
                if(equipments.get(i).getName().equals("잘못된 이름")) continue;
                add += (int) equipments.get(i).getStats().get(equipStat);
                add += (int) equipments.get(i).getStarStat().get(equipStat);
                add += (int) equipments.get(i).getEnhance().get(equipStat);
                add += (int) equipments.get(i).getAdditional().get(equipStat);
            }
        }

        //주스탯
        add += character.getStats(charStat);

        //스킬 스탯
        add += character.getSkillStats(charStat);

        //스탯퍼 적용
        add = add * (100+getStatPer(option))/100;

        return add;
    }

    //스탯퍼 적용 안되는 추가 스탯 (아케인, 하이퍼, 몬라, 어빌, 유니온)
    public int getAddStat2(String option) {
        int charStat = 0;
        int result = 0;

        if(option.equals("str")) charStat = 0;
        else if(option.equals("dex")) charStat = 1;
        else if(option.equals("int")) charStat = 2;
        else if(option.equals("luk")) charStat = 3;
        else if(option.equals("hp")) charStat = 4;
        else return 0;

        //하이퍼 스탯
        result += character.getHyperStats(charStat);

        return result;
    }

    //장비들 스탯퍼 추출
    public int getStatPer(String option) {
        int possibility = 0;
        String[] potential1;
        String[] potential2;

        //잠재옵션 스탯퍼
        for(int i=0; i<equipments.size(); i++) {
            potential1 = equipments.get(i).getPotential1();
            potential2 = equipments.get(i).getPotential2();
            for(int j=0;j<3;j++) {
                if(getCubeOptionType(potential1[j]).equals(option))
                    possibility += getCubePossible(potential1[j]);
                if(getCubeOptionType(potential2[j]).equals(option))
                possibility += getCubePossible(potential2[j]);
            }

            //추가옵션 (올스탯퍼)
            possibility += (int) equipments.get(i).getAdditional().get(12);
        }

        return possibility;
    }

    //큐브 옵션 판별
    public String getCubeOptionType(String option) {
        if(option.contains("STR")) return "str";
        else if(option.contains("DEX")) return "dex";
        else if(option.contains("INT")) return "int";
        else if(option.contains("LUK")) return "luk";
        else if(option.contains("HP")) return "hp";
        else if(option.contains("공격력")) return "attk";
        else if(option.contains("마력")) return "magic";
        else return "err";
    }

    //잠재옵션에서 스탯퍼 추출
    public int getCubePossible(String option) {
        if(!option.contains("%")) return 0;
        return Integer.parseInt(option.substring(option.indexOf("+"), option.indexOf("%")));
    }

    //잠재옵션에서 스탯 추출 {
    public int getCubeStat(String option) {
        if(option.contains("%")) return 0;
        return Integer.parseInt(option.substring(option.indexOf("+")));
    }

    public void calculateEquipment(Equipment equipment) { //특정 장비 계산
        if(equipment.getName().equals("잘못된 이름") || equipment.getName().equals("")) return;

        for(int i=0;i<equipment.getStats().size();i++) {
            stats[i] += (int) equipment.getStats().get(i);
        }
        for(int i=0;i<21;i++) {
            stats[i] += (int) equipment.getStarStat().get(i);
            stats[i] += (int) equipment.getAdditional().get(i);
            stats[i] += (int) equipment.getEnhance().get(i);
        }
    }

    //능력치 0으로 초기화
    public void initStats() {
        this.stats = new int[21];
    }
}

