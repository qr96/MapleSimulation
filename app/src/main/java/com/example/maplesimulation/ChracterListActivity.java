package com.example.maplesimulation;

import android.app.Activity;
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

    private ArrayList<Integer> imageList;
    private static final int DP = 24;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);

        imageList = new ArrayList();



        //광고 초기화
        initAd();

    }

    public void initData() {

    }

    public void initViewpage() {
        ViewPager viewPager = findViewById(R.id.character_viewpager);
        viewPager.setClipToPadding(false);

        float density = getResources().getDisplayMetrics().density;
        int margin = (int) (DP * density);
        viewPager.setPadding(margin, 0, margin, 0);
        viewPager.setPageMargin(margin/2);

        viewPager.setAdapter(new ChracterPageAdapter(this, imageList));
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

                        }
                    });
                } else {
                    CustomNotice customNotice = new CustomNotice(ChracterListActivity.this);
                    customNotice.show();
                    customNotice.setContent("광고가 아직 준비되지 않았습니다ㅠㅠ\n 다음에 다시 시도해주세요.");
                }
            }
            @Override
            public void onNegativeClick() {
            }
        });
        customDialog.show();
        customDialog.setMessage("광고를 보고 인벤토리를 확장하시겠습니까? (4칸 확장)");
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
        notice.setContent("이곳에서 저장된 장비들을 볼 수 있습니다.\n" +
                "1. 장비 클릭 후 하단의 강화하기 버튼을 누르면 강화할 수 있습니다.\n" +
                "2. 장비를 길게 누르면 장비가 삭제 됩니다.\n" +
                "3. 상단의 선물상자를 누르면 광고 시청 후 인벤토리를 확장할 수 있습니다.");
    }

    public void goBack(View view) {
        onBackPressed();
    }

}
