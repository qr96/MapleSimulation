package com.example.maplesimulation;

import java.util.Objects;

public class Flame {
    Equipment equipment;
    AdditionalTable additionalTable;

    //Constructor
    public Flame(Equipment equipment) {
        this.additionalTable = new AdditionalTable();
        this.equipment = equipment;
    }

    // n개중에 r개 중복 없이 랜덤으로 선택
    public static int[] selectN(int n, int r) {
        int result[] = new int[r];

        // 번호 생성
        for (int i = 0; i < r; i++) {
            result[i] = (int) (Math.random() * n);

            // 중복 존재하면 다시 생성
            for (int j = 0; j < i; j++) {
                if (result[i] == result[j]) {
                    i--;
                    break;
                }
            }
        }

        return result;
    }

    // 테이블 확률대로 하나 반환
    public int tableRandom(double[] table) {
        // 번호 생성
        double ran = Math.random() * 100;
        double w = 0;

        for (int i = 0; i < table.length; i++) {
            w = w + table[i];

            if (ran < w) {
                return i;
            }
        }
        return -1;
    }

    //강력한 환생의 불꽃
    public int usePowerfulFlame() {

        int grade = -1; //추가 옵션의 등급
        int selected[]; // 선택된 추가옵션들
        double table[] = {20, 30, 36, 14, 0}; //강환불 추옵 등급 확률 테이블

        selected = selectN(19, 4); //강환불은 4개의 추옵 상승

        equipment.initAdditional(); //추옵 초기화

        for(int i=0; i<selected.length; i++){
            grade = tableRandom(table);

            //방어구, 장신구인 경우
            if(equipment.isArmor() || equipment.isAccessary()) flameToArmor(selected[i], grade);
            else flameToWeapon(selected[i], grade);
        }

        return 0;
    }

    //영원한 환생의 불꽃
    public int useEternalFlame() {

        int grade = -1; //추가 옵션의 등급
        int selected[]; // 선택된 추가옵션들
        double table[] = {0, 29, 45, 25, 1}; //강환불 추옵 등급 확률 테이블

        selected = selectN(19, 4); //강환불은 4개의 추옵 상승

        equipment.initAdditional(); //추옵 초기화

        for(int i=0; i<selected.length; i++){
            grade = tableRandom(table);

            //방어구, 장신구인 경우
            if(equipment.isArmor() || equipment.isAccessary()) flameToArmor(selected[i], grade);
            else flameToWeapon(selected[i], grade);
        }

        return 0;
    }

    //방어구, 장신구 추옵 설정
    public void flameToArmor(int selected, int grade) {
        int tmp = 0;
        int levReq = equipment.getLevReq();

        if(selected < 4) { //주스텟
            tmp = Objects.requireNonNull(additionalTable.justat_table.get(levReq))[grade];
            equipment.setAdditionStat(selected, tmp);
        }
        else if(selected < 10) { //복합스텟
            int mixstats[][] = {{0, 1}, {0, 2}, {0, 3}, {1, 2}, {1, 3}, {2, 3}}; //복합스텟 조합들
            int stat1 = mixstats[selected-4][0];
            int stat2 = mixstats[selected-4][1];
            tmp = Objects.requireNonNull(additionalTable.mixstat_table.get(levReq))[grade];
            equipment.setAdditionStat(stat1, tmp);
            tmp = Objects.requireNonNull(additionalTable.mixstat_table.get(levReq))[grade];
            equipment.setAdditionStat(stat2, tmp);
        }
        else if(selected < 12) { //HP, MP
            tmp = Objects.requireNonNull(additionalTable.hpmp_table.get(levReq))[grade];
            equipment.setAdditionStat(selected-6, tmp);
        }
        else if(selected == 12) { //착용 레벨 감소
            tmp = Objects.requireNonNull(additionalTable.levdown_table.get(levReq))[grade];
            equipment.setAdditionStat(6, tmp);
        }
        else if(selected == 13) { //방어력
            tmp = Objects.requireNonNull(additionalTable.justat_table.get(levReq))[grade];
            equipment.setAdditionStat(7, tmp);
        }
        else if(selected < 18) { //공격력, 마력, 이동속도, 점프력
            tmp = Objects.requireNonNull(additionalTable.allstat_table.get(levReq))[grade];
            equipment.setAdditionStat(selected-6, tmp);
        }
        else if(selected == 18) { //올스텟
            tmp = Objects.requireNonNull(additionalTable.allstat_table.get(levReq))[grade];
            equipment.setAdditionStat(12, tmp);
        }

    }

    //무기 추옵 설정
    public void flameToWeapon(int selected, int grade) {
        int tmp = 0;
        int levReq = equipment.getLevReq();
        String type = equipment.getType();

        if(selected < 4) { //주스텟
            tmp = Objects.requireNonNull(additionalTable.justat_table.get(levReq))[grade];
            equipment.setAdditionStat(selected, tmp);
        }
        else if(selected < 10) { //복합스텟
            int mixstats[][] = {{0, 1}, {0, 2}, {0, 3}, {1, 2}, {1, 3}, {2, 3}}; //복합스텟 조합들
            int stat1 = mixstats[selected-4][0];
            int stat2 = mixstats[selected-4][1];
            tmp = Objects.requireNonNull(additionalTable.mixstat_table.get(levReq))[grade];
            equipment.setAdditionStat(stat1, tmp);
            tmp = Objects.requireNonNull(additionalTable.mixstat_table.get(levReq))[grade];
            equipment.setAdditionStat(stat2, tmp);
        }
        else if(selected < 12) { //HP, MP
            tmp = Objects.requireNonNull(additionalTable.hpmp_table.get(levReq))[grade];
            equipment.setAdditionStat(selected-6, tmp);
        }
        else if(selected == 12) { //착용 레벨 감소
            tmp = Objects.requireNonNull(additionalTable.levdown_table.get(levReq))[grade];
            equipment.setAdditionStat(6, tmp);
        }
        else if(selected == 13) { //방어력
            tmp = Objects.requireNonNull(additionalTable.justat_table.get(levReq))[grade];
            equipment.setAdditionStat(7, tmp);
        }
        else if(selected < 16) { //공격력, 마력
            tmp = additionalTable.weaponTable.get(type+levReq)[grade];
            equipment.setAdditionStat(selected-6, tmp);
        }
        else if(selected == 16) { //보스 데미지
            tmp = Objects.requireNonNull(additionalTable.bossdmg_table.get(levReq))[grade];
            equipment.setAdditionStat(19, tmp);
        }
        else if(selected == 17) { //데미지
            tmp = Objects.requireNonNull(additionalTable.allstat_table.get(levReq))[grade];
            equipment.setAdditionStat(20, tmp);
        }
        else if(selected == 18) { //올스텟
            tmp = Objects.requireNonNull(additionalTable.allstat_table.get(levReq))[grade];
            equipment.setAdditionStat(12, tmp);
        }
    }
}
