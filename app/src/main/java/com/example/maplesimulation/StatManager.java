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

        result += getSTR() + "\n";

        return result;
    }

    public String getSTR() {
        String result = "";
        int sum = 0;
        int add = 0;
        
        for(int i=0; i<equipments.size(); i++) {
            if(equipments.get(i).getName().equals("잘못된 이름")) continue;
            add += (int) equipments.get(i).getStats().get(0);
            add += (int) equipments.get(i).getStarStat().get(0);
            add += (int) equipments.get(i).getEnhance().get(0);
            add += (int) equipments.get(i).getAdditional().get(0);
        }

        sum = character.getStats(character.STR) + add;

        result = sum + " (" + character.getStats(character.STR) + "+" + add + ")";

        return result;
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

