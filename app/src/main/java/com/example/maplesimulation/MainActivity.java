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

    private int INVENSIZE = 32; //인벤토리 크기

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //광고 초기화
        initAd();

        //인벤토리 배열 초기화
        initInvenList();

        //인벤토리 크기 초기화
        initINVENSIZE();

        now = -1; //현재 선택된 장비 없음
    }

    //인벤토리 크기 초기화
    public void initINVENSIZE() {
        INVENSIZE = PreferenceManager.getInt(this, "INVENSIZE");
        if(INVENSIZE == -1) { //최초 실행인 경우
            INVENSIZE = 32;
            PreferenceManager.setInt(this, "INVENSIZE", INVENSIZE);
        }
    }

    //인벤토리 배열 초기화, DB에서 읽어올 예정
    public void initInvenList() {
        inventory = PreferenceManager.getInventory(this, "inventory");
    }

    public void infoPopup(View view) {
        if(now == -1) return;
        Intent intent = new Intent(this, InfoActivity.class);
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
        if(INVENSIZE==inventory.size()){
            CustomNotice customNotice = new CustomNotice(MainActivity.this);
            customNotice.show();
            customNotice.setContent("인벤토리가 부족합니다.\n 장비를 삭제하거나 인벤토리를 확장해주세요.");
            return;
        }

        Intent intent = new Intent(this, SearchActivity.class);
        startActivityForResult(intent, 0);
    }

    public void goScroll(View view) {
        //장비 미선택 시
        if(now == -1) {
            nothingDialog();
            return;
        }
        if(inventory.get(now).getType().equals("엠블렘") || inventory.get(now).getType().equals("뱃지")
                || inventory.get(now).getType().equals("보조무기")){
            CustomNotice customNotice = new CustomNotice(MainActivity.this);
            customNotice.show();
            customNotice.setContent("주문서 사용이 불가능한 장비입니다.");
            return;
        }

        Intent intent = new Intent(this, ScrollActivity.class);
        intent.putExtra("inventory", inventory);
        intent.putExtra("now", now);
        startActivityForResult(intent, 1);
    }

    public void goPotential(View view) {
        //장비 미선택 시
        if(now == -1) {
            nothingDialog();
            return;
        }
        if(inventory.get(now).getType().equals("뱃지") || inventory.get(now).getType().equals("포켓아이템")){
            CustomNotice customNotice = new CustomNotice(MainActivity.this);
            customNotice.show();
            customNotice.setContent("잠재능력 재설정이 불가능한 장비입니다.");
            return;
        }
        Intent intent = new Intent(this, PotentialActivity.class);
        intent.putExtra("inventory", inventory);
        intent.putExtra("now", now);
        startActivityForResult(intent, 1);
    }

    public void goStarforce(View view) {
        //장비 미선택 시
        if(now == -1 || inventory.size() == 0) {
            nothingDialog();
            return;
        }
        if(inventory.get(now).isNoljang || inventory.get(now).getType().equals("뱃지") ||
                inventory.get(now).getType().equals("엠블렘") || inventory.get(now).getType().equals("포켓아이템")
                || inventory.get(now).getType().equals("보조무기")){
            CustomNotice customNotice = new CustomNotice(MainActivity.this);
            customNotice.show();
            customNotice.setContent("스타포스를 할 수 없는 장비입니다.");
            return;
        }
        Intent intent = new Intent(this, StarforceActivity.class);
        intent.putExtra("inventory", inventory);
        intent.putExtra("now", now);
        startActivityForResult(intent, 1);
    }

    public void goInven(View view) {
        Intent intent = new Intent(this, InvenActivity.class);
        intent.putExtra("inventory", inventory);
        intent.putExtra("now", now);
        startActivityForResult(intent, 1);
    }

    public void goEquip(View view) {
        Intent intent = new Intent(this, EquipActivity.class);
        startActivity(intent);
    }

    //"장비를 추가해주세요" 다이얼로그
    public void nothingDialog() {
        CustomNotice customNotice = new CustomNotice(MainActivity.this);
        customNotice.setCanceledOnTouchOutside(false);
        customNotice.show();
        customNotice.setContent("장비 추가 후 시도해주세요!");
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

        if(now <0 || now>inventory.size()){
            CustomNotice customNotice = new CustomNotice(MainActivity.this);
            customNotice.show();
            customNotice.setContent("썸네일 오류가 발생했습니다!\n개발자에게 얘기해주시면 더 빠르게 오류가 수정됩니다\nerr code:"+now);
            return;
        }

        int lid = this.getResources()
                .getIdentifier(inventory.get(now).getImage(), "drawable", this.getPackageName());

        imageView.setImageResource(lid);
        textView.setText(inventory.get(now).getName());

    }
}
