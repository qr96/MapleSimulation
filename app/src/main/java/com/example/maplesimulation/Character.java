package com.example.maplesimulation;

import java.util.ArrayList;

public class Character {
    private String name;
    private String job;
    private int level;
    private ArrayList<Equipment> equipped;

    public Character() {
        this.name = "이름 없음";
        this.job = "초보자";
        this.level = 250;
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

    public String getInfo() {
        String info = "";
        info = "이름 : " + this.name + "\n" +
                "레벨 : " + this.level + "\n" +
                "직업 : " + this.job;

        return info;
    }
}
