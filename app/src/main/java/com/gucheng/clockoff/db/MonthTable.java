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
        Cursor cursor = db.query(TABLE_NAME,null,null,null,null,null,null);
        if (cursor == null) {
            return null;
        }

        if (cursor.moveToFirst()) {
            items = new ArrayList<MonthReportItem>();
            int monthIndex = cursor.getColumnIndex("month");
            int hourIndex = cursor.getColumnIndex("hour");
            int minuteIndex = cursor.getColumnIndex("minute");
            int countIndex = cursor.getColumnIndex("count");
            do {
                MonthReportItem item = new MonthReportItem();
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
        Cursor cursor = db.query(TABLE_NAME,null,"month like " + month,null,null,null,null);
        if (cursor == null ) {
            return 0;
        } else {
            return cursor.getCount();
        }
       /* if (cursor.moveToNext()) {
            items = new ArrayList<MonthReportItem>();
            int monthIndex = cursor.getColumnIndex("month");
            int hourIndex = cursor.getColumnIndex("hour");
            int minuteIndex = cursor.getColumnIndex("minute");
            int countIndex = cursor.getColumnIndex("count");
            do {
                MonthReportItem item = new MonthReportItem();
                item.month = cursor.getString(monthIndex);
                item.hour = cursor.getString(hourIndex);
                item.minute = cursor.getString(minuteIndex);
                item.count = cursor.getInt(countIndex);
                items.add(item);
            } while (cursor.moveToNext());

        }
        cursor.close();
        return items;*/
    }

    public static class MonthReportItem {
        public String month;
        public String hour;
        public String minute;
        public int count;

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("MonthReportItem{");
            sb.append("month='").append(month).append('\'');
            sb.append(", hour='").append(hour).append('\'');
            sb.append(", minute='").append(minute).append('\'');
            sb.append(", count=").append(count);
            sb.append('}');
            return sb.toString();
        }
    }
}
