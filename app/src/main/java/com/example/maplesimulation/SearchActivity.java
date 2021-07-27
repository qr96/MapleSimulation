package com.example.maplesimulation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends Activity {
    public ArrayList<String> equipNameList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // 장비 이름 배열에 값들 추가
        Intent intent = getIntent();
        equipNameList = (ArrayList<String>) intent.getSerializableExtra("equipList");

        // 검색창
        SearchView searchView;
        searchView = findViewById(R.id.searchView);

        // 장비 이름 배열들을 리스트뷰에 추가
        listToView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(SearchActivity.this, query, Toast.LENGTH_SHORT).show();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    //List를 뷰에 추가
    private void listToView() {
        ListView listView;
        listView = findViewById(R.id.listview);

        //dp로 변환하기 위함
        final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_selectable_list_item, equipNameList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                confirmDialog(position);
            }
        });
    }

    //장비 선택 시 확인 다이얼로그
    private void confirmDialog(final int position) {
        String equipment = equipNameList.get(position);

        AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(SearchActivity.this);
        myAlertBuilder.setTitle("장비 확인");
        myAlertBuilder.setMessage(equipment+"를 강화하시겠습니까?");

        // 버튼 추가 (Ok 버튼과 Cancle 버튼 )
        myAlertBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){
                Intent intent = new Intent();
                intent.putExtra("equip", position);
                setResult(0, intent);
                finish();
            }
        });
        myAlertBuilder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        myAlertBuilder.show();
    }

}
