package com.example.maplesimulation;

import androidx.appcompat.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity {
    public ArrayList<Equipment> equipmentList ; //장비 객체 배열
    public ArrayList<Equipment> inventory; //인벤토리 배열
    public int now; //현재 강화중인 장비의 인덱스

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

        now = -1; //현재 선택된 장비 없음

    }

    //인벤토리 배열 초기화, DB에서 읽어올 예정
    public void initInvenList() {
        inventory = PreferenceManager.getInventory(this, "inventory");
    }

    public void infoPopup(View view) {
        if(inventory.get(now) == null) return;
        Intent intent = new Intent(this, EquipmentPopup.class);
        intent.putExtra("equipment", inventory.get(now));
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
        if(now == -1) {
            nothingDialog();
            return;
        }
        if(inventory.get(now).getType().equals("엠블렘") || inventory.get(now).getType().equals("뱃지")){
            Toast.makeText(this, "주문서 사용이 불가능한 장비입니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, ScrollActivity.class);
        intent.putExtra("inventory", inventory);
        intent.putExtra("now", now);
        startActivityForResult(intent, 1);
    }

    public void goPotential(View view) {
        if(inventory.get(now) == null) {
            nothingDialog();
            return;
        }
        if(inventory.get(now).getType().equals("뱃지")){
            Toast.makeText(this, "잠재능력 재설정이 불가능한 장비입니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, PotentialActivity.class);
        intent.putExtra("inventory", inventory);
        intent.putExtra("now", now);
        startActivityForResult(intent, 1);
    }

    public void goStarforce(View view) {
        if(inventory.get(now) == null) {
            nothingDialog();
            return;
        }
        if(inventory.get(now).isNoljang || inventory.get(now).getType().equals("뱃지") ||
                inventory.get(now).getType().equals("엠블렘")){
            Toast.makeText(this, "스타포스를 할 수 없는 장비입니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, StarforceActivity.class);
        intent.putExtra("equipment", inventory.get(now));
        startActivityForResult(intent, 1);
    }

    public void goInven(View view) {
        Intent intent = new Intent(this, InvenActivity.class);
        intent.putExtra("inventory", inventory);
        intent.putExtra("now", now);
        startActivityForResult(intent, 1);
    }

    //"장비를 추가해주세요" 다이얼로그
    public void nothingDialog() {
        CustomDialog customDialog = new CustomDialog(this, new CustomDialogClickListener() {
            @Override
            public void onPositiveClick() {
            }
            @Override
            public void onNegativeClick() {
            }
        });
        customDialog.show();
        customDialog.setMessage("장비를 추가해 주세요");
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
                        inventory.add(newEquipment);
                        PreferenceManager.setInventory(this, inventory);
                        now = inventory.size()-1;
                        setThumnail();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 1:
                if(data != null) {
                    inventory = (ArrayList<Equipment>) data.getSerializableExtra("inventory");
                    now = (int) data.getSerializableExtra("now");
                    setThumnail();
                }
                break;
        }
    }

    //선택된 장비 이미지 표시, 이름 표시
    public void setThumnail() {
        ImageView imageView = (ImageView)findViewById(R.id.thumnail);
        TextView textView = (TextView)findViewById(R.id.selected_name);

        if(now == -1 || inventory.size() == 0) {
            Toast.makeText(this, "새로운 장비를 추가해 주세요", Toast.LENGTH_SHORT).show();
            textView.setText("장비를 추가해 주세요");
            imageView.setImageResource(R.drawable.ui_request_equipment);
            return;
        }

        int lid = this.getResources()
                .getIdentifier(inventory.get(now).getImage(), "drawable", this.getPackageName());

        imageView.setImageResource(lid);
        textView.setText(inventory.get(now).getName());

    }

    //DB 읽어서 List에 추가
    private void initLoadDB() {
        DataAdapter mDbHelper = new DataAdapter(getApplicationContext());
        mDbHelper.createDatabase();
        mDbHelper.open();

        // db에 있는 값들을 model을 적용해서 넣는다.
        equipmentList = mDbHelper.getTableData();
        System.out.println("init DB");

        // db 닫기
        mDbHelper.close();
    }
}
