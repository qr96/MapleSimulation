package com.example.maplesimulation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

//추가옵션 테이블
public class FlameTable implements Serializable {
    public Map<String, int[]> weaponTable = new HashMap<>(); //무기 공격력 추옵 테이블  키:부위+레벨 ex)부채150

    public Map<Integer, int[]> justat_table = new HashMap<>(); //단일스텟, 방어력
    public Map<Integer, int[]> mixstat_table = new HashMap<>(); //이중스텟
    public Map<Integer, int[]> hpmp_table = new HashMap<>(); //HP, MP
    public Map<Integer, int[]> allstat_table = new HashMap<>(); //공격력,마력,이속,점프,올스텟,데미지
    public Map<Integer, int[]> levdown_table = new HashMap<>(); //착용 레벨 감소
    public Map<Integer, int[]> bossdmg_table = new HashMap<>(); //보스 공격 데미지

    public FlameTable() {
        initOptionTable();
        initWeaponOptionTable();
    }

    public void initOptionTable() {
        //100제 추옵 테이블
        justat_table.put(100, new int[] {18, 24, 30, 36, 42});
        mixstat_table.put(100, new int[] {9, 12, 15, 18, 21});
        hpmp_table.put(100, new int[] {900, 1200, 1500, 1800, 2150});
        allstat_table.put(100, new int[] {3, 4, 5, 6, 7});
        levdown_table.put(100, new int[] {-15, -20, -25, -30, -35});
        bossdmg_table.put(100, new int[] {6, 8, 10, 12, 14});

        //150제 추옵 테이블
        justat_table.put(150, new int[] {24, 32, 40, 48, 56});
        mixstat_table.put(150, new int[] {12, 16, 20, 24, 28});
        hpmp_table.put(150, new int[] {1350, 1800, 2250, 2700, 3150});
        allstat_table.put(150, new int[] {3, 4, 5, 6, 7});
        levdown_table.put(150, new int[] {-15, -20, -25, -30, -35});
        bossdmg_table.put(150, new int[] {6, 8, 10, 12, 14});

        //160제 추옵 테이블
        justat_table.put(160, new int[] {26, 36, 45, 54, 64});
        mixstat_table.put(160, new int[] {15, 20, 25, 30, 35});
        hpmp_table.put(160, new int[] {1440, 1920, 2400, 2880, 3360});
        allstat_table.put(160, new int[] {3, 4, 5, 6, 7});
        levdown_table.put(160, new int[] {-15, -20, -25, -30, -35});
        bossdmg_table.put(160, new int[] {6, 8, 10, 12, 14});

        //200제 추옵 테이블
        justat_table.put(200, new int[] {33, 44, 55, 66, 77});
        mixstat_table.put(200, new int[] {18, 24, 30, 36, 42});
        hpmp_table.put(200, new int[] {1800, 2400, 3000, 3600, 4200});
        allstat_table.put(200, new int[] {3, 4, 5, 6, 7});
        levdown_table.put(200, new int[] {-15, -20, -25, -30, -35});
        bossdmg_table.put(200, new int[] {6, 8, 10, 12, 14});
    }

