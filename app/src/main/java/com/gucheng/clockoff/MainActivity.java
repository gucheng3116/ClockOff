package com.gucheng.clockoff;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

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
    }
}
