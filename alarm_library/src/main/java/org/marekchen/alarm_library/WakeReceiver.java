package org.marekchen.alarm_library;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by chenpei on 2017/12/15.
 */

public class WakeReceiver extends BroadcastReceiver {
    public static final String GRAY_WAKE_ACTION = "android.intent.action.GRAY_WAKE_ACTION";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("chenpei","android.intent.action.GRAY_WAKE_ACTION");
    }
}
