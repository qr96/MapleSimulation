package com.example.maplesimulation;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CubeDataAdapter extends DataAdapter {
    protected static final String TAG = "CubeDataAdapter";

    // TODO : TABLE 이름을 명시해야함
    protected static final String TABLE_NAME = "큐브테이블";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private DataBaseHelper mDbHelper;

    public CubeDataAdapter(Context context) {
        super(context);
        this.mContext = context;
        mDbHelper = new DataBaseHelper(mContext);
    }

    @Override
    public List getTableData()  {
        try {
            String sql ="SELECT * FROM " + TABLE_NAME;

            // 모델 넣을 리스트 생성
            List cubeTable = new ArrayList();


            CubeTable blackCubeTable = new CubeTable();
            String sqlBlckCubeWeaponRare = "SELECT 옵션,확률 FROM " + TABLE_NAME + " WHERE 큐브이름=블랙큐브 and 잠재등급=레어 and 줄=";
            String type = "";

            for(int i=1; i<=3; i++) {

                Cursor mCur = mDb.rawQuery(sqlBlckCubeWeaponRare+i, null);
                HashMap<String, String[]> table = new HashMap<>();
                String option = "";

                if(mCur!=null) {
                    while(mCur.moveToNext()) {
                        type = mCur.getString(1);
                        option = 
                    }
                }
            }


            // TODO : 모델 선언


            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {
                // 칼럼의 마지막까지
                while( mCur.moveToNext() ) {

                    // TODO : 커스텀 모델 생성
                    equip = new Equipment();

                    // TODO : Record 기술
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
