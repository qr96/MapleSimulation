package com.example.maplesimulation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public List<Equipment> equipmentList ; //장비 객체 배열
    public ArrayList<String> equipNameList; //장비 이름 배열
    private Equipment equipment; //현재 장비

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLoadDB();
    }

    public void goPotential(View view) {
        Intent intent = new Intent(this, PotentialActivity.class);
        startActivity(intent);
    }

    public void goSearch(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("equipList", equipNameList);
        startActivityForResult(intent, 0);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 0: //최초 장비
                if(data!=null) // 데이터가 빈값인지
                {
                    String ReceiveData = data.getStringExtra("equip");
                    System.out.println(ReceiveData);
                }
        }
    }

    //DB 읽어서 List에 추가
    private void initLoadDB() {
        DataAdapter mDbHelper = new DataAdapter(getApplicationContext());
        mDbHelper.createDatabase();
        mDbHelper.open();

        // db에 있는 값들을 model을 적용해서 넣는다.
        equipmentList = mDbHelper.getTableData();
        System.out.println("init DB");

        equipNameList = new ArrayList<String>();

        // 이름은 배열에 따로 저장
        for(Equipment equipment : equipmentList) {
            equipNameList.add(equipment.getName());
        }

        // db 닫기
        mDbHelper.close();
    }
}
