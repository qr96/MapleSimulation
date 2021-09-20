package com.example.maplesimulation;

import java.util.ArrayList;

public class StatManager {
    private ArrayList<Equipment> equipped;

    private ArrayList<Integer> stats;//장비 종합 능력치들
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
                stats.get(4)+"\n"+
                stats.get(5)+"\n"+
                stats.get(0)+"\n"+
                stats.get(1)+"\n"+
                stats.get(2)+"\n"+
                stats.get(3)+"\n";

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
        if(equipment.getName().equals("잘못된 이름")) return;

        int tmp = 0;
        for(int i=0;i<21;i++) {
            tmp = stats.get(i);
            tmp += (int) equipment.getStats().get(i);
            tmp += (int) equipment.getStarStat().get(i);
            tmp += (int) equipment.getAdditional().get(i);
            tmp += (int) equipment.getEnhance().get(i);
            stats.set(i, tmp);
        }
    }

    //능력치 0으로 초기화
    public void initStats() {
        stats.clear();
        for(int i=0;i<stats.size();i++) {
            stats.add(0);
        }
    }
}
