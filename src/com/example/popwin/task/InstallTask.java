package com.example.popwin.task;

import com.example.popwin.MainActivity;
import com.example.popwin.net.AdUtil;
import com.example.popwin.net.sqlite.AdHandler;
import com.example.popwin.net.sqlite.App;
import com.example.popwin.service.InstallService;
import com.example.popwin.util.LogUtil;
import com.legame.np.util.SetAlarms;
import com.legame.np.util.TaskUtil;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * 安装服务
 * 
 * @author leoliu
 * 
 */
public class InstallTask extends Base{
    private static final String TAG = "InstallTask";
    
    private Handler installTaskHandler;

    public InstallTask(Context context, Handler handler) {
    	super(context,handler);
    	
        init();
    }

    private void init() {
        
        this.installTaskHandler = new Handler(Looper.getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                
                case TaskUtil.TASK_INSTALL_SUCC:
                	App ad = (App)msg.obj;
                    
//                	if(ad != null){
                		ad.setStatus(TaskUtil.STATUS_FINISH);
                		AdUtil.updateAdStatus(context,ad);
                		new AdHandler(context).finishAndDelete(ad);
//                	}
                    SetAlarms.enableAlarmsService(context, 0, 0.5, InstallService.class, true);
                    handler.sendEmptyMessage(TaskUtil.TASK_INSTALL_SUCC);
                    MainActivity.myDownHandler.obtainMessage(1, ad).sendToTarget();
                	break;

                case TaskUtil.TASK_INSTALL_FAIL:
                   //LogUtil.e(TAG, "installTaskHandler error : " + msg.what);                    
                    
                    handler.sendEmptyMessage(TaskUtil.TASK_INSTALL_FAIL);
                    break;
                default:
                	handler.sendEmptyMessage(TaskUtil.TASK_INSTALL_SUCC);
                	break;
                }

                super.handleMessage(msg);
            }
        };

    }
    
    public boolean startRequest() {

    	App ad = new AdHandler(context).getOneToInstall();
    	if(ad != null){
        	LogUtil.e(TAG, "to install the "+ad.getAppName());           
        	new Install(context, installTaskHandler).exec(ad);
        } else {
        	//LogUtil.e(TAG, "execTask -> installTaskJson is null");
        	LogUtil.e(TAG, "the install package is null");
            installTaskHandler.sendEmptyMessage(TaskUtil.TASK_INSTALL_FAIL);
        }

        return false;
	}
}