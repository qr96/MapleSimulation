package com.example.maplesimulation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.CheckBox;
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
    public Cube blackCube;
    public Cube redCube;
    public Cube addiCube;

    Animation autoAnim;

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

        blackCube = new Cube(this.equipment, (CubeTable) cubeTableList.get(0));
        redCube = new Cube(this.equipment, (CubeTable) cubeTableList.get(1));
        addiCube = new Cube(this.equipment, (CubeTable) cubeTableList.get(2));

        autoAnim = new AlphaAnimation(0.4f, 1.0f);
        autoAnim.setDuration(400);
        autoAnim.setRepeatMode(Animation.REVERSE);
        autoAnim.setRepeatCount(Animation.INFINITE);
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

    //auto모드 계속 돌릴지 여부
    boolean keepGoing = false;

    public void useCube(View view) {
        CheckBox autoCheck = findViewById(R.id.auto);
        String cube = "";

        if(selected_button_id == R.id.button0){
            //오토모드
            if(autoCheck.isChecked()) {
                autoCube("black", view);
                return;
            }
            cube = "black";
        }
        else if(selected_button_id == R.id.button1){
            //오토모드
            if(autoCheck.isChecked()) {
                autoCube("red", view);
                return;
            }
            cube = "red";
        }
        else if(selected_button_id == R.id.button2){
            cube = "additional";
        }
        else return;

        view.setEnabled(false);
        usingCube(cube);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }
        }, 600);
    }

    public void autoCube(String cube, View view) {
        CheckBox autoCheck = findViewById(R.id.auto);

        if(keepGoing) keepGoing = false;
        else keepGoing = true;
        if(keepGoing==false){
            view.getAnimation().cancel();
            autoCheck.setEnabled(true);
        }
        else{
            autoCheck.setEnabled(false);
            view.startAnimation(autoAnim);
        }

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(keepGoing) {
                    usingCube(cube);
                    handler.postDelayed(this, 400);  // 1 second delay
                }
            }
        };
        handler.post(runnable);
    }

    public void usingCube(String name) {
        if(name.equals("black")) {
            blackCube.useBlackCube();
        }
        else if(name.equals("red")) {
            redCube.useRedCube();
        }
        else if(name.equals("additional")) {
            addiCube.useAddiCube();
        }
        else {
            return;
        }

        updateText();
        sparkleEffect();
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

