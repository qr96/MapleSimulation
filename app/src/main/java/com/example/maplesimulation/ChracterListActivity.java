package com.example.maplesimulation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.ArrayList;

public class ChracterListActivity extends Activity {
    private AdView mAdView;
    private RewardedAd mRewardedAd;

    private ArrayList<Character> characterList;
    private ChracterPageAdapter mChracterPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);

        initData();
        initViewpage();

        //광고 초기화
        initAd();

    }

    public void initData() {
        characterList = new ArrayList();

        Character character = new Character("파래박", "패스파인더", 250);
        Character character2 = new Character("난뒤로만가지", "호영", 250);
        Character character3 = new Character("루델팡", "아델", 250);

        characterList.add(character);
        characterList.add(character2);
        characterList.add(character3);
    }

    public void initViewpage() {
        mChracterPageAdapter = new ChracterPageAdapter(this, characterList);
        ViewPager viewPager = findViewById(R.id.character_viewpager);
        viewPager.setClipToPadding(false);
        viewPager.setAdapter(mChracterPageAdapter);
    }

    public void goEquipped(View view) {
        Intent intent = new Intent(this, EquipActivity.class);
        startActivityForResult(intent, 0);
    }

    public void goRewardAd(View view) {
        CustomDialog customDialog = new CustomDialog(this, new CustomDialogClickListener() {
            @Override
            public void onPositiveClick() {
                if (mRewardedAd != null) {
                    Activity activityContext = ChracterListActivity.this;
                    mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            Character character = new Character("이름없음", "초보자", 250);
                            characterList.add(character);
                            mChracterPageAdapter.notifyDataSetChanged();

                            CustomNotice customNotice = new CustomNotice(ChracterListActivity.this);
                            customNotice.show();
                            customNotice.setContent("감사합니다. 캐릭터 슬롯이 확장되었습니다.");
                        }
                    });
                } else {
                    CustomNotice customNotice = new CustomNotice(ChracterListActivity.this);
                    customNotice.show();
                    customNotice.setContent("광고가 아직 준비되지 않았습니다...\n 다음에 다시 시도해주세요.");
                }
            }
            @Override
            public void onNegativeClick() {
            }
        });
        customDialog.show();
        customDialog.setMessage("광고를 보고 슬롯을 확장하시겠습니까?");
    }

    //광고 초기화
    public void initAd(){
        //하단 배너 광고
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //보상형 광고 초기화
        RewardedAd.load(this, getResources().getString(R.string.inven_reward),
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

    public void goHelp(View view) {
        CustomNotice notice = new CustomNotice(this);
        notice.show();
        notice.setTitle("도움말");
        notice.setContent("이곳에서 캐릭터들을 볼 수 있습니다.\n" +
                "1. 캐릭터를 선택하여 장비 장착 화면으로 이동 가능합니다.\n" +
                "2. 슬라이드하면 다른 슬롯들도 확인 가능합니다.\n" +
                "3. 상단의 선물상자를 누르면 광고 시청 후 슬롯을 확장할 수 있습니다.");
    }

    public void goBack(View view) {
        onBackPressed();
    }

}
