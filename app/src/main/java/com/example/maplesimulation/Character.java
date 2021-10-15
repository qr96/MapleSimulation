package com.example.maplesimulation;

import java.util.ArrayList;

public class Character {
    private String name;
    private String job;
    private int level;
    private int[] stats; // str, dex, int, luk, hp
    private int[] hyperStats; //str, dex, int, luk, hp, 크확, 크뎀, 방무, 데미지, 보뎀, 공, 마
    private int[] skillStats;
    private ArrayList<Equipment> equipped;
    public final int STR = 0;
    public final int DEX = 1;
    public final int INT = 2;
    public final int LUK = 3;
    public final int HP = 4;
    public final int CRIPOSS = 5;

    public Character() {
        this.name = "이름 없음";
        this.job = "초보자";
        this.level = 250;
        this.stats = new int[10];
    }

    public Character(String name, String job, int level) {
        this.name = name;
        this.job = job;
        this.level = level;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setLevel(int level) { this.level = level; }

    public void setStats(final int option, int n) {
        stats[option] = n;
    }

    public String getInfo() {
        String info = "";
        info = "이름 : " + this.name + "\n" +
                "레벨 : " + this.level + "\n" +
                "직업 : " + this.job;

        return info;
    }

    public String getName() { return this.name; }

    public String getJob() {
        return this.job;
    }

    public int getLevel() { return this.level; }

    public ArrayList<Equipment> getEquipped() {
        return equipped;
    }

    public int getStats(final int option) {
        return stats[option];
    }
}
