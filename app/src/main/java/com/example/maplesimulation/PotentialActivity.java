package com.example.maplesimulation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class PotentialActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_potential);
    }

    public void useCube(View view){
        Cube cube = new Cube();
        cube.useCube();
    }

    //뒤로가기
    public void goBack(View view){
        onBackPressed();
    }

}

