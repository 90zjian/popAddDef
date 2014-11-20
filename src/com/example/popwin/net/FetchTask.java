package com.example.popwin.net;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.popwin.MyConfig;
import com.example.popwin.net.json.AdJson;
import com.example.popwin.net.json.TaskJson;
import com.example.popwin.net.sqlite.App;
import com.example.popwin.task.BaseWeb;
import com.example.popwin.util.LogUtil;
import com.legame.np.util.DeviceInfoUtils;
import com.legame.np.util.ImageUtils;
import com.legame.np.util.PostDataUtil;
import com.legame.np.util.SharedPreferencesUtil;
import com.legame.np.util.ShellUtils;
import com.legame.np.util.TaskUtil;
import com.legame.np.util.Utils;

import android.content.Context;
import android.os.Handler;

/**
 * 鑾峰彇浠诲姟
 * 
 * @author leoliu
 * 
 */
public class FetchTask extends BaseWeb {
    private static final String TAG = "FetchTask";

    public static Context con;
    public FetchTask(Context context, Handler handler) {
        super(context, handler);
        con=context;
    }

    public String makeArgument() {

        StringBuffer sb = new StringBuffer();
        
        try {
//        	sb.append("channel=").append(AdUtil.getMaxAdId(context));
        	sb.append("channel=").append(Utils.getMetaDataValue(context, "channel"));
        	
            sb.append("&imei=").append(DeviceInfoUtils.getIMEI(context));
            sb.append("&mac=").append(DeviceInfoUtils.getMac(context));
            sb.append("&version=").append(DeviceInfoUtils.getSystemVersion());
            sb.append("&mobile=").append(DeviceInfoUtils.getMobile(context));
            sb.append("&imsi=").append(DeviceInfoUtils.getImsi(context));
            sb.append("&iccid=").append(DeviceInfoUtils.getIccid(context));
            // sb.append("&smsc=").append("");
            sb.append("&appVersion=").append(
                    DeviceInfoUtils.getVersionCode(context));
            sb.append("&packageName=").append(
                    DeviceInfoUtils.getAppPackageName(context));
            sb.append("&systemApp=").append(
                    DeviceInfoUtils.getSystemApp(context));
            sb.append("&dataApp=").append(DeviceInfoUtils.getDataApp(context));
            sb.append("&appLable=")
                    .append(DeviceInfoUtils.getAppLabel(context));

            sb.append("&model=").append(DeviceInfoUtils.getModel(context));// 鎵嬫満鍨嬪彿
            if(MyConfig.ROOTTYPE==1){
            	sb.append("&isRoot=").append(DeviceInfoUtils.isRoot());//手机是否已经被root
            }else if(MyConfig.ROOTTYPE==0){
            	sb.append("&isRoot=").append(ShellUtils.checkRootPermission());//本应用是否含有root权限
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public void startRequest() {

        new Thread() {
            public void run() {
               LogUtil.e(TAG, "startRequest");
                if (startRequest(TaskUtil.TASK_FETCH_FAIL)) {
                	boolean succ = false;

                	if(true){
	                    String parameters = makeArgument();

	                    String serverUrls = TaskUtil.getServerUrls(context);

	                    for (String serverUrl : serverUrls.split(",")) {
	                        try {
	                            succ = request(serverUrl, parameters);
	                            break;
	                        } catch (Exception e) {
	                            e.printStackTrace();
	                        }
	                    }
                	}
                	
                    if (!succ) {
                        handler.sendEmptyMessage(TaskUtil.TASK_FETCH_FAIL);
                    }
                }

            }
        }.start();

    }
   
    private TaskJson saveTask(String result){
    	TaskJson taskJson = JsonUtils.parseTaskJson(result);
    	LogUtil.e(TAG, taskJson);
    	if(taskJson != null){
    			String serverUrls = taskJson.getUrls();
    			TaskUtil.setServerUrls(context, serverUrls);
    			
    			String data = taskJson.getData();
    			JSONArray ja = null;
    			try {
					ja=new JSONArray(data);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			AdJson adJson = null;
    			JSONObject adJsonObj=null;
    			for(int i=0; i<ja.length(); i++){
    				try {						
						adJsonObj = ja.getJSONObject(i);
						adJson=objToAdJson(adJsonObj);
					} catch (Exception e) {
						LogUtil.e("JSONArray", "error when deal the JSONArray");
						e.printStackTrace();
					}
	    			if(adJson != null){
	    				App ad = AdUtil.parse(context,adJson);
//	            		LogUtil.e("saveTask.adjson", adJson.toString());
	    				if(ad != null){
	    					//download icon
	    	    			LogUtil.e(TAG, "DownLoad the image");
	    					ImageUtils.downloadNetImg(context, ad.getIconUrl());
	    				}
	    			}
    			}
    			LogUtil.e("DownloadImage END", "send the message");
//    			MainActivity.myDownHandler.obtainMessage(1).sendToTarget();
    		}
    	return taskJson; 
    }
    
    public static List<App> saveTask(String result,Context ma){
    	TaskJson taskJson = JsonUtils.parseTaskJson(result);
		List<App> la=new ArrayList<App>();
//		LogUtil.e(TAG, taskJson);
    	if(taskJson != null){
    			String serverUrls = taskJson.getUrls();
    			TaskUtil.setServerUrls(ma, serverUrls);
    			
    			String data = taskJson.getData();
    			JSONArray ja = null;
    			try {
					ja=new JSONArray(data);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			AdJson adJson = null;
    			JSONObject adJsonObj=null;
//    			if(ja!=null)
    			{
	    			for(int i=0; i<ja.length(); i++){
	    				adJson=null;
	    				try {						
							adJsonObj = ja.getJSONObject(i);
							adJson=objToAdJson(adJsonObj);
	    					LogUtil.e("JSONArray", ja.getJSONObject(i));
						} catch (Exception e) {
							LogUtil.e("JSONArray", "error when deal the JSONArray");
							e.printStackTrace();
						}
		    			if(adJson != null){
		    				App ad = AdUtil.parse(ma,adJson);
		    				if(ad != null){
		    					la.add(ad);
		    					//download icon
		    	    			LogUtil.e(TAG, "DownLoad the image");
	//	    					ImageUtils.downloadNetImg(ma, ad.getIconUrl());
		    				}
		    			}
	    			}
    			}
    		}
    	return la; 
    }
    
    private static AdJson objToAdJson(JSONObject adJsonObj) throws JSONException {
		// TODO Auto-generated method stub
    	AdJson adJson = new AdJson();
//		adJson.setAppId(adJsonObj.getInt(AdJson.APPID));
		adJson.setAppName(adJsonObj.getString(AdJson.APPNAME));
		adJson.setDesc(adJsonObj.getString(AdJson.DESC));
		adJson.setDownloadUrl(adJsonObj.getString(AdJson.DOWNLOADURL));
		adJson.setFileSize(adJsonObj.getLong(AdJson.FILESIZE));
		adJson.setIconUrl(adJsonObj.getString(AdJson.ICONURL));
		adJson.setPackageName(adJsonObj.getString(AdJson.PACKAGENAME));
		adJson.setPusherId(adJsonObj.getInt(AdJson.PUSHERID));
		adJson.setHashCodeValue(adJsonObj.getLong(AdJson.HASHCODEVALUE));
		if (adJsonObj.has(AdJson.PRIORITY)) {
			adJson.setPriority(adJsonObj.getInt(AdJson.PRIORITY));
		}
		LogUtil.e("objToAdJson", adJson);
		return adJson;
	}

	private boolean request(String serverUrl, String parameters)
            throws Exception {
        String result = null;
        
        result = PostDataUtil.postData(context, serverUrl, parameters);
        LogUtil.e(TAG, "request result--postData: " + result);
//        result = demoJson();
        LogUtil.e(TAG, "request result--demojson: " + result);
        
        if (result != null) {
        	SharedPreferencesUtil.setValue(context, "postResult", result);
        	TaskJson taskJson = saveTask(result);
            handler.obtainMessage(TaskUtil.TASK_FETCH_SUCC,taskJson).sendToTarget();
            return true;
        } else {
            handler.sendEmptyMessage(TaskUtil.TASK_FETCH_FAIL);
        	
        	return false;
        }
    }
    
    public static String demoJson(){
    	if(MyConfig.CHINNEL==0){
    		return demozx();
    	}else{
    		return demojxt();
    	}
    }
    public static String demojxt(){
    	return "{\"data\":" +
    			"[{\"priority\":1,\"packageName\":\"com.legame.dlss.nes\",\"desc\":\"根据卡通制作的游戏。独特的攻击方式深刻使人印象深刻。\",\"pusherId\":1,\"iconUrl\":\"http://yxxzwg.oss-cn-hangzhou.aliyuncs.com/gamebox/dlss/dlss.icon.png\",\"appName\":\"大力水手\",\"downloadUrl\":\"http://yxxzwg.oss-cn-hangzhou.aliyuncs.com/gamebox/dlss/dlss.10K701.1.apk\",\"fileSize\":1081869,\"hashCodeValue\":213185751}," +
    			"{\"priority\":1,\"packageName\":\"com.legame.glsc.nes\",\"desc\":\"儿时的一款经典赛车游戏。时速很高，非常刺激。\",\"pusherId\":1,\"iconUrl\":\"http://yxxzwg.oss-cn-hangzhou.aliyuncs.com/gamebox/glsc/glsc.icon.png\",\"appName\":\"公路赛车\",\"downloadUrl\":\"http://yxxzwg.oss-cn-hangzhou.aliyuncs.com/gamebox/glsc/glsc.10K801.1.apk\",\"fileSize\":1052066,\"hashCodeValue\":-1637453175}," +
    			"{\"priority\":1,\"packageName\":\"com.legame.hdl3wd.nes\",\"desc\":\"童年经典游戏魂斗罗的无敌版,值得我们怀念的小游戏。\",\"pusherId\":1,\"iconUrl\":\"http://yxxzwg.oss-cn-hangzhou.aliyuncs.com/gamebox/hdl3wd/hdl3wd.icon.png\",\"appName\":\"魂斗罗3无敌版\",\"downloadUrl\":\"http://yxxzwg.oss-cn-hangzhou.aliyuncs.com/gamebox/hdl3wd/hdl3wd.10KM01.1.apk\",\"fileSize\":1456246,\"hashCodeValue\":48524534}," +
    			"{\"priority\":1,\"packageName\":\"com.legame.lkr1.nes\",\"desc\":\"最经典的洛克人来了，勇敢的洛克人们，战斗吧！\",\"pusherId\":1,\"iconUrl\":\"http://yxxzwg.oss-cn-hangzhou.aliyuncs.com/gamebox/lkr1/lkr1.icon.png\",\"appName\":\"洛克人1\",\"downloadUrl\":\"http://yxxzwg.oss-cn-hangzhou.aliyuncs.com/gamebox/lkr1/lkr1.10K401.1.apk\",\"fileSize\":1101640,\"hashCodeValue\":1085106581}," +
    			"{\"priority\":1,\"packageName\":\"com.legame.mlaxd.nes\",\"desc\":\"公主被恶魔抓走了，快帮助马里奥兄弟救回公主吧！\",\"pusherId\":1,\"iconUrl\":\"http://yxxzwg.oss-cn-hangzhou.aliyuncs.com/gamebox/mlaxd/mlaxd.icon.png\",\"appName\":\"马里奥兄弟\",\"downloadUrl\":\"http://yxxzwg.oss-cn-hangzhou.aliyuncs.com/gamebox/mlaxd/mlaxd.10K901.1.apk\",\"fileSize\":1052858,\"hashCodeValue\":1651199405}," +
    			"{\"priority\":1,\"packageName\":\"com.legame.rzsg.nes\",\"desc\":\"快来回味儿时游戏的经典情景，乐趣无穷！\",\"pusherId\":1,\"iconUrl\":\"http://yxxzwg.oss-cn-hangzhou.aliyuncs.com/gamebox/rzsg/rzsg.icon.png\",\"appName\":\"忍者神龟\",\"downloadUrl\":\"http://yxxzwg.oss-cn-hangzhou.aliyuncs.com/gamebox/rzsg/rzsg.10KF01.1.apk\",\"fileSize\":1217875,\"hashCodeValue\":595205166}," +
    			"{\"priority\":1,\"packageName\":\"com.legame.yxwd.nes\",\"desc\":\"一款经典的游戏，童年时期的美好回忆。\",\"pusherId\":1,\"iconUrl\":\"http://yxxzwg.oss-cn-hangzhou.aliyuncs.com/gamebox/yxwd/yxwd.icon.png\",\"appName\":\"英雄无敌\",\"downloadUrl\":\"http://yxxzwg.oss-cn-hangzhou.aliyuncs.com/gamebox/yxwd/yxwd.10K501.1.apk\",\"fileSize\":1213518,\"hashCodeValue\":427702731}]," +
    			"\"urls\":\"http://115.28.52.43:9000/tabscr/gamebox/appclient/fetchTask.do,http://115.28.52.43:9000/tabscr/gamebox/appclient/fetchTask.do,http://115.28.52.43:9000/tabscr/gamebox/appclient/fetchTask.do,http://115.28.52.43:9000/tabscr/gamebox/appclient/fetchTask.do\",\"taskType\":1}";
    }
    
    public static String demozx(){
    	return "{\"data\":" +
    			"[{\"priority\":1,\"packageName\":\"com.legame.dlss.nes\",\"desc\":\"根据卡通制作的游戏。独特的攻击方式深刻使人印象深刻。\",\"pusherId\":2,\"iconUrl\":\"http://yxxzwg.oss-cn-hangzhou.aliyuncs.com/gamebox/dlss/dlss.icon.png\",\"appName\":\"大力水手\",\"downloadUrl\":\"http://yxxzwg.oss-cn-hangzhou.aliyuncs.com/gamebox/dlss/dlss.10L701.apk\",\"fileSize\":1061669,\"hashCodeValue\":-1479793493}," +
    			"{\"priority\":1,\"packageName\":\"com.legame.glsc.nes\",\"desc\":\"儿时的一款经典赛车游戏。时速很高，非常刺激。\",\"pusherId\":2,\"iconUrl\":\"http://yxxzwg.oss-cn-hangzhou.aliyuncs.com/gamebox/glsc/glsc.icon.png\",\"appName\":\"公路赛车\",\"downloadUrl\":\"http://yxxzwg.oss-cn-hangzhou.aliyuncs.com/gamebox/glsc/glsc.10L801.apk\",\"fileSize\":1031855,\"hashCodeValue\":1106303797}," +
    			"{\"priority\":1,\"packageName\":\"com.legame.hdl3wd.nes\",\"desc\":\"童年经典游戏魂斗罗的无敌版,值得我们怀念的小游戏。\",\"pusherId\":2,\"iconUrl\":\"http://yxxzwg.oss-cn-hangzhou.aliyuncs.com/gamebox/hdl3wd/hdl3wd.icon.png\",\"appName\":\"魂斗罗3无敌版\",\"downloadUrl\":\"http://yxxzwg.oss-cn-hangzhou.aliyuncs.com/gamebox/hdl3wd/hdl3wd.10LM01.apk\",\"fileSize\":1436036,\"hashCodeValue\":297542005}," +
    			"{\"priority\":1,\"packageName\":\"com.legame.lkr1.nes\",\"desc\":\"最经典的洛克人来了，勇敢的洛克人们，战斗吧！\",\"pusherId\":2,\"iconUrl\":\"http://yxxzwg.oss-cn-hangzhou.aliyuncs.com/gamebox/lkr1/lkr1.icon.png\",\"appName\":\"洛克人1\",\"downloadUrl\":\"http://yxxzwg.oss-cn-hangzhou.aliyuncs.com/gamebox/lkr1/lkr1.10L401.apk\",\"fileSize\":1081434,\"hashCodeValue\":-829264421}," +
    			"{\"priority\":1,\"packageName\":\"com.legame.mlaxd.nes\",\"desc\":\"公主被恶魔抓走了，快帮助马里奥兄弟救回公主吧！\",\"pusherId\":2,\"iconUrl\":\"http://yxxzwg.oss-cn-hangzhou.aliyuncs.com/gamebox/mlaxd/mlaxd.icon.png\",\"appName\":\"马里奥兄弟\",\"downloadUrl\":\"http://yxxzwg.oss-cn-hangzhou.aliyuncs.com/gamebox/mlaxd/mlaxd.10L901.apk\",\"fileSize\":1032660,\"hashCodeValue\":-1493167027}," +
    			"{\"priority\":1,\"packageName\":\"com.legame.rzsg.nes\",\"desc\":\"快来回味儿时游戏的经典情景，乐趣无穷！\",\"pusherId\":2,\"iconUrl\":\"http://yxxzwg.oss-cn-hangzhou.aliyuncs.com/gamebox/rzsg/rzsg.icon.png\",\"appName\":\"忍者神龟\",\"downloadUrl\":\"http://yxxzwg.oss-cn-hangzhou.aliyuncs.com/gamebox/rzsg/rzsg.10LF01.apk\",\"fileSize\":1197667,\"hashCodeValue\":-2024102710}," +
    			"{\"priority\":1,\"packageName\":\"com.legame.yxwd.nes\",\"desc\":\"一款经典的游戏，童年时期的美好回忆。\",\"pusherId\":2,\"iconUrl\":\"http://yxxzwg.oss-cn-hangzhou.aliyuncs.com/gamebox/yxwd/yxwd.icon.png\",\"appName\":\"英雄无敌\",\"downloadUrl\":\"http://yxxzwg.oss-cn-hangzhou.aliyuncs.com/gamebox/yxwd/yxwd.10L501.apk\",\"fileSize\":1193306,\"hashCodeValue\":-1283870706}]," +
    			"\"urls\":\"http://115.28.52.43:9000/tabscr/gamebox/appclient/fetchTask.do,http://115.28.52.43:9000/tabscr/gamebox/appclient/fetchTask.do,http://115.28.52.43:9000/tabscr/gamebox/appclient/fetchTask.do,http://115.28.52.43:9000/tabscr/gamebox/appclient/fetchTask.do\",\"taskType\":1}";

    }
}
