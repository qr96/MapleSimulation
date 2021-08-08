package com.example.maplesimulation;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
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

    //큐브 테이블 리스트 반환
    public List getTableData()  {
        try {

            List cubeTableList = new ArrayList();

            cubeTableList.add(cubeTable("블랙큐브테이블"));
            cubeTableList.add(cubeTable("레드큐브테이블"));
            cubeTableList.add(cubeTable("에디큐브테이블"));
            cubeTableList.add(cubeTable("명장의큐브테이블"));
            cubeTableList.add(cubeTable("장인의큐브테이블"));

            return cubeTableList;

        }
        catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }

    //지정된 테이블 이름 반환
    public CubeTable cubeTable(String table_name) {
        CubeTable cubeTable = new CubeTable();

        String sql = "SELECT * FROM " + table_name;
        String key1 = "";
        String key2 = "";
        String key3 = "";

        Cursor mCur = mDb.rawQuery(sql, null);
        List list;

        if(mCur!=null) {
            while(mCur.moveToNext()) {
                key1 = mCur.getString(1)+mCur.getString(0)+"1"; //ex)레어무기1
                key2 = mCur.getString(1)+mCur.getString(0)+"2"; //ex)레어무기2
                key3 = mCur.getString(1)+mCur.getString(0)+"3"; //ex)레어무기3

                if(cubeTable.optionTable.containsKey(key1)) cubeTable.optionTable.get(key1).add(mCur.getString(2));
                else {
                    list = new ArrayList();
                    list.add(mCur.getString(2));
                    cubeTable.optionTable.put(key1, list);
                }
                if(cubeTable.percentTable.containsKey(key1))
                    cubeTable.percentTable.get(key1).add(convert(mCur.getString(3)));
                else {
                    list = new ArrayList();
                    list.add(convert(mCur.getString(3)));
                    cubeTable.percentTable.put(key1, list);
                }

                if(cubeTable.optionTable.containsKey(key2)) cubeTable.optionTable.get(key2).add(mCur.getString(4));
                else {
                    list = new ArrayList();
                    list.add(mCur.getString(4));
                    cubeTable.optionTable.put(key2, list);
                }
                if(cubeTable.percentTable.containsKey(key2))
                    cubeTable.percentTable.get(key2).add(convert(mCur.getString(5)));
                else {
                    list = new ArrayList();
                    list.add(convert(mCur.getString(5)));
                    cubeTable.percentTable.put(key2, list);
                }

                if(cubeTable.optionTable.containsKey(key3)) cubeTable.optionTable.get(key3).add(mCur.getString(6));
                else {
                    list = new ArrayList();
                    list.add(mCur.getString(6));
                    cubeTable.optionTable.put(key3, list);
                }
                if(cubeTable.percentTable.containsKey(key3))
                    cubeTable.percentTable.get(key3).add(convert(mCur.getString(7)));
                else {
                    list = new ArrayList();
                    list.add(convert(mCur.getString(7)));
                    cubeTable.percentTable.put(key3, list);
                }
            }
        }

        return cubeTable;
    }

    //문자열 %를 double 로 바꿔줌
    public double convert(String percent){
        if(percent == null) return 0.0;
        percent = percent.substring(0, percent.length()-1);
        return Double.parseDouble(percent);
    }
}
