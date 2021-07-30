package com.example.maplesimulation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.util.ArrayList;
import java.util.Arrays;

public class ScrollActivity extends Activity {
    public int selected_button_id = -1; //선택된 아이템의 id
    public int selected_check_id = -1; //선택된 아이템의 체크 id
    public int selected_detail_id = -1; //선택된 아이템의 세부 옵션 id  ex) 70%, 30% 주문서
    public Equipment equipment;

    ArrayList<String> armors; //방어구 목록
    ArrayList<String> accessories; //장신구 목록
    ArrayList<String> weapons; //무기 목록

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);

        Intent intent = getIntent();
        this.equipment = (Equipment) intent.getSerializableExtra("equipment");

        setThumnail();
        updateText();
        initEquipType();

    }

    public void initEquipType(){
        armors = new ArrayList<>(Arrays.asList("상의","하의","모자","망토","신발","한벌옷"));
        accessories = new ArrayList<>(Arrays.asList("얼굴장식","눈장식","벨트","귀고리","견장","반지","목걸이"));
    }

    //성공 이펙트 실행
    public void successEffect() {
        ImageView effect = (ImageView) findViewById(R.id.resultEffect);
        effect.setImageResource(R.drawable.effect_success);
        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(effect, 1);
        Glide.with(this).load(R.drawable.effect_success).into(gifImage);
    }
    
    //실패 이펙트 실행
    public void failEffect() {
        ImageView effect = (ImageView) findViewById(R.id.resultEffect);
        effect.setImageResource(R.drawable.effect_failed);
        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(effect, 1);
        Glide.with(this).load(R.drawable.effect_failed).into(gifImage);
    }

    //강화 실행 버튼
    public void runEnhance(View view) {
        int result = -2;
        int possibility = 0;
        String justat = "none";
        
        //주문의 흔적
        if(selected_button_id == R.id.scroll_button_0) {

            if(armors.contains(this.equipment.getType())) { //방어구인 경우
                //힘,덱,인,럭  100, 70, 30
                if(selected_detail_id%3 == 0) possibility = 100;
                else if(selected_detail_id%3 == 1) possibility = 70;
                else if(selected_detail_id%3 == 2) possibility = 30;

                if(selected_detail_id/3 == 0) justat = "STR";
                else if(selected_detail_id/3 == 1) justat = "DEX";
                else if(selected_detail_id/3 == 2) justat = "INT";
                else if(selected_detail_id/3 == 3) justat = "LUK";

                result = this.equipment.doArmorScroll2(possibility, justat);
            }
            else if(accessories.contains(this.equipment.getType())) { //장신구인 경우
                //힘,덱,인,럭  100, 70, 30
                if(selected_detail_id%3 == 0) possibility = 100;
                else if(selected_detail_id%3 == 1) possibility = 70;
                else if(selected_detail_id%3 == 2) possibility = 30;

                if(selected_detail_id/3 == 0) justat = "STR";
                else if(selected_detail_id/3 == 1) justat = "DEX";
                else if(selected_detail_id/3 == 2) justat = "INT";
                else if(selected_detail_id/3 == 3) justat = "LUK";

                if(this.equipment.getLevReq()>=120 && this.equipment.getLevReq()<=200){ //120~200
                    result = this.equipment.doAccessaryScroll2(possibility, justat);
                }
                else if(this.equipment.getLevReq()>=75 && this.equipment.getLevReq()<=120) { //75~110
                    result = this.equipment.doAccessaryScroll1(possibility, justat);
                }
            }
            else if(this.equipment.getType().equals("무기")) { //무기인 경우
                //힘,덱,인,럭  100, 70, 30, 15
                if(selected_detail_id%4 == 0) possibility = 100;
                else if(selected_detail_id%4 == 1) possibility = 70;
                else if(selected_detail_id%4 == 2) possibility = 30;
                else if(selected_detail_id%4 == 3) possibility = 15;

                if(selected_detail_id/4 == 0) justat = "STR";
                else if(selected_detail_id/4 == 1) justat = "DEX";
                else if(selected_detail_id/4 == 2) justat = "INT";
                else if(selected_detail_id/4 == 3) justat = "LUK";

                result = this.equipment.doWeaponScroll2(possibility, justat);
            }
            else if(this.equipment.getType().equals("장갑")){ //장갑
                //공,마  100, 70, 30
                if(selected_detail_id%3 == 0) possibility = 100;
                else if(selected_detail_id%3 == 1) possibility = 70;
                else if(selected_detail_id%3 == 2) possibility = 30;

                if(selected_detail_id/3 == 0) justat = "physic";
                else if(selected_detail_id/3 == 1) justat = "magic";

                result = this.equipment.doGloveScroll2(possibility, "physic");
                result = this.equipment.doGloveScroll2(possibility, "magic");
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
            result = this.equipment.doWhiteScroll(possibility);
        }
        //황금 망치
        else if(selected_button_id == R.id.scroll_button_2) {
            //100%
            if(selected_detail_id == 0) {
                result = this.equipment.useGoldHammer(100);
            }
            else if(selected_detail_id == 1) {
                result = this.equipment.useGoldHammer(50);
            }
        }
        //이노센트 주문서
        else if(selected_button_id == R.id.scroll_button_3) {
            //100%
            if(selected_detail_id == 0) {
                result = this.equipment.useInnocent(100);
            }
            else if(selected_detail_id == 1) {
                result = this.equipment.useInnocent((50));
            }
            else if(selected_detail_id == 2) {
                result = this.equipment.useInnocent((30));
            }
        }
        //혼돈의 주문서
        else if(selected_button_id == R.id.scroll_button_4) {
            //100%
            if(selected_detail_id == 0) {
                result = equipment.useAwesomeChaos(100);
            }
            //70%
            else if(selected_detail_id == 1) {
                result = equipment.useAwesomeChaos(70);
            }
            //100% 리턴 스크롤 적용
            else  if(selected_detail_id == 2) {
                result = equipment.useAwesomeChaos(100);
                returnScrollDialog();
            }
            //70% 리턴 스크롤 적용
            else if(selected_detail_id == 3) {
                result = equipment.useAwesomeChaos(70);
                if(result == 1) returnScrollDialog();
            }

        }

        if(result==1) successEffect();
        else if(result==0) failEffect();
        else return;

        updateText();
        setEquipName();
        view.setEnabled(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }


        }, 1000);
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
        String equipInfo = makeText();

        textView.setText(Html.fromHtml(equipInfo));
    }

    //장비의 정보 텍스트로 변환
    public String makeText() {
        String equipInfo = "";
        String[] table = {"STR", "DEX", "INT", "LUK", "최대HP", "최대MP", "착용레벨감소",
                "방어력", "공격력", "마력", "이동속도", "점프력", "올스텟%",
                "보스데미지%", "데미지%", "최대HP%", "방어율무시%"};  //12~16 %붙음

        ArrayList equipStats = this.equipment.getStats();
        ArrayList equipEnhance = this.equipment.getEnhance();
        ArrayList equipAdditional = this.equipment.getAdditional(); //추가옵션

        int sum = 0;

        for(int i=0; i<table.length; i++){
            sum = (Integer) equipStats.get(i) + (Integer)equipAdditional.get(i) + (Integer) equipEnhance.get(i);
            if(sum == 0 || i==6) continue;
            equipInfo = equipInfo + table[i] + " : " + "+" + sum;

            if(i>=12 && i<=16) equipInfo = equipInfo + "%"; //%붙은 스텟

            if(sum != (Integer) equipStats.get(i)){ //추가옵션이나 강화 수치가 있는 경우
                equipInfo = equipInfo + " (" + equipStats.get(i);
                if(i>=12 && i<=16) equipInfo = equipInfo + "%"; //%붙은 스텟

                if((Integer)equipAdditional.get(i) > 0) { //추가 옵션
                    equipInfo = equipInfo + "<font color=\"#66FF66\"> +" + equipAdditional.get(i);
                    if(i>=12 && i<=16) equipInfo = equipInfo + "%"; //%붙은 스텟
                    equipInfo = equipInfo + "</font>";
                }

                if((Integer)equipEnhance.get(i) > 0) { //강화 수치
                    equipInfo = equipInfo + "<font color=\"#99CCFF\"> +" + equipEnhance.get(i);
                    equipInfo = equipInfo + "</font>";
                }
                equipInfo = equipInfo + ")";
            }
            equipInfo = equipInfo + "<br>";
        }

        if((Integer) equipAdditional.get(6) !=0 )
            equipInfo = equipInfo + "착용 레벨 감소 : <font color=\"#66FF66\">" + equipAdditional.get(6) + "</font><br>";
        equipInfo = equipInfo + "업그레이드 가능 횟수 : "
                + (equipment.getMaxUp()-equipment.getNowUp()-equipment.getFailUp())
                + "<br>(복구 가능 횟수 : " + equipment.getFailUp() + ")";

        if(equipment.getGoldHammer() == 1) equipInfo = equipInfo + "\n황금망치 제련 적용";

        return equipInfo;
    }

    //리턴스크롤 다이얼로그
    private void returnScrollDialog() {

        AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(this);
        myAlertBuilder.setTitle("리턴 스크롤");
        myAlertBuilder.setMessage("주문서 사용에 성공했습니다. 아이템의 옵션을 되돌리시겠습니까?\n"+recentChoas());
        myAlertBuilder.setCancelable(false);

        // 버튼 추가
        myAlertBuilder.setNegativeButton("    예    ",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog,int which){
                equipment.useReturnScroll();
                updateText();
                setEquipName();
            }
        });
        myAlertBuilder.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });


        myAlertBuilder.show();
    }

    //혼돈의 주문서로 증가한 능력치 문자열 반환
    public String recentChoas(){
        ArrayList<Integer> recent = this.equipment.getRecentChaos();
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
        if(view.getId() == R.id.scroll_button_0){
            //주문의 흔적
            ImageView new_select = (ImageView)findViewById(R.id.scroll_check_0);
            new_select.setVisibility(View.VISIBLE);

            selected_button_id = view.getId();
            selected_check_id = R.id.scroll_check_0;

            //주문서의 세부 옵션 선택 팝업
            Intent intent = new Intent(this, SelectBasicScollPopup.class);
            String scrollType = "";
            //방어구, 장신구인 경우
            if(armors.contains(equipment.getType()) || accessories.contains(equipment.getType())){
                scrollType = "armors";
            }
            //무기인 경우
            else if(equipment.getType().equals("무기")){
                scrollType = "weapons";
            }
            //장갑인 경우
            else if(equipment.getType().equals("장갑")){
                scrollType = "glove";
            }

            intent.putExtra("type", scrollType);
            intent.putExtra("scroll", 0);
            startActivityForResult(intent, 0);
        }

        else if(view.getId() == R.id.scroll_button_1){
            //순백의 주문서
            ImageView new_select = (ImageView)findViewById(R.id.scroll_check_1);
            new_select.setVisibility(View.VISIBLE);

            selected_button_id = view.getId();
            selected_check_id = R.id.scroll_check_1;

            //주문서의 세부 옵션 선택 팝업
            Intent intent = new Intent(this, SelectBasicScollPopup.class);
            intent.putExtra("scroll", 1);
            startActivityForResult(intent, 0);

        }
        else if(view.getId() == R.id.scroll_button_2){
            //황금 망치
            ImageView new_select = (ImageView)findViewById(R.id.scroll_check_2);
            new_select.setVisibility(View.VISIBLE);

            selected_button_id = view.getId();
            selected_check_id = R.id.scroll_check_2;

            //주문서의 세부 옵션 선택 팝업
            Intent intent = new Intent(this, SelectBasicScollPopup.class);
            intent.putExtra("scroll", 2);
            startActivityForResult(intent, 0);
        }
        else if(view.getId() == R.id.scroll_button_3){
            //이노센트 주문서
            ImageView new_select = (ImageView)findViewById(R.id.scroll_check_3);
            new_select.setVisibility(View.VISIBLE);

            selected_button_id = view.getId();
            selected_check_id = R.id.scroll_check_3;

            //주문서의 세부 옵션 선택 팝업
            Intent intent = new Intent(this, SelectBasicScollPopup.class);
            intent.putExtra("scroll", 3);
            startActivityForResult(intent, 0);
        }
        else if(view.getId() == R.id.scroll_button_4){
            //혼돈의 주문서
            ImageView new_select = (ImageView)findViewById(R.id.scroll_check_4);
            new_select.setVisibility(View.VISIBLE);

            selected_button_id = view.getId();
            selected_check_id = R.id.scroll_check_4;

            //주문서의 세부 옵션 선택 팝업
            Intent intent = new Intent(this, SelectBasicScollPopup.class);
            intent.putExtra("scroll", 4);
            startActivityForResult(intent, 0);
        }

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



