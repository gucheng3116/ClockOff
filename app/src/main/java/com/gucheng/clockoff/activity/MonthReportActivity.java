package com.gucheng.clockoff.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gucheng.clockoff.DBHelper;
import com.gucheng.clockoff.DBManger;
import com.gucheng.clockoff.R;
import com.gucheng.clockoff.db.MonthTable;

import java.util.ArrayList;

/**
 * Created by suolong on 2018/8/11.
 */

public class MonthReportActivity extends AppCompatActivity {
    private ListView mListView;
    private ArrayList<MonthTable.MonthReportItem> mDatas = new ArrayList<MonthTable.MonthReportItem>();
    private DBManger mDbManager = DBManger.getInstance(this);
    private DBHelper mDbHelper;
    private SQLiteDatabase mDatabase;
    private MyAdapter mMonthAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_report);
        mListView = (ListView) findViewById(R.id.month_list);
        mDbHelper = new DBHelper(this);
        mDatabase = mDbHelper.getWritableDatabase();
        mDatas = MonthTable.queryAll(mDatabase);
        if (mDatas != null) {
            for (int i = 0; i < mDatas.size(); i++) {
                Log.d("MonthReportActivity", mDatas.get(i).toString());
            }
        }
        mMonthAdapter = new MyAdapter(this, mDatas);
        mListView.setAdapter(mMonthAdapter);
        mDatabase.close();
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, final long id) {
                AlertDialog dialog = new AlertDialog.Builder(MonthReportActivity.this)
                        .setTitle("删除").setMessage("确认删除该记录？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SQLiteDatabase database = mDbHelper.getWritableDatabase();
                                if (database == null) {
                                    return;
                                }
                                MonthTable.delete(database, id);
                                database.close();
                                Toast.makeText(MonthReportActivity.this, "delete data is " + mDatas.get(position).month, Toast.LENGTH_SHORT).show();
                                mDatas.remove(position);
                                mMonthAdapter.notifyDataSetChanged();
                            }
                        }).create();
                dialog.show();

                return false;
            }
        });

    }

    class MyAdapter extends BaseAdapter {
        private ArrayList<MonthTable.MonthReportItem> mDatas = new ArrayList<MonthTable.MonthReportItem>();
        private Context mContext;

        public MyAdapter(Context context, ArrayList<MonthTable.MonthReportItem> datas) {
            mContext = context;
            mDatas = datas;
        }

        @Override
        public int getCount() {
            return mDatas == null ? 0 : mDatas.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return mDatas.get(i).id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.month_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.date = (TextView) convertView.findViewById(R.id.date);
                viewHolder.time = (TextView) convertView.findViewById(R.id.time);
                viewHolder.count = (TextView) convertView.findViewById(R.id.month_count);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            MonthTable.MonthReportItem item = mDatas.get(position);
            String month = item.month;
            viewHolder.date.setText(month);
            String timeFormat = "%02d时%02d分";
            String time = String.format(timeFormat, Integer.parseInt(item.hour), Integer.parseInt(item.minute));
            viewHolder.time.setText(time);
            viewHolder.count.setText(String.valueOf(item.count));

            return convertView;
        }


    }

    class ViewHolder {
        TextView date;
        TextView time;
        TextView count;
    }


}
