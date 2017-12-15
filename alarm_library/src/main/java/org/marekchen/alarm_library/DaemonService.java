package org.marekchen.alarm_library;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.coolerfall.daemon.Daemon;

/**
 * Created by chenpei on 2017/12/15.
 */

public class DaemonService extends Service {

    public static final int GRAY_SERVICE_ID = 1000;
    public static final int WAKE_REQUEST_CODE = 2000;
    public static final int ALARM_INTERVAL = 5 * 1000;

    private MyBinder mBinder = new MyBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        Daemon.run(DaemonService.this,
                DaemonService.class, Daemon.INTERVAL_ONE_MINUTE);
        //startTimeTask();
        grayGuard();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("chenpei", "onStartCommand:" + startId);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("chenpei", "onBind");
        return mBinder;
    }

    public class MyBinder extends Binder {
        public void setAlarm(Intent intent) {
            Log.d("chenpei", "MyBinder setAlarm");
            AlarmUtil.setAlarm(DaemonService.this, intent);
        }
    }

    private void grayGuard() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            startForeground(GRAY_SERVICE_ID, new Notification());
        } else {
            Intent innerIntent = new Intent(this, DaemonInnerService.class);
            startService(innerIntent);
            startForeground(GRAY_SERVICE_ID, new Notification());
        }
        //发送唤醒广播来促使挂掉的UI进程重新启动起来
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent();
        alarmIntent.setAction(WakeReceiver.GRAY_WAKE_ACTION);
        PendingIntent operation = PendingIntent.getBroadcast(this,
                WAKE_REQUEST_CODE, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (alarmManager == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setWindow(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis(), ALARM_INTERVAL, operation);
        } else {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis(), ALARM_INTERVAL, operation);
        }
    }

    /**
     * 给 API >= 18 的平台上用的灰色保活手段
     */
    public static class DaemonInnerService extends Service {

        public static final String LOG_TAG = "DaemonInnerService";

        @Override
        public void onCreate() {
            Log.i(LOG_TAG, "InnerService -> onCreate");
            super.onCreate();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Log.i(LOG_TAG, "InnerService -> onStartCommand");
            startForeground(GRAY_SERVICE_ID, new Notification());
            //stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public IBinder onBind(Intent intent) {
            throw new UnsupportedOperationException("Not yet implemented");
        }

        @Override
        public void onDestroy() {
            Log.i(LOG_TAG, "InnerService -> onDestroy");
            super.onDestroy();
        }
    }
}
