package com.example.maplesimulation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class SelectBasicScollPopup extends Activity {
    TextView txtText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_select);

        txtText = (TextView)findViewById(R.id.title);
        txtText.setText("주문서를 선택하세요.");

        String basicScrolltable[] = {"100% 힘 주문서", "70% 힘 주문서", "30% 힘 주문서",
                "100% 민첩 주문서", "70% 민첩 주문서", "30% 민첩 주문서",
                "100% 지력 주문서", "70% 지력 주문서", "30% 지력 주문서",
                "100% 운 주문서", "70% 운 주문서", "30% 운 주문서",};

        ListView listView = findViewById(R.id.listview);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.text_popup_select, basicScrolltable);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent();
                intent.putExtra("scroll", position);
                setResult(0, intent);

                finish();
            }
        });
    }

    //확인 버튼 클릭
    public void mOnClose(View v){

        finish();
    }

}
