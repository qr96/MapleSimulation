package com.example.maplesimulation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class
PotentialActivity extends Activity {
    public int selected_button_id = -1; //선택된 아이템의 id
    public int selected_check_id = -1; //선택된 아이템의 체크 id

    public List cubeTableList; //[블랙큐브, 레드큐브, 에디셔널큐브, 명장의큐브, 장인의큐브]

    private ArrayList<Equipment> inventory;
    private Equipment equipment; //현재 강화중인 장비
    private int now;

    private Cube blackCube;
    private Cube redCube;
    private Cube addiCube;
    private Cube myungjangCube;
    private Cube janginCube;
    private Cube strangeAddiCube;

    private AdView mAdView;
    private RewardedAd mRewardedAd;

    Animation autoAnim;

    //미라클 타임 적용 여부
    private boolean miracle = false;

    //auto모드로 버튼 눌렸는지 여부
    boolean keepGoing = false;
    
    //auto모드, 원하는 옵션 번호
    int autoOption = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_potential);

        Intent intent = getIntent();
        inventory = (ArrayList<Equipment>) intent.getSerializableExtra("inventory");
        now = (int) intent.getSerializableExtra("now");
        equipment = inventory.get(now);

        //큐브 DB 초기화
        initLoadCubeDB();

        updateText();
        setThumnail();
        setOptionText();

        initAutoAni();
        initCube();
        //initSpinner();

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

        //보상형 광고 초기화
        RewardedAd.load(this, getResources().getString(R.string.potential_reward),
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

    public void initAutoAni() {
        autoAnim = new AlphaAnimation(0.4f, 1.0f);
        autoAnim.setDuration(400);
        autoAnim.setRepeatMode(Animation.REVERSE);
        autoAnim.setRepeatCount(Animation.INFINITE);
    }

    public void initCube() {
        blackCube = new Cube(equipment, (CubeTable) cubeTableList.get(0), "블랙큐브");
        redCube = new Cube(equipment, (CubeTable) cubeTableList.get(1), "레드큐브");
        addiCube = new Cube(equipment, (CubeTable) cubeTableList.get(2), "에디셔널큐브");
        myungjangCube = new Cube(equipment, (CubeTable) cubeTableList.get(3), "명장의큐브");
        janginCube = new Cube(equipment, (CubeTable) cubeTableList.get(4), "장인의큐브");
        strangeAddiCube = new Cube(equipment, (CubeTable) cubeTableList.get(2), "수상한에디셔널큐브");
    }

    /*
    public void initSpinner() {
        Spinner event_spinner = (Spinner) findViewById(R.id.option);
        ArrayAdapter<CharSequence> adapter;

        adapter = ArrayAdapter.createFromResource(this,
                R.array.potential_list, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        event_spinner.setAdapter(adapter);
        event_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                autoOption = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }*/

    public void goRewardAd(View view) {

        CustomDialog customDialog = new CustomDialog(this, new CustomDialogClickListener() {
            @Override
            public void onPositiveClick() {
                if (mRewardedAd != null) {
                    Activity activityContext = PotentialActivity.this;
                    mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            //미라클타임 활성화
                            blackCube.setMiracle();
                            redCube.setMiracle();
                            addiCube.setMiracle();
                            myungjangCube.setMiracle();
                            janginCube.setMiracle();
                            strangeAddiCube.setMiracle();

                            TextView textView = findViewById(R.id.potentialTitle);
                            textView.setText("잠재능력(미라클타임)");

                            CustomNotice customNotice = new CustomNotice(activityContext);
                            customNotice.show();
                            customNotice.setContent("감사합니다. 미라클 타임이 적용되었습니다.");
                        }
                    });
                } else {
                    CustomNotice customNotice = new CustomNotice(PotentialActivity.this);
                    customNotice.show();
                    customNotice.setContent("광고가 아직 준비되지 않았습니다ㅠㅠ\n 다음에 다시 시도해주세요.");
                }
            }
            @Override
            public void onNegativeClick() {
            }
        });
        customDialog.show();
        customDialog.setMessage("광고를 보고 미라클 타임을 켜시겠습니까?\n(등급업 확률 2배)");
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

    public void infoPopup(View view){
        Intent intent = new Intent(this, InfoActivity.class);
        intent.putExtra("equipment", equipment);
        startActivityForResult(intent, 1);
    }

    public void updateText() {
        if(equipment == null) return;

        TextView textView = findViewById(R.id.info);
        String equipInfo = "";

        equipInfo = equipInfo + EquipmentInfo.potential(equipment);

        textView.setText(Html.fromHtml(equipInfo));
    }

    public void useCube(View view) {
        CheckBox autoCheck = findViewById(R.id.auto);
        String cube = "";

        if(selected_button_id == R.id.button0){ //블랙큐브
            cube = "블랙큐브";
        }
        else if(selected_button_id == R.id.button1){ //레드큐브
            cube = "레드큐브";
        }
        else if(selected_button_id == R.id.button2){ //에디셔널큐브
            cube = "에디셔널큐브";
        }
        else if(selected_button_id == R.id.button3){ //명장의큐브
            cube = "명장의큐브";
        }
        else if(selected_button_id == R.id.button4){ //장인의큐브
            cube = "장인의큐브";
        }
        else if(selected_button_id == R.id.button5){ //수에큐
            cube = "수상한에디셔널큐브";
        }
        else return;

        //오토모드
        if(autoCheck.isChecked()) {
            autoCube(cube, view);
            return;
        }

        view.setEnabled(false);
        usingCube(cube);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }
        }, 600);
    }

    //큐브들 오토 중 클릭 방지
    public void toggleButtonsEnable() {
        LinearLayout panel = findViewById(R.id.buttonPanel);
        for(int i=0; i<panel.getChildCount(); i++){
            View child = panel.getChildAt(i);
            if(child instanceof ViewGroup) {
                ImageButton button = (ImageButton) ((ViewGroup) child).getChildAt(0);
                if(button.isEnabled()) button.setEnabled(false);
                else button.setEnabled(true);
            }
        }
        Button backButton = findViewById(R.id.back);
        if(backButton.isEnabled()) backButton.setEnabled(false);
        else backButton.setEnabled(true);
    }

    public void goSelect(View view) {
        Intent intent = new Intent(this, SelectCubeAutoPopup.class);
        startActivityForResult(intent, 0);
    }

    public void autoCube(String cube, View view) {
        CheckBox autoCheck = findViewById(R.id.auto);
        //Spinner optionSpinner = findViewById(R.id.option);
        Button enhance = findViewById(R.id.enhance);
        toggleButtonsEnable();

        if(keepGoing){ //AUTO가 동작중인 경우
            keepGoing = false;
            if(view.getAnimation() != null) view.getAnimation().cancel();
            autoCheck.setEnabled(true);
            //optionSpinner.setEnabled(true);
            enhance.setText("사용하기");
            return;
        }
        else{ //AUTO가 동작중이지 않은 경우
            if(!isAutoKeep(cube)) {//이미 조건에 맞는 경우
                CustomDialog customDialog = new CustomDialog(this, new CustomDialogClickListener() {
                    @Override
                    public void onPositiveClick() {
                        usingCube(cube);
                    }
                    @Override
                    public void onNegativeClick() {
                    }
                });
                customDialog.show();
                customDialog.setMessage("이미 조건에 맞는 옵션입니다. 그래도 돌리시겠습니까?");
            }
            keepGoing = true;
            autoCheck.setEnabled(false);
            //optionSpinner.setEnabled(false);
            view.startAnimation(autoAnim);
            enhance.setText("멈추기");
        }

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(keepGoing) {
                    if(isAutoKeep(cube)){
                        usingCube(cube);
                        handler.postDelayed(this, 200);  // 0.2 second delay
                    }
                    else{
                        stopAuto(view);
                    }
                }
            }
        };
        handler.post(runnable);
    }

    public void stopAuto(View view) {
        CheckBox autoCheck = findViewById(R.id.auto);
        //Spinner optionSpinner = findViewById(R.id.option);
        Button enhance = findViewById(R.id.enhance);
        Animation animation = view.getAnimation();

        toggleButtonsEnable();
        keepGoing = false;
        if(animation == null) return;
        animation.cancel();
        enhance.setText("사용하기");
        autoCheck.setEnabled(true);
        //optionSpinner.setEnabled(true);
    }

    //계속 진행할지 여부 반환 (조건에 맞는 옵션인지 판별
    public boolean isAutoKeep(String cube) {
        String option[];
        int attk = 0; //공격력
        int magic = 0; //마력
        int str = 0;
        int dex = 0;
        int intel = 0;
        int luk = 0;
        int ignore = 0; //방무
        int bossdmg = 0; //보공
        int cridmg = 0; //크뎀

        if(cube.equals("에디셔널큐브") || cube.equals("수상한에디셔널큐브")) option = equipment.getPotential2();
        else option = equipment.getPotential1();

        if(option==null || option.length<3) return true;

        for(int i=0; i<3; i++){
            System.out.println(option[i]);
            if(option[i].length()<3) return true;
            String tmp = option[i];
            if(tmp.contains("공격력")) attk++;
            else if(tmp.contains("마력")) magic++;
            else if(tmp.contains("STR")) str++;
            else if(tmp.contains("DEX")) dex++;
            else if(tmp.contains("INT")) intel++;
            else if(tmp.contains("LUK")) luk++;
            else if(tmp.contains("올스탯")) {str++; dex++; intel++; luk++;}
            else if(tmp.contains("보스 몬스터")) bossdmg++;
            else if(tmp.contains("몬스터 방어율")) ignore++;
            else if(tmp.contains("크리티컬 데미지")) cridmg++;
        }

        if(autoOption==0 && str==3){
            return false;
        }
        else if(autoOption==1 && dex==3){
            return false;
        }
        else if(autoOption==2 && intel>=3){
            return false;
        }
        else if(autoOption==3 && luk>=3){
            return false;
        }
        else if(autoOption==4 && attk==3){
            return false;
        }
        else if(autoOption==5 && magic==3){
            return false;
        }
        else if(autoOption==6 && bossdmg==2 && attk==1) { //보보공
            return false;
        }
        else if(autoOption==7 && bossdmg==2 && magic==1) { //보보마
            return false;
        }
        else if(autoOption==8 && bossdmg==2 && ignore==1) { //보보방
            return false;
        }
        else if(autoOption==9 && bossdmg==1 && attk==2) { //보공공
            return false;
        }
        else if(autoOption==10 && bossdmg==1 && magic==2) { //보마마
            return false;
        }
        else if(autoOption==11 && cridmg==3) { //크크크(크뎀)
            return false;
        }

        return true;
    }

    public void usingCube(String name) {
        if(name.equals("블랙큐브")) {
            blackCube.useCube();
        }
        else if(name.equals("레드큐브")) {
            redCube.useCube();
        }
        else if(name.equals("에디셔널큐브")) {
            addiCube.useAddiCube();
        }
        else if(name.equals("명장의큐브")) {
            myungjangCube.useCube();
        }
        else if(name.equals("장인의큐브")) {
            janginCube.useCube();
        }
        else if(name.equals("수상한에디셔널큐브")) {
            strangeAddiCube.useAddiCube();
        }
        else {
            return;
        }

        updateText();
        sparkleEffect();
        PreferenceManager.setInventory(this, inventory);
    }

    //반짝 효과
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

        Animation anim = new AlphaAnimation(0.0f, 0.4f);
        anim.setDuration(400);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(1);

        sparkle.startAnimation(anim);
        anim.setAnimationListener(listener);
    }
    
    //DB 읽어서 List에 추가
    private void initLoadCubeDB() {
        CubeDataAdapter mDbHelper = new CubeDataAdapter(getApplicationContext());
        mDbHelper.createDatabase();
        mDbHelper.open();

        // db에 있는 값들을 model을 적용해서 넣는다.
        this.cubeTableList = mDbHelper.getTableData();
        System.out.println("init DB");

        // db 닫기
        mDbHelper.close();
    }

    //N번 아이템 버튼 이벤트
    public void select(View view) {

        //이전에 선택된 항목의 체크 제거
        ImageView prev_select = (ImageView)findViewById(selected_check_id);
        if(prev_select != null) prev_select.setVisibility(View.INVISIBLE);
        selected_button_id = -1;
        selected_check_id = -1;

        //이미 선택된 버튼 클릭한 경우
        if(selected_button_id == view.getId()) {
            return;
        }

        //선택한 번호의 아이템에 체크 표시
        if(view.getId() == R.id.button0){
            selected_check_id = R.id.check0;
        }
        else if(view.getId() == R.id.button1){
            selected_check_id = R.id.check1;
        }
        else if(view.getId() == R.id.button2){
            selected_check_id = R.id.check2;
        }
        else if(view.getId() == R.id.button3){
            selected_check_id = R.id.check3;
        }
        else if(view.getId() == R.id.button4){
            if(equipment.getPotentialGrade1().equals("레전드리")){
                Toast.makeText(this, "유니크 등급까지만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            selected_check_id = R.id.check4;
        }
        else if(view.getId() == R.id.button5){
            if(!equipment.getPotentialGrade2().equals("레어")){
                Toast.makeText(this, "레어 등급에만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            selected_check_id = R.id.check5;
        }
        else{
            return;
        }

        selected_button_id = view.getId();
        ImageView new_select = (ImageView)findViewById(selected_check_id);
        new_select.setVisibility(View.VISIBLE);
    }

    public void goHelp(View view) {
        CustomNotice notice = new CustomNotice(this);
        notice.show();
        notice.setTitle("도움말");
        notice.setContent("이곳에서 잠재능력을 재설정 할 수 있습니다. \n" +
                "1. 하단에서 원하는 큐브를 선택 후, \"강화하기\"를 누르면 잠재능력의 재설정이 시작됩니다.\n" +
                "2. AUTO 체크 시 특정 옵션이 나올때까지 큐브가 사용됩니다. " +
                "3. 이미 조건을 만족하는 경우 AUTO모드가 작동되지 않습니다." +
                "(AUTO 모드를 해제하여 큐브를 돌린 후 시작하면 됩니다.)\n" +
                "4. 선물상자를 누르면 미라클 타임이 적용됩니다.");
    }

    public void goBack(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        keepGoing = false;
        Intent intent = new Intent();
        intent.putExtra("inventory", inventory);
        intent.putExtra("now", now);
        setResult(1, intent);
        finish();
        super.onBackPressed();
    }

    //intent 받는 부분
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 0:
                if(data!=null) {
                    this.autoOption = data.getIntExtra("option", 0);
                    setOptionText();
                }
                break;
        }
    }

    public void setOptionText() {
        TextView textView = findViewById(R.id.autoOption);
        String optionTable[] = new String[]{"STR 3줄", "DEX 3줄", "INT 3줄", "LUK 3줄",
                "공격력 3줄", "마력 3줄", "보보공", "보보마", "보보방", "보공공", "보마마", "크크크(크뎀)"};

        textView.setText(optionTable[this.autoOption]+" ⚙️");
    }
}


