package com.gucheng.clockoff;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liuwei on 2017/11/22.
 */

public class DBManger {
    private DBHelper mDBHelper;
    private SQLiteDatabase database;
    private static Context mContext;
    private static DBManger dbManger = null;
    private DBManger() {
        mDBHelper = new DBHelper(mContext);
    }
    public static DBManger getInstance(Context context) {
        mContext = context;
        if (dbManger == null) {
             dbManger = new DBManger();
        }

        return dbManger;
    }

    public void addClockOff() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        String date = df.format(new Date());
        database = mDBHelper.getWritableDatabase();
        String insert = "insert into clockoff(date,month,day) values('2017年12月2日',12,2)";
        database.execSQL(insert);
        Log.d("liuwei", "date is " + date);
        database.close();

    }

    public void queryCurrentMonth() {

    }

    public void querySpecMonth(String YearAndMont) {

    }

    public void queryAllData() {
        
    }
}
