package com.example.maplesimulation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ScrollActivity extends Activity {
    public String selected = ""; //선택된 아이템의 id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
    }

    //뒤로가기
    public void goBack(View view){
        onBackPressed();
    }

    public void select0(View view) {
        if(selected == "scroll0") return;





    }
}
