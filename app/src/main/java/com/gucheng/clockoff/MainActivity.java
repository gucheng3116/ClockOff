package com.gucheng.clockoff;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private DBHelper dbHelper = new DBHelper(MainActivity.this);
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button createDB = (Button)findViewById(R.id.create_database);
        createDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               db = dbHelper.getWritableDatabase();
            }
        });
    }
}
