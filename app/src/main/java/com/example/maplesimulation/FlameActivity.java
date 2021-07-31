package com.example.maplesimulation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;

public class FlameActivity extends Activity {
    public int selected_button_id = -1; //선택된 아이템의 id
    public int selected_check_id = -1; //선택된 아이템의 체크 id
    Equipment equipment;
    Flame flame;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flame);

        Intent intent = getIntent();
        this.equipment = (Equipment) intent.getSerializableExtra("equipment");
        flame = new Flame(this.equipment);

        setThumnail();
        updateText();

        //광고 초기화
        initAd();

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

    public void sparkleEffect() {
        TextView sparkle = findViewById(R.id.sparkle_effect);

        Animation.AnimationListener listener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                sparkle.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                sparkle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };

        Animation anim = new AlphaAnimation(0.0f, 0.8f);
        anim.setDuration(400);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(1);

        sparkle.startAnimation(anim);
        anim.setAnimationListener(listener);

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
        String equipInfo = EquipmentInfo.makeText(this.equipment);

        textView.setText(Html.fromHtml(equipInfo));
    }

    public void goBack(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        intent.putExtra("equip", this.equipment);
        setResult(1, intent);
        finish();
        super.onBackPressed();
    }

    //N번 아이템 버튼 이벤트
    public void select(View view) {

        //이전에 선택된 항목의 체크 제거
        ImageView prev_select = (ImageView)findViewById(selected_check_id);
        if(prev_select != null) prev_select.setVisibility(View.INVISIBLE);

        //이미 선택된 버튼 클릭한 경우
        if(selected_button_id == view.getId()) {
            selected_button_id = -1;
            selected_check_id = -1;

            return;
        }

        //선택한 번호의 아이템에 체크 표시
        if(view.getId() == R.id.flame_button_0){
            //영환불
            ImageView new_select = (ImageView)findViewById(R.id.flame_check_0);
            new_select.setVisibility(View.VISIBLE);

            selected_button_id = view.getId();
            selected_check_id = R.id.flame_check_0;

        }

        else if(view.getId() == R.id.flame_button_1){
            //강환불
            ImageView new_select = (ImageView)findViewById(R.id.flame_check_1);
            new_select.setVisibility(View.VISIBLE);

            selected_button_id = view.getId();
            selected_check_id = R.id.flame_check_1;

        }
    }

    //강화 실행 버튼
    public void runEnhance(View view) {
        sparkleEffect();

        //0번 선택 : 영원한 환생의 불꽃
        if(selected_check_id == R.id.flame_check_0) {
            flame.useEternalFlame();
        }
        //1번 선택 : 강력한 환생의 불꽃
        else if(selected_check_id == R.id.flame_check_1) {
            flame.usePowerfulFlame();
        }

        updateText();
        setEquipName();
        view.setEnabled(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }

        }, 700);
    }
}

