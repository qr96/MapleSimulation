package com.example.maplesimulation;

public class StarforceSuperior extends Starforce{

    public int table_stat[]; //능력치 상승 수치

    public StarforceSuperior(Equipment equipment) {
        super(equipment);
        initTable();
    }

    //테이블 초기화
    @Override
    public void initTable() {
        this.table_success = new double[]{
                52.5, 52.5, 47.25, 42.0, 42.0,
                42.0, 42.0, 42.0, 42.0, 38.85,
                36.75, 36.75, 3.15, 2.10, 1.05
        };
        this.table_destroyed = new double[]{
                0.0, 0.0, 0.0, 0.0, 0.0,
                1.74, 2.90, 4.06, 5.80, 9.17,
                12.65, 15.81, 48.43, 48.95, 49.48
        };
        this.table_stat = new int[]{
                19, 20, 22, 25, 29, //5성까진 주스텟
                9, 10, 11, 12, 13,  // 여기부터 공마
                15, 17, 19, 21, 23
        };

    }

    @Override
    public int doStarforce(boolean starCatch) {
        if(equipment.getStar() == equipment.getMaxStar()) return -1;
        double success = table_success[equipment.getStar()];
        double destroy = table_destroyed[equipment.getStar()];
        double fail = 100.0 - success - destroy;

        equipment.used_meso += howMuch(0, 0); //타일런트는 고정
        if(isChanceTime()) success = 100;

        int result = tableRandom(new double[]{success, destroy, fail});

        if(result == 0) { //성공
            success();
        }
        else if(result == 1) { //파괴
            destroyed();
        }
        else if(result == 2) { //실패(하락)
            failed();
        }

        return result;
    }

    @Override
    public void success() {
        int star = equipment.getStar();
        if(star<5) { //주스텟 증가
            for(int i=0; i<4; i++){
                equipment.upgradeStarStat(i, table_stat[star]);
            }
        }
        else { //공마 증가
            equipment.upgradeStarStat(8, table_stat[star]);
            equipment.upgradeStarStat(9, table_stat[star]);
        }
        equipment.upStar();
        chanceStack = 0;
    }

    @Override
    public void destroyed() {
        equipment.initStarStat();
        equipment.used_destroy++;
        chanceStack = 0;
    }

    @Override
    public void failed() {
        if(equipment.getStar()==0) return;
        equipment.downStar();
        chanceStack++;

        int star = equipment.getStar();
        if(star<5) { //주스텟 다시 감소
            for(int i=0; i<4; i++){
                equipment.downStarStat(i, table_stat[star]);
            }
        }
        else { //공마 다시 감소
            equipment.downStarStat(8, table_stat[star]);
            equipment.downStarStat(9, table_stat[star]);
        }
    }

    @Override
    public int[] increment(int star) {
        int stats[] = {0, 0, 0}; //주스텟, 공격력, 마력
        if(star<5) { //주스텟
            stats[0] = table_stat[star];
        }
        else { //공,마
            stats[1] = table_stat[star];
            stats[2] = table_stat[star];
        }
        return stats;
    }

    @Override
    public int howMuch(int reqlv, int step) { //무조건 고정
        return 55832200;
    }
}
