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
    private int min;
    private int max;
    private int now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popup_set_number);
        this.setFinishOnTouchOutside(false);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        min = intent.getIntExtra("min", 0);
        max = intent.getIntExtra("max", 1000);
        now = intent.getIntExtra("now", 500);

        txtText = (TextView)findViewById(R.id.selectTitle);
        txtText.setText("숫자를 입력하세요.");

        numberPicker = findViewById(R.id.number_picker);

        numberPicker.setMaxValue(max);
        numberPicker.setMinValue(min);
        numberPicker.setValue(now);
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
