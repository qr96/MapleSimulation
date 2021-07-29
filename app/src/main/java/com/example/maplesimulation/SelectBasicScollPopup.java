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
    String scrollTable[];
    int scrollType = -1; //0:주문의흔적, 1:순백의주문서

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_select);

        txtText = (TextView)findViewById(R.id.title);
        txtText.setText("주문서를 선택하세요.");

        Intent secondIntent = getIntent();
        scrollType = secondIntent.getIntExtra("scroll", 0);

        if(scrollType == 0) {
            scrollTable = new String[]{"100% 힘 주문서", "70% 힘 주문서", "30% 힘 주문서",
                    "100% 민첩 주문서", "70% 민첩 주문서", "30% 민첩 주문서",
                    "100% 지력 주문서", "70% 지력 주문서", "30% 지력 주문서",
                    "100% 운 주문서", "70% 운 주문서", "30% 운 주문서"};
        }
        else if(scrollType == 1) {
            scrollTable = new String[]{"순백의 주문서 100%", "순백의 주문서 10%", "순백의 주문서 5%"};
        }
        else if(scrollType == 2) {
            scrollTable = new String[]{"황금 망치 100%", "황금 망치 50%"};
        }
        else if(scrollType == 3) {
            scrollTable = new String[]{"이노센트 주문서 100%", "이노센트 주문서 50%", "이노센트 주문서 30%"};
        }
        else {
            finish();
        }

        ListView listView = findViewById(R.id.listview);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.text_popup_select, scrollTable);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent();
                intent.putExtra("scroll", position);
                setResult(scrollType, intent);

                finish();
            }
        });
    }

    //확인 버튼 클릭
    public void mOnClose(View v){
        finish();
    }

}
