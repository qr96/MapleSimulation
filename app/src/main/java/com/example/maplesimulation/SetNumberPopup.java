package com.example.maplesimulation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;


public class SetNumberPopup extends Activity {
    private TextView txtText;
    private NumberPicker numberPicker;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popup_set_number);
        this.setFinishOnTouchOutside(false);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");

        txtText = (TextView)findViewById(R.id.selectTitle);
        txtText.setText("숫자를 선택하세요.");

        numberPicker = findViewById(R.id.number_picker);
        if(type.equals("level")) {
            txtText.setText("레벨을 입력하세요.");
            numberPicker.setMaxValue(300);
            numberPicker.setMinValue(1);
            numberPicker.setValue(250);
        }
        else if(type.equals("stat")) {
            txtText.setText("스탯을 입력하세요.");
            numberPicker.setMaxValue(1600);
            numberPicker.setMinValue(1);
            numberPicker.setValue(1268);
        }
        else if(type.equals("str")) {
            txtText.setText("STR을 입력하세요.");
            numberPicker.setMaxValue(1600);
            numberPicker.setMinValue(1);
            numberPicker.setValue(1268);
        }
        else if(type.equals("dex")) {
            txtText.setText("DEX를 입력하세요.");
            numberPicker.setMaxValue(1600);
            numberPicker.setMinValue(1);
            numberPicker.setValue(1268);
        }
        else if(type.equals("int")) {
            txtText.setText("INT를 입력하세요.");
            numberPicker.setMaxValue(1600);
            numberPicker.setMinValue(1);
            numberPicker.setValue(1268);
        }
        else if(type.equals("luk")) {
            txtText.setText("LUK를 입력하세요.");
            numberPicker.setMaxValue(1600);
            numberPicker.setMinValue(1);
            numberPicker.setValue(1268);
        }
        else if(type.equals("hp")) {
            txtText.setText("HP를 입력하세요.");
            numberPicker.setMaxValue(1600);
            numberPicker.setMinValue(1);
            numberPicker.setValue(1268);
        }
    }

    public void goApply(View view) {
        Intent intent = getIntent();
        intent.putExtra("type", type);
        intent.putExtra("number", numberPicker.getValue());
        setResult(0, intent);
        finish();
    }

    //확인 버튼 클릭
    public void goBack(View view){
        onBackPressed();
    }
}
