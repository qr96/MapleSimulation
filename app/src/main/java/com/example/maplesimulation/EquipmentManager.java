package com.example.maplesimulation;

import java.util.ArrayList;
import java.util.Arrays;

public class EquipmentManager {
    static public ArrayList<String> armors //방어구 종류
            = new ArrayList<>(Arrays.asList("상의","하의","모자","망토","신발","한벌옷","장갑","파워소스"));

    static public ArrayList<String> accessories //장신구 종류
            = new ArrayList<>(Arrays.asList("얼굴장식","눈장식","벨트","귀고리","어깨장식","반지","펜던트"));

    static public ArrayList<String> weapons
            = new ArrayList<>(Arrays.asList("한손검", "한손도끼", "한손둔기",  "두손검", "두손도끼",
            "두손둔기", "창", "폴암", "데스페라도", "건틀렛리볼버", "튜너", "완드", "스태프", "샤이닝로드",
            "ESP리미터", "매직건틀렛", "활", "석궁", "듀얼보우건", "에인션트보우", "브레스슈터", "단검",
            "블레이드", "아대", "케인", "에너지소드", "체인", "부채", "건", "너클", "핸드캐논", "소울슈터"));

    //방어구인지 여부
    static public boolean isArmor(String type) {
        if(armors.contains(type)) return true;
        else return false;
    }

    //장신구인지 여부
    static public boolean isAccessary(String type) {
        if(accessories.contains(type)) return true;
        else return false;
    }

    //무기인지 여부
    static public boolean isWeapon(String type) {
        if(weapons.contains(type)) return true;
        return false;
    }
}
