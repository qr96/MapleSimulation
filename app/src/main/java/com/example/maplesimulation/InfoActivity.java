package com.example.maplesimulation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class InfoActivity extends Activity {
    Equipment equipment;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popup_equipment);

        //데이터 가져오기
        Intent intent = getIntent();
        this.equipment = (Equipment) intent.getSerializableExtra("equipment");

        initInfo();

        //광고 초기화
        initAd();
    }

    public void usedPopup(View view) {
        if(this.equipment == null) return;
        Intent intent = new Intent(this, UsedPopup.class);
        intent.putExtra("equipment", this.equipment);
        startActivity(intent);
    }

    public void close(View view) {
        onBackPressed();
    }

    public void initInfo() {
        String tmp = "";

        TextView star = findViewById(R.id.star);
        tmp = EquipmentInfo.starText(equipment);
        star.setText(Html.fromHtml(tmp));
        if(equipment.getMaxStar()==0) star.setVisibility(View.GONE);

        TextView name = findViewById(R.id.name);
        tmp = equipment.getName();
        if(equipment.getNowUp()>0) tmp = tmp+"(+"+equipment.getNowUp()+")";
        name.setText(tmp);

        TextView grade = findViewById(R.id.grade);
        grade.setText("("+equipment.getPotentialGrade1()+" 아이템)");

        ImageView thumail = findViewById(R.id.thumnail);
        int imageRID = this.getResources().getIdentifier(equipment.getImage(), "drawable", this.getPackageName());
        thumail.setImageResource(imageRID);

        TextView attack = findViewById(R.id.attack);

        TextView reqlev = findViewById(R.id.reqlev);
        reqlev.setText("REQ LEV : "+equipment.getLevReq());

        TextView info = findViewById(R.id.info);
        tmp = "장비분류 : " + equipment.getType() + "<br>";
        tmp = tmp + EquipmentInfo.makeText(equipment);
        info.setText(Html.fromHtml(tmp));

        TextView potential1 = findViewById(R.id.potential1);
        tmp = EquipmentInfo.potential1(equipment);
        potential1.setText(Html.fromHtml(tmp));

        TextView potential2 = findViewById(R.id.potential2);
        tmp = EquipmentInfo.potential2(equipment);
        potential2.setText(Html.fromHtml(tmp));
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
