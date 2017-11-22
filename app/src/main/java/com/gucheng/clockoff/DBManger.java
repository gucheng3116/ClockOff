package com.gucheng.clockoff;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by liuwei on 2017/11/22.
 */

public class DBManger {
    private DBHelper mDBHelper;
    private SQLiteDatabase database;
    private DBManger(DBHelper dbHelper) {
        mDBHelper = dbHelper;
        database = dbHelper.getWritableDatabase();
    }
    public static DBManger getInstance(DBHelper dbHelper) {
        return new DBManger(dbHelper);
    }
    public void addClockOff() {
        
    }
}
