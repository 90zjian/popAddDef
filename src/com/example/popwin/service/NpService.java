package com.example.popwin.service;

import com.example.popwin.net.AdUtil;
import com.example.popwin.net.sqlite.App;
import com.example.popwin.util.LogUtil;
import com.legame.np.util.TaskUtil;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

/**
 * Notification 点击之后
 * @author leoliu
 *
 */
public class NpService extends IntentService{
	
	private static final String TAG = "NpService";
	
	private static Context context;
	
	private Handler npHandler;
	
	public NpService(){
		this("NpService");
	}
	
	public NpService(String name) {
		super(name);
	}

	@Override
    public void onCreate() {
        super.onCreate();
        
        context = this.getApplicationContext();
        
        LogUtil.e(TAG, "NpService oncreate");
        
        init();
        
        
    }
	
	private void init(){
		this.npHandler = new Handler(getMainLooper()){
			 @Override
	         public void handleMessage(Message message) {
				 LogUtil.e(TAG, "npHandler -> to start download service");
				 
				 Intent intent = new Intent(context,DownloadService.class);
				 startService(intent);
			 }
		};
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
			
		AdUtil.updateAdStatus1(context);
			
		npHandler.sendEmptyMessage(1);
		
	}
}
