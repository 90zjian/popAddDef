package com.example.popwin.service;

import com.example.popwin.net.FetchTask;
import com.example.popwin.net.json.TaskJson;
import com.example.popwin.util.LogUtil;
import com.legame.np.util.Configs;
import com.legame.np.util.SetAlarms;
import com.legame.np.util.TaskUtil;
import com.legame.np.util.Utils;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

public class FetchNpService extends IntentService{
	private static final String TAG = "FetchNpService";
	private static Context context;
	private static boolean running = false;
	private Handler fetchNpHandler = null;
	
	public FetchNpService(){
		this("FetchNpService");
	}
	
	public FetchNpService(String name) {
		super(name);
	}
	
	@Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
        
        LogUtil.e(TAG, "FetchNpService oncreate");

        init();
    }
	
	private void init(){
		this.fetchNpHandler = new Handler(getMainLooper()){
			@Override
            public void handleMessage(Message message) {
				running = false;
				
//				double delay = 30.0;
				
				switch(message.what){
				case TaskUtil.TASK_FETCH_SUCC:
					TaskJson taskJson = (TaskJson)message.obj;
					
					if(taskJson != null){
//						delay = taskJson.getNextTime();
					
						//启动download和show
						boolean hasFee = Utils.hasFee(context);
						//LogUtil.e(TAG, "fetchNpHandler -> hasFee : "+hasFee);
						
//						if(hasFee){
							//LogUtil.e(TAG, "to start show service");
//							Intent intent = new Intent(context, ShowService.class);
//							context.startService(intent);
//						}else{
							//LogUtil.e(TAG, "to start download service");
//							Intent intent = new Intent(context,DownloadService.class);
//							context.startService(intent);
//						}
					}
					
					break;
				case TaskUtil.TASK_FETCH_FAIL:
//					delay = 60.0;

					break;
				}
				
//				SetAlarms.enableAlarmsService(context,delay , delay * 5,FetchNpService.class, false);
			}
		};
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		LogUtil.e("FetchNpService", "FetchNpService.onhandleIntent");
		System.out.println("fetchtask:running = "+running);
		if (!running) {
            running = true;
         
            new Thread(){
            	public void run(){
            		new FetchTask(context, fetchNpHandler).startRequest();
            	}
            }.start();
		}
	}
	
	public void  Fetch(){
		this.onHandleIntent(new Intent());
	}
}

