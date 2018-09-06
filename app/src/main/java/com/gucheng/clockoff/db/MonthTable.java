package com.gucheng.clockoff.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

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

    public static ArrayList<MonthReportItem> queryAll(SQLiteDatabase db) {
        ArrayList<MonthReportItem> items = null;
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        if (cursor == null) {
            return null;
        }

        if (cursor.moveToFirst()) {
            items = new ArrayList<MonthReportItem>();
            int idIndex = cursor.getColumnIndex("id");
            int monthIndex = cursor.getColumnIndex("month");
            int hourIndex = cursor.getColumnIndex("hour");
            int minuteIndex = cursor.getColumnIndex("minute");
            int countIndex = cursor.getColumnIndex("count");
            do {
                MonthReportItem item = new MonthReportItem();
                item.id = cursor.getInt(idIndex);
                item.month = cursor.getString(monthIndex);
                item.hour = cursor.getString(hourIndex);
                item.minute = cursor.getString(minuteIndex);
                item.count = cursor.getInt(countIndex);
                items.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return items;
    }

    public static int querySpecificMonthCount(SQLiteDatabase db, String month) {
        ArrayList<MonthReportItem> items = null;
        month = "'" + month + "'";
        Cursor cursor = db.query(TABLE_NAME, null, "month like " + month, null, null, null, null);
        if (cursor == null) {
            return 0;
        } else {
            return cursor.getCount();
        }
    }

    public static class MonthReportItem {
        public int id;
        public String month;
        public String hour;
        public String minute;
        public int count;

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("MonthReportItem{");
            sb.append("id=").append(id);
            sb.append(", month='").append(month).append('\'');
            sb.append(", hour='").append(hour).append('\'');
            sb.append(", minute='").append(minute).append('\'');
            sb.append(", count=").append(count);
            sb.append('}');
            return sb.toString();
        }
    }

    public static void delete(SQLiteDatabase db, long id) {
        String deleteSql = "delete from " + TABLE_NAME + " where id = " + id;
        db.execSQL(deleteSql);
    }

    public static void delete(SQLiteDatabase db, String month) {
        String deleteSql = "delete from " + TABLE_NAME + " where month = ?";
        db.execSQL(deleteSql, new Object[]{month});
    }

    public static void update(SQLiteDatabase db, String month, String hour, String minute, int count) {
        String sql = "update %s set hour = %s, minute = %s, count = %d where month = ?";
        sql = String.format(sql, TABLE_NAME, hour, minute, count);
        db.execSQL(sql, new Object[]{month});
    }
}
