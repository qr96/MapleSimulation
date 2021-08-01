package com.example.maplesimulation;

import java.util.HashMap;
import java.util.List;

public class CubeTable {

    //<부위, 옵션목록> , <부위, 확률목록>
    public HashMap<String, List> optaionTable;
    public HashMap<String, List> percentTable;

    public CubeTable() {
        optaionTable = new HashMap<>();
        percentTable = new HashMap<>();
    }

    public void setOptaionTable(HashMap<String, List> optaionTable) {
        this.optaionTable = optaionTable;
    }

    public void setPercentTable(HashMap<String, List> percentTable) {
        this.percentTable = percentTable;
    }
}

