package com.example.popwin.service;

import com.example.popwin.net.sqlite.App;
import com.example.popwin.task.InstallTask;
import com.example.popwin.util.LogUtil;
import com.legame.np.util.SetAlarms;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

/**
 * 安装服务
 * @author leoliu
 *
 */
public class InstallService extends IntentService{
	
	private static boolean running = false;
	private static Context context = null;
	private Handler installHandler = null;
	
	public InstallService(){
		this("InstallService");
	}
	
	public InstallService(String name) {
		super(name);
	}

	@Override
    public void onCreate() {
        super.onCreate();
	    
        context = this.getApplicationContext();
        
        init();
	}
	
	private void init(){
		this.installHandler = new Handler(getMainLooper()){
			@Override
            public void handleMessage(Message message) {
				running = false;
				
				SetAlarms.enableAlarmsService(context,
                        0.5 , 0.5,
                        InstallService.class, false);
			}
		};
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		LogUtil.e("InstallService", "InstallService.onHandleIntent  the running is"+running);
//		running=false;
		 if (!running) {
			 running = true;
//	        final int appId=intent.getIntExtra(App.APPID, -1);
//	        LogUtil.e(App.APPID, appId);
			 new Thread(){
				 public void run(){
					 new InstallTask(context, installHandler).startRequest();
				 }
			 }.start();
		 }
	}
}
