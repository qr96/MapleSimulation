package com.example.maplesimulation;

import java.io.Serializable;
import java.util.ArrayList;

public class Equipment implements Cloneable, Serializable {
    //private String id;
    private String name;
    private String Image;
    private String group;
    private String job;

    private int levReq;

    private int maxUp; //최대 강화수
    private int nowUp; //현재 강화수
    private int failUp; //실패한 강화수

    private int maxStar; //최대 스타포스 수치
    private int star; //스타포스 수치

    //기본 능력치들
    //STR, DEX, INT, LUK, 최대HP, 최대MP, 착용레벨감소, 방어력, 공격력, 마력, 이동속도, 점프력, 올스텟%, 최대HP%, 방무, 보공, 뎀지
    private ArrayList stats;

    //강화된 수치
    private ArrayList enhance;

    //추가 옵션
    private ArrayList additional;

    public Equipment() {
        stats = new ArrayList();
        enhance = new ArrayList();
        additional = new ArrayList();
    }

    //장비 강화   [-1:남은 횟수X, 0:실패 1:성공]
    public int doScroll(int possibility, String justat) {
        if(nowUp+failUp == maxUp) return -1;

        //강화 성공 시
        if(isSuccess(possibility) == true) {
            nowUp++;
            System.out.println("SUCCESS!!!!");
            int new_justat = 0;
            int new_hp = (int)enhance.get(4);
            int new_def = (int)enhance.get(7);

            if(possibility == 100) {
                new_justat += 3;
                new_hp += 30;
                new_def += 3;
            }
            else if(possibility == 70) {
                new_justat += 4;
                new_hp += 70;
                new_def += 5;
            }
            else if(possibility == 30) {
                new_justat += 7;
                new_hp += 120;
                new_def += 10;
            }

            if(justat == "STR") {
                new_justat += (int)enhance.get(0);
                enhance.set(0, new_justat);
                enhance.set(4, new_hp);
                enhance.set(7, new_def);
            }
            else if(justat == "DEX") {
                new_justat += (int)enhance.get(1);
                enhance.set(1, new_justat);
                enhance.set(4, new_hp);
                enhance.set(7, new_def);
            }
            return 1;
        }

        failUp++;

        return 0;
    }

    public Boolean isSuccess(int possibility) {
        int random = (int)(Math.random()*100);

        if(random < possibility) return true;
        return false;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getName() {
        return name;
    }

    public String getImage() { return Image; }

    public String getGroup() { return group; }

    public String getJob() { return  job; }

    public int getLevReq() { return levReq; }

    public int getMaxUp() { return maxUp; }

    public int getNowUp() { return nowUp; }

    public int getFailUp() { return failUp; }

    public ArrayList getStats() {
        return stats;
    }

    public ArrayList getEnhance() {
        return enhance;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) { this.Image = image; }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setJob(String job) { this.job = job; }

    public void setLevReq(int levReq) {
        this.levReq = levReq;
    }

    public void setMaxUp(int maxUp) { this.maxUp = maxUp; }

    public void setNowUp(int nowUp) { this.nowUp = nowUp; }

    public void setFailUp(int failUp) { this.failUp = failUp; }

    public void setMaxStar(int maxStar) { this.maxStar = maxStar; }

    public void setStar(int star) { this.star = star; }

    public void addStats(int stat) {
        this.stats.add(stat);
        this.enhance.add(0);
        this.additional.add(0);
    }

}
