package com.example.maplesimulation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class EquipActivity extends Activity {
    public int selected_button_id = -1; //선택된 아이템의 id
    public int selected_detail_id = -1; //선택된 아이템의 세부 옵션 id  ex) 70%, 30% 주문서

    public Flame flame;

    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equip);

        //광고 초기화
        initAd();
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
