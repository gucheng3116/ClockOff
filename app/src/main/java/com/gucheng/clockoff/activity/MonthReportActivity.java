package com.gucheng.clockoff.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.gucheng.clockoff.R;

import java.util.ArrayList;

/**
 * Created by suolong on 2018/8/11.
 */

public class MonthReportActivity extends AppCompatActivity {
    private ListView mListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_report);
        mListView = (ListView) findViewById(R.id.month_list);
    }

    class MyAdapter extends BaseAdapter {

        public MyAdapter(Context context, ArrayList<String> data) {

        }

        @Override
        public int getCount() {
            return 0;
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
        public View getView(int i, View view, ViewGroup viewGroup) {
            return null;
        }
    }
}
