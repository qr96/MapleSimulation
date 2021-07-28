package com.example.maplesimulation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ScrollActivity extends Activity {
    public int selected_button_id = -1; //선택된 아이템의 id
    public int selected_check_id = -1; //선택된 아이템의 체크 id
    public int selected_detail_id = -1; //선택된 아이템의 세부 옵션 id  ex) 70%, 30% 주문서
    public Equipment equipment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);

        Intent intent = getIntent();
        this.equipment = (Equipment) intent.getSerializableExtra("equipment");

        setThumnail();
        updateText();
    }
    
    //강화 실행
    public void runEnhance(View view) {
        //주문의 흔적
        if(selected_button_id == R.id.scroll_button_0) {
            //힘 100%
            if(selected_detail_id == 0) {
                System.out.println("힘 100%");
            }
            //힘 70%
            else if(selected_detail_id == 1) {
                System.out.println("힘 100%");
            }
            //힘 30%
            else if(selected_detail_id == 2) {
                System.out.println("힘 100%");
            }
            //민첩 100%
            else if(selected_detail_id == 3) {
                System.out.println("힘 100%");
            }
        }
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

    public void updateText() {
        if(equipment == null) return;

        TextView textView = findViewById(R.id.info);
        String equipInfo = "";
        String[] table = {"STR", "DEX", "INT", "LUK", "최대HP", "최대MP", "착용레벨감소",
                "방어력", "공격력", "마력", "이동속도", "점프력", "올스텟%", "최대HP%", "방무", "보공", "뎀지"};

        ArrayList equipStats = equipment.getStats();
        ArrayList equipEnhance = equipment.getEnhance();

        int sum = 0;

        for(int i=0; i<table.length; i++){
            sum = (Integer) equipStats.get(i) + (Integer) equipEnhance.get(i);
            if(sum == 0) continue;
            equipInfo = equipInfo + table[i] + " : " + equipStats.get(i) + " +" + equipEnhance.get(i) +"\n";
        }

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

        //이미 선택된 버튼 클릭
        if(selected_button_id == view.getId()) {
            selected_button_id = -1;
            selected_check_id = -1;

            return;
        }

        //선택한 번호의 아이템에 체크 표시
        if(view.getId() == R.id.scroll_button_0){
            ImageView new_select = (ImageView)findViewById(R.id.scroll_check_0);
            new_select.setVisibility(View.VISIBLE);

            selected_button_id = view.getId();
            selected_check_id = R.id.scroll_check_0;

            Intent intent = new Intent(this, SelectBasicScollPopup.class);
            startActivityForResult(intent, 0);
        }


    }

    //intent 받는 부분
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 0: //0번 주문서
                if(data!=null) {
                    int selected = data.getIntExtra("scroll", -1);
                    this.selected_detail_id = selected;
                }
                break;
        }
    }

}
