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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends Activity {
    public ArrayList<String> equipNameList;
    public int selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        equipNameList = new ArrayList<String>();

        // 장비 이름 배열에 값들 추가
        Intent intent = getIntent();
        equipNameList = (ArrayList<String>) intent.getSerializableExtra("equipList");

        // 검색창
        SearchView searchView;
        searchView = findViewById(R.id.searchView);

        // 장비 목록 recyclerview에 추가
        initRecyclerView();

    }

    public void initRecyclerView() {
        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerView = findViewById(R.id.recycler1) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this)) ;

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        TextAdapter adapter = new TextAdapter(equipNameList) ;
        recyclerView.setAdapter(adapter) ;

        adapter.setOnItemClicklistener(new OnItemClickListener() {
            @Override
            public void onItemClick(TextAdapter.ViewHolder viewHolder, View view, int position) {
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
                intent.putExtra("equipment", position);
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
