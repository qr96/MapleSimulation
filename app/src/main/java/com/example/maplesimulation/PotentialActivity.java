package com.example.maplesimulation;

import android.app.Activity;
import android.content.Intent;
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

import java.util.List;
import java.util.Set;

public class PotentialActivity extends Activity {
    public int selected_button_id = -1; //선택된 아이템의 id
    public int selected_check_id = -1; //선택된 아이템의 체크 id

    public List cubeTableList; //[블랙큐브, 레드큐브, 에디셔널큐브, 명장의큐브, 장인의큐브]
    public Equipment equipment;
    public Cube blackCube;
    public Cube redCube;
    public Cube addiCube;

    private AdView mAdView;

    Animation autoAnim;

    //auto모드 계속 돌릴지 여부
    boolean keepGoing = false;
    
    //auto모드, 원하는 옵션 번호
    int autoOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_potential);

        Intent intent = getIntent();
        this.equipment = (Equipment) intent.getSerializableExtra("equipment");

        //큐브 DB 초기화
        initLoadCubeDB();

        updateText();
        setThumnail();

        initAutoAni();
        initCube();
        initSpinner();

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

    public void initAutoAni() {
        autoAnim = new AlphaAnimation(0.4f, 1.0f);
        autoAnim.setDuration(400);
        autoAnim.setRepeatMode(Animation.REVERSE);
        autoAnim.setRepeatCount(Animation.INFINITE);
    }

    public void initCube() {
        blackCube = new Cube(this.equipment, (CubeTable) cubeTableList.get(0));
        redCube = new Cube(this.equipment, (CubeTable) cubeTableList.get(1));
        addiCube = new Cube(this.equipment, (CubeTable) cubeTableList.get(2));
    }

    public void initSpinner() {
        Spinner event_spinner = (Spinner) findViewById(R.id.option);
        ArrayAdapter<CharSequence> adapter;
        if(equipment.isWeapon()){
            adapter = ArrayAdapter.createFromResource(this,
                    R.array.weapon_potential_list, android.R.layout.simple_spinner_item);
        }
        else {
            adapter = ArrayAdapter.createFromResource(this,
                    R.array.armor_potential_list, android.R.layout.simple_spinner_item);
        }

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

    public void infoPopup(View view){
        Intent intent = new Intent(this, EquipmentPopup.class);
        intent.putExtra("equipment", this.equipment);
        startActivityForResult(intent, 1);
    }

    public void updateText() {
        if(this.equipment == null) return;

        TextView textView = findViewById(R.id.info);
        String equipInfo = EquipmentInfo.potential(this.equipment);

        textView.setText(Html.fromHtml(equipInfo));
    }

    public void useCube(View view) {
        CheckBox autoCheck = findViewById(R.id.auto);
        String cube = "";

        if(selected_button_id == R.id.button0){
            //오토모드
            if(autoCheck.isChecked()) {
                autoCube("black", view);
                return;
            }
            cube = "black";
        }
        else if(selected_button_id == R.id.button1){
            //오토모드
            if(autoCheck.isChecked()) {
                autoCube("red", view);
                return;
            }
            cube = "red";
        }
        else if(selected_button_id == R.id.button2){
            //오토모드
            if(autoCheck.isChecked()) {
                autoCube("additional", view);
                return;
            }
            cube = "additional";
        }
        else return;

        view.setEnabled(false);
        usingCube(cube);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }
        }, 600);
    }

    public void autoCube(String cube, View view) {
        CheckBox autoCheck = findViewById(R.id.auto);
        Spinner optionSpinner = findViewById(R.id.option);

        if(keepGoing){
            keepGoing = false;
            if(view.getAnimation() != null) view.getAnimation().cancel();
            autoCheck.setEnabled(true);
            optionSpinner.setEnabled(true);
        }
        else{
            keepGoing = true;
            autoCheck.setEnabled(false);
            optionSpinner.setEnabled(false);
            view.startAnimation(autoAnim);
        }

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(keepGoing) {
                    if(isAutoKeep(cube)){
                        usingCube(cube);
                        handler.postDelayed(this, 200);  // 1 second delay
                    }
                    else{
                        keepGoing = false;
                        view.getAnimation().cancel();
                        autoCheck.setEnabled(true);
                        optionSpinner.setEnabled(true);
                    }
                }
            }
        };
        handler.post(runnable);
    }

    //자동모드 계속할지 여부
    public boolean isAutoKeep(String cube) {
        String option[];
        int attk = 0;
        int magic = 0;
        int str = 0;
        int dex = 0;
        int intel = 0;
        int luk = 0;

        if(cube.equals("additional")) option = equipment.getPotential2();
        else option = equipment.getPotential1();

        if(option==null || option.length<3) return true;

        for(int i=0; i<3; i++){
            System.out.println(option[i]);
            if(option[i].length()<3) return true;
            String tmp = option[i].substring(0, 3);
            if(tmp.equals("공격력")) attk++;
            else if(tmp.equals("마력")) magic++;
            else if(tmp.equals("STR") || option[i].equals("캐릭터 기준 10레벨 당 STR : +2") || option[i].equals("캐릭터 기준 10레벨 당 STR : +1")) str++;
            else if(tmp.equals("DEX") || option[i].equals("캐릭터 기준 10레벨 당 DEX : +2") || option[i].equals("캐릭터 기준 10레벨 당 DEX : +1")) dex++;
            else if(tmp.equals("INT") || option[i].equals("캐릭터 기준 10레벨 당 INT : +2") || option[i].equals("캐릭터 기준 10레벨 당 INT : +1")) intel++;
            else if(tmp.equals("LUK") || option[i].equals("캐릭터 기준 10레벨 당 LUK : +2") || option[i].equals("캐릭터 기준 10레벨 당 LUK : +1")) luk++;
            else if(tmp.equals("올스텟")) {str++; dex++; intel++; luk++;}
        }

        if(equipment.isWeapon()){
            if(autoOption==0 && attk==3){
                return false;
            }
            else if(autoOption==1 && magic==3){
                return false;
            }
            else if(autoOption==2 && attk>=2){
                return false;
            }
            else if(autoOption==3 && magic>=2){
                return false;
            }
        }
        else {
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
        }

        return true;
    }

    public void usingCube(String name) {
        if(name.equals("black")) {
            blackCube.useBlackCube();
        }
        else if(name.equals("red")) {
            redCube.useRedCube();
        }
        else if(name.equals("additional")) {
            addiCube.useAddiCube();
        }
        else {
            return;
        }

        updateText();
        sparkleEffect();
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

        Animation anim = new AlphaAnimation(0.0f, 0.6f);
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

        //이미 선택된 버튼 클릭한 경우
        if(selected_button_id == view.getId()) {
            selected_button_id = -1;
            selected_check_id = -1;

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
            selected_check_id = R.id.check4;
        }
        else{
            return;
        }

        selected_button_id = view.getId();
        ImageView new_select = (ImageView)findViewById(selected_check_id);
        new_select.setVisibility(View.VISIBLE);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("equip", this.equipment);
        setResult(1, intent);
        finish();
        super.onBackPressed();
    }

}

