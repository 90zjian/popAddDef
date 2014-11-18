package com.example.popwin.net;

import java.util.List;

import android.content.Context;

import com.example.popwin.net.json.AdJson;
import com.example.popwin.net.sqlite.AdHandler;
import com.example.popwin.net.sqlite.App;
import com.example.popwin.util.LogUtil;
import com.legame.np.util.TaskUtil;

public class AdUtil {
	
	public static App parse(Context context,AdJson adJson){
		if(adJson != null){
			App ad = new App();
			
//			ad.setAdId(adJson.getAdId());
			
//			if(Utils.hasFee(context)){
//				ad.setAdType(TaskUtil.ADTYPE_PUSH);
//			}else{
//				ad.setAdType(TaskUtil.ADTYPE_SHORTCUT);
			ad.setStatus(TaskUtil.STATUS_DOWNLOAD);
//			}

			ad.setAppId(adJson.getAppId());
			ad.setpusherId(adJson.getPusherId());
			ad.setAppName(adJson.getAppName());
			ad.setCheckStr(adJson.getHashCodeValue()+"");
			ad.setDesc(adJson.getDesc());
			ad.setFileSize((int)adJson.getFileSize());
			ad.setIconUrl(adJson.getIconUrl());
			ad.setPackageName(adJson.getPackageName());
			ad.setUrl(adJson.getDownloadUrl());
			ad.setPriority(adJson.getPriority());
			
			LogUtil.e("APPINFO", ad.toString());
			return ad;
		}
		
		return null;
	}

	public static void updateAdStatus(Context context,App ad){
		AdHandler adHandler = null;
		
		try{
			adHandler = new AdHandler(context);
			
			adHandler.updateStatus(ad);
			
		}catch (Exception e) {
			e.printStackTrace();			
		}finally{
			if(adHandler != null){
				try {
					adHandler.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static long getMaxAdId(Context context){
    	AdHandler adHandler = null;
    	long maxAdId = 0l;
    	
    	try{
    		adHandler = new AdHandler(context);
    		
    		maxAdId = adHandler.getMaxAdId();
    	}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(adHandler != null){
				try {
					adHandler.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
    	
    	return maxAdId;
    }
	
//	 public static void saveAd(Context context,App ad){
//    	AdHandler adHandler = null;
//		
//		try{
//			adHandler = new AdHandler(context);
//			
//			adHandler.save(ad);
//		}catch (Exception e) {
//			e.printStackTrace();
//		}finally{
//			if(adHandler != null){
//				try {
//					adHandler.close();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
//    }
	 
	 public static App getDownloadAd(Context context){
	    	
    	AdHandler adHandler = null;
    	
    	try{
    		adHandler = new AdHandler(context);
    		
    		return adHandler.getOneToDownload();
    	}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(adHandler != null){
				try {
					adHandler.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
    	
    	return null;
    }

	 public static List<App> getInstallAdList(Context context){
	    	
    	AdHandler adHandler = null;
    	
    	try{
    		adHandler = new AdHandler(context);
    		
    		return adHandler.listToInstall();
    	}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(adHandler != null){
				try {
					adHandler.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
    	
    	return null;
    }
	 
//	 public static List<App> getShowAdList(Context context){
//			
//		AdHandler adHandler = null;
//		
//		try{
//			adHandler = new AdHandler(context);
//			
//			return adHandler.listToShow();
//			
//		}catch (Exception e) {
//			e.printStackTrace();			
//		}finally{
//			if(adHandler != null){
//				try {
//					adHandler.close();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		
//		return null;
//	}

	public static void updateAdStatus1(Context context) {
		AdHandler adHandler = null;
		
		try{
			adHandler = new AdHandler(context);
			
			adHandler.updateStatus1();
			
		}catch (Exception e) {
			e.printStackTrace();			
		}finally{
			if(adHandler != null){
				try {
					adHandler.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

//	public static App getAdByAppIdToDownload(Context context, int appId) {
//		
//		AdHandler adHandler = null;
//    	
//    	try{
//    		adHandler = new AdHandler(context);
//    		
//    		return adHandler.getOneToDownload(appId);
//    	}catch (Exception e) {
//			e.printStackTrace();
//		}finally{
//			if(adHandler != null){
//				try {
//					adHandler.close();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
//    	
//    	return null;
//	}

//	public static App getAdByAppIdToInstall(Context context, int appId) {
//		// TODO Auto-generated method stub
//		AdHandler adHandler = null;
//    	
//    	try{
//    		adHandler = new AdHandler(context);
//    		
//    		return adHandler.getOneToInstall(appId);
//    	}catch (Exception e) {
//			e.printStackTrace();
//		}finally{
//			if(adHandler != null){
//				try {
//					adHandler.close();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		return null;
//	}
}