    public void initWeaponOptionTable() {
        //150제 무기들
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

        //160제 무기들
        weaponTable.put("아대160", new int[] {16, 23, 32, 42, 53});
        weaponTable.put("건160", new int[] {23, 33, 46, 60, 77});
        weaponTable.put("에너지소드160", new int[] {24, 34, 47, 62, 79});
        weaponTable.put("너클160", new int[] {24, 34, 47, 62, 79});
        weaponTable.put("건틀렛 리볼버160", new int[] {24, 34, 47, 62, 79});
        weaponTable.put("소울슈터160", new int[] {24, 34, 47, 62, 79});
        weaponTable.put("폴암160", new int[] {28, 51, 56, 74, 95});
        weaponTable.put("에인션트 보우160", new int[] {29, 43, 59, 77, 99});
        weaponTable.put("부채160", new int[] {29, 43, 59, 77, 99});
        weaponTable.put("활160", new int[] {29, 43, 59, 77, 99});
        weaponTable.put("브레스 슈터160", new int[] {29, 43, 59, 77, 99});
        weaponTable.put("듀얼보우건160", new int[] {29, 43, 59, 77, 99});
        weaponTable.put("단검160", new int[] {29, 43, 59, 77, 99});
        weaponTable.put("블레이드160", new int[] {0, 0, 0, 0, 0});
        weaponTable.put("체인160", new int[] {29, 43, 59, 77, 99});
        weaponTable.put("한손검160", new int[] {30, 44, 60, 79, 101});
        weaponTable.put("한손도끼160", new int[] {30, 44, 60, 79, 101});
        weaponTable.put("한손둔기160", new int[] {30, 44, 60, 79, 101});
        weaponTable.put("케인160", new int[] {30, 44, 60, 79, 101});
        weaponTable.put("석궁160", new int[] {30, 44, 60, 79, 101});
        weaponTable.put("튜너160", new int[] {31, 46, 63, 82, 106});
        weaponTable.put("창160", new int[] {31, 46, 63, 82, 106});
        weaponTable.put("두손검160", new int[] {31, 46, 63, 82, 106});
        weaponTable.put("두손도끼160", new int[] {31, 46, 63, 82, 106});
        weaponTable.put("두손둔기160", new int[] {31, 46, 63, 82, 106});
        weaponTable.put("데스페라도160", new int[] {31, 46, 63, 82, 106});
        weaponTable.put("핸드캐논160", new int[] {32, 47, 64, 84, 108});

        //200제 무기들
        weaponTable.put("아대200", new int[] {27, 40, 55, 72, 92});
        weaponTable.put("건200", new int[] {39, 58, 79, 104, 133});
        weaponTable.put("에너지소드200", new int[] {40, 59, 81, 106, 136});
        weaponTable.put("너클200", new int[] {40, 59, 81, 106, 136});
        weaponTable.put("건틀렛 리볼버200", new int[] {40, 59, 81, 106, 136});
        weaponTable.put("소울슈터200", new int[] {40, 59, 81, 106, 136});
        weaponTable.put("폴암200", new int[] {48, 70, 96, 127, 163});
        weaponTable.put("에인션트 보우200", new int[] {50, 73, 101, 133, 170});
        weaponTable.put("부채200", new int[] {50, 73, 101, 133, 170});
        weaponTable.put("활200", new int[] {50, 73, 101, 133, 170});
        weaponTable.put("브레스 슈터200", new int[] {50, 73, 101, 133, 170});
        weaponTable.put("듀얼보우건200", new int[] {50, 73, 101, 133, 170});
        weaponTable.put("단검200", new int[] {50, 73, 101, 133, 170});
        weaponTable.put("블레이드200", new int[] {0, 0, 0, 0, 0});
        weaponTable.put("체인200", new int[] {50, 73, 101, 133, 170});
        weaponTable.put("한손검200", new int[] {51, 75, 103, 136, 175});
        weaponTable.put("한손도끼200", new int[] {51, 75, 103, 136, 175});
        weaponTable.put("한손둔기200", new int[] {51, 75, 103, 136, 175});
        weaponTable.put("케인200", new int[] {51, 75, 103, 136, 175});
        weaponTable.put("석궁200", new int[] {51, 75, 103, 136, 175});
        weaponTable.put("대검200", new int[] {18, 40, 65, 95, 131});
        weaponTable.put("태도200", new int[] {18, 40, 65, 95, 131});
        weaponTable.put("튜너200", new int[] {54, 78, 108, 142, 182});
        weaponTable.put("창200", new int[] {54, 78, 108, 142, 182});
        weaponTable.put("두손검200", new int[] {54, 78, 108, 142, 182});
        weaponTable.put("두손도끼200", new int[] {54, 78, 108, 142, 182});
        weaponTable.put("두손둔기200", new int[] {54, 78, 108, 142, 182});
        weaponTable.put("데스페라도200", new int[] {54, 78, 108, 142, 182});
        weaponTable.put("핸드캐논200", new int[] {55, 80, 110, 145, 186});
        weaponTable.put("완드200", new int[] {63, 92, 126, 167, 214});
        weaponTable.put("샤이닝로드200", new int[] {63, 92, 126, 167, 214});
        weaponTable.put("ESP리미터200", new int[] {63, 92, 126, 167, 214});
        weaponTable.put("매직 건틀렛200", new int[] {63, 92, 126, 167, 214});
        weaponTable.put("스태프200", new int[] {64, 94, 129, 170, 218});

    }
}
