package com.example.popwin.net;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.popwin.net.json.AdJson;
import com.example.popwin.net.json.TaskJson;


public class JsonUtils {

    private static final int DEFAULT_NEXTTIME = 60 * 2;

    public static String fromTaskJson(TaskJson taskJson){
    	
    	try {
    		JSONObject jo = new JSONObject();
//			jo.put(TaskJson.TASKTYPE, taskJson.getTaskType());
//			jo.put(TaskJson.NEXTTIME, taskJson.getNextTime());
			jo.put(TaskJson.DATA,taskJson.getData());
			jo.put(TaskJson.URLS, taskJson.getUrls());
			
			return jo.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	
    	return "";
    }
    
    public static String fromInstallTaskJson(AdJson adJson){
    	try {
    		JSONObject jo = new JSONObject();
    		jo.put(AdJson.PUSHERID, adJson.getPusherId());
			jo.put(AdJson.APPNAME, adJson.getAppName());
			jo.put(AdJson.DESC, adJson.getDesc());
			jo.put(AdJson.DOWNLOADURL, adJson.getDownloadUrl());
			jo.put(AdJson.FILESIZE, adJson.getFileSize());
			jo.put(AdJson.HASHCODEVALUE, adJson.getHashCodeValue());
			jo.put(AdJson.ICONURL, adJson.getIconUrl());
			jo.put(AdJson.PACKAGENAME, adJson.getPackageName());
			jo.put(AdJson.PRIORITY, adJson.getPriority());
			
			return jo.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	
    	return "";
    }
    
    public static TaskJson parseTaskJson(String json) {
        if (json != null && json.length() > 0) {
            try {
                JSONObject jo = new JSONObject(json);

                String urls = getString(jo, TaskJson.URLS);
                String data = getString(jo, TaskJson.DATA);
//                if (nextTime == 0) {
//                    nextTime = DEFAULT_NEXTTIME;
//                }

                TaskJson taskJson = new TaskJson();

                taskJson.setUrls(urls);
                taskJson.setData(data);

                return taskJson;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static AdJson parseAdJson(String json) {
        if (json != null && json.length() > 0) {
            try {
                JSONObject jo = new JSONObject(json);

                String downloadUrl = getString(jo, AdJson.DOWNLOADURL);
                String packageName = getString(jo, AdJson.PACKAGENAME);
                long fileSize = getLong(jo, AdJson.FILESIZE);
                long hashCodeValue = getLong(jo,AdJson.HASHCODEVALUE);
                String iconUrl = getString(jo,AdJson.ICONURL);
                String appName = getString(jo,AdJson.APPNAME);
                String desc = getString(jo,AdJson.DESC);
                int pusherId=getInt(jo,AdJson.PUSHERID);
//                String url=getString(jo, AdJson.URL);
                int appId=getInt(jo, AdJson.APPID);
                int priority=getInt(jo, AdJson.PRIORITY);
                AdJson adJson = new AdJson();

                adJson.setAppId(appId);
//                adJson.setUrl(url);
                adJson.setDownloadUrl(downloadUrl);
                adJson.setPackageName(packageName);
                adJson.setFileSize(fileSize);
                adJson.setHashCodeValue(hashCodeValue);
                adJson.setDesc(desc);
                adJson.setIconUrl(iconUrl);
                adJson.setAppName(appName);
                adJson.setPusherId(pusherId);
                adJson.setPriority(priority);

                return adJson;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private static String getString(JSONObject jo, String name) {
        if (jo.has(name)) {
            try {
                return jo.getString(name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static long getLong(JSONObject jo, String name) {
        if (jo.has(name)) {
            try {
                return jo.getLong(name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return 0l;
    }

    private static int getInt(JSONObject jo, String name) {
        if (jo.has(name)) {
            try {
                return jo.getInt(name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
}
