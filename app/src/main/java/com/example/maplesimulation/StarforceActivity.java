package com.example.maplesimulation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class StarforceActivity extends Activity {
    private ArrayList<Equipment> inventory;
    private Equipment equipment;
    private int now;

    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    public AnimationDrawable resultAnimation;
    public Starforce starforce;

    public String events[] = {"이벤트없음", "10성1+1", "30%할인", "5,10,15성100%"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starforce);

        Intent intent = getIntent();
        inventory = (ArrayList<Equipment>) intent.getSerializableExtra("inventory");
        now = (int) intent.getSerializableExtra("now");
        equipment = inventory.get(now);

        if(equipment.getName().contains("타일런트")){
            starforce = new StarforceSuperior(equipment);
            initForSuperior();
        }
        else{
            starforce = new Starforce(equipment);
            initSpinner();
            initCheckBox();
        }

        setThumnail();
        updateText();
        initAd();
        initFullAd();
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

    //슈페리얼 장비용 초기화
    public void initForSuperior() {
        Spinner event_spinner = (Spinner) findViewById(R.id.event_spinner);
        event_spinner.setEnabled(false);

        CheckBox checkBox = findViewById(R.id.preventdestroy);
        checkBox.setEnabled(false);
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
    }

    //체크박스 초기화
    public void initCheckBox() {
        CheckBox prevent = findViewById(R.id.preventdestroy);
        prevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starforce.prevent = prevent.isChecked();
                updateText();
            }
        });
    }

    public void infoPopup(View view){
        Intent intent = new Intent(this, EquipmentPopup.class);
        intent.putExtra("equipment", equipment);
        startActivityForResult(intent, 1);
    }

    public void doStarforce(View view) {
        view.setEnabled(false);

        CheckBox starcatch = findViewById(R.id.starcatch);
        CheckBox prevent = findViewById(R.id.preventdestroy);

        int result = starforce.doStarforce(starcatch.isChecked());

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
        PreferenceManager.setInventory(this, inventory);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
                if(equipment.isFullAd()) startFullAd();
            }
        }, 600);
    }

    //맨 위의 썸네일 설정
    public void setThumnail() {
        if(equipment == null) return;

        ImageView imageView = findViewById(R.id.equipment_image);
        int imageRID = this.getResources().getIdentifier(equipment.getImage(),
                "drawable", this.getPackageName());
        imageView.setImageResource(imageRID);

        setEquipName();
    }

    public void setEquipName() {
        TextView textView = findViewById(R.id.equipment_name);
        if(equipment.getNowUp()>0)
            textView.setText(equipment.getName()+" (+"+equipment.getNowUp()+")");
        else textView.setText(equipment.getName());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("inventory", inventory);
        intent.putExtra("now", now);
        setResult(1, intent);
        finish();
        super.onBackPressed();
    }

    //장비 강화 상태창 업데이트
    public void updateText() {
        if(equipment == null) return;

        double success = starforce.table_success[equipment.getStar()];
        double destroy = starforce.table_destroyed[equipment.getStar()];
        double fail = 100.0 - success - destroy;
        int stats[] = starforce.increment(equipment.getStar());
        String equipInfo = "";

        TextView textView = findViewById(R.id.info);
        if(starforce.canDouble()) equipInfo = equipment.getStar()+"성 > "+(equipment.getStar()+2)+"성\n\n";
        else equipInfo = equipment.getStar()+"성 > "+(equipment.getStar()+1)+"성\n\n";
        if(starforce.isChanceTime()){
            equipInfo = equipInfo + "찬스타임!!!!\n성공확률:100%\n\n";
        }
        else if(starforce.can1516event()){
            equipInfo = equipInfo+"성공확률: 100%\n(5,10,15성 100% event)\n\n";
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
            starforce.prevent = false;
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

    //전면 광고 시작
    public void startFullAd() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }

    //전면 광고 초기화
    public void initFullAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitialAd = null;
                    }
                });
    }
}
