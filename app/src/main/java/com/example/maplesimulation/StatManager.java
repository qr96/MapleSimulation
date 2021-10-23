package com.example.maplesimulation;

import java.util.ArrayList;

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

        result = result + "1000~1000\n";
        result = result + "1000\n";
        result = result + "1000\n";
        result = result + getStat("str") + " (" + character.getStats(character.STR)+"+"+getAddStat("str")+")\n";
        result = result + getStat("dex") + " (" + character.getStats(character.DEX)+"+"+getAddStat("dex")+")\n";
        result = result + getStat("int") + " (" + character.getStats(character.INT)+"+"+getAddStat("int")+")\n";
        result = result + getStat("luk") + " (" + character.getStats(character.LUK)+"+"+getAddStat("luk")+")\n";

        return result;
    }

    public int getStat(String option) {
        int sum = 0;

        if(option.equals("str")) sum = character.getStats(character.STR) + getAddStat(option);
        else if(option.equals("dex")) sum = character.getStats(character.DEX) + getAddStat(option);
        else if(option.equals("int")) sum = character.getStats(character.INT) + getAddStat(option);
        else if(option.equals("luk")) sum = character.getStats(character.LUK) + getAddStat(option);
        else if(option.equals("hp")) sum = character.getStats(character.HP) + getAddStat(option);
        else if(option.equals("dmg")) sum = character.getStats(character.DMG) + getAddStat(option);

        return sum;
    }

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

        //장비 스탯
        for(int i=0; i<equipments.size(); i++) {
            if(equipments.get(i).getName().equals("잘못된 이름")) continue;
            add += (int) equipments.get(i).getStats().get(equipStat);
            add += (int) equipments.get(i).getStarStat().get(equipStat);
            add += (int) equipments.get(i).getEnhance().get(equipStat);
            add += (int) equipments.get(i).getAdditional().get(equipStat);
        }

        //하이퍼스탯
        add += character.getHyperStats(charStat);
        
        //스킬 스탯
        add += character.getSkillStats(charStat);

        return add;
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

