package com.example.maplesimulation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

public class StarforceActivity extends Activity {
    private Equipment equipment;
    private AdView mAdView;
    public AnimationDrawable resultAnimation;
    public Starforce starforce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starforce);

        Intent intent = getIntent();
        this.equipment = (Equipment) intent.getSerializableExtra("equipment");
        starforce = new Starforce(this.equipment);

        setThumnail();
        updateText();
        initAd();
        initSpinner();

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

    //스피너 초기화 (이벤트, MVP 설정)
    public void initSpinner() {
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

    public void doStarforce(View view) {
        int result = starforce.doStarforce();

        if(result == 0){
            successEffect();
        }
        else if(result == 1){
            destroyEffect();
        }
        else if(result == 2){
            failEffect();
        }

        updateText();
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

        double success = starforce.table_success[equipment.getStar()];
        double destroy = starforce.table_destroyed[equipment.getStar()];
        double fail = 100.0 - success - destroy;

        TextView textView = findViewById(R.id.info);
        String equipInfo = equipment.getStar()+"성 > "+(equipment.getStar()+1)+"성\n\n"+
                "성공확률: "+success+"%\n"+
                "실패(유지)확률: "+fail+"\n"+
                "파괴확률: "+destroy+"%\n";
        textView.setText(equipInfo);

        TextView needMoney = findViewById(R.id.needmoney);
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        String money = "필요한 메소: "
                +decimalFormat.format(starforce.howMuch(equipment.getLevReq(), equipment.getStar()+1));
        needMoney.setText(money);
    }

    //성공 이펙트 실행
    public void successEffect() {
        ImageView resultImage = (ImageView) findViewById(R.id.resultEffect);
        resultImage.setBackgroundResource(0);
        resultImage.setBackgroundResource(R.drawable.ui_success_effect);
        resultAnimation = (AnimationDrawable) resultImage.getBackground();
        resultAnimation.start();
    }

    //실패 이펙트 실행
    public void failEffect() {
        ImageView resultImage = (ImageView) findViewById(R.id.resultEffect);
        resultImage.setBackgroundResource(0);
        resultImage.setBackgroundResource(R.drawable.ui_fail_effect);
        resultAnimation = (AnimationDrawable) resultImage.getBackground();
        resultAnimation.start();
    }

    //파괴 이펙트 실행
    public void destroyEffect() {
        ImageView resultImage = (ImageView) findViewById(R.id.resultEffect);
        resultImage.setBackgroundResource(0);
        resultImage.setBackgroundResource(R.drawable.ui_destroy_effect);
        resultAnimation = (AnimationDrawable) resultImage.getBackground();
        resultAnimation.start();
    }
}
