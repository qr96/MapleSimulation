package com.example.maplesimulation;

import java.util.ArrayList;

public class EquipmentInfo {
    //장비의 정보 텍스트로 변환
    public static String makeText(Equipment equipment) {
        String equipInfo = "";
        String[] table = {"STR", "DEX", "INT", "LUK", "최대HP", "최대MP", "착용레벨감소",
                "방어력", "공격력", "마력", "이동속도", "점프력", "올스텟%",
                "보스데미지%", "데미지%", "최대HP%", "방어율무시%"};  //12~16 %붙음

        ArrayList<Integer> equipStats = equipment.getStats();
        ArrayList<Integer> equipEnhance = equipment.getEnhance();
        ArrayList<Integer> equipAdditional = equipment.getAdditional(); //추가옵션
        ArrayList<Integer> starStat = equipment.getStarStat(); //스타포스 수치

        int sum = 0;

        for(int i=0; i<table.length; i++){
            sum = equipStats.get(i) + equipAdditional.get(i) + equipEnhance.get(i) + starStat.get(i);

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

                if((Integer)equipEnhance.get(i)+(Integer) starStat.get(i) > 0) { //강화 수치
                    equipInfo = equipInfo + "<font color=\"#99CCFF\"> +" + ((Integer)equipEnhance.get(i)+(Integer) starStat.get(i));
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

    public static String starText(Equipment equipment) {
        int star = equipment.getStar();
        int maxStar = equipment.getMaxStar();
        String text = "";
        int count = 0;

        if(equipment.isNoljang) { //놀장 바른 경우
            text = "<font color=\"#33CCFF\">";
            for(int i=0; i<star; i++){
                if(count==15) text = text+"<br>";
                else if(count%5==0) text = text+" ";
                text = text+"★";
                count++;
            }
            text = text+"</font>";
        }
        else {
            text = "<font color=\"#FFC107\">";
            for(int i=0; i<star; i++){
                if(count==15) text = text+"<br>";
                else if(count%5==0) text = text+" ";
                text = text+"★";
                count++;
            }
            text = text+"</font>";
        }

        text = text+"<font color=\"#CCCCCC\">";
        for(int i=0; i<maxStar-star; i++){
            if(count==15) text = text+"<br>";
            else if(count%5==0) text = text+" ";
            text = text+"★";
            count++;
        }
        text = text+"</font>";

        return text;
    }

    public static String potential(Equipment equipment) {
        String potential1[] = equipment.getPotential1();
        String potential2[] = equipment.getPotential2();
        String info = "";

        switch (equipment.getPotentialGrade1()) {
            case "레어":
                info = info + "<font color=\"#00CCFF\">" +"R ";
                break;
            case "에픽":
                info =  info + "<font color=\"#CC33CC\">" + "E ";
                break;
            case "유니크":
                info = info + "<font color=\"#FFFF33\">" + "U ";
                break;
            case "레전드리":
                info = info + "<font color=\"#33FF00\">" + "L ";
                break;
            default:
                break;
        }
        info = info + "잠재옵션</font><br>";

        for(int i=0; i<potential1.length; i++){
            info = info + potential1[i] + "<br>";
        }
        info = info + "<br>";
        switch (equipment.getPotentialGrade2()) {
            case "레어":
                info = info + "<font color=\"#00CCFF\">" +"R ";
                break;
            case "에픽":
                info =  info + "<font color=\"#CC33CC\">" + "E ";
                break;
            case "유니크":
                info = info + "<font color=\"#FFFF33\">" + "U ";
                break;
            case "레전드리":
                info = info + "<font color=\"#33FF00\">" + "L ";
                break;
            default:
                break;
        }
        info = info + "에디셔널 잠재옵션</font><br>";
        for(int i=0; i<potential2.length; i++){
            info = info + potential2[i] + "<br>";
        }

        return info;
    }

    public static String potential1(Equipment equipment) {
        String potential1[] = equipment.getPotential1();
        String info = "";

        switch (equipment.getPotentialGrade1()) {
            case "레어":
                info = info + "<font color=\"#00CCFF\">" +"R ";
                break;
            case "에픽":
                info =  info + "<font color=\"#CC33CC\">" + "E ";
                break;
            case "유니크":
                info = info + "<font color=\"#FFFF33\">" + "U ";
                break;
            case "레전드리":
                info = info + "<font color=\"#33FF00\">" + "L ";
                break;
            default:
                break;
        }
        info = info + "잠재옵션</font><br>";

        for(int i=0; i<potential1.length; i++){
            info = info + potential1[i];
            if(i+1<potential1.length) info = info + "<br>";
        }

        return info;
    }

    public static String potential2(Equipment equipment) {
        String potential[] = equipment.getPotential2();
        String info = "";

        switch (equipment.getPotentialGrade2()) {
            case "레어":
                info = info + "<font color=\"#00CCFF\">" +"R ";
                break;
            case "에픽":
                info =  info + "<font color=\"#CC33CC\">" + "E ";
                break;
            case "유니크":
                info = info + "<font color=\"#FFFF33\">" + "U ";
                break;
            case "레전드리":
                info = info + "<font color=\"#33FF00\">" + "L ";
                break;
            default:
                break;
        }
        info = info + "잠재옵션</font><br>";

        for(int i=0; i<potential.length; i++){
            info = info + potential[i];
            if(i+1<potential.length) info = info + "<br>";
        }

        return info;
    }
}
