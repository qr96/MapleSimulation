package com.example.maplesimulation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;

import java.util.List;

public class SearchActivity extends Activity {
    public List<Equipment> equipmentList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //search bar
        SearchView searchView;
        searchView = findViewById(R.id.searchView);

        initLoadDB();

        for(int i=0; i<equipmentList.toArray().length; i++){
            System.out.println(equipmentList.get(i).getName());
        }

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

    public void test(View view) {
        System.out.println("clicked");
    }

    private void initLoadDB() {

        DataAdapter mDbHelper = new DataAdapter(getApplicationContext());
        mDbHelper.createDatabase();
        mDbHelper.open();

        // db에 있는 값들을 model을 적용해서 넣는다.
        equipmentList = mDbHelper.getTableData();

        // db 닫기
        mDbHelper.close();
    }
}
