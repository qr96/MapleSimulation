package com.example.maplesimulation;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public ArrayList<Equipment> equipmentList ; //장비 객체 배열
    public ArrayList<String> equipNameList; //장비 이름 배열
    private Equipment equipment; //현재 장비
    public ArrayList<Equipment> invenList; //인벤토리 배열
    public final int INVENTORY_SIZE = 40;
    int nowEquip;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DB 초기화
        initLoadDB();

        //광고 초기화
        initAd();

        //인벤토리 배열 초기화
        initInvenList();

    }

    //인벤토리 배열 초기화, DB에서 읽어올 예정
    public void initInvenList() {
        Equipment tmp;
        this.invenList = new ArrayList<Equipment>();
        for(int i=0; i<INVENTORY_SIZE; i++) {
            tmp = PreferenceManager.getEquipment(this, "equip"+i);
            if(tmp != null && tmp.getId() != -1) {
                this.invenList.add(tmp);
            }
        }
    }

    public void infoPopup(View view) {
        if(this.equipment == null) return;
        Intent intent = new Intent(this, EquipmentPopup.class);
        intent.putExtra("equipment", this.equipment);
        startActivity(intent);
    }

    //광고 초기화
    public void initAd(){
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void goSearch(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("equipList", this.equipmentList);
        startActivityForResult(intent, 0);
    }

    public void goScroll(View view) {
        //장비 미선택 시
        if(equipment == null) {
            nothingDialog();
            return;
        }

        Intent intent = new Intent(this, ScrollActivity.class);
        intent.putExtra("equipment", equipment);
        startActivityForResult(intent, 1);

    }

    public void goPotential(View view) {
        if(equipment == null) {
            nothingDialog();
            return;
        }
        Intent intent = new Intent(this, PotentialActivity.class);
        intent.putExtra("equipment", equipment);
        startActivityForResult(intent, 1);
    }

    public void goStarforce(View view) {
        if(equipment == null) {
            nothingDialog();
            return;
        }
        Intent intent = new Intent(this, StarforceActivity.class);
        intent.putExtra("equipment", equipment);
        startActivityForResult(intent, 1);
    }

    public void goInven(View view) {
        Intent intent = new Intent(this, InvenActivity.class);
        intent.putExtra("invenList", this.invenList);
        intent.putExtra("Equipment", this.equipment);
        startActivityForResult(intent, 2);
    }


    //"장비를 추가해주세요" 다이얼로그
    public void nothingDialog() {
        AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(MainActivity.this);
        myAlertBuilder.setTitle("장비 확인");
        myAlertBuilder.setMessage("장비 추가 후에 시도해주세요.");

        // 버튼 추가 (Ok 버튼)
        myAlertBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){
                
            }
        });

        myAlertBuilder.show();
    }

    //intent 받는 부분
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 0: //장비 등록
                if(data!=null) {
                    Equipment equipment = (Equipment) data.getSerializableExtra("equipment");
                    try {
                        Equipment newEquipment = (Equipment) equipment.clone();
                        newEquipment.setId(invenList.size());
                        invenList.add(newEquipment);
                        this.equipment = invenList.get(invenList.size()-1);
                        setThumnail();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 1:
                if(data != null) {
                    Equipment equip = (Equipment) data.getSerializableExtra("equip");
                    this.equipment = equip;
                    invenList.set(this.equipment.getId(), this.equipment);
                    setThumnail();
                }
                break;
            case 2: //from inventory
                if(data != null) {
                    this.invenList = (ArrayList<Equipment>) data.getSerializableExtra("invenList");
                    this.equipment = (Equipment) data.getSerializableExtra("Equipment");
                    setThumnail();
                }
        }
    }

    //선택된 장비 이미지 표시, 이름 표시
    public void setThumnail() {
        ImageView imageView = (ImageView)findViewById(R.id.thumnail);
        TextView textView = (TextView)findViewById(R.id.selected_name);

        if(this.equipment == null || this.equipment.getId() == -1) {
            Toast.makeText(this, "새로운 장비를 추가해 주세요", Toast.LENGTH_SHORT).show();
            textView.setText("장비를 추가해 주세요");
            imageView.setImageResource(R.drawable.ui_request_equipment);
            return;
        }

        int lid = this.getResources()
                .getIdentifier(equipment.getImage(), "drawable", this.getPackageName());

        imageView.setImageResource(lid);
        textView.setText(equipment.getName());

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
