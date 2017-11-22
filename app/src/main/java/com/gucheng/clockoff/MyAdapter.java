package com.gucheng.clockoff;

import android.content.Context;
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
    public MyAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return 30;
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
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.date.setText("2017/11/22");
        return convertView;
    }

    static class ViewHolder {
        public TextView date;
    }
}
