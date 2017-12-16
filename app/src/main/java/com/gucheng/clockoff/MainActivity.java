package com.gucheng.clockoff;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private DBHelper dbHelper = new DBHelper(MainActivity.this);
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView)findViewById(R.id.time_list);
        MyAdapter myAdapter = new MyAdapter(MainActivity.this);
        listView.setAdapter(myAdapter);
        Button btn = (Button)findViewById(R.id.clock_off);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH) + 1;
                int day = c.get(Calendar.DAY_OF_MONTH);
                String date = year + "年" + month + "月" + day + "日";
                Toast.makeText(MainActivity.this, date, Toast.LENGTH_SHORT).show();*/
               DBManger dbManger = DBManger.getInstance(MainActivity.this);
               dbManger.addClockOff();
            }
        });
    }
}
