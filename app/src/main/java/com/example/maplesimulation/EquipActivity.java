package com.example.maplesimulation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

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

    public void equip(int position) {
        ImageView imageView = findViewById(R.id.ring1);

        if (inventory.get(position).isWeapon()) {
            imageView = findViewById(R.id.weapon);
        }
        else {
            switch (inventory.get(position).getType()) {
                case "반지":
                    imageView = findViewById(R.id.ring1);
                    break;
                case "펜던트":
                    imageView = findViewById(R.id.pendent2);
                    break;
                case "귀고리":
                    imageView = findViewById(R.id.earing);
                    break;
                case "벨트":
                    imageView = findViewById(R.id.belt);
                    break;
                case "모자":
                    imageView = findViewById(R.id.hat);
                    break;
                case "상의":
                    imageView = findViewById(R.id.shirt);
                    break;
                case "하의":
                    imageView = findViewById(R.id.pants);
                    break;
                case "한벌옷":
                    imageView = findViewById(R.id.shirt);
                    break;
                case "장갑":
                    imageView = findViewById(R.id.glove);
                    break;
                case "신발":
                    imageView = findViewById(R.id.shoes);
                    break;
                case "망토":
                    imageView = findViewById(R.id.cape);
                    break;
                case "어깨장식":
                    imageView = findViewById(R.id.shoulder);
                    break;
                case "얼굴장식":
                    imageView = findViewById(R.id.face);
                    break;
                case "눈장식":
                    imageView = findViewById(R.id.eye);
                    break;
                case "포켓아이템":
                    imageView = findViewById(R.id.pocket);
                    break;
                case "엠블렘":
                    imageView = findViewById(R.id.emblem);
                    break;
                case "보조무기":
                    imageView = findViewById(R.id.subweapon);
                    break;
                case "기계심장":
                    imageView = findViewById(R.id.heart);
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
            }

            @Override
            public void onNegativeClick() {
            }
        });
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.show();
        customDialog.setMessage(inventoryForGrid.get(position).getName() + "을(를) 장착하시겠습니까?");
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

    public void infoPopup(View view) {

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
