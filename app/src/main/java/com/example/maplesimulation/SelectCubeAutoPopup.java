package com.example.maplesimulation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SelectCubeAutoPopup extends Activity {
    TextView txtText;
    String optionTable[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_select);
        this.setFinishOnTouchOutside(false);

        txtText = findViewById(R.id.selectTitle);
        txtText.setText("목표 옵션을 선택하세요.");

        optionTable = new String[]{"STR 3줄", "DEX 3줄", "INT 3줄", "LUK 3줄", "올스텟 3줄",
                "공격력 3줄", "마력 3줄", "보보공", "보보마", "보보방", "보공공", "보마마",
                "크크크(크뎀)", "크뎀 2줄"};

        ListView listView = findViewById(R.id.selectListview);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.text_popup_select, optionTable);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = getIntent();
                intent.putExtra("option", position);
                setResult(0, intent);
                finish();
            }
        });
    }

    //확인 버튼 클릭
    public void goBack(View view){
        onBackPressed();
    }
}
