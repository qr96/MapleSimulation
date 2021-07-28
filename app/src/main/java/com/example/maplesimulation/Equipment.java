package com.example.maplesimulation;

import java.io.Serializable;
import java.util.ArrayList;

public class Equipment implements Cloneable, Serializable {
    //private String id;
    private String name;
    private String Image;
    private String group;
    private int levReq;

    private int maxUp; //업가횟
    private int nowUp; //남은 업가횟

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

    public int getLevReq() { return levReq; }

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

    public void setLevReq(int levReq) {
        this.levReq = levReq;
    }

    public void setMaxUp(int maxUp) { this.maxUp = maxUp; }

    public void setNowUp(int nowUp) { this.nowUp = nowUp; }

    public void setMaxStar(int maxStar) { this.maxStar = maxStar; }

    public void setStar(int star) { this.star = star; }

    public void addStats(int stat) {
        this.stats.add(stat);
        this.enhance.add(0);
        this.additional.add(0);
    }

}
