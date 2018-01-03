package com.gucheng.clockoff;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

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
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder.date = (TextView)convertView.findViewById(R.id.date);
            holder.time = (TextView)convertView.findViewById(R.id.time);
            holder.edit = (Button)convertView.findViewById(R.id.edit);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        final ClockItem item = clockItems[position];
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
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(mContext,"position is " + position, Toast.LENGTH_SHORT).show();
                TimePickerDialog.OnTimeSetListener timeListener =
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker timerPicker,
                                                  int hourOfDay, int minute) {
                                Toast.makeText(mContext,"选择了" + hourOfDay + "时" + minute + "分", Toast.LENGTH_SHORT).show();
                            }
                        };
                Dialog dialog = new TimePickerDialog(mContext, timeListener,
                        Integer.parseInt(item.hour),
                        Integer.parseInt(item.minute),
                        true);   //是否为二十四制
                dialog.show();
            }
        });
        return convertView;
    }

    static class ViewHolder {
        public TextView date;
        public TextView time;
        public Button edit;
    }

    @Override
    public void notifyDataSetChanged() {
        clockItems = dbManger.queryAllData();
        super.notifyDataSetChanged();
    }
}
