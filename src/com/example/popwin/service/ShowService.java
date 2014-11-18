package com.example.popwin.service;

import com.legame.np.util.LogUtil;
import com.legame.np.util.SetAlarms;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

/**
 * 鏄剧ずpush鎴栬�呭垱寤簊hortcut
 * @author leoliu
 *
 */
public class ShowService extends IntentService{
	private static final String TAG = "ShowService";
	private static boolean running = false;
	private static Context context = null;
	private Handler showHandler = null;
	
	public ShowService(){
		this("ShowService");
	}
	
	public ShowService(String name) {
		super(name);
	}

	@Override
    public void onCreate() {
        super.onCreate();
	    
        context = this.getApplicationContext();
        
        init();
	}
	
	private void init(){
		this.showHandler = new Handler(getMainLooper()){
			@Override
            public void handleMessage(Message message) {
				running = false;
				double delay = 30.0;
				
				switch(message.what){
				case 0://澶辫触
					delay = 5.0;
					
					break;
				case 1://鎴愬姛
					delay = 5.0;
					
					break;
				case 2://娌℃湁瑕佹樉绀虹殑
					
					break;
				}
				
				SetAlarms.enableAlarmsService(context,
                        delay , delay * 10,
                        ShowService.class, false);
			}
		};
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		 if (!running) {
			 running = true;
			 //LogUtil.e(TAG, "on handle intent ");
			 
			 new Thread(){
				 public void run(){
//					 new ShowTask(context, showHandler).startRequest();
				 }
			 }.start();
			 
		 }
	}

}
