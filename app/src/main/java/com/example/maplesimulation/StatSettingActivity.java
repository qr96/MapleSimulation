package com.example.maplesimulation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;

public class StatSettingActivity extends Activity {
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting_stat);


        //광고 초기화
        initAd();
    }

    public void goInput(View view) {
        Intent intent = new Intent(this, InputPopup.class);
        startActivity(intent);
    }

    public void goSetLevel(View view) {
        Intent intent = new Intent(this, SetNumberPopup.class);
        intent.putExtra("type", "level");
        startActivityForResult(intent, 0);
    }

    public void goSetStat(View view) {
        Intent intent = new Intent(this, SetNumberPopup.class);
        intent.putExtra("type", "stat");
        startActivityForResult(intent, 0);
    }

    //데이터 받는 부분
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 0:
                if(data!=null) {
                    String type = data.getStringExtra("type");
                    int number = data.getIntExtra("number", 0);

                    if(type.equals("level")) {
                        TextView textView = findViewById(R.id.set_level);
                        textView.setText("레벨 : "+number);
                    }
                    else if(type.equals("stat")) {

                    }

                }
                else { //받은 데이터가 없는 경우
                }
                break;
        }
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
