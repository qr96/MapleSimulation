package com.example.maplesimulation;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


//출처: https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=jogilsang&logNo=221605185222
public class DataAdapter {
    protected static final String TAG = "DataAdapter";

    // TODO : TABLE 이름을 명시해야함
    protected static final String TABLE_NAME = "장비";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private DataBaseHelper mDbHelper;

    public DataAdapter(Context context)
    {
        this.mContext = context;
        mDbHelper = new DataBaseHelper(mContext);
    }

    public DataAdapter createDatabase() throws SQLException
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

    public DataAdapter open() throws SQLException
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
            // Table 이름 -> antpool_bitcoin 불러오기
            String sql ="SELECT * FROM " + TABLE_NAME+" order by \"이름\"";

            // 모델 넣을 리스트 생성
            List equipList = new ArrayList();

            // TODO : 모델 선언
            Equipment equip = null;

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {
                // 칼럼의 마지막까지
                while( mCur.moveToNext() ) {

                    // TODO : 커스텀 모델 생성
                    equip = new Equipment();

                    // TODO : Record 기술
                    // 이름, 이미지, 분류, 직업군
                    // 레벨제한, 최대강화수, 현재강화수, 실패강화수,
                    // 최대스타포스, 스타포스,
                    // 기본 스텟, 강화 스텟, 추가옵션
                    equip.setName(mCur.getString(0));
                    equip.setImage(mCur.getString(1));
                    equip.setType(mCur.getString(2));
                    equip.setJob(mCur.getString(3));

                    equip.setLevReq(mCur.getInt(4));
                    equip.setMaxUp(mCur.getInt(5));
                    equip.setNowUp(mCur.getInt(6));
                    equip.setFailUp(mCur.getInt(7));

                    equip.setMaxStar(mCur.getInt(8));
                    equip.setStar(mCur.getInt(9));

                    for(int i=10; i<27; i++) {
                        equip.addStats(mCur.getInt(i));
                    }

                    // 리스트에 넣기
                    equipList.add(equip);
                }

            }
            return equipList;
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }
}

