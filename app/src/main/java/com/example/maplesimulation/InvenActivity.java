package com.example.maplesimulation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;

public class InvenActivity extends Activity {
    private AdView mAdView;
    private ArrayList<Equipment> inventory;
    private ArrayList<Equipment> inventoryForGrid;
    private int now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inven);

        Intent intent = getIntent();
        inventory = (ArrayList<Equipment>) intent.getSerializableExtra("inventory");
        now = (int) intent.getSerializableExtra("now");

        //인벤토리 초기화
        initGridView();

        //광고 초기화
        initAd();
    }

    //인벤토리 그리드뷰 초기화
    public void initGridView() {
        GridView gridView = findViewById(R.id.gridView);
        GridListAdapter adapter = new GridListAdapter();

        inventoryForGrid = new ArrayList<Equipment>();

        inventoryForGrid.addAll(inventory);
        while(inventoryForGrid.size()<40) inventoryForGrid.add(new Equipment());

        for(int i=0; i<inventoryForGrid.size(); i++){
            adapter.addItem(inventoryForGrid.get(i));
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position<inventory.size()) infoPopup(view.getContext(), position);
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(position<inventory.size()) removeItemDialog(position);
                return true;
            }
        });

        gridView.setAdapter(adapter);
    }

    public void removeItemDialog(int position) {
        CustomDialog customDialog = new CustomDialog(this, new CustomDialogClickListener() {
            @Override
            public void onPositiveClick() {
                inventory.remove(position);
                if(position == now) now = -1; //현재 강화중인 아이템 삭제한 경우
                PreferenceManager.setInventory(InvenActivity.this, inventory);
                initGridView();
                Toast.makeText(InvenActivity.this, "장비가 삭제되었습니다", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNegativeClick() {
            }
        });
        customDialog.show();
        customDialog.setMessage("장비를 삭제하시겠습니까?");
    }

    public void infoPopup(Context context, int position) {
        Intent intent = new Intent(context, InfoActivityForInven.class);
        intent.putExtra("equipment", inventory.get(position));
        intent.putExtra("now", position);
        startActivityForResult(intent, 0);
    }

    //intent 받는 부분
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 0: //강화를 눌렀다는 것
                if(data!=null) {
                    now = (int) data.getSerializableExtra("now");
                    onBackPressed();
                }
                break;
        }
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

    public void goHelp(View view) {
        CustomNotice notice = new CustomNotice(this);
        notice.show();
        notice.setTitle("도움말");
        notice.setContent("이곳에서 저장된 장비들을 볼 수 있습니다.\n" +
                "1. 장비 클릭 후 하단의 강화하기 버튼을 누르면 강화할 수 있습니다.\n" +
                "2. 장비를 길게 누르면 장비가 삭제 됩니다.\n" +
                "3. 장비 상세보기 하단의 영수증을 클릭하면 사용된 아이템 내역을 볼 수 있습니다.");
    }

    public void goBack(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("inventory", inventory);
        intent.putExtra("now", now);
        setResult(1, intent);
        finish();
        super.onBackPressed();
    }
}
