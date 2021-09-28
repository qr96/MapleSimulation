package com.example.maplesimulation;

import java.util.ArrayList;

public class StatManager {
    private ArrayList<Equipment> equipped;

    private int[] stats;//장비 종합 능력치들
    // ["STR", "DEX", "INT", "LUK", "최대HP", "최대MP", "착용레벨감소",
    //  "방어력", "공격력", "마력", "이동속도", "점프력", "올스텟%",
    //  "보스데미지%", "데미지%", "최대HP%", "방어율무시%"]

    public StatManager() {

    }

    public String getStats(ArrayList<Equipment> equipped) {
        String result = "";
        this.equipped = equipped;

        initStats();
        calculate();

        result = makeString();

        return result;
    }

    //계산 결과를 문자열로 반환
    public String makeString() {
        String result = "150,000 ~ 150,000\n"+
                stats[4]+"\n"+
                stats[5]+"\n"+
                stats[0]+"\n"+
                stats[1]+"\n"+
                stats[2]+"\n"+
                stats[3]+"\n";

        return result;
    }

    public void calculate() {
        calculateAllEquipment();
        calculateJob();
    }

    public void calculateJob() { //직업별로 정해진 수치대로 계산

    }

    public void calculateAllEquipment() { //모든 장비에 대한 계산
        for(int i=0;i<equipped.size();i++) {
            calculateEquipment(equipped.get(i));
        }
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

