package com.gucheng.clockoff;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    private DBHelper dbHelper = new DBHelper(MainActivity.this);
    private SQLiteDatabase db;
    MyAdapter myAdapter;
    DBManger dbManger;
    String mDate = "";
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

        Button btnSupple = (Button)findViewById(R.id.supplement);
        btnSupple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar =Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int monthOfYear = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                final TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                      String time;
                      if (hour > 9) {
                          time = "" +hour;
                      } else {
                          time = "0" + hour;
                      }
                      if (minute > 9) {
                          time = time + ":" + minute;
                      } else {
                          time = time + ":0" + minute;
                      }
                        Toast.makeText(MainActivity.this, "选择了" + time, Toast.LENGTH_SHORT).show();
                        dbManger.addClockOff(mDate, hour, minute);

                    }
                }, hour, minute, true);
                DatePickerDialog datePickerDialog =
                        new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {


                                if ((i1 + 1) > 9)  {
                                    mDate = i + "年" + (i1 + 1) + "月";

//                                    Toast.makeText(MainActivity.this, "选择了" + i + "年" + (i1 + 1) + "月" + i2 + "日", Toast.LENGTH_SHORT).show();

                                } else {
//                                    Toast.makeText(MainActivity.this, "选择了" + i + "年0" + (i1 + 1) + "月" + i2 + "日", Toast.LENGTH_SHORT).show();
                                    mDate = i + "年0" + (i1 + 1) + "月";
                                }
                                if (i2 > 9) {
                                    mDate = mDate + i2 + "日";
                                } else {
                                    mDate = mDate +  "0" + i2 + "日";
                                }
                                timePickerDialog.show();
                            }
                        }, year, monthOfYear, day);
                datePickerDialog.show();
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
        } else if (event.equals("calcAvgTime")) {
            TextView textView = (TextView)findViewById(R.id.avgTIme);
            textView.setText(msg.getAvgTime());
        }
    }

}
