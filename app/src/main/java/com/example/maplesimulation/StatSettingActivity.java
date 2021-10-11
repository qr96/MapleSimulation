package com.example.maplesimulation;

import android.annotation.SuppressLint;
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
    private Character character;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting_stat);

        initCharater();
        updateView();

        //광고 초기화
        initAd();
    }

    public void initCharater() {
        character = new Character();
        character.setName("이름없음");
        character.setJob("초보자");
        character.setLevel(250);
    }

    @SuppressLint("SetTextI18n")
    public void updateView() {
        TextView nameText = findViewById(R.id.set_name);
        TextView jobText = findViewById(R.id.set_job);
        TextView levelText = findViewById(R.id.set_level);
        TextView strText = findViewById(R.id.set_str);
        TextView dexText = findViewById(R.id.set_dex);
        TextView intText = findViewById(R.id.set_int);
        TextView lukText = findViewById(R.id.set_luk);
        TextView hpText = findViewById(R.id.set_hp);

        nameText.setText("이름 : "+character.getName());
        jobText.setText("직업 : "+character.getJob());
        levelText.setText("레벨 : "+character.getLevel());
        strText.setText("STR : "+character.getStats("str"));
        dexText.setText("DEX : "+character.getStats("dex"));
        intText.setText("INT : "+character.getStats("int"));
        lukText.setText("LUK : "+character.getStats("luk"));
        hpText.setText("HP : "+character.getStats("hp"));
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

    public void goSetSTR(View view) {
        Intent intent = new Intent(this, SetNumberPopup.class);
        intent.putExtra("type", "str");
        startActivityForResult(intent, 0);
    }
    public void goSetDEX(View view) {
        Intent intent = new Intent(this, SetNumberPopup.class);
        intent.putExtra("type", "dex");
        startActivityForResult(intent, 0);
    }
    public void goSetINT(View view) {
        Intent intent = new Intent(this, SetNumberPopup.class);
        intent.putExtra("type", "int");
        startActivityForResult(intent, 0);
    }
    public void goSetLUK(View view) {
        Intent intent = new Intent(this, SetNumberPopup.class);
        intent.putExtra("type", "luk");
        startActivityForResult(intent, 0);
    }
    public void goSetHP(View view) {
        Intent intent = new Intent(this, SetNumberPopup.class);
        intent.putExtra("type", "hp");
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
                        character.setLevel(number);
                    }
                    else if(type.equals("str") ||
                            type.equals("dex") ||
                            type.equals("int") ||
                            type.equals("luk") ||
                            type.equals("hp")) {
                        character.setStats(type, number);
                    }

                    updateView();

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
