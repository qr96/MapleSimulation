package com.example.maplesimulation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class ScrollActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
    }

    //뒤로가기
    public void goBack(View view){
        onBackPressed();
    }
}
