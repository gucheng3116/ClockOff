package com.gucheng.clockoff;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gucheng.clockoff.activity.MonthReportActivity;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    MyAdapter myAdapter;
    DBManger dbManger;
    String mDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.time_list);
        myAdapter = new MyAdapter(MainActivity.this);
        dbManger = DBManger.getInstance(MainActivity.this);

        listView.setAdapter(myAdapter);
        findViewById(R.id.month_report).setOnClickListener(this);
        Button btn = (Button) findViewById(R.id.clock_off);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbManger.addClockOff();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long id) {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("删除").setMessage("确认删除该记录？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dbManger.delete(position);
                            }
                        }).create();
                dialog.show();
                return false;
            }
        });

        Button btnSupple = (Button) findViewById(R.id.supplement);
        btnSupple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
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
                            time = "" + hour;
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


                                if ((i1 + 1) > 9) {
                                    mDate = i + "年" + (i1 + 1) + "月";

                                } else {
                                    mDate = i + "年0" + (i1 + 1) + "月";
                                }
                                if (i2 > 9) {
                                    mDate = mDate + i2 + "日";
                                } else {
                                    mDate = mDate + "0" + i2 + "日";
                                }
                                timePickerDialog.show();
                            }
                        }, year, monthOfYear, day);
                datePickerDialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月");
        String date = df.format(new Date());
        dbManger.calcAvgTime(date + "%");
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
        if (event.equals("notifyDataSetChange")) {
            myAdapter.notifyDataSetChanged();
        } else if (event.equals("calcAvgTime")) {
            TextView textView = (TextView) findViewById(R.id.avgTIme);
            textView.setText(msg.getAvgTime());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, ContactAuthorActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.month_report:
                Intent intent = new Intent(MainActivity.this, MonthReportActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
