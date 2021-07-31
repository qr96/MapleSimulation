package com.example.maplesimulation;

import java.util.ArrayList;

public class EquipmentInfo {
    //장비의 정보 텍스트로 변환
    public static String makeText(Equipment equipment) {
        String equipInfo = "";
        String[] table = {"STR", "DEX", "INT", "LUK", "최대HP", "최대MP", "착용레벨감소",
                "방어력", "공격력", "마력", "이동속도", "점프력", "올스텟%",
                "보스데미지%", "데미지%", "최대HP%", "방어율무시%"};  //12~16 %붙음

        ArrayList equipStats = equipment.getStats();
        ArrayList equipEnhance = equipment.getEnhance();
        ArrayList equipAdditional = equipment.getAdditional(); //추가옵션

        int sum = 0;

        for(int i=0; i<table.length; i++){
            sum = (Integer) equipStats.get(i) + (Integer)equipAdditional.get(i) + (Integer) equipEnhance.get(i);
            if(sum == 0 || i==6) continue;
            equipInfo = equipInfo + table[i] + " : " + "+" + sum;

            if(i>=12 && i<=16) equipInfo = equipInfo + "%"; //%붙은 스텟

            if(sum != (Integer) equipStats.get(i)){ //추가옵션이나 강화 수치가 있는 경우
                equipInfo = equipInfo + " (" + equipStats.get(i);
                if(i>=12 && i<=16) equipInfo = equipInfo + "%"; //%붙은 스텟

                if((Integer)equipAdditional.get(i) > 0) { //추가 옵션
                    equipInfo = equipInfo + "<font color=\"#66FF66\"> +" + equipAdditional.get(i);
                    if(i>=12 && i<=16) equipInfo = equipInfo + "%"; //%붙은 스텟
                    equipInfo = equipInfo + "</font>";
                }

                if((Integer)equipEnhance.get(i) > 0) { //강화 수치
                    equipInfo = equipInfo + "<font color=\"#99CCFF\"> +" + equipEnhance.get(i);
                    equipInfo = equipInfo + "</font>";
                }
                equipInfo = equipInfo + ")";
            }
            equipInfo = equipInfo + "<br>";
        }

        if((Integer) equipAdditional.get(6) !=0 )
            equipInfo = equipInfo + "착용 레벨 감소 : <font color=\"#66FF66\">" + equipAdditional.get(6) + "</font><br>";
        equipInfo = equipInfo + "업그레이드 가능 횟수 : "
                + (equipment.getMaxUp()-equipment.getNowUp()-equipment.getFailUp())
                + "<br>(복구 가능 횟수 : " + equipment.getFailUp() + ")";

        if(equipment.getGoldHammer() == 1) equipInfo = equipInfo + "<br>황금망치 제련 적용";

        return equipInfo;
    }
}
