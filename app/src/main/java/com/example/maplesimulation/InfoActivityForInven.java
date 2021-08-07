package com.example.maplesimulation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InfoActivityForInven extends InfoActivity{
    private Equipment equipment;
    private int now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popup_equipment);

        //데이터 가져오기
        Intent intent = getIntent();
        this.equipment = (Equipment) intent.getSerializableExtra("equipment");
        this.now = (int) intent.getSerializableExtra("now");

        initInfo();

        //광고 초기화
        initAd();

        //강화버튼 활성화
        Button enhanceButton = findViewById(R.id.enhance);
        enhanceButton.setVisibility(View.VISIBLE);
    }

    public void goMain(View view) {
        if(this.equipment == null) return;
        Intent intent = getIntent();
        intent.putExtra("now", now);
        setResult(0, intent);
        finish();
    }
}
