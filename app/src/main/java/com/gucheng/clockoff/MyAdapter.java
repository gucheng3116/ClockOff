package com.gucheng.clockoff;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by liuwei on 2017/11/22.
 */

public class MyAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    DBManger dbManger;
    private ClockItem clockItems[];
    public MyAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        dbManger = DBManger.getInstance(context);
        int count = dbManger.queryAllDataCount();
        Log.d("liuwei", "count is " + count);

        clockItems = dbManger.queryAllData();
    }

    @Override
    public int getCount() {
        return clockItems.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder.date = (TextView)convertView.findViewById(R.id.date);
            holder.time = (TextView)convertView.findViewById(R.id.time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        ClockItem item = clockItems[i];
        holder.date.setText(item.date);
        String time;
        String hour;
        String minute;
        if (Integer.parseInt(item.hour) < 10) {
            hour = '0' + item.hour;
        } else {
            hour = item.hour;
        }

        if (Integer.parseInt(item.minute) < 10) {
            minute = '0' + item.minute;
        } else {
            minute = item.minute;
        }
        time = hour + ":" + minute;



        holder.time.setText(time);
        return convertView;
    }

    static class ViewHolder {
        public TextView date;
        public TextView time;
    }
}
