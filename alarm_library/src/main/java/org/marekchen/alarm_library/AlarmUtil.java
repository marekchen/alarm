package org.marekchen.alarm_library;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

/**
 * Created by chenpei on 2017/12/15.
 */

public class AlarmUtil {
    public static final int JOB_ID = 1000;

    public static void saveAlarmToSQ(Context context){

    }

    public static void setAlarm(Context context, Intent intent) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am == null) {
            return;
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setExact(AlarmManager.RTC_WAKEUP, 3 * 1000, pendingIntent);
        } else {
            am.set(AlarmManager.RTC_WAKEUP, 3 * 1000, pendingIntent);
        }
    }

    public static void setScheduleJob(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.i("chenpei","set JobScheduler");
            JobScheduler mJobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            JobInfo.Builder builder = new JobInfo.Builder(JOB_ID,
                    new ComponentName(context.getPackageName(), JobSchedulerService.class.getName()));

            builder.setPeriodic(60 * 1000); //每隔60秒运行一次
            builder.setRequiresCharging(true);
            builder.setPersisted(true);  //设置设备重启后，是否重新执行任务
            builder.setRequiresDeviceIdle(true);

            if (mJobScheduler.schedule(builder.build()) != JobScheduler.RESULT_SUCCESS) {
                //If something goes wrong
                Log.i("chenpei","mJobScheduler error");
            }
        }
    }
}
