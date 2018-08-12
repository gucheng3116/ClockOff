package com.gucheng.clockoff;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.gucheng.clockoff.db.MonthTable;

/**
 * Created by liuwei on 2017/11/14.
 */

public class DBHelper extends SQLiteOpenHelper {
    private Context mContext;

    private static final String DATABASE_NAME = "TimeLeaveOffice.db";
    private static final int DATABASE_VERSION = 2;
    private static final String CREATE_TABLE = "create table if not exists clockoff (id integer primary key autoincrement, " +
            "date text, " +
            "hour text, " +
            "minute text)";
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
        sqLiteDatabase.execSQL(MonthTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
        sqLiteDatabase.execSQL(MonthTable.CREATE_TABLE);
    }


}
