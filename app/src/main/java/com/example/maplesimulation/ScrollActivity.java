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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;

public class ScrollActivity extends Activity {
    public int selected_button_id = -1; //선택된 아이템의 id
    public int selected_check_id = -1; //선택된 아이템의 체크 id
    public int selected_detail_id = -1; //선택된 아이템의 세부 옵션 id  ex) 70%, 30% 주문서

    private ArrayList<Equipment> inventory; //인벤토리 배열
    private Equipment equipment;
    private int now; //현재 강화중인 장비의 인덱스

    public Scroll scroll;
    public Flame flame;
    public Noljang noljang;

    public AnimationDrawable resultAnimation;

    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);

        Intent intent = getIntent();
        inventory = (ArrayList<Equipment>) intent.getSerializableExtra("inventory");
        now = (int) intent.getSerializableExtra("now");
        equipment = inventory.get(now);

        this.scroll = new Scroll(equipment);
        this.flame = new Flame(equipment);
        this.noljang = new Noljang(equipment);

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

    public void infoPopup(View view){
        Intent intent = new Intent(this, InfoActivity.class);
        intent.putExtra("equipment", equipment);
        startActivityForResult(intent, 1);
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

    //강화 실행 버튼
    public void runEnhance(View view) {
        int result = -2;
        int possibility = 0;
        String justat = "none";
        if(selected_button_id == -1) return;
        
        //주문의 흔적
        if(selected_button_id == R.id.scroll_button_0) {
            if(equipment.getType().equals("장갑")){ //장갑
                //공,마  100, 70, 30
                if(selected_detail_id%3 == 0) possibility = 100;
                else if(selected_detail_id%3 == 1) possibility = 70;
                else if(selected_detail_id%3 == 2) possibility = 30;

                if(selected_detail_id/3 == 0) justat = "physic";
                else if(selected_detail_id/3 == 1) justat = "magic";

                result = scroll.doGloveScroll2(possibility, justat);
            }
            else if(equipment.isArmor()) { //방어구인 경우
                //힘,덱,인,럭  100, 70, 30
                if(selected_detail_id%3 == 0) possibility = 100;
                else if(selected_detail_id%3 == 1) possibility = 70;
                else if(selected_detail_id%3 == 2) possibility = 30;

                if(selected_detail_id/3 == 0) justat = "STR";
                else if(selected_detail_id/3 == 1) justat = "DEX";
                else if(selected_detail_id/3 == 2) justat = "INT";
                else if(selected_detail_id/3 == 3) justat = "LUK";
                else if(selected_detail_id/3 == 4) justat = "HP";

                result = scroll.doArmorScroll2(possibility, justat);
            }
            else if(equipment.isAccessary()) { //장신구인 경우
                //힘,덱,인,럭  100, 70, 30
                if(selected_detail_id%3 == 0) possibility = 100;
                else if(selected_detail_id%3 == 1) possibility = 70;
                else if(selected_detail_id%3 == 2) possibility = 30;

                if(selected_detail_id/3 == 0) justat = "STR";
                else if(selected_detail_id/3 == 1) justat = "DEX";
                else if(selected_detail_id/3 == 2) justat = "INT";
                else if(selected_detail_id/3 == 3) justat = "LUK";
                else if(selected_detail_id/3 == 4) justat = "HP";

                if(equipment.getLevReq()>=120 && equipment.getLevReq()<=200){ //120~200
                    result = scroll.doAccessaryScroll2(possibility, justat);
                }
                else if(equipment.getLevReq()>=75 && equipment.getLevReq()<=120) { //75~110
                    result = scroll.doAccessaryScroll1(possibility, justat);
                }
            }
            else if(equipment.isWeapon()){ //무기인 경우
                //힘,덱,인,럭  100, 70, 30, 15
                if(selected_detail_id%4 == 0) possibility = 100;
                else if(selected_detail_id%4 == 1) possibility = 70;
                else if(selected_detail_id%4 == 2) possibility = 30;
                else if(selected_detail_id%4 == 3) possibility = 15;

                if(selected_detail_id/4 == 0) justat = "STR";
                else if(selected_detail_id/4 == 1) justat = "DEX";
                else if(selected_detail_id/4 == 2) justat = "INT";
                else if(selected_detail_id/4 == 3) justat = "LUK";

                result = scroll.doWeaponScroll2(possibility, justat);
            }
        }
        //순백의 주문서
        else if(selected_button_id == R.id.scroll_button_1) {
            //100%
            if(selected_detail_id == 0) {
                possibility = 100;
            }
            else if(selected_detail_id == 1) {
                possibility = 10;
            }
            else if(selected_detail_id == 2) {
                possibility = 5;
            }
            result = scroll.doWhiteScroll(possibility);
        }
        //황금 망치
        else if(selected_button_id == R.id.scroll_button_2) {
            //100%
            if(selected_detail_id == 0) {
                result = scroll.useGoldHammer(100);
            }
            else if(selected_detail_id == 1) {
                result = scroll.useGoldHammer(50);
            }
        }
        //이노센트 주문서
        else if(selected_button_id == R.id.scroll_button_3) {
            if((selected_detail_id == 3 || selected_detail_id == 4) && equipment.isNoljang) {
                CustomNotice notice = new CustomNotice(this);
                notice.show();
                notice.setTitle("안내");
                notice.setContent("놀라운 장비강화 주문서가 적용된 아이템은 아크이노센트 주문서를 사용할 수 없습니다.");
                return;
            }

            //100%
            if(selected_detail_id == 0) {
                result = scroll.useInnocent(100);
            }
            else if(selected_detail_id == 1) {
                result = scroll.useInnocent((50));
            }
            else if(selected_detail_id == 2) {
                result = scroll.useInnocent((30));
            }
            else if(selected_detail_id == 3) {
                result = scroll.useArkInnocent(100);
            }
            else if(selected_detail_id == 4) {
                result = scroll.useArkInnocent(30);
            }
        }
        //혼돈의 주문서
        else if(selected_button_id == R.id.scroll_button_4) {
            //100%
            if(selected_detail_id == 0) {
                result = scroll.useAwesomeChaos(100);
            }
            //70%
            else if(selected_detail_id == 1) {
                result = scroll.useAwesomeChaos(70);
            }
            //100% 리턴 스크롤 적용
            else  if(selected_detail_id == 2) {
                result = scroll.useAwesomeChaos(100);
                returnScrollDialog();
            }
            //70% 리턴 스크롤 적용
            else if(selected_detail_id == 3) {
                result = scroll.useAwesomeChaos(70);
                if(result == 1) returnScrollDialog();
            }

        }
        else if(selected_button_id == R.id.scroll_button_5) { //영환불
            flame.useEternalFlame();
            result = 2;
        }
        else if(selected_button_id == R.id.scroll_button_6) { //강환불
            flame.usePowerfulFlame();
            result = 2;
        }
        else if(selected_button_id == R.id.scroll_button_7) { //놀장강
            if(equipment.getLevReq()>150 || equipment.isStarforce || equipment.getStar()==15){
                Toast.makeText(this, "주문서를 사용할 수 없는 아이템 입니다", Toast.LENGTH_SHORT).show();
                return;
            }
            result = noljang.useNoljang();
            if(result == 0) Toast.makeText(this, "프로텍트 실드가 사용되었습니다", Toast.LENGTH_SHORT).show();
        }
        else if(selected_button_id == R.id.scroll_button_8) { //매지컬
            result = scroll.useMagicalScroll(selected_detail_id/2);
            if(selected_detail_id%2 == 1) { //리턴스크롤
                returnScrollDialog();
            }
        }

        if(result==1) successEffect();
        else if(result==0) failEffect();
        else if(result==2); //환생의 불꽃 사용 시 이펙트 없음
        else return;

        sparkleEffect();
        updateText();
        setEquipName();
        view.setEnabled(false);
        PreferenceManager.setInventory(this, inventory);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }


        }, 600);
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

        Animation anim = new AlphaAnimation(0.0f, 0.6f);
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
        if(equipment.getNowUp()>0)
            textView.setText(equipment.getName()+" (+"+equipment.getNowUp()+")");
        else textView.setText(equipment.getName());
    }
    
    //장비의 능력치 표시 업데이트
    public void updateText() {
        if(equipment == null) return;

        TextView textView = findViewById(R.id.info);
        String equipInfo = EquipmentInfo.makeText(equipment);

        textView.setText(Html.fromHtml(equipInfo));
    }

    //리턴스크롤 다이얼로그
    private void returnScrollDialog() {
        CustomDialog customDialog = new CustomDialog(this, new CustomDialogClickListener() {
            @Override
            public void onPositiveClick() {
                scroll.useReturnScroll();
                updateText();
                setEquipName();
            }
            @Override
            public void onNegativeClick() {
            }
        });
        customDialog.setCancelable(false);
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.show();
        customDialog.setMessage("[리턴스크롤]\n주문서 사용에 성공했습니다. 아이템의 옵션을 되돌리시겠습니까?\n"+recentChoas());
    }

    //혼돈의 주문서로 증가한 능력치 문자열 반환
    public String recentChoas(){
        ArrayList<Integer> recent = scroll.getRecentChaos();
        String info = "";
        String[] table = {"STR", "DEX", "INT", "LUK", "최대HP", "최대MP", "착용레벨감소",
                "방어력", "공격력", "마력", "이동속도", "점프력", "올스텟%",
                "보스데미지%", "데미지%", "최대HP%", "방어율무시%"};

        for(int i=0; i<recent.size();i++){
            if((Integer) recent.get(i)!=0){ //수치가 변했다면
                info = info+table[i]+":+"+recent.get(i)+"  ";
            }
        }

        return info;
    }

    public void goBack(View view) {
        onBackPressed();
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
        if(view.getId() == R.id.scroll_button_0){//주문의 흔적
            if(equipment.getType().equals("포켓아이템")) {
                Toast.makeText(this, "사용할 수 없는 장비입니다.", Toast.LENGTH_SHORT).show();
                selected_button_id = -1;
                return;
            }
            
            selected_check_id = R.id.scroll_check_0;

            //주문서의 세부 옵션 선택 팝업
            Intent intent = new Intent(this, SelectBasicScollPopup.class);
            String scrollType = "";
            //장갑인 경우
            if(equipment.getType().equals("장갑")){
                scrollType = "glove";
            }
            //방어구, 장신구인 경우
            else if(equipment.isArmor() || equipment.isAccessary()){
                scrollType = "armors";
            }
            //무기인 경우
            else if(equipment.isWeapon() || equipment.getType().equals("블레이드")){
                scrollType = "weapons";
            }

            intent.putExtra("type", scrollType);
            intent.putExtra("scroll", 0);
            startActivityForResult(intent, 0);
        }
        else if(view.getId() == R.id.scroll_button_1){//순백의 주문서
            if(equipment.getType().equals("포켓아이템")) {
                Toast.makeText(this, "사용할 수 없는 장비입니다.", Toast.LENGTH_SHORT).show();
                selected_button_id = -1;
                return;
            }

            selected_check_id = R.id.scroll_check_1;

            //주문서의 세부 옵션 선택 팝업
            Intent intent = new Intent(this, SelectBasicScollPopup.class);
            intent.putExtra("scroll", 1);
            startActivityForResult(intent, 0);

        }
        else if(view.getId() == R.id.scroll_button_2){//황금 망치
            if(equipment.getType().equals("포켓아이템")) {
                Toast.makeText(this, "사용할 수 없는 장비입니다.", Toast.LENGTH_SHORT).show();
                selected_button_id = -1;
                return;
            }

            selected_check_id = R.id.scroll_check_2;

            //주문서의 세부 옵션 선택 팝업
            Intent intent = new Intent(this, SelectBasicScollPopup.class);
            intent.putExtra("scroll", 2);
            startActivityForResult(intent, 0);
        }
        else if(view.getId() == R.id.scroll_button_3){//이노센트 주문서
            if(equipment.getType().equals("포켓아이템")) {
                Toast.makeText(this, "사용할 수 없는 장비입니다.", Toast.LENGTH_SHORT).show();
                selected_button_id = -1;
                return;
            }

            selected_check_id = R.id.scroll_check_3;

            //주문서의 세부 옵션 선택 팝업
            Intent intent = new Intent(this, SelectBasicScollPopup.class);
            intent.putExtra("scroll", 3);
            startActivityForResult(intent, 0);
        }
        else if(view.getId() == R.id.scroll_button_4){//혼돈의 주문서
            if(equipment.getType().equals("포켓아이템")) {
                Toast.makeText(this, "사용할 수 없는 장비입니다.", Toast.LENGTH_SHORT).show();
                selected_button_id = -1;
                return;
            }

            selected_check_id = R.id.scroll_check_4;

            //주문서의 세부 옵션 선택 팝업
            Intent intent = new Intent(this, SelectBasicScollPopup.class);
            intent.putExtra("scroll", 4);
            startActivityForResult(intent, 0);
        }
        else if(view.getId() == R.id.scroll_button_5){//영환불
            CustomNotice customNotice = new CustomNotice(this);
            customNotice.setCanceledOnTouchOutside(false);
            customNotice.show();
            customNotice.setTitle("영원한 환생의 불꽃");

            if(equipment.getType().equals("반지") || equipment.getType().equals("기계심장") ||
                    equipment.getType().equals("어깨장식") || equipment.getType().equals("블레이드")) {
                customNotice.setContent("사용 불가능한 장비입니다.");
                return;
            }

            customNotice.setContent("최고 수준까지 추가옵션을 부여합니다.");
            selected_check_id = R.id.scroll_check_5;
        }
        else if(view.getId() == R.id.scroll_button_6){//강환불
            CustomNotice customNotice = new CustomNotice(this);
            customNotice.setCanceledOnTouchOutside(false);
            customNotice.show();
            customNotice.setTitle("강력한 환생의 불꽃");

            if(equipment.getType().equals("반지") || equipment.getType().equals("기계심장") ||
                    equipment.getType().equals("어깨장식") || equipment.getType().equals("블레이드")) {
                customNotice.setContent("사용할 수 없는 장비입니다.");
                return;
            }

            customNotice.setContent("높은 수준까지 추가옵션을 부여합니다.");
            selected_check_id = R.id.scroll_check_6;
        }
        else if(view.getId() == R.id.scroll_button_7) {
            CustomNotice customNotice = new CustomNotice(this);
            customNotice.setCanceledOnTouchOutside(false);
            customNotice.show();
            customNotice.setTitle("놀라운 장비강화 주문서");

            //놀장
            if(equipment.getName().contains("타일런트") || equipment.getLevReq()>150 || equipment.isStarforce){
                customNotice.setContent("사용할 수 없는 장비입니다.");
                return;
            }

            customNotice.setContent("놀라운 장비강화 주문서입니다. 사용 시 스타포스 강화가 불가능하며 최대 15성으로 제한됩니다.\n" +
                    "[성공확률]\n" +
                    "1성:60% 2성:55% 3성:50% 4성:40% 5성:30% 6성:20% 7성:19% 8성:18% 9성:17% 10성:16% 11성:14% 12성:12% 13성 이상10%");

            selected_check_id = R.id.scroll_check_7;
        }
        else if(view.getId() == R.id.scroll_button_8) {
            //매지컬 무기 주문서
            if(equipment.isWeapon() || equipment.getType().equals("기계심장")){
                selected_check_id = R.id.scroll_check_8;

                //주문서의 세부 옵션 선택 팝업
                Intent intent = new Intent(this, SelectBasicScollPopup.class);
                intent.putExtra("scroll", 8);
                startActivityForResult(intent, 0);
            }
            else {
                CustomNotice customNotice = new CustomNotice(this);
                customNotice.setCanceledOnTouchOutside(false);
                customNotice.show();
                customNotice.setTitle("매지컬 주문서");
                customNotice.setContent("아직 무기와 기계심장만 가능합니다.");
                return;
            }
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
        notice.setContent("이곳에서 주문서 강화를 할 수 있습니다. \n1. 상단에는 현재 강화중인 장비가 표시됩니다.\n" +
                "2. 중앙에는 장비의 스텟이 표시 됩니다.\n" +
                "3. 하단에서 원하는 주문서를 선택 후, \"강화하기\"를 누르면 강화가 시작됩니다.\n" +
                "4. 장비를 클릭하면 장비의 세부적인 내용을 볼 수 있습니다.");
    }

    //주문서 세부사항 선택 후, 데이터 받는 부분
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 0: //주문의 흔적
                if(data!=null) {
                    int selected = data.getIntExtra("scroll", -1);
                    this.selected_detail_id = selected;
                }
                else { //받은 데이터가 없는 경우
                    ImageView new_select = (ImageView)findViewById(selected_check_id);
                    new_select.setVisibility(View.INVISIBLE);
                    selected_button_id = -1;
                    selected_check_id = -1;
                }
                break;
        }
    }
}
