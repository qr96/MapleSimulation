package com.example.maplesimulation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LauncherActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inven);

        Intent intent = getIntent();
        this.invenList = (ArrayList<Equipment>) intent.getSerializableExtra("invenList");

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
            adapter.addItem(null);
        }

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Equipment equipment = (Equipment) adapter.getItem(position);
                if(equipment != null) {
                    Intent intent = new Intent(view.getContext(), EquipmentPopup.class);
                    intent.putExtra("equipment", equipment);
                    startActivity(intent);
                }
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(InvenActivity.this);
                myAlertBuilder.setMessage("장비를 삭제하시겠습니까?");

                // 버튼 추가 (Ok 버튼과 Cancle 버튼 )
                myAlertBuilder.setPositiveButton("  예  ",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int which){
                        PreferenceManager.removeKey(InvenActivity.this, invenList.get(position).getId());
                        invenList.remove(position);
                        for(int i=0; i<invenList.size(); i++) {
                            invenList.get(i).setId(i);
                        }
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
                return true;
            }
        });
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

    public void infoPopup(View view) {

    }
}
