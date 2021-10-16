package com.example.maplesimulation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;

public class EquipActivity extends Activity {
    public int selected_button_id = -1; //선택된 아이템의 id
    public int selected_detail_id = -1; //선택된 아이템의 세부 옵션 id  ex) 70%, 30% 주문서

    private ArrayList<Equipment> inventory;
    private ArrayList<Equipment> inventoryForGrid;
    private ArrayList<Equipment> equipped;
    private ArrayList<Character> characterList;

    private int INVENSIZE = 20; //인벤토리 크기

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equip);

        //광고 초기화
        initAd();

        INVENSIZE = PreferenceManager.getInt(this, "INVENSIZE");
        inventory = PreferenceManager.getInventory(this, "inventory");

        //캐릭터 목록 테스트 부분
        characterList = new ArrayList<Character>();
        characterList.add(new Character());
        equipped = characterList.get(0).getEquipped();

        initGridView();
    }

    //인벤토리 그리드뷰 초기화
    public void initGridView() {
        GridView gridView = findViewById(R.id.inven_gridview);
        GridListAdapter adapter = new GridListAdapter();

        inventoryForGrid = new ArrayList<Equipment>();

        inventoryForGrid.addAll(inventory);
        while(inventoryForGrid.size()<INVENSIZE) inventoryForGrid.add(new Equipment());

        for(int i=0; i<INVENSIZE; i++){
            adapter.addItem(inventoryForGrid.get(i));
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                confirmDialog(position);
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return true;
            }
        });

        gridView.setAdapter(adapter);
    }

    public void goStatInfo(View view) {
        Intent intent = new Intent(this, StatInfoActivity.class);
        intent.putExtra("equipped", equipped);
        startActivity(intent);
    }

    public void goStatSetting(View view) {
        Intent intent = new Intent(this, StatSettingActivity.class);
        startActivity(intent);
    }

    public void equip(int position) {
        ImageView imageView = findViewById(R.id.ring1);
        
        if (inventory.get(position).isWeapon()) {
            imageView = findViewById(R.id.weapon);
            equipped.set(7, inventory.get(position));
        }
        else {
            switch (inventory.get(position).getType()) {
                case "반지":
                    if(equipped.get(0).getName().equals("잘못된 이름")) {
                        equipped.set(0, inventory.get(position));
                        imageView = findViewById(R.id.ring1);
                    }
                    else if(equipped.get(1).getName().equals("잘못된 이름")) {
                        equipped.set(1, inventory.get(position));
                        imageView = findViewById(R.id.ring2);
                    }
                    else if(equipped.get(2).getName().equals("잘못된 이름")) {
                        equipped.set(2, inventory.get(position));
                        imageView = findViewById(R.id.ring3);
                    }
                    else if(equipped.get(3).getName().equals("잘못된 이름")){
                        equipped.set(3, inventory.get(position));
                        imageView = findViewById(R.id.ring4);
                    }
                    else {
                        inventory.add(equipped.get(3));
                        equipped.set(3, inventory.get(position));
                        imageView = findViewById(R.id.ring4);
                    }
                    break;
                case "포켓아이템":
                    imageView = findViewById(R.id.pocket);
                    equipped.set(4, inventory.get(position));
                    break;
                case "펜던트":
                    if(equipped.get(5).getName().equals("잘못된 이름")) {
                        equipped.set(5, inventory.get(position));
                        imageView = findViewById(R.id.pendant1);
                    }
                    else if(equipped.get(6).getName().equals("잘못된 이름")) {
                        equipped.set(6, inventory.get(position));
                        imageView = findViewById(R.id.pendant2);
                    }
                    else {
                        inventory.add(equipped.get(6));
                        equipped.set(6, inventory.get(position));
                        imageView = findViewById(R.id.pendant2);
                    }
                    break;
                case "벨트":
                    imageView = findViewById(R.id.belt);
                    equipped.set(8, inventory.get(position));
                    break;
                case "모자":
                    imageView = findViewById(R.id.hat);
                    equipped.set(9, inventory.get(position));
                    break;
                case "얼굴장식":
                    imageView = findViewById(R.id.face);
                    equipped.set(10, inventory.get(position));
                    break;
                case "눈장식":
                    imageView = findViewById(R.id.eye);
                    equipped.set(11, inventory.get(position));
                    break;
                case "상의":
                    imageView = findViewById(R.id.shirt);
                    equipped.set(12, inventory.get(position));
                    break;
                case "한벌옷":
                    imageView = findViewById(R.id.shirt);
                    equipped.set(12, inventory.get(position));
                    break;
                case "하의":
                    imageView = findViewById(R.id.pants);
                    equipped.set(13, inventory.get(position));
                    break;
                case "신발":
                    imageView = findViewById(R.id.shoes);
                    equipped.set(14, inventory.get(position));
                    break;
                case "귀고리":
                    imageView = findViewById(R.id.earing);
                    equipped.set(15, inventory.get(position));
                    break;
                case "어깨장식":
                    imageView = findViewById(R.id.shoulder);
                    equipped.set(16, inventory.get(position));
                    break;
                case "장갑":
                    imageView = findViewById(R.id.glove);
                    equipped.add(17, inventory.get(position));
                    break;
                case "안드로이드":
                    imageView = findViewById(R.id.android);
                    equipped.add(18, inventory.get(position));
                    break;
                case "엠블렘":
                    imageView = findViewById(R.id.emblem);
                    equipped.set(19, inventory.get(position));
                    break;
                case "뱃지":
                    imageView = findViewById(R.id.badge);
                    equipped.set(20, inventory.get(position));
                    break;
                case "훈장":
                    imageView = findViewById(R.id.medal);
                    equipped.add(21, inventory.get(position));
                    break;
                case "보조무기":
                    imageView = findViewById(R.id.subweapon);
                    equipped.set(22, inventory.get(position));
                    break;
                case "망토":
                    imageView = findViewById(R.id.cape);
                    equipped.set(23, inventory.get(position));
                    break;
                case "기계심장":
                    imageView = findViewById(R.id.heart);
                    equipped.set(24, inventory.get(position));
                    break;
                default:
                    break;
            }
        }

        int lid = this.getResources()
                .getIdentifier(inventory.get(position).getImage(), "drawable", this.getPackageName());

        imageView.setImageResource(lid);
    }

    //장비 추가 다이얼로그
    private void confirmDialog(final int position) {
        if (position >= inventory.size()) return;
        CustomDialog customDialog = new CustomDialog(this, new CustomDialogClickListener() {
            @Override
            public void onPositiveClick() {
                equip(position);
                inventory.remove(position);
                initGridView();
            }
            @Override
            public void onNegativeClick() {
            }
        });
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.show();
        customDialog.setMessage(inventoryForGrid.get(position).getName() + "을(를) 장착하시겠습니까?");
    }

    public void openInventory(View view) {
        LinearLayout linearLayout = findViewById(R.id.inventory_popup);
        linearLayout.setVisibility(View.VISIBLE);

        View view1 = findViewById(R.id.equipped_blank);
        view1.setVisibility(View.VISIBLE);
    }

    public void closeInventory(View view) {
        LinearLayout linearLayout = findViewById(R.id.inventory_popup);
        linearLayout.setVisibility(View.INVISIBLE);

        View view1 = findViewById(R.id.equipped_blank);
        view1.setVisibility(View.GONE);
    }

    //광고 초기화
    public void initAd() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @SuppressLint("NonConstantResourceId")
    public void infoPopup(View view) {
        int now = 0;
        switch (view.getId()) {
            case R.id.ring1:
                now = 0;
                break;
            case R.id.ring2:
                now = 1;
                break;
            case R.id.ring3:
                now = 2;
                break;
            case R.id.ring4:
                now = 3;
                break;
            case R.id.pocket:
                now = 4;
                break;
            case R.id.pendant1:
                now = 5;
                break;
            case R.id.pendant2:
                now = 6;
                break;
            case R.id.weapon:
                now = 7;
                break;
            case R.id.belt:
                now = 8;
                break;
            case R.id.hat:
                now = 9;
                break;
            case R.id.face:
                now = 10;
                break;
            case R.id.eye:
                now = 11;
                break;
            case R.id.shirt:
                now = 12;
                break;
            case R.id.pants:
                now = 13;
                break;
            case R.id.shoes:
                now = 14;
                break;
            case R.id.earing:
                now = 15;
                break;
            case R.id.shoulder:
                now = 16;
                break;
            case R.id.glove:
                now = 17;
                break;
            case R.id.android:
                now = 18;
                break;
            case R.id.emblem:
                now = 19;
                break;
            case R.id.badge:
                now = 20;
                break;
            case R.id.medal:
                now = 21;
                break;
            case R.id.subweapon:
                now = 22;
                break;
            case R.id.cape:
                now = 23;
                break;
            case R.id.heart:
                now = 24;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
        if(equipped.isEmpty() || equipped.get(now).getName().equals("잘못된 이름")) return;
        Intent intent = new Intent(this, InfoActivity.class);
        intent.putExtra("equipment", equipped.get(now));
        startActivity(intent);
    }

    public void goBack(View view) {
        onBackPressed();
    }

    public void goHelp(View view) {
        CustomNotice notice = new CustomNotice(this);
        notice.show();
        notice.setTitle("도움말");
        notice.setContent("이곳에서 장비를 장착할 수 있습니다. \n1. 상단에는 현재 강화중인 장비가 표시됩니다.\n");
    }
}
