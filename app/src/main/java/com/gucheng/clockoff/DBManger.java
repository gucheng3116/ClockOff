package com.gucheng.clockoff;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.gucheng.clockoff.db.MonthTable;

import org.greenrobot.eventbus.EventBus;

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
        String insert = "insert into clockoff(date,hour,minute) values('" + date + "'," + hour + ","+ minute + ")";
        Cursor cursor = database.query("clockoff",null,"date = ?",new String[]{date},null,null,null);
        if (cursor.getCount() > 0) {
            Toast.makeText(mContext, "数据已更新", Toast.LENGTH_SHORT).show();
            String updateSql = "update clockoff set hour = " + hour + ",minute = " + minute + " where date = '" + date + "'";
            database.execSQL(updateSql);
        } else {
            Toast.makeText(mContext, "增加了一条新数据", Toast.LENGTH_SHORT).show();
            database.execSQL(insert);
        }

        Log.d("suolong", "date is " + date);
        EventBus.getDefault().post(new MessageEvent("notifyDataSetChange"));
        database.close();
        calcAvgTime();

    }

    public void addClockOff(String date, int hour, int minute) {
        database = mDBHelper.getWritableDatabase();
        String insert = "insert into clockoff(date,hour,minute) values('" + date + "'," + hour + ","+ minute + ")";
        Cursor cursor = database.query("clockoff",null,"date = ?",new String[]{date},null,null,null);
        if (cursor.getCount() > 0) {
            Toast.makeText(mContext, "数据已更新", Toast.LENGTH_SHORT).show();
            String updateSql = "update clockoff set hour = " + hour + ",minute = " + minute + " where date = '" + date + "'";
            database.execSQL(updateSql);
        } else {
            Toast.makeText(mContext, "增加了一条新数据", Toast.LENGTH_SHORT).show();
            database.execSQL(insert);
        }

        Log.d("suolong", "date is " + date);
        EventBus.getDefault().post(new MessageEvent("notifyDataSetChange"));
        database.close();
        calcAvgTime();
    }

    public void queryCurrentMonth() {

    }

    public void querySpecMonth(String YearAndMont) {

    }

    public int queryAllDataCount() {
        database = mDBHelper.getReadableDatabase();
        Cursor cursor = database.query("clockoff",null,null,
                null,null,null,"date desc, hour desc, minute desc");
        int count = cursor.getCount();
        cursor.close();
        database.close();
        return count;
    }

    public ClockItem[] queryAllData() {

        database = mDBHelper.getReadableDatabase();
        Cursor cursor = database.query("clockoff",null,null,null,
                null,null,"date desc, hour desc, minute desc");
        ClockItem[] clockItems = new ClockItem[cursor.getCount()];

        int i = 0;
        if ((cursor!=null) && cursor.moveToFirst()) {
            do {
                clockItems[i] = new ClockItem();
                clockItems[i].date = cursor.getString(cursor.getColumnIndex("date"));
                clockItems[i].hour = cursor.getString(cursor.getColumnIndex("hour"));
                clockItems[i].minute = cursor.getString(cursor.getColumnIndex("minute"));
                clockItems[i].id = cursor.getInt(cursor.getColumnIndex("id"));
                i++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return  clockItems;
    }

    public void delete(int position) {
        database = mDBHelper.getWritableDatabase();
        Cursor cursor = database.query("clockoff",null,null,null,null,null,"date desc, hour desc, minute desc");
        cursor.moveToPosition(position);
        int id = 0;
        if (cursor != null) {
            id = cursor.getInt(cursor.getColumnIndex("id"));
        }
        String sqlDelete = "delete from clockoff where id = " + id;
        database.execSQL(sqlDelete);
        cursor.close();
        database.close();
        calcAvgTime();
        EventBus.getDefault().post(new MessageEvent("notifyDataSetChange"));
    }

    public void updateTime(int id, int hour, int minute) {
        String updateSql = "update clockoff set hour = " + hour + ",minute = " + minute + " where id = " +id;
        database = mDBHelper.getWritableDatabase();
//        Cursor cursor = database.query("clockoff", null, null, null, null,null,null);
        database.execSQL(updateSql);
        database.close();
        calcAvgTime();
        EventBus.getDefault().post(new MessageEvent("notifyDataSetChange"));
    }

    public void calcAvgTime(String date) {
        database = mDBHelper.getReadableDatabase();
        Cursor cursor = database.query("clockoff", null, "date like ?", new String[]{date}, null, null, null);
        int count = 0;
        int totalHour = 0;
        int totalMinute = 0;
        int avgTimeByMinute = 0;
        int avgHour = 0;
        int avgMinute = 0;
        if (cursor != null) {
            count = cursor.getCount();
            if (count > 0) {
                while (cursor.moveToNext()) {
                    totalHour += cursor.getInt(cursor.getColumnIndex("hour"));
                    totalMinute += cursor.getInt(cursor.getColumnIndex("minute"));

                }
                avgTimeByMinute = (totalHour * 60 + totalMinute) / count;
                avgHour = avgTimeByMinute / 60;
                avgMinute = avgTimeByMinute % 60;
                Log.d("suolong", "avgHour is " + avgHour + ", avgMinute is " + avgMinute);
            }
            Log.d("suolong", "totalHour is " + totalHour + ", totalMinute is " + totalMinute);

        }
        MessageEvent msg = new MessageEvent("calcAvgTime");
        String hour = "";
        String minute = "";
        if (avgHour < 10) {
            hour = "0" + avgHour + "时";
        } else {
            hour = avgHour + "时";
        }
        if (avgMinute < 10) {
            minute = "0" + avgMinute + "分";
        } else {
            minute = avgMinute + "分";
        }
        msg.setAvgTime("本月平均时间为 " + hour + minute);
        MonthTable.querySpecificMonthCount(database, date.replace("%", ""));
        if (count == 0) {
             MonthTable.insert(database, date.replace("%", ""), avgHour + "", avgMinute + "", count);
        } else {

        }
        EventBus.getDefault().post(msg);
        cursor.close();
        database.close();

    }

    public void calcAvgTime() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String date;
        if (month < 10) {
            date = year + "年0" + month + "月%";
        } else {
            date = year + "年" + month + "月%";
        }
        calcAvgTime(date);
    }

}
