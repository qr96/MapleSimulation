package com.example.maplesimulation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class FlameActivity extends Activity {
    public int selected_button_id = -1; //선택된 아이템의 id
    public int selected_check_id = -1; //선택된 아이템의 체크 id
    Equipment equipment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flame);

        Intent intent = getIntent();
        this.equipment = (Equipment) intent.getSerializableExtra("equipment");

        setThumnail();
        updateText();
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

        Animation anim = new AlphaAnimation(0.0f, 0.8f);
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
        if(view.getId() == R.id.flame_button_0){
            //영환불
            ImageView new_select = (ImageView)findViewById(R.id.flame_check_0);
            new_select.setVisibility(View.VISIBLE);

            selected_button_id = view.getId();
            selected_check_id = R.id.flame_check_0;

        }

        else if(view.getId() == R.id.flame_button_1){
            //강환불
            ImageView new_select = (ImageView)findViewById(R.id.flame_check_1);
            new_select.setVisibility(View.VISIBLE);

            selected_button_id = view.getId();
            selected_check_id = R.id.flame_check_1;

        }
    }

    //강화 실행 버튼
    public void runEnhance(View view) {
        sparkleEffect();

        //0번 선택 : 영원한 환생의 불꽃
        if(selected_check_id == R.id.flame_check_0) {
            equipment.useEternalFlame();
        }
        else if(selected_check_id == R.id.flame_check_1) {
            equipment.usePowerfulFlame();
        }

        updateText();
        setEquipName();
        view.setEnabled(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }

        }, 700);
    }
}

