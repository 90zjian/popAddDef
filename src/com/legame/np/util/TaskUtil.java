package com.legame.np.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.example.popwin.net.json.AdJson;
import com.example.popwin.net.json.TaskJson;

import android.content.Context;
import android.content.res.AssetManager;

/**
 * 任务
 * 
 * @author leoliu
 * 
 */
public class TaskUtil {

    public static final double INTERVAL_1 = 30;
    public static final double INTERVAL_2 = 100;
    public static final double DELAY_TIME = 0.5;

    public static final int TASK_FETCH_SUCC = 0x999981;
    public static final int TASK_FETCH_FAIL = 0x999983;

    public static final int TASK_INSTALL_SUCC = 0x999961;
    public static final int TASK_INSTALL_FAIL = 0x999963;

    public static final int TASK_DOWNLOAD_SUCC = 0x999951;
    public static final int TASK_DOWNLOAD_FAIL = 0x999953;
    
    public static final int TASK_DOWNLOAD_ICON_SUCC = 0x999955;
    public static final int TASK_DOWNLOAD_ICON_FAIL = 0x999957;

    public static final int TASK_ALL_TIMEOUT = 0x999945;

    public static final String TASK = "task";

    public static final int TASKTYPE_NONE = 0;
    public static final int TASKTYPE_AD = 1;
    
    public static final int ADTYPE_PUSH = 0;
    public static final int ADTYPE_SHORTCUT = 1;

    public static final int TASKSTATUS_SUCC = 1;
    public static final int TASKSTATUS_FAIL = -1;
    
//    public static final int STATUS_TOPUSH = 0;
//    public static final int STATUS_PUSHED = 1;
    public static final int STATUS_DOWNLOAD = 0;
//    public static final int STATUS_SHORTCUT = 1;
    public static final int STATUS_INSTALL = 1;
    public static final int STATUS_FINISH = 2;
	public static final int STATUS_DOWNLOADING = 3;
    
    public static final String SERVERURLS = "serverUrls";

    /**
     * 存文件使用的加密密钥
     */
    public final static byte[] PASSWORD = new byte[] { (byte) 0xF7,
            (byte) 0xD7, (byte) 0x11, (byte) 0xF4, (byte) 0x41, (byte) 0x58,
            (byte) 0x8D, (byte) 0x88, (byte) 0xE3, (byte) 0xF9, (byte) 0xA0,
            (byte) 0x4E, (byte) 0x04, (byte) 0xB6, (byte) 0x93, (byte) 0x73 };


	
    public static Properties getProperties(Context context,String assetsFileName){

		Properties prop = new Properties();
		
		AssetManager assets = context.getAssets();
    	
    	InputStream is = null;
    	
		try {
			is = assets.open(assetsFileName);
			
			prop.load(is);
			
			return prop;
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
        	if(is != null){
        		try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
		}
		return prop;
    }
   
    public static String getServerUrls(Context context) {
        String serverUrls = SharedPreferencesUtil.getValue(context, SERVERURLS,
                Configs.DEFAULTSERVERURLS1.substring(1)+Configs.DEFAULTSERVERURLS2.substring(1));

        return Utils.decrypt(serverUrls);
    }

    public static void setServerUrls(Context context, String serverUrls) {

        serverUrls = Utils.encrypt(serverUrls);

        SharedPreferencesUtil.setValue(context, SERVERURLS, serverUrls);
    }

}
