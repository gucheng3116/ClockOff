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
    private static final String TAG = "DBManager";
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
        String insert = "insert into clockoff(date,hour,minute) values('" + date + "'," + hour + "," + minute + ")";
        Cursor cursor = database.query("clockoff", null, "date = ?", new String[]{date}, null, null, null);
        if (cursor.getCount() > 0) {
//            Toast.makeText(mContext, "数据已更新", Toast.LENGTH_SHORT).show();
//            String updateSql = "update clockoff set hour = " + hour + ",minute = " + minute + " where date = '" + date + "'";
            database.execSQL(insert);
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
        String insert = "insert into clockoff(date,hour,minute) values('" + date + "'," + hour + "," + minute + ")";
        Cursor cursor = database.query("clockoff", null, "date = ?", new String[]{date}, null, null, null);
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
        int end = date.indexOf("月");
        String month = date.substring(0, end + 1);
        calcAvgTime(month);
    }

    public void queryCurrentMonth() {

    }

    public void querySpecMonth(String YearAndMont) {

    }

    public int queryAllDataCount() {
        database = mDBHelper.getReadableDatabase();
        Cursor cursor = database.query("clockoff", null, null,
                null, null, null, "date desc, hour desc, minute desc");
        int count = cursor.getCount();
        cursor.close();
        database.close();
        return count;
    }

    public ClockItem[] queryAllData() {

        database = mDBHelper.getReadableDatabase();
        Cursor cursor = database.query("clockoff", null, null, null,
                null, null, "date desc, hour desc, minute desc");
        ClockItem[] clockItems = new ClockItem[cursor.getCount()];

        int i = 0;
        if ((cursor != null) && cursor.moveToFirst()) {
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
        return clockItems;
    }

    public void delete(int position) {
        database = mDBHelper.getWritableDatabase();
        Cursor cursor = database.query("clockoff", null, null, null, null, null, "date desc, hour desc, minute desc");
        cursor.moveToPosition(position);
        int id = 0;
        String date = null;
        if (cursor != null) {
            id = cursor.getInt(cursor.getColumnIndex("id"));
            date = cursor.getString(cursor.getColumnIndex("date"));
            int end = date.indexOf("月");
            date = date.substring(0, end + 1);

        }
        String sqlDelete = "delete from clockoff where id = " + id;
        database.execSQL(sqlDelete);
        cursor.close();
        database.close();
        calcAvgTime(date);
        EventBus.getDefault().post(new MessageEvent("notifyDataSetChange"));
    }

    public void updateTime(int id, int hour, int minute) {
        String updateSql = "update clockoff set hour = " + hour + ",minute = " + minute + " where id = " + id;
        database = mDBHelper.getWritableDatabase();
        database.execSQL(updateSql);
        Cursor cursor = database.query("clockoff", null, "id=" + id, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int dateIndex = cursor.getColumnIndex("date");
            String date = cursor.getString(dateIndex);
            int end = date.indexOf("月");
            date = date.substring(0, end + 1);
            Log.d(TAG, "date is " + date);
            calcAvgTime(date);
        }
        database.close();
        EventBus.getDefault().post(new MessageEvent("notifyDataSetChange"));
    }

    public void calcAvgTime(String month) {

        month = month + "%";
        database = mDBHelper.getReadableDatabase();
        Cursor cursor = database.query("clockoff", null, "date like ?", new String[]{month}, null, null, null);
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
        int monthData = MonthTable.querySpecificMonthCount(database, month.replace("%", ""));
        if (monthData == 0) {
            MonthTable.insert(database, month.replace("%", ""), avgHour + "", avgMinute + "", count);
        } else {
            if (count == 0) {
                MonthTable.delete(database, month.replace("%", ""));
            } else {
                MonthTable.update(database, month.replace("%", ""), avgHour + "", avgMinute + "", count);
            }
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月");
        String currentMonth = df.format(new Date());
        if (month.contains(currentMonth)) {
            EventBus.getDefault().post(msg);
            Log.d(TAG, "update current month");
        } else {
            Log.d(TAG, "not update");
        }
        cursor.close();
        database.close();

    }

    public void calcAvgTime() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String date;
        if (month < 10) {
            date = year + "年0" + month + "月";
        } else {
            date = year + "年" + month + "月";
        }
        calcAvgTime(date);
    }

}
