package com.example.maplesimulation;

import java.io.Serializable;

public class Equipment implements Cloneable, Serializable {
    public String id;
    public String name;
    public String Image;
    public String group;
    public int levReq;
    public int STR;
    public int DEX;
    public int INT;
    public int LUK;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getName() {
        return name;
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

    public void setSTR(int STR) {
        this.STR = STR;
    }

    public void setDEX(int DEX) {
        this.DEX = DEX;
    }

    public void setINT(int INT) {
        this.INT = INT;
    }

    public void setLUK(int LUK) {
        this.LUK = LUK;
    }
}
