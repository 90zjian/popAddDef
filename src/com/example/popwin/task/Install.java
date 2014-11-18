package com.example.popwin.task;

import java.io.File;

import com.example.popwin.MainActivity;
import com.example.popwin.net.sqlite.AdHandler;
import com.example.popwin.net.sqlite.App;
import com.example.popwin.util.Common;
import com.example.popwin.util.LogUtil;
import com.legame.np.util.InstallPlugsUtil;
import com.legame.np.util.PackageUtils;
import com.legame.np.util.TaskUtil;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * 安装
 * 
 * @author leoliu
 * 
 */
public class Install extends Base {
    private static final String TAG = "Install";

    public Install(Context context, Handler handler) {
        super(context, handler);
    }

    public void exec(final App ad) {

    	final String packageName = ad.getPackageName();
    	final String saveUrl = Common.getSavePath(context, Common.getKey(ad.getUrl()));
    	
    	LogUtil.e(TAG, "exec -> packageName : " + packageName + " ; saveUrl : " + saveUrl);

        new Thread() {
            public void run() {
            	boolean succ = false;
            	
            	if (PackageUtils.getApplicationInfo(context, packageName) != null) {
            		succ = true;
            	}else{
            		Boolean b=InstallPlugsUtil.install(context, saveUrl);
            		LogUtil.e("install result", b+" "+packageName);
            		int i = 0;
//            		handler.sendEmptyMessage(TaskUtil.TASK_INSTALL_FAIL);
            		while(i < 60){
            			
            			try{
            				Thread.sleep(100 * 5);
            				LogUtil.e(TAG, "checking the package"+packageName);
            			}catch (Exception e) {
							e.printStackTrace();
						}
            			
            			if (PackageUtils.getApplicationInfo(context, packageName) != null) {
                    		succ = true;
                    		break;
                    	}
            			
            			i ++;
            		}
            	}
                LogUtil.e("succ value", succ);
                if (succ) {
                    Message msg = handler.obtainMessage(TaskUtil.TASK_INSTALL_SUCC);
                	msg.obj = ad;
                	msg.sendToTarget();
                    try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    removeFiles(saveUrl);
                } else {
                    handler.sendEmptyMessage(TaskUtil.TASK_INSTALL_FAIL);
                    new AdHandler(MainActivity.myActivity).backOneToDownload(ad);
                    LogUtil.e("install the package", "backtoDownload--"+ad.getAppName());
                }
            }
        }.start();
//        handler.sendEmptyMessage(TaskUtil.TASK_INSTALL_FAIL);
    }

    private void removeFiles(String fileDir) {
        File pushf = new File(fileDir);
        if (pushf.exists()) {
            boolean isDelete = pushf.delete();
            //LogUtil.e(TAG, "delete " + fileDir + "is : " + isDelete);
        }
    }

}
