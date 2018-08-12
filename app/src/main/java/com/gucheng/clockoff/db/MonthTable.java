package com.gucheng.clockoff.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by suolong on 2018/8/11.
 */

public class MonthTable {
    public static final String CREATE_TABLE = "create table if not exists MonthReport (id integer primary key autoincrement, month text, hour text, minute text, count integer)";
    public static final String TABLE_NAME = "MonthReport";

    public static void insert(SQLiteDatabase db, String month, String hour, String minute, int count) {
        String sql = "insert into " + TABLE_NAME + "(month,hour,minute,count) values (?, ?, ?, ?)";
        db.execSQL(sql, new Object[]{month, hour, minute, count});
    }
}
