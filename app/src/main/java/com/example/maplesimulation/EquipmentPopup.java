package com.example.maplesimulation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

public class EquipmentPopup extends Activity {
    Equipment equipment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_equipment);

        //데이터 가져오기
        Intent intent = getIntent();
        this.equipment = (Equipment) intent.getSerializableExtra("equipment");

        initInfo();
    }

    public void initInfo() {
        Typeface face = ResourcesCompat.getFont(this, R.font.nexon_low);
        String tmp = "";

        TextView star = findViewById(R.id.star);
        tmp = EquipmentInfo.starText(equipment);
        star.setText(Html.fromHtml(tmp));
        System.out.println(tmp);

        TextView name = findViewById(R.id.name);
        name.setText(equipment.getName());
        name.setTypeface(face);

        TextView grade = findViewById(R.id.grade);
        grade.setText("("+equipment.getPotentialGrade1()+" 아이템)");
        grade.setTypeface(face);

        ImageView thumail = findViewById(R.id.thumnail);
        int imageRID = this.getResources().getIdentifier(equipment.getImage(), "drawable", this.getPackageName());
        thumail.setImageResource(imageRID);

        TextView attack = findViewById(R.id.attack);
        attack.setTypeface(face);

        TextView reqlev = findViewById(R.id.reqlev);
        reqlev.setTypeface(face);

        TextView info = findViewById(R.id.info);
        tmp = "장비분류 : " + equipment.getName() + "<br>";
        tmp = tmp + EquipmentInfo.makeText(equipment);
        info.setText(Html.fromHtml(tmp));
        info.setTypeface(face);

        TextView potential1 = findViewById(R.id.potential1);
        tmp = EquipmentInfo.potential1(equipment);
        potential1.setText(Html.fromHtml(tmp));
        potential1.setTypeface(face);

        TextView potential2 = findViewById(R.id.potential2);
        tmp = EquipmentInfo.potential2(equipment);
        potential2.setText(Html.fromHtml(tmp));
        potential2.setTypeface(face);
    }
}
