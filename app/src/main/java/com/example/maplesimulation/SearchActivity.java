package com.example.maplesimulation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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
    private ArrayList<Equipment> equipmentList;
    private TextAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        equipmentList = new ArrayList<Equipment>();

        // 장비 이름 배열에 값들 추가
        Intent intent = getIntent();
        equipmentList = (ArrayList<Equipment>) intent.getSerializableExtra("equipList");

        // 장비 목록 recyclerview에 추가
        initRecyclerView();

        //검색창 초기화
        initSearchView();
    }

    public void initSearchView() {
        SearchView searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }

        });
    }

    public void initRecyclerView() {
        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerView = findViewById(R.id.recycler1) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this)) ;

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        adapter = new TextAdapter(equipmentList) ;
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
        String equipment = equipmentList.get(position).getName();

        AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(SearchActivity.this);
        myAlertBuilder.setTitle("장비 확인");
        myAlertBuilder.setMessage(equipment+"를 추가하시겠습니까?");

        // 버튼 추가 (Ok 버튼과 Cancle 버튼 )
        myAlertBuilder.setPositiveButton("  예  ",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){
                Intent intent = new Intent();
                intent.putExtra("equipment", equipmentList.get(position));
                setResult(0, intent);
                Toast.makeText(SearchActivity.this, "아이템이 추가되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        myAlertBuilder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        myAlertBuilder.show();
    }
}
