package com.example.maplesimulation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class UsedPopup extends Activity {
    private Equipment equipment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_used);

        //데이터 가져오기
        Intent intent = getIntent();
        this.equipment = (Equipment) intent.getSerializableExtra("equipment");

        updateUsed();
    }

    public void updateUsed(){
        TextView usedView = findViewById(R.id.used);
        String usedList = "";

        if(equipment.used_sunback > 0)
            usedList = usedList + "순백의 주문서 : " + equipment.used_sunback + "\n";
        if(equipment.used_goldhammer > 0)
            usedList = usedList + "황금 망치 : " + equipment.used_goldhammer + "\n";
        if(equipment.used_innocent > 0)
            usedList = usedList + "이노센트 주문서 : " + equipment.used_innocent + "\n";
        if(equipment.used_chaos > 0)
            usedList = usedList + "혼돈의 주문서 : " + equipment.used_chaos + "\n";

        if(equipment.used_eternal > 0)
            usedList = usedList + "영원한 환생의 불꽃 : " + equipment.used_eternal + "\n";
        if(equipment.used_powerful > 0)
            usedList = usedList + "강력한 환생의 불꽃 : " + equipment.used_powerful + "\n";

        if(equipment.used_noljang > 0)
            usedList = usedList + "놀라운 장비강화 주문서 : " + equipment.used_noljang + "\n";
        if(equipment.used_protect_shield > 0)
            usedList = usedList + "프로텍트 쉴드 : " + equipment.used_protect_shield + "\n";

        if(equipment.used_blackcube > 0)
            usedList = usedList + "블랙 큐브 : " + equipment.used_blackcube + "\n";
        if(equipment.used_redcube > 0)
            usedList = usedList + "레드 큐브 : " + equipment.used_redcube + "\n";
        if(equipment.used_addicube > 0)
            usedList = usedList + "에디셔널 큐브 : " + equipment.used_addicube + "\n";

        if(equipment.used_meso > 0)
            usedList = usedList + "사용한 메소(스타포스) : " + equipment.used_meso + "\n";

        if(usedList.equals("")) usedList = "사용한 내역이 없습니다.";

        usedView.setText(usedList);
    }

    public void goBack(View view){
        finish();
    }
}
