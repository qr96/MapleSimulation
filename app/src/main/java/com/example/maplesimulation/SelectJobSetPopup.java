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

public class SelectJobSetPopup extends Activity {
    TextView txtText;
    String jobList[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_select);
        this.setFinishOnTouchOutside(false);

        txtText = (TextView)findViewById(R.id.selectTitle);
        txtText.setText("주문서를 선택하세요.");

        jobList = new String[]{"히어로", "팔라딘", "다크나이트", "소울마스터", "미하일", "아란", "블래스터",
                "데몬슬레이어", "카이저", "제로", "아델", "하야토", "핑크빈",
                "보우마스터", "신궁", "패스파인더", "윈드브레이커", "메르세데스", "와일드헌터", "카인",
                "아크메이지(불,독)", "아크메이지(썬,콜)", "비숍", "플레임위자드", "에반",
                "루미너스", "배틀메이지", "키네시스", "일리움", "라라", "칸나", "비스트테이머",
                "나이트로드", "섀도어", "듀얼블레이드", "나이트워커", "팬텀", "카데나", "호영"};

        ListView listView = findViewById(R.id.selectListview);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.text_popup_select, jobList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                dialog(position);
            }
        });
    }

    public void dialog(int position) {
        CustomDialog customDialog = new CustomDialog(this, new CustomDialogClickListener() {
            @Override
            public void onPositiveClick() {
                Intent intent = getIntent();
                intent.putExtra("scroll", position);
                setResult(0, intent);
                finish();
            }
            @Override
            public void onNegativeClick() {
            }
        });
        customDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.show();

        customDialog.setMessage("해당 직업을 선택하시겠습니까?");
    }

    //확인 버튼 클릭
    public void goBack(View view){
        onBackPressed();
    }
}

