package com.gucheng.clockoff;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class MainActivity extends AppCompatActivity {

    private DBHelper dbHelper = new DBHelper(MainActivity.this);
    private SQLiteDatabase db;
    MyAdapter myAdapter;
    DBManger dbManger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView)findViewById(R.id.time_list);
        myAdapter = new MyAdapter(MainActivity.this);
        dbManger = DBManger.getInstance(MainActivity.this);

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

               dbManger.addClockOff();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long id) {
                Toast.makeText(MainActivity.this,"position is " + position, Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("删除").setMessage("确认删除该记录？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(MainActivity.this, "点击了取消按钮", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(MainActivity.this, "点击了确认按钮", Toast.LENGTH_SHORT).show();
                                dbManger.delete(position);
                            }
                        }).create();
                dialog.show();
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent msg) {
        String event = msg.getMsg();
//        Toast.makeText(MainActivity.this,msg.getMsg(),Toast.LENGTH_SHORT).show();
        if (event.equals("notifyDataSetChange")) {
            myAdapter.notifyDataSetChanged();
        }
    }
}
