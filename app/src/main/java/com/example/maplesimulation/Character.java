package com.example.maplesimulation;

import java.util.ArrayList;

public class Character {
    private String name;
    private String job;
    private int level;
    private int[] stats; // str, dex, int, luk, hp
    private int[] hyperStats; //str, dex, int, luk, hp, 크확, 크뎀, 방무, 데미지, 보뎀, 공/마
    private int[] skillStats;
    private ArrayList<Equipment> equipped;

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

    public void setStats(String option, int n) {
        if(option.equals("str")) {
            stats[0] = n;
        }
        else if(option.equals("dex")) {
            stats[1] = n;
        }
        else if(option.equals("int")) {
            stats[2] = n;
        }
        else if(option.equals("luk")) {
            stats[3] = n;
        }
        else if(option.equals("hp")) {
            stats[4] = n;
        }
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

    public int getStats(String option) {
        if(option.equals("str")) {
            return stats[0];
        }
        else if(option.equals("dex")) {
            return stats[1];
        }
        else if(option.equals("int")) {
            return stats[2];
        }
        else if(option.equals("luk")) {
            return stats[3];
        }
        else if(option.equals("hp")) {
            return stats[4];
        }

        return 0;
    }
}
