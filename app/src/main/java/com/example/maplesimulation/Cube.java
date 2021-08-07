package com.example.maplesimulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cube {
    Equipment equipment;
    CubeTable cubeTable;
    String cubeType;
    double[] upgradePossibility;

    //큐브 같은 테이블 쓰는 조합들
    ArrayList<String> subWeapon = new ArrayList<>(Arrays.asList("포스실드", "소울링", "방패")); //보조무기
    ArrayList<String> armor2 = new ArrayList<>(Arrays.asList("망토", "벨트", "어깨장식")); //망토
    ArrayList<String> accessary = new ArrayList<>(Arrays.asList("얼굴장식", "눈장식", "귀고리", "반지", "펜던트")); //얼굴장식

    //에디셔널 큐브 같은 테이블 쓰는 부위들
    ArrayList<String> armorsForAddi = new ArrayList<>(Arrays.asList("한벌옷", "하의", "신발", "망토", "벨트", "어깨장식", "얼굴장식", "눈장식", "귀고리", "반지", "펜던트"));
    ArrayList<String> subWeaponForAddi = new ArrayList<>(Arrays.asList("포스실드", "소울링", "방패"));

    public Cube(Equipment equipment, CubeTable cubeTable, String cubeType){
        this.equipment = equipment;
        this.cubeTable = cubeTable;

        this.cubeType = cubeType;

        if(cubeType.equals("블랙큐브")) {
            upgradePossibility = new double[]{15.0, 3.5, 1.0};
        }
        else if(cubeType.equals("레드큐브")) {
            upgradePossibility = new double[]{6.0, 1.8, 0.3};
        }
        else if(cubeType.equals("에디셔널큐브")) {
            upgradePossibility = new double[]{4.7619, 1.9608, 0.4975};
        }
        else if(cubeType.equals("명장의큐브")) {
            upgradePossibility = new double[]{7.9994, 1.6959, 0.1996};
        }
        else if(cubeType.equals("장인의큐브")) {
            upgradePossibility = new double[]{4.7619, 1.1858, 0.0};
        }
        else if(cubeType.equals("수상한에디셔널큐브")) {
            upgradePossibility = new double[]{0.0, 0.0, 0.0};
        }
        else {
            upgradePossibility = new double[]{0.0, 0.0, 0.0};
        }
    }

    public void useCube() {
        if(cubeType.equals("레드큐브")) equipment.used_redcube++;
        else if(cubeType.equals("블랙큐브")) equipment.used_blackcube++;
        else if(cubeType.equals("명장의큐브")) equipment.used_myungjangcube++;
        else if(cubeType.equals("장인의큐브")) equipment.used_jangincube++;
        else return;;

        String[] option;
        String type = equipment.getType();

        //같은 테이블인 경우들 처리
        if(equipment.isWeapon()) type = "무기";
        else if(subWeapon.contains(type)) type="보조무기";
        else if(type.equals("한벌옷")) type="상의";
        else if(armor2.contains(type)) type="망토";
        else if(accessary.contains(type)) type="얼굴장식";

        switch (equipment.getPotentialGrade1()) {
            case "레어":
                if(isSuccess(upgradePossibility[0])){ //등급업
                    equipment.potentialGradeUp1();
                }
                break;
            case "에픽":
                if(isSuccess(upgradePossibility[1])){ //등급업
                    equipment.potentialGradeUp1();
                }
                break;
            case "유니크":
                if(isSuccess(upgradePossibility[2])){ //등급업
                    equipment.potentialGradeUp1();
                }
                break;
        }
        option = determine(type, equipment.getPotentialGrade1());
        this.equipment.setPotential1(option);
    }

    public void useAddiCube() {
        if(cubeType.equals("에디셔널큐브")) equipment.used_addicube++;
        else if(cubeType.equals("수상한에디셔널큐브")) equipment.used_strangeAddicube++;
        String[] option;
        String type = equipment.getType();

        if(equipment.isWeapon()) type = "무기";
        else if(subWeaponForAddi.contains(type)) type="보조무기";
        else if(armorsForAddi.contains(type)) type="상의"; //armors 들은 상의와 에디 옵션 테이블이 같기 때문

        switch (equipment.getPotentialGrade2()) {
            case "레어":
                if(isSuccess(upgradePossibility[0])){ //등급업
                    equipment.potentialGradeUp2();
                }
                break;
            case "에픽":
                if(isSuccess(upgradePossibility[1])){ //등급업
                    equipment.potentialGradeUp2();
                }
                break;
            case "유니크":
                if(isSuccess(upgradePossibility[2])){ //등급업
                    equipment.potentialGradeUp2();
                }
                break;
        }
        option = determine(type, equipment.getPotentialGrade2());
        this.equipment.setPotential2(option);
    }

    String[] mustTwo = {"몬스터 방어율 무시", "보스 몬스터", "아이템 드롭률", "피격 시"}; //최대 두개만 설정되는 옵션들

    //[장비 종류, 잠재 종류]
    private String[] determine(String type, String grade){
        String option[] = new String[3];

        option[0] = select(grade+type+"1");
        option[1] = select(grade+type+"2");
        option[2] = select(grade+type+"3");

        for(String must : mustTwo) {
            if(option[0].contains(must) && option[1].contains(must)) { //1,2번에 must2 옵션 있는 경우
                while(option[2].contains(must)) option[2] = select(grade+type+"3"); //3번에 없을때까지 반복
            }
        }

        return option;
    }

    //정해진 확률에 따른 성공 여부
    public Boolean isSuccess(double possibility) {
        double random = Math.random()*100;

        if(random < possibility) return true;
        return false;
    }

    //테이블 확률대로 반환
    public int tableRandom(List table) {
        // 번호 생성
        double ran = Math.random() * 100;
        double w = 0;

        for (int i = 0; i < table.size(); i++) {
            w = w + (double) table.get(i);

            if (ran < w) {
                return i;
            }
        }
        return -1;
    }

    //테이블에서 key값에 맞는 옵션 랜덤하게 선택
    private String select(String key){
        System.out.println("Cube key : "+key);
        List table = cubeTable.percentTable.get(key);
        System.out.println("percent table : "+cubeTable.percentTable.get(key));
        if(table == null) return "Error : " + key;
        int random = tableRandom(table);
        return (String) cubeTable.optionTable.get(key).get(random);
    }
}
