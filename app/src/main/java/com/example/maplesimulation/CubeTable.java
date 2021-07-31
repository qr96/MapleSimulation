package com.example.maplesimulation;

import java.util.HashMap;

public class CubeTable {

    //<부위, option> , table = option[n번째줄]
    public HashMap<String, String[]> legendary[];
    public HashMap<String, String[]> unique[];
    public HashMap<String, String[]> epic[];
    public HashMap<String, String[]> rare[];

    public CubeTable() {
        legendary = new HashMap[3];
        unique = new HashMap[3];
        epic = new HashMap[3];
        rare = new HashMap[3];
    }


}
