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
    private ArrayList<Equipment> invenList;
    private Equipment equipment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inven);

        Intent intent = getIntent();
        this.invenList = (ArrayList<Equipment>) intent.getSerializableExtra("invenList");
        this.equipment = (Equipment) intent.getSerializableExtra("Equipment");

        //인벤토리 초기화
        initGridView();

        //광고 초기화
        initAd();
    }

    //인벤토리 그리드뷰 초기화
    public void initGridView() {
        GridView gridView = findViewById(R.id.gridView);
        GridListAdapter adapter = new GridListAdapter();

        for(int i=0; i<invenList.size(); i++){
            adapter.addItem(invenList.get(i));
        }
        for(int i=0; i<40-invenList.size(); i++){
            adapter.addItem(new Equipment());
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Equipment equipment = (Equipment) adapter.getItem(position);
                if(equipment != null && equipment.getId() != -1) {
                    infoPopup(view.getContext(), equipment);
                }
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Equipment equipment = (Equipment) adapter.getItem(position);
                if(equipment != null && equipment.getId()!=-1) {
                    removeItemDialog(position);
                }
                return true;
            }
        });

        gridView.setAdapter(adapter);
    }

    public void removeItemDialog(int position) {
        AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(InvenActivity.this);
        myAlertBuilder.setMessage("장비를 삭제하시겠습니까?");

        // 버튼 추가 (Ok 버튼과 Cancle 버튼 )
        myAlertBuilder.setPositiveButton("  예  ",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){
                int id = invenList.get(position).getId();
                if(id == equipment.getId()){
                    equipment = new Equipment();
                }
                invenList.remove(position);

                for(int i=position; i<invenList.size(); i++){
                    PreferenceManager.setEquipment(InvenActivity.this, "equip"+i, invenList.get(i));
                }
                PreferenceManager.removeKey(InvenActivity.this, "equip"+invenList.size());

                initGridView();
                Toast.makeText(InvenActivity.this, "장비가 삭제되었습니다", Toast.LENGTH_SHORT).show();
            }
        });
        myAlertBuilder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        myAlertBuilder.show();
    }

    public void infoPopup(Context context, Equipment equipment) {
        Intent intent = new Intent(context, EquipmentPopup2.class);
        intent.putExtra("equipment", equipment);
        startActivityForResult(intent, 0);
    }

    //intent 받는 부분
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 0: //강화를 눌렀다는 것
                if(data!=null) {
                    Equipment equipment = (Equipment) data.getSerializableExtra("equipment");
                    this.equipment = equipment;
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("invenList", this.invenList);
        intent.putExtra("Equipment", this.equipment);
        setResult(2, intent);
        finish();
        super.onBackPressed();
    }
}
