package com.example.maplesimulation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Set;

public class PotentialActivity extends Activity {
    public int selected_button_id = -1; //선택된 아이템의 id
    public int selected_check_id = -1; //선택된 아이템의 체크 id
    public int selected_detail_id = -1; //선택된 아이템의 세부 옵션 id  ex) 70%, 30% 주문서

    public List cubeTableList; //[블랙큐브, 레드큐브, 에디셔널큐브, 명장의큐브, 장인의큐브]
    public Equipment equipment;
    public Cube cube;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_potential);

        Intent intent = getIntent();
        this.equipment = (Equipment) intent.getSerializableExtra("equipment");

        //큐브 DB 초기화
        initLoadCubeDB();

        updateText();
        setThumnail();

        cube = new Cube(this.equipment, (CubeTable) cubeTableList.get(0));
    }

    //맨 위의 썸네일 설정
    public void setThumnail() {
        if(equipment == null) return;

        ImageView imageView = findViewById(R.id.equipment_image);
        int imageRID = this.getResources().getIdentifier(equipment.getImage(), "drawable", this.getPackageName());
        imageView.setImageResource(imageRID);

        setEquipName();
    }

    public void setEquipName() {
        TextView textView = findViewById(R.id.equipment_name);
        if(equipment.getNowUp()>0) textView.setText(equipment.getName()+" (+"+equipment.getNowUp()+")");
        else textView.setText(equipment.getName());
    }

    public void infoPopup(View view){
        Intent intent = new Intent(this, EquipmentPopup.class);
        intent.putExtra("equipment", this.equipment);
        startActivityForResult(intent, 1);
    }

    public void updateText() {
        if(this.equipment == null) return;

        TextView textView = findViewById(R.id.info);
        String equipInfo = EquipmentInfo.potential(this.equipment);

        textView.setText(Html.fromHtml(equipInfo));
    }

    public void useCube(View view) {

        if(selected_button_id == R.id.button0){
            cube.useBlackCube();
        }
        else if(selected_button_id == R.id.button2){
            cube.useAddiCube();
        }
        else return;

        updateText();
        sparkleEffect();
        view.setEnabled(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }
        }, 600);
    }

    //반짝 효과
    public void sparkleEffect() {
        TextView sparkle = findViewById(R.id.sparkle_effect);

        Animation.AnimationListener listener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                sparkle.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                sparkle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };

        Animation anim = new AlphaAnimation(0.0f, 0.6f);
        anim.setDuration(400);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(1);

        sparkle.startAnimation(anim);
        anim.setAnimationListener(listener);

    }
    
    //DB 읽어서 List에 추가
    private void initLoadCubeDB() {
        CubeDataAdapter mDbHelper = new CubeDataAdapter(getApplicationContext());
        mDbHelper.createDatabase();
        mDbHelper.open();

        // db에 있는 값들을 model을 적용해서 넣는다.
        this.cubeTableList = mDbHelper.getTableData();
        System.out.println("init DB");

        // db 닫기
        mDbHelper.close();
    }

    //N번 아이템 버튼 이벤트
    public void select(View view) {

        //이전에 선택된 항목의 체크 제거
        ImageView prev_select = (ImageView)findViewById(selected_check_id);
        if(prev_select != null) prev_select.setVisibility(View.INVISIBLE);

        //이미 선택된 버튼 클릭한 경우
        if(selected_button_id == view.getId()) {
            selected_button_id = -1;
            selected_check_id = -1;

            return;
        }

        //선택한 번호의 아이템에 체크 표시
        if(view.getId() == R.id.button0){
            selected_check_id = R.id.check0;
        }

        else if(view.getId() == R.id.button1){
            selected_check_id = R.id.check1;
        }
        else if(view.getId() == R.id.button2){
            selected_check_id = R.id.check2;
        }
        else if(view.getId() == R.id.button3){
            selected_check_id = R.id.check3;
        }
        else if(view.getId() == R.id.button4){
            selected_check_id = R.id.check4;
        }
        else{
            return;
        }

        selected_button_id = view.getId();
        ImageView new_select = (ImageView)findViewById(selected_check_id);
        new_select.setVisibility(View.VISIBLE);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("equip", this.equipment);
        setResult(1, intent);
        finish();
        super.onBackPressed();
    }

}

