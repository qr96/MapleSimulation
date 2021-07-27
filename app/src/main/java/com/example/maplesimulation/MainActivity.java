package com.example.maplesimulation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void goPotential(View view) {
        Intent intent = new Intent(this, PotentialActivity.class);
        startActivity(intent);
    }

    public void goSearch(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

}
