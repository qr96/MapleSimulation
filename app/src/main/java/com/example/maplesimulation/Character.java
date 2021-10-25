package com.example.maplesimulation;

import java.io.Serializable;
import java.util.ArrayList;

public class Character implements Serializable {
    private String name;
    private String job;
    private int level;
    private int[] stats; // str, dex, int, luk, hp
    private int[] hyperStats; //str, dex, int, luk, hp, 크확, 크뎀, 방무, 데미지, 보스뎀, 공, 마, 최종뎀, 공퍼, 마퍼, 숙련도
    private int[] skillStats;
    private ArrayList<Equipment> equipped;

    public final int STR = 0;
    public final int DEX = 1;
    public final int INT = 2;
    public final int LUK = 3;
    public final int HP = 4;
    public final int CRIPOSS = 5;
    public final int CRIDMG = 6;
    public final int IGNORE = 7;
    public final int DMG = 8;
    public final int BOSSDMG = 9;
    public final int ATTK = 10;
    public final int MAGIC = 11;
    public final int FDMG = 12;
    public final int ATTKPER = 13;
    public final int MAGICPER = 14;

    public Character() {
        this.name = "이름 없음";
        this.job = "초보자";
        this.level = 250;
        this.stats = new int[20];
        this.hyperStats = new int[20];
        this.skillStats = new int[20];
        equipped = new ArrayList<Equipment>();
        for(int i=0;i<25;i++) { equipped.add(new Equipment()); }
        for(int i=0;i<4;i++) stats[i] = 4;
    }

    public Character(String name, String job, int level) {
        this.name = name;
        this.job = job;
        this.level = level;
        this.stats = new int[10];
        this.hyperStats = new int[20];
        this.skillStats = new int[20];
        equipped = new ArrayList<Equipment>();
        for(int i=0;i<25;i++) { equipped.add(new Equipment()); }
        for(int i=0;i<4;i++) stats[i] = 4;
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

    public void setEquipped(int idx, Equipment equipment) {
        equipped.set(idx, equipment);
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

    public int getHyperStats(final int option) { return hyperStats[option]; }

    public int getSkillStats(final int option) { return skillStats[option]; }
}
