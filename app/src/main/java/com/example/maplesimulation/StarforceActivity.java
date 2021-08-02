package com.example.maplesimulation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class StarforceActivity extends Activity {
    public Equipment equipment;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starforce);

        Intent intent = getIntent();
        this.equipment = (Equipment) intent.getSerializableExtra("equipment");

        setThumnail();
        updateText();
        initAd();

        Spinner spinner = (Spinner) findViewById(R.id.event_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.event_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner = (Spinner) findViewById(R.id.mvp_spinner);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.mvp_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
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

    //맨 위의 썸네일 설정
    public void setThumnail() {
        if(equipment == null) return;

        ImageView imageView = findViewById(R.id.equipment_image);
        int imageRID = this.getResources().getIdentifier(equipment.getImage(), "drawable", this.getPackageName());
        imageView.setImageResource(imageRID);

        setEquipName();
    }

    public void setEquipName() {
        TextView textView = findViewById(R.id.equipment_name);
        if(equipment.getNowUp()>0) textView.setText(equipment.getName()+" (+"+equipment.getNowUp()+")");
        else textView.setText(equipment.getName());
    }

    //장비의 능력치 표시 업데이트
    public void updateText() {
        if(this.equipment == null) return;

        TextView textView = findViewById(R.id.info);
        String equipInfo;
    }
}
