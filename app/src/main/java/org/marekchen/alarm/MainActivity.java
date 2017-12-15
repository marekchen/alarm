package org.marekchen.alarm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.marekchen.alarm_library.AlarmDBHelper;
import org.marekchen.alarm_library.AlarmItem;
import org.marekchen.alarm_library.AlarmUtil;
import org.marekchen.alarm_library.BatteryUtils;
import org.marekchen.alarm_library.DaemonService;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ServiceConnection mConnection;
    DaemonService.MyBinder myBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.set_alarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this,SecondActivity.class);
//                myBinder.setAlarm(intent);
                AlarmDBHelper helper = new AlarmDBHelper(MainActivity.this);
                AlarmItem item = new AlarmItem("", "message1", 1, 1);
                helper.addAlarm(item);
            }
        });

        findViewById(R.id.get_alarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmDBHelper helper = new AlarmDBHelper(MainActivity.this);
                List<AlarmItem> list = helper.queryAlarm();
                StringBuilder result = new StringBuilder();
                for (AlarmItem item : list) {
                    result.append(item.toString());
                    result.append("\n");
                }
                ((TextView) findViewById(R.id.result)).setText(result.toString());
            }
        });
        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.i("chenpei", "onServiceConnected");
                myBinder = (DaemonService.MyBinder) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.i("chenpei", "onServiceDisconnected");
            }
        };
        initAlarmService();
    }

    private void initAlarmService() {
        startService(new Intent(this, DaemonService.class));//启动闹钟服务
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.i("chenpei","1");
            if(!BatteryUtils.isIgnoringBatteryOptimizations(this)){
                Log.i("chenpei","2");
                BatteryUtils.setIgnoreBatteryOption(this);
            }
        }
        //绑定闹钟服务
        Intent intent = new Intent(this, DaemonService.class);
        intent.setAction("android.intent.action.DaemonService");
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {//判断是否有闹钟，没有则关闭闹钟服务
//            String alarm = localPreferencesHelper.getString(LocalPreferencesHelper.ALARM_CLOCK);
//            if (daemonService != -1 && mIRemoteService != null) {
//                mIRemoteService.resetAlarm();
//            }
//
//            if (!alarm.equals("[]")) {
//                if (daemonService != -1) {
//                    startService(new Intent(this, DaemonService.class));
//                }
//            } else {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    mJobScheduler.cancel(JOB_ID);
//                }
//
//            }
            unbindService(mConnection); //解除绑定服务。
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == BatteryUtils.REQUEST_IGNORE_BATTERY_CODE) {
                AlarmUtil.setScheduleJob(this);
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == BatteryUtils.REQUEST_IGNORE_BATTERY_CODE) {
                Toast.makeText(this, "请开启忽略电池优化~", Toast.LENGTH_LONG).show();
            }
        }
    }

}
