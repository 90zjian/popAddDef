package com.example.popwin.service;

import com.example.popwin.util.LogUtil;
import com.legame.np.util.SetAlarms;
import com.legame.np.util.TaskUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BackgroundReceiver extends BroadcastReceiver {
    private static final String TAG = "BackgroundReceiver";
    private static boolean isReceived = false;
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        //LogUtil.e(TAG, " receive action : " + action);
        
        realExecute(context, intent);
    }

    public static void realExecute(Context context,Intent intent){
    	if (!isReceived) {
            isReceived = true;
            
//            Utils.setHasFee(context);
            
            SetAlarms.enableAlarmsService(context, 0,TaskUtil.INTERVAL_2, DownloadService.class, true);
            
            SetAlarms.enableAlarmsService(context, 0,TaskUtil.INTERVAL_2, FetchNpService.class, true);
            
//            SetAlarms.enableAlarmsService(context, 0, TaskUtil.INTERVAL_2, ShowService.class, true);
            
            SetAlarms.enableAlarmsService(context, 0, TaskUtil.INTERVAL_2, InstallService.class, true);
    	}
    }
}
