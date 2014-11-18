package com.legame.np.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class SetAlarms {
    private static final String TAG = "SetAlarms";

    public static void enableAlarmsService(Context context, double delay,
            double interval, Class<?> cls, boolean isFirst) {
       //LogUtil.e(TAG, "enableAlarmsService");
        AlarmManager am = (AlarmManager) context
                .getSystemService(context.ALARM_SERVICE);

        Intent intent = new Intent(context, cls);
        PendingIntent sender = PendingIntent.getService(context, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        am.setRepeating(AlarmManager.RTC_WAKEUP,
                (long) (System.currentTimeMillis() + delay * 60 * 1000),
                (long) (interval * 60 * 1000), sender);
    }

}