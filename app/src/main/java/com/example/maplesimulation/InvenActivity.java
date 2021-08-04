package com.example.maplesimulation;

import android.app.Activity;
import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class InvenActivity extends Activity {
    private AdView mAdView;
    private Equipment equipment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inven);

        Intent intent = getIntent();
        this.equipment = (Equipment) intent.getSerializableExtra("equipment");



        //인벤토리 초기화
        initGridView();

        //광고 초기화
        initAd();
    }

    //인벤토리 그리드뷰 초기화
    public void initGridView() {
        GridView gridView = findViewById(R.id.gridView);
        GridListAdapter adapter = new GridListAdapter();

        adapter.addItem(this.equipment);
        for(int i=0; i<39; i++){
            adapter.addItem(new Equipment());
        }

        gridView.setAdapter(adapter);
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
