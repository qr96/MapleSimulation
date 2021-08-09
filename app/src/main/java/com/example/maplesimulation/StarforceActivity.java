package com.example.maplesimulation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
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

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class StarforceActivity extends Activity {
    private ArrayList<Equipment> inventory;
    private Equipment equipment;
    private int now;

    private AdView mAdView;
    private RewardedAd mRewardedAd;

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
    }

    public void goRewardAd(View view) {
        String message = "";

        if(equipment.getName().contains("타일런트") && equipment.getStar()<10)
            message = "광고를 보고 장비를 10성으로 만드시겠습니까?";
        else if(equipment.getMaxStar() >= 20 && equipment.getStar()<20)
            message = "광고를 보고 장비를 20성으로 만드시겠습니까?";
        else return;
        
        CustomDialog customDialog = new CustomDialog(this, new CustomDialogClickListener() {
            @Override
            public void onPositiveClick() {
                if (mRewardedAd != null) {
                    Activity activityContext = StarforceActivity.this;
                    mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            if (equipment.getName().contains("타일런트")) {
                                while(equipment.getStar()<10) starforce.success();
                            }
                            else {
                                while(equipment.getStar()<20) starforce.success();
                            }
                            updateText();
                            PreferenceManager.setInventory(StarforceActivity.this, inventory);

                            CustomNotice customNotice = new CustomNotice(activityContext);
                            customNotice.show();
                            customNotice.setContent("감사합니다. 스타포스가 적용되었습니다.");
                        }
                    });
                } else {
                    CustomNotice customNotice = new CustomNotice(StarforceActivity.this);
                    customNotice.show();
                    customNotice.setContent("광고가 아직 준비되지 않았습니다ㅠㅠ\n 다음에 다시 시도해주세요.");
                }
            }
            @Override
            public void onNegativeClick() {
            }
        });
        customDialog.show();
        customDialog.setMessage(message);
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
                +decimalFormat.format(starforce.howMuch(equipment.getLevReq(), equipment.getStar()));
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

    public void goBack(View view) {
        onBackPressed();
    }

    public void goHelp(View view) {
        CustomNotice notice = new CustomNotice(this);
        notice.show();
        notice.setTitle("도움말");
        notice.setContent("이곳에서 스타포스 강화를 할 수 있습니다.\n" +
                "1. 상단에는 현재 강화중인 장비가 표시됩니다.\n" +
                "2. 스타캐치 체크 시, 성공 확률이 5% 증가합니다.\n" +
                "3. 파괴 방지 체크 시, 파괴가 되지 않고 필요 메소가 2배 증가합니다.\n"+
                "4. 상단의 선물상자를 누르고 광고를 보면 높은 스타포스가 부여됩니다.");
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

        //보상형 광고 초기화
        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;

                        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Set the ad reference to null so you don't show the ad a second time.
                                mRewardedAd = null;
                            }
                        });
                    }
                });
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
        Intent intent = new Intent(this, InfoActivity.class);
        intent.putExtra("equipment", equipment);
        startActivityForResult(intent, 1);
    }
}
