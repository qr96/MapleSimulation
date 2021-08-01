package com.example.maplesimulation;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CubeDataAdapter {
    protected static final String TAG = "CubeDataAdapter";

    // TODO : TABLE 이름을 명시해야함
    protected static final String TABLE_NAME = "큐브테이블";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private DataBaseHelper mDbHelper;

    public CubeDataAdapter(Context context) {
        this.mContext = context;
        mDbHelper = new DataBaseHelper(mContext);
    }

    public CubeDataAdapter createDatabase() throws SQLException
    {
        try
        {
            mDbHelper.createDataBase();
        }
        catch (IOException mIOException)
        {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public CubeDataAdapter open() throws SQLException
    {
        try
        {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "open >>"+ mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close()
    {
        mDbHelper.close();
    }

    public List getTableData()  {
        try {

            List cubeTableList = new ArrayList();

            String cubeTypes[] = {"'블랙큐브'"}; //, "'레드큐브'", "'에디셔널큐브'", "'명장의큐브'", "'장인의큐브'"};
            String grades[] = {"레어", "에픽", "유니크", "레전더리"};
            String types[] = {"무기", "엠블렘", "보조무기", "보조무기2", "방패"};
            String lines[] = {"1", "2", "3"};

            CubeTable cubeTable = new CubeTable();

            String sql = "SELECT * FROM " + TABLE_NAME;
            String key = "";

            Cursor mCur = mDb.rawQuery(sql, null);
            List list;

            if(mCur!=null) {
                while(mCur.moveToNext()) {
                    key = mCur.getString(2) + mCur.getString(1) + mCur.getString(3);
                    if(cubeTable.optaionTable.containsKey(key)) cubeTable.optaionTable.get(key).add(mCur.getString(4));
                    else {
                        list = new ArrayList();
                        list.add(mCur.getString(4));
                        cubeTable.optaionTable.put(key, list);
                    }
                    if(cubeTable.percentTable.containsKey(key))
                        cubeTable.percentTable.get(key).add(convert(mCur.getString(5)));
                    else {
                        list = new ArrayList();
                        list.add(convert(mCur.getString(5)));
                        cubeTable.percentTable.put(key, list);
                    }
                }
            }

            cubeTableList.add(cubeTable);

            return cubeTableList;
        }
        catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }
    
    //문자열 %를 double 로 바꿔줌
    public double convert(String percent){
        percent = percent.substring(0, percent.length()-1);
        return Double.parseDouble(percent);
    }
}
