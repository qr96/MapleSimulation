package com.example.maplesimulation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

//추가옵션 테이블
public class AdditionalTable implements Serializable {
    public Map<String, int[]> weaponTable = new HashMap<>(); //무기 추옵 테이블  키:부위+레벨 ex)부채150

    public Map<Integer, int[]> justat_table = new HashMap<>(); //단일스텟, 방어력
    public Map<Integer, int[]> mixstat_table = new HashMap<>(); //이중스텟
    public Map<Integer, int[]> hpmp_table = new HashMap<>(); //HP, MP
    public Map<Integer, int[]> allstat_table = new HashMap<>(); //공격력,마력,이속,점프,올스텟,데미지
    public Map<Integer, int[]> levdown_table = new HashMap<>(); //착용 레벨 감소
    public Map<Integer, int[]> bossdmg_table = new HashMap<>(); //보스 공격 데미지

    public AdditionalTable() {
        initOptionTable();
        initWeaponOptionTable();
    }

    public void initOptionTable() {
        //150제 추옵 테이블
        justat_table.put(150, new int[] {24, 32, 40, 48, 56});
        mixstat_table.put(150, new int[] {12, 16, 20, 24, 28});
        hpmp_table.put(150, new int[] {1350, 1800, 2250, 2700, 3150});
        allstat_table.put(150, new int[] {3, 4, 5, 6, 7});
        levdown_table.put(150, new int[] {-15, -20, -25, -30, -35});
        bossdmg_table.put(150, new int[] {6, 8, 10, 12, 14});
    }

    public void initWeaponOptionTable() {
        weaponTable.put("아대150", new int[] {11, 16, 21, 28, 36});
        weaponTable.put("건150", new int[] {15, 22, 31, 40, 52});
        weaponTable.put("건틀렛리볼버150", new int[] {16, 23, 31, 41, 53});
        weaponTable.put("너클150", new int[] {16, 23, 31, 41, 53});
        weaponTable.put("소울슈터150", new int[] {16, 23, 31, 41, 53});
        weaponTable.put("에너지소드150", new int[] {16, 23, 31, 41, 53});
        weaponTable.put("폴암150", new int[] {19, 27, 38, 49, 63});
        weaponTable.put("단검150", new int[] {20, 29, 39, 52, 66});
        weaponTable.put("듀얼보우건150", new int[] {20, 29, 39, 52, 66});
        weaponTable.put("부채150", new int[] {20, 29, 39, 52, 66});
        weaponTable.put("브레스슈터150", new int[] {20, 29, 39, 52, 66});
        weaponTable.put("에이션트보우150", new int[] {20, 29, 39, 52, 66});
        weaponTable.put("체인150", new int[] {20, 29, 39, 52, 66});
        weaponTable.put("활150", new int[] {20, 29, 39, 52, 66});
        weaponTable.put("석궁150", new int[] {20, 29, 40, 53, 68});
        weaponTable.put("케인150", new int[] {20, 29, 40, 53, 68});
        weaponTable.put("한손검150", new int[] {20, 29, 40, 53, 68});
        weaponTable.put("한손도끼150", new int[] {20, 29, 40, 53, 68});
        weaponTable.put("한손둔기150", new int[] {20, 29, 40, 53, 68});
        weaponTable.put("데스페라도150", new int[] {21, 31, 42, 55, 71});
        weaponTable.put("두손검150", new int[] {21, 31, 42, 55, 71});
        weaponTable.put("두손도끼150", new int[] {21, 31, 42, 55, 71});
        weaponTable.put("두손둔기150", new int[] {21, 31, 42, 55, 71});
        weaponTable.put("창150", new int[] {21, 31, 42, 55, 71});
        weaponTable.put("튜너150", new int[] {21, 31, 42, 55, 71});
        weaponTable.put("핸드캐논150", new int[] {21, 31, 42, 55, 71});
        weaponTable.put("매직건틀렛150", new int[] {25, 36, 49, 65, 83});
        weaponTable.put("샤이닝로드150", new int[] {25, 36, 49, 65, 83});
        weaponTable.put("완드150", new int[] {25, 36, 49, 65, 83});
        weaponTable.put("ESP리미터150", new int[] {25, 36, 49, 65, 83});
        weaponTable.put("스태프150", new int[] {25, 36, 50, 66, 84});
        weaponTable.put("해방된카이세리움150", new int[] {16, 36, 59, 86, 118});
    }
}
