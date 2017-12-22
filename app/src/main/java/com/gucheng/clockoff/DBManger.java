package com.gucheng.clockoff;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        database = mDBHelper.getWritableDatabase();
//        String insert = "insert into clockoff(date,hour,minute) values('2017年12月2日',12,2)";
        String insert = "insert into clockoff(date,hour,minute) values('" + date + "'," + hour + ","+ minute + ")";
        database.execSQL(insert);
        Log.d("liuwei", "date is " + date);
        database.close();

    }

    public void queryCurrentMonth() {

    }

    public void querySpecMonth(String YearAndMont) {

    }

    public int queryAllDataCount() {
        database = mDBHelper.getReadableDatabase();
        Cursor cursor = database.query("clockoff",null,null,null,null,null,null);
        int count = cursor.getCount();
        cursor.close();
        database.close();
        return count;
    }

    public ClockItem[] queryAllData() {

        database = mDBHelper.getReadableDatabase();
        Cursor cursor = database.query("clockoff",null,null,null,null,null,null);
        ClockItem[] clockItems = new ClockItem[cursor.getCount()];

        int i = 0;
        if ((cursor!=null) && cursor.moveToFirst()) {
            do {
                clockItems[i] = new ClockItem();
                clockItems[i].date = cursor.getString(cursor.getColumnIndex("date"));
                clockItems[i].hour = cursor.getString(cursor.getColumnIndex("hour"));
                clockItems[i].minute = cursor.getString(cursor.getColumnIndex("minute"));
                i++;
            } while (cursor.moveToNext());
        }
        return  clockItems;
    }




}
