package com.example.maplesimulation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import org.w3c.dom.Text;

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
        accessories = new ArrayList<>(Arrays.asList("얼굴장식","눈장식","벨트","귀고리","견장","반지"));
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
            //힘 100%
            if(selected_detail_id == 0) {
                possibility = 100;
                justat = "STR";
            }
            //힘 70%
            else if(selected_detail_id == 1) {
                possibility = 70;
                justat = "STR";
            }
            //힘 30%
            else if(selected_detail_id == 2) {
                possibility = 30;
                justat = "STR";
            }
            else if(selected_detail_id == 3) {
                possibility = 100;
                justat = "DEX";
            }
            else if(selected_detail_id == 4) {
                possibility = 70;
                justat = "DEX";
            }
            else if(selected_detail_id == 5) {
                possibility = 30;
                justat = "DEX";
            }
            else if(selected_detail_id == 6) {
                possibility = 100;
                justat = "INT";
            }
            else if(selected_detail_id == 7) {
                possibility = 70;
                justat = "INT";
            }
            else if(selected_detail_id == 8) {
                possibility = 30;
                justat = "INT";
            }
            else if(selected_detail_id == 9) {
                possibility = 100;
                justat = "LUK";
            }
            else if(selected_detail_id == 10) {
                possibility = 70;
                justat = "LUK";
            }
            else if(selected_detail_id == 11) {
                possibility = 30;
                justat = "LUK";
            }
            if(armors.contains(this.equipment.getType())) { //방어구인 경우
                result = this.equipment.doArmorScroll2(possibility, justat);
            }
            else if(accessories.contains(this.equipment.getType())) { //장신구인 경우
                if(this.equipment.getLevReq()>=120 && this.equipment.getLevReq()<=200){ //120~200
                    result = this.equipment.doAccessaryScroll2(possibility, justat);
                }
                else if(this.equipment.getLevReq()>=75 && this.equipment.getLevReq()<=120) { //75~110
                    result = this.equipment.doAccessaryScroll1(possibility, justat);
                }
            }
            else if(weapons.contains(this.equipment.getType())) { //무기인 경우
                result = this.equipment.doWeaponScroll2(possibility, justat);
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

        if(result==1) successEffect();
        else if(result==0) failEffect();

        updateText();
    }
    
    //맨 위의 썸네일 설정
    public void setThumnail() {
        if(equipment == null) return;

        ImageView imageView = findViewById(R.id.equipment_image);
        int imageRID = this.getResources().getIdentifier(equipment.getImage(), "drawable", this.getPackageName());
        imageView.setImageResource(imageRID);

        TextView textView = findViewById(R.id.equipment_name);
        textView.setText(equipment.getName());
    }
    
    //장비의 능력치 표시 부분
    public void updateText() {
        if(this.equipment == null) return;

        TextView textView = findViewById(R.id.info);
        String equipInfo = "";
        String[] table = {"STR", "DEX", "INT", "LUK", "최대HP", "최대MP", "착용레벨감소",
                "방어력", "공격력", "마력", "이동속도", "점프력", "올스텟%", "최대HP%", "방무", "보공", "뎀지"};

        ArrayList equipStats = this.equipment.getStats();
        ArrayList equipEnhance = this.equipment.getEnhance();

        int sum = 0;

        for(int i=0; i<table.length; i++){
            sum = (Integer) equipStats.get(i) + (Integer) equipEnhance.get(i);
            if(sum == 0) continue;
            equipInfo = equipInfo + table[i] + " : " + equipStats.get(i) + " +" + equipEnhance.get(i) +"\n";
        }

        equipInfo = equipInfo + "업그레이드 가능 횟수 : "
                + (equipment.getMaxUp()-equipment.getNowUp()-equipment.getFailUp())
                + "\n(복구 가능 횟수 : " + equipment.getFailUp() + ")";

        if(equipment.getGoldHammer() == 1) equipInfo = equipInfo + "\n황금망치 제련 적용";

        textView.setText(equipInfo);
    }

    //뒤로가기
    public void goBack(View view){
        Intent intent = new Intent();
        intent.putExtra("equipment", this.equipment);
        setResult(1, intent);
        finish();
    }

    //현재 선택된 N번 아이템에 표시
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
            
            //방어구, 장신구인 경우
            if(armors.contains(equipment.getType()) || accessories.contains(equipment.getType())){
                intent.putExtra("type", "armors");
                intent.putExtra("scroll", 0);
                startActivityForResult(intent, 0);
            }

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
