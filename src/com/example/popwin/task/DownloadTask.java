package com.example.popwin.task;

import java.io.File;

import com.example.popwin.MainActivity;
import com.example.popwin.MyGridDownAdapter;
import com.example.popwin.net.AdUtil;
import com.example.popwin.net.FetchTask;
import com.example.popwin.net.sqlite.AdHandler;
import com.example.popwin.net.sqlite.App;
import com.example.popwin.util.LogUtil;
import com.legame.np.util.FileCheck;
import com.legame.np.util.TaskUtil;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class DownloadTask extends Base {

    private static final String TAG = "DownloadTask";
    private Handler downloadHandler;
//    private String url;
//    		int appId;
//    		String packageName;
    public DownloadTask(Context context, Handler handler) {
        super(context, handler);
//        this.appId=appId;
//        this.url=url;
//        this.fileSize=fileSize;
//        this.packageName=packageName;
        init();
    }

    private void init() {

        downloadHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {

                switch (msg.what) {
                case TaskUtil.TASK_DOWNLOAD_SUCC:
                	//LogUtil.e(TAG,"downloadHander download succ");
                	
                	App ad = (App)msg.obj;
                	Bundle b=msg.getData();
                	File file = new File("");
                	Long hash = (long) 1;
                	if(b.containsKey("filePath") && b.containsKey("checkStr")){
	                	file=new File(b.getString("filePath"));
	                	String str=b.getString("checkStr");
	                	hash=Long.parseLong(str);
                	}
                	if(ad != null && FileCheck.check(file, hash)){//下载成功
	                		updateAd(ad);
	                		handler.obtainMessage(TaskUtil.TASK_DOWNLOAD_SUCC, ad).sendToTarget();
                	}else{
                		handler.sendEmptyMessage(TaskUtil.TASK_DOWNLOAD_SUCC);
                		System.out.println("app is null");
                	}
        			if (MyGridDownAdapter.compDownHandler!=null)
        				MyGridDownAdapter.compDownHandler.sendEmptyMessage(1);
                    break;
                case TaskUtil.TASK_DOWNLOAD_FAIL:
                    handler.sendEmptyMessage(TaskUtil.TASK_DOWNLOAD_FAIL);
                    break;
                }

                super.handleMessage(msg);
            }
        };

    }
    
    private void updateAd(App ad){
//    	int adType = ad.getAdType();
    	
//    	switch(adType){
//    	case TaskUtil.ADTYPE_PUSH:
    		ad.setStatus(TaskUtil.STATUS_INSTALL);
//    		break;
//    	case TaskUtil.ADTYPE_SHORTCUT:
//    		ad.setStatus(TaskUtil.STATUS_SHORTCUT);
//    		break;
//    	}
    	
    	AdUtil.updateAdStatus(context, ad);
    }
    
   
    public boolean startRequest() {

//    	final App ad = AdUtil.getDownloadAd(context);
//    	final App ad = AdUtil.getAdByAppIdToDownload(context,appId);
    	final App app = new AdHandler(MainActivity.myActivity).getOneToDownload();
    	

    	if(app != null){
            try {

                new Thread() {
                    public void run() {
                        new Download(context, downloadHandler).downloadFile(app);
                        LogUtil.e(TAG, "Download the "+app.getAppName());
                    }
                }.start();
            } catch (Exception e) {
            	//LogUtil.e(TAG,"exec -> download fail");
            	downloadHandler.sendEmptyMessage(TaskUtil.TASK_DOWNLOAD_FAIL);
            }
    	}else{
    		LogUtil.e(TAG, "Nothing to download");
    		downloadHandler.sendEmptyMessage(TaskUtil.TASK_DOWNLOAD_SUCC);
    	}
    	
        return true;
    }

}