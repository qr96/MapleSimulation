package com.example.maplesimulation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;


public class SelectBasicScollPopup extends Activity {
    TextView txtText;
    String scrollTable[];
    String equipType = ""; //장비 부위
    int scrollType = -1; //0:주문의흔적, 1:순백의주문서

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_select);

        txtText = (TextView)findViewById(R.id.selectTitle);
        txtText.setText("주문서를 선택하세요.");

        Intent intent = getIntent();
        equipType = intent.getStringExtra("type");
        scrollType = intent.getIntExtra("scroll", 0);

        if(scrollType == 0) {
            if(equipType.equals("armors")) {
                scrollTable = new String[]{"100% 힘 주문서", "70% 힘 주문서", "30% 힘 주문서",
                        "100% 민첩 주문서", "70% 민첩 주문서", "30% 민첩 주문서",
                        "100% 지력 주문서", "70% 지력 주문서", "30% 지력 주문서",
                        "100% 운 주문서", "70% 운 주문서", "30% 운 주문서",
                        "100% 체력 주문서", "70% 체력 주문서", "30% 체력 주문서"};
            }
            else if(equipType.equals("weapons")){
                scrollTable = new String[]{"100% 공격력(힘) 주문서", "70% 공격력(힘) 주문서", "30% 공격력(힘) 주문서", "15% 공격력(힘) 주문서",
                        "100% 공격력(민첩) 주문서", "70% 공격력(민첩) 주문서", "30% 공격력(민첩) 주문서", "15% 공격력(민첩) 주문서",
                        "100% 마력(지력) 주문서", "70% 마력(지력) 주문서", "30% 마력(지력) 주문서", "15% 마력(지력) 주문서",
                        "100% 공격력(운) 주문서", "70% 공격력(운) 주문서", "30% 공격력(운) 주문서", "15% 공격력(운) 주문서",};
            }
            else if(equipType.equals("glove")){
                scrollTable = new String[]{"100% 공격력 주문서", "70% 공격력 주문서", "30% 공격력 주문서",
                        "100% 마력 주문서", "70% 마력 주문서", "30% 마력 주문서"};
            }
            else {
                scrollTable = new String[]{};
            }
        }
        else if(scrollType == 1) {
            scrollTable = new String[]{"순백의 주문서 100%", "순백의 주문서 10%", "순백의 주문서 5%"};
        }
        else if(scrollType == 2) {
            scrollTable = new String[]{"황금 망치 100%", "황금 망치 50%"};
        }
        else if(scrollType == 3) {
            scrollTable = new String[]{"이노센트 주문서 100%", "이노센트 주문서 50%", "이노센트 주문서 30%",
                    "아크 이노센트 주문서 100%", "아크 이노센트 주문서 30%"};
        }
        else if(scrollType == 4) {
            scrollTable = new String[]{"놀라운 긍정의 혼돈 주문서 100%", "놀라운 긍정의 혼돈 주문서 주문서 60%",
                    "놀라운 긍정의 혼돈 주문서 100% (리턴 스크롤)", "놀라운 긍정의 혼돈 주문서 주문서 60% (리턴 스크롤)"};
        }
        else if(scrollType == 8) {
            scrollTable = new String[]{"매지컬 공격력 주문서 100%", "매지컬 공격력 주문서 100%(리턴 스크롤)",
                    "매지컬 마력 주문서 100%", "매지컬 마력 주문서 100%(리턴 스크롤)"};
        }

        ListView listView = findViewById(R.id.selectListview);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.text_popup_select, scrollTable);
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
        customDialog.show();

        if(scrollType == 0) {
            customDialog.setMessage("장비를 강화하는 주문의 흔적입니다.\n(보너스확률 15%가 적용됩니다.)\n사용하시겠습니까?");
        }
        else if(scrollType == 1) {
            customDialog.setMessage("주문서 적용 실패로 차감된 업그레이드 가능 횟수를 복구하는 주문서 입니다.\n사용하시겠습니까?");
        }
        else if(scrollType == 2) {
            customDialog.setMessage("장비의 업그레이드 가능횟수를 1회 추가해줍니다.(아이템 당 1회만 사용 가능)\n사용하시겠습니까?");
        }
        else if(scrollType == 3) {
            if(position==3 || position==4) customDialog.setMessage("잠재 능력과 스타포스를 제외한 모든 옵션을 초기화하는 주문서 입니다.\n사용하시겠습니까?");
            else customDialog.setMessage("잠재 능력을 제외한 모든 옵션을 초기화하는 주문서 입니다.\n사용하시겠습니까?");
        }
        else if(scrollType == 4) {
            customDialog.setMessage("장비의 현재 옵션을 하락시키지 않고 더 좋게 재조정 하는 주문서 입니다.\n" +
                    "[확률]\n+6(5.93%) +4(4.94%) +3(13.87%) +2(23.87%) +1(33.01%) +0(18.38)\n" +
                    "사용하시겠습니까?");
        }
        else if(scrollType == 8) {
            if(position<2) customDialog.setMessage("공격력 향상 옵션을 추가합니다.\n" +
                    "[확률]\n올스텟+3 공격력+11(10%) 공격력+10(40%) 공격력+9(50%)");
            else customDialog.setMessage("마력 향상 옵션을 추가합니다.\n" +
                    "[확률]\n올스텟+3 마력+11(10%) 마력+10(40%) 마력+9(50%)");
        }
    }

    //확인 버튼 클릭
    public void goBack(View view){
        onBackPressed();
    }
}
