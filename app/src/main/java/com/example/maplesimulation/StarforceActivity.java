package com.example.maplesimulation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
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

    public String events[] = {"이벤트없음", "10성1+1", "30%할인", "5,10,15성100%"};
    public String mvps[] = {"브론즈", "실버", "골드", "다이아"};

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
        initCheckBox();
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
        Spinner event_spinner = (Spinner) findViewById(R.id.event_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.event_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        event_spinner.setAdapter(adapter);
        event_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                starforce.event = events[position];
                updateText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.mvp_spinner);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.mvp_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                starforce.mvp = mvps[position];
                updateText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //체크박스 초기화
    public void initCheckBox() {
        CheckBox pcbang = findViewById(R.id.pcbang);
        pcbang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starforce.pcbang = pcbang.isChecked();
                updateText();
            }
        });

        CheckBox prevent = findViewById(R.id.preventdestroy);
        prevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starforce.prevent = prevent.isChecked();
                updateText();
            }
        });
    }

    public void doStarforce(View view) {
        view.setEnabled(false);

        CheckBox starcatch = findViewById(R.id.starcatch);
        CheckBox prevent = findViewById(R.id.preventdestroy);

        int result = starforce.doStarforce(starcatch.isChecked(), starforce.event);

        if(result == 0){
            successEffect();
        }
        else if(result == 1){
            destroyEffect();
        }
        else if(result == 2){
            failEffect();
        }

        sparkleEffect();
        updateText();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }
        }, 600);
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

    //장비 강화 상태창 업데이트
    public void updateText() {
        if(this.equipment == null) return;

        double success = starforce.table_success[equipment.getStar()];
        double destroy = starforce.table_destroyed[equipment.getStar()];
        double fail = 100.0 - success - destroy;
        int stats[] = starforce.increment();

        TextView textView = findViewById(R.id.info);
        String equipInfo = equipment.getStar()+"성 > "+(equipment.getStar()+1)+"성\n\n";
        if(starforce.isChanceTime()){
            equipInfo = "찬스타임!!!!\n\n성공확률:100%\n\n";
        }
        else{
            equipInfo = equipInfo+"성공확률: "+success+"%\n"+"실패";
            if(starforce.canDown()) equipInfo = equipInfo+"(하락)";
            else equipInfo = equipInfo+"(유지)";
            equipInfo = equipInfo+"확률: "+fail+"\n"+
                    "파괴확률: "+destroy+"%\n\n";
        }
         equipInfo = equipInfo+"주스텟: +"+stats[0]+"\n"+
                "공격력: +"+stats[1]+"\n"+
                "마력: +"+stats[2]+"\n";


        textView.setText(equipInfo);

        TextView needMoney = findViewById(R.id.needmoney);
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        String money = "필요한 메소: "
                +decimalFormat.format(starforce.howMuch(equipment.getLevReq(), equipment.getStar()+1));
        needMoney.setText(money);

        //파괴방지 버튼 비활성화
        CheckBox prevent = findViewById(R.id.preventdestroy);

        if(starforce.canPrevent()){
            prevent.setEnabled(true);
        }
        else{
            if(prevent.isChecked()) prevent.toggle();
            prevent.setEnabled(false);
        }
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

    //반짝 이펙트
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

        Animation anim = new AlphaAnimation(0.0f, 0.6f);
        anim.setDuration(400);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(1);

        sparkle.startAnimation(anim);
        anim.setAnimationListener(listener);

    }
}
