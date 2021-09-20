package com.example.maplesimulation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;

public class StatInfoActivity extends Activity {
    private AdView mAdView;
    private ArrayList<Equipment> equipped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stats);

        Intent intent = getIntent();
        equipped = (ArrayList<Equipment>) intent.getSerializableExtra("equipped");
        initStatInfo();

        //광고 초기화
        initAd();
    }

    public void initStatInfo() {
        String stats = "";
        TextView textView = findViewById(R.id.stat_info);

        StatManager statManager = new StatManager();
        stats = statManager.getStats(equipped);

        textView.setText(stats);
    }


    public void close(View view) {
        onBackPressed();
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
}
